package no.ntnu.game.models;

import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInfo {
    private String gameid;
    private Player player;
    private Player opponent;
    private String started;
    private String ended;
    private List<String> moves;
    private String fen;
    private String winner;

    /**
     * Reads the values from json to this object
     * @param json from server
     * @param user User
     */
    public GameInfo(JsonValue json, User user) {
        gameid = json.has("gameid") ? json.getString("gameid") : null;
        started = json.has("started") ? json.getString("started") : null;
        ended = json.has("ended") ? json.getString("ended") : null;
        winner = json.has("winner") ? json.getString("winner") : null;
        fen = json.has("fen") ? json.getString("fen") : null;

        Player p1 = json.has("player1") ? new Player(json.get("player1")) : null;
        Player p2 = json.has("player2") ? new Player(json.get("player2")) : null;
        player = p1.userid().equals(user.userid()) ? p1 : p2;
        opponent = player.equals(p1) ? p2 : p1;

        String [] list = json.has("moves") ? json.get("moves").asStringArray() : null;
        moves = list!=null ? Arrays.asList(list) : new ArrayList<String>();
    }
    /* Gameid*/
    public String gameid() {
        return gameid;
    }

    /* This is you*/
    public Player player() {
        return player;
    }

    /* User playing against */
    public Player opponent() {
        return opponent;
    }

    /* Time game started */
    public String started() {
        return started;
    }

    /* Time game ended*/
    public String ended() {
        return ended;
    }

    /* Current or last position as fen string */
    public String fen() {
        return fen;
    }

    /* Winner of the game*/
    public String winner() {
        return winner;
    }

    /* Returns your color*/
    public Piece.Color color() {
        return (player.color().equals("white")) ? Piece.Color.WHITE : Piece.Color.BLACK;
    }

    /* Update gameinfo object with new fen*/
    public void update(String fen) {
        this.fen = fen;
        this.moves.add(fen);
    }

    /* Print */
    public String toString() {
        return "id: " + gameid + ", " + player.toString() + " vs " + opponent.toString() + ", fen: " + fen;
    }
}
