package no.ntnu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.game.models.User;
import no.ntnu.game.network.MyNetwork;
import no.ntnu.game.views.LoginScreen;


public class MyGame extends Game{
	MyNetwork myNetwork;
	
	@Override
	public void create () {
		Gdx.app.log("ANDYPANDY", "game init");
		myNetwork = new MyNetwork(this);
		setScreen(new LoginScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		getScreen().dispose();
	}
	
	public void login(String username, String password) {
		myNetwork.login(username, password);
	}

	public void register(String username, String password) {
		myNetwork.register(username, password);
	}
	
	// delete
	public String getToken() {
		return myNetwork.getToken();
	}
	
	public User getUser() {
		return myNetwork.getUser();
	}
	
	public void sendMessage() {
		
	}
}
