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

public class URDiagonalStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;

    public URDiagonalStrategyDecorator(){}
    public URDiagonalStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public List<Function<Square, List<Move>>> legalMoves() {
        List<Function<Square, List<Move>>> moves = new ArrayList<>();

        moves.add(square -> {
            List<Move> movesList = new ArrayList<Move>();
            Board b = square.board();
            int col = square.col() + 1;
            int rank = square.row() + 1;
            // Empty squares to the upper right
            while (col < b.cols() && rank < b.rows()
                    && b.square(col, rank).piece() == null) {
                Square dest = b.square(col, rank);
                movesList.add(new Move(square, dest));
                col++;
                rank++;
            }

            // Opposite color to the upper right
            if (col < b.cols() && rank < b.rows()
                    && b.square(col, rank).piece() != null
                    && b.square(col, rank).piece().color() != square.piece().color()) {
                movesList.add(new Move(square, b.square(col, rank)));
            }

            col = square.col() - 1;
            rank = square.row() - 1;

            // Empty squares to the lower left
            while (col >= 0 && rank >= 0
                    && b.square(col, rank).piece() == null) {
                movesList.add(new Move(square, b.square(col, rank)));
                col--;
                rank--;
            }

            // Opposite color to the lower right
            if (col >= 0 && rank >= 0
                    && b.square(col, rank).piece() != null
                    && b.square(col, rank).piece().color() != square.piece().color()) {
                movesList.add(new Move(square, b.square(col, rank)));
            }

            return movesList;
        });

        if (this.moveStrategy != null) {
            moves.addAll(this.moveStrategy.legalMoves());
        }
        return moves;
    }
}
