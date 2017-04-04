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

public class ULDiagonalStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;

    public ULDiagonalStrategyDecorator() {}

    public ULDiagonalStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public List<Function<Square, List<Move>>> legalMoves() {
        List<Function<Square, List<Move>>> moves = new ArrayList<>();

        moves.add(square -> {
            List<Move> movesList = new ArrayList<Move>();
            Board b = square.board();
            int col = square.col() + 1;
            int rank = square.row() - 1;
            // Empty squares to the lower right
            while (col < b.cols() && rank >= 0
                    && b.square(col, rank).piece() == null) {
                Square dest = b.square(col, rank);
                movesList.add(new Move(square, dest));
                col++;
                rank--;
            }

            if (b.square(col, rank) != null) {
                // Opposite color to the lower right
                if (b.square(col, rank).piece() != null
                    && b.square(col, rank).piece().color() != square.piece().color()) {
                    movesList.add(new Move(square, b.square(col, rank)));
                }
            }

            col = square.col() - 1;
            rank = square.col() +1 ;
            // Empty squares to the upper left
            while (col >= 0 && rank < b.rows()
                    && b.square(col, rank).piece() == null) {
                movesList.add(new Move(square, b.square(col, rank)));
                col--;
                rank++;
            }

            if (b.square(col, rank) != null) {
                // Opposite color to the lower right
                if (b.square(col, rank).piece() != null
                    && b.square(col, rank).piece().color() != square.piece().color()) {
                    movesList.add(new Move(square, b.square(col, rank)));
                }
            }

            return movesList;
        });

        if (this.moveStrategy != null) {
            moves.addAll(this.moveStrategy.legalMoves());
        }
        return moves;
    }
}
