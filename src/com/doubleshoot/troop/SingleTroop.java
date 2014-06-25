package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.alien.Alien;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GORegistry;

public class SingleTroop implements ITroop {
	private DeterminePair<IMotion> mMotionDetermin;
	private DeterminePair<Vector2> mLocationDetermin;
	private DeterminePair<String> mSoldierDetermin;
	private DeterminePair<Float> mTimeGapDetermin;

	private int index;
	private final GORegistry<Alien> mAlienRegistry;
	private final int mSize;
	
	public SingleTroop(int size, GORegistry<Alien> pAlienRegistry,
			ITroopDetermin<String> soldier, ITroopDetermin<Vector2> position,
			ITroopDetermin<IMotion> motion, ITroopDetermin<Float> timegap) {
		mAlienRegistry = pAlienRegistry;
		mSize = size;
		mSoldierDetermin = new DeterminePair<String>(soldier);
		mLocationDetermin = new DeterminePair<Vector2>(position);
		mMotionDetermin = new DeterminePair<IMotion>(motion);
		mTimeGapDetermin = new DeterminePair<Float>(timegap);
	}

	@Override
	public float dispatchSoldiers(final GOEnvironment pEnv) {
		if (hasFinished())
			return -1;
		
		String soldierType = mSoldierDetermin.next(index);
		IMotion motion = mMotionDetermin.next(index);
		Vector2 pos = mLocationDetermin.next(index).cpy();
		pEnv.toAbsolutePosition(pos);
		float delay = mTimeGapDetermin.next(index);
		assert(delay >= 0);
		
		GOFactory<Alien> factory = mAlienRegistry.getFilteredFactory(soldierType);
		Alien alien = factory.create(pEnv, pos, new Vector2());
		alien.getShape().getWidthScaled();
		alien.setMotion(motion);
		index++;
		
		return delay;
	}
	
	@Override
	public boolean hasFinished() {
		return index >= mSize;
	}
	
	@Override
	public void reset() {
		index = 0;
		mSoldierDetermin.mPrevResult = null;
		mMotionDetermin.mPrevResult = null;
		mLocationDetermin.mPrevResult = null;
		mTimeGapDetermin.mPrevResult = null;
	}

	private static class DeterminePair<T> {
		ITroopDetermin<T> mDetermin;
		T mPrevResult;
		
		public DeterminePair(ITroopDetermin<T> pDetermin) {
			mDetermin = pDetermin;
		}
		
		public T next(int index) {
			mPrevResult = mDetermin.next(index, mPrevResult);
			return mPrevResult;
		}
	}

}
