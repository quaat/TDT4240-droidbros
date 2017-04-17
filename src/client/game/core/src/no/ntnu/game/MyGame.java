package no.ntnu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.views.AbstractView;
import no.ntnu.game.views.BoardView;
import no.ntnu.game.views.LoginView;
import no.ntnu.game.views.MenuView;
import no.ntnu.game.views.SetupView;
import no.ntnu.game.views.TestView2;


public class MyGame extends Game{

	// model
	private GameModel model;

	// views
	private AbstractView loginView;
	private AbstractView menuView;
	private AbstractView setupView;
	private AbstractView testView2; // game room
	private AbstractView boardView;

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
		menuView = new MenuView(model, controller);
		setupView = new SetupView(model, controller);
		testView2 = new TestView2(model, controller);
		boardView = new BoardView(model, controller);
	}

	// Setters view
	public void setLoginView() {
		setScreen(loginView);
	}

	public void setMenuView() {
		setScreen(menuView);
	}

	public void setSetupView() {
		setScreen(setupView);
	}

	public void setTestView2() {
		setScreen(testView2);
	}

	public void playComputer() {
		setScreen(boardView);
	}

	// Getters view
	public LoginView getLoginView() {
		return (LoginView) loginView;
	}

	public MenuView getMenuView() {
		return (MenuView) menuView;
	}

	public SetupView getSetupView() {
		return (SetupView) setupView;
	}

	public TestView2 getTestView2() {
		return (TestView2) testView2;
	}
}