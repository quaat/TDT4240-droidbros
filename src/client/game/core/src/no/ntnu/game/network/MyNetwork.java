package no.ntnu.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.NetJavaImpl;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import no.ntnu.game.MyGame;
import no.ntnu.game.models.User;
import no.ntnu.game.views.LoginScreen;
import no.ntnu.game.views.MenuScreen;
import no.ntnu.game.views.RegisterScreen;

public class MyNetwork {
	private MyGame game;
	//private final String host = "https://fast-crag-60223.herokuapp.com";
	private final String host = "http://192.168.22.1:8081";
	private User user;
	private String token;

	public MyNetwork(MyGame game) {
		this.game = game;
	}
	
	public String getToken() {
		return token;
	}
	
	public User getUser() {
		return user;
	}
	
	// Can't change screen from inside another thread (httpResponse) than the rendering thread
	// This thread runs the code in rendering thread in the next frame
	public void changeScreenHelper(final int screen) {
		new Thread(new Runnable() {
		   @Override
		   public void run() {
		      Gdx.app.postRunnable(new Runnable() {
		         @Override
		         public void run() {
		        	 if (screen==0) game.setScreen(new MenuScreen(game));
		        	 else if (screen==1) game.setScreen(new LoginScreen(game));
		        	 else if (screen==2) game.setScreen(new RegisterScreen(game));
		         }
		      });
			}
		}).start();
	}
	
	// sends a login auth post call, if success
	public void login(String userid, String password) {
		user = new User(userid, password);
		sendPostRequest("/authenticate", writeJson(user), new HttpResponseListener() {
			public void handleHttpResponse (Net.HttpResponse httpResponse) {
				JsonValue response = readJson(httpResponse.getResultAsString());
				if (response.getBoolean("success")) {
					token = response.getString("token");
					changeScreenHelper(0); // menu screen
				} else {
					changeScreenHelper(1); // login screen
				}
			}
			public void failed(Throwable t) { }
			public void cancelled() { }
		});
	}
	
	// NOT WORKING, må endre i server koden til (POST)
	// sends a register (/admin) post call, if success
	public void register(String userid, String password) {
		user = new User(userid, password);
		sendPostRequest("/admin", writeJson(user), new HttpResponseListener() {
			public void handleHttpResponse (Net.HttpResponse httpResponse) {
				JsonValue response = readJson(httpResponse.getResultAsString());
				Gdx.app.log("ANDYPANDY", response.getString("message"));
				if (response.getBoolean("success")) {
					changeScreenHelper(1); // login screen
				} else {
					// change to 2!!!!!! etter at servern er ordna
					changeScreenHelper(1); // register screen
				}
			}
			public void failed(Throwable t) {}
			public void cancelled() {}
		});
	}

	public void sendPostRequest(String route, String data, Net.HttpResponseListener listener) {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		String url = host + "/api" + route;

		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(url).build();
		httpRequest.setHeader("Content-Type", "application/json");
		httpRequest.setContent(data);
		NetJavaImpl net = new NetJavaImpl();
		
		net.sendHttpRequest(httpRequest, listener);
	}

	public void sendGetRequest(String route, Net.HttpResponseListener listener) {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		String url = host + "/api" + route;

		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
		httpRequest.setHeader("Content-Type", "application/json");
		NetJavaImpl net = new NetJavaImpl();
		
		net.sendHttpRequest(httpRequest, listener);
	}

	public JsonValue readJson(String data) {
		JsonReader jsonReader = new JsonReader();
		return jsonReader.parse(data);
	}

	public String writeJson(Object object) {
		Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);
		return json.toJson(object);
	}
	
    public String prettyPrint(JsonValue value){
        JsonValue.PrettyPrintSettings settings = new JsonValue.PrettyPrintSettings();
        settings.outputType = JsonWriter.OutputType.json;
        return value.prettyPrint(settings);
    }
	
}