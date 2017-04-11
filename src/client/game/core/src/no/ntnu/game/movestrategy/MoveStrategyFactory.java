package no.ntnu.game.movestrategy;

import no.ntnu.game.models.Piece;

/**
 * Created by thomas on 4/11/17.
 */

public class MoveStrategyFactory {
    static public MoveStrategy getStrategy(Piece.Type type) {
        switch (type) {
            case ROOK:
                return new HorizontalStrategyDecorator(
                        new VerticalStrategyDecorator());
            case KNIGHT:
                return new LJumpStrategyDecorator();
            case BISHOP:
                return new ULDiagonalStrategyDecorator(
                        new URDiagonalStrategyDecorator());
            case QUEEN:
                return new ULDiagonalStrategyDecorator(
                        new URDiagonalStrategyDecorator(
                                new HorizontalStrategyDecorator(
                                        new VerticalStrategyDecorator())));
            case KING:
                return new AllSurroundingStrategyDecorator();
            case PAWN:
            default:
                return new DoubleForwardStrategyDecorator(new SingleForwardStrategyDecorator());
        }
    }

}
