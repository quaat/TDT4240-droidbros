package no.ntnu.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import no.ntnu.game.models.Message;
import no.ntnu.game.models.Room;
import no.ntnu.game.util.NetworkObserver;

public class SocketCommunication extends NetworkCommunication {

    private Socket socket;

    public SocketCommunication(NetworkObserver observer, HostInfo hostInfo) {
        super(observer, hostInfo);
    }

    // Connect socket to server
    public void connect(String token) {
        Gdx.app.log("ANDYPANDY", "socket trying to connect");
        try {
            IO.Options options = new IO.Options();
            options.query = "token="+token;

            socket = IO.socket(getRouteUrl("").toString(), options);
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on("join", onJoin); //change name to something else: joinRoom etc?
            socket.on("message", onMessage);
            socket.on("userJoined", onUserJoined); // TODO

            socket.connect();
        } catch(Exception e){
            Gdx.app.log("ANDYPANDY", "socket, failed to connect to server");
        }
    }

    // Disconnect socket from server
    public void disconnect() {
        socket.disconnect();
    }

    // Tell server to join room
    public void joinRoom(Room room) {
        Gdx.app.log("ANDYPANDY", room.getRoomid());
        socket.emit("join", room.getRoomid());
        Gdx.app.log("ANDYPANDY", "emitting");
    }

    // Tell server to leave room
    public void leaveRoom() {
        socket.emit("leave", "i want to leave :(");
    }

    // Send message
    public void sendMessage(Message message) {
        socket.emit("message", serializer.write(message));
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "Socket connected");
            // emitConnected here?
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "Socket disconnected");
        }
    };

    private Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            String roomid = response.getString("roomid");
            List<String> users = new ArrayList<String>();
            JsonValue map = response.getChild("users");
            for (JsonValue entry = map.child; entry != null; entry = entry.next) {
                Gdx.app.log("ANDYPANDY", entry.name + " = " + entry.asString());
                users.add(entry.name);
            }
            Room room = new Room(roomid);
            room.setUser(users);
            emitConnected(room);
        }
    };

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            Message message = new Message(response.getString("time"), response.getString("userid"), response.getString("text"));
            emitMessage(message);
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            List<String> users = new ArrayList<String>();
            JsonValue map = response.getChild("users");
            for (JsonValue entry = map.child; entry != null; entry = entry.next) {
                Gdx.app.log("ANDYPANDY", entry.name + " = " + entry.asString());
                users.add(entry.name);
            }
            emitUserJoined(users);
        }
    };

}