package com.doubleshoot.troop;

import java.util.Random;

import com.doubleshoot.alien.Alien;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.object.ICoordnateTransformer;

public class CompositeTroopGenerator implements TroopGenerator {
	private WeightedRandom<TroopGenerator> mWeights = new WeightedRandom<TroopGenerator>();
	private Random mRandom = new Random();
	private TroopGenerator mPrevious;
	
	public void addGenerator(float weight, TroopGenerator generator) {
		if (generator == null)
			throw new NullPointerException();
		
		mWeights.addData(weight, generator);
	}
	
	@Override
	public ITroop nextTroop(ICoordnateTransformer pEnv, GORegistry<Alien> pAlienRegistry) {
		mPrevious = mWeights.shuffle(mRandom);
		return mPrevious.nextTroop(pEnv, pAlienRegistry);
	}
	
	@Override
	public float nextSleepTime() {
		if (mPrevious != null)
			return mPrevious.nextSleepTime();
		
		return 0;
	}
}
