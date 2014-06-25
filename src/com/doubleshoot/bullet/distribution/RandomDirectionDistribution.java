package com.doubleshoot.bullet.distribution;

import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;

public class RandomDirectionDistribution extends CountableBulletDistribution {
	private int mMinAngle;
	private int mMaxAngle;
	
	public RandomDirectionDistribution(int minAngle, int maxAngle) {
		mMinAngle = minAngle;
		mMaxAngle = maxAngle;
		
		setBulletCount(1);
	}
	
	@Override
	public Vector2 getBulletVelocity(int index) {
		checkIndex(index);
		
		double angle = Math.toRadians(MathUtils.random(mMinAngle, mMaxAngle));
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}

	@Override
	public Vector2 getBulletPosition(int index) {
		checkIndex(index);
		return new Vector2();
	}

	@Override
	public BulletDistribution copy() {
		return new RandomDirectionDistribution(mMinAngle, mMaxAngle);
	}

}
