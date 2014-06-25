package com.doubleshoot.alien;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.movable.MovableBody;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.score.Scoreable;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.Harmful;

public class Alien extends BaseShooter implements Harmful, Scoreable {
	private int mScores;
	private IUpdateHandler mMotionUpdater;
	
	public Alien(IAreaShape shape, Body body, GOEnvironment pEnv) {
		super(shape, body, pEnv);
	}
	
	public void setMotion(IMotion pMotion) {
		getShape().unregisterUpdateHandler(mMotionUpdater);

		mMotionUpdater = pMotion.createMotionModifier(new MovableBody(getBody()));
		getShape().registerUpdateHandler(mMotionUpdater);
	}

	public void setSocre(int scores) {
		if (scores < 0)
			throw new IllegalArgumentException("Negative score");
		
		mScores = scores;
	}
	
	@Override
	protected Filter getBulletFilter() {
		return GameObjectType.EnemyBullet.getSharedFilter();
	}

	@Override
	public int getTotalScore() {
		return mScores;
	}

}
