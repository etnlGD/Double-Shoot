package com.doubleshoot.shooter;

public class ContinuousShootPolicy implements ShootPolicy {

	@Override
	public float nextCheckTime(BaseShooter shooter) {
		return 0.01f;
	}

	@Override
	public boolean shouldShoot(BaseShooter shooter) {
		return true;
	}

}
