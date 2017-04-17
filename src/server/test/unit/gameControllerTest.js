var supertest   = require('supertest');
var should      = require('should');
var User        = require('../../app/models/user');
var mongoose    = require('mongoose');
var config      = require('../../config');

var gameController = require('../../app/controller/gamecontroller')


describe('Controller', function(){

  function Player(userid) {
    this.userid = userid;
    this.level = 15;
    this.fen = '3pp3/3kp3';
  };

  let p1 = new Player('Andy');
  let p2 = new Player('Johan');
  let p3 = new Player('Nora');
  let p4 = new Player('Bear');

  beforeEach(function() {
    gameController.reset();
    p1 = new Player('Andy');
    p2 = new Player('Johan');
    p3 = new Player('Nora');
    p4 = new Player('Bear');
  });

  it('get info', function(done) {
    gameController.info().should.be.json;
    gameController.info().users.should.be.a.Number;
    gameController.info().queue.should.be.a.Number;
    gameController.info().games.should.be.a.Number;
    done();
  });

  it('connect & disconnect', function(done) {
    gameController.connect(p1);
    gameController.info().users.should.equal(1);
    gameController.connect(p2);
    gameController.info().users.should.equal(2);
    gameController.disconnect(p2);
    gameController.disconnect(p1);
    gameController.info().users.should.equal(0);
    done();
  });

  it('reconnect', function(done) {
  	// todo
    done();
  });

  it('join & leave queue', function(done) {
    gameController.joinQueue(p1);
    gameController.joinQueue(p1);
    gameController.info().queue.should.equal(1);
    gameController.joinQueue(p2);
    gameController.info().queue.should.equal(2);

    gameController.leaveQueue(p1);
    gameController.leaveQueue(p2);
    gameController.info().queue.should.equal(0);
    done();
  });

  it('createFen', function(done) {
    p1.color = 'white';
    p2.color = 'black';
    p1.fen = '3pp3/3kp3';
    p2.fen = 'pppppppp/3k4';
    let fen = gameController.createFen(p1, p2);
    fen.should.be.ok;
    fen.should.equal('3k4/pppppppp/8/8/8/8/3PP3/3KP3 w - - 0 1');

    p1.color = 'black';
    p2.color = 'white';
    p1.fen = '3pp3/3kp3';
    p2.fen = 'pppppppp/3k4';
    let fen2 = gameController.createFen(p1, p2);
    fen2.should.be.ok;
    fen2.should.equal('3kp3/3pp3/8/8/8/8/PPPPPPPP/3K4 w - - 0 1');

    done();
  })

  it('createGame', function(done) {
    let game = gameController.createGame(p1, p2);

    game.should.be.ok;
    game.should.have.property('gameid');
    game.should.have.property('started');
    game.should.have.property('fen');
    game.should.have.property('player1');
    game.should.have.property('player2');
    game.player1.should.have.property('userid');
    game.player1.should.have.property('color');
    game.player1.should.have.property('fen');
    game.player1.should.have.property('level');
    done();
  });

  it('matchmaking', function(done) {
    var game;

    gameController.joinQueue(p1);
    game = gameController.matchmaking();
    gameController.info().queue.should.equal(1);
    should.not.exist(game);

    gameController.joinQueue(p2);
    game = gameController.matchmaking();
    game.should.be.ok;
    gameController.info().games.should.equal(1);
    gameController.info().queue.should.equal(0);

    done();
  });

  it('updateGame', function(done) {
    let game = gameController.createGame(p1, p2);
    game.should.be.ok;
    game.fen.should.equal('3kp3/3pp3/8/8/8/8/3PP3/3KP3 w - - 0 1');

    let newFen = '3kp3/3pp3/8/8/8/3P4/4P3/3KP3 w - - 0 1';

    gameController.updateGame(game, newFen);

    game.fen.should.equal('3kp3/3pp3/8/8/8/3P4/4P3/3KP3 w - - 0 1');
    game.moves.length.should.equal(1);
    done();
  });

  it('findGame gameid & player', function(done) {
    gameController.joinQueue(p1);
    gameController.joinQueue(p2);
    gameController.joinQueue(p3);
    gameController.joinQueue(p4);

    let game1 = gameController.matchmaking();
    let game2 = gameController.matchmaking();

    game1.should.be.ok;
    game2.should.be.ok;
    gameController.info().games.should.equal(2);

    gameController.findGame(game1.gameid).should.equal(game1);
    gameController.findGame(game2.gameid).should.equal(game2);

    gameController.findGameWithPlayer(p1).should.equal(game1);
    gameController.findGameWithPlayer(p4).should.equal(game2);

    gameController.findGameWithPlayer(p2).should.not.equal(game2);
    done();
  });

  it('removeGame', function(done) {
    gameController.joinQueue(p1);
    gameController.joinQueue(p2);

    let game = gameController.matchmaking();

    game.should.be.ok;
    gameController.info().games.should.equal(1);

    gameController.removeGame(game.gameid);
    gameController.info().games.should.equal(0);

    done();
  });

  it('endGame', function(done) {
    gameController.joinQueue(p1);
    gameController.joinQueue(p2);

    let game = gameController.matchmaking();

    game.should.be.ok;
    gameController.info().games.should.equal(1);

    gameController.endGame(game, p2);
    game.winner.should.equal(p1.userid);
    game.ended.should.be.ok;

    gameController.removeGame(game.gameid);

    gameController.info().games.should.equal(0);
    done();
  });
});