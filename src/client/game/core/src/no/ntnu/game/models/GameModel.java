package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;

public class GameModel extends ObservableModel {
    private User currentUser;
    private Room currentRoom; // change to game


    // Statistics from server
    private String currentUsers;
    private String currentQueue;
    private String currentGames;

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
    public void updateStatistics(String users, String queue, String games) {
        currentUsers = users;
        currentQueue = queue;
        currentGames = games;
        emitChanges();
    }

    // add message to current room
    public void addMessage(Message message) {
        currentRoom.addMessage(message);
        emitMessage();
    }

    public User getUser() {
        return currentUser;
    }

    public Room getRoom() {
        return currentRoom;
    }

    public String getCurrentUsers() {
        return currentUsers;
    }

    public String getCurrentQueue() {
        return currentQueue;
    }

    public String getCurrentGames() {
        return currentGames;
    }
}
