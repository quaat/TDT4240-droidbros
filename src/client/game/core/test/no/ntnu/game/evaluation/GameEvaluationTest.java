package no.ntnu.game.evaluation;

import org.junit.Test;

import no.ntnu.game.FEN;
import no.ntnu.game.models.Board;

import static org.junit.Assert.*;

/**
 * Created by thomash on 13.04.2017.
 */
public class GameEvaluationTest {

    @Test
    public void pieceValueTest() throws Exception
    {
        GameEvaluation eval = new PieceValue();

        String startPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        assertEquals(0.0, eval.score(FEN.toBoard(startPos)), 1.0E-10);

        // Missing black rook
        startPos = "1nbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        assertEquals(5.0, eval.score(FEN.toBoard(startPos)), 1.0E-10);

        // Missing white rook
        startPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR w - - 0 1";
        assertEquals(-5.0, eval.score(FEN.toBoard(startPos)), 1.0E-10);
    }

}