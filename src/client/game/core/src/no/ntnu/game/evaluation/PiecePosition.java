package no.ntnu.game.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 13.04.2017.
 */

public class PiecePosition implements GameEvaluation{
    protected GameEvaluation gameEvaluation = null;
    public PiecePosition() {}
    public PiecePosition(GameEvaluation gameEvaluation) {
        this.gameEvaluation = gameEvaluation;
    }

    /**
     * WARNING: This method assumes an 8x8 square board
     * @param square
     * @return
     */
    private float value(Square square) {

        final float factor = 0.2f;
        float maxdist = Math.max(Math.abs(square.row() - 3.5f),
                Math.abs(square.col() - 3.5f));
        return factor * (1.0f - (maxdist - 0.5f)/3.5f);

    }
    @Override
    public float score(final Board board) {
        // Compute a score for all squares that contains a piece
        float score = 0.0f;
        for (Square square : board.allSquares()) {
            if (square.piece ()!= null) {
                float pieceValue = value(square);
                score += (square.piece().color() == Piece.Color.WHITE ? pieceValue : -pieceValue);
            }
        }

        if (this.gameEvaluation != null) {
            score += this.gameEvaluation.score(board);
        }
        return score;
    }
}
