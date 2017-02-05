package com.lloydapp.fiveguessmovie.xlapi;

import com.lloydapp.fiveguessmovie.data.Const;
import com.lloydapp.fiveguessmovie.model.Movie;
import com.lloydapp.fiveguessmovie.ui.MainActivity;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;

import android.app.Activity;
import android.content.Context;

public class XLUtil {
	/**
	 * 微博文字分享
	 * @param hasText
	 * @param hasImage
	 * @param hasWebpage
	 * @param hasMusic
	 * @param hasVideo
	 * @param hasVoice
	 */
	public static void sendMultiMessage(Context context,String path,int mCurrentStage,Movie mCurrentMovie) {
		
		
		ImageObject imageObject = new ImageObject();
		imageObject.imagePath= path;
	    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
	    weiboMessage.mediaObject = imageObject;

	    String passPath = Const.SD_CARD_IMAGE_PATH+"/"+mCurrentMovie.getSongName()+".png";
	    String passAllPath = Const.SD_CARD_IMAGE_PATH+Const.PIC_OF_PASS_ALL;
	    
	    if(path.equals(passPath)){
	    	TextObject passText = new TextObject();
			passText.text =  "我正在玩“五秒猜电影”，"
					+ "已经到第"+(mCurrentStage+1)+"关了哦！赶快来一起玩吧！";
	    	weiboMessage.textObject = passText;
	    }else if(path.equals(passAllPath)){
	    	TextObject passAllText = new TextObject();
			passAllText.text = "我正在玩“五秒猜电影”，"
					+ "已经完成全部关卡了哦！赶快来一起玩吧！";
			weiboMessage.textObject = passAllText;
	    }
	    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
	    request.transaction = String.valueOf(System.currentTimeMillis());
	    request.multiMessage = weiboMessage;
	    Const.WBAPI.sendRequest((Activity)context,request); //发送请求消息到微博，唤起微博分享界面 
	  
	   
	}
}
