package com.doubleshoot.alien;

import org.andengine.entity.IEntity;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.motion.UniformVelocityMotion;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class RevengeBehavior implements IBehavior {
	private UniformVelocityMotion mMotion;
	private float mLimitY;
	private float mVelSize;
	
	public RevengeBehavior(float pVelSize, float pLimitY) {
		mVelSize = pVelSize;
		mLimitY = pLimitY;
		mMotion = new UniformVelocityMotion();
	}
	
	private IEntity extractTarget(Harmful source) {
		if (source instanceof Bullet) {
			Bullet bullet = (Bullet) source;
			return bullet.getShooter();
		}
		
		return null;
	}
	
	private Vector2 getVelocity(IEntity avenger, IEntity target) {
		Vector2 pos1 = new Vector2(avenger.getX(), target.getY());
		Vector2 pos2 = new Vector2(target.getX(), target.getY());
		
		return pos2.sub(pos1).nor().mul(mVelSize);
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source) {
		if (host.getShape().getY() > mLimitY) 
			return;
		
		if (host instanceof Alien) {
			Alien avenger = (Alien) host;
			IEntity target = extractTarget(source); 
			if (target != null) {
				mMotion.setVelocity(getVelocity(avenger.getShape(), target));
				avenger.setMotion(mMotion);
			}
		}
	}
}
