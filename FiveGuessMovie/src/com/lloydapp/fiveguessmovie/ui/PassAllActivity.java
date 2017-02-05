package com.lloydapp.fiveguessmovie.ui;



import java.io.File;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.Utils.ScreenShotUtils;
import com.lloydapp.fiveguessmovie.Utils.Utils;
import com.lloydapp.fiveguessmovie.Utils.WXUtil;
import com.lloydapp.fiveguessmovie.data.Const;
import com.lloydapp.fiveguessmovie.model.IDialogClickListener;
import com.lloydapp.fiveguessmovie.xlapi.XLUtil;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class PassAllActivity extends Activity implements OnClickListener,
 IDialogClickListener{
 
	//���水ť
	private Button mReplayBtn;
	private Button mShareBtn;
	private Button mExitBtn;
	
	
	//�����
	private LinearLayout mShareLin;
	private ImageButton mWXPengBtn;
	private ImageButton mWXHaoYou;
	private ImageButton mXLShareBtn;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paa_all_view);
		//��ʼ���ؼ�
		mReplayBtn = (Button)findViewById(R.id.replay_button);
		mShareBtn = (Button)findViewById(R.id.pass_all_share_button);
		mExitBtn = (Button)findViewById(R.id.pass_all_exit_button);
		mShareLin = (LinearLayout)findViewById(R.id.share_lin);
		mWXPengBtn = (ImageButton)findViewById(R.id.weixin_button);
		mWXHaoYou = (ImageButton)findViewById(R.id.weixin_haoyou_button);
		mXLShareBtn = (ImageButton)findViewById(R.id.xinlan_share_image_button);
		
		mReplayBtn.setOnClickListener(this);
		mShareBtn.setOnClickListener(this);
		mExitBtn.setOnClickListener(this);
	}
	

	/**
	 * ����
	 */
	private void share(){
		mShareLin.setVisibility(View.VISIBLE);
		mWXPengBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
					WXUtil.handleWXShare( null,mShareLin,PassAllActivity.this,
						true,Const.PIC_OF_PASS_ALL,
						Const.VIEW_OF_PASS_ALL, 0,null);
				
			}
		});
		mWXHaoYou.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				WXUtil.handleWXShare( null,mShareLin,PassAllActivity.this,
						false,Const.PIC_OF_PASS_ALL,
						Const.VIEW_OF_PASS_ALL, 0,null);
			}
		});
		mXLShareBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				File f = new File(Const.SD_CARD_IMAGE_PATH+Const.PIC_OF_PASS_ALL);
				if(!f.exists()){
					ScreenShotUtils.shotBitmap(PassAllActivity.this, Const.PIC_OF_PASS_ALL);
				}
				XLUtil.sendMultiMessage(PassAllActivity.this, 
						Const.SD_CARD_IMAGE_PATH+Const.PIC_OF_PASS_ALL, 0, null);
			}
		});
		
	}
	/**
	 * ���水ť����¼�
	 */
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.replay_button:
			//��ʾ�Զ���Ի���
			Utils.showDialog(this,"���潫�����ù����ͷ������Ƿ������",this);
			break;
		case R.id.pass_all_share_button:
			//΢�ŷ���
			share();
			break;
		case R.id.pass_all_exit_button:
			//�˳�����
			Utils.saveData(this, Const.MOVIE_INFO.length-2, Const.TOTAL_COIN);
			finish();
			break;
		}
	}
	/**
	 * �Ի��������ȷ������¼�
	 */
	@Override
	public void onClick(){
		Utils.saveData(PassAllActivity.this, -1, Const.TOTAL_COIN);
		Intent intent = new Intent(PassAllActivity.this,MainActivity.class);
		intent.putExtra("from_pass_game", true);
		startActivity(intent);
		finish();
	}
		/**
	 * ���ؼ�
	 */
	public void onBackPressed(){
		Utils.saveData(this, Const.MOVIE_INFO.length-2, Const.TOTAL_COIN);
		finish();
	}
}
