var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

module.exports = mongoose.model('Game', new Schema({
    gameid: String,
    player1: String,
    player2: String,
    started: String,
    ended: String,
    moves: [String],
    winner: String
}));
