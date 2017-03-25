package no.ntnu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.models.Message;
import no.ntnu.game.views.AbstractView;
import no.ntnu.game.views.LoginView;
import no.ntnu.game.views.TestView;


public class MyGame extends Game{

	// model
	private GameModel model;

	// views
	private AbstractView loginView;
	private AbstractView testView;

	// controllers
	private GameController controller;

	@Override
	public void create () {
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
		testView = new TestView(model, controller);
	}

	// Setters view
	public void setLoginView() {
		setScreen(loginView);
	}

	public void setTestView() {
		Gdx.app.log("ANDYPANDY", "set test view");
		//model.addMessage(new Message("13:37", "Andy", "HALLA"));
		//model.addMessage(new Message("13:37", "Per", "Yo"));
		//model.addMessage(new Message("13:37", "Andy", "Sup?"));
		//model.addMessage(new Message("13:37", "Per", "Chillin"));
		Gdx.app.log("ANDYPANDY", "gTEST VIEW");
		setScreen(testView);
	}

	public LoginView getLoginView() {
		return (LoginView) loginView;
	}

	public TestView getTestView() {
		return (TestView) testView;
	}
}
