package no.ntnu.game.movestrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 28.03.2017.
 */

public class HorizontalStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;
    public HorizontalStrategyDecorator()
    {}

    public HorizontalStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public List<Function<Square, List<Move>>> legalMoves() {
        List<Function<Square, List<Move>>> moves = new ArrayList<>();

        moves.add(square -> {
            List<Move> movesList = new ArrayList<Move>();
            Board b = square.board();
            int col = square.col() + 1;
            final int rank = square.row();

            // Empty square to the right
            while (col < b.cols() && b.square(col, rank).piece() == null) {
                Square dest = b.square(col, rank);
                movesList.add(new Move(square, dest));
                col++;
            }
            // Opposite color to the right
            if (col < b.cols()
                    && b.square(col, rank).piece() != null
                    && b.square(col, rank).piece().color() != square.piece().color()) {
                movesList.add(new Move(square, b.square(col, rank)));
            }

            // Emtpty squares to the left
            col = square.col() - 1;
            while (col >= 0 && b.square(col, rank).piece() == null) {
                Square dest = b.square(col, rank);
                movesList.add(new Move(square, dest));
                col--;
            }

            if (col >= 0
                    && b.square(col, rank).piece() != null
                    && b.square(col, rank).piece().color() != square.piece().color()) {
                movesList.add(new Move(square, b.square(col, rank)));
            }
            return movesList;
        });


        if (this.moveStrategy != null)
            moves.addAll(this.moveStrategy.legalMoves());
        return moves;
    }
}
