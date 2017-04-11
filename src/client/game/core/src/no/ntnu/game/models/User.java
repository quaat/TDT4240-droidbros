package no.ntnu.game.models;

import com.badlogic.gdx.Gdx;

/**
 *
 */
public class User {
	private String userid;
	private String password;
	private String token;
	private String fen;
	private int level;

	public User(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public User(User user) {
		this.userid = user.userid;
		this.password = user.password;
		this.token = user.token;
		this.fen = user.fen;
		this.level = user.level;
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

	public String token() {
		return token;
	}

	public String userid() {
		return userid;
	}

	public String fen() {
		return fen;
	}

	public int level() {
		return level;
	}

}