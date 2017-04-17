package no.ntnu.game.models;

import com.badlogic.gdx.utils.JsonValue;

public class User {
	private String userid;
	private String name;
	private String email;
	private String token;
	private String fen;
	private int level;

	/**
	 * Reads the values from json to this object
	 * @param json from server
	 */
	public User(JsonValue json) {
		if (json.has("userid")) userid = json.getString("userid");
		if (json.has("name")) name = json.getString("name");
		if (json.has("email")) fen = json.getString("email");
		if (json.has("token")) token = json.getString("token");
		if (json.has("fen")) fen = json.getString("fen");
		if (json.has("level")) level = json.getInt("level");

	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String userid() {
		return userid;
	}

	public String name() {
		return name;
	}

	public String email() {
		return email;
	}

	public String token() {
		return token;
	}

	public String fen() {
		return fen;
	}

	public int level() {
		return level;
	}

	public String toString() {
		return "userid: " + userid;
	}
}