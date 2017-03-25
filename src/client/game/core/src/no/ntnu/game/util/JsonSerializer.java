package no.ntnu.game.util;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class JsonSerializer {
    public JsonValue read(String data) {
        JsonReader jsonReader = new JsonReader();
        return jsonReader.parse(data);
    }

    public String write(Object object) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        return json.toJson(object);
    }
}
