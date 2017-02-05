package com.lloydapp.fiveguessmovie.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lloydapp.fiveguessmovie.data.Const;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ScreenShotUtils {

	
	/**
	 * ����
	 * @param pActivity
	 * @return
	 */
	public static Bitmap takeScreenShot(Activity pActivity)  
    {  
        Bitmap bitmap=null;  
        View view=pActivity.getWindow().getDecorView();  
        // �����Ƿ���Խ��л�ͼ����  
        view.setDrawingCacheEnabled(true);  
        // �����ͼ�����޷���ǿ�ƹ�����ͼ����  
        view.buildDrawingCache();  
        // �������������ͼ   
        bitmap=view.getDrawingCache();  
          
        // ��ȡ״̬���߶�  
        Rect frame=new Rect();  
        // ������Ļ��͸�  
        view.getWindowVisibleDisplayFrame(frame);  
        int stautsHeight=frame.top;  
        Log.d("jiangqq", "״̬���ĸ߶�Ϊ:"+stautsHeight);  
        DisplayMetrics dm = new DisplayMetrics();  
        pActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        
        // ������������Ҫ�Ŀ�͸ߴ���bitmap  
        bitmap=Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height-stautsHeight);  
        return bitmap;
	    
	    
	    
		
    }  
	/**
	 * ����������ͼ��SD����
	 * @param pBitmap
	 * @param fileName
	 * @return
	 */
	private static void savePic(Context context,Bitmap pBitmap,String fileName)  
    {  
      FileOutputStream fos=null;  
      
      try {  
    	boolean sdCardExist = Environment.getExternalStorageState()
    			  .equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����
    	if(sdCardExist){
      		File outFile = new File(Const.SD_CARD_IMAGE_PATH);
      		if(!outFile.exists()){
      			outFile.mkdirs();
      		}
      		String outPath = Const.SD_CARD_IMAGE_PATH+"/"+fileName;
      		File file = new File(outPath);
      		if(!file.exists()){
      		   fos = new FileOutputStream(outPath);
      	        if(null!=fos)  
      	        {  
      	            pBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);  
      	            fos.flush();  
      	            fos.close();  
      	           
      	        }  
      		}
    	}else{
    		Toast.makeText(context, "�����SD��", Toast.LENGTH_SHORT).show();
    	}
    	
     
    } catch (FileNotFoundException e) {  
        e.printStackTrace();  
    }catch (IOException e) {  
        e.printStackTrace();  
        Toast.makeText(context, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
    }  
     
    }   
	
	/**
	 * ���������浽SD����
	 * @param pActivity
	 * @param fileName
	 * @return
	 */
	public static void shotBitmap(Activity pActivity,String fileName)  
    {  
       ScreenShotUtils.savePic(pActivity,takeScreenShot(pActivity),fileName );  
    }  
}
