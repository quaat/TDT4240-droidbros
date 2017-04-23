package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class MenuView extends AbstractView {

	public MenuView(GameModel model, GameController controller) {
		super(model, controller);
	}

	@Override
	public void build() {
		// Button
		final TextButton playButton = new TextButton("PLAY", skin);
		final TextButton tutorialButton = new TextButton("TUTORIAL", skin);
		final TextButton settingsButton = new TextButton("OPTIONS", skin);
		final TextButton aboutButton = new TextButton("ABOUT", skin);

		playButton.setColor(buttonColor);
		tutorialButton.setColor(buttonColor);
		settingsButton.setColor(buttonColor);
		aboutButton.setColor(buttonColor);

		// Listeners
		playButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				controller.toGame();
			}
		});

		tutorialButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				controller.toTutorial();
			}
		});

		settingsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				controller.toSetting();
			}
		});

		aboutButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				controller.toAbout();
			}
		});

		table.add(playButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(tutorialButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(settingsButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(aboutButton).width(objectWidth).height(objectHeight);
	}

	@Override
	public void reset() {

	}
}