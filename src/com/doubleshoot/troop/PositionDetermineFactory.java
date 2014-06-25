package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;

public interface PositionDetermineFactory {
	
	public ITroopDetermin<Vector2> create(int alienCount);
	
}
