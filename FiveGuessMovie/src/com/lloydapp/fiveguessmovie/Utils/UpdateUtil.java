package com.lloydapp.fiveguessmovie.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import com.lloydapp.fiveguessmovie.model.UpdateInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * 自动更新应用工具类
 * @author Lloyd
 * @date 2016-06-12
 */
public class UpdateUtil {
	


	
	/**
	 * 获取服务端版本号
	 * @return
	 */
	public static int getVerCodeFromSer(){
		return UpdateInfo.getInstance().getmVersion();
	}
	/**
	 * 获取新版本apk下载地址
	 * @return
	 */
	public static String getApkUrl(){
		return UpdateInfo.getInstance().getmUrl();
	}
	/**
	 * 获取本地应用版本号
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context){
		int verCode = -1;
		try {
			verCode = context.getPackageManager().
					getPackageInfo("com.lloydapp.fiveguessmovie", 0).
					versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verCode;
	}
	/**
	 * 获取本地应用版本名称
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context){
		String verName = "";
		try {
			verName = context.getPackageManager().
					getPackageInfo("com.lloydapp.fiveguessmovie", 
							0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verName;
	}
	/**
	 * 从服务器上获取新版本信息
	 * @return
	 */
	public static  String getUpdateInfoFromService(){
				String path = "https://raw.githubusercontent.com/Lloyd0577/"
						+ "Lloyd.github.io/master/version.xml";
				StringBuffer sb = new StringBuffer();
				String line = null;
				BufferedReader reader = null;
				try {
					URL url = new URL(path);
					HttpURLConnection urlConnection = (HttpURLConnection)url.
							openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setConnectTimeout(60000);
					urlConnection.setReadTimeout(60000);
					reader = new BufferedReader(new InputStreamReader
							(urlConnection.getInputStream()));
					while((line = reader.readLine())!= null){
						sb.append(line);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(reader != null){
						try {
							reader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				return sb.toString();
	}
	
	/**
	 * xml解析服务器返回的版本信息
	 * @param response
	 */
	public static  void   parseXMLWithPull(String response){
		Log.d("####", "this is xml ");
		response = response.replaceAll("[^\\x20-\\x7e]", "");
		Log.d("####", "this is xml "+response);
		UpdateInfo updateInfo = UpdateInfo.getInstance();
		try {
			XmlPullParserFactory factory = 
					XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(response));
			int eventType = xmlPullParser.getEventType();
			String  version = "";
			String name = "";
			String url = "";
			String describtion = "";
			while(eventType != XmlPullParser.END_DOCUMENT){
				String nodeName = xmlPullParser.getName();
				switch(eventType){
				case XmlPullParser.START_TAG:{
					if("version".equals(nodeName)){
						version = xmlPullParser.nextText();
						Log.d("####", "version is "+version);
					}else if("name".equals(nodeName)){
						name = xmlPullParser.nextText();
						Log.d("####", "name is "+name);
					}else if("url".equals(nodeName)){
						url = xmlPullParser.nextText();
					}else if("describtion".equals(nodeName)){
						describtion = xmlPullParser.nextText();
					}
					break;
				}
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
			Log.d("####", "version is "+version);
			updateInfo.setmVersion(Integer.parseInt(version)); 
			updateInfo.setmName(name);
			updateInfo.setmDescribtion(describtion);
			updateInfo.setmUrl(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
}
