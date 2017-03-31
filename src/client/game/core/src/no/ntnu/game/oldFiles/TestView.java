/*package no.ntnu.game.oldFiles;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Message;
import no.ntnu.game.views.AbstractView;

public class TestView extends AbstractView {

    private final String roomText = "Room id: ";
    private final String usersText = "Users: ";

    private Label roomLabel;
    private Label usersLabel;
    private Label message1Label;
    private Label message2Label;
    private Label message3Label;
    private Label message4Label;

    public TestView(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void build() {
        // Button
        final TextButton joinButton = new TextButton("JOIN", skin);
        final TextButton sendButton = new TextButton("SEND", skin);

        // Label
        roomLabel = new Label(roomText + "#", skin);
        usersLabel = new Label(usersText + "#", skin);
        message1Label = new Label("", skin);
        message2Label = new Label("", skin);
        message3Label = new Label("", skin);
        message4Label = new Label("", skin);

        // Textfield
        final TextField roomField = new TextField("123", skin);
        final TextField messageField = new TextField("Yo!", skin);

        // Listeners
        joinButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.joinRoom(roomField.getText());
            }
        });

        sendButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.sendMessage(messageField.getText());
            }
        });

        table.add(roomLabel).width(objectWidth).height(objectHeight).row();
        table.add(usersLabel).width(objectWidth).height(objectHeight).padBottom(padY*5).row();
        table.add(message1Label).width(objectWidth).height(objectHeight).row();
        table.add(message2Label).width(objectWidth).height(objectHeight).row();
        table.add(message3Label).width(objectWidth).height(objectHeight).row();
        table.add(message4Label).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(roomField).width(objectWidth).height(objectHeight).padBottom(padY);
        table.add(joinButton).width(objectWidth/3).height(objectHeight).padBottom(padY).row();
        table.add(messageField).width(objectWidth).height(objectHeight).padBottom(padY);
        table.add(sendButton).width(objectWidth/3).height(objectHeight).padBottom(padY).row();
    }

    @Override
    public void onUpdate() {
        roomLabel.setText(roomText+model.getRoom().getRoomid());
        usersLabel.setText(usersText+model.getRoom().getUsers());
    }

    @Override
    public void onMessage() {
        List<Message> messages = model.getRoom().getMessages();
        message1Label.setText(messages.get(messages.size() - 4).toString());
        message2Label.setText(messages.get(messages.size() - 3).toString());
        message3Label.setText(messages.get(messages.size() - 2).toString());
        message4Label.setText(messages.get(messages.size() - 1).toString());
    }
}
*/
