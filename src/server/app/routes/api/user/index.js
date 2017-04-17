const api = require('express').Router();
const user = require('./user');

api.get('/', user.get);
api.post('/', user.update);
api.get('/games', user.games);
api.post('/password', user.changePassword);
api.post('/delete', user.remove);

module.exports = api;