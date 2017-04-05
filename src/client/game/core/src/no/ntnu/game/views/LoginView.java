package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class LoginView extends AbstractView {

	private Label statusLabel;
	
	public LoginView(GameModel model, GameController controller) {
		super(model, controller);
	}

	@Override
	public void build() {
		String userid = "nick";
		String password = "password";

		// Textfield
		final TextField usernameField = new TextField(userid, skin);
		final TextField passwordField = new TextField(password, skin);
		usernameField.setAlignment(1);
		passwordField.setAlignment(1);
		
		// Button
		TextButton loginButton = new TextButton("LOGIN", skin);
		TextButton registerButton = new TextButton("REGISTER", skin);
        TextButton testGameViewButton = new TextButton("Test gameView", skin);

        // Label
		statusLabel = new Label("", skin);

		// Listeners

		// Listeners
		loginButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				// Add some logic for correct input check
				controller.login(usernameField.getText(), passwordField.getText());
			}
		});

		registerButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				// Register call
			}
		});

		usernameField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				usernameField.setText("");
			}
		});

		passwordField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				passwordField.setText("");
			}
		});

        testGameViewButton.addListener(new ChangeListener() {
            @Override
                public void changed(ChangeEvent event, Actor actor) {
                controller.testGameView();
            }
        });
		
		table.add(usernameField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(passwordField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(loginButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(registerButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(statusLabel).width(objectWidth).height(objectHeight).row();
        table.add(testGameViewButton).width(objectWidth).height(objectHeight);
    }

	@Override
	public void onUpdate() {
	}
}
