package com.doubleshoot.game;

import java.io.IOException;

import org.andengine.engine.Engine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.game.ScreenShot.IScreenSavedListener;
import com.doubleshoot.image.ImageProcesser;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;
import com.doubleshoot.shooter.TagManager;

// Dead
final class ScreenCapture implements IBehavior {
	private ScreenShot mLeftShot;
	private ScreenShot mRightShot;
	private Bitmap mLeftBmp;
	private Bitmap mRightBmp;
	private Engine mEngine;
	private IScreenSavedListener mListener;
	private AssetManager mAssetManager;

	ScreenCapture(Engine pEngine, AssetManager pManager, IScreenSavedListener pListener) {
		assert(pEngine != null && pListener != null);
		mEngine = pEngine;
		mListener = pListener;
		mAssetManager = pManager;
	}

	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (host.hasTag(TagManager.sLeftHero)) {
			mLeftShot = new ScreenShot(mEngine);
			mLeftShot.captureRequest();
			mEngine.registerDrawHandler(mLeftShot);
		} else {
			mRightShot = new ScreenShot(mEngine);
			mRightShot.captureRequest();
			mEngine.registerDrawHandler(mRightShot);
		}
	}
	
	public void save() {
		mLeftShot.saveRequest(new IScreenSavedListener() {

			@Override
			public void onSaved(Bitmap pBitmap) {
				mLeftBmp = pBitmap;
				if (mRightBmp != null)
					notifySaved();
			}
		});
		mRightShot.saveRequest(new IScreenSavedListener() {
			
			@Override
			public void onSaved(Bitmap pBitmap) {
				mRightBmp = pBitmap;
				if (mLeftBmp != null)
					notifySaved();
			}
		});
	}
	
	private void notifySaved() {
		try {
			Bitmap center = BitmapFactory.decodeStream(
					mAssetManager.open("gfx/vs.png"));
			Bitmap merged = ImageProcesser.combine(mLeftBmp, center, mRightBmp);
			mListener.onSaved(ImageProcesser.invert(merged));
			mLeftBmp = null;
			mRightBmp = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}