package com.doubleshoot.shape;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Log;

public class TexturedSpriteFactory implements ShapeFactory {
	private Set<IAreaShape> mFreeList = new HashSet<IAreaShape>();
	private ITextureRegion mTextureRegion;
	private VertexBufferObjectManager mVBOManager;
	private Color mAddedColor;
	private float mScale;
	private int mBlendSource = IShape.BLENDFUNCTION_SOURCE_DEFAULT;
	private int mBlendDest = IShape.BLENDFUNCTION_DESTINATION_DEFAULT;
	
	private int mProfileNewCount = 0;
	public String textureName = "";
	
	public void setBlendFunction(int pBlendSource, int pBlendDest) {
		mBlendSource = pBlendSource;
		mBlendDest = pBlendDest;
	}
	
	public TexturedSpriteFactory(VertexBufferObjectManager vbom, ITextureRegion pTextureRegion, Color added, float pScale) {
		mVBOManager = vbom;
		mTextureRegion = pTextureRegion;
		mAddedColor = added;
		mScale = pScale;
	}
	
	public TexturedSpriteFactory(VertexBufferObjectManager vbom, ITextureRegion pTextureRegion) {
		this(vbom, pTextureRegion, Color.WHITE, 1.f);
	}
	
	public TexturedSpriteFactory(VertexBufferObjectManager vbom, ITextureRegion pTextureRegion, Color pColor) {
		this(vbom, pTextureRegion, pColor, 1.f);
	}
	
	public TexturedSpriteFactory(VertexBufferObjectManager vbom, ITextureRegion pTextureRegion, float scale) {
		this(vbom, pTextureRegion, Color.WHITE, scale);
	}
	
	private void onShapeRecycled(IAreaShape shape) {
		mFreeList.add(shape);
		shape.reset();
		shape.clearUpdateHandlers();
	}
	
	protected IAreaShape newShape() {
		if (mTextureRegion instanceof ITiledTextureRegion) {
			return new AnimatedSprite(0, 0, (ITiledTextureRegion) mTextureRegion, mVBOManager) {
				@Override
				public void onDetached() {
					onShapeRecycled(this);
				}
			};
		}
		
		return new Sprite(0, 0, mTextureRegion, mVBOManager) {
				@Override
				public void onDetached() {
					onShapeRecycled(this);
				}
			};
	}
	
	private IAreaShape findShape() {
		IAreaShape shape;
		if (mFreeList.size() > 0) {
			Iterator<IAreaShape> it = mFreeList.iterator();
			shape = it.next();
			it.remove();
		} else {
			shape = newShape();
			++mProfileNewCount;
			
			if (mProfileNewCount % 10 == 1) {
				Log.i("TextureSpriteFactory",
						"New shape<" + textureName + "> count: " + mProfileNewCount);
			}
		}
		
		return shape;
	}
	
	@Override
	public IAreaShape createShape() {
		IAreaShape shape = findShape();
		shape.setColor(mAddedColor);
		shape.setScale(mScale);
		shape.setBlendFunction(mBlendSource, mBlendDest);
		return shape;
	}


}
