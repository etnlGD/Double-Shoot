package com.doubleshoot.troop;

import com.doubleshoot.alien.Alien;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.object.ICoordnateTransformer;

public interface TroopGenerator {
	
	public ITroop nextTroop(
			ICoordnateTransformer pTransformer,
			GORegistry<Alien> pAlienRegistry);
	
	public float nextSleepTime();
	
}
