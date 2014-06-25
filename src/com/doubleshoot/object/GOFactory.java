package com.doubleshoot.object;

import com.badlogic.gdx.math.Vector2;

public interface GOFactory<T extends GameObject> {
	
	/**
	 * Create a game object in the specified environment.
	 * 
	 * @param env
	 * @param pos
	 * @param velocity
	 * @return
	 */
	public T create(GOEnvironment env, Vector2 pos, Vector2 velocity);
	
}
