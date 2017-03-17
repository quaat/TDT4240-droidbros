var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

module.exports = mongoose.model('User', new Schema({
    userid: String,
    name: String,
    email: String,
    hash: String,
    admin: Boolean
}));
