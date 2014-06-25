package com.doubleshoot.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DurationEntityModifier;


public class VisibilityEntityModifier extends DurationEntityModifier {
	private boolean mFrom;
	private boolean mTo;
	
	public VisibilityEntityModifier(float pDuration, boolean from, boolean to, 
			IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pEntityModifierListener);
		mFrom = from;
		mTo = to;
	}

	public VisibilityEntityModifier(float pDuration, boolean from, boolean to) {
		super(pDuration);
		mFrom = from;
		mTo = to;
	}

	private VisibilityEntityModifier(VisibilityEntityModifier pOther) {
		super(pOther);
		mFrom = pOther.mFrom;
		mTo = pOther.mTo;
	}
	
	@Override
	public VisibilityEntityModifier deepCopy() {
		return new VisibilityEntityModifier(this);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed, IEntity pItem) {
		if (getSecondsElapsed() >= mDuration)
			pItem.setVisible(mTo);
	}

	@Override
	protected void onManagedInitialize(IEntity pItem) {
		pItem.setVisible(mFrom);
	}
}