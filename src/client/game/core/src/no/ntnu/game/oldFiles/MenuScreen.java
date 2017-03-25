/*
package no.ntnu.game.oldFiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.MyGame;
import no.ntnu.game.views.AbstractScreen;
import no.ntnu.game.views.TestScreen;

public class MenuScreen extends AbstractScreen {
	
	public MenuScreen(MyGame game) {
		super(game);
	}

	@Override
	void build() {
		// Button
		final TextButton playButton = new TextButton("PLAY", skin);
		final TextButton settingsButton = new TextButton("OPTIONS", skin);
		final TextButton creditsButton = new TextButton("CREDITS", skin);

		// Label
		final Label statusLabel = new Label("welcome user", skin);
		
		// Listeners
		playButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//game.connect();
				game.setScreen(new TestScreen(game));
				Gdx.app.log("ANDYPANDY", "JOIN GAME");
			}
		});
		
		settingsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new no.ntnu.game.oldFiles.SettingsScreen(game));
			}
		});
		
		creditsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				
			}
		});
		table.add(statusLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(playButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(settingsButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(creditsButton).width(objectWidth).height(objectHeight);
	}

	@Override
	public void onUpdate() {

	}
}
*/