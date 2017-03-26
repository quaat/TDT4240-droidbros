package no.ntnu.game.models;

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

    public Piece.Color activeColor() {
        return this.activeColor;
    }

    public Boolean[] castlingAvailability() {
        return this.castlingAvailability;
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
                squares[c][r] = new Square(c,r);
            }
        }
    }

    public Square square(int col, int row) {
        return this.squares[col][row];
    }

    public int rows() {
        return this.rows;
    }

    public int cols() {
        return this.cols;
    }
}
