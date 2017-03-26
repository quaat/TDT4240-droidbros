package no.ntnu.game;

import org.junit.Test;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;

import static org.junit.Assert.*;

/**
 * Created by thomash on 26.03.2017.
 */
public class FENTest {
    @Test
    public void toBoard() throws Exception {
        // TODO: Implement method
    }

    @Test
    public void toFen() throws Exception {
        Board board = new Board();
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, 1).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
        }
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, board.rows()-2).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.BLACK));
        }

        assertEquals(FEN.toFen(board), "8/pppppppp/8/8/8/8/PPPPPPPP/8 w KQkq - 0 1");
    }

}