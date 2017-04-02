package no.ntnu.game.movestrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import no.ntnu.game.Move;
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
        if (this.moveStrategy != null) {
            moves.addAll(this.moveStrategy.legalMoves());
        }
        return moves;
    }
}
