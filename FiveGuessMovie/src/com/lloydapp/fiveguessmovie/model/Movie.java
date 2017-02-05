package com.lloydapp.fiveguessmovie.model;

public class Movie {

	private String mMovieName;
	private String mMovieFileName;
	
	public void setSongName(String name){
		this.mMovieName = name;
	}
	public String getSongName(){
		return mMovieName;
	}
	public void setSongFileName(String name){
		this.mMovieFileName = name;
	}
	public String getSongFileName(){
		return mMovieFileName;
	}
	
	//获取歌曲名长度
	public int getSongLength(){
		return mMovieName.length();
	}
	
	//将歌曲名字符串转换为子数组
	public char[] toCharArray(){
		return mMovieName.toCharArray();
	}
}
