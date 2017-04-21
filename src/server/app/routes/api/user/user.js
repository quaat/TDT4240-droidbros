var crypto  = require('../../../crypto/hashing');
var jwt     = require('jsonwebtoken');

var User = require('./../../../models/user');
var Game = require('./../../../models/game');
/**
 * Returns user logged in as
 * @return {success, message, user} json
 */
module.exports.get = function (req, res) {
  User.findOne({
    userid: req.decoded._doc.userid
  }, function(err, user) {
    if (err) res.json({success: false, message: 'Database find user error  -- ' + err});
    var token = jwt.sign(user, req.app.get('superSecret'));
    res.json({
      success: true, 
      message: 'User delivered', 
      user: user,
      token: token
    });
  });
};

/**
 * Returns a list of games played
 * todo - Add a list of games on user for faster getter!!
 * @return {sucess message games} json
 */
module.exports.games = function (req, res) {
  Game.find({
      '$or': [
         { player1: req.decoded._doc.userid},
         { player2: req.decoded._doc.userid}
      ]
    }, function (err, games) {
      if (err) res.json({success: false, message: 'Database error -- ' + err});
      else res.json({success: true, message: 'Games found', games: games});
    });
};

/**
 * Updates user with new name, email or fen
 * @param {name || email} req.body
 * @return {error, message} json
 * @return {success, message, user} json
 */
module.exports.update = function (req, res) {
  if(!req.body.fen) return res.json({success: false, message: 'Missing fen'});

  User.findOneAndUpdate({ 
    userid: req.decoded._doc.userid // query - user logged in as
  }, {
    fen: req.body.fen
  }, {
    new: true
  },function (err, user) { 
    if (err) res.json({success: false, message: 'Database failed to find user -- ' + err});
    var token = jwt.sign(user, req.app.get('superSecret'));
    return res.json({
      success: true, 
      message: 'User updated',
      fen: user.fen,
      token: token
    });
  });
};

/**
 * Updates user with new password
 * @param {oldPassword, newPassword} json
 * @return {error, message} json
 * @return {successs, messsage} json
 */
module.exports.changePassword = function (req, res) {
  if (!req.body.oldPassword) return res.json({success: false, message: 'Missing new password...'});
  if (!req.body.newPassword) return res.json({success: false, message: 'Missing old password...'});

  // check if old password is correct
  if (!crypto.checkPassword(req.body.oldPassword, req.decoded._doc.hash)) {
    return res.json({success: false, message: 'Wrong old password.'});
  } else {
    // Create new hash with new password
    try { 
      var passwordHash = crypto.hash(req.body.newPassword);
    } catch (err) {
      return res.json({success: false, message: 'Password hash failed -- ' + err});
    }
    // Update user hash in database
    User.findOneAndUpdate({ 
      userid: req.decoded._doc.userid // query
    }, {
      hash: passwordHash  // update
    }, {
      new: true // options
    }, function (err, user) {
      if (err) return res.json({success: false, message: 'Database failed to update user -- ' + err});
      var token = jwt.sign(user, req.app.get('superSecret'));
      return res.json({success: true, message: 'Password changed', token: token});
    });
  }
};

/**
 * Updates user with new password
 * @return {error, message} json
 * @return {success, message} json
 */
module.exports.remove = function (req, res) {

  if (!req.body.oldPassword) return res.json({success: false, message: 'Missing password...'});

  // check if password is correct
  if (!crypto.checkPassword(req.body.oldPassword, req.decoded._doc.hash)) {
    return res.json({success: false, message: 'Wrong password.'});
  } else {
    User.findOneAndRemove({
      userid: req.decoded._doc.userid
    }, function(err) {
      if (err) return res.json({success: false, message: 'Database failed to find or delete user -- ' + err});
      return res.json({success: true, message: 'User deleted'});
    });
  }
}