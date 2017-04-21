package no.ntnu.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;
import no.ntnu.game.models.Piece;

import static org.junit.Assert.*;

/**
 * Created by thomash on 26.03.2017.
 */

/*
public class MoveTest {
    private Board createBoard() {
        Board board = new Board();
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, 1).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.WHITE, null));
        }
        for (int col = 0; col < board.cols(); col++) {
            board.square(col, board.rows()-2).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.BLACK, null));
        }

        board.square(0, 0).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.WHITE, null));
        board.square(1, 0).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE, null));
        board.square(2, 0).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.WHITE, null));
        board.square(3, 0).setPiece(new Piece(Piece.Type.QUEEN, Piece.Color.WHITE, null));
        board.square(4, 0).setPiece(new Piece(Piece.Type.KING, Piece.Color.WHITE, null));
        board.square(5, 0).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.WHITE, null));
        board.square(6, 0).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE, null));
        board.square(7, 0).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.WHITE, null));
        board.square(0, 7).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.BLACK, null));
        board.square(1, 7).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK, null));
        board.square(2, 7).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.BLACK, null));
        board.square(3, 7).setPiece(new Piece(Piece.Type.QUEEN, Piece.Color.BLACK, null));
        board.square(4, 7).setPiece(new Piece(Piece.Type.KING, Piece.Color.BLACK, null));
        board.square(5, 7).setPiece(new Piece(Piece.Type.BISHOP, Piece.Color.BLACK, null));
        board.square(6, 7).setPiece(new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK, null));
        board.square(7, 7).setPiece(new Piece(Piece.Type.ROOK, Piece.Color.BLACK, null));
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


    @Test
    public void findKing() throws Exception {
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        Square compare = board.square(4, 0);
        List<Square> squares = GameAction.findAllPieces(board, Piece.Type.KING, Piece.Color.WHITE);
        assertEquals(squares.size(), 1);
        assertTrue(squares.get(0) == compare);
    }

    @Test
    public void findCheck() throws Exception {
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        final Square kingSquare = GameAction.findAllPieces(board, Piece.Type.KING, Piece.Color.WHITE).get(0);
        List<Square> allOpponents =  GameAction.findAllPieces(board, Piece.Color.BLACK);
        boolean threat = false;
        for (Square opponent : allOpponents) {
            List<Move> threats = GameAction.legalMoves(opponent).stream()
                    .filter(s -> s.to() == kingSquare)
                    .collect(Collectors.toList());
            if (threats.size() > 0) {
                threat = true;
                break;
            }
        }
        assertEquals(threat, false);

        board = FEN.toBoard("rnb1kbnr/pp1ppppp/2p5/q7/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3");
        final Square newKingSquare = GameAction.findAllPieces(board, Piece.Type.KING, Piece.Color.WHITE).get(0);
        allOpponents =  GameAction.findAllPieces(board, Piece.Color.BLACK);
        for (Square opponent : allOpponents) {
            if (GameAction.legalMoves(opponent).stream()
                    .filter(s -> s.to() == newKingSquare)
                    .collect(Collectors.toList()).size() > 0) {
                threat = true;
                break;
            }
        }
        assertEquals(threat, true);
    }

    @Test
    public void checkMate() throws Exception {
        // Check every move if king is still in check:
        // For every move, check if there exist an opponent move that threatens the king
        String position = "r7/2pQ4/p1Ppkr2/4p3/p3P3/8/PqPb1PPP/R4RK1 b - - 7 25";
        assertTrue(GameAction.isMate(position));

        position = "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3";
        assertTrue(GameAction.isMate(position));

        position = "rnbq1rk1/ppp1ppbp/5np1/3p4/3P4/N1P2N1P/PP2PPP1/R1BQKB1R w KQ - 1 6";
        assertFalse(GameAction.isMate(position));
    }

    @Test
    public void boardAndPieces() throws Exception {
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        List<Square> whitePieces = board.allSquares().stream()
                .filter(s ->
                    ((s.piece() != null) && (s.piece().color() == Piece.Color.WHITE)))
                .collect(Collectors.toList());
        assertEquals(whitePieces.size(), 16);

        List<Square> blackPawns = board.allSquares().stream()
                .filter(s -> s.piece() != null && s.piece().color() == Piece.Color.BLACK && s.piece().type() == Piece.Type.PAWN)
                .collect(Collectors.toList());
        assertEquals(blackPawns.size(), 8);
    }
}
*/