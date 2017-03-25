package no.ntnu.game.network;

import com.badlogic.gdx.utils.async.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.models.User;
import no.ntnu.game.util.NetworkObserver;

/**
 * Created by thomash on 19.03.2017.
 */

public abstract class NetworkCommunication {
    protected List<NetworkObserver> observers = new ArrayList<NetworkObserver>();
    private HostInfo hostInfo;
    public abstract void login(User user);

    NetworkCommunication(){
        super();
    }

    public void addObserver(NetworkObserver observer) {
        observers.add(observer);
    }

    NetworkCommunication(HostInfo hostInfo){
        super();
        this.hostInfo = hostInfo;
    }

    protected HostInfo hostInfo() {
        return this.hostInfo;

    }
    protected void emitConnected() {
        for (NetworkObserver observer : observers) {
            observer.onConnected();
        }
    }

    protected void emitLogin(String response) {
        for (NetworkObserver observer : observers) {
            observer.onLogin(response);
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
}

