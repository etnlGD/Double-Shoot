package com.doubleshoot.troop;

import org.andengine.entity.modifier.PathModifier.Path;

import com.badlogic.gdx.math.Vector2;

public class VMotionFactory extends AbstractPathMotionFactory {
	private float mMaxY;
	
	public VMotionFactory(float maxY, Randomizer.Float velSize) {
		super(velSize);
		mMaxY = maxY;
	}
	
	@Override
	protected Path createRelativePath(Vector2 leaderPos) {
		float[] xCoords = new float[] { 0.5f, 1.0f - leaderPos.x };
		float[] yCoords = new float[] { mMaxY, leaderPos.y };
		return new Path(xCoords, yCoords);
	}

}
