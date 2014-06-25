package com.doubleshoot.body;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public interface BodyFactory {
	
	public Body createBody(PhysicsWorld world, BodyType type, Vector2 pos, float rot);
	
}