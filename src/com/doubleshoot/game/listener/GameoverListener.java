package com.doubleshoot.game.listener;

import com.doubleshoot.hero.Hero;

public abstract class GameoverListener implements IGameListener {

	@Override
	public void onGameStart(Hero pLeftHero, Hero pRightHero) {
	}
	
	@Override
	public void onGamePause() {
	}

	@Override
	public void onGameResume() {
	}

}