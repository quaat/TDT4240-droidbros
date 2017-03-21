package no.ntnu.game.network;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.NetJavaImpl;
import com.badlogic.gdx.utils.JsonValue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.models.User;
import no.ntnu.game.util.AbstractSerializer;
import no.ntnu.game.util.JsonSerializer;
import no.ntnu.game.util.NetworkObserver;

/**
 * Created by thomash on 19.03.2017.
 */

public class HttpCommunication extends NetworkCommunication {
    private static final String apiPath = "api/";

    private String token;
    private AbstractSerializer serializer = new JsonSerializer();
    private final String protocol = "http";
    private List<NetworkObserver> observers = new ArrayList<NetworkObserver>();

    public HttpCommunication() {
        super();
    }

    public HttpCommunication(HostInfo hostInfo) {
        super(hostInfo);
    }

    private URL getRouteUrl(String route) {
        URL url = null;
        try {
            url = new URL(this.protocol, hostInfo().hostAddress(), hostInfo().port(), route);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public void login(User user) {
        // sends a login auth post call, if success
        sendPostRequest("/authenticate", serializer.write(user), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    token = response.getString("token");
                    emitLogin(token);
                    //changeScreenHelper(0); // NO! Let listener take care of this
                } else {
                    emitError("Authentication failed");
                    // changeScreenHelper(1); // // NO! Let listener take care of this
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
