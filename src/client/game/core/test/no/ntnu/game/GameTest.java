package no.ntnu.game;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by thomas on 4/11/17.
 */

public class GameTest {
    @Test
    public void playRandomGame() throws Exception {
        String fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        while (!GameAction.isMate(fen) && !GameAction.isDrawn(fen)) {
            List<Move> candiateMoves = GameAction.legalMoves(fen);
            int randomMove = ThreadLocalRandom.current().nextInt(0, candiateMoves.size());
            fen = FEN.toFen(GameAction.movePiece(fen, candiateMoves.get(randomMove)));
            System.out.println(fen);
        }
    }

}
