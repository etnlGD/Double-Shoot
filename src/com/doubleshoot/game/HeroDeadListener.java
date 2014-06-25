package com.doubleshoot.game;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.game.listener.IGameListener;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;
import com.doubleshoot.shooter.TagManager;

public class HeroDeadListener implements IBehavior {
	private final int mInitLives;
	private int mLeftLives;
	private int mRightLives;
	private IGameListener mListener;
	
	public HeroDeadListener(int pInitLives, IGameListener pListener) {
		mInitLives = pInitLives;
		mListener = pListener;
	}
	
	public void reset() {
		mLeftLives = mInitLives;
		mRightLives = mInitLives;
	}
	
	private boolean hasNoLives(int lives) {
		return lives <= 0;
	}
	
	private void livesDetect(String logInfo, int lives) {
		if (hasNoLives(lives))
			throw new NullPointerException(
					logInfo + " hero has no live to dead, current Lives:" + lives);
	}
	
	public boolean gameover() {
		return hasNoLives(mLeftLives) && hasNoLives(mRightLives);
	}

	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (host.hasTag(TagManager.sLeftHero)) {
			livesDetect("Left", mLeftLives);
			--mLeftLives;
		}
		
		if (host.hasTag(TagManager.sRightHero)) {
			livesDetect("Right", mRightLives);
			--mRightLives;
		}
		
		if (gameover())
			mListener.onGameover();
	}
}