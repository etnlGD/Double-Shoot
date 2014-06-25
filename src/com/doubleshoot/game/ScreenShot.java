package com.doubleshoot.game;


import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IDrawHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;

import android.graphics.Bitmap;

import com.doubleshoot.hud.GameStatusScene;

public class ScreenShot implements IDrawHandler {
	private Engine mEngine;	
	private RenderTexture mTextureTarget;

	private boolean mCapturePending;
	private boolean mPicSavePending;
	
	private Bitmap mBitmap;
	private IScreenSavedListener mListener;
	
	public ScreenShot(Engine pEngine) {
		mEngine = pEngine;
		mTextureTarget = new RenderTexture(mEngine.getTextureManager(), 
				(int) GameActivity.CAMERA_WIDTH, (int) GameActivity.CAMERA_HEIGHT);
	}
	
	public void captureRequest() {
		mCapturePending = true;
	}
	
	public void saveRequest(IScreenSavedListener pListener) {
		mPicSavePending = true;
		mListener = pListener;
	}

	@Override
	public void onDraw(GLState pGLState, Camera pCamera) {
		if (mCapturePending) {
			if (!mTextureTarget.isInitialized())
				mTextureTarget.init(pGLState);
			
			mTextureTarget.begin(pGLState);
			onDrawScene(pGLState, pCamera);
			mTextureTarget.end(pGLState);
			
			mCapturePending = false;
		}
		
		if (shouldSaveImage()) {
			mBitmap = mTextureTarget.getBitmap(pGLState);
			mTextureTarget.destroy(pGLState);
		}
		
		if (mPicSavePending) {
			mPicSavePending = false;
			notifyScreeenSaved();
		}
	}
	
	private boolean shouldSaveImage() {
		return mPicSavePending && mBitmap == null; 
	}
 	
	private void onDrawScene(GLState pGLState, Camera pCamera) {
		Scene scene = mEngine.getScene();
		if (scene != null) {
			scene.onDraw(pGLState, pCamera);
		}
		
		HUD hud = pCamera.getHUD();
		if (hud instanceof GameStatusScene) {
			GameStatusScene gsScene = (GameStatusScene) hud;
			gsScene.beginDraw();
			gsScene.onDraw(pGLState, pCamera);
			gsScene.endDraw();
		}
	}

	private void notifyScreeenSaved() {
		if (mListener != null)
			mListener.onSaved(mBitmap);
	}

	public static interface IScreenSavedListener {
		public void onSaved(Bitmap pBitmap);
	}

}
