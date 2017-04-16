package no.ntnu.game.network;

import com.badlogic.gdx.utils.JsonValue;

import org.junit.Before;
import org.junit.Test;

import no.ntnu.game.models.User;
import no.ntnu.game.util.NetworkObserver;

import static org.junit.Assert.assertEquals;

/**
 * Created by Leppis on 12.04.2017.
 * Not really a unit test
 * This test socket connection to server
 */

public class SocketCommunicationTest implements NetworkObserver{
    private final static String url = "fast-crag-60223.herokuapp.com/";
    private static HostInfo hostInfo = new HostInfo(url);

    private HttpCommunication http = null;
    private SocketCommunication socket = null;
    private SocketCommunication socket2 = null;

    private User user = null;
    private String users = null;
    private String queue = null;

    private boolean loginSuccess = false;

    private boolean connected = false;
    private boolean disconnected = false;

    private boolean updated = false;

    @Before
    public void reset() {
        http = new HttpCommunication(hostInfo);
        http.addObserver(this);

        socket = new SocketCommunication(hostInfo);
        socket.addObserver(this);


        user = null;
        users = null;
        queue = null;

        loginSuccess = false;

        connected = false;
        disconnected = false;

        updated = false;
    }

    public void sleep(int num) {
        try {Thread.sleep(num);} catch (InterruptedException e) { }
    }

    public void login() {
        http.login("admin", "admin");
    }

    public void connect() {
        socket.connect(user.token());
    }

    public void disconnect() {
        socket.disconnect();
    }

    @Test(timeout = 10000)
    public void testConnection() {
        login();
        sleep(2000);
        connect();
        sleep(2000);
        socket.findGame();
        sleep(2000);
        disconnect();
        sleep(1000);

        assertEquals(users, "1");
        assertEquals(queue, "1");
        assertEquals(loginSuccess, true);
        assertEquals(connected, true);
        assertEquals(disconnected, true);
    }



    @Override
    public void onRegister() {

    }

    @Override
    public void onLogin(User user) {
        this.user = user;

        loginSuccess = true;
    }

    @Override
    public void onGetUser(User user) {

    }

    @Override
    public void onGetGames() {

    }

    @Override
    public void onChangedPassword() {

    }

    @Override
    public void onChangedFen() {

    }

    @Override
    public void onDeletedUser() {

    }

    @Override
    public void onConnected() {
        connected = true;
    }

    @Override
    public void onUpdate(String users, String queue, String games) {
        updated = true;
        this.users = users;
        this.queue = queue;
    }

    @Override
    public void onStartGame(JsonValue response) {
    }

    @Override
    public void onNewMove(String fen) {
    }

    @Override
    public void onGameOver(JsonValue gameInfo) {
    }

    @Override
    public void onDisconnected() {
        disconnected = true;
    }

    @Override
    public void onError(String error) {
        System.out.println("Error: " + error);
    }
}
