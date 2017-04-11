package no.ntnu.game.network;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.NetJavaImpl;
import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.models.User;
import no.ntnu.game.util.NetworkObserver;

/**
 * Created by thomash on 19.03.2017.
 */

public class HttpCommunication extends NetworkCommunication {

    public HttpCommunication(NetworkObserver observer, HostInfo hostInfo) {
        super(observer, hostInfo);
    }

    public void login(final User user) {
        // sends a login auth post call, if success
        sendPostRequest("/authenticate", serializer.write(user), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    user.setToken(response.getString("token"));
                    user.setFen(response.getString("fen"));
                    user.setLevel(response.getInt("level"));
                    emitLogin(user);
                } else {
                    emitError("Authentication failed");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    private void sendPostRequest(String route, String data, Net.HttpResponseListener listener) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        String url = getRouteUrl(apiPath+route).toString();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(url).build();
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setContent(data);
        NetJavaImpl net = new NetJavaImpl();

        net.sendHttpRequest(httpRequest, listener);
    }

    public void sendGetRequest(String route, Net.HttpResponseListener listener) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        String url = getRouteUrl(apiPath+route).toString();

        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
        httpRequest.setHeader("Content-Type", "application/json");
        NetJavaImpl net = new NetJavaImpl();

        net.sendHttpRequest(httpRequest, listener);
    }
}