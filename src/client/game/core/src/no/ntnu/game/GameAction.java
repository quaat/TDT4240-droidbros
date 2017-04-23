package no.ntnu.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import no.ntnu.game.FEN;
import no.ntnu.game.Move;
import no.ntnu.game.TypeErrorException;
import no.ntnu.game.evaluation.GameEvaluation;
import no.ntnu.game.evaluation.PiecePosition;
import no.ntnu.game.evaluation.PieceValue;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;
import no.ntnu.game.movestrategy.MoveStrategyFactory;

/**
 * Created by thomash on 28.03.2017.
 */

public class GameAction {

    static private boolean hasReachedEigthRank(Square square) {
        if (square.piece() != null) {
            int rank = square.piece().color() == Piece.Color.WHITE ? square.row() + 1 :
                    square.board().rows() - square.row();
            return rank == 8;
        }
        return false;
    }
    /**
     * Move a piece on a given board
     * @param board
     * @param move
     * @return board
     */
    static public Board movePiece(Board board, Move move) {
        Square origin = board.square(move.from().col(), move.from().row());
        Square dest = board.square(move.to().col(), move.to().row());
        Piece piece = origin.piece();
        dest.setPiece(piece);
        origin.setPiece(null);
        if (piece.type() != Piece.Type.PAWN) {
            board.incrementHalfmoveClock();
        } else {
            board.resetHalfmoveClock();
        }
        if (board.activeColor() == Piece.Color.BLACK) {
            board.incrementFullmoveClock();
        }
        // Automatically promote the pawn to queen if the opponent rank is reached
        // TODO: Add user specific promotion type
        if (piece.type() == Piece.Type.PAWN &&
                hasReachedEigthRank(dest))
        {
            Piece queen = new Piece(Piece.Type.QUEEN,
                    piece.color(),
                    MoveStrategyFactory.getStrategy(Piece.Type.QUEEN));
            dest.setPiece(queen);
        }
        board.flipActiveColor();
        return board;
    }

    /**
     * Overloaded version of movePiece where a FEN string is given as input
     * @param fen
     * @param move
     * @return a new instance of Board
     */
    static public Board movePiece(final String fen, Move move) {
        Board board = FEN.toBoardS(fen);
        if (board == null) return null;

        return movePiece(board, move);
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

    /**
     * Check for technical draw.
     * @param board
     * @return
     */
    static public boolean isDrawn(Board board) {
        return (board.halfmoveClock() >= 50);
    }

    /**
     * Check for technical draw.
     * @param fen
     * @return true if the position is a draw
     */
    static public boolean isDrawn(final String fen) {
        boolean isDrawn = false;
        try {
            Board board = FEN.toBoard(fen);
            isDrawn = board.halfmoveClock() >= 50;
        } catch (TypeErrorException ex){
            System.out.println(ex.toString());
        }

        return isDrawn;
    }

    /**
     * This method inspects all piece movements and check if any pieces of activeColor can attack the
     * opponent king.
     * @param board
     * @param activeColor
     * @return
     */
    static public boolean isCheck(Board board, Piece.Color activeColor) {
        // Find the square of the opponents king
        Square opponentKing = null;
        for (Square square : board.allSquares()) {
            if (square.piece() != null && square.piece().color() != activeColor && square.piece().type() == Piece.Type.KING) {
                opponentKing = square;
                break;
            }
        }

        // Update all active players pieces
        List<Square> allActive = new ArrayList<Square>();
        for (Square square : board.allSquares()) {
            if (square.piece() != null && square.piece().color() == activeColor) {
                allActive.add(square);
            }
        }

        // If any pieces may move to the opponent king square, it is check.
        boolean isCheck = false;
        for (Move move : GameAction.legalMoves(allActive)) {
            if (move.to() == opponentKing) {
                isCheck = true;
                break;
            }
        }

        return isCheck;
    }

    /**
     * Overloaded method where the predicate pOppositeColor identifies the the active
     * color should be the boards' active player, or the opposite.
     * @param board
     * @param pOppositeColor - true if opposite color than active turn should be the attacker
     * @return
     */
    static public boolean isCheck(Board board, boolean pOppositeColor) {

        Piece.Color color = board.activeColor();
        if (pOppositeColor) color = board.activeColor() == Piece.Color.BLACK ? Piece.Color.WHITE: Piece.Color.BLACK;
        return isCheck(board, color);
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

        return piece.legalMoves(square);
    }

    /**
     * Return a list of moves which doesn't set the king in check.
     * @param fen
     * @return
     */
    public static List<Move> legalMoves(final String fen) {

        List<Move> moves = new ArrayList<Move>();
        try {
            Board board = FEN.toBoard(fen);
            List<Square> squares = new ArrayList<>();
            for (Square square : board.allSquares()) {
                if (square.piece() != null && square.piece().color() == board.activeColor()) {
                    squares.add(square);
                }
            }

            // Filter out only the legal candidate moves that does not set its own king in check
            List<Move> candidateMoves  = new ArrayList<>();
            for (Move move : GameAction.legalMoves(squares)) {
                if (!GameAction.isCheck(GameAction.movePiece(fen, move), false)) {
                    candidateMoves.add(move);
                }
            }

            moves.addAll(candidateMoves);

        } catch (TypeErrorException ex){
            // Illegal setup
            System.out.println(ex.toString());
        }
        return moves;
    }

    /**
     * Check if there is a mate situation on the board
     * @param fen
     * @return true if the king is mated
     */
    public static boolean isMate(final String fen) {
        return legalMoves(fen).isEmpty();
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


    /**
     * Suggest the best move based on a list of legal moves and the current position
     * @param moves
     * @param fen
     * @return
     * @throws Exception
     */
    public static Move bestMove(List<Move> moves, final String fen) throws Exception{
        final float delta = 1.0E-8f;
        final float factor = FEN.toBoard(fen).activeColor() == Piece.Color.WHITE ? 1.0f : -1.0f;
        float highScore = -Float.MAX_VALUE;
        GameEvaluation eval = new PieceValue(new PiecePosition());
        List<Move>goodMoves = new ArrayList<>();
        for (Move move : moves) {
            float score = factor * eval.score(GameAction.movePiece(fen, move));
            if (score > highScore+delta) {
                highScore = score;
                goodMoves.add(0,move);
            } else if (score < highScore+delta && score > highScore-delta) {
                goodMoves.add(0,move);
            }
        }
        // evaluate further maximum 10 candidate moves
        if (goodMoves.size() > 10) {
            goodMoves.subList(10, goodMoves.size()).clear();
        }

        return goodMoves.get(0);
    }
}
