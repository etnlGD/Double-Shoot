package com.doubleshoot.bullet.distribution;


public abstract class CountableBulletDistribution implements BulletDistribution {
	private int mCount;
	
	public void setBulletCount(int count) {
		if (count < 0)
			throw new IllegalArgumentException("Negative count");
		
		mCount = count;
	}

	@Override
	public int getBulletCount() {
		return mCount;
	}
	
	protected void checkIndex(int index) {
		if (index < 0 || index >= mCount)
			throw new ArrayIndexOutOfBoundsException();
	}
}
