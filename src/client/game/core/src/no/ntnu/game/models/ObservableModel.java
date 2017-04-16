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
        observers.forEach(ScreenObserver::onServerUpdate);
    }

    /* User info updated */
    protected void emitUserUpdate() {
        observers.forEach(ScreenObserver::onUserUpdate);
    }

    /* User info updated */
    protected void emitGameUpdate() {
        observers.forEach(ScreenObserver::onGameUpdate);
    }

    /* New move */
    protected void emitNewMove() {
        observers.forEach(ScreenObserver::onNewMove);
    }
}
