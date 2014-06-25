package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;

public class EquidiffPosDeterminer implements ITroopDetermin<Vector2> {
	private Vector2 mInitial;
	private Vector2 mDelta;
	
	public EquidiffPosDeterminer(Vector2 initial, Vector2 delta) {
		mInitial = initial.cpy();
		mDelta = delta.cpy();
	}
	
	@Override
	public Vector2 next(int index, Vector2 prev) {
		return mDelta.cpy().mul(index).add(mInitial);
	}

}
