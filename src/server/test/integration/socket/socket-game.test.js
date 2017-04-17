var request     = require('supertest');
var should      = require('should');
var io          = require('socket.io-client');

var api         = require('../test-apihelper');
var database    = require('../test-databasehelper');


describe('Socket Game', function(){
  var server;
  var token;

  var client1 = io('');
  var client2 = io('');

  var token1;
  var token2;

  before(function() {
    server = require('../../../server');
  });

  after(function() {
    server.close();
  });

  it('Should get tokens', function(done) {
    api.login(request(server), {userid: 'socketDummy1', password: 'password'}, function(err, res){
      token1 = res.body.user.token;
    });
    api.login(request(server), {userid: 'socketDummy2', password: 'password'}, function(err, res){
      token2 = res.body.user.token;
      done();
    });
  });


  it('Should succeed to connect both users', function(done) {
    client1 = io('http://localhost:8081/', {query: {token: token1}});
    client2 = io('http://localhost:8081/', {query: {token: token2}});

    client2.on('update', function(data) {
      data.users.should.equal(2);
      done();
      client1.disconnect();
      client2.disconnect();
    });
  });

  it('Should find, play and resign a game (2 players)', function(done) {
    var game;
    var info;

    client1 = io('http://localhost:8081/', {query: {token: token1}});
    client2 = io('http://localhost:8081/', {query: {token: token2}});
    // find game
    client1.emit('findGame');
    client2.emit('findGame');
    // game found
    client1.on('gameReady', function(data) {
      client1.emit('joinGame', data.gameid)
    });

    client2.on('gameReady', function(data) {
      client2.emit('joinGame', data.gameid)
    });
    // game started
    client1.on('startGame', function(data) {
      game = data;
      client1.emit('newMove', 'another');
    });

    client1.on('newMove', function(data) {
      game.moves.push(data.fen);
      client1.emit('newMove', data.fen);
    })

    client2.on('newMove', function(data) {
      if (game.moves.length==20) client2.emit('resign');
      game.moves.push(data.fen);
      client2.emit('newMove', data.fen);
    });

    client1.on('update', function(data) {
      info = data;
    })

    // Game over

    client1.on('gameOver', function (data) {
      data.winner.should.equal('socketDummy1');
      info.users.should.equal(2);
      info.games.should.equal(1);
      done();

      client1.disconnect();
      client2.disconnect();
    });
  });

  it('Should reconnect', function(done) {
    var info;
    var num = 0;

    client1 = io('http://localhost:8081/', {query: {token: token1}});
    client2 = io('http://localhost:8081/', {query: {token: token2}});

    // find game
    client1.emit('findGame');
    client2.emit('findGame');
    // game found
    client1.on('gameReady', function(data) {
      client1.emit('joinGame', data.gameid)
    });

    client2.on('gameReady', function(data) {
      client2.emit('joinGame', data.gameid)
    });
    // game started
    client1.on('startGame', function(data) {
      client1.emit('newMove', 'client2');
      num++;
    });

    client1.on('reconnect', function(data) {
      if (data.fen=='client1') {
        num++;
        client1.emit('newMove', 'client2');
      }
    });


    client1.on('newMove', function(data) {
      if (data.fen=='client1') {
        num++;
        client1.emit('newMove', 'client2');
      }
    });

    client2.on('newMove', function(data) {
      if (num==21) client1.io.disconnect();
      if (num==41) client1.io.disconnect();

      if (num==99) {
        client2.emit('resign')
      }else {
        if (data.fen=='client2') {
          num ++;
          client2.emit('newMove', 'client1');
        }
      }
    });

    client1.on('update', function(data) {
      info = data;
    });

    client1.on('disconnect', function(data) {
      client1.io.connect();
    })

    client2.on('disconnect', function(data) {
      client2.io.connect();
    })

    // Game over

    client1.on('gameOver', function (data) {
      data.winner.should.equal('socketDummy1');
      info.users.should.equal(2);
      info.games.should.equal(1);
      data.moves.length.should.equal(99);
      done();

      client1.disconnect();
      client2.disconnect();
    });
  });

  it('Should ', function(done) {
    done();
  });
});
