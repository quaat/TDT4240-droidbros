package no.ntnu.game.movestrategy;

import org.junit.Test;

import java.util.List;

import no.ntnu.game.FEN;
import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;
import no.ntnu.game.GameAction;

import static org.junit.Assert.assertEquals;

/**
 * Created by thomas on 4/11/17.
 */
public class MoveStrategyTest {

    @Test
    public void testPawnMoveStrategy() throws Exception {
        MoveStrategy pawnStrategy = new SingleForwardStrategyDecorator(new DoubleForwardStrategyDecorator());
        Piece piece = new Piece(Piece.Type.PAWN, Piece.Color.WHITE, pawnStrategy);
        Board board = FEN.toBoard("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1");
        Square square = board.square(0, 1);
        square.setPiece(piece);
        for (Move m : GameAction.legalMoves(square)) {
            String s = m.toString();
            assertEquals(s, s);
        }
    }

    @Test
    public void testQueenMoveStrategy() throws Exception {
        MoveStrategy queenStrategy = new HorizontalStrategyDecorator(
                new VerticalStrategyDecorator(
                        new ULDiagonalStrategyDecorator(
                                new URDiagonalStrategyDecorator()
                        )
                )
        );
        Piece piece = new Piece(Piece.Type.QUEEN, Piece.Color.WHITE, queenStrategy);
        Board board = FEN.toBoard("8/8/8/8/8/8/8/8 w - - 0 1");
        Square square = board.square(4, 4);
        square.setPiece(piece);

        List<Move> moves = GameAction.legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 27);
    }

    @Test
    public void testRookMoveStrategy() throws Exception {
        MoveStrategy rookStrategy = new HorizontalStrategyDecorator(
                new VerticalStrategyDecorator()
        );
        Piece piece = new Piece(Piece.Type.ROOK, Piece.Color.WHITE, rookStrategy);
        Board board = FEN.toBoard("8/8/8/8/8/8/8/8 w - - 0 1");
        Square square = board.square(4, 4);
        square.setPiece(piece);
        List<Move> moves = GameAction.legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 14);
    }

    @Test
    public void testRookMoveStrategy2() throws Exception {
        MoveStrategy rookStrategy = new HorizontalStrategyDecorator(
                new VerticalStrategyDecorator()
        );
        Piece piece = new Piece(Piece.Type.ROOK, Piece.Color.WHITE, rookStrategy);
        Board board = FEN.toBoard("8/pppppppp/8/8/8/8/PPPPPPPP/8 w - - 0 1");
        Square square = board.square(4, 4);
        square.setPiece(piece);
        List<Move> moves = GameAction.legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 11);
    }

    @Test
    public void testKnightMoveStrategy() throws Exception {
        MoveStrategy ljump = new LJumpStrategyDecorator();
        Piece knight = new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE, ljump);
        Board board = FEN.toBoard("8/pppppppp/8/8/8/8/PPPPPPPP/8 w - - 0 1");
        Square square = board.square(3, 3);
        square.setPiece(knight);
        List<Move> moves = GameAction.legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 6);

        square = board.square(0, 2);
        square.setPiece(knight);
        moves = GameAction.legalMoves(square);
        numMoves = moves.size();
        assertEquals(numMoves, 3);
    }

    @Test
    public void bishopMoveStrategy() throws Exception {
        String standardStartPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        Square square = board.square(2, 0);
        assertEquals(square.piece().type(), Piece.Type.BISHOP);
        List<Move> moves = GameAction.legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 0);
    }
}