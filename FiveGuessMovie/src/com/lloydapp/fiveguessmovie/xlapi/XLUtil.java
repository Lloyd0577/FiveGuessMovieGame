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
	 * ΢�����ַ���
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
	    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//��ʼ��΢���ķ�����Ϣ
	    weiboMessage.mediaObject = imageObject;

	    String passPath = Const.SD_CARD_IMAGE_PATH+"/"+mCurrentMovie.getSongName()+".png";
	    String passAllPath = Const.SD_CARD_IMAGE_PATH+Const.PIC_OF_PASS_ALL;
	    
	    if(path.equals(passPath)){
	    	TextObject passText = new TextObject();
			passText.text =  "�������桰����µ�Ӱ����"
					+ "�Ѿ�����"+(mCurrentStage+1)+"����Ŷ���Ͽ���һ����ɣ�";
	    	weiboMessage.textObject = passText;
	    }else if(path.equals(passAllPath)){
	    	TextObject passAllText = new TextObject();
			passAllText.text = "�������桰����µ�Ӱ����"
					+ "�Ѿ����ȫ���ؿ���Ŷ���Ͽ���һ����ɣ�";
			weiboMessage.textObject = passAllText;
	    }
	    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
	    request.transaction = String.valueOf(System.currentTimeMillis());
	    request.multiMessage = weiboMessage;
	    Const.WBAPI.sendRequest((Activity)context,request); //����������Ϣ��΢��������΢��������� 
	  
	   
	}
}
