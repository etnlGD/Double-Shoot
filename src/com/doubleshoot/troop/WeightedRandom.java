package com.doubleshoot.troop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WeightedRandom<T> {
	private float mTotalWeight;
	private List<Float> mWeights = new ArrayList<Float>();
	private List<T> mAssociatedData = new ArrayList<T>();
	
	public void addData(float weight, T data) {
		if (weight <= 0)
			throw new IllegalArgumentException("Weight must be positive");
		
		mTotalWeight += weight;
		mWeights.add(mTotalWeight);
		mAssociatedData.add(data);
	}
	
	public T shuffle(Random rnd) {
		float value = rnd.nextFloat() * mTotalWeight;
		int idx = Collections.binarySearch(mWeights, value);
		if (idx < 0)
			idx = Math.min(-idx - 1, mWeights.size() - 1);
		
		return mAssociatedData.get(idx);
	}
	
}
