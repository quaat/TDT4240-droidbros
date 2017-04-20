package no.ntnu.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class SettingView extends AbstractView {

    private boolean music = true;
    private boolean sound = true;
    private boolean notis = true;

    public SettingView(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Button
        final TextButton musicButton = new TextButton("MUSIC ON", skin);
        final TextButton soundButton = new TextButton("SOUND ON", skin);
        final TextButton notisButton = new TextButton("NOTIFICATIONS ON", skin);
        final TextButton backButton = new TextButton("BACK", skin);

        musicButton.setColor(buttonColor);
        soundButton.setColor(buttonColor);
        notisButton.setColor(buttonColor);
        backButton.setColor(buttonColor);

        musicButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (music) {
                    // todo turn off music
                    musicButton.setText("MUSIC OFF");
                    music = false;
                } else {
                    // todo turn on music
                    musicButton.setText("MUSIC ON");
                    music = true;
                }
            }
        });

        soundButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (music) {
                    // todo turn off sound
                    soundButton.setText("SOUND OFF");
                    sound = false;
                } else {
                    // todo turn on sound
                    soundButton.setText("SOUND ON");
                    sound = true;
                }
            }
        });

        notisButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (music) {
                    // todo turn off notis
                    notisButton.setText("NOTIFICATIONS OFF");
                    notis = false;
                } else {
                    // todo turn on notis
                    notisButton.setText("NOTIFICATIONS ON");
                    notis = true;
                }
            }
        });
        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.toMenu();
            }
        });

        table.add(musicButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(soundButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(notisButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(backButton).width(objectWidth).height(objectHeight);
    }

    @Override
    public void reset() {
    }
}