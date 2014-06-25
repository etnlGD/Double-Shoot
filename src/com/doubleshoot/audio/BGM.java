package com.doubleshoot.audio;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.ui.activity.BaseGameActivity;

public class BGM {
	private BaseGameActivity mContext;
	private String mBase;
	
	public BGM(BaseGameActivity pContext, String pBase) {
		mContext = pContext;
		mBase = pBase;
	}
	
	public Music load(String pPath) throws IOException {
		Music music = MusicFactory.createMusicFromAsset(
				mContext.getMusicManager(), mContext, mBase + pPath);
		music.setLooping(true);
		return music;
	}
}