// global variables
var Game = require('../models/game');

var users = []; // users connected
var games = []; // games running
var queue = []; // users in game queue

exports.connect = function(player) {
  users.push(player);
};

exports.disconnect = function(player) {
  // remove player from users list
  for (var i = 0; i < users.length; i++) {
    if (users[i].id == player.id)
      users.splice(i, 1);
  }
};

exports.joinQueue = function(player) {
  // Wont add same player to queue
  for (var i = 0; i < queue.length; i++) {
    if (queue[i].id == player.id) return;
  }
  queue.push(player); // add user to queue
};

exports.leaveQueue = function(player) {
  // remove player from queue
  for (var i = 0; i < queue.length; i++) {
    if (queue[i].id == player.id)
      queue.splice(i, 1);
  }
};

// Finds a player to play against
// Very simple solution for now
exports.matchmaking = function() {
  if (queue.length >= 1) return queue.shift();
}

// Create game with players
exports.createGame = function(p1, p2) {
  // set random color on each player
  if (Math.floor(Math.random()*2)==0) {
    p1.color = "white";
    p2.color = "black";
  } else {
    p1.color = "black";
    p2.color = "white";
  }

  var game = {
    gameid: (Math.random()+1).toString(36).slice(2, 18),
    player1: p1, // player1
    player2: p2, // player2
    started: new Date().toLocaleString(), // date
    ended: "",
    moves: [], // history of all moves
    fen: this.createFen(p1, p2), // Starting fen string
    winner: ""
  };
  games.push(game); // add game object to list
  return game;
};

// Creates initial fen based on custom start moves of both players
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

exports.updateGame = function(game, fen) {
  if (game) {
    game.moves.push(fen);
    game.fen = fen; // Update fen
  }
}

// Find game in list
exports.findGame = function(gameid) {
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid)
      return games[i];
  }
}

// Find game in database
exports.findGameDB = function(gameid) {
  Game.findone({
    gameid: gameid
  }, function(err, game) {
    if (err) throw err;
    console.log("found game with id: ", gameid);
    return game;
  })
}

exports.endGame = function(game, winner) {
  if (game) {
    // set winner
    game.winner = winner;
    game.ended = new Date().toLocaleString();
    // Save to database
    this.saveGame(game);
  }
};

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
  console.log("game undefined");
}

exports.removeGame = function(gameid) {
  // remove game from games list
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid)
      games.splice(i, 1);
  }
};

exports.getOpponent = function(game, player) {
  if (game) return (player.id == game.player1.id) ? game.player2 : game.player1;
};

exports.getInfo = function() {
  var ret = {
    users: users.length,
    queue: queue.length,
    games: games.length
  };
  console.log(ret);
  return ret;
};