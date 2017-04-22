package no.ntnu.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class FenView extends AbstractView {
    private TextButton backButton;
    private TextButton saveButton;

    private TextField fenField;

    private Label statusLabel;

    public FenView(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Button
        backButton = new TextButton("BACK", skin);
        saveButton = new TextButton("SAVE", skin);

        backButton.setColor(buttonColor);
        saveButton.setColor(buttonColor);

        // TextFields
        fenField = new TextField("", skin);
        fenField.setAlignment(1);

        // Labels
        statusLabel = new Label("", skin);

        // Listeners
        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.toGame();
            }
        });

        saveButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                // TODO Check if fen is valid
                if (fenField.getText().equals("")) return;
                else {
                    statusLabel.setText("Changing fen...");
                    controller.changeFen(fenField.getText());
                }
            }
        });

        table.add(fenField).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(saveButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(backButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(statusLabel).width(objectWidth).height(objectHeight);
    }

    @Override
    public void reset() {
        statusLabel.setText("");
    }

    @Override
    public void onUserUpdate() {
        fenField.setText(model.user().fen());
    }

    @Override
    public void onError() {
        statusLabel.setText(model.error());
    }
}