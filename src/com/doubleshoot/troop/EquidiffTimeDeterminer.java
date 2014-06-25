package com.doubleshoot.troop;

public class EquidiffTimeDeterminer implements ITroopDetermin<Float> {
	private float mInitial;
	private float mDelta;
	
	public EquidiffTimeDeterminer(float initial, float delta) {
		mInitial = initial;
		mDelta = delta;
	}
	
	@Override
	public Float next(int index, Float prev) {
		return Math.max(mInitial + index * mDelta, 0);
	}

}
