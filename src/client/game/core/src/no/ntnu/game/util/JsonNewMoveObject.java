package no.ntnu.game.util;

/**
 * Created by Leppis on 02.04.2017.
 */

public class JsonNewMoveObject {
    private String fen;
    private String move;
    private String turn;

    public JsonNewMoveObject(String fen, String move, String turn) {
        this.fen = fen;
        this.move = move;
        this.turn = turn;
    }
}
