package com.lloydapp.fiveguessmovie.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.data.Const;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class UpdateActivity extends Activity{

	private ProgressDialog mDownloadDialog;
	private int progress;
	public static final int SHOW_PROGREE = 0;
	public static final int DONE = 1;
	public static final int CANSEL_DOWN = 2;
	public static final int ERROR_DOWN = 3;
	private boolean mIsCancelDown = false;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_layout);
		
		final String url = getIntent().getStringExtra("apk_url");
	
		//更新对话框
		Dialog dialog = new AlertDialog.Builder
				(UpdateActivity.this)
				.setTitle("软件更新")
				.setMessage("发现新版本是否立即更新？")
				.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//显示下载对话框
						showDownloadDialog();
						//下载apk
						downloadApk(url);
						
					}
				}).setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent i = 
								new Intent(UpdateActivity.this,MainActivity.class);
						startActivity(i);
						finish();
					}
				}).setCancelable(false)
				.create();
		
		dialog.show();
		
	}
	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog(){
		mDownloadDialog = new ProgressDialog(UpdateActivity.this);
		mDownloadDialog.setTitle("正在下载");
		mDownloadDialog.setMessage("请稍后...");
		mDownloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDownloadDialog.setButton(DialogInterface.BUTTON_NEUTRAL, 
				"取消下载", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mIsCancelDown = true;
					}
			
		});
		mDownloadDialog.setCancelable(false);
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_PROGREE:
				mDownloadDialog.setProgress(progress);
				break;
			case DONE:
				mDownloadDialog.cancel();
				update();
				break;
			case CANSEL_DOWN:
				mDownloadDialog.cancel();
				Intent i =  new Intent(UpdateActivity.this,MainActivity.class);
				startActivity(i);
				finish();
				break;
			case ERROR_DOWN:
				mDownloadDialog.cancel();
				Toast.makeText(UpdateActivity.this,
						"下载失败", Toast.LENGTH_SHORT).show();
				Intent intent =  new Intent(UpdateActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}
	};
	/**
	 * 安装apk
	 */
	private void update(){
			Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(new File(Const.SD_CARD_APK_PATH+"/fiveguessmovie.apk")),
	                "application/vnd.android.package-archive");
	        startActivity(intent);
	}
	/**
	 * 下载APK文件
	 * @param path
	 */
	private void downloadApk(final String path){
		new Thread(){
			public void run(){
				FileOutputStream fileOutputStream = null;
				boolean result = false;
				boolean sdCardExist = Environment.getExternalStorageState()
			  			  .equals(android.os.Environment.MEDIA_MOUNTED);
				if(!sdCardExist){
					Toast.makeText(UpdateActivity.this,
							"下载失败请先插入SD卡", Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					Log.d("#####", "path is "+path);
					URL url = new URL(path);
					HttpURLConnection connection = 
							(HttpURLConnection)url.openConnection();
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setRequestMethod("GET");
					InputStream is = connection.getInputStream();
					int length = connection.getContentLength();
					Log.d("####", "length is "+length);
					File file = new File(Const.SD_CARD_APK_PATH);
					if(!file.exists()){
						file.mkdirs();
					}
					fileOutputStream = new FileOutputStream(Const.SD_CARD_APK_PATH+"/fiveguessmovie.apk");
					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while((ch = is.read(buf)) != -1){
						if(!mIsCancelDown){
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							progress = (int)(((float)count/length)*100);
							Message msg = new Message();
							msg.what = SHOW_PROGREE;
							handler.sendMessage(msg);
							result = true;
						}else{
							Message msg1 = new Message();
							msg1.what = CANSEL_DOWN;
							handler.sendMessage(msg1);
							result = false;
							return;
						}
						
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg2 = new Message();
					msg2.what = ERROR_DOWN;
					handler.sendMessage(msg2);
					result = false;
				}finally{
					if(fileOutputStream != null){
						try {
							fileOutputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(result){
						Message msg = new Message();
						msg.what = DONE;
						handler.sendMessage(msg);
					}
				}
			}
		}.start();
	}
}
