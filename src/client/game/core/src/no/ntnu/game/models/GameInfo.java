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
    private Player player;
    private Player opponent;
    private String started;
    private String ended;
    private List<String> moves;
    private String fen;
    private String winner;

    public GameInfo(JsonValue response, User user) {
        this.gameid = response.getString("gameid");
        Player p1 = new Player(response.get("player1"));
        Player p2 = new Player(response.get("player2"));
        if (p1.userid().equals(user.userid())) {
            this.player = p1;
            this.opponent = p2;
        } else {
            this.player = p2;
            this.opponent = p1;
        }
        this.started = response.getString("started");
        this.ended = response.getString("ended");
        this.moves = new ArrayList<String>();
        this.winner = response.getString("winner");
        this.fen = response.getString("fen");
    }

    public String gameid() {
        return gameid;
    }

    public Player player() {
        return player;
    }

    public Player opponent() {
        return opponent;
    }

    public String started() {
        return started;
    }

    public String ended() {
        return ended;
    }

    public String fen() {
        return fen;
    }

    public String winner() {
        return winner;
    }

    public Piece.Color color() {
        return (player.color().equals("white")) ? Piece.Color.WHITE : Piece.Color.BLACK;
    }

    public void update(String fen) {
        this.fen = fen;
        this.moves.add(fen);
    }

    public String toString() {
        return "id: " + gameid + ", " + player.toString() + " vs " + opponent.toString() + ", fen: " + fen;
    }
}
