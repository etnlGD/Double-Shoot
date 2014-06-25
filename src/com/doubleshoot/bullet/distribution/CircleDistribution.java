package com.doubleshoot.bullet.distribution;

import com.badlogic.gdx.math.Vector2;

public class CircleDistribution extends CountableBulletDistribution {
	private float mRadius;
	
	public CircleDistribution(float pRadius, int pCount) {
		mRadius = pRadius;
		
		setBulletCount(pCount);
	}
	
	@Override
	public Vector2 getBulletVelocity(int index) {
		checkIndex(index);
		
		float delta = 360 / getBulletCount();
		double angle = Math.toRadians(delta * index);
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}

	@Override
	public Vector2 getBulletPosition(int index) {
		return getBulletVelocity(index).mul(mRadius);
	}

	@Override
	public BulletDistribution copy() {
		return new CircleDistribution(mRadius, getBulletCount());
	}

}
