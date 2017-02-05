package com.lloydapp.fiveguessmovie.data;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;

import android.os.Environment;

public class Const {

	public static final String PIC_OF_SHARE = "share_pic.png";
	public static final String PIC_OF_PASS_ALL ="passall.png";
	//sd卡目录
	public static final String SD_ROOT = Environment.
			getExternalStorageDirectory().toString();
	public static final String SD_CARD_IMAGE_PATH =SD_ROOT+"/fiveguessmovie/image";
	public static final String SD_CARD_VIDEO_PATH = SD_ROOT+"/fiveguessmovie/video";
	public static final String SD_CARD_APK_PATH = SD_ROOT+"/fiveguessmovie/apk";
	//微信API
	public static final String APP_ID = "wx82c73133679f8a3b";
	public static IWXAPI api;
	
	//新浪API
	public static final String XL_APP_KEY      = "3526904270";		   // 应用的APP_KEY
	public static final String REDIRECT_URL = "http://www.sina.com";// 应用的回调页
	public static final String SCOPE = 							   // 应用申请的高级权限
	            "email,direct_messages_read,direct_messages_write,"
		           + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
		           + "follow_app_official_microblog," + "invitation_write";
	public static IWeiboShareAPI WBAPI;
	
	//音效索引
	public static final int TONE_OF_ENTER  = 0;
	public static final int TONE_OF_CANCEL = 1;
	public static final int TONE_OF_COIN   = 2;
	public static final int TONE_OF_CHEER  = 3;
	
	
	//游戏界面索引
	public static final int VIEW_OF_FIRST = 0;
	public static final int VIEW_OF_GAME  = 1;			
	public static final int VIEW_OF_PASS  = 2;	
	public static final int VIEW_OF_PASS_ALL = 3;
	
	//asset中文件类型的索引
	public static final int FILE_OF_PIC  =  0;
	public static final int FILE_OF_MUSIC = 1;
	public static final int FILE_OF_MOVIE = 2;
		
	//初始化金币
	public static final int TOTAL_COIN = 200;
	
	public static final String[][]MOVIE_INFO = {
			{"gongfu.mp4","功夫"},
			{"taitannikehao.mp4","泰坦尼克号"},
			{"bingxueqiyuan.mp4","冰雪奇缘"},
			{"dahuaxiyou2.mp4","大话西游二"},
			{"taijiong.mp4","泰囧"},
			{"chaoti.mp4","超体"},
			{"jiupinzhimaguan.mp4","九品芝麻官"},
			{"bingchuanshidai5.mp4","冰川时代五"},
			{"xiaoshenkedejiushu.mp4","肖申克的救赎"},
			{"chaofanzhizhuxia2.mp4","超凡蜘蛛侠二"},
			{"aganzhengzhuan.mp4","阿甘正传"},
			{"zhengguzhuanjia.mp4","整蛊专家"},
			{"shentanxialuoke.mp4","神探夏洛克"},
			{"feichengwurao.mp4","非诚勿扰"},
			{"bawangbieji.mp4","霸王别姬"},
			{"fengkuangdeshitou.mp4","疯狂的石头"},
			{"fengkuangdongwucheng.mp4","疯狂动物城"},
			{"fulian2.mp4","复联二"},
			{"gangtiexia2.mp4","钢铁侠二"},
			{"gongfuxiongmao3.mp4","功夫熊猫三"},
			{"gongfuzuqiu.mp4","功夫足球"},
			{"guochanlinglingqi.mp4","国产凌凌漆"},
			{"haishanggangqinshi.mp4","海上钢琴师"},
			{"huoxingjiuyuan.mp4","火星救援"},
			{"jiqizongdongyuan.mp4","机器总动员"},
			{"laonanhai.mp4","老男孩"},
			{"chumendeshijie.mp4","楚门的世界"},
			{"laopaoer.mp4","老炮儿"},
			{"leishen.mp4","雷神"},
			{"longmao.mp4","龙猫"},
			{"meiguoduizhang.mp4","美国队长"},
			{"houhuiwuqi.mp4","后会无期"},
			{"meiguoduizhang3.mp4","美国队长三"},
			{"qingchunpai.mp4","青春派"},
			{"bianhuren.mp4","辩护人"},
			{"yiren.mp4","蚁人"},
			{"qianyuqianxun.mp4","千与千寻"},
			{"taoxueweilong.mp4","逃学威龙"},
			{"tiantaiaiqing.mp4","天台爱情"},
			{"xingjichuanyue.mp4","星际穿越"},
			{"xunlonggaoshou.mp4","驯龙高手"},
			{"yewen3.mp4","叶问三"},
			{"jigong.mp4","济公"},
			{"laorenyuhai.mp4","老人与海"},
			{"yingxiongbense.mp4","英雄本色"},
			{"huozhe.mp4","活着"},
	};
}
