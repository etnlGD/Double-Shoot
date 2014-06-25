package com.doubleshoot.movable;

import com.badlogic.gdx.math.Vector2;

public interface IMovable {
	public Vector2 getPosition();
	public void setPosition(Vector2 pos);
	
	public Vector2 getVelocity();
	public void setVolocity(Vector2 vel);
}
