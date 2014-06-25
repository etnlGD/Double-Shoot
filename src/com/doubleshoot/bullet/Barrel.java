package com.doubleshoot.bullet;

import org.andengine.entity.IEntity;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.bullet.distribution.BulletDistribution;
import com.doubleshoot.coordinate.EntityCoordinate;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GOFactory;

public class Barrel {
	private BulletDistribution mDistribution;
	private GOFactory<Bullet> mPrototype;
	private float mCycle;
	private float mFrozenTime;
	private Vector2 mBarrelPosition = new Vector2();
	
	public Barrel(BulletDistribution d, GOFactory<Bullet> p) {
		setDistribution(d);
		setPrototype(p);
	}
	
	public void setDistribution(BulletDistribution pDistribution) {
		if (pDistribution == null)
			throw new IllegalArgumentException("Null distribution");
		
		mDistribution = pDistribution;
	}
	
	public void setPrototype(GOFactory<Bullet> pPrototype) {
		if (pPrototype == null)
			throw new IllegalArgumentException("Null prototype");
		
		mPrototype = pPrototype;
	}
	
	public void setBarrelPosition(float x, float y) {
		mBarrelPosition.set(x, y);
	}
	
	public void setFrozenCycle(float pCycle) {
		mCycle = pCycle;
	}
	
	public float getFrozenCycle() {
		return mCycle;
	}
	
	public boolean isAvailable() {
		return mFrozenTime <= 0;
	}
	
	public float getFrozenTime() {
		return mFrozenTime;
	}

	public void unfreeze(float delta) {
		if (!isAvailable())
			mFrozenTime -= delta;
	}
	
	private void freeze() {
		mFrozenTime += mCycle;
	}
	
	private Vector2 getScenePos(int index, IEntity parent) {
		Vector2 pos = mDistribution.getBulletPosition(index);
		pos.add(mBarrelPosition);
		return EntityCoordinate.localToScenePosition(parent, pos);
	}
	
	private Vector2 getSceneDir(int index, IEntity parent) {
		Vector2 dir = mDistribution.getBulletVelocity(index);
		return EntityCoordinate.localToSceneDirection(parent, dir);
	}
	
	public void doEmit(GOEnvironment env, IEntity parent, NewBulletCallback callback) {
		while (isAvailable()) {
			int cnt = mDistribution.getBulletCount();
			for (int j = 0; j < cnt; ++j) {
				Vector2 scenePos = getScenePos(j, parent);
				Vector2 sceneDir = getSceneDir(j, parent);
				Bullet bullet = mPrototype.create(env, scenePos, sceneDir);
				bullet.setShooter(parent);
				if (callback != null)
					callback.onNewBullet(bullet);
				bullet.onShooted();
			}
			
			freeze();
		}
	}
	
	public void reset() {
		mFrozenTime = 0;
	}
	
	public Barrel copy() {
		Barrel copy = new Barrel(mDistribution.copy(), mPrototype);
		copy.mBarrelPosition = mBarrelPosition.cpy();
		copy.mCycle = mCycle;
		
		return copy;
	}
}