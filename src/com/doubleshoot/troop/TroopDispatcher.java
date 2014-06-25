package com.doubleshoot.troop;

import com.doubleshoot.alien.Alien;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GORegistry;

public class TroopDispatcher implements Runnable {
	private TroopGenerator mGenerator;
	private GOEnvironment mEnvironment;
	private GORegistry<Alien> mAlienRegistry;
	
	public TroopDispatcher(GOEnvironment pEnv,
			GORegistry<Alien> pAlienRegistry,
			TroopGenerator pGenerator) {
		mEnvironment = pEnv;
		mAlienRegistry = pAlienRegistry;
		mGenerator = pGenerator;
	}
	
	@Override
	public void run() {
		final ITroop troop = mGenerator.nextTroop(mEnvironment, mAlienRegistry);
		mEnvironment.schedule(new Runnable() {
			
			@Override
			public void run() {
				mEnvironment.schedule(this, troop.dispatchSoldiers(mEnvironment));
			}
		});
		
		mEnvironment.schedule(this, mGenerator.nextSleepTime());
	}
	
}
