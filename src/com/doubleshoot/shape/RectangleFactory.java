package com.doubleshoot.shape;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class RectangleFactory implements ShapeFactory {
	private VertexBufferObjectManager vbom;
	private float mWidth;
	private float mHeight;
	private Color mColor;
	
	public RectangleFactory(VertexBufferObjectManager vbom,
			float width, float height, Color color) {
		this.vbom = vbom;
		mWidth = width;
		mHeight = height;
		mColor = color;
	}
	
	@Override
	public IAreaShape createShape() {
		IAreaShape shape = new Rectangle(0, 0, mWidth, mHeight, vbom);
		shape.setColor(mColor);
		return shape;
	}

}