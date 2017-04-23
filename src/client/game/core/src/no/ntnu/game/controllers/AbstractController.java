package no.ntnu.game.controllers;

import no.ntnu.game.models.GameModel;

/**
 * Created by thomas on 4/22/17.
 */

public abstract class AbstractController {
    protected final GameModel model;
    public AbstractController(GameModel model) {
        this.model = model;

    }
}
