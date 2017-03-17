var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

module.exports = mongoose.model('Game', new Schema({
    player1: String,
    player2: String,
    started: Date,
    runningTime: Number,
    moves: [String]
    
}));
