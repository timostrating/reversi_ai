package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// new LwjglApplication(new MyGdxGame(), config);
		new LwjglApplication(new Load3dModelTest(), config); 	// Note: libJDX code does not have a main loop
			// we give libJDX a application Listener and it wil main loop it for us
	}
}
