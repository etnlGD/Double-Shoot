package com.doubleshoot.hud;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class DefaultFontCreator implements ITextCreator {
	private VertexBufferObjectManager vbom;
	private IFont mFont;
	
	public DefaultFontCreator(VertexBufferObjectManager vbom, IFont pFont) {
		this.vbom = vbom;
		mFont = pFont;
	}
	
	@Override
	public Text create(float x, float y, String text) {
		return new Text(x, y, mFont, text, vbom);
	}

	@Override
	public IFont getFont() {
		return mFont;
	}

	@Override
	public void setFont(IFont font) {
		mFont = font;
	}

}
