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

public class GameView extends AbstractView{

    private TextButton findButton;
    private TextButton computerButton;
    private TextButton fenButton;
    private TextButton backButton;

    private Label useridLabel;
    private Label statsLabel;
    private Label levelLabel;
    private Label usersLabel;

    private boolean searching = false;

    public GameView(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Buttons
        findButton = new TextButton("FIND OPPONENT", skin);
        computerButton = new TextButton("VS COMPUTER", skin);
        fenButton = new TextButton("MY BOARD", skin);
        //final TextButton gamesButton = new TextButton("GAMES", skin);
        backButton = new TextButton("BACK", skin);

        findButton.setColor(buttonColor);
        computerButton.setColor(buttonColor);
        fenButton.setColor(buttonColor);
        //gamesButton.setColor(buttonColor);
        backButton.setColor(buttonColor);

        // Label
        useridLabel = new Label("", skin);
        usersLabel = new Label("", skin);
        levelLabel = new Label("", skin);
        statsLabel = new Label("", skin);

        useridLabel.setAlignment(1);
        usersLabel.setAlignment(1);
        levelLabel.setAlignment(1);
        statsLabel.setAlignment(1);

        findButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (!searching) {
                    controller.findGame();
                    findButton.setText("LEAVE QUEUE");
                    searching = true;
                    computerButton.setDisabled(true);
                    fenButton.setDisabled(true);
                    backButton.setDisabled(true);
                } else {
                    findButton.setText("FIND OPPONENT");
                    controller.leaveQueue();
                    searching = false;
                    computerButton.setDisabled(false);
                    fenButton.setDisabled(false);
                    backButton.setDisabled(false);
                }
            }
        });

        computerButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.playComputer();
            }
        });

        fenButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.toFen();
            }
        });

        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.toMenu();
            }
        });
        table.add(useridLabel).width(objectWidth).height(objectHeight).row();
        //table.add(levelLabel).width(objectWidth).height(objectHeight).row();
        table.add(statsLabel).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(findButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(computerButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(fenButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        //table.add(gamesButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(backButton).width(objectWidth).height(objectHeight).padBottom(padY*2).row();
        table.add(usersLabel).width(objectWidth).height(objectHeight);
    }

    @Override
    public void reset() {
        findButton.setText("FIND OPPONENT");
        controller.leaveQueue();
        searching = false;
        computerButton.setDisabled(false);
        fenButton.setDisabled(false);
        backButton.setDisabled(false);
    }

    @Override
    public void onServerUpdate() {
        usersLabel.setText("Online players: " +model.currentUsers());
    }

    @Override
    public void onUserUpdate() {
        System.out.println(model.user());
        useridLabel.setText(model.user().userid() + " (" + model.user().level() + ")");
        statsLabel.setText("Games: " + model.user().games() + "       Wins: " + model.user().wins());
    }
}
