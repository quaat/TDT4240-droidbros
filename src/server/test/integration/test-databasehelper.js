var User        = require('../../app/models/user');
var Game        = require('../../app/models/game');

module.exports.addGame = function(gameid, player1id, player2id, callback) {
  var game = new Game({
    gameid: gameid,
    player1: player1id,
    player2: player2id,
    started: 'today',
    ended: 'today',
    moves: ['first', 'second', 'third'],
    winner: player1id
  }).save(function(err) {
    if (callback) callback(err);
  });
}

module.exports.delGame = function(gameid, callback) {
  Game.findOneAndRemove({
    gameid: gameid
  }, function (err) {
    if (callback) callback(err);
  })
}

module.exports.delUser = function(userid, callback) {
  User.findOneAndRemove({
    userid: userid
  }, function (err) {
    if (callback) callback(err);
  });
}