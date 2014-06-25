package com.doubleshoot.alien;

import java.util.Random;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class RandomDeadBulletReward implements IBehavior {
	private static Random sRandom = new Random();
	
	@Override
	public void onActivated(BaseShooter host, Harmful source) {
		boolean hasReward = true;
		for (int i = 0; i < 3; i++) {
			hasReward &= sRandom.nextBoolean();
		}
		
		if (hasReward) {
		}
	}

}
