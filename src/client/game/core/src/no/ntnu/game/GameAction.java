package no.ntnu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import no.ntnu.game.FEN;
import no.ntnu.game.Move;
import no.ntnu.game.TypeErrorException;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 28.03.2017.
 */

public class GameAction {
    static Board movePiece(Board board, Move move) throws TypeErrorException {
        Square origin = board.square(move.from().col(), move.from().row());
        Square dest = board.square(move.to().col(), move.to().row());
        Piece piece = origin.piece();
        dest.setPiece(piece);
        origin.setPiece(null);
        if (board.activeColor() == Piece.Color.BLACK) {
            board.incrementFullmoveClock();
        }
        board.flipActiveColor();
        return board;
    }

    /**
     * Find all legal moves from a collection of Squares
     * @param squares
     * @return
     */
    static public List<Move> legalMoves(List<Square> squares) {
        List<Move> moves = new ArrayList<Move>();
        for (Square square : squares) {
            moves.addAll(legalMoves(square));
        }
        return moves;
    }

    static public boolean isCheck(Board board, Piece.Color activeColor) {
        // Find the square of the opponents king
        Square opponentKing =  board.allSquares().stream()
                .filter(s -> s.piece() != null && s.piece().color() != activeColor && s.piece().type() == Piece.Type.KING)
                .collect(Collectors.toList()).get(0);

        // Update all active players pieces
        List<Square> allActive = board.allSquares().stream()
                .filter(s -> s.piece() != null && s.piece().color() == activeColor)
                .collect(Collectors.toList());

        // If any pieces may move to the opponent king square, it is check.
        boolean isCheck = GameAction.legalMoves(allActive).stream()
                .filter(s -> s.to() == opponentKing)
                .collect(Collectors.toList()).size() > 0;

        return isCheck;
    }

    static private Board copy(Board board) throws Exception {
        return FEN.toBoard(FEN.toFen(board));
    }
    /**
     * Find all legal moves from a particular square
     * @param square
     * @return
     */
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
    /**
     *
     * @param board
     * @return All squares with pieces
     */
    static List<Square> findAllPieces(final Board board) {
        List<Square> squares = new ArrayList<Square>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                Square square = board.square(col, row);
                if (square.piece() != null) {
                    squares.add(square);
                }
            }
        }
        return squares;
    }

    /**
     *
     * @param board
     * @param color
     * @return All squares with pieces of a given color
     */
    static List<Square> findAllPieces(final Board board, final Piece.Color color) {
        List<Square> squares = new ArrayList<Square>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                Square square = board.square(col, row);
                if (square != null) {
                    if (square.piece() != null) {
                        if (square.piece().color() == color) {
                            squares.add(square);
                        }
                    }
                }
            }
        }
        return squares;
    }

    /**
     *
     * @param board
     * @param type
     * @return All squares with pieces of a given type
     */
    static List<Square> findAllPieces(final Board board, final Piece.Type type) {
        List<Square> squares = new ArrayList<Square>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                Square square = board.square(col, row);
                if (square != null) {
                    if (square.piece() != null) {
                        if (square.piece().type() == type) {
                            squares.add(square);
                        }
                    }
                }
            }
        }
        return squares;
    }

    /**
     *
     * @param board
     * @param type
     * @param color
     * @return All squares with pieces of a given type and color
     */
    static List<Square> findAllPieces(final Board board, final Piece.Type type, final Piece.Color color) {
        List<Square> squares = new ArrayList<Square>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                Square square = board.square(col, row);
                if (square != null) {
                    Piece piece = square.piece();
                    if (piece != null) {
                        if (piece.type() == type && piece.color() == color)
                            squares.add(square);
                    }
                }
            }
        }
        return squares;
    }

    /**
     *
     * @param board
     * @param type
     * @param color
     * @return All pieces except those of a given type and color
     */
    static List<Square> findAllPiecesExcept(final Board board, final Piece.Type type, final Piece.Color color) {
        List<Square> squares = new ArrayList<Square>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                Square square = board.square(col, row);
                if (square != null) {
                    Piece piece = square.piece();
                    if (piece != null) {
                        if (piece.type() != type && piece.color() != color)
                            squares.add(square);
                    }
                }
            }
        }
        return squares;
    }

}
