package no.ntnu.game.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.game.MyGame;

public class LoginScreen extends MyScreen {
	
	public LoginScreen(MyGame game) {
		super(game);
	}

	@Override
	void build() {
		// Textfield
		final TextField usernameField = new TextField("Username", skin);
		usernameField.setPosition(300, 1400);
		usernameField.setSize(800, 200);
		usernameField.setAlignment(1);
		stage.addActor(usernameField);
		
		// Button
		final TextButton loginButton = new TextButton("JOIN", skin);
		loginButton.setPosition(400, 1100);
		loginButton.setSize(600, 200);
		stage.addActor(loginButton);
		
		// Listeners
		loginButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.login(usernameField.getText());
				game.setScreen(new MenuScreen(game));
			}
		});
		
		usernameField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (usernameField.getText().equals("Username")) {
					usernameField.setText("");
				}
			}
		});
		
	}
}
