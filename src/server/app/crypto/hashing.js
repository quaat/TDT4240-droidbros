var bcrypt = require('bcrypt');
var salt = bcrypt.genSaltSync(8);

module.exports = {   
    hash: function(cleartext) {
	var output = bcrypt.hashSync(cleartext, salt);
	return output;
    },
    checkPassword: function(cleartext, hash) {
	return (bcrypt.hashSync(cleartext,salt) == hash);
    }
};
    
