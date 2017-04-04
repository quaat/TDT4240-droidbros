package no.ntnu.game.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by thomash on 26.03.2017.
 */
public class PieceTest {

    private Piece p;
    public PieceTest() {
        this.p = new Piece(Piece.Type.BISHOP, Piece.Color.BLACK, null);
        String v = "foo";
        this.p.setObject(v);
    }

    @Test
    public void type() throws Exception {
        assertEquals(p.type(), Piece.Type.BISHOP);
    }

    @Test
    public void color() throws Exception {
        assertEquals(p.color(), Piece.Color.BLACK);
    }

    @Test
    public void setObject() throws Exception {
        String obj = "SOME OBJ";
        p.setObject(obj);
        String s = (String)p.object();
        assertTrue(0 == s.compareTo("SOME OBJ"));
    }

    @Test
    public void object() throws Exception {
        String s = (String)p.object();
        assertTrue(0 == s.compareTo("foo"));
    }

}