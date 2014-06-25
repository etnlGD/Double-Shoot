package com.doubleshoot.alien;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.score.IScorer;
import com.doubleshoot.score.Scoreable;
import com.doubleshoot.score.ScorerFinder;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public abstract class ScoreGainBehavior implements IBehavior {
	private ScorerFinder mScorerFinder;
	
	public ScoreGainBehavior(ScorerFinder scorerFinder) {
		mScorerFinder = scorerFinder;
	}
	
	protected abstract int calculateGained(
			BaseShooter host, int totalScore, float damage);
	
	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (host instanceof Scoreable) {
			IScorer scorer = mScorerFinder.findScorer(source);
			if (scorer != null) {
				int totalScore = ((Scoreable) host).getTotalScore();
				scorer.addScore(calculateGained(host, totalScore, damage));
			}
		}
	}

}