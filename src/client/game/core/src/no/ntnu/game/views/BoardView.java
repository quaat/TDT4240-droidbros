package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import no.ntnu.game.FEN;
import no.ntnu.game.GameAction;
import no.ntnu.game.Move;
import no.ntnu.game.TypeErrorException;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Piece;
import no.ntnu.game.models.Square;
import no.ntnu.game.util.GameObserver;

/**
 * Created by Even on 27.03.2017.
 */

public class BoardView extends AbstractView {

    private List<GameObserver> observers;
    private OrthographicCamera camera;
    private Board board;
    private Piece.Color playerColor;

    private boolean isYourTurn = true;
    private boolean highlightMode;
    private int highlightedX;
    private int highlightedY;
    private Square squareLastClicked;
    private float moveDuration = 1;

    private Texture tile1Texture;
    private Texture tile2Texture;

    private Texture pawnTexture;
    private Texture rookTexture;
    private Texture kingTexture;
    private Texture queenTexture;
    private Texture bishopTexture;
    private Texture knightTexture;

    private Texture bPawnTexture;
    private Texture bRookTexture;
    private Texture bKingTexture;
    private Texture bQueenTexture;
    private Texture bBishopTexture;
    private Texture bKnightTexture;


    private Sprite highlightTile;

    private float squareWidth;
    private float squareHeight;
    private float startHeight;

    public BoardView(GameModel model) {
        this(model, new Board(), Piece.Color.WHITE);
    }

    //The method that will actually be used to create this view
    public BoardView(GameModel model, Board board, Piece.Color playerColor) {
        super(model, null);
        this.observers = new ArrayList<>();
        this.board = board;
        this.playerColor = playerColor;
        loadTextures();
    }

    public void registerObserver(GameObserver observer) {
        this.observers.add(observer);
    }

    public void emitUpdate() {
        for (GameObserver observer : observers) {
            observer.onUpdate();
        }
    }

    public void emitRenderFinished() {
        for (GameObserver observer : observers) {
            observer.onReady();
        }
    }

    private void loadTextures() {
        tile1Texture = new Texture("tile1.png");
        tile2Texture = new Texture("tile2.png");

        pawnTexture = new Texture("pawn.png");
        rookTexture = new Texture("rook.png");
        kingTexture = new Texture("king.png");
        queenTexture = new Texture("queen.png");
        bishopTexture = new Texture("bishop.png");
        knightTexture = new Texture("horse.png");

        bPawnTexture = new Texture("bpawn.png");
        bRookTexture = new Texture("brook.png");
        bKingTexture = new Texture("bking.png");
        bQueenTexture = new Texture("bqueen.png");
        bBishopTexture = new Texture("bbishop.png");
        bKnightTexture = new Texture("bhorse.png");

    }

    public void resetBoard(final String fen) {
        stage.getActors().clear();
        try {
            board = FEN.toBoard(fen);
            //Rendering of the background in individual squares
            squareWidth = screenWidth / board.cols();
            squareHeight = screenWidth / board.rows();

            //the position which we will start to draw chess squares on, the other space will be used to display other stuff
            startHeight = (screenHeight - screenWidth) / 2;


            for (int x = 0; x < board.cols(); x++) {
                for (int y = 0; y < board.rows(); y++) {
                    float xPos = squareWidth * x;
                    float yPos = startHeight + squareHeight * y;
                    Actor squareActor = (y % 2 == 0 && x % 2 == 1 || y % 2 == 1 && x % 2 == 0) ? new Image(tile1Texture) : new Image(tile2Texture);
                    squareActor.setPosition(xPos, yPos);
                    squareActor.setSize(squareWidth, squareHeight);
                    squareActor.setZIndex(0);

                    stage.addActor(squareActor);
                    Piece piece = board.square(x, y).piece();
                    if (piece != null) {
                        Texture actorTexture = getPieceTexture(piece);
                        PieceActor pieceActor = null;
                        if (playerColor.equals(Piece.Color.BLACK)) {
                            Sprite flippedPiece = new Sprite(getPieceTexture(piece));
                            flippedPiece.flip(false, true);
                            pieceActor = new PieceActor(flippedPiece);
                        } else {
                            pieceActor = new PieceActor(actorTexture);
                        }
                        pieceActor.setZIndex(1);
                        pieceActor.setPosition(xPos, yPos);
                        pieceActor.setSize(squareWidth, squareHeight);
                        stage.addActor(pieceActor);
                    }
                }
            }
        } catch (TypeErrorException ex) {
            Gdx.app.log("TypeErrorException", ex.toString());
        }
    }

