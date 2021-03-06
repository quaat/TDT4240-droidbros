package no.ntnu.game.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thomash on 25.03.2017.
 */


public class Board {
    final private int rows;
    final private int cols;
    private Piece.Color activeColor = Piece.Color.WHITE;
    private int halfmoveClock = 0;
    private int fullmoveClock = 1;
    private Boolean[] castlingAvailability;

    private Square[][] squares = null;
    final static int DEFAULT_NUM_ROWS = 8;
    final static int DEFAULT_NUM_COLS = 8;

    public Board() {
        this.rows = DEFAULT_NUM_ROWS;
        this.cols = DEFAULT_NUM_COLS;
        this.castlingAvailability = new Boolean[4];
        squares = new Square[this.cols][this.rows];
        initalizeSquares();
        initializeCastlingAvailability();
    }

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        squares = new Square[this.cols][this.rows];
        this.castlingAvailability = new Boolean[4];
        initalizeSquares();
        initializeCastlingAvailability();
    }

    public int halfmoveClock() {
        return this.halfmoveClock;
    }

    public int fullmoveClock() {
        return this.fullmoveClock;
    }

    public void setActiveColor(Piece.Color color) {
        this.activeColor = color;
    }

    public void flipActiveColor() {
        this.activeColor = (this.activeColor == Piece.Color.BLACK ? Piece.Color.WHITE: Piece.Color.BLACK);
    }

    public Piece.Color activeColor() {
        return this.activeColor;
    }

    public Boolean[] castlingAvailability() {
        return this.castlingAvailability;
    }

    public void setCastlingAvailability(int idx, Boolean value) {
        this.castlingAvailability[idx] = value;
    }

    private void initializeCastlingAvailability() {
        this.castlingAvailability[0] = Boolean.TRUE;
        this.castlingAvailability[1] = Boolean.TRUE;
        this.castlingAvailability[2] = Boolean.TRUE;
        this.castlingAvailability[3] = Boolean.TRUE;
    }

    private void initalizeSquares() {
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                squares[c][r] = new Square(this,c,r);
            }
        }
    }

    public void setSquare(int col, int row, Square square)
    {
        if (col >= 0 && col <= this.cols()
                && row >= 0 && row <= this.rows()) {
            this.squares[col][row] = square;
        }
        // TODO: Throw an exception
    }

    public Square square(int col, int row) {
        if (col >= 0 && col < this.cols()
                && row >= 0 && row < this.rows()) {
            return this.squares[col][row];
        }
        return null;
    }

    /**
     * Find all squares on the board
     * @return a list containing all squares.
     */
    public List<Square> allSquares() {
        List<Square> squareList = new ArrayList<Square>();
        for (Square[] row : this.squares) {
            squareList.addAll(Arrays.asList(row));
        }
        return squareList;
    }

    public int rows() {
        return this.rows;
    }

    public int cols() {
        return this.cols;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public void resetHalfmoveClock() {
        this.halfmoveClock = 0;
    }
    public void incrementHalfmoveClock() {
        this.halfmoveClock++;
    }

    public void setFullmoveClock(int fullmoveClock) {
        this.fullmoveClock = fullmoveClock;
    }

    public void incrementFullmoveClock() {
        this.fullmoveClock++;
    }
}
