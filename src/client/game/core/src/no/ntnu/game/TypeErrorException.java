package no.ntnu.game;

/**
 * Created by thomash on 27.03.2017.
 * Thow this exception is the piece type is faulty/unknown
 */

public class TypeErrorException extends Exception {
    public TypeErrorException(String msg) {
        super(msg);
    }
}
