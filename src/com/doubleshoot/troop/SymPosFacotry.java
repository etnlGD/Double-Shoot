package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.troop.Randomizer.Float;

final class SymPosFacotry implements PositionDetermineFactory {
	private final Float deltaX;
	private final Float centerX;

	SymPosFacotry(Float centerX, Float deltaX) {
		this.deltaX = deltaX;
		this.centerX = centerX;
	}

	@Override
	public ITroopDetermin<Vector2> create(int alienCount) {
		return new SymmetryPosDeterminer(
			new Vector2(centerX.shuffle(), 0), deltaX.shuffle());
	}
}