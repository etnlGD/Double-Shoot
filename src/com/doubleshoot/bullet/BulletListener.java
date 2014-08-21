package com.doubleshoot.bullet;

import com.doubleshoot.object.GameObject;

public interface BulletListener {
	
	public void onExplosion(Bullet pBullet, GameObject pCollided);
	
	public void onCollision(Bullet pBullet, GameObject pCollided);
	
	public void onShooted(Bullet bullet);
	
}
