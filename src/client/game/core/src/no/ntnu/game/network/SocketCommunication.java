package no.ntnu.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import no.ntnu.game.util.NetworkObserver;

public class SocketCommunication extends NetworkCommunication {

    private Socket socket;

    public SocketCommunication(NetworkObserver observer, HostInfo hostInfo) {
        super(observer, hostInfo);
    }

    /**
     * Connect socket to server with token auth
     * @param token - user token
     */
    public void connect(String token) {
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
            socket.on("gameOver", onGameOver);

            socket.connect();
        } catch(Exception e){
            Gdx.app.log("ANDYPANDY", "socket, failed to connect to server");
        }
    }

    /**
     * Disconnect from socket
     */
    public void disconnect() {
        socket.disconnect();
    }

    /**
     * Updates user board with new fen
     */
    public void updateUserBoard(String newFen) {
        socket.emit("updateUserBoard", newFen);
    }
    /**
     * Find another player to play against
     */
    public void findGame() {
        socket.emit("findGame");
    }

    /**
     * Join a game that is ready for you
     */
    public void joinGame(String gameid) {
        socket.emit("joinGame", gameid);
    }

    /**
     * Emit a new move to the server
     * @param fen - fen string
     */
    public void doMove(String fen) {
        socket.emit("newMove", fen);
    }

    /**
     * Give up the game
     */
    public void resign() {
        socket.emit("resign");
    }

    /**
     * Called when client successfully connects to server
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "Socket connected");
        }
    };

    /**
     * Called when client disconnects from server
     */
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gdx.app.log("ANDYPANDY", "Socket disconnected");
        }
    };

    /**
     * Called when server emits update to client socket.
     */
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

    /**
     * Called when server emits that a game is ready for the client
     */
    private Emitter.Listener onGameReady = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            String gameid = response.getString("gameid");
            joinGame(gameid);
        }
    };

    /**
     * Called when server emits that clients game has started
     */
    private Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            Gdx.app.log("ANDYPANDY", "game started");
            emitStartGame(response);
        }
    };

    /**
     * Called when server emits that a new move has been done
     */
    private Emitter.Listener onNewMove = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            String fen = response.getString("fen");
            emitNewMove(fen);
        }
    };

    /**
     * Called when server emits that the game is over
     */
    private Emitter.Listener onGameOver = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JsonValue response = (JsonValue)serializer.read(args[0].toString());
            Gdx.app.log("ANDYPANDY", args[0].toString());
            String winner = response.getString("winner");
            emitGameOver(winner);
        }
    };
}