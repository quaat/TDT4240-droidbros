package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.graphics.Color;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;


public class TestView2 extends AbstractView {

    private final String queueText = "Users in queue: ";
    private final String usersText = "Users online: ";
    private final String winnerText = "Winner is: ";

    String randFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private TextButton findGameButton;
    private TextButton doMoveButton;
    private TextButton resignButton;

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
        findGameButton = new TextButton("FIND GAME", skin);
        doMoveButton = new TextButton("DO MOVE", skin);
        resignButton = new TextButton("RESIGN", skin);

        // Labels
        queueLabel = new Label(queueText, skin);
        statusLabel = new Label("", skin);
        player1Label = new Label("", skin);
        player2Label = new Label("", skin);
        winnerLabel = new Label("", skin);

        // Listeners
        findGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.findGame();
            }
        });

        doMoveButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (controller.doMove(randFen));
                    doMoveButton.setVisible(false);
            }
        });

        resignButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.resign();
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

        changeState(true);
    }

    @Override
    public void onUpdate() {
        queueLabel.setText(usersText + model.currentUsers() + " - " + queueText + model.currentQueue());
    }

    public void gameJoined() {
        changeState(false);
        if (model.isItMyTurn()) doMoveButton.setVisible(true);
        else doMoveButton.setVisible(false);
        player1Label.setText("You: " + model.player().toString());
        player2Label.setText("Opponent: " + model.opponent().toString());
        statusLabel.setText("gameid: "+ model.gameid());
    }

    public void onNewMove() {
        doMoveButton.setVisible(true);
        Gdx.app.log("ANDYPANDY", model.gameid() + "   " );
    }

    public void gameLeft() {
        changeState(true);
    }

    // search -> game -> search -> ...
    private void changeState(boolean bool) {
        findGameButton.setVisible(bool);
        queueLabel.setVisible(bool);
        player1Label.setVisible(!bool);
        player2Label.setVisible(!bool);
        doMoveButton.setVisible(!bool);
        resignButton.setVisible(!bool);
        winnerLabel.setVisible(!bool);
    }
}
