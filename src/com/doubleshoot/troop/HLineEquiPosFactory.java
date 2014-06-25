package com.doubleshoot.troop;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.troop.Randomizer.Float;

final class HLineEquiPosFactory implements PositionDetermineFactory {
	private Random sRandom = new Random();
	private final Float mDeltaX;
	private final Float mOffXCoord;

	HLineEquiPosFactory(Float offX, Float deltaX) {
		this.mDeltaX = deltaX;
		this.mOffXCoord = offX;
	}

	@Override
	public ITroopDetermin<Vector2> create(int alienCount) {
		float x = mOffXCoord.shuffle();
		float d = mDeltaX.shuffle();
		if (sRandom.nextBoolean()) {
			x = 1.f - x;
			d = -d;
		}
		
		return new EquidiffPosDeterminer(
				new Vector2(x, 0),
				new Vector2(d, 0));
	}
}