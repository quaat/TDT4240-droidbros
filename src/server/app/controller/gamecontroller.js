var Game = require('../models/game');

var users = []; // users connected
var games = []; // games running
var queue = []; // users in game queue

/**
 * Add player to users list
 * @param {Player} player
 */
exports.connect = function(player) {
  users.push(player);
};

/**
 * Removes player from user list
 * @param {Player} player
 */
exports.disconnect = function(player) {
  for (var i = 0; i < users.length; i++) {
    if (users[i].id == player.id)
      users.splice(i, 1);
  }
};

/**
 * Reconnects player to his game after disconnect
 * @param {Player} player
 */
exports.reconnect = function(player) {
  // Todo, some more logic? :/
  return this.findGameWithPlayer(player);
}

/**
 * Add player to queue
 * @param {Player} player
 */
exports.joinQueue = function(player) {
  for (var i = 0; i < queue.length; i++) {
    if (queue[i].id == player.id) return;
  }
  queue.push(player);
};

/**
 * Remove player from queue
 * @param {Player} player
 */
exports.leaveQueue = function(player) {
  for (var i = 0; i < queue.length; i++) {
    if (queue[i].id == player.id)
      queue.splice(i, 1);
  }
};

/**
 * Finds an opponent to play against
 * @return {Player} opponent
 */
exports.matchmaking = function() {
  if (queue.length >= 1) return queue.shift();
}

/**
 * Create a new game with given players, adds new game to games list
 * @param {Player} p1
 * @param {Player} p2
 * @return {Game} new game
 */
exports.createGame = function(p1, p2) {
  if (Math.floor(Math.random()*2)==0) {
    p1.color = "white";
    p2.color = "black";
  } else {
    p1.color = "black";
    p2.color = "white";
  }

  var game = {
    gameid: (Math.random()+1).toString(36).slice(2, 18), // random id
    player1: p1, // player1
    player2: p2, // player2
    started: new Date().toLocaleString(), // start date
    ended: "",
    moves: [], // history of all moves
    fen: this.createFen(p1, p2), // Starting fen string
    winner: ""
  };
  games.push(game);
  return game;
};

/**
 * Creates initial fen based on custom start setup of both players
 * @param {Player} p1
 * @param {Player} p2
 * @return {String} fen
 */
exports.createFen = function(p1, p2) {
  var fen = "";
  // TODO make pretty! :)
  if (p1.color=="black") {
    fen += p1.fen;
    fen += "/8/8/8/8/";
    fen += p2.fen.toUpperCase().substring(9, 17);
    fen += "/";
    fen += p2.fen.toUpperCase().substring(0, 8);
  } else {
    fen += p2.fen;
    fen += "/8/8/8/8/";
    fen += p1.fen.toUpperCase().substring(9, 17);
    fen += "/";
    fen += p1.fen.toUpperCase().substring(0, 8);
  }
  fen += " w - - 0 1";
  return fen;
}

/**
 * Updates given game with new fen
 * @param {Game} game
 * @param {String} fen
 */
exports.updateGame = function(game, fen) {
  if (game) {
    game.moves.push(fen);
    game.fen = fen;
  }
}

/**
 * Finds and return game with given param
 * @param {String} gameid
 * @return {Game} game
 */
exports.findGame = function(gameid) {
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid)
      return games[i];
  }
}

/**
 * Finds and return game if given player is in it
 * @param {Player} player
 * @return {Game} game
 */
exports.findGameWithPlayer = function(player) {
  for (var i = 0; i < games.length; i++) {
    if (games[i].player1.userid == player.userid || games[i].player2.userid == player.userid)
      return games[i];
  }
}

/**
 * Finds and returns game from database given param
 * @param {String} gameid
 * @return {Game} game
 */
exports.findGameDB = function(gameid) {
  Game.findone({
    gameid: gameid
  }, function(err, game) {
    if (err) throw err;
    console.log("found game with id: ", gameid);
    return game;
  })
}

/**
 * Ends given game, set end date and winner
 * @param {Game} game
 * @param {String} winner
 */
exports.endGame = function(game, winner) {
  if (game) {
    game.winner = winner;
    game.ended = new Date().toLocaleString();
    this.saveGame(game);
  }
};

/**
 * Stores game in database
 * @param {Game} game
 */
exports.saveGame = function(game) {
  if (game) {
    var g = new Game({
      gameid: game.gameid,
      player1: game.player1.name,
      player2: game.player2.name,
      started: game.started,
      ended: game.ended,
      moves: game.moves,
      winner: game.winner
    }); 
    g.save(function(err) {
      if (err) throw err;
      console.log('Game saved successfully');
    });
  }
}

/**
 * Removes game from games list
 * @param {String} gameid
 */
exports.removeGame = function(gameid) {
  // remove game from games list
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid)
      games.splice(i, 1);
  }
};

/**
 * Removes game from games list
 * @param {String} gameid
 */
exports.getOpponent = function(game, player) {
  if (game) return (player.id == game.player1.id) ? game.player2 : game.player1;
};

/**
 * Returns length of users, queue, games
 * @param {String} gameid
 */
exports.getInfo = function() {
  var ret = {
    users: users.length,
    queue: queue.length,
    games: games.length
  };
  console.log(ret);
  return ret;
};