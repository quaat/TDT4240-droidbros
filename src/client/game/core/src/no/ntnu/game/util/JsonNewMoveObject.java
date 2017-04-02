package no.ntnu.game.util;

/**
 * Created by Leppis on 02.04.2017.
 */

public class JsonNewMoveObject {
    private String id;
    private String state;
    private String move;
    private String turn;

    public JsonNewMoveObject(String id, String state, String move, String turn) {
        this.id = id;
        this.state = state;
        this.move = move;
        this.turn = turn;
    }
}
