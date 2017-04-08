package com.dbeef.madpirates;

import com.badlogic.gdx.Game;
import com.dbeef.madpirates.screens.GameScreen;

public class Main extends Game {
	@Override
	public void create() {
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
}
