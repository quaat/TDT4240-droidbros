package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.sun.glass.events.TouchEvent;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Piece;

/**
 * Created by Even on 27.03.2017.
 */

public class GameView extends AbstractView {

    private OrthographicCamera camera;
    private Board board;
    private Piece.Color playerColor;

    private Texture tile1Texture;
    private Texture tile2Texture;

    private Texture pawnTexture;
    private Texture rookTexture;
    private Texture kingTexture;
    private Texture queenTexture;
    private Texture bishopTexture;
    private Texture knightTexture;

    private Sprite highlightTile;

    private float squareWidth;
    private float squareHeight;
    private float startHeight;

    public GameView(GameModel model, GameController controller) {
        this(model, controller, new Board(), Piece.Color.WHITE);
    }
    //The method that will actually be used to create this view
    public GameView(GameModel model, GameController controller, Board board, Piece.Color playerColor) {
        super(model, controller);
        this.board = board;
        this.playerColor = playerColor;
        loadTextures();
        setupView();
    }
    private void loadTextures(){
        tile1Texture =  new Texture("tile1.png");
        tile2Texture = new Texture("tile2.png");
        //// TODO: 02.04.2017 Make these legit
        pawnTexture = null;
        rookTexture = new Texture("rook.png");
        kingTexture = null;
        queenTexture = null;
        bishopTexture = null;
        knightTexture = null;

    }

    private void setupView() {
        Gdx.app.log("Even/GameView","building gameView");
        Gdx.input.setInputProcessor(stage);

        if (playerColor.equals(Piece.Color.BLACK)) {
            //// TODO: 02.04.2017 We should somehow just rotate the view 180 degrees to maintain easyness of models
            //Something like this
            // stage.getCamera().rotate(Vector3.X,180);
        }

        //Rendering of the background in individual squares
        squareWidth =  screenWidth/board.cols();
        squareHeight = screenWidth/board.rows();

        //the position which we will start to draw chess squares on, the other space will be used to display other stuff
        startHeight = (screenHeight-screenWidth)/2;

        for(int columnNo = 0; columnNo < board.cols(); columnNo++){
            for(int rowNo = 0; rowNo < board.rows(); rowNo++){
                float xPos = squareWidth * columnNo;
                float yPos = startHeight + squareHeight * rowNo;
                Actor tileActor = (rowNo % 2 == 0 && columnNo % 2 == 1 || rowNo % 2 == 1 && columnNo % 2 == 0) ? new Image(tile1Texture) : new Image(tile2Texture);
                tileActor.setPosition(xPos,yPos);
                tileActor.setSize(squareWidth,squareHeight);
                stage.addActor(tileActor);
                Piece piece = board.square(columnNo, rowNo).piece();
                if (piece!=null) {
                    Texture actorTexture = getActorTexture(piece);
                    Actor pieceActor = new Image(actorTexture);
                    if (piece.color() == playerColor) {
                        pieceActor.addListener(onOwnPieceClickedListener);
                    }
                    pieceActor.setPosition(xPos,yPos);
                    pieceActor.setSize(squareWidth,squareHeight);
                    stage.addActor(pieceActor);
                }
            }
        }
        final Actor testPiece = new Image(rookTexture);
        testPiece.setPosition(squareWidth * 2, startHeight + squareHeight * 1);
        testPiece.setSize(squareWidth, squareHeight);
        //testPiece.addListener(onOwnPieceClickedListener);
        testPiece.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MoveToAction move = new MoveToAction();
                testPiece.addAction(Actions.moveTo(squareWidth*5,startHeight+squareHeight*3,2));
                super.clicked(event, x, y);
            }
        });

        stage.addActor(testPiece);

        //Rendering of pieces
    }

    private Texture getActorTexture(Piece piece) {
        Texture result;


        if (piece.type().equals(Piece.Type.BISHOP)) {
            return bishopTexture;
        }
        else if(piece.type().equals(Piece.Type.KING)){
            return kingTexture;
        }
        else if(piece.type().equals(Piece.Type.KNIGHT)){
            return knightTexture;
        }
        else if(piece.type().equals(Piece.Type.PAWN)){
            return pawnTexture;
        }
        else if(piece.type().equals(Piece.Type.QUEEN)){
            return queenTexture;
        }
        else if(piece.type().equals(Piece.Type.ROOK)){
            return rookTexture;
        }
        else{
            return null;
        }

    }

    private ClickListener onOwnPieceClickedListener = new

    ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            Gdx.app.log("Even/GameView, ","Yo,x:"+x+"y:"+y);

            super.clicked(event, x, y);

        }
    };


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
