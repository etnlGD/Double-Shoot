package com.doubleshoot.troop;

public class UniformDeterminer<T> implements ITroopDetermin<T> {
	private T mValue;
	
	public UniformDeterminer(T uniformValue) {
		mValue = uniformValue;
	}
	
	@Override
	public T next(int index, T prev) {
		return mValue;
	}

}
