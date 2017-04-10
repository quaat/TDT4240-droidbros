package no.ntnu.game;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;
import no.ntnu.game.models.Piece;
import no.ntnu.game.movestrategy.DoubleForwardStrategyDecorator;
import no.ntnu.game.movestrategy.HorizontalStrategyDecorator;
import no.ntnu.game.movestrategy.LJumpStrategyDecorator;
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
        Board board = FEN.toBoard("8/8/8/8/8/8/8/8 w - - 0 1");
        Square square = board.square(4,4);
        square.setPiece(piece);

        List<Move> moves = legalMoves(square);
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
        Square square = board.square(4,4);
        square.setPiece(piece);
        List<Move> moves = legalMoves(square);
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
        Square square = board.square(4,4);
        square.setPiece(piece);
        List<Move> moves = legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 11);
    }

    @Test
    public void testKnightMoveStrategy() throws Exception {
        MoveStrategy ljump = new LJumpStrategyDecorator();
        Piece knight = new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE, ljump);
        Board board = FEN.toBoard("8/pppppppp/8/8/8/8/PPPPPPPP/8 w - - 0 1");
        Square square = board.square(3,3);
        square.setPiece(knight);
        List<Move> moves = legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 6);

        square = board.square(0, 2);
        square.setPiece(knight);
        moves = legalMoves(square);
        numMoves = moves.size();
        assertEquals(numMoves, 3);
    }

    @Test
    public void bishopMoveStrategy() throws Exception {
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        Square square = board.square(2, 0);
        assertEquals(square.piece().type(),Piece.Type.BISHOP);
        List<Move> moves = legalMoves(square);
        int numMoves = moves.size();
        assertEquals(numMoves, 0);
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

    static public boolean isCheck(Board board, Piece.Color activeColor) {
        // Find the square of the opponents king
        Square opponentKing =  board.allSquares().stream()
                .filter(s -> (s.piece() != null) && (s.piece().color() != activeColor && s.piece().type() == Piece.Type.KING))
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

    static public boolean isLegal(String fen, Move move) throws Exception {
        Board board = FEN.toBoard(fen);
        GameAction.movePiece(board, move);
        return isCheck(board, board.activeColor());
    }

    @Test
    public void getOutOfCheck() throws Exception {
        String fen = "1nbqk1nr/rp2bpp1/3pp2p/1N6/ppPP4/1K3P1N/P3P1PP/R1BQ1B1R w - - 0 11";
        Board board = FEN.toBoard(fen);
        List<Square> allActive = board.allSquares().stream()
                .filter(s -> (s.piece() != null) && (s.piece().color() == Piece.Color.WHITE))
                .collect(Collectors.toList());
        List<Move> candidateMoves = GameAction.legalMoves(allActive).stream()
                .filter(m -> isLegal(FEN.toFen(board), m))
                .collect(Collectors.toList());

    }

    @Test
    public void moveRandomPiece2() throws Exception {
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        int i = 0;
        boolean isCheck = false;
        while (true) {
            Piece.Color activeColor = board.activeColor();
            List<Square> allActive = board.allSquares().stream()
                    .filter(s -> s.piece() != null && s.piece().color() == activeColor)
                    .collect(Collectors.toList());
            for (Move move : GameAction.legalMoves(allActive)) {
                Board boardCopy = FEN.toBoard(FEN.toFen(board));
                GameAction.movePiece(boardCopy, move);
            }

            List<Move> moves = GameAction.legalMoves(allActive); /*.stream()
                    .filter(move -> {
                        try {
                            GameAction.movePiece(boardCopy, move);
                            return !isCheck(boardCopy, boardCopy.activeColor());
                        } catch (TypeErrorException ex) {
                            System.console().printf("exception error");
                            ;
                        } catch (Exception e) {
                            throw e;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
*/
            if (moves.size() == 0) {
                System.out.println("Check Mate!");
                String fen = FEN.toFen(board);
                System.out.println(fen);
            } else {
                int randomMove = ThreadLocalRandom.current().nextInt(0, moves.size());
                if (randomMove > moves.size() - 1) {
                    isCheck = false;
                }
                board = GameAction.movePiece(board, moves.get(randomMove));
                isCheck = isCheck(board, activeColor);

                String fen = FEN.toFen(board);
                System.out.println(fen);
            }
        }
    }

    @Ignore @Test
    public void moveRandomPiece() throws Exception {
        String standardStartPosition ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        Board board = FEN.toBoard(standardStartPosition);
        int i = 0;
        while (i++ < 20) {
            Piece.Color activeColor = board.activeColor();
            List<Move> moves = new ArrayList<Move>();
            for (int row = 0; row < board.rows(); row++) {
                for (int col = 0; col < board.cols(); col++) {

                    Square square = board.square(col, row);
                    if (square != null) {
                        Piece piece = square.piece();
                        if (piece != null && piece.color() == activeColor) {
                            moves.addAll(legalMoves(square));
                        }
                    }
                }

            }
            Random rand = new Random();
            int randomMove = rand.nextInt(moves.size());
            board = GameAction.movePiece(board, moves.get(randomMove));
            String fen = FEN.toFen(board);
            System.out.println(fen);
        }
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
