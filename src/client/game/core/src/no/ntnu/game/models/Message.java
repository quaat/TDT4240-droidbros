package no.ntnu.game.models;

public class Message {
    private String time;
    private String userid;
    private String text;
    // COLOR

    public Message() {
        time = "";
        userid = "";
        text = "";
    }

    public Message(String time, String userid, String text) {
        this.time = time;
        this.userid = userid;
        this.text = text;
    }

    public String toString() {
        if (userid.equals(""))
            return "";
        return time + " <" + userid + "> "+text;
    }
}
