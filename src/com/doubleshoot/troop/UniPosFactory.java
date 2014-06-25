package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;

public class UniPosFactory implements PositionDetermineFactory {
	private Randomizer.Float mXCoord;
	private Randomizer.Float mYCoord;
	
	public UniPosFactory(Randomizer.Float xCoord, Randomizer.Float yCoord) {
		mXCoord = xCoord;
		mYCoord = yCoord;
	}
	
	@Override
	public ITroopDetermin<Vector2> create(int alienCount) {
		return new UniformDeterminer<Vector2>(
				new Vector2(mXCoord.shuffle(), mYCoord.shuffle()));
	}

}
