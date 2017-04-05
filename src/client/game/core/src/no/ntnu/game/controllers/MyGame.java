package no.ntnu.game.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Square;
import no.ntnu.game.views.AbstractView;
import no.ntnu.game.views.GameView;
import no.ntnu.game.views.LoginView;
import no.ntnu.game.views.TestView2;


public class MyGame extends Game {

    // model
    private GameModel model;

    // views
    private AbstractView loginView;
    private AbstractView testView2; // game room
    private AbstractView gameView;

    // controllers
    private GameController controller;

    @Override
    public void create() {
        Gdx.app.log("ANDYPANDY", "game init");

        createModel();
        createControllers();
        createViews();

        // Set start screen of application
        setScreen(loginView);
    }

    // Creators
    private void createModel() {
        model = new GameModel();
    }

    private void createControllers() {
        controller = new GameController(model, this);
    }

    private void createViews() {
        loginView = new LoginView(model, controller);
        testView2 = new TestView2(model, controller);
    }

    // Setters view
    public void setLoginView() {
        setScreen(loginView);
    }

    public void setTestView2() {
        setScreen(testView2);
    }

    // Getters view
    public LoginView getLoginView() {
        return (LoginView) loginView;
    }

    public TestView2 getTestView2() {
        return (TestView2) testView2;
    }

    public void setGameView() {
        Gdx.app.log("Even/ViewController", "Changing view to testGameview");
        //This is probably gonna take some more inputs
        gameView = new GameView(model, controller);
        setScreen(gameView);
    }

    //// TODO: 30.03.2017
    public void setGameView(Square[][] squares) {

    }
}