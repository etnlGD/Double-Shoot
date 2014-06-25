package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.motion.UniformVelocityMotion;
import com.doubleshoot.object.ICoordnateTransformer;

public class UniVelMotionFactory implements MotionDetermineFactory {
	private Vector2 mDirection;
	private Randomizer.Float mVelSize;
	
	public UniVelMotionFactory(Vector2 dir, Randomizer.Float velSize) {
		mDirection = dir.cpy().nor();
		mVelSize = velSize;
	}
	
	@Override
	public ITroopDetermin<IMotion> create(
			ICoordnateTransformer transfomer, Vector2 leaderPos) {
		Vector2 vel = mDirection.cpy().mul(mVelSize.shuffle());
		return new UniformDeterminer<IMotion>(new UniformVelocityMotion(vel));
	}

}
