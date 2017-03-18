package no.ntnu.game.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.game.MyGame;

public class RegisterScreen extends MyScreen {
	
	public RegisterScreen(MyGame game) {
		super(game);
	}

	@Override
	void build() {
		// Textfield
		final TextField emailField = new TextField("Email", skin);
		final TextField usernameField = new TextField("Username", skin);
		final TextField passwordField = new TextField("Password", skin);
		usernameField.setAlignment(1);
		
		// Button
		final TextButton registerButton = new TextButton("REGISTER", skin);

		
		// Listeners
		registerButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//game.register(data);
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

		table.add(emailField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(usernameField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(passwordField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(registerButton).width(objectWidth).height(objectHeight);
	}
}