    public void setup(final String fen) {
        Gdx.app.log("BoardView", "building boardView");
        Gdx.input.setInputProcessor(stage);

        try {
            board = FEN.toBoard(fen);
        } catch (TypeErrorException ex) {
            Gdx.app.log("BoardView", "exception caught " + ex.toString());
        }

        //Rendering of the background in individual squares
        squareWidth = screenWidth / board.cols();
        squareHeight = screenWidth / board.rows();

        //the position which we will start to draw chess squares on, the other space will be used to display other stuff
        startHeight = (screenHeight - screenWidth) / 2;


        for (int x = 0; x < board.cols(); x++) {
            for (int y = 0; y < board.rows(); y++) {
                float xPos = squareWidth * x;
                float yPos = startHeight + squareHeight * y;
                Actor squareActor = (y % 2 == 0 && x % 2 == 1 || y % 2 == 1 && x % 2 == 0) ? new Image(tile1Texture) : new Image(tile2Texture);
                squareActor.setPosition(xPos, yPos);
                squareActor.setSize(squareWidth, squareHeight);
                squareActor.setZIndex(0);

                stage.addActor(squareActor);
                Piece piece = board.square(x, y).piece();
                if (piece != null) {
                    Texture actorTexture = getPieceTexture(piece);
                    PieceActor pieceActor = null;
                    if (playerColor.equals(Piece.Color.BLACK)) {
                        Sprite flippedPiece = new Sprite(getPieceTexture(piece));
                        flippedPiece.flip(false, true);
                        pieceActor = new PieceActor(flippedPiece);
                    } else {
                        pieceActor = new PieceActor(actorTexture);
                    }
                    pieceActor.setZIndex(1);
                    pieceActor.setPosition(xPos, yPos);
                    pieceActor.setSize(squareWidth, squareHeight);
                    stage.addActor(pieceActor);
                }
            }
        }
        if (playerColor.equals(Piece.Color.BLACK)) {
            ((OrthographicCamera) stage.getCamera()).rotate(180);
            isYourTurn = false;
        }
        stage.addListener(onOwnPieceClickedListener);
        stage.addListener(onChangeListener);
    }


    private boolean isMoveLegal(Move move) {
        List<Move> candiateMoves = GameAction.legalMoves(move.from());
        for (Move candidate : candiateMoves) {
            if (candidate.equals(move)) {
                return true;
            }
        }

        return false;
    /*
        //// TODO: 05.04.2017 Needs to check the model
        Piece piece = move.from().piece();
        if (piece.color() != playerColor || move.to().piece()!=null && piece.color()==move.to().piece().color()) {
            if (piece.color() == null) {
                log("piece was null");
            }
            log("move not executed - illegal piece color");
            return false;
        }
        if (move.from().row() == move.to().row() && move.from().col() == move.to().col()) {
            return false;
        }
        return true;
        */
    }

    private int screenToXBoardPosition(float screenPos) {
        for (int pieceX = 0; pieceX < board.rows(); pieceX++) {
            if (screenPos < squareWidth) {
                return pieceX;
            }
            screenPos -= squareWidth;
        }
        return board.cols() - 1;
    }

    //To be called when the opponent performs a move

    public void executeOpponentMove(Move move) {
        Actor pieceActor = getActorAt(move.from.col(), move.from.row());
        Piece piece = board.square(move.from.col(), move.from().row()).piece();
        pieceActor.setZIndex(stage.getActors().size);
        if (board.square(move.to.col(), move.to.row()).piece() != null) {
            //Todo create animation for removal of pieces

            removePiece(move.to.col(), move.to.row());
        } else if (getActorAt(move.to.col(), move.to.row()) != null) {
            log("BOARD AND VIEW NOT SYNCED");
        }

        pieceActor.addAction(Actions.moveTo(squareWidth * move.to().col(), startHeight + squareHeight * move.to.row(), moveDuration));
        board = GameAction.movePiece(board, move);
        resetBoard(FEN.toFen(board));
        isYourTurn = true;
    }

    private int screenToYBoardPosition(float screenPos) {
        if (screenPos < startHeight || screenPos > startHeight + squareHeight * board.rows()) {
            return -1;
        }
        screenPos -= startHeight;

        for (int pieceY = 0; pieceY < board.rows(); pieceY++) {
            if (screenPos < squareHeight) {
                return pieceY;
            }
            screenPos -= squareHeight;
        }
        return board.rows() - 1;
    }

    private void highlightPossibleMoves(Piece piece) {
        //// TODO: 05.04.2017 For example draw a dot(actor) over the possible squares to move to
    }

    private void log(String msg) {
        Gdx.app.log("Even/GameView", msg);
    }


    private Texture getPieceTexture(Piece piece) {


        if (piece.type().equals(Piece.Type.BISHOP)) {
            if (piece.color() == Piece.Color.WHITE) {
                return bishopTexture;
            } else {
                return bBishopTexture;
            }
        } else if (piece.type().equals(Piece.Type.KING)) {
            if (piece.color() == Piece.Color.WHITE) {
                return kingTexture;
            } else {
                return bKingTexture;
            }
        } else if (piece.type().equals(Piece.Type.KNIGHT)) {
            if (piece.color() == Piece.Color.WHITE) {
                return knightTexture;
            } else {
                return bKnightTexture;
            }
        } else if (piece.type().equals(Piece.Type.PAWN)) {
            if (piece.color() == Piece.Color.WHITE) {
                return pawnTexture;
            } else {
                return bPawnTexture;
            }
        } else if (piece.type().equals(Piece.Type.QUEEN)) {
            if (piece.color() == Piece.Color.WHITE) {
                return queenTexture;
            } else {
                return bQueenTexture;
            }
        } else if (piece.type().equals(Piece.Type.ROOK)) {
            if (piece.color() == Piece.Color.WHITE) {
                return rookTexture;
            } else {
                return bRookTexture;
            }

        }
        throw new RuntimeException("Unable to find texture");
    }

