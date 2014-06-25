package com.doubleshoot.reward;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.doubleshoot.object.AbstractGOFactory;
import com.doubleshoot.object.GOEnvironment;

public class RewardFactory extends AbstractGOFactory<Reward> {
	private IRewardPolicy mPolicy;
	
	public RewardFactory(IRewardPolicy pPolicy) {
		mPolicy = pPolicy;
	}
	
	@Override
	protected Reward createObject(GOEnvironment env, 
			IAreaShape shape, Body body) {
		return new Reward(mPolicy, body, shape, env);
	}

}
