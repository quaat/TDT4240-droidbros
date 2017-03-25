package no.ntnu.game.util;

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
    public void onConnected(Room room);
    public void onMessage(Message message);
    public void onDisconnected();

    // error
    public void onError(String error);
}

