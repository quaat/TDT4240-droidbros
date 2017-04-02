package no.ntnu.game;

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
}
