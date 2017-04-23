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

public class DoubleForwardStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;
    public DoubleForwardStrategyDecorator()
    {}

    public DoubleForwardStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public List<Move> legalMoves(Square square)
    {
        List<Move> moves = new ArrayList<>();
        if (square.piece() == null) return null;

        Board b = square.board();
        final int forward = square.piece().color() == Piece.Color.BLACK ? -1 : 1;
        final int startRank = square.piece().color() == Piece.Color.BLACK ? 6 : 1;
        if (square.row() == startRank) {
            if (b.square(square.col(), square.row() + forward).piece() == null
                    && b.square(square.col(), square.row() + 2 * forward).piece() == null) {
                Square dest = b.square(square.col(), square.row() + 2 * forward);
                moves.add(new Move(square, dest));
            }
        }

        if (this.moveStrategy != null)
            moves.addAll(this.moveStrategy.legalMoves(square));
        return moves;
    }
}
