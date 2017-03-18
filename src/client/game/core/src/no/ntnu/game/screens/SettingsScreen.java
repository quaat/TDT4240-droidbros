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
		final TextButton musicButton = new TextButton("MUSIC", skin);
		final TextButton vibrationButton = new TextButton("VIBRATION", skin);
		final TextButton backButton = new TextButton("BACK", skin);
		
		// Listeners
		musicButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {

			}
		});
		
		vibrationButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				
			}
		});
		
		backButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new MenuScreen(game));
			}
		});

		table.add(musicButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(vibrationButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(backButton).width(objectWidth).height(objectHeight);
	}
}
