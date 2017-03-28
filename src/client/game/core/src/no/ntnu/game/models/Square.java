package no.ntnu.game.models;

import no.ntnu.game.TypeErrorException;

/**
 * Created by thomash on 25.03.2017.
 */



public class Square {
    final int col;
    final int row;
    Piece piece = null;
    Board board = null; // back-reference

    public Square(String pos) throws TypeErrorException {
        if (pos.length() != 2) throw new TypeErrorException("Illegal square");
        Character column = Character.toLowerCase(pos.charAt(0));
        if (column >= 'a' && column <= 'h') {
            col = column - 'a';
        } else {
            throw new TypeErrorException("Illegal square - error in column");
        }

        Character rank = Character.toLowerCase(pos.charAt(1));
        if (rank >= '1' && rank <= '8') {
            row = rank-'1';
        } else {
            throw  new TypeErrorException("Illegal square - error in row/rank");
        }
    }

    public Square(Board board, int col, int row) {
        this.board = board;
        this.col = col;
        this.row = row;
    }

    public Board board() {
        return this.board;
    }

    public Piece piece() { return this.piece; }
    public void setPiece(Piece piece) {this.piece = piece;}
    public int row()  { return this.row; }
    public int col() { return this.col; }

    public String getAlgebraicCoordinate() {
        StringBuilder sb = new StringBuilder();
        sb.append('a' + (char)this.col);
        sb.append(this.row);
        return sb.toString();
    };
}
