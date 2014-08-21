package com.doubleshoot.game.listener;

import com.doubleshoot.hero.Hero;

public interface IGameListener {
	public void onGameStart(Hero pLeftHero, Hero pRightHero);
	public void onGamePause();
	public void onGameResume();
	public void onGameover();
}
