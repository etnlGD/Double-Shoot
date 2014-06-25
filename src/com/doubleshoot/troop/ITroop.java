package com.doubleshoot.troop;

import com.doubleshoot.object.GOEnvironment;

public interface ITroop {
	
	/**
	 * Means dispatch this batch of soldiers.
	 */
	public float dispatchSoldiers(GOEnvironment pEnv);
	
	/**
	 * @return if finished
	 */
	public boolean hasFinished();
	
	/**
	 * Reset to redispatch the same soldiers
	 */
	public void reset();
	
}
