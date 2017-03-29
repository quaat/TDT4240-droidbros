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

var users = [];
var games = [];
var queue = [];

function user(id, name, level) {
  this.id = id;
  this.name = name;
  this.level = level;
  this.inGame = false;
  this.setInGame = function(bool) {
    this.inGame = bool;
  };
}

function game(id) {
  this.id = id;
  this.active = false;
  this.start = null;
  this.player1 = null;
  this.player2 = null;

  this.initGame = function(player1, player2, date) {
    this.start = date;
    this.active  = true;
    this.player1 = player1;
    this.player2 = player2;
  }
  this.resetGame = function() {
    this.start = null;
    this.active = false;
    this.player1 = null;
    this.player2 = null;
  }
}

function findUser(user) {
  return user.id == 2;
}
/*
// Add games
var g1 = new game(1);
var g2 = new game(2);

games.push(g1);
games.push(g2);

// Add users
var u1 = new user(1, "andy", 1);
var u2 = new user(2, "johan", 1);
var u3 = new user(3, "nora", 1);

users.push(u1);
users.push(u2);
users.push(u3);

console.log(users.find(findUser));
*/

// Add users to queue
//queue.push(u1);
//queue.push(u2);
//queue.push(u3);


// FIFO from queue, and 2 players to game object
if (queue.length >= 2) {
  var player1 = queue.shift();
  var player2 = queue.shift();
  g1.initGame(player1, player2, new Date().toLocaleString());
}



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
    console.log('connected user');
    // Create new user when connected
    var user_ = new user(socket.id, socket.decoded._doc.userid, 1);
    // Add user to list of players online
    users.push(user_);
    // Send welcome message back, with username and users online
    socket.emit("welcome", users.length, queue.length);

    socket.on('findGame', function() {
      socket.join("queue", function(){ // add user to a "queue" room
        // if user already in queue return blank
        var bool = queue.find(function(user) {
          return user.id == user_.id;
        });
        if (bool) return;
        queue.push(user_); // add user to queue
        io.in("queue").emit("queue", queue.length); // Update clients queue length
      });
    });












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
      console.log('disconnected');
      // remove from users list
      for (var i = 0; i < users.length; i++) {
        if (users[i].id == socket.id) {
          users.splice(i, 1);
        }
      }
      // remove from queue list
      for (var i = 0; i < queue.length; i++) {
        if (queue[i].id == socket.id) {
          queue.splice(i, 1);
          io.in("queue").emit("queue", queue.length); // update queue for clients
        }
      }
    })
});

console.log('Server started. http://localhost:' + port);
