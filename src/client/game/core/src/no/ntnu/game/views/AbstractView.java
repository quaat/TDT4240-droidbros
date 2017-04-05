package no.ntnu.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.game.controllers.GameController;
import no.ntnu.game.models.GameModel;
import no.ntnu.game.util.ScreenObserver;

public abstract class AbstractView implements Screen, ScreenObserver {
	GameModel model;
	GameController controller;

	SpriteBatch batch;
	Stage stage;
	Skin skin;
	Table table;

	int screenWidth;
	int screenHeight;
	int objectWidth;
	int objectHeight;
	int padX;
	int padY;

	private boolean changeInData;
	
	public AbstractView(GameModel model, GameController controller) {
		this.model = model;
		this.controller = controller;
		model.addObserver(this);
		init();
	}

	private void init() {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		objectWidth = screenWidth/2;
		objectHeight = screenWidth/10;
		padX = screenWidth/40;
		padY = screenHeight/40;

		skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin.getFont("default-font").getData().setScale(4f, 4f);
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		table = new Table();
		
		table.setSize(screenWidth/2, screenHeight/2);
		table.setPosition(screenWidth/4, screenHeight/4);
		stage.addActor(table);

		this.build();
	}
	
	// Put all the objects on the screen!
	public abstract void build();

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();
	}

	@Override
	public void onUpdate() { }

	@Override
	public void onNewMove() { }
}
