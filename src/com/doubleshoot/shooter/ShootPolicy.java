package com.doubleshoot.shooter;


/**
 * Determine when to shoot.
 * 
 * @author etnlGD
 *
 */
public interface ShootPolicy {
	
	public float nextCheckTime(BaseShooter shooter);
	
	public boolean shouldShoot(BaseShooter shooter);
	
}
