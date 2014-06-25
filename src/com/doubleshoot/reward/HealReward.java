package com.doubleshoot.reward;

import com.doubleshoot.shooter.BaseShooter;

public class HealReward implements IRewardPolicy {
	private float mHealPercent;
	
	public HealReward(float pHealPercent) {
		mHealPercent = pHealPercent;
	}
	
	@Override
	public void onRewarding(BaseShooter target) {
		target.heal(mHealPercent);
	}

}
