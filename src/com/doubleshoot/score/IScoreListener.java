package com.doubleshoot.score;

public interface IScoreListener {
	public void onScoreChange(int pDelta);
	public void onScoreReset();
}
