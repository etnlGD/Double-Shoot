package com.doubleshoot.reward;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.doubleshoot.motion.AcceleratedMotion;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.movable.IMovable;
import com.doubleshoot.movable.MovableBody;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GameObject;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.TagManager;

public class Reward extends GameObject {
	private static IMotion sMotion = new AcceleratedMotion(new Vector2(0, 100));
	private IRewardPolicy mPolicy;
	
	public Reward(IRewardPolicy pPolicy, 
			Body body, IAreaShape shape, GOEnvironment env) {
		super(body, shape, env);
		mPolicy = pPolicy;
		IMovable movable = new MovableBody(getBody());
		movable.setVolocity(new Vector2(0, -50));
		getShape().registerUpdateHandler(
				sMotion.createMotionModifier(movable));
	}

	@Override
	protected int onContactBegin(GameObject other, int param) {
		assert(other instanceof BaseShooter);
		
		if (!other.hasTag(TagManager.sBoundTag)) {
			mPolicy.onRewarding((BaseShooter) other);
			destroy();
		}
		
		return 0;
	}

	@Override
	protected int onContactEnd(GameObject other, int param) {
		return 1;
	}

}
