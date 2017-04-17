package no.ntnu.game.network;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.NetJavaImpl;
import com.badlogic.gdx.utils.JsonValue;

import no.ntnu.game.models.User;

public class HttpCommunication extends NetworkCommunication {

    public HttpCommunication(HostInfo hostInfo) {
        super(hostInfo);
    }

    public void login(String userid, String password) {
        LoginJson json = new LoginJson(userid, password);
        sendPostRequest("/authenticate", serializer.write(json), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response =  (JsonValue) serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    User user = new User(response.get("user"));
                    emitLogin(user);
                } else {
                    emitError("Authentication failed");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    public void register(String userid, String password, String name, String email) {
        RegisterJson json = new RegisterJson(userid, password, name, email);
        sendPostRequest("/register", serializer.write(json), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue) serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    emitRegister();
                } else {
                    emitError("Register failed");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    public void getUserInformation(String token) {
        sendGetRequest("/user", token, new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                System.out.println(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    User user = new User(response.get("user"));
                    emitGetUser(user);
                } else {
                    emitError("Get user fail");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    // todo fetch games
    public void getGames(String token) {
        sendGetRequest("/games", token, new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    emitGetGames(/* Something */);
                } else {
                    emitError("Get games fail");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    public void changePassword(String token, String oldPassword, String newPassword) {
        PasswordJson json = new PasswordJson(token, oldPassword, newPassword);
        sendPostRequest("/user/password", serializer.write(json), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    emitChangedPassword();
                } else {
                    Gdx.app.log("ANDYPANDY", response.getString("message"));
                    emitError("Change password fail");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    public void changeFen(String token, String fen) {
        ChangeFenJson json = new ChangeFenJson(token, fen);
        sendPostRequest("/user", serializer.write(json), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    emitChangedFen();
                } else {
                    emitError("Change fen failed");
                }
            }
            public void failed(Throwable t) { }
            public void cancelled() { }
        });
    }

    public void deleteUser(String token, String password) {
        PasswordJson json = new PasswordJson(token, password, null);
        sendPostRequest("/user/delete", serializer.write(json), new Net.HttpResponseListener() {
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                JsonValue response = (JsonValue)serializer.read(httpResponse.getResultAsString());
                if (response.getBoolean("success")) {
                    emitDeletedUser();
                } else {
                    emitError("Delete user fail");
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

    public void sendGetRequest(String route, String token, Net.HttpResponseListener listener) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        String url = getRouteUrl(apiPath+route).toString();

        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
        httpRequest.setHeader("x-access-token", token);
        NetJavaImpl net = new NetJavaImpl();

        net.sendHttpRequest(httpRequest, listener);
    }

    /* Used for creating json, must be better another way!*/
    private class RegisterJson {
        private String userid;
        private String password;
        private String name;
        private String email;

        private RegisterJson (String userid, String password, String name, String email) {
            this.userid = userid;
            this.password = password;
            this.name = name;
            this.email = email;
        }
    }

    private class LoginJson {
        private String userid;
        private String password;

        private LoginJson (String userid, String password) {
            this.userid = userid;
            this.password = password;
        }
    }

    private class ChangeFenJson {
        private String token;
        private String fen;

        private ChangeFenJson (String token, String fen){
            this.token = token;
            this.fen = fen;
        }
    }

    private class PasswordJson {
        private String token;
        private String oldPassword;
        private String newPassword;

        private PasswordJson(String token, String oldPassword, String newPassword) {
            this.token = token;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }
    }

    private class TokenJson {
        private String token;

        private TokenJson(String token) {
            this.token = token;
        }
    }
}