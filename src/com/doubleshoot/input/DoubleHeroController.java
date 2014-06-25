package com.doubleshoot.input;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

import com.doubleshoot.hero.Hero;

public class DoubleHeroController implements IOnSceneTouchListener {
	private HeroController mLeftHeroController;
	private HeroController mRightHeroController;
	
	private float yMin = 225;
	
	public DoubleHeroController(Hero left, Hero right, float[] seps) {
		mLeftHeroController = new HeroController(left, seps[0], seps[1]);
		mRightHeroController = new HeroController(right, seps[2], seps[3]);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		int pPointerID = pSceneTouchEvent.getPointerID();
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
			if (pSceneTouchEvent.getY() < yMin) return false;
			
			if (mLeftHeroController.getPointerID() == -1)
				mLeftHeroController.onActionDown(pPointerID, pSceneTouchEvent.getX());
			
			if (mRightHeroController.getPointerID() == -1)
				mRightHeroController.onActionDown(pPointerID, pSceneTouchEvent.getX());
		}
		
		if (pPointerID == mLeftHeroController.getPointerID())
			return mLeftHeroController.onTargetReset(pSceneTouchEvent);
		else if (pPointerID == mRightHeroController.getPointerID())
			return mRightHeroController.onTargetReset(pSceneTouchEvent);
		
		return false;
	}

}