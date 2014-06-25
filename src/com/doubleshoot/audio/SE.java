package com.doubleshoot.audio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.ui.activity.BaseGameActivity;

public class SE {
	private BaseGameActivity mContext;
	private String mBase;
	private Map<String, Sound> mSounds;

	public SE(BaseGameActivity pContext, String pBase) {
		mContext = pContext;
		mBase = pBase;
		mSounds = new HashMap<String, Sound>();
	}

	public Sound load(String pPath) throws IOException {
		Sound sound = SoundFactory.createSoundFromAsset(
				mContext.getSoundManager(), mContext, mBase + pPath);
		return sound;
	}

	public Sound get(String pPath) {
		return mSounds.get(pPath);
	}
}