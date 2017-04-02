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

public class VerticalStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;

    public VerticalStrategyDecorator(){}

    public VerticalStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public List<Function<Square, List<Move>>> legalMoves() {
        List<Function<Square, List<Move>>> moves = new ArrayList<>();
        moves.add(square -> {

            List<Move>movesList = new ArrayList<Move>();
            Board b = square.board();
            int rank = square.row() + 1;
            // empty squares upwards
            final int col = square.col();
            while (rank < b.rows() && b.square(col, rank).piece() == null) {
                Square dest = b.square(col, rank);
                movesList.add(new Move(square, dest));
                rank++;
            }
            // opposite color up
            Piece squarePiece = square.piece();
            Piece tmpPiece = b.square(col,rank).piece();
            if (rank < b.rows() && tmpPiece != null && tmpPiece.color() != square.piece().color()) {
                movesList.add(new Move(square, b.square(col,rank)));
            }

            // empty squares downwards
            rank = square.row() - 1;
            while (rank >= 0 && b.square(col, rank).piece() == null) {
                Square dest = b.square(col, rank);
                movesList.add(new Move(square, dest));
                rank--;
            }

            // opposite color down
            if (rank >= 0 && b.square(col,rank).piece() != null && b.square(col,rank).piece().color() != square.piece().color()) {
                movesList.add(new Move(square, b.square(col,rank)));
            }

            return movesList;
        });

        if (this.moveStrategy != null) {
            moves.addAll(this.moveStrategy.legalMoves());
        }
        return moves;
    }
}
