package no.ntnu.game.network;

import com.badlogic.gdx.utils.JsonValue;

import org.junit.Before;
import org.junit.Test;

import no.ntnu.game.models.User;
import no.ntnu.game.util.NetworkObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Leppis on 12.04.2017.
 * Not really a unit test
 * This test http requests to server
 */

public class HttpCommunicationTest implements NetworkObserver{
    private final static String url = "fast-crag-60223.herokuapp.com/";
    private static HostInfo hostInfo = new HostInfo(url);

    private HttpCommunication http = null;

    // Resetting
    User user = null;

    boolean registerSuccess = false;
    boolean loginSuccess = false;

    boolean userChanged = false;
    boolean fenChanged = false;
    boolean passwordChanged = false;

    boolean getUserSuccess = false;
    boolean getGamesSuccess = false;
    boolean userDeleted = false;

    @Before
    public void reset() {
        http = new HttpCommunication(hostInfo);
        http.addObserver(this);

        user = null;

        registerSuccess = false;
        loginSuccess = false;

        userChanged = false;
        fenChanged = false;
        passwordChanged = false;

        getUserSuccess = false;
        getGamesSuccess = false;
        userDeleted = false;
    }

    public void sleep(int num) {
        try {Thread.sleep(num);} catch (InterruptedException e) { }
    }

    public void login() {
        http.login("admin", "admin");
        sleep(2000);
        if (user==null) {
            assertTrue(false); // fail to login
            return;
        }
    }

    @Test(timeout = 10000)
    public void TestRegisterLoginDelete() {
        http.register("testy", "password", "testy", "testy");
        sleep(2000);
        if (!registerSuccess) assertTrue(false); // abort

        http.login("testy", "password");
        sleep(2000);
        if (user==null) assertTrue(false); // abort

        http.deleteUser(user.token(), "password");
        sleep(2000);

        assertEquals(user.userid(), "testy");
        assertEquals(registerSuccess, true);
        assertEquals(loginSuccess, true);
        assertEquals(userDeleted, true);
    }

    @Test(timeout = 10000)
    public void TestChangeFen() {
        login();

        http.changeFen(user.token(), "new fen");
        sleep(2000);

        assertEquals(loginSuccess, true);
        assertEquals(fenChanged, true);
    }

    @Test(timeout = 10000)
    public void TestPasswordChange() {
        login();

        http.changePassword(user.token(), "admin", "admin");
        sleep(2000);

        assertEquals(loginSuccess, true);
        assertEquals(passwordChanged, true);
    }

    @Test(timeout = 10000)
    public void TestGetUser() {
        login();

        http.getUserInformation(user.token());
        sleep(2000);

        assertEquals(loginSuccess, true);
        assertEquals(getUserSuccess, true);
    }

    @Test(timeout = 10000)
    public void TestGetGames() {
        // todo
    }

    @Override
    public void onRegister() {
        registerSuccess = true;
    }

    @Override
    public void onLogin(User user) {
        this.user = user;
        loginSuccess = true;
    }

    @Override
    public void onGetUser(User user) {
        this.user = user;
        getUserSuccess = true;
    }

    @Override
    public void onGetGames() {
        getGamesSuccess = true;
    }

    @Override
    public void onChangedPassword() {
        passwordChanged = true;
    }

    @Override
    public void onChangedFen() {
        fenChanged= true;
    }

    @Override
    public void onDeletedUser() {
        userDeleted = true;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onUpdate(String users, String queue, String games) {

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

    }

    @Override
    public void onError(String error) {
        System.out.println("Error: " + error);
    }
}
