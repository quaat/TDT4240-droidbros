package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class LoginView extends AbstractView {

	private static final String usernamePlaceholder = "nick";
	private static final String passwordPlaceholder = "password";

	private static final String loginWaitText = "Logging in...";
	private static final String loginPasswordFailText = "Password incorrect";
	private static final String loginUsernameFailText = "Could not find username";

	private Label statusLabel;
	private TextField usernameField;
	private TextField passwordField;
	
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
		
		// Label
		statusLabel = new Label("", skin);

		// Listeners
		loginButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				statusLabel.setText(loginWaitText);
				controller.login(usernameField.getText(), passwordField.getText());
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

		// Add to table
		table.add(usernameField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(passwordField).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(loginButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(registerButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(statusLabel).width(objectWidth).height(objectHeight);
	}

	public void loginPasswordFailed() {
		statusLabel.setText(loginPasswordFailText);
	}

	public void loginUsernameFailed() {
		statusLabel.setText(loginUsernameFailText);
	}

}
