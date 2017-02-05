package com.lloydapp.fiveguessmovie.ui;

import java.util.ArrayList;
import java.util.List;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.Utils.Utils;
import com.lloydapp.fiveguessmovie.data.Const;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelActivity extends Activity{

	/*------------------------控件-------------------------*/
	//当前心数
	private TextView  mCurrentCoinText;
	//listView
	private ListView mListView;
	//标题
	private TextView mTitleText;
	//返回
	private Button mBackBtn;
	//分数栏
	private FrameLayout mStarFram;

	
	/*------------------------变量-------------------------*/
	
	private int mCurrentLevel;
	private int mCurrentCoin;
	private List<String> strList = new ArrayList<String>();
	private LevelAdapter mLevelAdapter;
	//屏幕高和宽
	private int mWidth;
	private int mHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_view);
		
		DisplayMetrics dm = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);  
		float dpi = dm.densityDpi;
		mWidth = dm.widthPixels; // 当前分辨率 宽度 单位px  
		mHeight = dm.heightPixels; // 当前分辨率 高度 单位px 
		
		mListView = (ListView)findViewById(R.id.list_view);
		mTitleText = (TextView)findViewById(R.id.stage_level);
		mBackBtn = (Button)findViewById(R.id.btn_bar_back);
		mStarFram = (FrameLayout)findViewById(R.id.star_fram);
		
		mStarFram.setVisibility(View.INVISIBLE);
		
		mTitleText.setText("关卡");
		
		
		//设置标题栏返回键
		mBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(LevelActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		mCurrentCoinText = (TextView)findViewById(R.id.txt_coin_num);
		
		mCurrentLevel = Utils.loadData(this)[0];
		mCurrentCoin = Utils.loadData(this)[1];
		mCurrentCoinText.setText(mCurrentCoin+"");
		initLevel();
		
		mLevelAdapter = new LevelAdapter(LevelActivity.this,
				R.layout.level_button_layout,strList);
		mListView.setAdapter(mLevelAdapter);
		
	}
	/**
	 * 初始化关卡
	 */
	private void initLevel(){
		for(int i = 0;i<Const.MOVIE_INFO.length;i++){
			strList.add(i+"");
		}
	}
	
	/**
	 * 自定义Adapter
	 * @author Administrator
	 *
	 */
	private class LevelAdapter extends ArrayAdapter<String>{
		private int resourceId;
		public LevelAdapter(Context context,int id,List<String> object){
			super(context,id,object);
			this.resourceId = id;
		}
		
		public View getView(final int position,View convertView,ViewGroup parent){
			View view;
			if(convertView == null){
				view = Utils.getView(LevelActivity.this, R.layout.level_button_layout);
				
			}else{
				view = convertView;
			}
			Button btn = (Button)view.findViewById(R.id.level_button_listview_item);
			
			btn.setWidth(mWidth);
			btn.setHeight((int)(mWidth/1.8));
			btn.setText((position+1)+"");
			if(position<=mCurrentLevel+1){
				btn.setTextColor(Color.RED);
				btn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						Intent i = new Intent(LevelActivity.this,MainActivity.class);
						i.putExtra("level_num", position-1);
						i.putExtra("from_level", true);
						startActivity(i);
						finish();
					}
				});
			}else{
				btn.setTextColor(Color.DKGRAY);
				btn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						Toast.makeText(LevelActivity.this, "该关未解锁", Toast.LENGTH_SHORT).show();
					}
				});
			}
			return view;
		}
	}
	
	/**
	 * 返回键
	 */
	public  void onBackPressed(){
		Intent intent = new Intent(LevelActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
}
