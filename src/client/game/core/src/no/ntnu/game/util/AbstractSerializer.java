package no.ntnu.game.util;

/**
 * Created by thomash on 19.03.2017.
 */

public abstract class AbstractSerializer {
    public abstract Object read(String data);
    public abstract String write(Object object);
}
