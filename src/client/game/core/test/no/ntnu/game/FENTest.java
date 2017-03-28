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
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Board board = FEN.toBoard(fen);
        assertEquals(FEN.toFen(board), fen);
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

        assertEquals(FEN.toFen(board), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void fenToBoardToFen() throws Exception {
        String fen = "rnbqkbnr/pppp4/4pppp/8/8/8/PPPPPPPP/QNBRRBNK b Qq - 10 2";
        Board board = FEN.toBoard(fen);

        String copy = FEN.toFen(board);
        Board board2 = FEN.toBoard(copy);

        String copy2 = FEN.toFen(board2);
        assertEquals(fen, copy2);
    }

    @Test
    public void multifen() throws Exception {
        String fen = "8/8/2k5/8/8/8/8/K7 w - - 0 1";
        Board board = FEN.toBoard(fen);
        assertEquals(fen, FEN.toFen(board));

        fen = "6k1/3P4/2N1p3/PPR1PB2/1pn3p1/1R3b2/P1K5/8 w - - 0 1";
        board = FEN.toBoard(fen);
        assertEquals(fen, FEN.toFen(board));

        fen = "1N6/3pPRp1/2P2pb1/2Kp2r1/2B1q1kP/2p5/8/8 w - - 0 1";
        board = FEN.toBoard(fen);
        assertEquals(fen, FEN.toFen(board));

        fen = "7q/N5Qp/1p2k3/4p1p1/pP2R2P/7p/5PR1/K7 w - - 0 1";
        board = FEN.toBoard(fen);
        assertEquals(fen, FEN.toFen(board));

        fen = "4N3/qP1Nr3/2p1pK1p/4R1Pr/PPpkBp2/pPpq2B1/PPPp1n2/b1Q2b2 w - - 0 1";
        board = FEN.toBoard(fen);
        assertEquals(fen, FEN.toFen(board));
    }

}