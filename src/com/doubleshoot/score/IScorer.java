package com.doubleshoot.score;

import com.doubleshoot.object.ITaggedObject;

public interface IScorer {
	
	public void scoring(Scoreable source, ITaggedObject obtainer);
	
	public int getScore(String tag);
	
	public void resetScore();
}
