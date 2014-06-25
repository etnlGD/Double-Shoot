package com.doubleshoot.shooter;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class FixtureFactory {
	
	public static FixtureDef createFixture(GameObjectType type, float density) {
		FixtureDef fixture = new FixtureDef();
		fixture.density = density;
		
		Filter filter = type.getSharedFilter();
		fixture.filter.categoryBits = filter.categoryBits;
		fixture.filter.maskBits = filter.maskBits;
		fixture.filter.groupIndex = filter.groupIndex;
		
		return fixture;
	}
	
	public static FixtureDef copy(FixtureDef pFixtureDef) {
		if (pFixtureDef == null)
			return null;
		
		FixtureDef copy = new FixtureDef();
		copy.friction = pFixtureDef.friction;
		copy.restitution = pFixtureDef.restitution;
		copy.density = pFixtureDef.density;
		copy.isSensor = pFixtureDef.isSensor;
		copy.filter.categoryBits = pFixtureDef.filter.categoryBits;
		copy.filter.groupIndex = pFixtureDef.filter.groupIndex;
		copy.filter.maskBits = pFixtureDef.filter.maskBits;
		
		return copy;
	}
	
	public static FixtureDef withDensity(float density) {
		FixtureDef def = new FixtureDef();
		def.density = density;
		return def;
	}
	
	public static FixtureDef sensor(Filter filter) {
		FixtureDef def = new FixtureDef();
		def.isSensor = true;
		if (filter != null) {
			def.filter.categoryBits = filter.categoryBits;
			def.filter.groupIndex = filter.groupIndex;
			def.filter.maskBits = filter.maskBits;
		}
		
		return def;
	}
	
	public static FixtureDef sensor() {
		return sensor(null);
	}
	
}