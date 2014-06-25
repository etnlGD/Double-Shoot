package com.doubleshoot.game.listener;
public interface ICompositeGameListener extends IGameListener {
	public void appendListener(IGameListener pListener);
	public void removeListener(IGameListener pListener);
}