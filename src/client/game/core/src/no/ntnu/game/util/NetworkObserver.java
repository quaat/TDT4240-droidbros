package no.ntnu.game.util;

/**
 * Created by thomash on 19.03.2017.
 */

public interface NetworkObserver {
    public void onConnected();
    public void onLogin(String response);
    public void onDisconnected();
    public void onError(String error);
}

