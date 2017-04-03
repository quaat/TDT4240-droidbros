package no.ntnu.game.controllers;

import com.badlogic.gdx.Gdx;

import no.ntnu.game.MyGame;
import no.ntnu.game.models.GameInfo;
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
     * Find another player to play against
     */
    public void findGame() {
        socket.findGame();
    }

    /**
     * todo change this, just for testing
     * @param fen - fen string of game position
     * @param move - last move
     * @return
     */
    public boolean doMove(String fen, String move) {
        if (model.isItMyTurn()) {
            String turn = (model.getPlayer().getColor().equals("white")) ? "black" : "white";
            socket.doMove(model.getGameid(), fen, move, turn);
            return true;
        }
        return false;
    }

    /**
     * Resign and give up current game
     */
    public void resign() {
        socket.resign(model.getGameid());
    }

    /**
     * Gets login confirmation from server
     * Connects then to socket with given token
     * @param user - user object logged in
     */
    @Override
    public void onLogin(User user) {
        model.setUser(user);
        socket.connect(user.getToken());
        viewController.setTestView2();
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
    public void onStartGame(GameInfo gameInfo) {
        Gdx.app.log("ANDYPANDY", "start game");
        model.startGame(gameInfo);
        viewController.getTestView2().gameJoined();
    }

    /**
     * Gets new move from opponent
     * todo send both move and fen? turn is just for confirmation atm
     * @param fen fen string of game position
     * @param move last move done
     * @param turn color of next move
     */
    @Override
    public void onNewMove(String fen, String move, String turn) {
        Gdx.app.log("ANDYPANDY", "your turn");
        model.updateGame(fen, move, turn);
    }

    /**
     * Gets game over message from other server
     * todo add game object with winner, and all moves, etc.
     */
    @Override
    public void onGameOver() {
        Gdx.app.log("ANDYPANDY", "game over");
        model.endGame();
    }

    /**
     * Gets disconnect (from socket) message
     */
    @Override
    public void onDisconnected() {

    }

    /**
     * Error with communication
     */
    @Override
    public void onError(String error) {
        Gdx.app.log("ANDYPANDY", "ERROR: " + error);
    }
}
