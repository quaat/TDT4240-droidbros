package no.ntnu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import no.ntnu.game.screens.LoginScreen;

public class MyGame extends Game{
	private MySocket mySocket;
	
	@Override
	public void create () {
		Gdx.app.log("ANDYPANDY", "game init");
		mySocket = new MySocket();
		setScreen(new LoginScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		getScreen().dispose();
	}
	
	public void login(String username) {
		mySocket.login(username);
	}
	
	public void sendMessage() {
		mySocket.sendMessage("CLICK CLICK");
	}
	
	
}
