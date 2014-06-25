package com.doubleshoot.alien;

import com.doubleshoot.score.ScorerFinder;
import com.doubleshoot.shooter.BaseShooter;

public class DeadScoreBehavior extends ScoreGainBehavior {

	public DeadScoreBehavior(ScorerFinder scorerFinder) {
		super(scorerFinder);
	}

	@Override
	protected int calculateGained(BaseShooter host, int totalScore, float damage) {
		return totalScore / 2;
	}

}
