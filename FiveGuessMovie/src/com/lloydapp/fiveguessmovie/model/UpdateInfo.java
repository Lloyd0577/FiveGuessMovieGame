package com.lloydapp.fiveguessmovie.model;

public class UpdateInfo {

	private static UpdateInfo updateInfo = null;
	private int mVersion;
	private String  mDescribtion;
	private String mUrl;
	private String mName;
	private UpdateInfo(){
		
	}
	public static UpdateInfo getInstance(){
		if(updateInfo == null){
			updateInfo = new UpdateInfo();
		}
		return updateInfo;
	}
	public int getmVersion() {
		return mVersion;
	}
	public void setmVersion(int mVersion) {
		this.mVersion = mVersion;
	}
	public String getmDescribtion() {
		return mDescribtion;
	}
	public void setmDescribtion(String mDescribtion) {
		this.mDescribtion = mDescribtion;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	
}
