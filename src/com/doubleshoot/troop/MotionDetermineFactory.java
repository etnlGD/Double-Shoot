package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.object.ICoordnateTransformer;

public interface MotionDetermineFactory {
	
	public ITroopDetermin<IMotion> create(
			ICoordnateTransformer transfomer, Vector2 leaderPos);
	
}
