package no.ntnu.game.models;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Leppis on 02.04.2017.
 */

public class Player {
    private String socketid;
    private String userid;
    private int level;
    private String color;
    private String fen;

    public Player(JsonValue response) {
        this.socketid = response.getString("socketid");
        this.userid = response.getString("userid");
        this.level = response.getInt("level");
        this.fen = response.getString("fen");
        this.color = response.getString("color");
    }

    public String userid() {
        return userid;
    }

    public int level() {
        return level;
    }

    public String color() {
        return color;
    }

    public String fen() {
        return fen;
    }

    public String toString() {
        return userid + " (" + color + ")";
    }
}
