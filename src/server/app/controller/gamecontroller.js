// global variables

var users = []; // users connected
var games = []; // games running
var queue = []; // users in game queue


exports.connect = function(player) {
  users.push(player);
};

exports.disconnect = function(player) {
  // remove player from users list
  for (var i = 0; i < users.length; i++) {
    if (users[i].id == player.id) {
      users.splice(i, 1);
    }
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
    if (queue[i].id == player.id) {
      queue.splice(i, 1);
    }
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
    start: new Date().toLocaleString(), // date
    moves: [], // history of all moves
    fen: "", // Fen string
    state: "start", // start->playing->complete
    turn: "white", //
    winner: ""
  };
  games.push(game); // add game object to list
  return game;
};

exports.updateGame = function(gameid, newstate, move, turn) {
  // Adds new move to a game
  var game = this.findGame(gameid);
  game.moves.push(move); // add new move
  game.state = newstate; // Update gamestate
  game.turn = turn; //
  return game;
  // Return nothing if gameid not found
}

exports.findGame = function(gameid) {
  // TODO finne game i databasen
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid) {
      return games[i];
    }
  }
}

exports.endGame = function(gameid, winner) {
  var game = this.findGame(gameid);
  game.winner = winner;
  game.state = "done";
  // Save to database
  this.removeGame(gameid);
  return game;
};

exports.removeGame = function(gameid) {
  // remove game from games list
  for (var i = 0; i < games.length; i++) {
    if (games[i].gameid == gameid)
      games.splice(i, 1);
  }
};

exports.toString = function() {
  return "users: "+users.length + ", queue: "+queue.length;
};

exports.getOpponent = function(game, player) {
  return (player.id == game.player1.id) ? game.player2 : game.player1;
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