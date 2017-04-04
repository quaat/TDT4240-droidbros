var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

module.exports = mongoose.model('User', new Schema({
    userid: String,
    name: String,
    email: String,
    fen: String,
    level: Number,
    hash: String,
    admin: Boolean
}));
