package no.ntnu.game;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 25.03.2017.
 * Class that defines one move
 */

public class Move {
    public Square from;
    public Square to;

    /**
     * Define a possible move
     * @param from - original position
     * @param to - destination
     */
    public Move(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Define a starting move, but leave the destination undefined. Use with
     * setDestination(Square dest)
     *
     * @param from - original position
     */
    public Move(Square from) {
        this.from = from;
    }

    /**
     * Set destination square
     * @param dest desination square
     */
    public void setDestination(Square dest) {
        this.to = dest;
    }

    /**
     *
     * @return originating square
     */
    public Square from() {
        return this.from;
    }

    /**
     *
     * @return destination square
     */
    public Square to() {
        return this.to;
    }

}
