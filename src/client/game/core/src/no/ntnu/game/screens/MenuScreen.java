package no.ntnu.game.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.MyGame;

public class MenuScreen extends MyScreen {
	
	public MenuScreen(MyGame game) {
		super(game);
	}

	@Override
	void build() {
		// Button
		final TextButton playButton = new TextButton("PLAY", skin);
		playButton.setPosition(400, 1500);
		playButton.setSize(600, 200);
		stage.addActor(playButton);
		
		// Button
		final TextButton settingsButton = new TextButton("OPTIONS", skin);
		settingsButton.setPosition(400, 1200);
		settingsButton.setSize(600, 200);
		stage.addActor(settingsButton);
		
		// Button
		final TextButton creditsButton = new TextButton("CREDITS", skin);
		creditsButton.setPosition(400, 900);
		creditsButton.setSize(600, 200);
		stage.addActor(creditsButton);
		
		// Listeners
		playButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen(game));
			}
		});
		
		settingsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new SettingsScreen(game));
			}
		});
		
		creditsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				
			}
		});
		
	}
}
