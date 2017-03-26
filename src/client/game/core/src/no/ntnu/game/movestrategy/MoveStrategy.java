package no.ntnu.game.movestrategy;

import no.ntnu.game.Move;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Square;

/**
 * Created by thomash on 25.03.2017.
 */

public interface MoveStrategy {
    public Move[] legalMoves();
}
