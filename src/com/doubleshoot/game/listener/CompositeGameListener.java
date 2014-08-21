package com.doubleshoot.game.listener;

import java.util.HashSet;
import java.util.Set;

import com.doubleshoot.hero.Hero;
import com.doubleshoot.shooter.BaseShooter;

public class CompositeGameListener implements ICompositeGameListener {
	private Set<IGameListener> mListeners = new HashSet<IGameListener>();
	
	@Override
	public void appendListener(IGameListener pListener) {
		mListeners.add(pListener);
	}

	@Override
	public void removeListener(IGameListener pListener) {
		mListeners.remove(pListener);
	}

	@Override
	public void onGameover() {
		for (IGameListener l : mListeners) {
			l.onGameover();
		}
	}

	@Override
	public void onGameStart(Hero pLeftHero, Hero pRightHero) {
		for (IGameListener l : mListeners) {
			l.onGameStart(pLeftHero, pRightHero);
		}
	}

	@Override
	public void onGamePause() {
		for (IGameListener l : mListeners) {
			l.onGamePause();
		}		
	}

	@Override
	public void onGameResume() {
		for (IGameListener l : mListeners) {
			l.onGameResume();
		}		
	}

}