    public String fen() {
        return FEN.toFen(board);
    }

    public void performMove(Move move) {
        tryToExecuteMove(move);
    }

    private ChangeListener onChangeListener = new
            ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    emitRenderFinished();
                }
            };

    private ClickListener onOwnPieceClickedListener = new

    ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            int xPos = screenToXBoardPosition(x);
            int yPos = screenToYBoardPosition(y);
            log("x:" + xPos + ", y:" + yPos);
            if (yPos == -1) {
                //Handle click outside board
                return;
            }
            Square square = board.square(xPos, yPos);
            Piece pieceClicked = square.piece();

            String infoString = "Your turn: "+ Boolean.toString(isYourTurn);
            if (isYourTurn) {
                if (pieceClicked != null && pieceClicked.color() == playerColor) {
                    highlightMode = true;
                    squareLastClicked = square;
                } else if (highlightMode) {
                    tryToExecuteMove(new Move(squareLastClicked, square));
                    infoString += "\n highlight on, trying to execute move1";
                    squareLastClicked = null;
                    highlightMode = false;
                    emitUpdate();
                }
            } else if (!isYourTurn) {
                infoString += "\n not your turn";
            }
            log(infoString);
            super.clicked(event, x, y);

        }
    };

    private void tryToExecuteMove(Move move) {
        if (isMoveLegal(move)) {
            executeMove(move);
        }
        else{
            //Todo Maybe indicate visually that the move is not allowed
        }
    }

    private void executeMove(Move move){
        int xPos = move.to.col();
        int yPos = move.to.row();
        Actor pieceActor = getActorAt(move.from.col(), move.from.row());
        Square fromSquare= move.from();
        Square destSquare = move.to();

        pieceActor.setZIndex(stage.getActors().size);
        if (destSquare.piece() != null) {
            //Todo create animation for this
            removePiece(xPos, yPos);
        } else if (getActorAt(xPos, yPos) != null) {
            log("BOARD AND VIEW NOT SYNCED");
        }

        pieceActor.addAction(Actions.moveTo(squareWidth*xPos,startHeight+squareHeight*yPos,moveDuration));
        board = GameAction.movePiece(board, new Move(fromSquare, destSquare));
        resetBoard(FEN.toFen(board));
        highlightMode = false;
    }

    private Actor getActorAt(int x, int y) {
        Actor actor = null;
        Array<Actor> actors = stage.getActors();
        for(int i = 0; i<actors.size;i++) {
            actor = actors.get(i);
            if (actor instanceof PieceActor){
                //log("screenX:" + actor.getX() + ",screenY" + actor.getY());
                if(((PieceActor) actor).isAtPosition(x, y)) {
                    log("Found actor");
                    return actor;
                }

            }
        }
        log("Reference to actor at x:" + x + ",y:" + y + " not found");
        return null;
    }

    private class PieceActor extends Image {

        public PieceActor(Sprite sprite) {
            super(sprite);
        }
        public PieceActor(Texture texture) {
            super(texture);
        }
        private boolean isAtPosition(int x, int y) {
            //log("boardPosX:" + screenToXBoardPosition(getX()) + ",boardPosY:" + screenToYBoardPosition(getY()));
            if (screenToXBoardPosition(getX()) == x && screenToYBoardPosition(getY()) == y) {
                return true;
            }
            return false;
        }
    }

    private void removePiece(int x, int y) {
        //Todo Update game interface showing how many of each piece is taken
        Actor pieceToRemove = getActorAt(x, y);
        if (pieceToRemove != null) {
            pieceToRemove.remove();
        }
        else{
            log("ACTOR WAS NOT DELETED");
        }

    }

    //initialize views
    @Override
    public void build() {

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resume () {
        int i = 0;
    }

    @Override
    public void reset() {
        // TODO Reset something
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        if ((screenWidth != width || screenHeight != height)) {
            Gdx.app.log("Even/GameView", "Must resize - W="+width+",H="+height + ", SW="+screenWidth+", SH="+screenHeight);
            screenWidth = width;
            screenHeight = height;

            float newSquareWidth =  screenWidth/(float) board.cols();
            float newSquareHeight = screenWidth/(float)board.rows();
            float newStartHeight = (screenHeight-screenWidth)/2;
            Array<Actor> actors = stage.getActors();
            Gdx.app.log("Even/GameView","Squares width: "+ newSquareWidth*8+""+squareWidth*8);
            for (Actor a : actors) {
                a.setPosition( (a.getX()/squareWidth)*newSquareWidth,(a.getY()-startHeight)/squareHeight*newSquareHeight+newStartHeight);
                a.setSize(newSquareWidth,newSquareHeight);
            }

            squareWidth =  newSquareWidth;
            squareHeight = newSquareHeight;
            startHeight = newStartHeight;
        }
    }

}
