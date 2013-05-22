package com.thelonelybyte.tard;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

// @Author: Isak Andersson

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "tard";
		cfg.useGL20 = false;
		cfg.width = Block.size * 12;
		cfg.height = Block.size * 10;
		
		new LwjglApplication(new Tard(), cfg);
	}
}
