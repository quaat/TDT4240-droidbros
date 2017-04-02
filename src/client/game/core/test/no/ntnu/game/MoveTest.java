package no.ntnu.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;
import no.ntnu.game.models.Piece;
import no.ntnu.game.movestrategy.DoubleForwardStrategyDecorator;
import no.ntnu.game.movestrategy.HorizontalStrategyDecorator;
import no.ntnu.game.movestrategy.MoveStrategy;
import no.ntnu.game.movestrategy.SingleForwardStrategyDecorator;
import no.ntnu.game.movestrategy.StandardChessPawnMovement;
import no.ntnu.game.movestrategy.ULDiagonalStrategyDecorator;
import no.ntnu.game.movestrategy.URDiagonalStrategyDecorator;
import no.ntnu.game.movestrategy.VerticalStrategyDecorator;

import static org.junit.Assert.*;

/**
 * Created by thomash on 26.03.2017.
 */
public class MoveTest {
    private Board createBoard() {
        Board board = new Board();
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, 1).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
        }
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, board.rows()-2).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.BLACK));
        }

        board.square(0, 0).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        board.square(1, 0).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        board.square(2, 0).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        board.square(3, 0).setPiece(new Piece(Piece.Type.QUEEN, Piece.Color.WHITE));
        board.square(4, 0).setPiece(new Piece(Piece.Type.KING, Piece.Color.WHITE));
        board.square(5, 0).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        board.square(6, 0).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        board.square(7, 0).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        board.square(0, 7).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
        board.square(1, 7).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        board.square(2, 7).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        board.square(3, 7).setPiece(new Piece(Piece.Type.QUEEN, Piece.Color.BLACK));
        board.square(4, 7).setPiece(new Piece(Piece.Type.KING, Piece.Color.BLACK));
        board.square(5, 7).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        board.square(6, 7).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        board.square(7, 7).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
        return board;
    }

    private Board createBoardWithStrategy() {


        Board board = new Board();
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, 1).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.WHITE, new StandardChessPawnMovement()));
        }
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, board.rows()-2).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.BLACK, new StandardChessPawnMovement()));
        }
        return board;
    }

    @Test
    public void testMovement() throws Exception {
        Function<Square,Move> forwardMove = s -> {
            Board b = s.board();
            Square from = s;
            int forward=1;
            if (s.piece().color() == Piece.Color.BLACK) {
                forward = -1;
            }
            Square to = b.square(s.col(), s.row()+forward);
            return new Move(from, to);
        };

        Board b = createBoard();
        Square s = b.square(1,1);
        Move m = forwardMove.apply(s);
        assertEquals(m.to().row(), 2);

        Square s2 = b.square(1,6); // should be a black piece
        Move m2 = forwardMove.apply(s2);
        assertEquals(m2.to().row(), 5);
    }

    static public List<Move> legalMoves(Square square) {
        Piece piece = square.piece();
        if (piece == null) return null;
        List<Move> moves = new ArrayList<Move>();

        for (Function<Square, List<Move>> fn : piece.legalMoves())
        {
            moves.addAll(fn.apply(square));
        }
        return moves;
    }

    @Test
    public void testPawnMoveStrategy() throws Exception {
        MoveStrategy pawnStrategy = new SingleForwardStrategyDecorator(new DoubleForwardStrategyDecorator());
        Piece piece = new Piece(Piece.Type.PAWN, Piece.Color.WHITE, pawnStrategy);
        Board board = FEN.toBoard("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1");
        Square square  = board.square(0, 1);
        square.setPiece(piece);
        for (Move m : legalMoves(square)) {
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
        Piece piece = new Piece(Piece.Type.QUEEN,Piece.Color.WHITE,queenStrategy);
        Board board = FEN.toBoard("8/8/8/4Q3/8/8/8/8 w - - 0 1");
        Square square = board.square(4,4);

        for (Function<Square, List<Move>> fn : piece.legalMoves()) {
            List<Move> moves = fn.apply(square);
            for (Move m : moves) {
                String s = m.toString();
                assertEquals(s, "failed");
            }
        }
    }

    @Test
    public void movePieceOnBoard() throws Exception {
        // Start
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Board board = FEN.toBoard(standardStartPosition);

        board = GameAction.movePiece(board, new Move("d2", "d4"));
        String fen = FEN.toFen(board);
        assertEquals(fen, "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1");

        board = GameAction.movePiece(board, new Move("d7", "d5"));
        board = GameAction.movePiece(board, new Move("b1", "c3"));
        board = GameAction.movePiece(board, new Move("c7", "c6"));
        fen = FEN.toFen(board);

        assertEquals(fen, "rnbqkbnr/pp2pppp/2p5/3p4/3P4/2N5/PPP1PPPP/R1BQKBNR w KQkq - 0 3");
    }
}