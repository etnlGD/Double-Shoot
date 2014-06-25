package com.doubleshoot.hud;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;

public interface ITextCreator {
	public Text create(float x, float y, String text);
	public IFont getFont();
	public void setFont(IFont font);
}