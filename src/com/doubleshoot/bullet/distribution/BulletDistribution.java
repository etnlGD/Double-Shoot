package com.doubleshoot.bullet.distribution;

import com.badlogic.gdx.math.Vector2;

public interface BulletDistribution {
	
	public int getBulletCount();
	
	/// Unit pixel/s
	public Vector2 getBulletVelocity(int index);
	
	/// Unit pixel
	public Vector2 getBulletPosition(int index);
	
	public BulletDistribution copy();
}
