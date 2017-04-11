package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.FEN;
import no.ntnu.game.GameAction;
import no.ntnu.game.Move;
import no.ntnu.game.TypeErrorException;

public class GameModel extends ObservableModel {
    // Client info
    private User user; // User logged in as
    private Boolean connected; // This socket connected to server socket

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
     * todo - use correct method for updating board
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
        try { board = FEN.toBoard(fen());
        }catch(TypeErrorException e) {System.out.println(e);}
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
     * Gets update message from server
     * @param user user
     */
    public void setUser(User user) {
        this.user = new User(user);
        emitUserUpdate();
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

    public Board board() {
        return board;
    }

    public String fen() {
        return gameInfo.fen();
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
        return board.activeColor()==gameInfo.color();
    }
}
