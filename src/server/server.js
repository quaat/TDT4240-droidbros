// server.hs

// -- setup
var express    = require('express');
var app        = express();
var bodyParser = require('body-parser');
var morgan     = require('morgan');
var mongoose   = require('mongoose');

var jwt        = require('jsonwebtoken');
var socketioJwt= require('socketio-jwt');
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
    var passwordHash = crypto.hash(password);
  } catch (err) {
	  console.log('error ' + err);
  }
  console.log('=> using crypto hash: ' + "password");
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
  // find the user
  User.findOne({ 
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
var server = require('http').createServer(app).listen(port)

// sockets

var io = require('socket.io').listen(server);

io.set('authorization', socketioJwt.authorize({
  secret: app.get('superSecret'),
  handshake: true
}));

io.sockets
  .on('connection', function(socket){
    console.log('id: ' + socket.handshake.decoded_token.userid);
});

console.log('Server started. http://localhost:' + port);
