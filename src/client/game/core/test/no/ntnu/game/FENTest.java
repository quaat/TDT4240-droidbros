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

}