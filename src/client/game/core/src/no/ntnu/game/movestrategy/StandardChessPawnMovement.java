package no.ntnu.game.movestrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 25.03.2017.
 */

public class StandardChessPawnMovement implements MoveStrategy {
    public List<Function<Square,Move[]>> legalMoves() {
        return null;
    }
    /*
        ArrayList<Function<Square,Move>> functionList = new ArrayList<Function<Square,Move>>();

        // Single forward
        functionList.add(from -> {
            int forward = from.piece().color() == Piece.Color.WHITE ? 1 : -1;
            Square to = from.board().square(from.col(), from.row()+forward);
            return (to.piece() == null ? new Move(from, to) : null);
        });

        // Double forward
        functionList.add(from ->  {
            if (from.row() <= 1 && from.piece().color() == Piece.Color.WHITE ||
                    from.row() >= 6 && from.piece().color() == Piece.Color.BLACK) {
                int forward = from.piece().color() == Piece.Color.WHITE ? 2 : -2;
                Square to = from.board().square(from.col(), from.row()+forward);
                return (to.piece() == null ? new Move(from, to) : null);
            }
            return null;
        });

        // Take right
        functionList.add(from -> {
            if (from.col() == from.board().cols()) return null;
            int forward = from.piece().color() == Piece.Color.WHITE ? 1 : -1;
            Square to = from.board().square(from.col()+1, from.row()+forward);
            return (to.piece() != null && (to.piece().color() != from.piece().color())
                    ? new Move(from, to)
                    : null);
        });

        // Take left
        functionList.add(from -> {
            if (from.col() == 0) return null;
            int forward = from.piece().color() == Piece.Color.WHITE ? 1 : -1;
            Square to = from.board().square(from.col()-1, from.row()+forward);
            return (to.piece() != null && (to.piece().color() != from.piece().color())
                    ? new Move(from, to)
                    : null);
        });


        functionList.removeAll(Collections.singleton(null));
        return functionList;
    }
    */
}
