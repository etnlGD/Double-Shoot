package com.doubleshoot.alien;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.shooter.ShooterFactory;

/**
 * This class describes the attributes of a kind of alien.
 * 
 * You create an instance of this class while loading alien definition
 * from files most of the time. Once instantiated, you can create as many
 * aliens of this kind as you want.
 * 
 * @author Alpha
 *
 */
public class AlienFactory extends ShooterFactory<Alien> {
	private int mScores;
	
	public AlienFactory(float pDamage, float pInitHealth, int pScores) {
		super(pDamage, pInitHealth);
		mScores = pScores;
		setBodyType(BodyType.DynamicBody);
	}
	
	@Override
	protected void onObjectCreated(Alien created) {
		super.onObjectCreated(created);
		created.setSocre(mScores);
//		mCallback.onNewAlien(alien, this);
	}
	
	@Override
	protected Alien createObject(GOEnvironment env, IAreaShape shape, Body body) {
		return new Alien(shape, body, env);
	}

}
}
