var request     = require('supertest');
var should      = require('should');

var api         = require('../test-apihelper');
var database    = require('../test-databasehelper');

var User        = require('../../../app/models/user');

/* Assumes admin user in database */

describe('/api/authenticate', function(){
  var server;

  before(function(done) {
    server = require('../../../server');
    done();
  });

  after(function(done) {
    server.close();
    done();
  });

  it('Should get error no data', function(done) {
    api.login(request(server), {}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });
  
  it('Should get error no username', function(done) {
    api.login(request(server), {userid: '', password: 'password'}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });
  
  it('Should get error no password', function(done) {
    api.login(request(server), {userid: 'admin', password: ''}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should get error wrong password', function(done) {
    api.login(request(server), {userid: 'admin', password: 'wrong'}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should get success', function(done) {
    api.login(request(server), {userid: 'admin', password: 'admin'}, function(err,res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.should.have.property('user');
      res.body.user.userid.should.equal('admin');
      res.body.user.should.have.property('token');
      done();
    });
  });
});