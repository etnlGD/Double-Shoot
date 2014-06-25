package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.motion.UniformVelocityMotion;
import com.doubleshoot.object.ICoordnateTransformer;

public class HorzMotionFactory implements MotionDetermineFactory {
	private Randomizer.Float mVelSize;
	
	public HorzMotionFactory(Randomizer.Float velSize) {
		mVelSize = velSize;
	}
	
	@Override
	public ITroopDetermin<IMotion> create(
			ICoordnateTransformer t, Vector2 leaderPos) {
		Vector2 vel = new Vector2(mVelSize.shuffle() * (1 - 2*leaderPos.x), 0);
		IMotion motion = new UniformVelocityMotion(vel);
		return new UniformDeterminer<IMotion>(motion);
	}

}
