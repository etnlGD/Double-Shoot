package com.doubleshoot.bullet.distribution;

import com.badlogic.gdx.math.Vector2;

public class ScatterDistribution extends CountableBulletDistribution {
	private float mMinAngle;
	private float mMaxAngle;
	
	public ScatterDistribution(float pMinAngle, float pMaxAngle, int pCount) {
		mMinAngle = pMinAngle;
		mMaxAngle = pMaxAngle;
		
		setBulletCount(pCount);
	}
	
	@Override
	public Vector2 getBulletVelocity(int index) {
		checkIndex(index);
		
		float delta = (mMaxAngle-mMinAngle) / (getBulletCount()+1);
		double angle = Math.toRadians(delta * (index+1) + mMinAngle);
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}

	@Override
	public Vector2 getBulletPosition(int index) {
		checkIndex(index);
		
		return new Vector2();
	}

	@Override
	public BulletDistribution copy() {
		return new ScatterDistribution(mMinAngle, mMaxAngle, getBulletCount());
	}
	
	

}
