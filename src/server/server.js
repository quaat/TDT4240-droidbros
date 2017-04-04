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

    var currentGame; // Current this user os game playing

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
        currentGame = controller.createGame(player, opponent);
        console.log("game created", currentGame.gameid);
        // Find opponents socket id
        var opponent = controller.getOpponent(currentGame, player);
        socket.join(currentGame.gameid);
        // Tell other player that game is ready
        socket.to(opponent.id).emit("gameReady", {gameid: currentGame.gameid});
      } else { // Did not find opponent
        controller.joinQueue(player); // Add player to queue
      }
      // Update server info to all clients
      io.sockets.emit("update", controller.getInfo());
    });

    socket.on('joinGame', function(gameid) {
      console.log(player.name, "joinGame", gameid);
      // Update game object
      currentGame = controller.findGame(gameid);
      // Update opponent object 
      if (currentGame) {
        socket.join(gameid);
        // Send game info to both clients connected
        io.in(gameid).emit("startGame", currentGame);
      } else {
        console.log("couldnt find game", gameid);
      }
    });

    socket.on('newMove', function(json) {
      console.log(player.name, "newMove");
      data = JSON.parse(json);
      console.log(data);
      // Update game object
      controller.updateGame(currentGame, data.fen, data.move, data.turn);
      console.log(currentGame);
      // Send new move to opponent
      socket.to(currentGame.gameid).emit("newMove", data);
    });

    socket.on('resign', function(gameid) {
      console.log(player.name, "Resign");
      // End game
      controller.endGame(currentGame);
      // Send game over to both players
      console.log(currentGame);
      io.in(game.gameid).emit("gameOver", currentGame)
      // remove game
      controller.removeGame(currentGame.gameid);
    });

    socket.on('disconnect', function() {
      console.log('disconnected ' + player.name);

      //****** test
      if (currentGame) {
        controller.endGame(currentGame);
        console.log(currentGame);
        io.in(currentGame.gameid).emit("gameOver", currentGame)
      }
      //******

      // Disconnects from controller
      controller.disconnect(player);
      // Remove from queue
      controller.leaveQueue(player);
      player = null;
      // Update all connected clients with correct info
      io.sockets.emit("update", controller.getInfo());
    });
});

console.log('Server started. http://localhost:' + port);
