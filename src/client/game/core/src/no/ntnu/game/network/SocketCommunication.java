package no.ntnu.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;

public class SocketCommunication extends NetworkCommunication {

    private Socket socket;

    public SocketCommunication(HostInfo hostInfo) {
        super(hostInfo);
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
     * Find another player to play against
     */
    public void findGame() {
        socket.emit("findGame");
    }

    /**
     * Join a game that is ready for you
     * @param gameid gameid
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
    private Listener onConnect = args -> emitConnected();

    /**
     * Called when client disconnects from server
     */
    private Listener onDisconnect = args -> emitDisconnected();

    /**
     * Called when server emits update to client socket.
     */
    private Listener onUpdate = args -> {
        JsonValue response = (JsonValue)serializer.read(args[0].toString());
        String users = response.getString("users");
        String queue = response.getString("queue");
        String games = response.getString("games");
        emitUpdate(users, queue, games);
    };

    /**
     * Called when server emits that a game is ready for the client
     */
    private Listener onGameReady = args -> {
        JsonValue response = (JsonValue)serializer.read(args[0].toString());
        String gameid = response.getString("gameid");
        joinGame(gameid);
    };

    /**
     * Called when server emits that clients game has started
     */
    private Listener onStartGame = args -> {
        JsonValue response = (JsonValue)serializer.read(args[0].toString());
        emitStartGame(response);
    };

    /**
     * Called when server emits that a new move has been done
     */
    private Listener onNewMove = args -> {
        JsonValue response = (JsonValue)serializer.read(args[0].toString());
        String fen = response.getString("fen");
        emitNewMove(fen);
    };

    /**
     * Called when server emits that the game is over
     */
    private Listener onGameOver = args -> {
        JsonValue response = (JsonValue)serializer.read(args[0].toString());
        emitGameOver(response);
    };
}