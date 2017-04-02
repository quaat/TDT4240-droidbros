package no.ntnu.game.models;

/**
 *
 */
public class User {
	private String userid;
	private String password;
	private String token;

	public User(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public User(User user) {
		this.userid = user.userid;
		this.password = user.password;
		this.token = user.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getUserid() {
		return userid;
	}

}