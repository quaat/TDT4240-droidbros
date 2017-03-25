package no.ntnu.game.models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomid;
    private String users;
    //private List<String> users = new ArrayList<String>();
    private List<Message> messages = new ArrayList<Message>();

    public Room(Room room) {
        this.roomid = room.roomid;
        this.users = room.users;
        this.messages = room.messages;
    }

    public Room(String roomid) {
        this.roomid = roomid;
        addBlankMessages();
    }

    private void addBlankMessages() {
        messages.add(new Message());
        messages.add(new Message());
        messages.add(new Message());
        messages.add(new Message());
    }

    //public void addUser(String user) {
    //    users.add(user);
    //}

    // TODO fikse json lista!
    public void addUser(String user) {
        users = user;
    }

    public void addUsers() {
        // ADD MANY USERS!!
    }
    public String getRoomid() {
        return roomid;
    }

    public String getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public String toString() {
        return "Roomid: " + roomid + ", users: " + users;
    }
}
