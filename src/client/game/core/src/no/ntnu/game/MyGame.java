package no.ntnu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.views.AboutView;
import no.ntnu.game.views.AbstractView;

//import no.ntnu.game.views.BoardView;

import no.ntnu.game.views.BoardView;
import no.ntnu.game.views.GameEndedView;
import no.ntnu.game.views.GameView;
import no.ntnu.game.views.LoginView;
import no.ntnu.game.views.RegisterView;
import no.ntnu.game.views.MenuView;
import no.ntnu.game.views.FenView;
import no.ntnu.game.views.SettingView;
import no.ntnu.game.views.TestView2;
import no.ntnu.game.views.TutorialView;


public class MyGame extends Game{

	// model
	private GameModel model;

	// views
	private AbstractView loginView;
	private AbstractView registerView;
	private AbstractView menuView;
	private AbstractView settingView;
	private AbstractView tutorialView;
	private AbstractView aboutView;
	private AbstractView fenView;
	private AbstractView gameView;
	private AbstractView gameEndedView;

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
		setLoginView();
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
		registerView = new RegisterView(model, controller);
		menuView = new MenuView(model, controller);
		tutorialView = new TutorialView(model, controller);
		settingView = new SettingView(model, controller);
		aboutView = new AboutView(model, controller);
		fenView = new FenView(model, controller);
		gameView = new GameView(model, controller);
		gameEndedView = new GameEndedView(model, controller);

		testView2 = new TestView2(model, controller);
		boardView = new BoardView(model, controller);
	}

	// Setters view
	public void setLoginView() {
		setScreen(loginView);
	}

	public void setRegisterView() {
		setScreen(registerView);
	}

	public void setMenuView() {
		setScreen(menuView);
	}

	public void setTutorialView() {
		setScreen(tutorialView);
	}

	public void setSettingView() {setScreen(settingView);}

	public void setAboutView() {
		setScreen(aboutView);
	}


	public void playComputer() {
		setScreen(boardView);
	}
/*
	// Getters view
	public LoginView getLoginView() {
		return (LoginView) loginView;
*/

	public void setFenView() {
		setScreen(fenView);
	}

	public void setGameView() {
		setScreen(gameView);
	}

	public void setGameEndedView() {
		setScreen(gameEndedView);
	}

	public void setTestView2() {
		setScreen(testView2);
	}

}