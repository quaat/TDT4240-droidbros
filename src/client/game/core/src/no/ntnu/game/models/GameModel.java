package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;

public class GameModel extends ObservableModel {
    private User user; // user logged in as

    // Current game
    private GameInfo gameInfo;

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
    public void startGame(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        Gdx.app.log("ANDYPANDY", user.getUserid());
        emitChanges();
    }

    // update ongoing game
    public void updateGame(String state, String move, String turn) {
        gameInfo.update(state, move, turn);
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

    public Player getOpponent() {
        return gameInfo.getOpponent(user.getUserid());
    }

    public Player getPlayer() {
        return gameInfo.getPlayer(user.getUserid());
    }

    public String getGameid() {
        return gameInfo.getGameid();
    }

    public String getTurn() {
        return gameInfo.getTurn();
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
        return gameInfo.isItMyTurn(user.getUserid());
    }
}
