package no.ntnu.game.models;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leppis on 02.04.2017.
 */

public class GameInfo {
    private String gameid;
    private Player player1;
    private Player player2;
    private String start;
    private List<String> moves;
    private String state;
    private String turn;

    public GameInfo(JsonValue response) {
        this.gameid = response.getString("gameid");
        this.player1 = new Player(response.get("player1"));
        this.player2 = new Player(response.get("player2"));
        this.start = response.getString("start");
        this.moves = new ArrayList<String>();
        this.state = response.getString("state");
        this.turn = response.getString("turn");
    }

    public Player getOpponent(String name) {
        return (name.equals(player1.getName())) ? player2 : player1;
    }

    public Player getPlayer(String name) {
        return (name.equals(player1.getName())) ? player1 : player2;
    }

    public String getGameid() {
        return gameid;
    }

    public String getState() {
        return state;
    }

    public String getTurn() {
        return turn;
    }

    public void update(String state, String move, String turn) {
        this.state = state;
        this.moves.add(move);
        this.turn = turn;
    }

    public boolean isItMyTurn(String name) {
        return getPlayer(name).getColor().equals(turn);
    }

    public String toString() {
        return "id: " + gameid + ", " + player1.toString() + " vs " + player2.toString() + ", turn: " + turn;
    }
}
