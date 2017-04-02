// server.hs

// -- setup
var express    = require('express');
var app        = express();
var bodyParser = require('body-parser');
var morgan     = require('morgan');
var mongoose   = require('mongoose');

var jwt        = require('jsonwebtoken');
var config     = require('./config');
var User       = require('./app/models/user');
var crypto     = require('./app/crypto/hashing');

var controller = require('./app/controller/gamecontroller');

var port = process.env.PORT || 8081;
mongoose.connect(config.database);
app.set('superSecret', config.secret);

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());

app.use(morgan('dev'));

// -- Routes

app.get('/', function(req,res){
  res.send('The API is at http://localhost:' + port + '/api');
});

app.get('/admin', function(req,res){
  try {
    var passwordHash = crypto.hash('password');
  } catch (err) {
    console.log('error ' + err);
  }
  console.log('=> using crypto hash: ' + passwordHash);
  var nick = new User({
    userid: "nick",
    name: 'Nick Cerminara',
    email: 'nick@night.com',
    hash: passwordHash,
    admin: true
  });
  nick.save(function(err){
    if (err) throw err;
    console.log('User saved successfully');
    res.json({success:true});
  });
});

// -- API
var apiRoutes = express.Router();

apiRoutes.post('/authenticate', function(req, res) {
    console.log("Requesting user " + req.body.userid);
  User.findOne({  // Find the user
    userid: req.body.userid
  }, function(err, user) {    
    if (err) throw err;

    if (!user) {
      res.json({ success: false, message: 'Authentication failed. User not found.' });
    } else if (user) {
      // check if password matches
      if (crypto.checkPassword(req.body.password, user.hash)) {
        res.json({ success: false, message: 'Authentication failed. Wrong password.' });
      } else {
        // if user is found and password is right
        // create a token
        var token = jwt.sign(user, app.get('superSecret'), {
          expiresIn: 60*60*24 // expires in 24 hours
        });
        // return the information including token as JSON
        res.json({
          success: true,
          message: 'Enjoy your token!',
          token: token
        });
      }
    }
  });
});

// -- define middleware
// -- everything that needs an authenticated token follows below
apiRoutes.use(function(req, res, next){
  var token = req.body.token || req.query.token || req.headers['x-access-token'];
  if (token) {
    jwt.verify(token, app.get('superSecret'), function(err, decoded) {
      if (err) {
        return res.json({
          success: false,
          message: 'Failed to authenticate token.'
        });
      } else {
      req.decoded = decoded;
      next();
      }
    });
  } else {
    return res.status(403).send({
      success: false,
      message: 'No token provided.'
    });
  }
});

apiRoutes.get('/', function(req, res){
  res.json({message: 'API'});
});

apiRoutes.get('/users', function(req, res){
  User.find({}, function(err, users) {
    res.json(users);
  });
});

app.use('/api', apiRoutes);

// Start the server
//app.listen(port);
var server = require('http').createServer(app).listen(port);

// sockets

var io = require('socket.io').listen(server);

io.use(function(socket, next){
  if (socket.handshake.query && socket.handshake.query.token){
    jwt.verify(socket.handshake.query.token, app.get('superSecret'), function(err, decoded) {
      if(err) return next(new Error('Authentication error'));
      socket.decoded = decoded;
      next();
    });
  }
  next(new Error('Authentication error'));
})
.on('connection', function(socket) {
    // Create new user object when connected
    var player = {
      id: socket.id, //socketid
      name: socket.decoded._doc.userid, // userid
      level: 1 // todo, fetch from database?
    };
    var game; // current game playing
    var opponent;

    // Connect user to controller
    controller.connect(player); 
    // Send update emit back, with some info (users online, etc)
    io.sockets.emit("update", controller.getInfo());

    // User request to find a game
    socket.on('findGame', function() {
      console.log(player.name, "findgame");
      // Try to find opponent in queue
      var opponent = controller.matchmaking();
      if (opponent) { // Found opponent
        // Create game
        game = controller.createGame(player, opponent);
        console.log("game created", game.gameid);
        var data = {
          gameid: game.gameid
        }
        opponent = controller.getOpponent(game, player);
        socket.join(game.id);
        // Tell other player that game is ready
        socket.to(opponent.id).emit("gameReady", data);
      } else { // Did not find opponent
        controller.joinQueue(player); // Add player to queue
      }
      // Update server info to all clients
      io.sockets.emit("update", controller.getInfo());
    });

    socket.on('joinGame', function(gameid) {
      console.log(player.name, "joinGame", gameid);
      // update game object
      game = controller.findGame(gameid);
      // update opponent object 
      opponent = controller.getOpponent(game, player);
      socket.join(game.id);
      // send game info to both clients connected
      io.in(game.id).emit("startGame", game);
    })

    socket.on('newMove', function(json) {
      console.log(player.name, "newMove");
      data = JSON.parse(json);
      console.log(data);
      // update game object
      // controller.updateGame(data.id, data.state, data.move);
      // send new move to opponent
      socket.to(data.gameid).emit("newMove", data);
    });

    socket.on('resign', function() {
      console.log(player.name, "Resign");
      // Give up current game
      // Send info to other client (opponent)
      // End and remove game
    })









    // ROOM METHODS, merge if we want text function
    socket.on('join', function(data) {
      socket.join(data, function(){
        room = data;
        var info =  {
          roomid: room,
          users: io.sockets.adapter.rooms[data] // gives all socket.id's
        };
        socket.emit("join", info);
        io.in(room).emit("userJoined", info);
      });
    });

    socket.on('leave', function(data) {
      socket.leave(room, function() {
        console.log(room, 'room left');
        io.sockets.to(room, 'a user has left the room');
        room = null;
      })
    });

    socket.on('message', function(message) {
      console.log(message);
      // Change message to json, not string
      io.in(room).emit("message", message);
    });

    // DISCONNECT FROM SOCKET

    socket.on('disconnect', function() {
      console.log('disconnected ' + player.name);
      controller.disconnect(player); // Disconnects from controller
      controller.leaveQueue(player); // Remove from queue
      player = null; // unødvendig
      // update all connected clients with correct info
      io.sockets.emit("update", controller.getInfo());
    })
});

console.log('Server started. http://localhost:' + port);
