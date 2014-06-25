package com.doubleshoot.bullet;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;

import android.util.SparseArray;

import com.doubleshoot.object.GOEnvironment;

public class BulletEmitter implements IUpdateHandler {
	private SparseArray<Barrel> mBarrels = new SparseArray<Barrel>();
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		for (int i = 0; i < mBarrels.size(); ++i)
			mBarrels.valueAt(i).unfreeze(pSecondsElapsed);
	}

	public void emit(GOEnvironment env, IEntity parent, NewBulletCallback callback) {
		for (int i = 0; i < mBarrels.size(); ++i)
			mBarrels.valueAt(i).doEmit(env, parent, callback);
	}
	
	public float getMinFrozenTime() {
		float minTime = Float.MAX_VALUE;
		for (int i = 0; i < mBarrels.size(); ++i) {
			float tm = mBarrels.valueAt(i).getFrozenTime();
			if (minTime > tm)
				minTime = tm;
		}
		
		return minTime;
	}
	
	public void setBarrel(int channel, Barrel barrel) {
		mBarrels.put(channel, barrel);
	}
	
	public void removeBarrel(int channel) {
		mBarrels.remove(channel);
	}
	
	public void clearBarrels() {
		mBarrels.clear();
	}
	
	public Barrel getBarrel(int channel) {
		return mBarrels.get(channel);
	}

	@Override
	public void reset() {
		for (int i = 0; i < mBarrels.size(); ++i) {
			Barrel barrel = mBarrels.valueAt(i);
			barrel.reset();
		}
	}
	
	public BulletEmitter copy() {
		BulletEmitter emitter = new BulletEmitter();
		for (int i = 0; i < mBarrels.size(); i++)
			emitter.setBarrel(mBarrels.keyAt(i), mBarrels.valueAt(i).copy());
		
		return emitter;
	}
}
