var User = require('./../../models/user');
var crypto  = require('../../crypto/hashing');

/**
 * Register - New user
 * @param {userid, password, name, email} req.body
 * @return {error, message} json
 * @return {success, message} json
 */
module.exports = (req, res) => {
  if (!req.body) return res.json({secess: false, message: 'Missing data...'});
  if (!req.body.userid) return res.json({success: false, message: 'Missing userid...'});
  if (!req.body.password) return res.json({success: false, message: 'Missing password...'});

  User.findOne({
    userid: req.body.userid
  }, function(err, user) {
    if (err) return res.json({success: false, message: 'Database find user error -- ' + err});
    if (user) return res.json({success: false, message: 'Userid taken'});
    
    try {
      var passwordHash = crypto.hash(req.body.password);
    } catch (err) { 
      return res.json({success: false, message: 'Password hash error -- ' + err});
    }
    var _name = (!req.body.name) ? 'noname' : req.body.name;
    var _email = (!req.body.email) ? 'noemail' : req.body.email;
    var newUser = new User({
      userid: req.body.userid,
      name: _name,
      email: _email,
      fen: '4p3/3pkp2',
      level: 15,
      hash: passwordHash,
      admin: false
    }).save(function(err) {
      if (err) return res.json({success: false, message: 'Database save error -- ' + err});
      else return res.json({success:true, message: 'New user registered'});
    });
  });
};