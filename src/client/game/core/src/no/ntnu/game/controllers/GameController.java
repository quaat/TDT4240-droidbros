package no.ntnu.game.controllers;

import com.badlogic.gdx.Gdx;

import java.util.List;

import no.ntnu.game.MyGame;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Message;
import no.ntnu.game.models.Room;
import no.ntnu.game.models.User;
import no.ntnu.game.network.HostInfo;
import no.ntnu.game.network.HttpCommunication;
import no.ntnu.game.network.SocketCommunication;
import no.ntnu.game.util.NetworkObserver;
import no.ntnu.game.network.NetworkCommunication;

public class GameController implements NetworkObserver{

    // change to real time
    String time = "13:37";

    GameModel model;
    MyGame viewController;

    HttpCommunication http;
    SocketCommunication socket;
    HostInfo hostInfo = new HostInfo("fast-crag-60223.herokuapp.com");

    public GameController(GameModel model, MyGame viewController) {
        this.model = model;
        this.viewController = viewController;

        http = new HttpCommunication(this, hostInfo);
        socket = new SocketCommunication(this, hostInfo);
    }

    public void login(String username, String password) {
        User user = new User(username, password);
        http.login(user);
        // wait for response, see onLogin()
    }

    // Find new game
    public void findGame() {
        Gdx.app.log("ANDYPANDY", "Find game");
        socket.findGame();
    }

    // Do a move
    public void doMove(String state, String move) {
        if (model.isItMyTurn()) {
            Gdx.app.log("ANDYPANDY", "I did a move");
            socket.doMove(model.getGameid(), state, move);
        }
    }

    @Override
    public void onLogin(User user) {
        Gdx.app.log("ANDYPANDY", "Logged in: " + user.getUserid());
        model.setUser(user);
        //Gdx.app.log("ANDYPANDY", "Token: " + user.getToken());
        socket.connect(user.getToken());
        viewController.setTestView2();
    }

    @Override
    public void onConnected() {
        Gdx.app.log("ANDYPANDY", "connected");
    }

    @Override
    public void onUpdate(String users, String queue, String games) {
        model.updateStatistics(users, queue, games);
    }

    @Override
    public void onStartGame(String gameid, String opponent, String color) {
        Gdx.app.log("ANDYPANDY", "start game");
        model.startGame(gameid, opponent, color);
        viewController.getTestView2().gameJoined();
    }

    @Override
    public void onNewMove(String state, String move) {
        Gdx.app.log("ANDYPANDY", "start game");
        model.updateGame(state, move);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onError(String error) {
        Gdx.app.log("ANDYPANDY", "ERROR: " + error);
    }
}
