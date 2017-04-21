package no.ntnu.game.models;

import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.FEN;
import no.ntnu.game.TypeErrorException;

/**
 * Model of the game
 * Notifies observers when model has changed
 */

public class GameModel extends ObservableModel {
    // Client info
    private User user; // User logged in as

    // Current game
    private GameInfo gameInfo; // All info about current game

    // Statistics from server
    private String currentUsers; // Users online
    private String currentQueue; // Users searching for game
    private String currentGames; // Games in progress

    // Error message
    private String error;

    public GameModel() {
        currentUsers = currentQueue = currentGames = error = "";
    }

    /**
     * Starts a new game
     * @param gameInfo - information about new game
     */
    public void startGame(JsonValue gameInfo) {
        this.gameInfo = new GameInfo(gameInfo, user);
        emitGameUpdate();
    }

    /**
     * Ends current game
     * @param gameInfo - information about new game
     */
    public void endGame(JsonValue gameInfo) {
        this.gameInfo = new GameInfo(gameInfo, user);
        emitGameUpdate();
    }

    /**
     * Updates board with fen string (from server)
     * @param fen Updates fen string
     */
    public void updateGame(String fen) {
        gameInfo.update(fen);
        emitNewMove();
    }

    public void addMove(String fen) {
        gameInfo.update(fen);
    }

    /**
     * Updates list with params
     * @param users users online
     * @param queue users searching for games
     * @param games games being played
     */
    public void updateStatistics(String users, String queue, String games) {
        currentUsers = users;
        currentQueue = queue;
        currentGames = games;
        emitServerUpdate();
    }

    public void updateError(String error) {
        this.error = error;
        emitError();
    }

    /**
     * Set user
     * @param user user
     */
    public void setUser(User user) {
        this.user = user;
        emitUserUpdate();
    }

    /* User logged in as*/
    public User user() {
        return user;
    }

    /* Object that opponent has recieved of you*/
    public Player player() {
        return gameInfo.player();
    }

    /* Player playing against*/
    public Player opponent() {
        return gameInfo.opponent();
    }

    /* Uniqe gameid */
    public String gameid() {
        return gameInfo.gameid();
    }

    /* Current or last position as fen string*/
    public String fen() {
        return gameInfo.fen();
    }

    /* Winner when game has ended*/
    public String winner() {
        return gameInfo.winner();
    }

    /* Users connected to server */
    public String currentUsers() {
        return currentUsers;
    }

    /* Users searching for game */
    public String currentQueue() {
        return currentQueue;
    }

    /* Games being played in real time*/
    public String currentGames() {
        return currentGames;
    }

    /* Returns if it is this clients turn*/
    public Piece.Color color() {
        return gameInfo.color();
    }

    public GameInfo gameInfo()  {
        return gameInfo;
    }

    /* Returns error message */
    public String error() {
        return error;
    }
}
