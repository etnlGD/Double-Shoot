package com.doubleshoot.hud;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.Scene;

import com.doubleshoot.score.IScoreChangeListener;

public class GameScore implements IScoreChangeListener, HudItem {
	private static final float[] DURATION_TABLE = {
		0.2f, 0.3f, 0.4f, 0.5f
	};
	
	private final float mMaxWidth;
	private List<DigitBit> mBits;
	private int mCurrentScore;
	private Scene mScene;
	private ITextCreator mFontCreator;
	private float mX, mY;
	
	public GameScore(float pMaxLetterWidth, int maxBit, ITextCreator pFontCreator) {
		mFontCreator = pFontCreator;
		mBits = new ArrayList<DigitBit>();
		mMaxWidth = pMaxLetterWidth;
		
		createDigigBits(maxBit);
	}

	@Override
	public void onScoreChanged(int current, int pDelta) {
		pDelta = current - mCurrentScore;
		if (pDelta != 0) {
			final int next = add(mCurrentScore, pDelta);
			
			ensureBitsSize(next);
			validateEachBit(next, mCurrentScore);
			
			mCurrentScore = next;
		}
	}
	
	private int add(int pX1, int pX2) {
		int next = pX1 + pX2;
		
		if (next < 0) {
			if (Integer.MAX_VALUE - pX1 < pX2) {
				next = Integer.MAX_VALUE;
			} else {
				next = 0;
			}
		}
		
		return next;
	}

	private void validateEachBit(int pNextScore, int pCurrentScore) {
		boolean isAdd = pNextScore > pCurrentScore;
		if (isAdd) {
			addOnEachBit(pNextScore);
		} else {
			subOnEachBit(pNextScore);
		}
	}

	private void addOnEachBit(int pNextScore) {
		for (DigitBit bit : mBits) {
			bit.addTo(pNextScore % 10);
			pNextScore /= 10;
		}
	}

	private void subOnEachBit(int pNextScore) {
		for (DigitBit bit : mBits) {
			bit.subTo(pNextScore % 10);
			pNextScore /= 10;
		}
	}

	private void ensureBitsSize(int pNextValue) {
		int bitCount = 1;
		if (pNextValue >= 10) { // avoid: log10(0);
			bitCount = (int) Math.log10(pNextValue) + 1;
		}
		
		if (bitCount > mBits.size()) {
			createDigigBits(bitCount - mBits.size());
		}
	}

	private void createDigigBits(int pCount) {
		int size = mBits.size();
		for (int i = size; i < pCount + size; i++) {
			int tableIndex = (i >= DURATION_TABLE.length) ? DURATION_TABLE.length-1 : i;
			float duration = DURATION_TABLE[tableIndex];
			DigitBit bit = new DigitBit(0, duration, mFontCreator);
			
			mBits.add(bit);
			
			if (mScene != null) {
				attachBit(i);
			}
		}
	}

	private float getAlignedX(float x, int index) {
		return x - mMaxWidth * (index + 1);
	}
	
	@Override
	public void attachToScene(float pX, float pY, Scene pScene) {
		mScene = pScene;
		mX = pX;
		mY = pY;
		
		int size = mBits.size();
		for (int i = 0; i < size; i++) {
			attachBit(i);
		}
	}
	
	private void attachBit(int pIndex) {
		DigitBit bit = mBits.get(pIndex);
		bit.attachToScene(getAlignedX(mX, pIndex), mY, mScene);
	}

	@Override
	public void detach() {
		mScene = null;
		for (DigitBit bit : mBits) {
			bit.detach();
		}
	}
}