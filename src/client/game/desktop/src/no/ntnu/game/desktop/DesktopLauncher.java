package no.ntnu.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.ntnu.game.controllers.ViewController;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Math.round(600);
		config.height = (int) Math.round(800);
		new LwjglApplication(new ViewController(), config);
	}
}
