package no.ntnu.game.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by thomash on 26.03.2017.
 */
public class SquareTest {
    @Test
    public void constructor() throws Exception {
        Square sq = new Square("A1");
        assertEquals(sq.col(), 0);
        assertEquals(sq.row(), 0);

        sq = new Square("H1");
        assertEquals(sq.col(), 7);
        assertEquals(sq.row(), 0);

        sq = new Square("A8");
        assertEquals(sq.col(), 0);
        assertEquals(sq.row(), 7);

        sq = new Square("H8");
        assertEquals(sq.col(), 7);
        assertEquals(sq.row(), 7);
    }

    @Test
    public void piece() throws Exception {

    }

    @Test
    public void setPiece() throws Exception {

    }

    @Test
    public void row() throws Exception {

    }

    @Test
    public void col() throws Exception {

    }

    @Test
    public void getAlgebraicCoordinate() throws Exception {

    }

}