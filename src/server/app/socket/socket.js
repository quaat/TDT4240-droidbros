
module.exports = function (app, server) {
  var jwt             = require('jsonwebtoken');
  var io              = require('socket.io').listen(server);
  var User            = require('../models/user');
  var Game            = require('../models/game');
  var gameController  = require('../controller/gamecontroller');
 
  /* Variables handling update intervals */
  var updating = false;
  var updateInterval;

  /* Matchmaking and update clients */
  function updateServer() {
    //console.log('\x1b[34m', 'Trying to match');
    var game = gameController.matchmaking();
    if (game) {
      //console.log('\x1b[34m', 'matched two players', game.player1.userid, game.player2.userid);
      io.to(game.player1.socketid).emit('gameReady', {gameid: game.gameid});
      io.to(game.player2.socketid).emit('gameReady', {gameid: game.gameid});
    }

    var info = gameController.info();
    //console.log('\x1b[35m', 'updating clients', info);
    io.sockets.emit('update', info);
  }

  /* Start update on intervals */
  function startUpdate() {
    if (!updating) {
      updateInterval = setInterval(updateServer, 1000);
      updating = true;
    }
  }

  /* Stop update */
  function stopUpdate() {
    if (gameController.info().users == 0) {
      clearInterval(updateInterval);
      updating = false;
    }
  }
  /**
  * Io listens for incomming emits from connected sockets 
  */
  io.use(function (socket, next){
    if (socket.handshake.query && socket.handshake.query.token){
      jwt.verify(socket.handshake.query.token, app.get('superSecret'), function(err, decoded) {
        if(err) return next(new Error('Authentication error'));
        socket.decoded = decoded;
        next();
      });
    }
    next(new Error('Authentication error'));
  })
  .on('connection', function (socket) {

      var currentGame;
      var player = {
        socketid: socket.id, //socketid
        userid: socket.decoded._doc.userid, // userid
        fen:    socket.decoded._doc.fen, // fen
        level:  socket.decoded._doc.level // level
      };

      gameController.connect(player); // Connect user to controller
      startUpdate(); // start updating

      /* Reconnect to game if it exists */
      // todo
      currentGame = gameController.reconnect(player);
      if (currentGame) {
        socket.join(currentGame.gameid);
        socket.emit('reconnect', currentGame); // reconnect
      }

      /* Search for new game */
      socket.on('findGame', function () {
        gameController.joinQueue(player);
      });

      /* If game exists, join room and emit start */
      socket.on('joinGame', function (gameid) {
        currentGame = gameController.findGame(gameid);
        socket.join(gameid);
        socket.emit('startGame', currentGame);
      });

      /* New move, send to opponent*/
      socket.on('newMove', function (fen) {
        gameController.updateGame(currentGame, fen);
        socket.to(currentGame.gameid).emit('newMove', {fen: fen});
      });

      /* Resign */
      socket.on('resign', function () {
        gameController.endGame(currentGame, player);
        io.in(currentGame.gameid).emit('gameOver', currentGame);
        gameController.removeGame(currentGame.gameid);
      });

      /* Disconnect */
      socket.on('disconnect', function() {
        gameController.disconnect(player);
        stopUpdate();
      });
  });
}