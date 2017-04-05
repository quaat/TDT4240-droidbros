package no.ntnu.game.models;

import java.util.List;
import java.util.function.Function;

import no.ntnu.game.Move;
import no.ntnu.game.movestrategy.MoveStrategy;

/**
 * Created by thomash on 25.03.2017.
 */

public class Piece {
    private MoveStrategy strategy;

    public enum Type {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
    }

    public enum Color {
        WHITE,BLACK
    }

    private Type type;
    private Color color;
    private Object object = null;
    private Boolean moved = false;

    public Piece (Type pieceType, Color pieceColor, MoveStrategy strategy) {
        this.type = pieceType;
        this.color = pieceColor;
        this.strategy = strategy;
    }

    public Type type() {
        return this.type;
    }

    public Color color() {
        return this.color;
    }

    public void setMoveStrategy(MoveStrategy strategy) {
        this.strategy = strategy;
    }

    public void setObject(Object o) {
        this.object = o;
    }

    public Object object() {
        return this.object;
    }

    public List<Function<Square, List<Move> >> legalMoves() {
        return this.strategy.legalMoves();
    }
}
