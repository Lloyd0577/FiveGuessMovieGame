package com.lloydapp.fiveguessmovie.ui;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.Utils.MyPlayer;
import com.lloydapp.fiveguessmovie.Utils.ScreenShotUtils;
import com.lloydapp.fiveguessmovie.Utils.Utils;
import com.lloydapp.fiveguessmovie.Utils.WXUtil;
import com.lloydapp.fiveguessmovie.data.Const;
import com.lloydapp.fiveguessmovie.model.IDialogClickListener;
import com.lloydapp.fiveguessmovie.model.Movie;
import com.lloydapp.fiveguessmovie.model.WordButton;
import com.lloydapp.fiveguessmovie.xlapi.AccessTokenKeeper;
import com.lloydapp.fiveguessmovie.xlapi.XLUtil;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMenuItemClickListener ,IWeiboHandler.Response{
	
	/*-------------------------动画----------------------------------*/

	//文字动画
	private Animation mBtnAnim;
	
	/*--------------------------控件---------------------------------*/
	private LinearLayout mVedioLin;
	//视频控件
	private SurfaceView mSurfaceView;
	private MediaPlayer mMediaPlayer;
	//开始按钮
	private ImageButton mPlayBtn;
	//已选文字容器
	private LinearLayout mSelectedWordContainer;
	//待选文字容器
	private TableLayout mWordContainer;
	//当前关控件
	private TextView mCurrentStageText;
	//当前金币控件
	public static TextView mCurrentCoinText;
	//过关界面
	private LinearLayout mPassLayout;
	//过关界面下一题按钮
	private Button mPassNextStageBtn;
	//过关界面分享按钮
	private Button mPassShareBtn;
	//过关界面关数
	private TextView mPassLevelText;
	//过关界面电影名
	private TextView mPassMovieText;
	//删除错误文字按钮
	private ImageButton mDeleteWordBtn;
	//提示正确文字按钮
	private ImageButton mTipAnsBtn;
	
	private RelativeLayout mFirstView;
	//titleBar
	private RelativeLayout mTopBarLin;
	private Button mBackBtn;
	
	//开始界面开始和关卡按钮
	private Button mStartBtn;
	private Button mLevelBtn;
	
	//开始界面上下控件，用于动画
	private LinearLayout mUpLin;
	private LinearLayout mDownLin;
	
	//游戏主界面
	private LinearLayout mGameView;
	
	//工具栏
	private LinearLayout mToolLin;
	
	//过关界面奖励文本框
	private LinearLayout mJiangLi;
	
	//分享框
	private LinearLayout mShareLin;
	private ImageButton mWeiXinBtn;
	private ImageButton mWeiXinHYBtn;
	private ImageButton mXLShareBtn;
	
	//进度条
	
	
	/*---------------------------常量------------------------------------*/
	//答案状态
	public static final int STATUS_OF_ANS_LACK  = 0;
	public static final int STATUS_OF_ANS_RIGHT = 1;
	public static final int STATUS_OF_ANS_WRROR = 2;
	
	//自定义对话框的索引
	public static final int DIALOG_OF_DELETE_WORD = 0;
	public static final int DIALOG_OF_TIP_ANSWER  = 1;
	public static final int DIALOG_OF_NO_COIN     = 2;
	

	
	
	
	/*---------------------------变量-----------------------------------*/
	
	//当前金币数
	public static int mCurrentCoin ;
	//当前歌曲名
	private Movie mCurrentMovie;
	//当前关卡数
	private int mCurrentStage;
	//已选文字数据
	private List<WordButton> mSelectedWordData;
	//用于累计删除文字的个数
	private int mCount;
	
	private int mPosition ;

	//屏幕高宽
	private int mWidth;
	private int mHeight;
	//动画控件的初始位置
	private float local;
	
	//活动来源标志
	private boolean mIsFromLevel = false;
	private boolean mIsAnimating = false;
	private boolean mIsFromLast =  false;
	private boolean mIsFromWXActivity = false;
	private boolean mIsFromWBShare = false;
	private boolean mIsWBShareSuc = false;
	
	private int mCurrentView = Const.VIEW_OF_FIRST;
	//微博客户端是否已安装
	private boolean isInstalledWeibo;
	//所支持的API版本
	private static int supportApiLevel;
	
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("MainActivity", "this is onCreate");
		regToWx();
		regToXl();
		if (savedInstanceState != null) {
            Const.WBAPI.handleWeiboResponse(getIntent(), this);
        }
		//判断活动来源
		mIsFromLevel = getIntent().getBooleanExtra("from_level", false);
		mIsFromLast = getIntent().getBooleanExtra("from_pass_game", false);
		mIsFromWXActivity = getIntent().getBooleanExtra("from_wxea", false);
		mIsFromWBShare = getIntent().getBooleanExtra("from_wb", false);
		 
		
		//初始化控件
		mFirstView = (RelativeLayout)findViewById(R.id.first_view);
		mToolLin = (LinearLayout)findViewById(R.id.tool_view);
		mVedioLin = (LinearLayout)findViewById(R.id.panpian);
		mGameView = (LinearLayout)findViewById(R.id.game_view);
		mPlayBtn = (ImageButton)findViewById(R.id.btn_paly);
		mSelectedWordContainer = (LinearLayout)findViewById(R.id.word_selecte_container);
		mWordContainer = (TableLayout)findViewById(R.id.tablelayout);
		mCurrentCoinText = (TextView)findViewById(R.id.txt_coin_num);
		mCurrentStageText = (TextView)findViewById(R.id.stage_level);
		mPassLayout = (LinearLayout)findViewById(R.id.pass_view);
		mPassNextStageBtn = (Button)findViewById(R.id.next_stage_button);
		mPassShareBtn = (Button)findViewById(R.id.pass_share_button);
		mDeleteWordBtn = (ImageButton)findViewById(R.id.delete);
		mTipAnsBtn = (ImageButton)findViewById(R.id.tip);
		
		mPassLevelText = (TextView)findViewById(R.id.level_pass_view);
		mPassMovieText = (TextView)findViewById(R.id.movie_name);
		mSurfaceView = (SurfaceView)findViewById(R.id.surface_view);
		mTopBarLin = (RelativeLayout)findViewById(R.id.top_bar);
		mBackBtn = (Button)findViewById(R.id.btn_bar_back);
		mShareLin = (LinearLayout)findViewById(R.id.share_lin);
		mWeiXinBtn = (ImageButton)findViewById(R.id.weixin_button);
		mWeiXinHYBtn = (ImageButton)findViewById(R.id.weixin_haoyou_button);
		mXLShareBtn = (ImageButton)findViewById(R.id.xinlan_share_image_button);
		
		mGameView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mShareLin.setVisibility(View.INVISIBLE);
				return false;
			}
		});
		mBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(mIsAnimating == false){
					if(mCurrentView == Const.VIEW_OF_GAME){
						mIsAnimating = true;
						stopMovie();
						gameViewOutAnim();
					}else if(mCurrentView == Const.VIEW_OF_FIRST){
						finish();
					}
				}
			}
		});
		mStartBtn = (Button)findViewById(R.id.start_game_button);
		mLevelBtn = (Button)findViewById(R.id.choose_level_button);
		mUpLin = (LinearLayout)findViewById(R.id.start_view_up_lin);
		mDownLin = (LinearLayout)findViewById(R.id.start_view_down_lin);
	
		mStartBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				mIsAnimating = true;
				startRemoveButtonAnim();
			}
		});
		
		mLevelBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,LevelActivity.class);
				startActivity(intent);
				finish();
			}
		});
	
		//初始化控件大小
		initViewSize();
		//初始化关数分数	
		initStageAndStarData();
		
		//初始化游戏数据（每关开始都要加载）
		initCurrentStageData();
		
		//初始化控件位置
		initViewsLocal();
		
		//开始按钮点击事件
		mPlayBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				handlePlayBtn();
			}
		});
		
		//删除按钮处理事件
		handleDeleteWord();
		
		//提示正确答案按钮处理事件
		handleTipAns();
	
		if(mIsFromLevel||mIsFromWXActivity||mIsFromWBShare){
			mIsAnimating = true;
			startRemoveButtonAnim();
			mIsFromLevel = false;
			mIsFromWXActivity = false;
			mIsFromWBShare = false;
		}
		
	}
	@Override
	protected void onStart(){
		super.onStart();
		Log.d("MainActivity", "this is onStart");
		if(mIsFromWBShare){
			Intent i = new Intent(MainActivity.this,MainActivity.class);
			if(mIsWBShareSuc){
				i.putExtra("add_star", 30);
			}
			i.putExtra("from_wb", true);
			startActivity(i);
			finish();
			mIsFromWBShare = false;
		}
		
	}
	@Override
	protected void onResume(){
		super.onResume();
		Log.d("MainActivity", "this is onResume");
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Log.d("MainActivity", "this is onDestroy");
	}
	 
	/**
	 * 注册到新浪
	 */
	private void regToXl(){
		 // 创建微博 SDK 接口实例
        Const.WBAPI = WeiboShareSDK.createWeiboAPI(this, Const.XL_APP_KEY);
        Const.WBAPI.registerApp();	
        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        isInstalledWeibo =Const.WBAPI.isWeiboAppInstalled();
        supportApiLevel = Const.WBAPI.getWeiboAppSupportAPI(); 
        mAuthInfo = new AuthInfo(this, Const.XL_APP_KEY, Const.REDIRECT_URL, null);
        mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
        
       
	}
	/**
	 * 注册微信接口
	 */
	private void regToWx(){
		Const.api = WXAPIFactory.createWXAPI(this,Const.APP_ID,true);
		Const.api.registerApp(Const.APP_ID);
	}
	/**
	 * 开始游戏界面进入动画
	 */
	private void startViewInAnim(){
	/*	float curStartBtnX = mUpLin.getTranslationX();
		float curLevelBtnX = mDownLin.getTranslationX();*/
		
		ObjectAnimator animatorStart = ObjectAnimator.ofFloat(mUpLin, "translationX",
				-local,0f);
				animatorStart.setDuration(800);
				animatorStart.start();
	
		ObjectAnimator animatorLevel = ObjectAnimator.ofFloat(mDownLin, "translationX", 
				-local,0f);
		animatorLevel.setDuration(800);
		animatorLevel.setStartDelay(200);
		animatorLevel.addListener(new AnimatorListener(){

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				mIsAnimating = false;
				mCurrentView = Const.VIEW_OF_FIRST;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		animatorLevel.start();
	
	}
	/**
	 * 游戏主界面退出动画
	 */
	private void gameViewOutAnim(){
		float curTopBarX = mTopBarLin.getTranslationX();
		float curToolViewX = mToolLin.getTranslationX();
		float curVedioViewX = mVedioLin.getTranslationX();
		float curWordContainer = mSelectedWordContainer.getTranslationX();
		Log.d("#######","curVedioViewX is "+curVedioViewX);
		ObjectAnimator animatorTopBar = ObjectAnimator.ofFloat(mTopBarLin, "translationX",
				curTopBarX,local);
		animatorTopBar.setDuration(800);
		animatorTopBar.start();
		
		
		ObjectAnimator animatorVedioView = ObjectAnimator.ofFloat(mVedioLin, "translationX",
				curVedioViewX,local);
		animatorVedioView.setDuration(800);
		
		animatorVedioView.start();
		
		
		ObjectAnimator animatorToolView = ObjectAnimator.ofFloat(mToolLin, "translationX",
				curToolViewX,local);
		animatorToolView.setDuration(800);
		animatorVedioView.setStartDelay(300);
		animatorToolView.start();
		
		ObjectAnimator animatorContainer = ObjectAnimator.ofFloat(mSelectedWordContainer, "translationX",
				curWordContainer,local);
		animatorContainer.setDuration(800);
		animatorContainer.setStartDelay(500);
		animatorContainer.addListener(new AnimatorListener(){

			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				mGameView.setVisibility(View.GONE);
				mFirstView.setVisibility(View.VISIBLE);
				startViewInAnim();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
		});
		animatorContainer.start();
	}
	/**
	 * 游戏主界面进入动画
	 */
	private void gameViewInAnim(){
		float curTopBarX = mTopBarLin.getTranslationX();
		float curToolViewX = mToolLin.getTranslationX();
		float curVedioViewX = mVedioLin.getTranslationX();
		float curWordContainer = mSelectedWordContainer.getTranslationX();
		Log.d("#######","curVedioViewX is "+curVedioViewX);
		ObjectAnimator animatorTopBar = ObjectAnimator.ofFloat(mTopBarLin, "translationX",
				curTopBarX,0f);
		animatorTopBar.setDuration(1000);
		animatorTopBar.start();
		
		
		ObjectAnimator animatorVedioView = ObjectAnimator.ofFloat(mVedioLin, "translationX",
				curVedioViewX,0f);
		animatorVedioView.setDuration(1000);
		animatorVedioView.start();
		
		
		ObjectAnimator animatorToolView = ObjectAnimator.ofFloat(mToolLin, "translationX",
				curToolViewX,0f);
		animatorToolView.setDuration(1000);
		animatorVedioView.setStartDelay(600);
		animatorToolView.start();
		
		ObjectAnimator animatorContainer = ObjectAnimator.ofFloat(mSelectedWordContainer, "translationX",
				curWordContainer,0f);
		animatorContainer.setDuration(1000);
		animatorContainer.setStartDelay(700);
		animatorContainer.addListener(new AnimatorListener(){

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				mIsAnimating = false;
				handlePlayBtn();
				mSurfaceView.getHolder().setKeepScreenOn(true);
				mSurfaceView.getHolder().addCallback(new SurfaceHolderLis());
				mCurrentView = Const.VIEW_OF_GAME;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		animatorContainer.start();
	}
	
	/**
	 * 开始游戏按钮退出动画
	 */
	private void startRemoveButtonAnim(){
		float curStartBtnX = mUpLin.getTranslationX();
		float curLevelBtnX = mDownLin.getTranslationX();
		
		
		ObjectAnimator animatorStart = ObjectAnimator.ofFloat(mUpLin, "translationX",
				curStartBtnX,-local);
				animatorStart.setDuration(800);
				animatorStart.start();
	
		ObjectAnimator animatorLevel = ObjectAnimator.ofFloat(mDownLin, "translationX", 
				curLevelBtnX,-local);
		animatorLevel.addListener(new AnimatorListener(){

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
			//	stopMovie();
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				mFirstView.setVisibility(View.GONE);
				mGameView.setVisibility(View.VISIBLE);
				gameViewInAnim();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		animatorLevel.setDuration(800);
		animatorLevel.setStartDelay(200);
		
		animatorLevel.start();
	}
	/**
	 * 播放电影
	 * @param fileName
	 */
	private void playMovie(String fileName){
		if(mMediaPlayer == null){
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDisplay(mSurfaceView.getHolder());
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mPlayBtn.setVisibility(View.VISIBLE);
				}
			});
		}
		mMediaPlayer.reset();
		AssetManager am = this.getAssets();
		AssetFileDescriptor fd = null;
		try {
			fd = am.openFd(fileName);
			mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
					fd.getLength());
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
	}
	/**
	 * 停止播放电影
	 */
	private  void stopMovie(){
		if(mMediaPlayer != null){
			mMediaPlayer.stop();
		}
	}
	
	/**
	 * SurfaceHolder回调
	 */
	private class SurfaceHolderLis implements SurfaceHolder.Callback{

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			
			
			if(mPosition == 0 ){
				playMovie(mCurrentMovie.getSongFileName());
				mMediaPlayer.seekTo(mPosition);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder,
				int format, int width, int height) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
		
	}
	/**
	 * 播放按钮
	 */
	private void handlePlayBtn(){
		//隐藏开始按钮
		mPlayBtn.setVisibility(View.GONE);
		playMovie(mCurrentMovie.getSongFileName());
	}

	
	/**
	 * 微信视频分享给好友(非官方)
	 */
	private void weiXinVideoShare(){
		
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.tools.ShareImgUI"));
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("video/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
				Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.putExtra("Kdescription", "1dfeh11 g1");
		Uri uri = Uri.fromFile(new File(Environment.
				getExternalStorageDirectory().toString()+"/fiveguessmovie/video",mCurrentMovie.getSongFileName()));
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(intent);
		
	}
	/**
	 * 删除按钮处理事件
	 */
	private void handleDeleteWord(){
		mDeleteWordBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(isCoinEnough(-getDeleteWordCoin())){
					showMyDialog(DIALOG_OF_DELETE_WORD);
				}else{
					showMyDialog(DIALOG_OF_NO_COIN);
				}
			}
		});
	}
	/**
	 * 提示按钮处理事件
	 */
	private void handleTipAns(){
		mTipAnsBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(isCoinEnough(-getTipAnsCoin())){
					showMyDialog(DIALOG_OF_TIP_ANSWER);
				}else{
					showMyDialog(DIALOG_OF_NO_COIN);
				}
				
			}
		});
	}
	//判断金币是否足够
	private boolean isCoinEnough(int data){
		return mCurrentCoin + data >= 0;
	}
	
	//获得过关奖励心数
	private int getPassLevelStar(){
		return this.getResources().getInteger(R.integer.pass_level_star_add);
	}
	//获取删除文字所需要心数
	private int getDeleteWordCoin(){
		return this.getResources().getInteger(R.integer.pay_delete_word);
	}
	//获取提示答案所需心数
	private int getTipAnsCoin(){
		return this.getResources().getInteger(R.integer.pay_tip_answer);
	}
	
	/**
	 * 用于显示自定义对话框
	 * @param index
	 */
	private void showMyDialog(int index){
		switch(index){
		case DIALOG_OF_DELETE_WORD:
			Utils.showDialog(this, "是否要花30颗心去掉一个错误文字", deleteWordListener);
			break;
		case DIALOG_OF_TIP_ANSWER:
			Utils.showDialog(this,"是否要花90颗心提示一个正确文字", tipAnsListener);
			break;
		case DIALOG_OF_NO_COIN:
			Utils.showDialog(this, "当前红心不足，是否分享以获得红心", noCoinListener);
			break;
		}
	}
	/**
	 * 删除文字监听器
	 */
	private IDialogClickListener deleteWordListener = new IDialogClickListener(){
		
		public void onClick(){
		
			if(mCount != 24 - mCurrentMovie.getSongLength()){
				deleteWord();
				mCurrentCoin = mCurrentCoin - getDeleteWordCoin();
				mCurrentCoinText.setText(mCurrentCoin+"");
			}else{
				Toast.makeText(MainActivity.this, "已无可删除文字！", Toast.LENGTH_SHORT).show();
			}
			
			
		}
	};
	/**
	 * 提示文字监听器
	 */
	private IDialogClickListener tipAnsListener = new IDialogClickListener(){
			
		public void onClick(){
			tipAns();
			mCurrentCoin = mCurrentCoin - getTipAnsCoin();
			mCurrentCoinText.setText(mCurrentCoin+"");
			
		
			
		}
	};
	/**
	 * 金币不足监听器
	 */
	private IDialogClickListener noCoinListener = new IDialogClickListener(){
			
		public void onClick(){
			//分享功能
			handleShareMenuItemEvent();	
		}
	};
	/**
	 * 删除错误文字
	 */
	private void deleteWord(){
		Random rd = new Random();
		while(true){
			int index = rd.nextInt(24);
			Button btn = (Button)mWordContainer.findViewWithTag(index);
			String str = btn.getText().toString();
			if(mCurrentMovie.getSongName().contains(str)||
					btn.getVisibility()== View.INVISIBLE){
				continue;
			}
			btn.setVisibility(View.INVISIBLE);
			mCount ++;
			break;
		}
	}
	/**
	 * 提示一个答案
	 */
	private void tipAns(){
		for(int i = 0; i < mCurrentMovie.getSongLength();i++){
			if(mSelectedWordData.get(i).mWordButton.getText().equals("")){
				for(int j = 0 ;j < 24 ;j++){
					Button btn = (Button)mWordContainer.findViewWithTag(j);
					if((mCurrentMovie.toCharArray()[i]+"").equals(btn.getText())){
						onButtonClick(btn);
					}
				}
				break;
			}
		}
	}
	/**
	 * 初始化关和分数据
	 */
	private void initStageAndStarData(){
		if(mIsFromLevel){
			mCurrentStage = getIntent().getIntExtra("level_num", 0);
				
		}else if(mIsFromLast){
			mCurrentStage = -1;
			mIsFromLast = false;
		}else {
			mCurrentStage = Utils.loadData(this)[0];
		}
			
		mCurrentCoin = Utils.loadData(this)[1];
			
		//如果来自与微信则心增加
		if(mIsFromWXActivity||mIsFromWBShare){
			int addStar = getIntent().getIntExtra("add_star", 0);
			mCurrentCoin  = mCurrentCoin + addStar;
			Utils.saveData(this, mCurrentStage, mCurrentCoin);
		
		}
	
	}
	/**
	 * 初始化空间大小
	 */
	private void initViewSize(){
		DisplayMetrics dm = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);  
		
		float densityDpi = dm.densityDpi;
	
		
		mWidth = dm.widthPixels; // 当前分辨率 宽度 单位px  
		mHeight = dm.heightPixels; // 当前分辨率 高度 单位px 
		Log.d("########", "width and height is "+mWidth+"::"+mHeight);
			android.view.ViewGroup.LayoutParams params =  mSurfaceView.getLayoutParams();
			if(densityDpi>160){
				params.width = mWidth*10/11;
				params.height = params.width*6/9;
			}else{
				params.width = mWidth*7/10;
				params.height = params.width*6/9;
			}
			
			
			mSurfaceView.setLayoutParams(params);
			
			android.view.ViewGroup.LayoutParams titleParams =  mTopBarLin.getLayoutParams();
			titleParams.height = mHeight/9;
			mTopBarLin.setLayoutParams(titleParams);
	}
	/**
	 * 初始化当前关数据
	 */
	private void initCurrentStageData(){
		
		//初始化已选文字框
		initSelectedContainer();
		//初始化待选文字框
		initWordContainer();
		//初始化关数和金币
		setStageAndCoin();
		
		
	}
	/**
	 * 初始化控件位置
	 */
	private void initViewsLocal(){
		local = (float)mWidth;
		//将动画控件设置到初始位置
		mVedioLin.setTranslationX(local);
		mSelectedWordContainer.setTranslationX(local);
		mTopBarLin.setTranslationX(local);
		mToolLin.setTranslationX(local);
	}
	/**
	 * 初始化关数和金币数
	 */
	private void setStageAndCoin(){
	
		//初始化金币
		
		mCurrentCoinText.setText(mCurrentCoin+"");
		
		//初始化关数
		mCurrentStageText.setText((mCurrentStage+1)+"");
		
	}
	/**
	 * 初始化已选文字框
	 */
	private void initSelectedContainer(){
		//清空已选文字框内容
		mSelectedWordContainer.removeAllViews();
		//初始化当前歌曲
		mCurrentMovie = loadCurrenrSong(++mCurrentStage);
		Log.d("Tag", "song is "+mCurrentMovie.getSongName());
		mSelectedWordData = initSelectedWordData();
		for(int i = 0; i< mSelectedWordData.size(); i++ ){
			LayoutParams params = new LayoutParams(mWidth/6,mWidth/6);
			mSelectedWordContainer.addView(mSelectedWordData.get(i).mWordButton,params);
		}
	}
	/**
	 * 初始化待选文字框
	 */
	private void initWordContainer(){
		mWordContainer.removeAllViews();
		String[] words = getAllWord();
		int index = 0;
		TableRow.LayoutParams params = new TableRow.LayoutParams(0, mWidth/8);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		params.setMargins(2, 2, 2, 2);
		for(int i = 0;i<3;i++){
			TableRow tr = new TableRow(this);
			for(int j = 0;j < 8 ; j++){
				final Button btn = (Button)Utils.getView(this, R.layout.self_ui_gridview_item);
				btn.setTag(index);
				btn.setText(words[index++]);
				btn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						//文字点击事件
						onButtonClick(btn);
					}
				});
				
				mBtnAnim = AnimationUtils.loadAnimation(this, R.anim.word_scale);
				mBtnAnim.setStartOffset(index*100);
				btn.startAnimation(mBtnAnim);
						
				tr.addView(btn,params);
			}
			mWordContainer.addView(tr);
		}
		
	}
	/**
	 * 文字点击事件
	 * @param btn
	 */
	private void onButtonClick(Button btn){
		
		//将点击的文字填入已选文字框中去
		selecteWord(btn);
		
		//获取答案状态
		int status = getAnsStatus();
		
		switch(status){
		case STATUS_OF_ANS_LACK:
			//将答案颜色设置为白色
			for(int i = 0; i<mCurrentMovie.getSongLength();i++){
				if(!mSelectedWordData.get(i).mWordButton.getText().equals("")){
					mSelectedWordData.get(i).mWordButton.setTextColor(Color.WHITE);
				}
			}
			break;
		case STATUS_OF_ANS_RIGHT:
			//进入过关界面
			handlePassEvent();
			break;
		case STATUS_OF_ANS_WRROR:
			//已选文字闪烁
			sparkWords();
			break;
		}
	}
	
	/**
	 * 处理过关逻辑
	 */
	private void handlePassEvent(){
		
		mCurrentView = Const.VIEW_OF_PASS;
		mJiangLi = (LinearLayout)findViewById(R.id.jiangli);
		mJiangLi.setVisibility(View.INVISIBLE);
		
		//停掉电影播放
		stopMovie();
		
		//出现过关界面
		mPassLayout.setVisibility(View.VISIBLE);
		
		//除过关界面的按钮设为不可点击
		mPassLayout.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(mShareLin.getVisibility()==View.VISIBLE){
					mShareLin.setVisibility(View.GONE);
				}
				return true;
			}
			
		});
		//过关界面下一关按钮监听事件
		mPassNextStageBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(mCurrentStage+1 == Const.MOVIE_INFO.length){
					MyPlayer.playTone(MainActivity.this, Const.TONE_OF_CHEER);
					Intent i = new Intent(MainActivity.this,PassAllActivity.class);
					startActivity(i);
					finish();
				}else{
					mPassLayout.setVisibility(View.INVISIBLE);
					mCurrentView = Const.VIEW_OF_GAME;
					initCurrentStageData();
					handlePlayBtn();
				}
				
			}
		});
		//过关界面分享按钮监听事件
		mPassShareBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mShareLin.setVisibility(View.VISIBLE);
				mWeiXinBtn.setVisibility(View.VISIBLE);
				
				//微信朋友圈分享
				mWeiXinBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						
						WXUtil.handleWXShare(Const.api,mShareLin,MainActivity.this,
								true,mCurrentMovie.getSongName()+".png",Const.VIEW_OF_PASS,mCurrentStage,mCurrentMovie);
					}
				});
				
				
				//微信好友分享
				mWeiXinHYBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						WXUtil.handleWXShare(Const.api,mShareLin,MainActivity.this,
								false,mCurrentMovie.getSongName()+".png",Const.VIEW_OF_PASS,mCurrentStage,mCurrentMovie);
					}
				});
				mXLShareBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						mShareLin.setVisibility(View.INVISIBLE);
						File passImageFile = new File(Const.SD_CARD_IMAGE_PATH,mCurrentMovie.getSongName()+".png");
						if(!passImageFile.exists()){
							ScreenShotUtils.shotBitmap(MainActivity.this,
									mCurrentMovie.getSongName()+".png");
						}
						
						if(!AccessTokenKeeper.readAccessToken(MainActivity.this).
								isSessionValid()){
							wbAuth();
						}else{
							XLUtil.sendMultiMessage(MainActivity.this,passImageFile.
									getAbsolutePath(),mCurrentStage,mCurrentMovie);
						}
						mIsFromWBShare = true; 
					}
				});
			}
		});
		
		//设置过关界面的关数和电影名
		mPassLevelText.setText((mCurrentStage+1)+"");
		mPassMovieText.setText(mCurrentMovie.getSongName());
		
		//保存游戏数据
		int maxLevel = Utils.loadData(this)[0];
		//当前的关卡大于已保存的则保存
		if(mCurrentStage > maxLevel){
			mJiangLi.setVisibility(View.VISIBLE);
			mCurrentCoin = mCurrentCoin+getPassLevelStar();
			mCurrentCoinText.setText(mCurrentCoin+"");
			MyPlayer.playTone(this, Const.TONE_OF_COIN);
			Utils.saveData(this, mCurrentStage, mCurrentCoin);
		}
		
	}
	/**
	 * 闪烁错误答案
	 */
	private void sparkWords(){
		
		TimerTask task = new TimerTask(){
			boolean mFlag =false;
			int changTime = 0;
			@Override
			public void run(){
			runOnUiThread(new Runnable(){
					@Override
					public void run(){
						if(++changTime > 6){
							return;
						}
						for(int i = 0; i<mCurrentMovie.getSongLength();i++){
							 mSelectedWordData.get(i).mWordButton.setTextColor(mFlag?Color.RED:Color.WHITE);
						}
						mFlag = !mFlag;
					}
				}); 
			}
		};
		Timer t = new Timer();
		t.schedule(task, 1,150);
	}
	/**
	 * 获取答案状态
	 * @return
	 */
	private int getAnsStatus(){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<mCurrentMovie.getSongLength();i++){
			if(mSelectedWordData.get(i).mWordButton.getText().equals("")){
				return STATUS_OF_ANS_LACK;
			}
			sb.append(mSelectedWordData.get(i).mWordButton.getText().toString());
		}
		if(sb.toString().equals(mCurrentMovie.getSongName())){
			return STATUS_OF_ANS_RIGHT;
		}else{
			return STATUS_OF_ANS_WRROR;
		}
		
	}
	/**
	 * 将点击的文字填入已选文字框中去
	 * @param btn
	 */
	private void selecteWord(Button btn){
		int index = (Integer) btn.getTag();
		for(int i = 0;i<mCurrentMovie.getSongLength();i++){
			if(mSelectedWordData.get(i).mWordButton.getText().equals("")){
				mSelectedWordData.get(i).mIndex = index;
				mSelectedWordData.get(i).mWordButton.setText(btn.getText().toString());
				mSelectedWordData.get(i).mWordString = btn.getText().toString();
				btn.setVisibility(View.INVISIBLE);
				break;
			}
		}
	}
	/**
	 * 获取待选文字
	 * @return
	 */
	private String[] getAllWord(){
		String[] str = new String[24];
		for(int i = 0;i < mCurrentMovie.getSongLength();i++){
			char[] songChar = mCurrentMovie.toCharArray();
			str[i] = songChar[i]+"";
		}
		for(int i = mCurrentMovie.getSongLength();i<24;i++){
			str[i] = getRandomWord();
		}
		for(int i = 23;i>=0;i--){
			Random rd = new Random();
			int index = rd.nextInt(i+1);
			String buf = "";
			buf = str[index];
			str[index] = str[i];
			str[i] = buf;
		}
		return str;
	}
	/**
	 * 获取随机汉字
	 * @return
	 */
	private String getRandomWord(){
		String str = "";
		int heightPos;
		int lowPos;
		Random rd = new Random();
		heightPos = 176 + Math.abs(rd.nextInt(39));
		lowPos = 161 + Math.abs(rd.nextInt(93));
		byte[] bt = new byte[2];
		bt[0] = Integer.valueOf(heightPos).byteValue();
		bt[1] = Integer.valueOf(lowPos).byteValue();
		try {
			str = new String(bt,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 加载当前歌曲
	 * @param index
	 * @return
	 */
	private Movie loadCurrenrSong(int index){
		Movie song = new Movie();
		String[] array = Const.MOVIE_INFO[index];
		song.setSongFileName(array[0]);
		song.setSongName(array[1]);
		return song;
	}
	/**
	 * 初始化已选文字数据
	 * @return
	 */
	private List<WordButton> initSelectedWordData(){
		List<WordButton> data = new ArrayList<WordButton>();
		for(int i = 0; i < mCurrentMovie.getSongLength(); i++){
			final WordButton wb = new WordButton();
			wb.mWordButton = (Button)Utils.
					getView(this,R.layout.self_ui_gridview_item);
			wb.mIndex = i;
			wb.mWordButton.setText("");
			wb.mWordButton.setTextColor(Color.WHITE);
			wb.mWordButton.setBackgroundResource(R.drawable.game_wordblank);
			wb.mWordButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					//清除答案
					clearAns(wb);
				}
			});
			wb.mWordString = "";
			data.add(wb);
		}
		return data;
		
	}
	
	
	/**
	 * 清除答案
	 * @param wb
	 */
	private void clearAns(WordButton wb){
		int index = wb.mIndex;
		wb.mWordButton.setText("");
		wb.mWordString = "";
		Button bt = (Button)mWordContainer.findViewWithTag(index);
		bt.setVisibility(View.VISIBLE);
	}
	
	
	@Override
	public void onPause(){
		stopMovie();
		super.onPause();
		Log.d("MainActivity", "this is onPaause");
	}
	/**
	 * 返回键
	 */
	public void onBackPressed(){
		if(mShareLin.getVisibility()==View.VISIBLE){
			mShareLin.setVisibility(View.INVISIBLE);
			return;
		}
		if(mIsAnimating == false){
			if(mCurrentView == Const.VIEW_OF_PASS){
				
			}
			if(mCurrentView == Const.VIEW_OF_GAME){
				mIsAnimating = true;
				stopMovie();
				gameViewOutAnim();
			}else if(mCurrentView == Const.VIEW_OF_FIRST){
				finish();
			}
		}
	}

	/**
	 * 通过反射机制设置菜单图片可见性
	 * @param menu
	 * @param enable
	 */
	 private void setIconEnable(Menu menu, boolean enable)  
	    {  
	        try   
	        {  
	            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");  
	            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);  
	            m.setAccessible(true);  
	              
	            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)  
	            m.invoke(menu, enable);  
	              
	        } catch (Exception e)   
	        {  
	            e.printStackTrace();  
	        }  
	    }  
	 
	 
	
	 /**
	  * 处理分享菜单项
	  */
	 private void handleShareMenuItemEvent(){
			//显示分享界面
			mShareLin.setVisibility(View.VISIBLE);
			mWeiXinBtn.setVisibility(View.VISIBLE);
			
			
			//微信朋友圈分享
			mWeiXinBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					WXUtil.handleWXShare( Const.api,mShareLin,MainActivity.this,
							true,Const.PIC_OF_SHARE,
							Const.VIEW_OF_GAME, mCurrentStage,mCurrentMovie);
				}
			});
			
			
			//微信好友分享
			mWeiXinHYBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					WXUtil.handleWXShare( Const.api,mShareLin,MainActivity.this,
							false,Const.PIC_OF_SHARE,
							Const.VIEW_OF_GAME, mCurrentStage,mCurrentMovie);
				}
			});
			//新浪分享
			mXLShareBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					File shareImageFile = new File(Const.SD_CARD_IMAGE_PATH,
							Const.PIC_OF_SHARE);
					if(!shareImageFile.exists()){
						try {
							Utils.copyBigDataToSD(MainActivity.this, Const.PIC_OF_SHARE, Const.FILE_OF_PIC);
						} catch (IOException e) {
							e.printStackTrace();
							Toast.makeText(MainActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
						}
					}
					if(!AccessTokenKeeper.readAccessToken(MainActivity.this).
							isSessionValid()){
						wbAuth();
					}else{
						XLUtil.sendMultiMessage(MainActivity.this,shareImageFile.
								getAbsolutePath().toString(),mCurrentStage,mCurrentMovie);
					}
					mIsFromWBShare = true; 
				}
			});
			
	 }
	/**
	 * 显示菜单
	 * @param v
	 */
	public void showPopup(View v){
		
		PopupMenu popup = new PopupMenu(this,v);
		MenuInflater inflater = popup.getMenuInflater();
		Menu m = popup.getMenu();
		setIconEnable(m,true);
		inflater.inflate(R.menu.main, m);
		popup.setOnMenuItemClickListener(this);
		popup.show();
	}
	
	//菜单项点击事件
	@Override
	public boolean onMenuItemClick(MenuItem item){
		switch(item.getItemId()){
		case R.id.share_menu:
			handleShareMenuItemEvent();
		
			return true;
		default:
				return false;
		}
	}
	
	  @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
		     Log.d("onNewIntent", "this in onNewIntent");   
	       
	        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
	        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
	        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
	        Const.WBAPI.handleWeiboResponse(intent, this);
	    }

	    /**
	     * 接收微客户端博请求的数据。
	     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
	     * 
	     * @param baseRequest 微博请求数据对象
	     * @see {@link IWeiboShareAPI#handleWeiboRequest}
	     */
	    @Override
	    public void onResponse(BaseResponse baseResp) {
	    	 Log.d("onResponse", "this in onResponse");  
	        if(baseResp!= null){
	            switch (baseResp.errCode) {
	            case WBConstants.ErrorCode.ERR_OK:
	            	 Log.d("onResponse", "this in onResponseSuess"); 
	                Toast.makeText(MainActivity.this,"分享成功", Toast.LENGTH_LONG).show();
	                mIsWBShareSuc = true;
	                break;
	            case WBConstants.ErrorCode.ERR_CANCEL:
	            	 Log.d("onResponse", "this in onResponsecancel"); 
	                Toast.makeText(MainActivity.this,"分享取消", Toast.LENGTH_LONG).show();
	                
	                break;
	            case WBConstants.ErrorCode.ERR_FAIL:
	            	 Log.d("onResponse", "this in onResponseeero"); 
	                Toast.makeText(MainActivity.this, 
	                      "分享出错", 
	                        Toast.LENGTH_LONG).show();
	                break;
	            }
	        }
	    }
	    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (mSsoHandler != null) {
	    	Log.d("MainActivity", "this is in onActivityResult");
	        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	       
	    }
	}
	/**
	 * 微博授权
	 */
	private void wbAuth(){
		mSsoHandler.authorize(new AuthListener());
	}
	/**
	 * 授权监听器
	 * @author Administrator
	 *
	 */
	class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
        	/*Intent i = new Intent(MainActivity.this,MainActivity.class);
 	       MainActivity.this.startActivity(i);
 	       MainActivity.this.finish();*/
        	Log.d("MainActivity", "this is in onCompel");
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息 
           
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
            	Log.d("MainActivity", "this is in onCompelsuccess");
                AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                Toast.makeText(MainActivity.this, 
                       "授权成功", Toast.LENGTH_SHORT).show();
                
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
        	Log.d("MainActivity", "this is in onCompelcancel");
            Toast.makeText(MainActivity.this, 
                   "授权取消", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
        	Log.d("MainActivity", "this is in onCompelerror");
            Toast.makeText(MainActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
       
	}
	
}
