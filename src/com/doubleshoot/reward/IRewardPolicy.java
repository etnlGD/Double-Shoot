package com.doubleshoot.reward;

import com.doubleshoot.shooter.BaseShooter;

public interface IRewardPolicy {
	public void onRewarding(BaseShooter target);
}
