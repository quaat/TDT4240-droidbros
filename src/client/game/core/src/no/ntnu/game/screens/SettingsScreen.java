package no.ntnu.game.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.MyGame;

public class SettingsScreen extends MyScreen {

	public SettingsScreen(MyGame game) {
		super(game);
	}

	@Override
	void build() {
		// Button
		final TextButton musicButton = new TextButton("MUSIC ON", skin);
		musicButton.setPosition(400, 1500);
		musicButton.setSize(600, 200);
		stage.addActor(musicButton);
		
		// Button
		final TextButton vibrationButton = new TextButton("VIBRATION ON", skin);
		vibrationButton.setPosition(400, 1200);
		vibrationButton.setSize(600, 200);
		stage.addActor(vibrationButton);
		
		// Button
		final TextButton backButton = new TextButton("BACK", skin);
		backButton.setPosition(400, 900);
		backButton.setSize(600, 200);	
		stage.addActor(backButton);
		
		// Listeners
		musicButton.addListener(new ChangeListener() {
			int i = 0;
			public void changed (ChangeEvent event, Actor actor) {
				String text = (i%2==0) ? "MUSIC ON" : "MUSIC OFF";
				musicButton.setText(text);
				i++;
			}
		});
		
		vibrationButton.addListener(new ChangeListener() {
			int i = 0;
			public void changed (ChangeEvent event, Actor actor) {
				String text = (i%2==0) ? "VIBRATION ON" : "VIBRATION OFF";
				vibrationButton.setText(text);
				i++;
			}
		});
		
		backButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new MenuScreen(game));
			}
		});

	}


}
