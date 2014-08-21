package com.doubleshoot.hud;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.shape.ShapeFactory;

public class Lifebar implements HudItem {
	public static class LookNFeel {
		public boolean leftAligned;
		public ShapeFactory frameFactory;
		public ShapeFactory bloodFactory;
		public float horzMargin;
		public float vertMargin;
		public int bloodUnitCount;
		public float animUnit;
		public float unitGap;
	}
	
	private IAreaShape mFrame;
	private IAreaShape[] mBloodUnits;
	private int mVisible;
	private int mTargetVisible;
	
	public Lifebar(LookNFeel pLook) {
		mFrame = pLook.frameFactory.createShape();
		int sz = pLook.bloodUnitCount;
		mBloodUnits = new IAreaShape[sz];
		
		for (int i = 0; i < sz; i++) {
			IAreaShape shape = pLook.bloodFactory.createShape();
			shape.setPosition(pLook.horzMargin + i*(shape.getWidthScaled() + pLook.unitGap), 
					pLook.vertMargin);
			shape.setVisible(false);
			mBloodUnits[pLook.leftAligned ? i : sz-i-1] = shape;
			mFrame.attachChild(shape);
		}
		TimerHandler timer = new TimerHandler(pLook.animUnit, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				adjustToTarget();
			}
		});
		
		mFrame.setSize(sz*mBloodUnits[0].getWidthScaled() + 2*pLook.horzMargin + (sz-1)*pLook.unitGap,
				mBloodUnits[0].getHeightScaled() + 2*pLook.vertMargin);
		
		mFrame.registerUpdateHandler(timer);
	}
	
	public Vector2 getSize() {
		return new Vector2(mFrame.getWidthScaled(), mFrame.getHeightScaled());
	}
	
	private void adjustToTarget() {
		if (mVisible == mTargetVisible) 
			return;
		
		if (mVisible < mTargetVisible)
			mBloodUnits[mVisible++].setVisible(true);
		else
			mBloodUnits[--mVisible].setVisible(false);
		
	}
	
	public void setPercent(float pPercent) {
		assert pPercent >= 0 && pPercent <= 1;
		mTargetVisible = (int) Math.ceil(pPercent * mBloodUnits.length);
	}
	
	@Override
	public void attachToScene(float pX, float pY, Scene pScene) {
		mFrame.setPosition(pX, pY);
		pScene.attachChild(mFrame);
	}

	@Override
	public void detach() {
		mFrame.detachSelf();
	}
}
