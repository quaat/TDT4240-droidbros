package no.ntnu.game.models;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.util.ScreenObserver;

public abstract class ObservableModel {
    protected List<ScreenObserver> observers = new ArrayList<ScreenObserver>();

    ObservableModel() {
        super();
    }

    public void addObserver(ScreenObserver observer) {
        observers.add(observer);
    }

    // Notify all views about change
    protected void emitChanges() {
        for (ScreenObserver observer : observers) {
            observer.onUpdate();
        }
    }

    // New message in model
    protected void emitMessage() {
        for (ScreenObserver observer : observers) {
            observer.onMessage();
        }
    }
}
