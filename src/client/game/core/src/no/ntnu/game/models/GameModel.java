package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;

public class GameModel extends ObservableModel {
    private User user;

    // Current game
    private String gameid; // gameid
    private String opponent; // playing against
    private String color; // your color
    private String state; // current game state
    private String move; // Last move of opponent
    private String turn; // Who takes next move

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
    public void startGame(String gameid, String opponent, String color) {
        this.gameid = gameid;
        this.opponent = opponent;
        this.color = color;
        this.state = "start";
        this.turn = "white";
        emitChanges();
    }

    // update ongoing game
    public void updateGame(String state, String move) {
        this.state = state;
        this.move = move;
        turn = (turn.equals("white")) ? "black" : "white";
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


    public User getUser() {
        return user;
    }

    public String getColor() {
        return color;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getGameid() {
        return gameid;
    }

    public String getState(){
        return state;
    }

    public String getTurn() {
        return turn;
    }

    public String getCurrentUsers() {
        return currentUsers;
    }

    public String getCurrentQueue() {
        return currentQueue;
    }

    public String getCurrentGames() {
        return currentGames;
    }

    public boolean isItMyTurn() {
        return color.equals(turn);
    }
}
