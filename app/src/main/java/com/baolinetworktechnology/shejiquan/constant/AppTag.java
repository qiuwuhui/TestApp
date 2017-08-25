package com.baolinetworktechnology.shejiquan.constant;

/**
 * 常用的标识
 * 
 * @author JiSheng.Guo
 * 
 */
public interface AppTag {
	String APP_TOKEN="app_token";
	String TAG = "TAG";//log专用tag
	String TAG_TITLE = "TAG_TITLE";// 标题
	String IS_MY = "IS_MY";// 是否登录用户自己
	String TAG_ID = "TAG_ID";//
	String TAG_PHONE = "TAG_PHONE";//
	String TAG_GUID = "USER_GUID";//
	String TAG_IS_CASE = "TAG_IS_CASE";//
	String TAG_JSON = "TAG_JSON";
	String TAG_NUMER = "TAG_NUMER";// 数字
	String TAG_TEXT = "TAG_TEXT";// 文本内容
	String CLIENT = "2004";// 安卓客户端
	String MD5 = "7A3C622164";
	String MARKNAME_DESIGNER = "DESIGNER";
	//正式
	String WeChatAppID = "wxc22edbd77cb42c30";// "wx1a89435b47e34be8";
	String WeChatAppSecret = "e1da4d54f19280469f63eef8c9b5b522";// "419c46b24055a4512b30749860a5e0c9";
    //测试
//	String WeChatAppID = "wx1a89435b47e34be8";
//	String WeChatAppSecret = "419c46b24055a4512b30749860a5e0c9";

	String QQAppID = "1104396630";
	String QQAppSecret = "419c46b24055a4512b30749860a5e0c9";
	int RESULT_OK = 0x11;// 返回码
	int DESIGNER = 100;
	int ZHUANXIU =200;
	String ENUM_USER_MARK_PERSONAL = "0";// 0 100 200 普通 设计 企业
	String ENUM_USER_MARK_DESIGNER = "100";// 0 100 200 普通 设计 企业
	String ENUM_USER_MARK_ENTERPRISE = "200";// 0 100 200 普通 设计 企业

	//请求验证码
	String YAN_ZHEN="A2F0FA1B60574BD198D0481A450E2F73";

	//用户喜好推荐
	//栏目
	String TJ_Column="HouseColumn";
	//风格
	String TJ_Style="AttrStyle";
	//户型
	String TJ_Type="ArrayAttrHouseType";
	//所在市ID
	String TJ_CityID="TJCityID";
	//所在市名称
	String TJ_CityName="TJCityname";
	//面积
	String TJ_SIZE="Size";
	//装修阶段
	String YE_ZXJD="zxjd";
	//一期一会
	String DAILY="daily";
	//String SinaID = "1615123269";
	//String SinaSecret = "1e0f4d850454424101158cd56411c144";
	
	//收藏方式
	String Sql_sc="0";
	String Sql_gz="1";
	
	//收藏类型
	String Sql_anli="0";
	String Sql_wenz="1";
	String Sql_yezhu="4";
	String Sql_sheji="5";
	String Sql_gs="6";
	
	
	//类型
	int ClassType1=0; //案例
	int ClassType2=1;  //文章
	int ClassType3=4;  //业主
	int ClassType4=5;  //设计师
	int ClassType5=6;  //装修公司

	//评论请求类型
	// 案例
	int CASE = 0;
    // 新闻
	int NEWS = 1;
	// 产品
	int PRODUCTS = 2;
    // 个人
	int PERSONAL = 4;
	// 设计师
	int DESIGNE = 5;
	// 企业
	int ENTERPRISE = 6;

	//新闻图片判断
	int DAN = 20;
	int DUO = 10;

	/**
	 * 建议反馈POST
	 */
	String KONNGJIAN ="空间";  //ParentID=2
	String FENGGE ="风格";  //ParentID=25
	String HUXING ="户型";  //ParentID=71
	String MIANJI ="面积";  //ParentID=2224
	String GEDIAO ="格调";  //ParentID=125
	String YUSUAN ="预算";  //ParentID=87
	String DESAERA ="擅长空间";  //ParentID=2241
}
