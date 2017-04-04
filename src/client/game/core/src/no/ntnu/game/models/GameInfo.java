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
    private String started;
    private String ended;
    private List<String> moves;
    private String fen;
    private String turn;
    private String winner;

    public GameInfo(JsonValue response) {
        this.gameid = response.getString("gameid");
        this.player1 = new Player(response.get("player1"));
        this.player2 = new Player(response.get("player2"));
        this.started = response.getString("started");
        this.ended = response.getString("ended");
        this.moves = new ArrayList<String>();
        this.fen = response.getString("fen");
        this.turn = response.getString("turn");
        this.winner = response.getString("winner");
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

    public String getWinner() {
        return winner;
    }

    public String getTurn() {
        return turn;
    }

    public void update(String fen, String move, String turn) {
        this.fen = fen;
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
