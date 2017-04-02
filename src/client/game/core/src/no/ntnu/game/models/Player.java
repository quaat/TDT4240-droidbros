package no.ntnu.game.models;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Leppis on 02.04.2017.
 */

public class Player {
    private String id;
    private String name;
    private int level;
    private String color;

    public Player(String id, String name, int level, String color) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.color = color;
    }

    public Player(JsonValue response) {
        this.id = response.getString("id");
        this.name = response.getString("name");
        this.level = response.getInt("level");
        this.color = response.getString("color");
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String toString() {
        return name + " (" + color + ")";
    }
}
