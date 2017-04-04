package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

public class GameModel extends ObservableModel {
    private User user; // user logged in as

    // Current game
    private GameInfo gameInfo;
    private Board board;

    // remove
    public boolean myTurn;

    // Statistics from server
    private String currentUsers; // Users online
    private String currentQueue; // Users searching for game
    private String currentGames; // Games in progress

    public GameModel() {

    }

    // set current user
    public void setUser(User user) {
        this.user = new User(user);
        emitChanges();
    }

    // start a new game
    public void startGame(JsonValue gameInfo) {
        this.gameInfo = new GameInfo(gameInfo, user);
        myTurn = (this.gameInfo.color()== Piece.Color.WHITE) ? true : false;

        //board = new Board();
        Gdx.app.log("ANDYPANDY", user.userid());
        emitChanges();
    }

    // update ongoing game
    public void updateGame(String fen) {
        gameInfo.update(fen);
        onNewMove();
    }

    // end game
    public void endGame() {
        // game over
    }

    // update queue size
    public void updateStatistics(String users, String queue, String games) {
        currentUsers = users;
        currentQueue = queue;
        currentGames = games;
        emitChanges();
    }

    public User user() {
        return user;
    }

    public Player player() {
        return gameInfo.player();
    }

    public Player opponent() {
        return gameInfo.opponent();
    }

    public String gameid() {
        return gameInfo.gameid();
    }

    public String currentUsers() {
        return currentUsers;
    }

    public String currentQueue() {
        return currentQueue;
    }

    public String currentGames() {
        return currentGames;
    }

    public boolean isItMyTurn() {
        return myTurn;
        //return board.activeColor()==gameInfo.color();
    }
}
