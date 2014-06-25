package com.doubleshoot.parallax;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.util.GLState;

// BAD fix
public class HVParallaxEntity extends ParallaxEntity {
	private float mParallaxFactorX;
	private float mParallaxFactorY;
	private IAreaShape mAreaShape;
	
	public HVParallaxEntity(float pParallaxFactorX, float pParallaxFactorY, IAreaShape pAreaShape) {
		super(pParallaxFactorX, pAreaShape);
		mParallaxFactorX = pParallaxFactorX;
		mParallaxFactorY = pParallaxFactorY;
		mAreaShape = pAreaShape;
		mAreaShape.setPosition(0, 0);
		mAreaShape.setScaleCenter(0, 0);
	}
	
	@Override
	public void onDraw(GLState pGLState, Camera pCamera, float pParallaxValue) {
		pGLState.pushModelViewGLMatrix();
		{
			float cameraWidth = pCamera.getWidth();
			float shapeWidthScaled = mAreaShape.getWidthScaled();
			float baseOffsetX = (pParallaxValue * mParallaxFactorX) % shapeWidthScaled;
			baseOffsetX -= Math.ceil(baseOffsetX / shapeWidthScaled) * shapeWidthScaled;
			
			float cameraHeight = pCamera.getHeight();
			float shapeHeightScaled= mAreaShape.getHeightScaled();
			float baseOffsetY = (pParallaxValue * mParallaxFactorY) % shapeHeightScaled;
			baseOffsetY -= Math.ceil(baseOffsetY / shapeWidthScaled) * shapeHeightScaled;

			pGLState.translateModelViewGLMatrixf(baseOffsetX, baseOffsetY, 0);
			
			float currentMaxX = baseOffsetX, currentMaxY = baseOffsetY;
			while(currentMaxX < cameraWidth) {
				while (currentMaxY < cameraHeight) {
					mAreaShape.onDraw(pGLState, pCamera);
					pGLState.translateModelViewGLMatrixf(0, shapeHeightScaled, 0);
					currentMaxY += shapeHeightScaled;
				}
				
				pGLState.translateModelViewGLMatrixf(shapeWidthScaled, baseOffsetY-currentMaxX, 0);
				currentMaxX += shapeWidthScaled;
			}
		}
		pGLState.popModelViewGLMatrix();
	}
}
