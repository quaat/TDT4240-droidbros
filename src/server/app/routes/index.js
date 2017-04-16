var routes  = require('express').Router();
var api     = require('./api');

routes.use('/api', api);

routes.get('/', (req, res) => {
  res.json({success: true, message: 'welcome!'});
});

routes.post('/logout', (req, res) => {
  // todo
  res.json({success: true, message: 'Logged out'});
});

module.exports = routes;

