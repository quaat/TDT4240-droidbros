package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;

public class GameModel extends ObservableModel {
    private User currentUser;
    private Room currentRoom; // change to game


    // Length of queue
    private String currentQueue;
    private String currentUsersOnline;

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

    // update queue size
    public void setQueue(String queue) {
        currentQueue = queue;
        emitChanges();
    }

    public void setCurrentUsersOnline(String users) {
        currentUsersOnline =  users;
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

    public String getQueue() {
        return currentQueue;
    }

    public String getCurrentUsersOnline() {
        return currentUsersOnline;
    }
}
