package no.ntnu.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.MyGame;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.User;
import no.ntnu.game.network.HostInfo;
import no.ntnu.game.network.HttpCommunication;
import no.ntnu.game.network.SocketCommunication;
import no.ntnu.game.util.NetworkObserver;

public class GameController implements NetworkObserver{
    private GameModel model;
    private MyGame viewController;

    private HttpCommunication http;
    private SocketCommunication socket;
    private HostInfo hostInfo = new HostInfo("fast-crag-60223.herokuapp.com");

    /**
     * Controller communicating with server
     * @param model - model
     * @param viewController - view controller
     */
    public GameController(GameModel model, MyGame viewController) {
        this.model = model;
        this.viewController = viewController;

        http = new HttpCommunication(this, hostInfo);
        socket = new SocketCommunication(this, hostInfo);
    }

    /**
     * Log in with given params
     * @param username - username
     * @param password - password
     */
    public void login(String username, String password) {
        User user = new User(username, password);
        http.login(user);
    }

    /**
     * Change to menu view
     */
    public void toMenu() {
        viewController.setMenuView();
    }

    /**
     * Change to setup view
     */
    public void toSetup() {
        viewController.setSetupView();
    }

    /**
     * Change to game view
     */
    public void toGame() {
        viewController.setTestView2();
    }

    /**
     * Update start board position
     */
    public void updateUserBoard(String newFen) {
        socket.updateUserBoard(newFen);
    }

    /**
     * Find another player to play against
     */
    public void findGame() {
        socket.findGame();
    }

    /**
     * todo change this, just for testing
     * @param fen - fen string of game position
     * @return
     */
    public boolean doMove(String fen) {
        if (model.isItMyTurn()) {
            socket.doMove(fen);
            model.updateGame(fen);
            model.myTurn = false;
            return true;
        }
        return false;
    }

    /**
     * Resign and give up current game
     */
    public void resign() {
        socket.resign();
    }

    /**
     * Gets login confirmation from server
     * Connects then to socket with given token
     * @param user - user object logged in
     */
    @Override
    public void onLogin(User user) {
        model.setUser(user);
        socket.connect(user.token());
        viewController.setMenuView();
    }

    /**
     * Gets socket connection confirmation
     */
    @Override
    public void onConnected() {
    }

    /**
     * Gets update message from server
     * @param users users online
     * @param queue users searching for games
     * @param games games being played
     */
    @Override
    public void onUpdate(String users, String queue, String games) {
        model.updateStatistics(users, queue, games);
    }

    /**
     * Gets info about joined game
     * Update model with new game
     * @param gameInfo game information
     */
    @Override
    public void onStartGame(JsonValue gameInfo) {
        Gdx.app.log("ANDYPANDY", "start game");
        model.startGame(gameInfo);
        toGame(); // change view
    }

    /**
     * Gets new move from opponent
     * @param fen fen string of game position
     */
    @Override
    public void onNewMove(String fen) {
        Gdx.app.log("ANDYPANDY", "your turn");
        model.updateGame(fen);
        model.myTurn = true;
    }

    /**
     * Gets "game over" message from server
     * todo add game object with winner, and all moves, etc.
     */
    @Override
    public void onGameOver(String winner) {
        Gdx.app.log("ANDYPANDY", "game over");
        model.endGame(winner);
        // To winner screen / analysis ?
        toMenu();
    }

    /**
     * Gets disconnect (from socket) message
     */
    @Override
    public void onDisconnected() {
        Gdx.app.log("ANDYPANDY", "Server crash? reconnecting when up!");
        toMenu();
    }

    /**
     * Error with communication
     */
    @Override
    public void onError(String error) {
        Gdx.app.log("ANDYPANDY", "ERROR: " + error);
    }
}
