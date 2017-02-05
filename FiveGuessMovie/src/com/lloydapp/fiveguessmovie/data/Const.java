package com.lloydapp.fiveguessmovie.data;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;

import android.os.Environment;

public class Const {

	public static final String PIC_OF_SHARE = "share_pic.png";
	public static final String PIC_OF_PASS_ALL ="passall.png";
	//sd��Ŀ¼
	public static final String SD_ROOT = Environment.
			getExternalStorageDirectory().toString();
	public static final String SD_CARD_IMAGE_PATH =SD_ROOT+"/fiveguessmovie/image";
	public static final String SD_CARD_VIDEO_PATH = SD_ROOT+"/fiveguessmovie/video";
	public static final String SD_CARD_APK_PATH = SD_ROOT+"/fiveguessmovie/apk";
	//΢��API
	public static final String APP_ID = "wx82c73133679f8a3b";
	public static IWXAPI api;
	
	//����API
	public static final String XL_APP_KEY      = "3526904270";		   // Ӧ�õ�APP_KEY
	public static final String REDIRECT_URL = "http://www.sina.com";// Ӧ�õĻص�ҳ
	public static final String SCOPE = 							   // Ӧ������ĸ߼�Ȩ��
	            "email,direct_messages_read,direct_messages_write,"
		           + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
		           + "follow_app_official_microblog," + "invitation_write";
	public static IWeiboShareAPI WBAPI;
	
	//��Ч����
	public static final int TONE_OF_ENTER  = 0;
	public static final int TONE_OF_CANCEL = 1;
	public static final int TONE_OF_COIN   = 2;
	public static final int TONE_OF_CHEER  = 3;
	
	
	//��Ϸ��������
	public static final int VIEW_OF_FIRST = 0;
	public static final int VIEW_OF_GAME  = 1;			
	public static final int VIEW_OF_PASS  = 2;	
	public static final int VIEW_OF_PASS_ALL = 3;
	
	//asset���ļ����͵�����
	public static final int FILE_OF_PIC  =  0;
	public static final int FILE_OF_MUSIC = 1;
	public static final int FILE_OF_MOVIE = 2;
		
	//��ʼ�����
	public static final int TOTAL_COIN = 200;
	
	public static final String[][]MOVIE_INFO = {
			{"gongfu.mp4","����"},
			{"taitannikehao.mp4","̩̹��˺�"},
			{"bingxueqiyuan.mp4","��ѩ��Ե"},
			{"dahuaxiyou2.mp4","�����ζ�"},
			{"taijiong.mp4","̩��"},
			{"chaoti.mp4","����"},
			{"jiupinzhimaguan.mp4","��Ʒ֥���"},
			{"bingchuanshidai5.mp4","����ʱ����"},
			{"xiaoshenkedejiushu.mp4","Ф��˵ľ���"},
			{"chaofanzhizhuxia2.mp4","����֩������"},
			{"aganzhengzhuan.mp4","��������"},
			{"zhengguzhuanjia.mp4","����ר��"},
			{"shentanxialuoke.mp4","��̽�����"},
			{"feichengwurao.mp4","�ǳ�����"},
			{"bawangbieji.mp4","������"},
			{"fengkuangdeshitou.mp4","����ʯͷ"},
			{"fengkuangdongwucheng.mp4","������"},
			{"fulian2.mp4","������"},
			{"gangtiexia2.mp4","��������"},
			{"gongfuxiongmao3.mp4","������è��"},
			{"gongfuzuqiu.mp4","��������"},
			{"guochanlinglingqi.mp4","����������"},
			{"haishanggangqinshi.mp4","���ϸ���ʦ"},
			{"huoxingjiuyuan.mp4","���Ǿ�Ԯ"},
			{"jiqizongdongyuan.mp4","�����ܶ�Ա"},
			{"laonanhai.mp4","���к�"},
			{"chumendeshijie.mp4","���ŵ�����"},
			{"laopaoer.mp4","���ڶ�"},
			{"leishen.mp4","����"},
			{"longmao.mp4","��è"},
			{"meiguoduizhang.mp4","�����ӳ�"},
			{"houhuiwuqi.mp4","�������"},
			{"meiguoduizhang3.mp4","�����ӳ���"},
			{"qingchunpai.mp4","�ഺ��"},
			{"bianhuren.mp4","�绤��"},
			{"yiren.mp4","����"},
			{"qianyuqianxun.mp4","ǧ��ǧѰ"},
			{"taoxueweilong.mp4","��ѧ����"},
			{"tiantaiaiqing.mp4","��̨����"},
			{"xingjichuanyue.mp4","�Ǽʴ�Խ"},
			{"xunlonggaoshou.mp4","ѱ������"},
			{"yewen3.mp4","Ҷ����"},
			{"jigong.mp4","�ù�"},
			{"laorenyuhai.mp4","�����뺣"},
			{"yingxiongbense.mp4","Ӣ�۱�ɫ"},
			{"huozhe.mp4","����"},
	};
}
