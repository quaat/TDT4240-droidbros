package no.ntnu.game.util;

/**
 * Created by Leppis on 23.03.2017.
 */

public interface ScreenObserver {
    public void onUserUpdate();
    public void onServerUpdate();
    public void onNewMove();
    public void onGameUpdate();
}
