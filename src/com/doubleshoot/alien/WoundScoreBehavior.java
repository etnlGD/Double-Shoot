package com.doubleshoot.alien;

import com.doubleshoot.score.ScorerFinder;
import com.doubleshoot.shooter.BaseShooter;

public class WoundScoreBehavior extends ScoreGainBehavior {

	public WoundScoreBehavior(ScorerFinder scorerFinder) {
		super(scorerFinder);
	}

	@Override
	protected int calculateGained(BaseShooter host, int totalScore, float damage) {
		if (damage > 0)
			return (int) (totalScore * damage / host.getInitHealth() * 0.5f);
		
		return 0;
	}

}
