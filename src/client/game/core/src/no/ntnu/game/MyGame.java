package no.ntnu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.game.models.User;
import no.ntnu.game.network.HostInfo;
import no.ntnu.game.network.HttpCommunication;
import no.ntnu.game.network.MyNetwork;
import no.ntnu.game.network.NetworkCommunication;
import no.ntnu.game.util.NetworkObserver;
import no.ntnu.game.views.LoginScreen;
import no.ntnu.game.views.MenuScreen;
import no.ntnu.game.views.RegisterScreen;


public class MyGame extends Game implements NetworkObserver {
	NetworkCommunication networkComm = null;
	HostInfo hostInfo = new HostInfo("192.168.22.1", 8081);
	User user = null;
	String token = null;
	//MyNetwork myNetwork;
	
	@Override
	public void create () {
		Gdx.app.log("ANDYPANDY", "game init");
		//myNetwork = new MyNetwork(this);
		networkComm = new HttpCommunication(hostInfo);
		networkComm.addObserver(this);
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
		user = new User(username, password);
		networkComm.login(user);
		//myNetwork.login(username, password);
	}

	public void register(String username, String password) {
		user = new User(username, password);
		//networkComm.login(user);
	}
	//	myNetwork.register(username, password);
	//}
	
	// delete
	public String getToken() {

		return this.token;
	}
	
	public User getUser() {
		return user;
	}
	//	return myNetwork.getUser();
	//}
	
	public void sendMessage() {
		
	}

	@Override
	public void onConnected() {

	}

	@Override
	public void onLogin(String response) {

		this.token = response;
		changeScreenHelper(0);
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onError(String error) {

	}

	public void changeScreenHelper(final int screen) {
		final Game game = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						if (screen==0) game.setScreen(new MenuScreen((MyGame)game));
						else if (screen==1) game.setScreen(new LoginScreen((MyGame)game));
						else if (screen==2) game.setScreen(new RegisterScreen((MyGame)game));
					}
				});
			}
		}).start();
	}
}
