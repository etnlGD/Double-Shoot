package com.doubleshoot.behavior;

import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public interface IBehavior {
	
	public void onActivated(BaseShooter host, Harmful source, float damage);
	
}
