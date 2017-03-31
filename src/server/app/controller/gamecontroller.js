// global variables

var users = []; // users connected
var games = []; // games running
var queue = []; // users in game queue

var nextGameId = 1; // 


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

exports.matchmaking = function() {
  // Creates a game if two players are in the queue
  // make new object in database
  if (queue.length >= 2) {
    var p1 = queue.shift();
    var p2 = queue.shift();

    // set random color on each player
    if (Math.floor(Math.random()*2)==0) {
      p1.color = "white";
      p2.color = "black";
    } else {
      p1.color = "black";
      p2.color = "white";
    }

    var game = {
      id: nextGameId, // gameid
      player1: p1, // player1
      player2: p2, // player2
      start: new Date().toLocaleString(), // date
      moves: [], // history of all moves
      state: "start", // Fen string
      turn: "white" //
    };
    nextGameId++;
    games.push(game); // add game object to list
    return game;
  }
}

exports.updateGame = function(gameid, newstate, move) {
  // Adds new move to a game
  // Save it to database!
  for (var i = 0; i < games.length; i++) {
    if (games[i].id == game.id) {
      games[i].moves.push(move); // add new move
      games[i].state = newstate; // Update gamestate
      games[i].turn = (games[i].turn == "white") ? "black" : "white";
      console.log("Updated game");
      return [games[i].state, games[i].turn]; // return the new state and turn
    }
  } // Return nothing if gameid not found
}

exports.endGame = function(gameid) {
  // save game to database, not save after each move, if server crash -> just erase game, and start another
  // when server is up again. Both players might not be ready at that time.
  this.removeGame();
  return;
};

exports.removeGame = function(gameid) {
  // remove game from games list
  for (var i = 0; i < games.length; i++) {
    if (games[i].id == game.id) {
      games.splice(i, 1);
    }
  }
};

exports.getUsersOnline = function() {
  return users.length;
};

exports.getUsersInQueue = function() {
  return queue.length;
};

exports.toString = function() {
  return "users: "+users.length + ", queue: "+queue.length;
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

/*
// FIFO from queue, and 2 players to game object
if (queue.length >= 2) {
  var player1 = queue.shift();
  var player2 = queue.shift();
  g1.initGame(player1, player2, new Date().toLocaleString());
}


// Add games
var g1 = new game(1);
var g2 = new game(2);

games.push(g1);
games.push(g2);

// Add users
var u1 = new user(1, "andy", 1);
var u2 = new user(2, "johan", 1);
var u3 = new user(3, "nora", 1);

users.push(u1);
users.push(u2);
users.push(u3);

console.log(users.find(findUser));
*/

// Add users to queue
//queue.push(u1);
//queue.push(u2);
//queue.push(u3);

function user(id, name, level) {
  this.id = id;
  this.name = name;
  this.level = level;
  this.inGame = false;
  this.setInGame = function(bool) {
    this.inGame = bool;
  };
}

function game(id) {
  this.id = id;
  this.active = false;
  this.start = null;
  this.player1 = null;
  this.player2 = null;

  this.initGame = function(player1, player2, date) {
    this.start = date;
    this.active  = true;
    this.player1 = player1;
    this.player2 = player2;
  }
  this.resetGame = function() {
    this.start = null;
    this.active = false;
    this.player1 = null;
    this.player2 = null;
  }
}