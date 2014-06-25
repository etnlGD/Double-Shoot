package com.doubleshoot.hero;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.shooter.ShooterFactory;

public class HeroFactory extends ShooterFactory<Hero> {
	private static BodyType sHeroBodyType = BodyType.KinematicBody;
	
	public HeroFactory(float pDamageToAlien, float pInitialHealth) {
		super(pDamageToAlien, pInitialHealth);
		setBodyType(sHeroBodyType);
	}
	
	@Override
	protected Hero createObject(GOEnvironment env, IAreaShape shape, Body body) {
		return new Hero(shape, body, env);
	}
	
}