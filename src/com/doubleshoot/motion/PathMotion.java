package com.doubleshoot.motion;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.PathModifier.Path;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.movable.IMovable;

public class PathMotion implements IMotion {
	private Path mPath;
	private float mVelSize;
	
	public PathMotion(Path pPath, float velSize) {
		mPath = pPath;
		mVelSize = velSize;
	}
	
	@Override
	public IUpdateHandler createMotionModifier(IMovable pMovable) {
		return new Status(pMovable);
	}

	private class Status implements IUpdateHandler {
		private int mNextDest;
		private Vector2 mPrevPosition;
		private IMovable mTarget;

		public Status(IMovable pMovable) {
			mTarget = pMovable;
			mPrevPosition = mTarget.getPosition();
		}
		
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (mNextDest == mPath.getSize())
				return;
			
			if (passedDest())
				mNextDest++;
			
			mPrevPosition = mTarget.getPosition();
			if (mNextDest < mPath.getSize())
				towardsDest();
		}

		private boolean passedDest() {
			float x = mPath.getCoordinatesX()[mNextDest];
			float y = mPath.getCoordinatesY()[mNextDest];
			
			Vector2 pixelCur = mTarget.getPosition();
			Vector2 center = pixelCur.cpy();
			center.add(mPrevPosition).mul(0.5f);
			float radius = pixelCur.dst(mPrevPosition)/2;

			return center.dst(x, y) <= radius;
		}
		
		private void towardsDest() {
			float x = mPath.getCoordinatesX()[mNextDest];
			float y = mPath.getCoordinatesY()[mNextDest];

			Vector2 pos = mTarget.getPosition();
			Vector2 dir = new Vector2(x, y).sub(pos);
			
			mTarget.setVolocity(dir.nor().mul(mVelSize));
		}

		@Override
		public void reset() {
			
		}
	}

}
