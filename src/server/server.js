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
    var game;
    var opponent;

    // Connect user to controller
    controller.connect(player); 
    // Send welcome message back, with some info
    io.sockets.emit("update", controller.getInfo());
    // User request to find a game
    socket.on('findGame', function() {
      controller.joinQueue(player); // Add player to queue
      game = controller.matchmaking(); // Find two players for game
      if (game) { // if game created
        console.log(game);
        // Tell players that game is ready!
        opponent = (player.id == game.player1.id) ? game.player2 : game.player1;
        // send only what is needed
        // see matchmaking function in controller.js, for game object variables.
        var dataToPlayer = {
          gameid: game.id,
          opponent: opponent.name,
          color: player.color
        }
        var dataToOpponent = {
          gameid: game.id,
          opponent: player.name,
          color: opponent.color
        }
        socket.to(opponent.id).emit("startGame", dataToOpponent); // opponent
        socket.emit("startGame", dataToPlayer); // player
      }
      io.sockets.emit("update", controller.getInfo());
    });

    socket.on('newMove', function(data) {
      console.log(data);
      // var state = controller.updateGame(data.id, data.newstate, data.move);
      socket.to(opponent.id).emit("newMove", "halla");
      // Assumes it is a valid move
      // Add move to game object
      // Send move to other client (opponent)
      // send confirmation back to client?
    });

    socket.on('resign', function() {
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
