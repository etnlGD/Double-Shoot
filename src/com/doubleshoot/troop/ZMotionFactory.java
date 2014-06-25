package com.doubleshoot.troop;

import org.andengine.entity.modifier.PathModifier.Path;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.troop.Randomizer.Float;

public class ZMotionFactory extends AbstractPathMotionFactory {
	private Randomizer.Float mHGap;
	private Randomizer.Float mXMin;
	private Randomizer.Float mXMax;
	
	public ZMotionFactory(Float velSize, Float hGap, Float xMin, Float xMax) {
		super(velSize);
		mHGap = hGap;
		mXMax = xMax;
		mXMin = xMin;
	}
	
	private static Path newZPath(Vector2 init, float hGap, float xMin, float xMax) {
		float[] xCoords = { xMin, xMax };
		if (init.x > 0.5f) {
			xCoords[0] = xMax;
			xCoords[1] = xMin;
		}
		
		float y = init.y;
		Path path = new Path((int) (Math.max((1.f - y) / hGap, 0)) + 1);
		for (int i = 0; i < path.getSize(); ++i) {
			y += hGap;
			path.to(xCoords[i & 1], y);
		}

		return path;
	}

	@Override
	protected Path createRelativePath(Vector2 leaderPos) {
		return newZPath(leaderPos, mHGap.shuffle(), mXMin.shuffle(), mXMax.shuffle());
	}

}
