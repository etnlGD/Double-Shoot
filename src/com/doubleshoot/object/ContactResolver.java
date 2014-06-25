package com.doubleshoot.object;

import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactResolver implements ContactListener, IUpdateHandler {
	private List<ContactPair> mBeginPairs = new LinkedList<ContactPair>();
	private List<ContactPair> mEndPairs = new LinkedList<ContactPair>();
	
	@Override
	public void beginContact(Contact contact) {
		ContactPair pair = ContactPair.create(contact);
		if (pair != null)
			mBeginPairs.add(pair);
	}

	@Override
	public void endContact(Contact contact) {
		ContactPair pair = ContactPair.create(contact);
		if (pair != null)
			mEndPairs.add(pair);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (!mBeginPairs.isEmpty()) {
			for (ContactPair pair : mBeginPairs) 
				pair.processBegin();

			mBeginPairs.clear();
		}
		
		if (!mEndPairs.isEmpty()) {
			for (ContactPair pair : mEndPairs)
				pair.processEnd();
			
			mEndPairs.clear();
		}
	}

	@Override
	public void reset() {
		mBeginPairs.clear();
		mEndPairs.clear();
	}

}