package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

/**
 * Created by Leppis on 21.04.2017.
 */

public class GameEndedView extends AbstractView {

    private Label winnerLabel;
    private Label fenLabel;
    private Label startedLabel;
    private Label endedLabel;

    private TextButton continueButton;

    public GameEndedView(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        continueButton = new TextButton("CONTINUE", skin);
        continueButton.setColor(buttonColor);

        winnerLabel = new Label("", skin);
        fenLabel = new Label("", skin);
        startedLabel = new Label("", skin);
        endedLabel = new Label("", skin);

        winnerLabel.setAlignment(1);
        fenLabel.setAlignment(1);
        startedLabel.setAlignment(1);
        endedLabel.setAlignment(1);

        continueButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.toGame();
            }
        });

        table.add(winnerLabel).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(fenLabel).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(startedLabel).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(endedLabel).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(continueButton).width(objectWidth).height(objectHeight);
    }

    @Override
    public void reset() {
    }

    @Override
    public void onGameUpdate() {
        winnerLabel.setText("Winner: " + model.gameInfo().winner());
        fenLabel.setText("Last fen: " + model.gameInfo().fen());
        startedLabel.setText("Started: " + model.gameInfo().started());
        endedLabel.setText("Ended: " + model.gameInfo().ended());
    }
}
