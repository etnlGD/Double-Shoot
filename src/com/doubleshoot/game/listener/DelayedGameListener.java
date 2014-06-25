package com.doubleshoot.game.listener;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

import com.doubleshoot.hero.Hero;

/**
 * the onGameover will delayed.
 * @author Alpha
 *
 */
public class DelayedGameListener implements IGameListener, IUpdateHandler {
	private IGameListener mCallback;
	private final float mDelayedSeconds;
	private float mSecondsElapsed;
	private boolean mTiming;
	
	public DelayedGameListener(Scene pScene, IGameListener pCallback, float pDelayedSeconds) {
		mCallback = pCallback;
		mDelayedSeconds = pDelayedSeconds;
		mTiming = false;
		pScene.registerUpdateHandler(this);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// timing not start or time up
		if (!mTiming) return;
		
		mSecondsElapsed += pSecondsElapsed;
		if (mSecondsElapsed > mDelayedSeconds) {
			mCallback.onGameover();
			mTiming = false;		// timeup
		}
	}

	@Override
	public void reset() {
		mSecondsElapsed = 0;
		mTiming = false;
	}
	
	@Override
	public void onGameover() {
		mTiming = true;
	}

	@Override
	public void onGameStart(Hero pLeftHero, Hero pRightHero) {
		reset();
		mCallback.onGameStart(pLeftHero, pRightHero);
	}
	
	@Override
	public void onGamePause() {
		mCallback.onGamePause();
	}

	@Override
	public void onGameResume() {
		mCallback.onGameResume();
	}
}