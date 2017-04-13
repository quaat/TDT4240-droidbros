package no.ntnu.game.evaluation;

import no.ntnu.game.models.Board;

/**
 * Created by thomash on 13.04.2017.
 *
 */

public interface GameEvaluation {
    public float score(final Board board);
}
