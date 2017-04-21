package no.ntnu.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class LoginView extends AbstractView {

	private static final String usernamePlaceholder = "Username";
	private static final String passwordPlaceholder = "Password";

	private static final String loginWaitText = "Logging in...";

	private TextField usernameField;
	private TextField passwordField;
	private Label statusLabel;

	public LoginView(GameModel model, GameController controller) {
		super(model, controller);
	}

	@Override
	public void build() {
		// Textfield
		usernameField = new TextField(usernamePlaceholder, skin);
		passwordField = new TextField(passwordPlaceholder, skin);
		usernameField.setAlignment(1);
		passwordField.setAlignment(1);

		// Button

		TextButton loginButton = new TextButton("LOGIN", skin);
		TextButton registerButton = new TextButton("REGISTER", skin);
		loginButton.setColor(buttonColor);
		registerButton.setColor(buttonColor);
		// Label
		statusLabel = new Label("", skin);

		// Listeners
		loginButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (usernameField.getText().equals("")) statusLabel.setText("Missing username");
				else if (passwordField.getText().equals("")) statusLabel.setText("Missing password");
				else {
					statusLabel.setText(loginWaitText);
					System.out.println("LOGIN");
					controller.login(usernameField.getText(), passwordField.getText());
				}
			}
		});

		registerButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				controller.toRegister();
			}
		});

		/*
		playComputerButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				controller.playComputer();
			}
		});
		*/

		usernameField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (usernameField.getText().equals(usernamePlaceholder)) usernameField.setText("");
			}
		});

		passwordField.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (passwordField.getText().equals(passwordPlaceholder)) passwordField.setText("");
			}
		});

		// Add to table
		table.add(usernameField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(passwordField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(loginButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(registerButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		//table.add(playComputerButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(statusLabel).width(objectWidth).height(objectHeight);
	}

	@Override
	public void reset() {
		statusLabel.setText("");
	}

	@Override
	public void onError() {
		statusLabel.setText(model.error());
	}
}
