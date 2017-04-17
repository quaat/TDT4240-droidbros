var request     = require('supertest');
var should      = require('should');

var api         = require('../test-apihelper');
var database    = require('../test-databasehelper');

/* Assumes admin user in database */

describe('/api/', function(){
  var server;
  var token;

  before(function(done) {
    server = require('../../../server');
    done();
  });

  after(function(done) {
    server.close();
    done();
  });

  it('Should Login and get token', function(done) {
    api.login(request(server), {userid: 'admin', password: 'admin'}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.user.token.should.be.ok;
      token = res.body.user.token;
      done();
    });
  });

  it('Should get forbidden with no token', function(done) {
    api.user(request(server), {}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should get token auth error with wrong token', function(done) {
    api.user(request(server), {token: 'wrong'}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should access API with correct token', function(done) {
    api.user(request(server), {token: token}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      done();
    });
  });
});