package no.ntnu.game.network;

import com.badlogic.gdx.Gdx;
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

            socket.on("update", onUpdate);
            socket.on("gameReady", onGameReady);
            socket.on("startGame", onStartGame);
            socket.on("newMove", onNewMove);

            socket.connect();
        } catch(Exception e){
            Gdx.app.log("ANDYPANDY", "socket, failed to connect to server");
        }
    }

    // Disconnect socket from server
    public void disconnect() {
        socket.disconnect();
    }

    public void findGame() {
        socket.emit("findGame");
    }

    public void joinGame(String gameid) {
        socket.emit("joinGame", gameid);
    }

    public void doMove(String id, String state, String move) {
        String data = serializer.write(new NewMove(id, state, move));
        socket.emit("newMove", data);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "Socket connected");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "Socket disconnected");
        }
    };

    private Emitter.Listener onUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            String users = response.getString("users");
            String queue = response.getString("queue");
            String games = response.getString("games");
            emitUpdate(users, queue, games);
        }
    };

    private Emitter.Listener onGameReady = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            Gdx.app.log("ANDYPANDY", response.toString());
            String gameid = response.getString("gameid");
            joinGame(gameid);
        }
    };

    private Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            Gdx.app.log("ANDYPANDY", "********");
            Gdx.app.log("ANDYPANDY", response.get("player1").toString());
            Gdx.app.log("ANDYPANDY", "********");
            //String player1 = response.getChild("player1").getString("name");
            //String gameid = response.getString("gameid");
            //String opponent = response.getString("player1");
            //String color = response.getString("color");
            //emitStartGame(gameid, opponent, color);
        }
    };



    private Emitter.Listener onNewMove = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            Gdx.app.log("ANDYPANDY", response.toString());
            String state = response.getString("state");
            String move = response.getString("move");

            emitNewMove(state, move);
        }
    };

    // Object to send to server
    private class NewMove {
        private String id;
        private String state;
        private String move;

        public NewMove(String id, String state, String move) {
            this.id = id;
            this.state = state;
            this.move = move;
        }
    }
}