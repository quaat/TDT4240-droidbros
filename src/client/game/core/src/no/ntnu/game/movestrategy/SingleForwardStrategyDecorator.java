package no.ntnu.game.movestrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 28.03.2017.
 */

public class SingleForwardStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;
    public SingleForwardStrategyDecorator()
    {}

    public SingleForwardStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public List<Function<Square, List<Move>>> legalMoves() {
        List<Function<Square, List<Move>>> moves = new ArrayList<>();
        moves.add(square -> {
            ArrayList<Move> movesArray = new ArrayList<Move>();
            Board b = square.board();
            final int rank = square.row();
            final int col = square.col();
            int forward = square.piece().color() == Piece.Color.BLACK ? -1 : 1;
            Square dest = b.square(col, rank+forward);
            if (dest != null && dest.piece() == null) {
                movesArray.add(new Move(square, dest));
            }

            // take opponent pieces to the upper left and upper right
            dest = b.square(col-1, rank+forward);
            if (dest != null && dest.piece() != null && dest.piece().color() != square.piece().color()) {
                movesArray.add(new Move(square, dest));
            }

            dest = b.square(col+1, rank+forward);
            if (dest != null && dest.piece() != null && dest.piece().color() != square.piece().color()) {
                movesArray.add(new Move(square, dest));
            }

            return movesArray;
        });
        if (this.moveStrategy != null)
            moves.addAll(this.moveStrategy.legalMoves());
        return moves;
    }
}
