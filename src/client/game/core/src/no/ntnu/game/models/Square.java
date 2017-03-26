package no.ntnu.game.models;

/**
 * Created by thomash on 25.03.2017.
 */

public class Square {
    final int col;
    final int row;
    Piece piece = null;

    public Square(int col, int row) {
        this.col = col;
        this.row = row;
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
