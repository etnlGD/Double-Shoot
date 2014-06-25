package com.doubleshoot.troop;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class VLineEquiPosFactory implements PositionDetermineFactory {
	private static Random sRandom = new Random();
	private Randomizer.Float mYCoord;
	private Randomizer.Float mYDelta;
	
	public VLineEquiPosFactory(Randomizer.Float yCoord, Randomizer.Float yDelta) {
		mYCoord = yCoord;
		mYDelta = yDelta;
	}
	
	@Override
	public ITroopDetermin<Vector2> create(int alienCount) {
		float x = sRandom.nextBoolean() ? 1 : 0;
		
		float yOffset = mYCoord.shuffle();
		float dy = Math.abs(mYDelta.shuffle());
		
		Vector2 initPos, delta;
		if (sRandom.nextBoolean()) {	// top to bottom
			delta = new Vector2(0, dy);
			initPos = new Vector2(x, yOffset);
		} else {	// bottom to top
			delta = new Vector2(0, -dy);
			initPos = new Vector2(x, yOffset + dy*alienCount);
		}
		
		
		return new EquidiffPosDeterminer(initPos, delta);
	}
}
