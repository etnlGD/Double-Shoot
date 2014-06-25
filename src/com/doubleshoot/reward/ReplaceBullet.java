package com.doubleshoot.reward;

import com.doubleshoot.bullet.BulletEmitter;
import com.doubleshoot.shooter.BaseShooter;

public class ReplaceBullet implements IRewardPolicy {
	private BulletEmitter mBulletEmitter;
	
	public ReplaceBullet(BulletEmitter pEmitter) {
		mBulletEmitter = pEmitter;
	}

	@Override
	public void onRewarding(BaseShooter target) {
		if (target instanceof BaseShooter) {
			BaseShooter shooter = target;
			shooter.setBulletEmitter(mBulletEmitter.copy());
		}
	}

}
