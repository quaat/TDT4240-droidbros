package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class RegisterView extends AbstractView {

    private static final String usernamePlaceholder = "Username";
    private static final String passwordPlaceholder = "Password";

    private static final String registerWaitText = "Registering user...";

    private static final String backButtonText = "BACK";
    private static final String registerButtonText = "REGISTER";

    private Label statusLabel;
    private TextField usernameField;
    private TextField passwordField;

    public RegisterView(GameModel model, GameController controller) {
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
        TextButton backButton = new TextButton(backButtonText, skin);
        TextButton registerButton = new TextButton(registerButtonText, skin);
        backButton.setColor(buttonColor);
        registerButton.setColor(buttonColor);

        // Label
        statusLabel = new Label("", skin);

        // Listeners
        registerButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                if (usernameField.getText().equals("")) statusLabel.setText("Missing username");
                else if (passwordField.getText().equals("")) statusLabel.setText("Missing password");
                else {
                    statusLabel.setText(registerWaitText);
                    controller.register(usernameField.getText(), passwordField.getText(), "noname", "noemail");
                }
            }
        });

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                controller.toLogin();
            }
        });

        usernameField.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                if (usernameField.getText().equals(usernamePlaceholder)) usernameField.setText("");
            }
        });

        passwordField.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                passwordField.setText("");
                if (passwordField.getText().equals(passwordPlaceholder)) passwordField.setText("");
            }
        });

        // Add to table
        table.add(usernameField).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(passwordField).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(registerButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(backButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(statusLabel).width(objectWidth).height(objectHeight);
    }

    @Override
    public void reset() {
        statusLabel.setText("");
    }

    @Override
    public void onError() {statusLabel.setText(model.error());}
}
