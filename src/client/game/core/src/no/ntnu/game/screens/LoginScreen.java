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
		final TextField passwordField = new TextField("Password", skin);
		usernameField.setAlignment(1); // center
		passwordField.setAlignment(1); // center
		
		// Button
		final TextButton loginButton = new TextButton("LOGIN", skin);
		final TextButton registerButton = new TextButton("REGISTER", skin);
		
		// Listeners
		loginButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//game.login(usernameField.getText(), passwordField.getText());
				game.setScreen(new MenuScreen(game));
			}
		});

		registerButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//game.login(usernameField.getText(), passwordField.getText());
				game.setScreen(new RegisterScreen(game));
			}
		});

		usernameField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (usernameField.getText().equals("Username")) {
					usernameField.setText("");
				}
			}
		});

		passwordField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (passwordField.getText().equals("Password")) {
					usernameField.setText("");
				}
			}
		});

		table.add(usernameField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(passwordField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(loginButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(registerButton).width(objectWidth).height(objectHeight);
	}
}
