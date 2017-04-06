package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;


public class TestView2 extends AbstractView {

    String randFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private TextButton doMoveButton;
    private TextButton resignButton;

    private Label gameLabel;
    private Label player1Label;
    private Label player2Label;
    private Label statusLabel;

    public TestView2(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Buttons
        doMoveButton = new TextButton("DO MOVE", skin);
        resignButton = new TextButton("RESIGN", skin);

        // Labels
        gameLabel = new Label("", skin);
        player1Label = new Label("", skin);
        player2Label = new Label("", skin);
        statusLabel = new Label("", skin);


        doMoveButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (controller.doMove(randFen));
                    doMove();
            }
        });

        resignButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.resign();
            }
        });

        table.add(gameLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(player1Label).width(objectWidth).height(objectHeight).padBottom(padY);
        table.add(player2Label).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(doMoveButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(resignButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(statusLabel).width(objectWidth).height(objectHeight);
    }

    @Override
    public void onNewMove() {
        doMoveButton.setVisible(true);
        statusLabel.setText("Your move!");
    }

    @Override
    public void onGameUpdate() {
        if (model.isItMyTurn()) doMoveButton.setVisible(true);
        else doMoveButton.setVisible(false);
        player1Label.setText("You: " + model.player().toString());
        player2Label.setText("Opponent: " + model.opponent().toString());
        gameLabel.setText("gameid: "+ model.gameid());
    }

    public void doMove() {
        statusLabel.setText("Wait for move...");
        doMoveButton.setVisible(false);
    }

}
