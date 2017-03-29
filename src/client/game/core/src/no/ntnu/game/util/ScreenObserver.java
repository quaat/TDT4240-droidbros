package no.ntnu.game.util;

import java.util.ArrayList;

import no.ntnu.game.models.Message;
import no.ntnu.game.models.ObservableModel;

/**
 * Created by Leppis on 23.03.2017.
 */

public interface ScreenObserver {
    public void onUpdate();
    public void onMessage();
    public void onQueueUpdate();
}
