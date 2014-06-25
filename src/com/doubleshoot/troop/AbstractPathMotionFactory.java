package com.doubleshoot.troop;

import org.andengine.entity.modifier.PathModifier.Path;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.motion.PathMotion;
import com.doubleshoot.object.ICoordnateTransformer;

public abstract class AbstractPathMotionFactory implements MotionDetermineFactory {
	private Randomizer.Float mVelSize;
	
	public AbstractPathMotionFactory(Randomizer.Float velSize) {
		mVelSize = velSize;
	}
	
	private Path transform(ICoordnateTransformer env, Path relative) {
		float[] xCoords = relative.getCoordinatesX();
		float[] yCoords = relative.getCoordinatesY();
		Vector2 point = new Vector2();
		for (int i = 0; i < xCoords.length; ++i) {
			point.set(xCoords[i], yCoords[i]);
			env.toAbsolutePosition(point);
			xCoords[i] = point.x;
			yCoords[i] = point.y;
		}
		
		return relative;
	}
	
	protected abstract Path createRelativePath(Vector2 leaderPos);
	
	@Override
	public ITroopDetermin<IMotion> create(
			ICoordnateTransformer transformer, Vector2 leaderPos) {
		Path p = createRelativePath(leaderPos);
		return new UniformDeterminer<IMotion>(
				new PathMotion(transform(transformer, p), mVelSize.shuffle()));
	}
	
}
