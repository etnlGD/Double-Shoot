package com.doubleshoot.bullet;

import org.andengine.audio.sound.Sound;

import com.doubleshoot.object.GameObject;

public class BulletSound implements BulletListener {
//	private Sound mHitSound;
	private Sound mShootSound;
	
	public BulletSound(Sound pShoot) {
		mShootSound = pShoot;
	}
 	
	@Override
	public void onExplosion(Bullet pBullet, GameObject pCollided) {
	}

	@Override
	public void onCollision(Bullet pBullet, GameObject pCollided) {
		
	}

	@Override
	public void onShooted(Bullet bullet) {
		mShootSound.play();
	}
	
}
