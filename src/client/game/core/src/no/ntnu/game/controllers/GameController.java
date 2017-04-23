package no.ntnu.game.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.MyGame;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.User;
import no.ntnu.game.network.HostInfo;
import no.ntnu.game.network.HttpCommunication;
import no.ntnu.game.network.SocketCommunication;
import no.ntnu.game.util.NetworkObserver;

public class GameController extends AbstractController implements NetworkObserver{
    private MyGame myGame;

    private HttpCommunication http;
    private SocketCommunication socket;
    private HostInfo hostInfo = new HostInfo("fast-crag-60223.herokuapp.com");

    /**
     * Controller combining model with views, plus communicating with server
     * @param model - model
     * @param myGame - view controller
     */
    public GameController(GameModel model, MyGame myGame) {
        super(model);
        this.myGame = myGame;

        http = new HttpCommunication(hostInfo);
        socket = new SocketCommunication(hostInfo);

        http.addObserver(this);
        socket.addObserver(this);
    }

    /**
     * Change to register view
     */
    public void toLogin() {
        myGame.setLoginView();
    }

    /**
     * Change to register view
     */
    public void toRegister() {
        myGame.setRegisterView();
    }

    /**
     * Change to menu view
     */
    public void toMenu() {
        myGame.setMenuView();
    }

    /**
     * Change to setup view
     */
    public void toTutorial() {
        myGame.setTutorialView();
    }

    /**
     * Change to register view
     */
    public void toSetting() {
        myGame.setSettingView();
    }

    /**
     * Change to register view
     */
    public void toAbout() {
        myGame.setAboutView();
    }

    /**
     * Change to game view
     */
    public void toGame() {
        myGame.setGameView();
    }

    /**
     * Change to game view
     */
    public void toFen() {
        myGame.setFenView();
    }

    /**
     * Change to game view
     */
    public void toBoard() {
        myGame.setTestView2();
    }

    /**
     * Change to game ended view
     */
    public void toGameEnded() {
        myGame.setGameEndedView();
    }

    public Game game() {return myGame;}
    /**
     * Sends request for register a new user
     * @param userid - String
     * @param password - String
     * @param name - String
     * @param email - String
     */
    public boolean register(String userid, String password, String name, String email) {
        // todo validate input
        http.register(userid, password, name, email);
        return true;
    }

    /**
     * Sends request for login
     * @param userid - String
     * @param password - String
     */
    public boolean login(String userid, String password) {
        // todo validate input
        http.login(userid, password);
        return true;
    }

    /**
     * Send request for user information
     */
    public void getUserInformation() {
        http.getUserInformation(model.user().token());
    }

    /**
     * Send request for get previous games
     * todo not complete, dont use
     */
    public void getGames() {
        http.getGames(model.user().token());
    }

    /**
     * Send request for change password
     * @param oldPassword - String
     * @param newPassword - String
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        // todo validate input (server handles oldPassword check)
        http.changePassword(model.user().token(), oldPassword, newPassword);
        return true;
    }

    /**
     * Send request for change fen
     * @param newFen String
     */
    public void changeFen(String newFen) {
        // todo validate input
        System.out.println("to server token before change: " +model.user().token());
        http.changeFen(model.user().token(), newFen);
    }

    /**
     * Find another player to play against
     */
    public void findGame() {
        socket.findGame();
    }

    /**
     * Leave queue
     */
    public void leaveQueue() {
        socket.leaveQueue();
    }

    /**
     * New move (fen string) from client
     * @param fen String
     */
    public void doMove(String fen) {
        //model.addMove(fen);
        socket.doMove(fen);
    }

    /**
     * Resign and give up current game
     */
    public void resign() {
        socket.resign();
    }

    /**
     * Gets register confirmation from server
     */
    @Override
    public void onRegister() {
        toLogin();
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
        myGame.setMenuView();
    }

    /**
     * Gets user information from server
     * @param user User
     */
    @Override
    public void onGetUser(String token, User user) {
        model.setUser(user);
        model.user().setToken(token);
        System.out.println("after end game: " );
    }

    /**
     * Gets previous games from server
     */
    @Override
    public void onGetGames(/* Something */) {
        // todo save
    }

    /**
     * Gets changed password confirmation
     */
    @Override
    public void onChangedPassword() {
        // todo visual
    }

    /**
     * Gets changed fen confirmation
     */
    @Override
    public void onChangedFen(String token, String fen) {
        System.out.println("incomming token: " + token);
        model.user().setFen(fen);
        model.user().setToken(token);
        // reset socket to update info.
        socket.disconnect();
        socket.connect(model.user().token());
    }

    @Override
    public void onDeletedUser() {
        // todo to login screen
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
        model.startGame(gameInfo);
        toBoard(); // change view
    }

    /**
     * Gets new move from opponent
     * @param fen fen string of game position
     */
    @Override
    public void onNewMove(String fen) {
        model.updateGame(fen);
    }

    /**
     * Gets 'game over' message from server
     */
    @Override
    public void onGameOver(JsonValue gameInfo) {
        model.endGame(gameInfo);
        // To winner screen / analysis ?
        http.getUserInformation(model.user().token());
        toGameEnded();
    }

    @Override
    public void onReconnect(JsonValue gameInfo) {
        model.startGame(gameInfo);
        toBoard();
    }

    /**
     * Gets disconnect (from socket) message
     */
    @Override
    public void onDisconnected() {
        toMenu();
    }

    /**
     * Error with communication
     */
    @Override
    public void onError(String error) {
        model.updateError(error);
    }


    public void playComputer() {
        myGame.playComputer();
    }
}
