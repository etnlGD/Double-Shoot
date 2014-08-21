package com.doubleshoot.hero;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GameObject;
import com.doubleshoot.score.IScoreChangeListener;
import com.doubleshoot.score.IScorer;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.Harmful;

public class Hero extends BaseShooter implements Harmful, IScorer {
	private static float NO_TARGET = 1.f/0;
	private float mSpeed = 25;
	private float mPrevX;
	private float mTargetX = NO_TARGET;
	private boolean mFastMoving = false;
	
	private boolean mIgnoreDamage = false;
	private int mScore;
	private IScoreChangeListener mListener;
	
	class MoveToTarget implements IUpdateHandler {
		void setVelX(Body body, float velX) {
			body.setLinearVelocity(velX, 0);
		}
		
		@Override
		public void onUpdate(float pSecondsElapsed) {
			mFastMoving = false;
			if (mTargetX == NO_TARGET)
				return;
			
			IAreaShape shape = getShape();
			float centerX = shape.getSceneCenterCoordinates()[0];
			
			float speed = mSpeed;
			if (Math.abs(mTargetX-centerX) < pSecondsElapsed*mSpeed*2*32) {
				speed /= 5;
			} else if (Math.abs(mTargetX-centerX) < pSecondsElapsed*mSpeed*4*32) {
				speed /= 2;
			} else {
				mFastMoving = true;
			}
				
			if (mTargetX > centerX && mTargetX > mPrevX) {
				setVelX(getBody(), Math.abs(speed));
				updatePrevX();
			} else if (mTargetX < centerX && mTargetX < mPrevX) {
				setVelX(getBody(), -Math.abs(speed));
				updatePrevX();
			} else {
				setVelX(getBody(), 0);
				mTargetX = NO_TARGET;
			}
		}

		@Override
		public void reset() {
		}
		
	}
	
	public Hero(IAreaShape shape, Body body, GOEnvironment pEnv) {
		super(shape, body, pEnv);
		mPrevX = shape.getX();
		shape.registerUpdateHandler(new MoveToTarget());
	}
	
	@Override
	protected int onContactBegin(GameObject other, int param) {
		if (!mIgnoreDamage)
			return super.onContactBegin(other, param);
		
		return 1;
	}
	
	private void updatePrevX() {
		mPrevX = getShape().getSceneCenterCoordinates()[0];
	}
	
	public void setIgnoreDamage(boolean b) {
		mIgnoreDamage = b;
	}
	
	public boolean isIgnoreDamage() {
		return mIgnoreDamage;
	}
	
	public void setTarget(float xCoord) {
		mTargetX = xCoord;
		mFastMoving = true;	// Mark as fast moving
	}
	
	public boolean isFastMoving() {
		return mFastMoving;
	}

	@Override
	protected Filter getBulletFilter() {
		return GameObjectType.HeroBullet.getSharedFilter();
	}

	@Override
	public void addScore(int score) {
		if (score > 0) {
			mScore += score;
			
			if (mListener != null)
				mListener.onScoreChanged(mScore, score);
		}
	}

	@Override
	public int getScore() {
		return mScore;
	}

	@Override
	public void setScoreChangeListener(IScoreChangeListener lis) {
		if (mListener != lis) {
			mListener = lis;
			mListener.onScoreChanged(mScore, 0);
		}
	}

}
