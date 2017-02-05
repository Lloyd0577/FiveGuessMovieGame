package com.lloydapp.fiveguessmovie.Utils;



import java.io.IOException;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.ui.MainActivity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.view.SurfaceView;

/**
 * “Ù¿÷≤•∑≈¿‡
 * @author Administrator
 *
 */
public class MyPlayer {
	
	public static final String[] TONES = {"enter.mp3","cancel.mp3","coin.mp3","cheer.mp3"};
	public static MediaPlayer[] mTonePlayer = new MediaPlayer[TONES.length];

	public static MediaPlayer mMediaPlayer;
	
	
	public static void playTone(Context context,int index){
		if(mTonePlayer[index] == null){
			mTonePlayer[index] = new MediaPlayer();
		}
		AssetManager am = context.getAssets();
		AssetFileDescriptor afd = null;
		try {
			afd = am.openFd(TONES[index]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			mTonePlayer[index].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mTonePlayer[index].prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTonePlayer[index].start();
	}
	
}
