package no.ntnu.game.controllers;

import com.badlogic.gdx.Game;

import java.util.List;

import no.ntnu.game.GameAction;
import no.ntnu.game.Move;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.util.GameObserver;
import no.ntnu.game.views.BoardView;

/**
 * Created by thomas on 4/22/17.
 */

public class PlayComputerController extends AbstractController implements GameObserver {
    private BoardView boardView;
    private Game game;
    private final String stdOpening = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";


    public PlayComputerController(GameModel model, Game game) {
        super(model);
        boardView = new BoardView(model);
        boardView.registerObserver(this);
        boardView.setup(stdOpening);
        this.game = game;

    }

    public void startGame() {
        game.setScreen(boardView);
    }

    @Override
    public void onReady() {
        String fen = boardView.fen();
        int i = 0;

    }

    @Override
    public void onUpdate() {
        String fen = boardView.fen();
        boardView.resetBoard(fen);
        List<Move> candiateMoves = GameAction.legalMoves(fen);
        try {
            Move move = GameAction.bestMove(candiateMoves, fen);
            boardView.executeOpponentMove(move);
        } catch (Exception ex) {
            System.out.println("Exception caught! " + ex.toString());
        }
    }

}
