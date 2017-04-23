package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import no.ntnu.game.FEN;
import no.ntnu.game.GameAction;
import no.ntnu.game.Move;
import no.ntnu.game.TypeErrorException;
import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Piece;


public class TestView2 extends AbstractView {
    private Board board;
    private Piece.Color playerColor;

    private TextButton doMoveButton;
    private TextButton resignButton;

    private Label gameLabel;
    private Label player1Label;
    private Label player2Label;
    private Label fenLabel;
    private Label statusLabel;

    public TestView2(GameModel model, GameController controller) {
        super(model, controller);
        this.controller = controller;
    }

    @Override
    public void build() {
        // Buttons
        doMoveButton = new TextButton("DO MOVE", skin);
        resignButton = new TextButton("RESIGN", skin);

        // Labels
        gameLabel = new Label("", skin);
        player1Label = new Label("", skin);
        player2Label = new Label("", skin);
        fenLabel = new Label("", skin);
        statusLabel = new Label("", skin);

        doMoveButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.doMove(randomMove());
                statusLabel.setText("Wait for move...");
                doMoveButton.setDisabled(true);
            }
        });

        resignButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                controller.resign();
            }
        });

        table.add(gameLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(player1Label).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(player2Label).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(fenLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(doMoveButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(resignButton).width(objectWidth).height(objectHeight).padBottom(padY).row();
        table.add(statusLabel).width(objectWidth).height(objectHeight).padBottom(padY).row();
    }

    public String randomMove() {
        String fen = model.fen();
        List<Move> candiateMoves = GameAction.legalMoves(fen);
        int randomMove = ThreadLocalRandom.current().nextInt(0, candiateMoves.size());
        Move move = candiateMoves.get(randomMove);
        board = GameAction.movePiece(board, move);
        return FEN.toFen(board);
    }

    @Override
    public void reset() {
        gameLabel = new Label("", skin);
        player1Label = new Label("", skin);
        player2Label = new Label("", skin);
        fenLabel = new Label("", skin);
        statusLabel = new Label("", skin);
    }

    @Override
    public void onNewMove() {
        doMoveButton.setDisabled(false);
        fenLabel.setText(model.fen());
        statusLabel.setText("Your move!");
        try {
            board = FEN.toBoard(model.fen());
        } catch (TypeErrorException e) {
            e.printStackTrace();
        }
        System.out.println(model.fen());
    }

    @Override
    public void onGameUpdate() {
        playerColor = model.color();
        player1Label.setText("You: " + model.player().toString());
        player2Label.setText("Opponent: " + model.opponent().toString());
        gameLabel.setText("gameid: "+ model.gameid());

        try {
            System.out.println(model.fen());
            board = FEN.toBoard(model.fen());
        } catch (TypeErrorException ex) {
            System.out.println("board fuckup");
        }

        if (playerColor == board.activeColor()) doMoveButton.setDisabled(false);
        else doMoveButton.setDisabled(true);
    }
}