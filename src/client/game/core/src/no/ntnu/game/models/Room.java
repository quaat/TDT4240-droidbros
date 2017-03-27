package no.ntnu.game.models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomid;
    private List<String> users = new ArrayList<String>();
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

    public void addUser(String user) {
        users.add(user);
    }

    public void setUser(List<String> users) {
        this.users = users;
    }

    public void setUsers() {
        // ADD MANY USERS!!
    }
    public String getRoomid() {
        return roomid;
    }

    public List<String> getUsers() {
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
