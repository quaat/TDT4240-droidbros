package no.ntnu.game.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;

public class MenuView extends AbstractView {

	private final String queueText = "Users in queue: ";
	private final String usersText = "Users online: ";
	private Label queueLabel;
	private Label usersLabel;
	private Label statusLabel;
	
	public MenuView(GameModel model, GameController controller) {
		super(model, controller);
	}

	@Override
	public void build() {
		// Button
		final TextButton playButton = new TextButton("PLAY", skin);
		final TextButton setupButton = new TextButton("BOARD SETUP", skin);
		final TextButton creditsButton = new TextButton("DUMMY", skin);

		// Label
		queueLabel = new Label("", skin);
		usersLabel = new Label("", skin);
		statusLabel = new Label("", skin);
		
		// Listeners
		playButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				controller.findGame();
				statusLabel.setText("Searching for opponent...");
			}
		});

		setupButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				controller.toSetup();
			}
		});
		
		creditsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {

			}
		});
		table.add(queueLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(usersLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(statusLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(playButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(setupButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
		table.add(creditsButton).width(objectWidth).height(objectHeight);
	}

	@Override
	public void onServerUpdate() {
		queueLabel.setText(queueText + model.currentQueue());
		usersLabel.setText(usersText + model.currentUsers());
	}

	@Override
	public void onGameUpdate() {
		statusLabel.setText("Winner is: " + model.winner());
	}
}