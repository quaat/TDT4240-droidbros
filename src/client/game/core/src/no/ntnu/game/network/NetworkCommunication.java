package no.ntnu.game.network;

import com.badlogic.gdx.utils.JsonValue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.models.User;
import no.ntnu.game.util.JsonSerializer;
import no.ntnu.game.util.NetworkObserver;

public abstract class NetworkCommunication {
    final String apiPath = "api";
    final String protocol = "http";

    JsonSerializer serializer = new JsonSerializer();

    protected List<NetworkObserver> observers = new ArrayList<>();
    private HostInfo hostInfo;

    NetworkCommunication(HostInfo hostInfo){
        this.hostInfo = hostInfo;
    }

    public void addObserver(NetworkObserver observer) {
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

    // Http

    protected void emitRegister() {
        for (NetworkObserver observer : observers) {
            observer.onRegister();
        }
    }

    protected void emitLogin(User user) {
        for (NetworkObserver observer : observers) {
            observer.onLogin(user);
        }
    }

    protected void emitGetUser(String token, User user) {
        for (NetworkObserver observer : observers) {
            observer.onGetUser(token, user);
        }
    }

    protected void emitGetGames(/* something */) {
        for (NetworkObserver observer : observers) {
            observer.onGetGames(/* something */);
        }
    }

    protected void emitChangedPassword() {
        for (NetworkObserver observer : observers) {
            observer.onChangedPassword();
        }
    }

    protected void emitChangedFen(String token, String fen) {
        for (NetworkObserver observer : observers) {
            observer.onChangedFen(token, fen);
        }
    }

    protected void emitDeletedUser() {
        for (NetworkObserver observer : observers) {
            observer.onDeletedUser();
        }
    }

    // Sockets

    protected void emitConnected() {
        for (NetworkObserver observer : observers) {
            observer.onConnected();
        }
    }

    protected void emitDisconnected() {
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

    protected void emitStartGame(JsonValue response){
        for (NetworkObserver observer : observers) {
            observer.onStartGame(response);
        }
    }

    protected void emitNewMove(String fen){
        for (NetworkObserver observer : observers) {
            observer.onNewMove(fen);
        }
    }

    protected void emitGameOver(JsonValue gameInfo) {
        for (NetworkObserver observer : observers) {
            observer.onGameOver(gameInfo);
        }
    }
}
