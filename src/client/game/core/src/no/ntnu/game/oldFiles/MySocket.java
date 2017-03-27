/*
package no.ntnu.game.oldFiles;

import com.badlogic.gdx.Gdx;

import no.ntnu.game.MyGame;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MySocket {
    private MyGame game;
    private Socket mySocket;
    private String host;
    private String token;

    public MySocket (MyGame game, String host) {
        this.game = game;
        this.host = host;
    }

    public void connect(String token) {
        this.token = token;
        Gdx.app.log("ANDYPANDY", "try to connect socket, token: "+token);
        try {
            IO.Options opts = new IO.Options();
            opts.query = "token="+token;

            mySocket = IO.socket(host, opts);
            mySocket.on(Socket.EVENT_CONNECT, onConnect);
            mySocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mySocket.on("welcome", onWelcome);
            mySocket.on("message", onMessage);
            mySocket.on("join", onJoin);
            mySocket.connect();
        } catch(Exception e){
            Gdx.app.log("ANDYPANDY", e.getMessage());
        }
        Gdx.app.log("ANDYPANDY", "Socket connection works");
    }

    public void disconnect() {
        mySocket.disconnect();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "CONNECTED to server");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "DISCONNECTED from server");

        }
    };

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //JSONObject data = (JSONObject) args[0];
            Gdx.app.log("ANDYPANDY", args.toString());
            //((TestScreen)game.getScreen()).updateMessageHelper("yo", "hey");
        }
    };

    private Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //JSONObject data = (JSONObject) args[0];
            //((TestScreen)game.getScreen()).updateRoomHelper("yo", "hey", "sup");
        }
    };

    private Emitter.Listener onWelcome = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "WHALECUM");
        }
    };

    // send message to server
    public void sendMessage(String message) {
        mySocket.emit("message", message);
    }

    public void joinRoom(String room) {
        mySocket.emit("join", room);
    }
}

*/