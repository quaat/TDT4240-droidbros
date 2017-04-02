package no.ntnu.game.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.models.GameInfo;
import no.ntnu.game.models.Message;
import no.ntnu.game.models.Room;
import no.ntnu.game.models.User;
import no.ntnu.game.util.JsonSerializer;
import no.ntnu.game.util.NetworkObserver;

/**
 * Created by thomash on 19.03.2017.
 */

public abstract class NetworkCommunication {
    final String apiPath = "api";
    final String protocol = "http";

    JsonSerializer serializer = new JsonSerializer();

    protected List<NetworkObserver> observers = new ArrayList<NetworkObserver>();
    private HostInfo hostInfo;

    NetworkCommunication(NetworkObserver observer, HostInfo hostInfo){
        this.hostInfo = hostInfo;
        observers.add(observer);
    }

    public URL getRouteUrl(String route) {
        URL url = null;
        try {
            if (hostInfo().port()!=0) {
                url = new URL(this.protocol, hostInfo().hostAddress(), hostInfo().port(), route);
            }
            url = new URL(this.protocol, hostInfo().hostAddress(), route);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    protected HostInfo hostInfo() {
        return this.hostInfo;
    }

    protected void emitConnected() {
        for (NetworkObserver observer : observers) {
            observer.onConnected();
        }
    }

    protected void emitLogin(User user) {
        for (NetworkObserver observer : observers) {
            observer.onLogin(user);
        }
    }

    protected void emitDisconnected()
    {
        for (NetworkObserver observer : observers) {
            observer.onDisconnected();
        }
    }

    protected void emitError(String error){
        for (NetworkObserver observer : observers) {
            observer.onError(error);
        }
    }

    protected void emitUpdate(String users, String queue, String games){
        for (NetworkObserver observer : observers) {
            observer.onUpdate(users, queue, games);
        }
    }

    protected void emitStartGame(GameInfo gameInfo){
        for (NetworkObserver observer : observers) {
            observer.onStartGame(gameInfo);
        }
    }

    protected void emitNewMove(String state, String move, String turn){
        for (NetworkObserver observer : observers) {
            observer.onNewMove(state, move, turn);
        }
    }
}
