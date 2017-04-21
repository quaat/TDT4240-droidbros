var Game = require('../models/game');
var User = require('../models/user');

var users = []; // users connected
var games = []; // games running
var queue = []; // users in game queue

module.exports.reset = function() {
  users = [];
  games = [];
  queue = [];
}

/**
 * Add player to users list
 * @param {player} Player
 */
module.exports.connect = function(player) {
  users.push(player);
};

/**
 * Removes player from user list
 * @param {player} Player
 */
module.exports.disconnect = function(player) {
  for (var i = 0; i < users.length; i++) {
    if (users[i].userid == player.userid) {
      users.splice(i, 1);
    }
  }
};

/**
 * Reconnects player to his game after disconnect
 * @param {player} Player
 */
module.exports.reconnect = function(player) {
  return this.findGameWithPlayer(player);
}

/**
 * Add player to queue
 * @param {player} Player
 */
module.exports.joinQueue = function(player) {
  for (var i = 0; i < queue.length; i++) {
    if (queue[i].userid == player.userid) {
      return;
    }
  }
  queue.push(player);
};

/**
 * Remove player from queue
 * @param {player} Player
 */
module.exports.leaveQueue = function(player) {
  for (var i = 0; i < queue.length; i++) {
    if (queue[i].userid == player.userid) {
      queue.splice(i, 1);
    }
  }
};

/**
 * Finds an opponent to play against
 * @return {players} Players
 */
module.exports.matchmaking = function() {
  if (queue.length >= 2) {
    var player1 = queue.shift();
    var player2 = queue.shift();

    var game = this.createGame(player1, player2);
    games.push(game);

    return game;
  }
};

/**
 * Create a new game with given players, adds new game to games list
 * @param {Player} p1
 * @param {Player} p2
 * @return {Game} new game
 */
module.exports.createGame = function(player1, player2) {
  if (Math.floor(Math.random()*2)==0) {
    player1.color = 'white';
    player2.color = 'black';
  } else {
    player1.color = 'black';
    player2.color = 'white';
  }

  var game = {
    gameid: (Math.random()+1).toString(36).slice(2, 18), // random id
    player1: player1, // player1
    player2: player2, // player2
    started: new Date().toLocaleString(), // start date
    moves: [], // history of all moves
    fen: this.createFen(player1, player2), // Starting fen string
  };
  return game;
};

/**
 * Creates initial fen based on custom start setup of both players
 * @param {player1} Player
 * @param {player2} Player
 * @return {fen} String
 */
module.exports.createFen = function(player1, player2) {

  var fen = '';
  if (player1.color=='black') {
    pos = player1.fen.indexOf('/');
    fen += player1.fen.slice(pos + 1) + '/';
    fen += player1.fen.slice(0, pos);
    fen += '/8/8/8/8/';
    fen += player2.fen.toUpperCase();
  } else {
    pos = player2.fen.indexOf('/');
    fen += player2.fen.slice(pos + 1) + '/';
    fen += player2.fen.slice(0, pos);
    fen += '/8/8/8/8/';
    fen += player1.fen.toUpperCase();
  }
  fen += ' w - - 0 1';
  return fen;
};

/**
 * Updates given game with new fen
 * @param {Game} game
 * @param {String} fen
 */
module.exports.updateGame = function(game, fen) {
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
module.exports.findGame = function(gameid) {
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid) {
      return games[i];
    }
  }
};

/**
 * Finds and return game if given player is in it
 * @param {Player} player
 * @return {Game} game
 */
module.exports.findGameWithPlayer = function(player) {
  for (var i = 0; i < games.length; i++) {
    if (games[i].player1.userid == player.userid || games[i].player2.userid == player.userid)
      return games[i];
  }
};

/**
 * Ends given game, set end date and winner
 * @param {Game} game
 * @param {String} winner
 */
module.exports.endGame = function(game, player) {
  if (game) {
    game.winner = (player.userid == game.player1.userid) ? game.player2.userid : game.player1.userid;
    game.ended = new Date().toLocaleString();
    this.saveGame(game);
    this.updatePoints(game.winner, player.userid);
  }
};

/**
 * Gives level points to winenr and looser
 * todo - testing yo!
 * @param {String} winners userid
 * @param {String} loosers userid
 */
module.exports.updatePoints = function (winnerid, looserid) {

  User.findOneAndUpdate({
    userid: winnerid
  }, {
    $inc: {
      level: 3,
      games: 1,
      wins: 1
    }
  }, function (err, data) {
    if (err) throw err;
  });

  User.findOneAndUpdate({
    userid: looserid
  }, {
    $inc: {
      level: 1,
      games: 1,
    }
  }, function (err, data) {
    if (err) throw err;
  });
};

/**
 * Stores game in database
 * @param {Game} game
 */
module.exports.saveGame = function(game) {
  if (game) {
    var g = new Game({
      gameid: game.gameid,
      player1: game.player1.userid,
      player2: game.player2.userid,
      started: game.started,
      ended: game.ended,
      moves: game.moves,
      winner: game.winner
    }).save(function(err) {
      if (err) throw err;
    });
  }
};

/**
 * Removes game from games list
 * @param {String} gameid
 */
module.exports.removeGame = function(gameid) {
  // remove game from games list
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid)
      games.splice(i, 1);
  }
};

/**
 * Returns length of users, queue, games
 * @param {String} gameid
 */
module.exports.info = function() {
  return {
    users: users.length,
    queue: queue.length,
    games: games.length
  };
};