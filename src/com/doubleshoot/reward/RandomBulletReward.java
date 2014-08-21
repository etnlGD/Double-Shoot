package com.doubleshoot.reward;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class RandomBulletReward implements IBehavior {
	private static Random sRandom = new Random();
	private final float mProbability;

	public RandomBulletReward(float pProbability) {
		mProbability = pProbability;
	}
	
	private ArrayList<GOFactory<Reward>> mRewardFactories
		= new ArrayList<GOFactory<Reward>>();
	
	public void addRewardType(GOFactory<Reward> factory) {
		mRewardFactories.add(factory);
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (damage <= 0 || mRewardFactories.isEmpty())
			return;
		
		if (sRandom.nextFloat() < mProbability) {
			int index = sRandom.nextInt(mRewardFactories.size());
			GOFactory<Reward> f = mRewardFactories.get(index);
			IAreaShape shape = host.getShape();
			f.create(host.getEnvironment(),
					new Vector2(shape.getX(), shape.getY()),
					new Vector2());
		}
	}

}
