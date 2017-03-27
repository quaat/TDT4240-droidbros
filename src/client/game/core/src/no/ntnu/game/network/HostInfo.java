package no.ntnu.game.network;

/**
 * Created by thomash on 19.03.2017.
 */

public class HostInfo{
    private String address;
    private int port;
    public HostInfo(String address) {
        this.address = address;
    }

    public HostInfo(String address, int port) {
        this.address = address;
        this.port = port;
    }

    String hostAddress() {
        return this.address;
    }

    int port() {
        return this.port;
    }

    void setHostAddress(String address) {
        this.address = address;
    }

    void setPort(int port) {
        this.port = port;
    }
}
