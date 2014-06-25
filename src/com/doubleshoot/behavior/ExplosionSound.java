package com.doubleshoot.behavior;

import org.andengine.audio.sound.Sound;

import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class ExplosionSound implements IBehavior {
	private Sound mSound;

	public ExplosionSound(Sound sound) {
		assert(sound != null);
		mSound = sound;
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (damage > 0)
			mSound.play();
	}

}
