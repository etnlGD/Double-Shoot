package com.doubleshoot.hud;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.opengl.GLES20;

public class DigitBit implements IModifierListener<IEntity>, HudItem {
	private static final int TOP_INDEX = 0;
	private static final int CENTER_INDEX = TOP_INDEX + 1;
	private static final int BOTTOM_INDEX = CENTER_INDEX + 1;
	private static final int SIZE = BOTTOM_INDEX + 1;

	private final float mHeight;

	private Text[] mTexts;
	private IEntityModifier mCenter2TopModifier;
	private IEntityModifier mCenter2BottomModifier;
	private IEntityModifier mBottom2CenterModifier;
	private IEntityModifier mTop2CenterModifier;
	private int mCurrentValue;
	private int mTargetValue;
	private boolean mSlideUp;
	private boolean mAnimating;
	private final float mDuration;
	private ITextCreator mFontCreator;

	public DigitBit(float pVerticalGap, float pDuration, ITextCreator pFontCreator) {
		this(pVerticalGap, 0, pDuration, pFontCreator);
	}

	public DigitBit(float pVerticalGap, int pInitValue, float pDuration, ITextCreator pFontCreator) {
		mFontCreator = pFontCreator;
		mDuration = pDuration;
		mTexts = new Text[SIZE];

		validateValue(pInitValue);

		Text curr = mFontCreator.create(0, 0, String.valueOf(mCurrentValue));
		mHeight = curr.getHeight() + pVerticalGap;
		initTexts(curr);
	}

	private void initTexts(Text pCurr) {
		Text prev = mFontCreator.create(0, 0, String.valueOf(prev()));
		Text next = mFontCreator.create(0, 0, String.valueOf(next()));
		
		mTexts[CENTER_INDEX] = pCurr;
		mTexts[TOP_INDEX] = prev;
		mTexts[BOTTOM_INDEX] = next;
		
		for (Text t : mTexts) {
			t.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}
		prev.setAlpha(0);
		next.setAlpha(0);
	}
	
	/**
	 * increase the displaying digit to target smoothly.
	 * 
	 * @param pValue
	 */
	public void addTo(int pValue) {
		validateValue(pValue);

		mSlideUp = true;
		updateAnimation();
	}

	/**
	 * decrease the displaying digit to target smoothly.
	 * 
	 * @param pValue
	 */
	public void subTo(int pValue) {
		validateValue(pValue);

		mSlideUp = false;
		updateAnimation();
	}

	private void validateValue(int pValue) {
		assert pValue >= 0 && pValue <= 9;
		mTargetValue = pValue;
	}

	private int prev() {
		return (mCurrentValue + 10 - 1) % 10;
	}

	private int next() {
		return (mCurrentValue + 1) % 10;
	}

	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
	}

	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
		if (pModifier == mTop2CenterModifier) {
			resetAnimations(mTop2CenterModifier, mCenter2BottomModifier);
			mTexts[TOP_INDEX].unregisterEntityModifier(mTop2CenterModifier);
			swap(BOTTOM_INDEX, CENTER_INDEX, TOP_INDEX);

			mCurrentValue = prev();
			mTexts[TOP_INDEX].setText(String.valueOf(prev()));
		} else if (pModifier == mBottom2CenterModifier) {
			resetAnimations(mBottom2CenterModifier, mCenter2TopModifier);
			mTexts[BOTTOM_INDEX].unregisterEntityModifier(mBottom2CenterModifier);
			swap(TOP_INDEX, CENTER_INDEX, BOTTOM_INDEX);

			mCurrentValue = next();
			mTexts[BOTTOM_INDEX].setText(String.valueOf(next()));
		} else { // do nothing.
			return ;
		}

		mAnimating = false;
		updateAnimation();
	}

	/**
	 * a = b, b = c, c = a.
	 * @param aIndex
	 * @param bIndex
	 * @param cIndex
	 */
	private void swap(int aIndex, int bIndex, int cIndex) {
		Text buffer = mTexts[aIndex];
		mTexts[aIndex] = mTexts[bIndex];
		mTexts[bIndex] = mTexts[cIndex];
		mTexts[cIndex] = buffer;
	}

	private void resetAnimations(IEntityModifier pToCenterModifier, IEntityModifier pCenterToOtherModifier) {
		pToCenterModifier.reset();
		pCenterToOtherModifier.reset();
		mTexts[CENTER_INDEX].unregisterEntityModifier(pCenterToOtherModifier);
	}

	private void updateAnimation() {
		if (mCurrentValue == mTargetValue) {
			return; // there is no need to animate.
		}
		
		if (mAnimating) {
			return; // wait for next animate cycle, then execute this command.
		}
		
		if (mSlideUp) {
			mTexts[CENTER_INDEX].registerEntityModifier(mCenter2TopModifier);
			mTexts[BOTTOM_INDEX].registerEntityModifier(mBottom2CenterModifier);
		} else {
			mTexts[CENTER_INDEX].registerEntityModifier(mCenter2BottomModifier);
			mTexts[TOP_INDEX].registerEntityModifier(mTop2CenterModifier);
		}
		
		mAnimating = true;
	}

	@Override
	public void attachToScene(float pX, float pY, Scene pScene) {
		mTexts[CENTER_INDEX].setPosition(pX, pY);
		mTexts[TOP_INDEX].setPosition(pX, pY-mHeight);
		mTexts[BOTTOM_INDEX].setPosition(pX, pY+mHeight);
		
		initModifiers(pX, pY);
		
		for (Text t : mTexts) {
			pScene.attachChild(t);
		}
	}
	
	private void initModifiers(float pX, float pY) {
		IEntityModifier smaller = new ScaleModifier(mDuration, 1, 0.5f);
		IEntityModifier bigger = new ScaleModifier(mDuration, 0.5f, 1);
		IEntityModifier disappear = new AlphaModifier(mDuration, 1, 0);
		IEntityModifier appear = new AlphaModifier(mDuration, 0, 1);
		IEntityModifier ceter2Top = new MoveModifier(mDuration, pX, pX, pY, pY - mHeight);
		IEntityModifier center2Bottom = new MoveModifier(mDuration, pX, pX, pY, pY + mHeight);
		IEntityModifier top2Center = new MoveModifier(mDuration, pX, pX, pY - mHeight, pY);
		IEntityModifier bottom2Center = new MoveModifier(mDuration, pX, pX, pY + mHeight, pY);

		mCenter2TopModifier = new ParallelEntityModifier(ceter2Top, smaller, disappear);
		mCenter2BottomModifier = new ParallelEntityModifier(center2Bottom, smaller.deepCopy(), disappear.deepCopy());
		mBottom2CenterModifier = new ParallelEntityModifier(bottom2Center, bigger, appear);
		mTop2CenterModifier = new ParallelEntityModifier(top2Center, bigger.deepCopy(), appear.deepCopy());

		mBottom2CenterModifier.addModifierListener(this);
		mTop2CenterModifier.addModifierListener(this);
	}

	@Override
	public void detach() {
		for (Text t : mTexts) {
			t.detachSelf();
		}
	}
}