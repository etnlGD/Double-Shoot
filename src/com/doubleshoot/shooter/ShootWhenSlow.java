package com.doubleshoot.shooter;

import com.doubleshoot.hero.Hero;

public class ShootWhenSlow implements ShootPolicy {

	@Override
	public float nextCheckTime(BaseShooter shooter) {
		return 0.01f;
	}

	@Override
	public boolean shouldShoot(BaseShooter shooter) {
		if (shooter instanceof Hero)
			return !((Hero) shooter).isFastMoving();
		
		return true;
	}
	
}
