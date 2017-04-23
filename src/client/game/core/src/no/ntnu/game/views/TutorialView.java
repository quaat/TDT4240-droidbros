package no.ntnu.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

/**
 * Created by Leppis on 20.04.2017.
 */

public class TutorialView extends AbstractView {
    public TutorialView(GameModel model, GameController controller) {
        super(model, controller);
        this.controller = controller;
    }

    @Override
    public void build() {
        // Button
        final TextButton backButton = new TextButton("BACK", skin);
        backButton.setColor(buttonColor);

        Label text1 = new Label("Build your own start line", skin);
        Label text2 = new Label("Play like chess ", skin);
        Label text3 = new Label("Fight to become the best", skin);
        Label text4 = new Label("", skin);

        text1.setAlignment(1);
        text2.setAlignment(1);
        text3.setAlignment(1);

        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.toMenu();
            }
        });

        table.add(text1).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(text2).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(text3).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(text4).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(backButton).width(objectWidth).height(objectHeight);
    }

    @Override
    public void reset() {
    }
}
