package no.ntnu.game.util;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by thomash on 19.03.2017.
 */

public class JsonSerializer extends AbstractSerializer {
    @Override
    public Object read(String data) {
        JsonReader jsonReader = new JsonReader();
        return (Object)jsonReader.parse(data);
    }

    @Override
    public String write(Object object) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        return json.toJson(object);
    }
}
