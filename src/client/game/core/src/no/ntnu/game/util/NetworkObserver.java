package no.ntnu.game.util;

import java.util.List;

import no.ntnu.game.models.GameInfo;
import no.ntnu.game.models.Message;
import no.ntnu.game.models.Room;
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
    public void onStartGame(GameInfo gameInfo);
    public void onNewMove(String state, String move, String turn);
    public void onDisconnected();

    // error
    public void onError(String error);
}

