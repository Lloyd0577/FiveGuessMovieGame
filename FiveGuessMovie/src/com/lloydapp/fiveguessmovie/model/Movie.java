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
	
	//��ȡ����������
	public int getSongLength(){
		return mMovieName.length();
	}
	
	//���������ַ���ת��Ϊ������
	public char[] toCharArray(){
		return mMovieName.toCharArray();
	}
}
