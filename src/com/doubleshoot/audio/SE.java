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

	public Sound put(String pPath, float pVolume) throws IOException {
		return put(pPath.substring(0, pPath.indexOf(".")), pPath, pVolume);
	}
	
	public Sound put(String name, String pPath, float pVolume) throws IOException {
		Sound sound = SoundFactory.createSoundFromAsset(
				mContext.getSoundManager(), mContext, mBase + pPath);
		sound.setVolume(pVolume);
		mSounds.put(name, sound);
		return sound;
	}

	public Sound get(String name) {
		return mSounds.get(name);
	}
}