package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;

public class SymmetryPosDeterminer implements ITroopDetermin<Vector2> {
	private Vector2 mOrigin;
	private float mDeltaX;
	
	public SymmetryPosDeterminer(Vector2 origin, float deltaX) {
		mOrigin = origin.cpy();
		mDeltaX = deltaX;
	}
	
	@Override
	public Vector2 next(int index, Vector2 prev) {
		float delta = (index+1) / 2 * mDeltaX;
		if ((index & 1) == 0)
			delta = -delta;
		
		return new Vector2(delta, 0).add(mOrigin);
	}

}
