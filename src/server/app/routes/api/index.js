var api 			= require('express').Router();
var register      	= require('./register');
var authenticate  	= require('./authenticate');
var middleware 		= require('./middleware');
var user			= require('./user');

api.post('/register', register);
api.post('/authenticate', authenticate);

api.use(middleware);

api.get('/', (req, res) => {
  res.json({success: true, message: 'API'});
});

api.use('/user', user);

module.exports = api;