package no.ntnu.game;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MySocket {
	private Socket mySocket;
	private String url = "http://aqueous-bayou-80568.herokuapp.com";
	private String id;
	private String username;
	
	public MySocket () {
		Gdx.app.log("ANDYPANDY", "Socket connection - start");
		try {
			mySocket = IO.socket(url);
			mySocket.on(Socket.EVENT_CONNECT, onConnect);
			mySocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
			mySocket.on("login", onLogin);
			mySocket.on("message", onMessage);
			mySocket.connect();
		} catch(Exception e){
			Gdx.app.log("ANDYPANDY", e.getMessage());
		}
		Gdx.app.log("ANDYPANDY", "Socket connection - done");
	}
	
	public void disconnect() {
		mySocket.disconnect();
	}
	
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        	Gdx.app.log("ANDYPANDY", "Connected to server");
        }
    };
    
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        	JSONObject data = (JSONObject) args[0];
        	String username = getStringFromJSON("username", data);
        	Gdx.app.log("ANDYPANDY", username + " left the server");
        }
    };
    
    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        	JSONObject data = (JSONObject) args[0];
        	id = getStringFromJSON("id", data);
        	username = getStringFromJSON("username", data);
        	Gdx.app.log("ANDYPANDY", "id: " + id + ", username: " + username);
        }
    };
    
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        	JSONObject data = (JSONObject) args[0];
        	String message = getStringFromJSON("message", data);
        	String name = getStringFromJSON("username", data);
        	Gdx.app.log("ANDYPANDY", name+ ": "+message);
        }
    };
    
    // Send username to server for login
    public void login(String username) {
    	JSONObject data = new JSONObject();
		try {
			data.put("username", username);
			mySocket.emit("login", data);
		} catch (JSONException e) {
			Gdx.app.log("ANDYPANDY", e.getMessage());
		}
    }
    
    // send message to server
    public void sendMessage(String message) {
    	JSONObject data = new JSONObject();
		try {
			data.put("message", message);
			mySocket.emit("message", data);
		} catch (JSONException e) {
			Gdx.app.log("ANDYPANDY", e.getMessage());
		}
    }
    
    // get string from json
    private String getStringFromJSON(String s, JSONObject data) {
    	try {
			return data.getString(s);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return "";
    }
	
}
