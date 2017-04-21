package no.ntnu.game.models;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.util.ScreenObserver;

public abstract class ObservableModel {
    private List<ScreenObserver> observers = new ArrayList<>();

    ObservableModel() {
        super();
    }

    public void addObserver(ScreenObserver observer) {
        observers.add(observer);
    }

    /* Server info updated */
    protected void emitServerUpdate() {
        for (ScreenObserver observer : observers) {
            observer.onServerUpdate();
        }
    }

    /* User info updated */
    protected void emitUserUpdate() {
        for (ScreenObserver observer : observers) {
            observer.onUserUpdate();
        }
    }

    /* User info updated */
    protected void emitGameUpdate() {
        for (ScreenObserver observer : observers) {
            observer.onGameUpdate();
        }
    }

    /* New move */
    protected void emitNewMove() {
        for (ScreenObserver observer : observers) {
            observer.onNewMove();
        }
    }

    /* Error */
    protected void emitError() {
        for (ScreenObserver observer : observers) {
            observer.onError();
        }
    }
}
