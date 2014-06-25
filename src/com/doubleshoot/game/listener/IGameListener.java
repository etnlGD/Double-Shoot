package com.doubleshoot.game.listener;

import com.doubleshoot.shooter.BaseShooter;


public interface IGameListener {
	public void onGameStart(BaseShooter pLeftHero, BaseShooter pRightHero);
	public void onGamePause();
	public void onGameResume();
	public void onGameover();
}
