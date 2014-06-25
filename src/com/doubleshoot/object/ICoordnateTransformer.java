package com.doubleshoot.object;

import com.badlogic.gdx.math.Vector2;

public interface ICoordnateTransformer {
	
	public void toAbsolutePosition(Vector2 relative);
	
}
