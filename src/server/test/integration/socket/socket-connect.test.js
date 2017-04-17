var request     = require('supertest');
var should      = require('should');
var io          = require('socket.io-client');

var api         = require('../test-apihelper');
var database    = require('../test-databasehelper');


describe('Socket Connection', function(){
  var server;
  var client;
  var token;

  before(function() {
    server = require('../../../server');
  });

  after(function() {
    server.close();
  });

  it('Should get token', function(done) {
    api.login(request(server), {userid: 'socketDummy1', password: 'password'}, function(err, res){
      res.body.success.should.equal(true);
      token = res.body.user.token;
      done();
    });
  });
  
  it('Should fail to connect', function(done) {
    client = io('http://localhost:8081/');
    client.on('error', function(err) {
      done();
      client.io.disconnect();
    });
  });

  it('Should succeed to connect', function(done) {
    client = io('http://localhost:8081/', {query: {token: token}});

    client.on('connect', function() {
      done();
      client.io.disconnect();
    });
  });

  it('Should get update', function(done) {
    client = io('http://localhost:8081/', {query: {token: token}});

    client.on('update', function() {
      done();
      client.io.disconnect();
    });
  });

  it('Should connect and disconnect', function(done) {
    client = io('http://localhost:8081/', {query: {token: token}});

    client.on('connect', function() {
      client.io.disconnect();
    });

    client.on('disconnect', function() {
      done();
    });
  });

  it('Should connect, disconnect and reconnect', function(done) {
    client = io('http://localhost:8081/', {query: {token: token}});
    var count = 0;

    client.on('connect', function() {
      client.io.disconnect();
      count ++;
    });

    client.on('disconnect', function() {
      if (count == 5) done();
      else client.io.connect();
    });
  });

});