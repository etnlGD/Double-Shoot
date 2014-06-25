package com.doubleshoot.score;

import com.doubleshoot.object.ITaggedObject;
import com.doubleshoot.shooter.TagManager;

public class DoubleScorer implements IScorer {
	private PackedScore mLeft;
	private PackedScore mRight;
	
	public DoubleScorer(IScoreListener pLeft, IScoreListener pRight) {
		mLeft = new PackedScore(pLeft);
		mRight = new PackedScore(pRight);
	}
	
	@Override
	public void scoring(Scoreable source, ITaggedObject obtainer) {
		if (obtainer.hasTag(TagManager.sLeftHero))
			mLeft.onScoreChanged(source.countScore());
		
		if (obtainer.hasTag(TagManager.sRightHero))
			mRight.onScoreChanged(source.countScore());
	}

	@Override
	public int getScore(String tag) {
		if (tag == TagManager.sLeftHero)
			return mLeft.value();
		
		if (tag == TagManager.sRightHero)
			return mRight.value();
		
		return 0;
	}

	@Override
	public void resetScore() {
		mLeft.reset();
		mRight.reset();
	}
	
	private class PackedScore {
		IScoreListener mListener;
		int mScore;
		
		public PackedScore(IScoreListener pListener) {
			mListener = pListener;
		}
		
		public void onScoreChanged(int delta) {
			mScore += delta;
			mListener.onScoreChange(delta);
		}
		
		public int value() {
			return mScore;
		}
		
		public void reset() {
			mScore = 0;
			mListener.onScoreReset();
		}
	}

}
