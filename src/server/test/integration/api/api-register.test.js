var request     = require('supertest');
var should      = require('should');

var api         = require('../test-apihelper');
var database    = require('../test-databasehelper');

/* Register a dummy user and deletes it after */

describe('/api/register', function(){
  var server;

  before(function() {
    server = require('../../../server');
  });

  after(function() {
    server.close();
    database.delUser('registerDummy');
  });

  it('Should get error no data', function(done) {
    api.register(request(server), {}, function(err,res){
      res.body.success.should.equal(false);
      done();
    });
  });

  it('Should get error no password', function(done) {
    api.register(request(server), {userid: 'registerDummy'}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message')
      done();
    });
  });

  it('Should get error bad username', function(done) {
    api.register(request(server), {userid: '', password: 'password'}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message')
      done();
    });
  });

  it('Should get error bad password', function(done) {
    api.register(request(server), {userid: 'registerDummy', password: ''}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message')
      done();
    });
  });

  it('Should get success', function(done) {
    api.register(request(server), {userid: 'registerDummy', password: 'password'}, function(err,res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message')
      done();
    });
  });

  it('Should get error user exists', function(done) {
    api.register(request(server), {userid: 'registerDummy', password: 'password'}, function(err,res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message')
      done();
    });
  });
});