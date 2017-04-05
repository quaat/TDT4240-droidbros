package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class SetupView extends AbstractView {

    private TextButton backButton;
    private TextButton saveButton;

    private TextField fen1Field;
    private TextField fen2Field;

    public SetupView(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Button
        backButton = new TextButton("BACK", skin);
        saveButton = new TextButton("SAVE", skin);

        // TextFields
        fen1Field = new TextField("", skin);
        fen2Field = new TextField("", skin);
        fen1Field.setAlignment(1);
        fen2Field.setAlignment(1);

        // Listeners
        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.menu();
            }
        });

        saveButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                // TODO Check if fen is valid
                String newFen = fen1Field.getText() + "/" + fen2Field.getText();
                controller.updateUserBoard(newFen);
            }
        });

        table.add(fen1Field).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(fen2Field).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(saveButton).width(objectWidth/2).height(objectHeight);
        table.add(backButton).width(objectWidth/2).height(objectHeight);
    }

    @Override
    public void onUserUpdate() {
        Gdx.app.log("ANDYPANDY", "FUCK ME");
        Gdx.app.log("ANDYPANDY", model.user().fen());
        fen1Field.setText(model.user().fen().substring(0, 8));
        fen2Field.setText(model.user().fen().substring(9, 17));
    }
}