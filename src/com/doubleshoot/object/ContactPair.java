package com.doubleshoot.object;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

class ContactPair {
	private GameObject objA;
	private GameObject objB;
	
	private ContactPair(GameObject a, GameObject b) {
		assert(a != null && b != null);
		objA = a;
		objB = b;
	}
	
	void processBegin() {
		if (objA.isDestroyed() || objB.isDestroyed())
			return;
		
		int ret = objA.onContactBegin(objB, 0);
		if (ret != 0)
			objB.onContactBegin(objA, ret);
	}
	
	void processEnd() {
		if (objA.isDestroyed() || objB.isDestroyed())
			return;
		
		int ret = objA.onContactEnd(objB, 0);
		if (ret != 0)
			objB.onContactEnd(objA, ret);
	}
	
	private static GameObject toGameObject(Fixture pFixture) {
		Object obj = pFixture.getBody().getUserData();
		if (obj instanceof GameObject)
			return (GameObject) obj;
		
		return null;
	}
	
	static ContactPair create(Contact pContact) {
		GameObject a = toGameObject(pContact.getFixtureA());
		GameObject b = toGameObject(pContact.getFixtureB());
		if (a != null && b != null)
			return new ContactPair(a, b);
		
		return null;
	}
}