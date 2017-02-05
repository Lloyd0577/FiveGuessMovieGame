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
	 * 截屏
	 * @param pActivity
	 * @return
	 */
	public static Bitmap takeScreenShot(Activity pActivity)  
    {  
        Bitmap bitmap=null;  
        View view=pActivity.getWindow().getDecorView();  
        // 设置是否可以进行绘图缓存  
        view.setDrawingCacheEnabled(true);  
        // 如果绘图缓存无法，强制构建绘图缓存  
        view.buildDrawingCache();  
        // 返回这个缓存视图   
        bitmap=view.getDrawingCache();  
          
        // 获取状态栏高度  
        Rect frame=new Rect();  
        // 测量屏幕宽和高  
        view.getWindowVisibleDisplayFrame(frame);  
        int stautsHeight=frame.top;  
        Log.d("jiangqq", "状态栏的高度为:"+stautsHeight);  
        DisplayMetrics dm = new DisplayMetrics();  
        pActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        
        // 根据坐标点和需要的宽和高创建bitmap  
        bitmap=Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height-stautsHeight);  
        return bitmap;
	    
	    
	    
		
    }  
	/**
	 * 保存截屏后的图到SD卡中
	 * @param pBitmap
	 * @param fileName
	 * @return
	 */
	private static void savePic(Context context,Bitmap pBitmap,String fileName)  
    {  
      FileOutputStream fos=null;  
      
      try {  
    	boolean sdCardExist = Environment.getExternalStorageState()
    			  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
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
    		Toast.makeText(context, "请插入SD卡", Toast.LENGTH_SHORT).show();
    	}
    	
     
    } catch (FileNotFoundException e) {  
        e.printStackTrace();  
    }catch (IOException e) {  
        e.printStackTrace();  
        Toast.makeText(context, "操作失败！", Toast.LENGTH_SHORT).show();
    }  
     
    }   
	
	/**
	 * 截屏并保存到SD卡中
	 * @param pActivity
	 * @param fileName
	 * @return
	 */
	public static void shotBitmap(Activity pActivity,String fileName)  
    {  
       ScreenShotUtils.savePic(pActivity,takeScreenShot(pActivity),fileName );  
    }  
}
