// -- Setup

var express    = require('express');
var app        = express();
var bodyParser = require('body-parser');
var morgan     = require('morgan');
var mongoose   = require('mongoose');
var jwt        = require('jsonwebtoken');
var config     = require('./config');

var port = process.env.PORT || 8081;

mongoose.Promise = require('bluebird');
mongoose.connect(config.database);

app.set('superSecret', config.secret);
app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
app.use(morgan('dev'));

// -- Routes
var routes = require('./app/routes/');
app.use('/', routes);

/* Start server */
var server = require('http').createServer(app).listen(port);

/* Attach sockethandler to server */
require('./app/socket/socket.js')(app, server);

console.log('server started');

module.exports = server;