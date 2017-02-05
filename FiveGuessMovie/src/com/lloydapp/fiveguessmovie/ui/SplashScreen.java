package com.lloydapp.fiveguessmovie.ui;



import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.Utils.UpdateUtil;
import com.lloydapp.fiveguessmovie.model.UpdateInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	private int mCurrentVersion ;
	private int mNewVersion;
	

	public static final int NEED_TO_UPDATE 	   = 0;
	public static final int NOT_NEED_TO_UPDATE = 1;
	
	 @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.launch_layout);
	        mCurrentVersion = UpdateUtil.getVerCode(SplashScreen.this);
	        Log.d("#####", "old version is "+mCurrentVersion);
	       new Thread(){
	    	   public void run(){
	    		   String response = UpdateUtil.getUpdateInfoFromService();
	    		   UpdateUtil.parseXMLWithPull(response);
	    		   mNewVersion = UpdateUtil.getVerCodeFromSer();
	    		   Log.d("####", "version of update"+mNewVersion);
	    		   Message msg = new Message();
	    		   
	    		   if(mNewVersion > mCurrentVersion ){
	    			   msg.what = NEED_TO_UPDATE;
	    			   msg.obj = UpdateUtil.getApkUrl();
	    			   Log.d("#####", "url is "+(String)msg.obj );
	    		   }else{
	    			   msg.what = NOT_NEED_TO_UPDATE;
	    		   }
	    		   handler.sendMessage(msg);
	    	   }
	       }.start();
	    }
	
	 private Handler handler = new Handler(){
		 public void handleMessage(Message msg){
			 switch(msg.what){
			 case NEED_TO_UPDATE:
				 Log.d("####", "this is need");
				 Intent i = new Intent(SplashScreen.this,UpdateActivity.class);
				 i.putExtra("apk_url", (String)msg.obj);
				 startActivity(i);
				 finish();
				 break;
			 case NOT_NEED_TO_UPDATE:
				 Log.d("####", "this is not need");
			        new Handler().postDelayed(new Runnable() {
			            public void run() {
			                /* Create an Intent that will start the Main WordPress Activity. */
			                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
			                SplashScreen.this.startActivity(mainIntent);
			                SplashScreen.this.finish();
			            }
			        }, 2900); //2900 for release
				 break;
			 }
		 }
	 };
}
