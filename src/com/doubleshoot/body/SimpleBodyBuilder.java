package com.doubleshoot.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.doubleshoot.shooter.FixtureFactory;

public class SimpleBodyBuilder {
	
	public static BodyBuilder newCircle(float radius, float density) {
		return newCircle(radius, FixtureFactory.withDensity(density));
	}
	
	public static BodyBuilder newCircle(float radius, FixtureDef pFixtureDef) {
		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.addCircle(new Vector2(), radius, pFixtureDef);
		return bodyBuilder;
	}
	
	public static BodyBuilder newBox(float width, float height, float density) {
		return newBox(width, height, FixtureFactory.withDensity(density));
	}
	
	public static BodyBuilder newBox(float width, float height, FixtureDef pFixtureDef) {
		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.addBox(new Vector2(), width/2, height/2, 0, pFixtureDef);
		return bodyBuilder;
	}
	
}