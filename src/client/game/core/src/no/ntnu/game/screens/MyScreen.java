package no.ntnu.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.game.MyGame;

public abstract class MyScreen implements Screen {
	MyGame game;
	SpriteBatch batch;
	Stage stage;
	Skin skin;
	
	public MyScreen(MyGame game) {
		this.game = game;
		init();
	}

	private void init() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin.getFont("default-font").getData().setScale(4f, 4f);
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		this.build();
	}
	
	// Put all the objects on the screen!
	abstract void build();

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();
	}
}
