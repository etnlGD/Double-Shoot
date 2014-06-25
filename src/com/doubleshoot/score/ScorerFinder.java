package com.doubleshoot.score;

import com.doubleshoot.shooter.Harmful;

public interface ScorerFinder {
	
	public IScorer findScorer(Harmful harmful);
	
}
