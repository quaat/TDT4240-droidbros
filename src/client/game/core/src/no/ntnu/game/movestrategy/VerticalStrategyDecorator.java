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
    public List<Move> legalMoves(Square square)
    {
        List<Move> moves = new ArrayList<>();
        if (square.piece() == null) return null;
        Board b = square.board();
        int rank = square.row() + 1;
        // empty squares upwards
        final int col = square.col();
        while (rank < b.rows() && b.square(col, rank).piece() == null) {
            Square dest = b.square(col, rank);
            moves.add(new Move(square, dest));
            rank++;
        }
        // opposite color up
        if (b.square(col, rank) != null) {
            Piece squarePiece = square.piece();
            Piece tmpPiece = b.square(col, rank).piece();
            if (tmpPiece != null && tmpPiece.color() != square.piece().color()) {
                moves.add(new Move(square, b.square(col, rank)));
            }
        }

        // empty squares downwards
        rank = square.row() - 1;
        while (rank >= 0 && b.square(col, rank).piece() == null) {
            Square dest = b.square(col, rank);
            moves.add(new Move(square, dest));
            rank--;
        }

        // opposite color down
        if (b.square(col, rank) != null) {
            if (b.square(col, rank).piece() != null && b.square(col, rank).piece().color() != square.piece().color()) {
                moves.add(new Move(square, b.square(col, rank)));
            }
        }
        if (this.moveStrategy != null)
            moves.addAll(this.moveStrategy.legalMoves(square));
        return moves;
    }
}
