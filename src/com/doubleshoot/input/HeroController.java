package com.doubleshoot.input;

import org.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

import com.doubleshoot.hero.Hero;
import com.doubleshoot.shooter.ShootPolicy;
import com.doubleshoot.shooter.ShootWhenSlow;

public class HeroController {
	private int mPointerID = -1;
	private ShootPolicy mShootPolicy = new ShootWhenSlow();
	private Hero mHero;
	private float mMinX;
	private float mMaxX;
	
	public HeroController(Hero pHero, float pMinX, float pMaxX) {
		mHero = pHero;
		mMinX = pMinX;
		mMaxX = pMaxX;
	}
	
	public int getPointerID() {
		return mPointerID;
	}
	
	private float clamp(float value) {
		return Math.min(mMaxX, Math.max(value, mMinX));
	}
	
	public boolean inRange(float x) {
		return x >= mMinX && x < mMaxX;
	}
	
	public boolean onActionDown(int pPointerID, float x) {
		if (inRange(x)) {
			mPointerID = pPointerID;
			return true;
		}
		
		return false;
	}
	
	public boolean onTargetReset(TouchEvent pSceneTouchEvent) {
		switch (pSceneTouchEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mHero.setShootPolicy(mShootPolicy);
			// Fall through
		case MotionEvent.ACTION_MOVE:
			float validX = clamp(pSceneTouchEvent.getX());
			mHero.setTarget(validX);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			mPointerID = -1;
			mHero.setShootPolicy(null);
			break;
		}
		
		return true;
	}
	
}