package com.doubleshoot.troop;

import org.andengine.entity.modifier.PathModifier.Path;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.motion.PathMotion;
import com.doubleshoot.object.ICoordnateTransformer;

public class VMotionFactory implements MotionDetermineFactory {
	private float mMaxY;
	private Randomizer.Float mVelSize;
	
	public VMotionFactory(float maxY, Randomizer.Float velSize) {
		mMaxY = maxY;
		mVelSize = velSize;
	}
	
	private Path transform(ICoordnateTransformer env, float[] xCoords, float[] yCoords) {
		Vector2 point = new Vector2();
		Path p = new Path(xCoords.length);
		for (int i = 0; i < xCoords.length; ++i) {
			point.set(xCoords[i], yCoords[i]);
			env.toAbsolutePosition(point);
			p.to(point.x, point.y);
		}
		
		return p;
	}
	
	@Override
	public ITroopDetermin<IMotion> create(
			ICoordnateTransformer transfomer, Vector2 leaderPos) {
		float[] xCoords = new float[] { 0.5f, 1.0f - leaderPos.x };
		float[] yCoords = new float[] { mMaxY, leaderPos.y };
		
		return new UniformDeterminer<IMotion>(
					new PathMotion(
							transform(transfomer, xCoords, yCoords),
							mVelSize.shuffle()));
	}

}
