var request     = require('supertest');
var should      = require('should');

var api         = require('../test-apihelper');
var database    = require('../test-databasehelper');

describe('/api/user/', function(){
  var server;
  var token;
  before(function(done) {
    server = require('../../../server');
    database.addGame('gameDummy1', 'userDummy', 'badFakeDummy');
    database.addGame('gameDummy2', 'GMFakeDummy', 'userDummy');
    database.addGame('gameDummy3', 'dumbFakeDummy', 'sickFakeDummy', function(err) { done() });
  });
  after(function(done) {
    server.close();
    database.delGame('gameDummy1');
    database.delGame('gameDummy2');
    database.delGame('gameDummy3', function(err) { done() });
  });

  /* Initial */

  it('Should Register', function(done) {
    api.register(request(server), {userid: 'userDummy', password: 'password'}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should Login and get token', function(done) {
    api.login(request(server), {userid: 'userDummy', password: 'password'}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.user.token.should.be.ok;
      token = res.body.user.token;
      done();
    });
  });

  /* Get user testing */
  it('Should get user', function(done) {
    api.user(request(server), {token: token}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.user.should.be.ok;
      res.body.user.userid.should.equal('userDummy');
      res.body.user.fen.should.be.ok;
      done();
    });
  });

  /* Get games testing */
  it('Should get games', function(done) {
    api.games(request(server), {token: token}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.games.should.be.ok;
      res.body.games.length.should.equal(2);
      done();
    });
  });

  /* Update user testing */

  it('Should fail update user with no data', function(done) {
    api.update(request(server), {token: token}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should update fen', function(done) {
    api.update(request(server), {token: token, fen: 'p1p1p1p1/1p1p1k1p'}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.fen.should.equal('p1p1p1p1/1p1p1k1p');
      res.body.token.should.be.ok;
      token = res.body.token;
      done();
    });
  });

  it('Should get updated user', function(done) {
    api.user(request(server), {token: token}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      res.body.user.should.be.ok;
      res.body.user.userid.should.equal('userDummy');
      res.body.user.fen.should.equal('p1p1p1p1/1p1p1k1p');
      done();
    });
  });

  /* Change password testing */
  it('Should fail change password with no old password', function(done) {
    api.changePassword(request(server), {token: token, newPassword:'password123'}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should fail change password with wrong old password', function(done) {
    api.changePassword(request(server), {token: token, newPassword:'password123', oldPassword: 'wrong'}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should change password with correct old password', function(done) {
    api.changePassword(request(server), {token: token, newPassword:'password123', oldPassword: 'password'}, function(err, res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      token = res.body.token;
      done();
    });
  });

  /* Remove user testing*/
  it('Should get error deleting with no password', function(done) {
    api.removeUser(request(server), {token: token}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should get error deleting user with wrong password', function(done) {
    api.removeUser(request(server), {token: token, oldPassword: 'wrong'}, function(err, res){
      res.body.success.should.equal(false);
      res.body.should.have.property('message');
      done();
    });
  });

  it('Should delete user', function(done) {
    api.removeUser(request(server), {token: token, oldPassword: 'password123'}, function(err,res){
      res.body.success.should.equal(true);
      res.body.should.have.property('message');
      done();
    });
  });
});