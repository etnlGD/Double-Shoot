package com.doubleshoot.alien;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.object.ITaggedObject;
import com.doubleshoot.score.IScorer;
import com.doubleshoot.score.Scoreable;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class ScorerBehaviorWapper implements IBehavior {
	private IScorer mScorer;
	
	public ScorerBehaviorWapper(IScorer pScorer) {
		assert(pScorer != null);
		mScorer = pScorer;
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source) {
		if (source instanceof ITaggedObject && host instanceof Scoreable )
			mScorer.scoring((Scoreable) host, (ITaggedObject) source);
	}

}
