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

    // Room test methods
    public void joinRoom(String roomid) {
        Gdx.app.log("ANDYPANDY", "Join room");
        Room room = new Room(roomid);
        socket.joinRoom(room);
    }

    public void sendMessage(String text) {
        Gdx.app.log("ANDYPANDY", "send message");
        // Todo set time to real time
        socket.sendMessage(new Message(time, model.getUser().getUserid(), text));
    }

    // Game test methods
    public void findGame() {
        Gdx.app.log("ANDYPANDY", "Find game");
        socket.findGame();
    }


    @Override
    public void onLogin(User user) {
        Gdx.app.log("ANDYPANDY", "Logged in: " + user.getUserid());
        model.setUser(user);
        Gdx.app.log("ANDYPANDY", "Token: " + user.getToken());
        socket.connect(user.getToken());
        viewController.setTestView2();
    }

    @Override
    public void onConnected(Room room) {
        Gdx.app.log("ANDYPANDY", room.toString());
        model.setRoom(room);
    }

    @Override
    public void onMessage(Message message) {
        Gdx.app.log("ANDYPANDY", "halla");
        model.addMessage(message);
    }

    @Override
    public void onQueueUpdate(String queue) {
        Gdx.app.log("ANDYPANDY", queue);
        model.setQueue(queue);
    }

    @Override
    public void onWelcome(String users, String queue) {
        Gdx.app.log("ANDYPANDY", "users online: " + users + ", queue num: " + queue);
        model.setQueue(queue);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onError(String error) {
        Gdx.app.log("ANDYPANDY", "ERROR: " + error);
    }
}
