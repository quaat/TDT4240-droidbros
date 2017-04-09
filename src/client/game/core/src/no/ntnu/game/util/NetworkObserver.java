package no.ntnu.game.util;

import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.models.User;

/**
 * Created by thomash on 19.03.2017.
 */

public interface NetworkObserver {
    // http
    public void onLogin(User user);

    // socket
    public void onConnected();
    public void onUpdate(String users, String queue, String games);
    public void onStartGame(JsonValue response);
    public void onNewMove(String fen);
    public void onGameOver(JsonValue gameInfo);
    public void onDisconnected();

    // error
    public void onError(String error);
}

