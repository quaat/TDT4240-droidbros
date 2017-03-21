package no.ntnu.game.util;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by thomash on 19.03.2017.
 */

public abstract class AbstractSerializer {
    public abstract Object read(String data);
    public abstract String write(Object object);
}
