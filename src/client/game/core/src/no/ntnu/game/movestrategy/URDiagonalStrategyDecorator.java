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
    public List<Move> legalMoves(Square square)
    {
        List<Move> moves = new ArrayList<>();
        if (square.piece() == null) return null;
        Board b = square.board();
        int col = square.col() + 1;
        int rank = square.row() + 1;
        // Empty squares to the upper right
        while (col < b.cols() && rank < b.rows()
                && b.square(col, rank).piece() == null) {
            Square dest = b.square(col, rank);
            moves.add(new Move(square, dest));
            col++;
            rank++;
        }

        // Opposite color to the upper right
        if (col < b.cols() && rank < b.rows()
                && b.square(col, rank).piece() != null
                && b.square(col, rank).piece().color() != square.piece().color()) {
            moves.add(new Move(square, b.square(col, rank)));
        }

        col = square.col() - 1;
        rank = square.row() - 1;

        // Empty squares to the lower left
        while (col >= 0 && rank >= 0
                && b.square(col, rank).piece() == null) {
            moves.add(new Move(square, b.square(col, rank)));
            col--;
            rank--;
        }

        // Opposite color to the lower right
        if (col >= 0 && rank >= 0
                && b.square(col, rank).piece() != null
                && b.square(col, rank).piece().color() != square.piece().color()) {
            moves.add(new Move(square, b.square(col, rank)));
        }
        if (this.moveStrategy != null)
            moves.addAll(this.moveStrategy.legalMoves(square));
        return moves;
    }
}
