package com.lloydapp.fiveguessmovie.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.data.Const;
import com.lloydapp.fiveguessmovie.model.Movie;
import com.lloydapp.fiveguessmovie.ui.MainActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WXUtil {

	
	/**
	 * 分享图片
	 */
	public static void weiXinPicShare(IWXAPI api,Context context,String fileName,boolean flag){
	
		WXImageObject imgObj = new WXImageObject();
		String path  = Environment.getExternalStorageDirectory().getAbsolutePath()
				+"/fiveguessmovie/image/"+fileName;
		File file = new File(path);
		if(!file.exists()){
			Toast.makeText(context,
					"文件不存在", Toast.LENGTH_SHORT).show();
		}
		imgObj.setImagePath(path);
	
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		msg.description="我正在玩“五秒猜电影”海量电影等你猜，赶快来玩挑战吧！";
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "image"+System.currentTimeMillis();
		req.message = msg;
		req.scene = flag ? SendMessageToWX.Req.WXSceneTimeline:
			SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
		
	}
	/**
	 * 微信图文分享
	 */
	public static void weixinPicAndTextShare(Context context,boolean flag,int view,
			int mCurrentStage,Movie mCurrentMovie){
		
		Intent intent = new Intent();
		if(flag){
			intent.setComponent(new ComponentName("com.tencent.mm",
					"com.tencent.mm.ui.tools.ShareToTimeLineUI"));	
		}else{
			intent.setComponent(new ComponentName("com.tencent.mm",
					"com.tencent.mm.ui.tools.ShareImgUI"));	
		}
		
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
				Intent.FLAG_GRANT_READ_URI_PERMISSION);
		if(view != Const.VIEW_OF_PASS_ALL){
			intent.putExtra("Kdescription", "我正在玩“五秒猜电影”，"
				+ "已经到第"+(mCurrentStage+1)+"关了哦！赶快来一起玩吧！");
		}else{
			intent.putExtra("Kdescription", "我正在玩“五秒猜电影”，"
					+ "已经通关了哦！赶快来一起玩吧！");
		}
		
		Uri uri = null;
		switch(view){
		
		case Const.VIEW_OF_PASS:
			uri = Uri.fromFile(new File(Environment.
					getExternalStorageDirectory()+"/fiveguessmovie/image",
					mCurrentMovie.getSongName()+".png"));
			break;
		case Const.VIEW_OF_PASS_ALL:
			uri = Uri.fromFile(new File(Environment.
					getExternalStorageDirectory()+"/fiveguessmovie/image",
					"passall.png"));
			break;
		}
	
		
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		context.startActivity(intent);
	
	}
	
	/**
	 * 判断是否安装微信
	 * @param context
	 * @return
	 */
	public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
	
	/**
	 * 微信分享
	 * @param api
	 * @param shareLin
	 * @param context
	 * @param flag
	 * @param fileName
	 * @param view
	 * @param mCurrentStage
	 * @param mCurrentMovie
	 */
	public static void handleWXShare( IWXAPI api,LinearLayout shareLin,Context context,
			boolean flag,String fileName,int view,int mCurrentStage,Movie mCurrentMovie){
	 	
		
			shareLin.setVisibility(View.INVISIBLE);
			File f = new File(Environment.
					getExternalStorageDirectory()+"/fiveguessmovie/image",
					fileName);
			if(!f.exists()){
				if(fileName.equals("share_pic.png")){
					//将要分享的图片拷贝到SD卡中
					try {
						Utils.copyBigDataToSD(context, "share_pic.png", Const.FILE_OF_PIC);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//屏幕截图
					ScreenShotUtils.shotBitmap((Activity)context,
						fileName);
				}
				
			}
		
			if(WXUtil.isWeixinAvilible(context)){
				switch(view){
				case Const.VIEW_OF_GAME:
					weiXinPicShare(api,context,fileName,flag);
					break;
				case Const.VIEW_OF_PASS:
					WXUtil.weixinPicAndTextShare(context,flag,Const.VIEW_OF_PASS,
					mCurrentStage,mCurrentMovie);
					break;
				case Const.VIEW_OF_PASS_ALL:
					WXUtil.weixinPicAndTextShare(context,flag,Const.VIEW_OF_PASS_ALL,
							mCurrentStage,mCurrentMovie);
				}
				
				
			}else{
				Toast.makeText(context, "微信未安装，无法进行微信分享", 
						Toast.LENGTH_SHORT).show();
			}
		
		
	}

}
