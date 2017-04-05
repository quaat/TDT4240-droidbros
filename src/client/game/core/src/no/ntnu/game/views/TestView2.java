package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;


public class TestView2 extends AbstractView {

    String randFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private TextButton findGameButton;
    private TextButton doMoveButton;
    private TextButton resignButton;

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
        doMoveButton = new TextButton("DO MOVE", skin);
        resignButton = new TextButton("RESIGN", skin);

        // Labels
        statusLabel = new Label("", skin);
        player1Label = new Label("", skin);
        player2Label = new Label("", skin);
        winnerLabel = new Label("", skin);


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

        table.add(statusLabel).width(objectWidth).height(objectHeight).row();
        table.add(player1Label).width(objectWidth).height(objectHeight);
        table.add(player2Label).width(objectWidth).height(objectHeight).row();
        table.add(doMoveButton).width(objectWidth).height(objectHeight).row();
        table.add(resignButton).width(objectWidth).height(objectHeight).row();
        table.add(winnerLabel).width(objectWidth).height(objectHeight);
    }

    @Override
    public void onNewMove() {
        doMoveButton.setVisible(true);
    }

    public void gameJoined() {
        if (model.isItMyTurn()) doMoveButton.setVisible(true);
        else doMoveButton.setVisible(false);
        player1Label.setText("You: " + model.player().toString());
        player2Label.setText("Opponent: " + model.opponent().toString());
        statusLabel.setText("gameid: "+ model.gameid());
    }

    public void gameLeft() {

    }

}
