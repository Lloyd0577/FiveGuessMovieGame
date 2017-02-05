package com.lloydapp.fiveguessmovie.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.data.Const;
import com.lloydapp.fiveguessmovie.model.IDialogClickListener;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 工具类
 * @author Administrator
 *
 */
public class Utils {
	
	
	
	public static final String FILE_NAME = "myData";
	public static AlertDialog mAlertDialog;
	/**
	 * 加载布局
	 * @param context
	 * @param layoutId
	 * @return
	 */
	public static View getView(Context context,int layoutId){
		LayoutInflater inflater = (LayoutInflater)context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutId,null );
		return v;
	}
	
	/**
	 * 显示自定义对话框
	 * @param context
	 * @param msg
	 * @param listener
	 */
	public static void showDialog(final Context context,String msg,
			final IDialogClickListener listener){
		 
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View v = getView(context,R.layout.dialog_layout);
		ImageButton mYesBtn = (ImageButton)v.findViewById(R.id.yes_button);
		ImageButton mNoBtn = (ImageButton)v.findViewById(R.id.no_button);
		TextView mTipText = (TextView)v.findViewById(R.id.tip_msg_text);
		
		mNoBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mAlertDialog.cancel();
				MyPlayer.playTone(context, Const.TONE_OF_CANCEL);
			}
		});
		mYesBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				listener.onClick();
				mAlertDialog.cancel();
				MyPlayer.playTone(context, Const.TONE_OF_ENTER);
			}
		});
		
		mTipText.setText(msg);
		
		mAlertDialog = builder.create();
		mAlertDialog.setView(v, 0, 0, 0, 0);
		mAlertDialog.show();
		
	}
	/**
	 * 保存游戏数据
	 * @param context
	 * @param level
	 * @param coin
	 */
	public static void saveData(Context context,int level,int coin){
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(level);
			dos.writeInt(coin);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 加载游戏数据
	 * @param context
	 * @return
	 */
	public static int[] loadData(Context context){
		int[] data = {-1,Const.TOTAL_COIN};
		FileInputStream  fis = null;
		
		try {
			fis = context.openFileInput(FILE_NAME);
			DataInputStream dis = new DataInputStream(fis);
			data[0] = dis.readInt();
			data[1] = dis.readInt();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return data;
	}
	
	/**
	 *  复制asset中的文件到SD卡
	 * @param context
	 * @param fileName
	 * @throws IOException
	 */
	public static  void copyBigDataToSD(Context context,String fileName,int index) throws IOException 
    {  
		boolean sdCardExist = Environment.getExternalStorageState()
  			  .equals(android.os.Environment.MEDIA_MOUNTED);
		String path="";
		if(sdCardExist){
			File sdcardDir = Environment.getExternalStorageDirectory();
			
			switch(index){
			case Const.FILE_OF_PIC:
				path = sdcardDir.getPath()+"/fiveguessmovie/image";
				break;
			case Const.FILE_OF_MUSIC:
				 path = sdcardDir.getPath()+"/fiveguessmovie/music";
				break;
			case Const.FILE_OF_MOVIE:
				path = sdcardDir.getPath()+"/fiveguessmovie/video";
				break;
			}
			
			File outFile = new File(path);
			if(!outFile.exists()){
				outFile.mkdirs();
			}
			String outPath = path+"/"+fileName;
			File file = new File(outPath);
			if(!file.exists()){
				 InputStream myInput;  
			     OutputStream myOutput = new FileOutputStream(outPath);  
			     myInput = context.getAssets().open(fileName);  
			     byte[] buffer = new byte[1024];  
			     int length = 0;
			     while((length = myInput.read(buffer))!= -1)
			     { 
			    	 myOutput.write(buffer, 0, buffer.length); 
			     }
			     myOutput.flush();  
			     myInput.close();  
			     myOutput.close();        
			}
		}else{
			Toast.makeText(context, "请插入SD卡", Toast.LENGTH_SHORT).show();
			return;
		}  
			
	}
	
	
	 
}
