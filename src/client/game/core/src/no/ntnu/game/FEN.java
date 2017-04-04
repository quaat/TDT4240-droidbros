package no.ntnu.game;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.movestrategy.DoubleForwardStrategyDecorator;
import no.ntnu.game.movestrategy.HorizontalStrategyDecorator;
import no.ntnu.game.movestrategy.LJumpStrategyDecorator;
import no.ntnu.game.movestrategy.MoveStrategy;
import no.ntnu.game.movestrategy.SingleForwardStrategyDecorator;
import no.ntnu.game.movestrategy.ULDiagonalStrategyDecorator;
import no.ntnu.game.movestrategy.URDiagonalStrategyDecorator;
import no.ntnu.game.movestrategy.VerticalStrategyDecorator;
import no.ntnu.game.movestrategy.AllSurroundingStrategyDecorator;
/**
 * Created by thomash on 26.03.2017.
 */

public class FEN {

    static private MoveStrategy getStrategy(Piece.Type type) {
        switch (type) {
            case ROOK:
                return new HorizontalStrategyDecorator(
                        new VerticalStrategyDecorator());
            case KNIGHT:
                return new LJumpStrategyDecorator();
            case BISHOP:
                return new ULDiagonalStrategyDecorator(
                        new URDiagonalStrategyDecorator());
            case QUEEN:
                return new ULDiagonalStrategyDecorator(
                        new URDiagonalStrategyDecorator(
                        new HorizontalStrategyDecorator(
                        new VerticalStrategyDecorator())));
            case KING:
                return new AllSurroundingStrategyDecorator();
            case PAWN:
            default:
                return new DoubleForwardStrategyDecorator(new SingleForwardStrategyDecorator());
        }
    }

    static private Character getCharacterType(Piece.Type type) {
        switch (type) {
            case ROOK:
                return new Character('R');
            case KNIGHT:
                return new Character('N');
            case BISHOP:
                return new Character('B');
            case QUEEN:
                return new Character('Q');
            case KING:
                return new Character('K');
            case PAWN:
            default:
                return new Character('P');
        }
    }

    static private Piece.Type getType(Character ch) throws TypeErrorException{
        switch (Character.toUpperCase(ch)) {
            case 'P':
                return Piece.Type.PAWN;
            case 'R':
                return Piece.Type.ROOK;
            case 'N':
                return Piece.Type.KNIGHT;
            case 'K':
                return Piece.Type.KING;
            case 'B':
                return Piece.Type.BISHOP;
            case 'Q':
                return Piece.Type.QUEEN;
            default:
                throw new TypeErrorException("Unkown piece type");
        }
    }

    static public Board toBoard(String fenstring) throws TypeErrorException {
        // NOTE!! Assumes an 8x8 board
        Board board = new Board(8,8);

        String delims = "[ ]+";
        String[] sections = fenstring.split(delims);
        if (sections.length != 6) {
            throw new TypeErrorException("Illegal FEN format");
        }

        String[] ranks = sections[0].split("[/]+");
        int row = ranks.length-1;
        for (int r = 0; r < ranks.length; r++, row--) {
            int col = 0;
            for (int c = 0; c < ranks[r].length(); c++, col++) {
                Piece.Type type = null;
                Piece.Color color = null;
                Character alnum = ranks[r].charAt(c);
                // If character is a number, move to the next non-empty position or end of row
                if (Character.isDigit(alnum)) {
                    int dc = Character.getNumericValue(alnum);
                    col += dc - 1; // remove 1 due to the autoincrement
                }
                else {
                    if (Character.isLowerCase(alnum)) {
                        color = Piece.Color.BLACK;
                    } else {
                        color = Piece.Color.WHITE;
                    }
                    try {
                        type = getType(alnum);
                    } catch (TypeErrorException err) {
                        //
                        throw err;
                    }
                    MoveStrategy moveStrategy = getStrategy(type);

                    board.square(col, row).setPiece(new Piece(type, color, moveStrategy));
                }
            }
        }

        // Get active color
        if (Character.toLowerCase(sections[1].charAt(0)) == 'w') {
            board.setActiveColor(Piece.Color.WHITE);
        } else if ((Character.toLowerCase(sections[1].charAt(0)) == 'b')) {
            board.setActiveColor(Piece.Color.BLACK);
        } else {
            throw new TypeErrorException("Illegal FEN format - faulty active color");
        }

        // Get castling availability
        board.setCastlingAvailability(0, Boolean.FALSE);
        board.setCastlingAvailability(1, Boolean.FALSE);
        board.setCastlingAvailability(2, Boolean.FALSE);
        board.setCastlingAvailability(3, Boolean.FALSE);
        for (int cidx = 0; cidx < sections[2].length(); cidx++) {
            switch (sections[2].charAt(cidx)) {
                case 'K':
                    board.setCastlingAvailability(0, Boolean.TRUE);
                    break;
                case 'Q':
                    board.setCastlingAvailability(1, Boolean.TRUE);
                    break;
                case 'k':
                    board.setCastlingAvailability(2, Boolean.TRUE);
                    break;
                case 'q':
                    board.setCastlingAvailability(3, Boolean.TRUE);
                    break;
            }
        }

        // TODO: parse en-passant target

        // TODO: Check errors here:
        int halfmoveClock = Integer.parseInt(sections[4]);
        board.setHalfmoveClock(halfmoveClock);

        int fullmoveClock = Integer.parseInt(sections[5]);
        board.setFullmoveClock(fullmoveClock );

        return board;
    }

    static public String toFen(final Board board) {
        StringBuilder sb = new StringBuilder();

        for(int row = board.rows()-1; row >= 0; row--) {
            StringBuilder sbc = new StringBuilder();
            int blank = 0;
            if (row < (board.rows()-1)) sb.append("/");

            for (int col = 0; col < board.cols(); col++) {
                Piece p = board.square(col,row).piece();
                if (p != null) {
                    Character ch;
                    if (blank > 0) {
                        sbc.append(blank);
                        blank = 0;
                    }

                    ch = getCharacterType(p.type());
                    if (p.color() == Piece.Color.BLACK) {
                        ch = Character.toLowerCase(ch);
                    }
                    sbc.append(ch);
                } else {
                    blank++;
                }
            }
            if (blank > 0) sbc.append(blank);
            sb.append(sbc.toString());
        }

        // Active piece
        sb.append(" ");
        if (board.activeColor() == Piece.Color.WHITE) {
            sb.append('w');
        } else {
            sb.append('b');
        }
        sb.append(" ");

        // Castling availability
        if (board.castlingAvailability()[0] ||
                board.castlingAvailability()[1] ||
                board.castlingAvailability()[2] ||
                board.castlingAvailability()[3]) {

            if (board.castlingAvailability()[0]) sb.append('K');
            if (board.castlingAvailability()[1]) sb.append('Q');
            if (board.castlingAvailability()[2]) sb.append('k');
            if (board.castlingAvailability()[3]) sb.append('q');
        } else {
            sb.append('-');
        }
        sb.append(' ');

        // TODO: Insert en passant target
        sb.append('-');

        sb.append(' ');
        sb.append(board.halfmoveClock());
        sb.append(' ');
        sb.append(board.fullmoveClock());


        return sb.toString();
    }
}
