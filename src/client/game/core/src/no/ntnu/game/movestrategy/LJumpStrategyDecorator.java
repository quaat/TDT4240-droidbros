package no.ntnu.game.movestrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 28.03.2017.
 */

public class LJumpStrategyDecorator implements MoveStrategy {
    protected MoveStrategy moveStrategy = null;
    public LJumpStrategyDecorator()
    {}

    public LJumpStrategyDecorator(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }


    @Override
    public List<Move> legalMoves(Square square)
    {
        List<Move> moves = new ArrayList<>();
        if (square.piece() == null) return null;

        Board b = square.board();
        final int rank = square.row();
        final int col = square.col();
        List<Square> targetSquares = Arrays.asList(
                b.square(col - 2, rank - 1),
                b.square(col - 2, rank + 1),
                b.square(col - 1, rank + 2),
                b.square(col + 1, rank + 2),
                b.square(col + 2, rank + 1),
                b.square(col + 2, rank - 1),
                b.square(col + 1, rank - 2),
                b.square(col - 1, rank - 2));
        for (Iterator it = targetSquares.iterator(); it.hasNext(); ) {
            Square dest = (Square) it.next();

            if (dest != null
                    && (dest.piece() == null
                    || (dest.piece() != null
                    && (dest.piece().color() != square.piece().color())))) {
                moves.add(new Move(square, dest));
            }
        }

        if (this.moveStrategy != null)
            moves.addAll(this.moveStrategy.legalMoves(square));
        return moves;
    }
}
