package com.doubleshoot.bullet.distribution;

import android.util.SparseArray;

import com.badlogic.gdx.math.Vector2;

public class ParrallelDistribution implements BulletDistribution {
	private SparseArray<Vector2> mEmittPositions = new SparseArray<Vector2>();
	private Vector2 mDirection = new Vector2(0, 1);
	
	public void setDirection(Vector2 direction) {
		assert(direction != null);
		mDirection = direction;
	}
	
	public void setPosition(int key, Vector2 vec) {
		if (vec == null)
			mEmittPositions.remove(key);
		else
			mEmittPositions.put(key, vec);
	}
	
	public void clearPositions() {
		mEmittPositions.clear();
	}
	
	@Override
	public Vector2 getBulletVelocity(int index) {
		return mDirection.cpy();
	}
	
	@Override
	public Vector2 getBulletPosition(int index) {
		return mEmittPositions.valueAt(index).cpy();
	}

	@Override
	public int getBulletCount() {
		return mEmittPositions.size();
	}

	// TODO verify.
	@Override
	public BulletDistribution copy() {
		ParrallelDistribution copy = new ParrallelDistribution();
		copy.setDirection(mDirection);
		for (int i = 0; i < mEmittPositions.size(); i++) {
			Vector2 vec = mEmittPositions.valueAt(i);
			int key = mEmittPositions.keyAt(i);
			copy.mEmittPositions.put(key, vec.cpy());
		}
		
		return copy;
	}

}
