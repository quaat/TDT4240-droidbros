package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;

public class GameModel extends ObservableModel {
    private User currentUser;
    private Room currentRoom;

    public GameModel() {
        setRoom(new Room(""));
    }

    // set current user
    public void setUser(User user) {
        currentUser = new User(user);
        emitChanges();
    }

    // set current room
    public void setRoom(Room room) {
        currentRoom = new Room(room);
        emitChanges();
    }

    // add message to current room
    public void addMessage(Message message) {
        currentRoom.addMessage(message);
        Gdx.app.log("ANDYPANDY", "message add");
        emitMessage();
    }

    public User getUser() {
        return currentUser;
    }

    public Room getRoom() {
        return currentRoom;
    }
}
