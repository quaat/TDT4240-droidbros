package no.ntnu.game.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 13.04.2017.
 */

public class PieceValue implements GameEvaluation {
    protected GameEvaluation gameEvaluation = null;
    public PieceValue() {}

    public PieceValue(GameEvaluation gameEvaluation) {
        this.gameEvaluation = gameEvaluation;
    }

    float value(Piece.Type type) {
        switch (type) {
            case QUEEN:
                return 9.0f;
            case ROOK:
                return 5.0f;
            case BISHOP:
            case KNIGHT:
                return 3.0f;
            case KING:
                return 9999.0f;
            case PAWN:
            default:
                return 1.0f;
        }
    }

    @Override
    public float score(final Board board) {
        float score = 0.0f;
        List<Square> squaresWithPiece = board.allSquares().stream()
                .filter(s -> s.piece() != null)
                .collect(Collectors.toList());
        for (Square square : squaresWithPiece) {
            float pieceValue = value(square.piece().type());
            score += (square.piece().color() == Piece.Color.WHITE ? pieceValue : -pieceValue);
        }

        if (this.gameEvaluation != null)
            score += this.gameEvaluation.score(board);
        return score;
    }
}
