package com.doubleshoot.troop;

import java.util.Random;

public class WeightedDeterminer<T> implements ITroopDetermin<T> {
	private WeightedRandom<T> mWeights = new WeightedRandom<T>();
	private Random mRandom = new Random();
	
	public void addData(float weight, T data) {
		mWeights.addData(weight, data);
	}
	
	@Override
	public T next(int index, T prev) {
		return mWeights.shuffle(mRandom);
	}

}
