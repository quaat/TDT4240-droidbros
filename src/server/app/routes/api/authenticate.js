var crypto  = require('../../crypto/hashing');
var jwt     = require('jsonwebtoken');
var User    = require('./../../models/user');
/**
 * Login - Password matches with hash
 * @param {userid, password} req.body
 * @return {error, message} json
 * @return {success, message, user, token} json
 */

module.exports = (req, res) => {
  if (!req.body) return res.json({success: false, message: 'Missing body...'});
  if (!req.body.userid) return res.json({success: false, message: 'Missing userid...'});
  if (!req.body.password) return res.json({success: false, message: 'Missing password...'});

  User.findOne({  // Find the user
    userid: req.body.userid
  }, function(err, user) {
    if (err) return res.json({success: false, message: 'Database find user error -- ' + err});
    if (!user) return res.json({success: false, message: 'Authentication failed. User not found.'});
    else if (user) {
      // check if password matches
      if (!crypto.checkPassword(req.body.password, user.hash)) {
        return res.json({success: false, message: 'Authentication failed. Wrong password.'});
      } else {
        // if user is found and password is right
        // create a token
        var token = jwt.sign(user, req.app.get('superSecret'));
        // return the information including token as JSON
        return res.json({
          success: true,
          message: 'Login success',
          user: {
            userid: user.userid,
            token: token,
            name: user.name,
            email: user.email,
            level: user.level,
            fen: user.fen
          }
        });
      }
    }
  });
};