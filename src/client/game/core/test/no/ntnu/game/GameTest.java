package no.ntnu.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import no.ntnu.game.evaluation.GameEvaluation;
import no.ntnu.game.evaluation.PiecePosition;
import no.ntnu.game.evaluation.PieceValue;
import no.ntnu.game.models.Board;
import no.ntnu.game.models.Piece;

import static org.junit.Assert.assertEquals;

/**
 * Created by thomas on 4/11/17.
 */

public class GameTest {
    @Test
    public void playRandomGame() throws Exception {
        String fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        while (!GameAction.isMate(fen) && !GameAction.isDrawn(fen)) {
            List<Move> candiateMoves = GameAction.legalMoves(fen);
            int randomMove = ThreadLocalRandom.current().nextInt(0, candiateMoves.size());
            fen = FEN.toFen(GameAction.movePiece(fen, candiateMoves.get(randomMove)));
            System.out.println(fen);
        }
    }

    private Move bestMove(List<Move> moves, final String fen) throws Exception{
        final float delta = 1.0E-8f;
        final float factor = FEN.toBoard(fen).activeColor() == Piece.Color.WHITE ? 1.0f : -1.0f;
        float highScore = -99999.0f;
        GameEvaluation eval = new PieceValue(new PiecePosition());
        List<Move>goodMoves = new ArrayList<Move>();
        for (Move move : moves) {
            float score = factor * eval.score(GameAction.movePiece(fen, move));
            if (score > highScore+delta) {
                highScore = score;
                goodMoves.add(0,move);
            } else if (score < highScore+delta && score > highScore-delta) {
                goodMoves.add(0,move);
            }
        }

        // evaluate further maximum 10 candidate moves
        if (goodMoves.size() > 10) {
            goodMoves.subList(10, goodMoves.size()).clear();
        }
        List<Move>candidateMoves = new ArrayList<Move>();
        candidateMoves.addAll(goodMoves);
        if (goodMoves.size() > 1) { // Find the best move that gives the lowest opponents score
            float lowScore= 999999.0f;
            for (Move move : goodMoves) {
                float score = factor * eval.score(GameAction.movePiece(fen, move));
                if (score < highScore-delta) {
                    lowScore = score;
                    candidateMoves.add(0,move);
                } else if (score < lowScore+delta && score > lowScore-delta) {
                    candidateMoves.add(0,move);
                }
            }
        }

        // Pick a random move of the best 3 possible moves.
        if (candidateMoves.size() > 3) {
            candidateMoves.subList(3, candidateMoves.size()).clear();
        }
        int randomMove = ThreadLocalRandom.current().nextInt(0, candidateMoves.size());
        return candidateMoves.get(randomMove);
    }

    @Test
    public void playBestMoveGame() throws Exception {
        String fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
        while (!GameAction.isMate(fen) && !GameAction.isDrawn(fen)) {
            List<Move> candiateMoves = GameAction.legalMoves(fen);
            Move move = bestMove(candiateMoves, fen);
            fen = FEN.toFen(GameAction.movePiece(fen, move));
            System.out.println(move.toString());
        }
    }
}
