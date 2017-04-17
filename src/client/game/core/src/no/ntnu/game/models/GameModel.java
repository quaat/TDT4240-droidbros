package no.ntnu.game.models;

import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.FEN;
import no.ntnu.game.TypeErrorException;

/**
 * Model of the game
 * Notifies observers when model has changed
 */

public class GameModel extends ObservableModel {
    // todo rewrite, uglyfugly
    // Client info
    private User user; // User logged in as

    // Current game
    private GameInfo gameInfo; // All info about current game
    private Board board; // Board of current game

    // Statistics from server
    private String currentUsers; // Users online
    private String currentQueue; // Users searching for game
    private String currentGames; // Games in progress

    public GameModel() {

    }

    /**
     * Starts a new game
     * @param gameInfo - information about new game
     */
    public void startGame(JsonValue gameInfo) {
        this.gameInfo = new GameInfo(gameInfo, user);
        updateBoard();
        emitGameUpdate();
    }

    /**
     * Ends current game
     * @param gameInfo - information about new game
     */
    public void endGame(JsonValue gameInfo) {
        this.gameInfo = new GameInfo(gameInfo, user);
        updateBoard();
        emitGameUpdate();
    }

    /**
     * Updates board with fen string (from server)
     * @param fen Updates fen string
     */
    public void updateGame(String fen) {
        gameInfo.update(fen);
        updateBoard();
        emitNewMove();
    }

    /**
     * Updates board with params fromMove and toMove (from client)
     * todo use correct method for updating board
     * @param from From square
     * @param to To square
     * @return boolean
     */
    public boolean updateGame(String from, String to) {
        //board = GameAction.movePiece(board, new Move(from, to));
        return false;
    }

    /**
     * Sets the board equal to fen string
     */
    private void updateBoard() {
        // todo do this correct
        //try { board = FEN.toBoard(fen());
        //}catch(TypeErrorException e) {}
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

    /* Current board */
    public Board board() {
        return board;
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
    public boolean isItMyTurn() {
        return board.activeColor()==gameInfo.color();
    }
}
