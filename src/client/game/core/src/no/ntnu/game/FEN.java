package no.ntnu.game;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;

/**
 * Created by thomash on 26.03.2017.
 */

public class FEN {

    static public Board toBoard(String fenstring){
        return new Board();
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
                    switch (p.type()) {
                        case ROOK:
                            ch = 'R';
                            break;
                        case KNIGHT:
                            ch = 'N';
                            break;
                        case BISHOP:
                            ch = 'B';
                            break;
                        case QUEEN:
                            ch = 'Q';
                            break;
                        case KING:
                            ch = 'K';
                            break;
                        case PAWN:
                        default:
                            ch = 'P';
                    }

                    if (p.color() == Piece.Color.BLACK) {
                        ch = Character.toLowerCase(ch);
                    }
                    sb.append(ch);
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
        if (board.castlingAvailability()[0]) sb.append('K');
        if (board.castlingAvailability()[1]) sb.append('Q');
        if (board.castlingAvailability()[2]) sb.append('k');
        if (board.castlingAvailability()[3]) sb.append('q');
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
