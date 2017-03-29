package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Message;

public class TestView2 extends AbstractView {

    private final String queueText = "Users in queue: ";
    private final String usersText = "Users online: ";
    private final String winnerText = "Winner is: ";

    private Label queueLabel;
    private Label statusLabel;
    private Label player1Label;
    private Label player2Label;
    private Label winnerLabel;

    public TestView2(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Buttons
        final TextButton findGameButton = new TextButton("FIND GAME", skin);
        final TextButton doMoveButton = new TextButton("DO MOVE", skin);
        final TextButton resignButton = new TextButton("RESIGN", skin);

        // Labels
        queueLabel = new Label(queueText, skin);
        statusLabel = new Label("", skin);
        player1Label = new Label("", skin);
        player2Label = new Label("", skin);
        winnerLabel = new Label(winnerText, skin);

        // Listeners
        findGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.findGame();
            }
        });

        doMoveButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //controller.doMove();
            }
        });

        resignButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //controller.resign();
            }
        });

        table.add(findGameButton).width(objectWidth).height(objectHeight).row();
        table.add(queueLabel).width(objectWidth).height(objectHeight).row();
        table.add(statusLabel).width(objectWidth).height(objectHeight).row();
        table.add(player1Label).width(objectWidth).height(objectHeight);
        table.add(player2Label).width(objectWidth).height(objectHeight).row();
        table.add(doMoveButton).width(objectWidth).height(objectHeight).row();
        table.add(resignButton).width(objectWidth).height(objectHeight).row();
        table.add(winnerLabel).width(objectWidth).height(objectHeight);
    }

    @Override
    public void onUpdate() {
        queueLabel.setText(usersText + model.getCurrentUsersOnline() + " - " + queueText + model.getQueue());
    }

    @Override
    public void onQueueUpdate() {

    }

    public void gameJoined() {
        // Set game stuff visible
        // Hide searching stuff
    }

    public void gameLeft() {
        // Hide game stuff
        // Set searching stuff visible
        // Add winner?
    }

    public void updateQueue() {

    }
}
