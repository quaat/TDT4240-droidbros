package no.ntnu.game.models;

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
        emitUserUpdate();
    }

    // start a new game
    public void startGame(JsonValue gameInfo) {
        this.gameInfo = new GameInfo(gameInfo, user);
        myTurn = (this.gameInfo.color()== Piece.Color.WHITE) ? true : false;

        //board = new Board();
        emitGameUpdate();
    }

    // update ongoing game
    public void updateGame(String fen) {
        gameInfo.update(fen);
        emitNewMove();
    }

    // end game
    public void endGame(String winner) {
        gameInfo.setWinner(winner);
        emitGameUpdate();
        // game over
    }

    // update queue size
    public void updateStatistics(String users, String queue, String games) {
        currentUsers = users;
        currentQueue = queue;
        currentGames = games;
        emitServerUpdate();
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

    public String winner() {
        return gameInfo.winner();
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
