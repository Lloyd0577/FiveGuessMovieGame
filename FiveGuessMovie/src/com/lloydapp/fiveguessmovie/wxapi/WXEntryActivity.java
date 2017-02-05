package com.lloydapp.fiveguessmovie.wxapi;

import com.lloydapp.fiveguessmovie.R;
import com.lloydapp.fiveguessmovie.ui.MainActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String APP_ID = "wx82c73133679f8a3b";
	private IWXAPI api;
	
	private boolean mIsShareDone = false;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 api = WXAPIFactory.createWXAPI(this,APP_ID,true);
		 api.registerApp(APP_ID);
	     api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp  resp) {
		// TODO Auto-generated method stub
		 switch (resp.errCode) {  
	        case BaseResp.ErrCode.ERR_OK:  
	        	Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
	        	mIsShareDone = true;
	        	break;  
	        case BaseResp.ErrCode.ERR_USER_CANCEL:  
	        	Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
	            break;  
	        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
	        	Toast.makeText(this, "分享拒绝", Toast.LENGTH_LONG).show();
	            break;  
	        default:  
	            break;  
	        }  
		 //回到游戏主界面
		 Intent i = new Intent(WXEntryActivity.this,MainActivity.class);
		 int addStar = this.getResources().getInteger(R.integer.pay_delete_word);
		 i.putExtra("add_star", mIsShareDone ? addStar: 0);
		 i.putExtra("from_wxea", true);
		 startActivity(i);
		 finish();
		
		 
	}

}
