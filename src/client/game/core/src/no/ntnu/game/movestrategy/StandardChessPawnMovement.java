package no.ntnu.game.movestrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 25.03.2017.
 */

public class StandardChessPawnMovement implements MoveStrategy {
    @Override
    public Move[] legalMoves(Square square)
    {
        Board board = square.board();
        int col = square.col();
        int row = square.row();

        // The Pawn may move 2 ranks forward on first move, otherwise 1
        // The pawn may take if opponent is at rank+1 and either col-1 and col+1



        return null;
    }

    public List<Function<Square,Move>> legalMoves() {
        return new ArrayList<Function<Square,Move>>();
    }
}
