package com.doubleshoot.score;

public interface IScorer {
	
//	public void scoring(Scoreable source, ITaggedObject obtainer, float damage);
	public void addScore(int score);
	
	public int getScore();
	
	public void setScoreChangeListener(IScoreChangeListener lis);

}
