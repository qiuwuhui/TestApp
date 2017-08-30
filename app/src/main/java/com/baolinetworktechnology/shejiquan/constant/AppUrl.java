package com.baolinetworktechnology.shejiquan.constant;

/**
 * 存放Api url，常用的字符串
 * 
 * @author JiSheng.Guo
 * 
 */
public interface AppUrl {
	int NEWS_SUBJECT = 60;// 专题活动
	int NEWS_SCHOOL = 92;// 学设计
	int NEWS_LINGAN = 142;// 灵感
	String CLIENT = "2004";// 安卓客户端
	String MD5 = "7A3C622164";
	String USER_JSON = "USER_JSON";
	String VERSION = "2.0";
	String APP_AGENT = "ANDROID_SHEJIQUAN_Ver.2.0";// Api 版本号


//	String API ="http://192.168.60.253:10063";

//	测试接口
	String API ="http://192.168.23.202:10063";//内网

	//  映射端口
//	String API = "http://218.66.157.174:38001";

//	String API ="http://117.28.255.183:8100";//预发布

//	String API = "http://appapi.sjq315.com";//旧版正式接口

//    String API ="http://appapi.sjq315.com/V2.4.0";//线上

	/**
	 * 案例作品列表
	 */
	String CASELISET = "/api/services/app/case/GetPageList?";
	/**
	 * 案例详情lv2
	 */
	String DETAIL_CASE2 = "/api/services/app/case/GetDetail?id=";
	/**
	 * 设计师列表
	 */
	String DESIGNER = "/api/services/app/designer/GetPage?";
	/**
	 * GET APP首页--设计师
	 */
	String GETRECOMMEND = "/api/services/app/adverts/GetReCommendDesigner?";
	/**
	 * 获取设计师详情页信息
	 */
	String GET_DESIGNER_INFO = "/api/services/app/designer/GetDetail?id=";
	/**
	 * 获取设计师详情页信息（实时）
	 */
	String GET_DESIGNER_INFO_SHISI = "/api/services/app/designer/GetDesignerInfo?id=";
	/**
	 * 设计资讯列表页
	 */
	String DECORATIONGTLIST = "/api/services/app/news/GetPageList?ClassID=";
	/**
	 * 设计资讯详情页
	 */
	String DETAILINFO = "/api/services/app/news/GetDetail?Id=";
	/**
	 * 建议反馈POST
	 */
	String FEEDBACK = "/api/services/app/user/AddFeedback";
	/**
	 * 我的积分
	 */
	String GETSIGNINList = "/api/services/app/user/GetUserIntegral?";
	/**
	 * 根据条件获取收藏设计师列表
	 */
	String GET_DESIGNER_LIST = "/api/services/app/user/GetUserFavoriteDesigners?UserGuid=";
	/**
	 * GET根据条件获取收藏案例列表
	 */
	String GET_CASE_LIST = "/api/services/app/user/GetUserFavoriteCase?";
	/**
	 * 根据条件获取收藏资讯列表GETU
	 */
	String GET_NEWS_LIST = "/api/services/app/user/GetUserFavoriteNews?";
	/**
	 * 案例分类列表
	 */
	String CASE_CLASS_LIST = "/api/services/app/classCase/GetAllClassTag";
	/**
	 * 删除我的案例
	 */
	String REMOVE = "/api/services/app/case/CaseRemove";
	/**
	 * 获取广告轮播图和推荐设计师
	 */
	String ADVERT = "/api/services/app/adverts/GetReCommendAdverts?ReCommendId=";// 推广广告轮播图10000
	/**
	 * 评论界面数据接口
	 */
	//  markName 0:案例1:资讯5:设计师
	String COMMENT_LIST = "/api/services/app/comment/GetCommentList?";
	/**
	 * 发布评论
	 */
	String COMMENT_SEND = "/api/services/app/comment/AddComment";
	/**
	 * /**
	 * 全部评论
	 * 0:用户所有评论1:用户收到的评论2:用户发出去的评论
	 */
	String GETALLCOMMENTLIST = "/api/services/app/comment/GetUserCommentList?userGUID=";
	/**
	 * 收藏案例POST
	 */
	String FAVORITE_ADD = "/api/services/app/favorite/AddFavorite";
	/**
	 * 批量删除收藏案例POST
	 */
	String FAVORITE_CANCEL = "/api/services/app/favorite/CancelFavorite";
	/**
	 * 获取你喜欢的类型的案例和攻略
	 */
	String CUESSYOULIKE = "/api/services/app/user/GuessYouLike?currentPage=";
	/**
	 * 手机验证码快速登陆POST
	 */
	String MOBILE_LOGIN = "/api/services/app/account/LoginForVCode";
	/**
	 * 第三方登录
	 */
	String ThirdLogin = "/api/services/app/account/LoginForThird";
	/**
	 * 获取账户信息GET
	 */
	String GET_USER_INFO = "/api/services/app/user/GetUserInfo?id=";
	/**
	 * 获取个人及账户信息
	 */
	String GET_PERSONAL_INFO = "/api/services/app/personal/GetPersonalInfo?id=";
	/**
	 * 修改业主个人信息
	 */
	String UPDATE_PERSON_INFO = "/api/services/app/personal/EditPersonal";
	/**
	 * PUT 修改账户身份信息
	 */
	String USER_CHANGE_IDENTITY = "/api/services/app/user/EditUserIdentity";
	/**
	 * GET 是否收藏关注
	 */
	String IS_Favorite = "/api/services/app/favorite/IsFavaorite?";
	/**
	 * 修改设计师信息POST
	 */
	String UPDATE_DESIGNER_INFO = "/api/services/app/designer/EditDesigner";
	/**
	 * 设计师身份认证POST
	 */
	String CardAuthentication = "/api/services/app/designer/SaveAuthentication";
	/**
	 * 获取设计师认证信息
	 */
	String DESIGNERCERTIFICATION = "/api/services/app/designer/GetAuthenticationInfo?DesignerGuid=";
	/**
	 * 设计师案例信息
	 */
	String DESINGNERCASELIST = "/api/services/app/case/GetPageTheUserCase?";
	/**
	 * 设计师荣誉资质
	 */
	String PHOTOGETPAGELIST = "/api/services/app/photo/GetPageList?";
	/**
	 * 设计师推荐品牌
	 */
	String DESIGNERRECOMMEND = "/api/services/app/designer/DesignerRecommendBusiness?";
	/**
	 * 品牌信息
	 */
	String GETBRANDMODEL = "/api/services/app/businessmenBrand/GetBrandModel?BrandGuid=";
	/**
	 * 获取商品列表
	 */
	String GETBRANDPRODUCTLIST = "/api/services/app/businessmenBrand/GetBrandProductList?brandGUID=";
	/**
	 * 获取商品详情
	 */
	String GETBRANDPRODUCTDETIAL = "/api/services/app/businessmenBrand/GetBrandProductDetial?ProductGuid=";
	/**
	 * 获取品牌企业文化资质
	 */
	String GETBRANDHONOR = "/api/services/app/businessmenBrand/GetBrandHonorAndCulture?BrandGuid=";
	/**
	 * 上传图片
	 */
	String UPLOAD_FILE = "/api/Upload";
	/**
	 * 修改头像信息
	 */
	String UPLOAD_LOGO = "/api/services/app/user/EditUserLogo";
	/**
	 * 获取一期一会数据
	 */
	String ExpectCover = "/api/services/app/expectCover/GetListExpectCover?";
	/**
	 * 增加案例的1:点赞,2:分享,3:收藏,4:评论,5:点击,6:粉丝,7:案例,8:资讯,9:关注,10:查看,11:访客
	 */
	String ASSISTNPUT = "/api/services/app/case/UpdateCaseAssist";
	/**
	 * 增加设计师的1:点赞,2:分享,3:收藏,4:评论,5:点击,6:粉丝,7:案例,8:资讯,9:关注,10:查看,11:访客
	 */
	String DESIGNASSISTNPUT = "/api/services/app/designer/UpdateDesignerAssist";
	/**
	 * 手机绑定
	 */
	String MOBILEBIND = "/api/services/app/account/MobileBind";
	/**
	 * 获取手机验证码  template   1:登录,2:注册
	 */
	String SEND_SMS_CODE_MLOGIN = "/api/services/app/sMSTemplate/SmsCode?templateType=1&mobile=";

	String SEND_SMS_CODE_MLOGINZH = "/api/services/app/sMSTemplate/SmsCode?templateType=3&mobile=";
	/**
	 * 解绑第三方登录
	 */
	String CANCEL_THIRDINFO = "/api/services/app/account/CancelThirdPartyBind";
	/**
	 * 添加第三方登录
	 */
	String AddUserThirdInfo = "/api/services/app/account/AddThirdPartyBind";

	/**
	 * 社区分页列表
	 */
	String GETPAGELIST = "/api/services/app/posts/GetPageList?";
	String GETPAGELISTNew = "/api/services/app/posts/GetCommunityList?";//新接口，增加点赞收藏
	/**
	 * 朋友圈分页列表
	 */
	String GETCRICLE = "/api/services/app/posts/GetCriclePostsList?";
	/**
	 * 添加新帖子
	 */
	String ADDPOSTS = "/api/services/app/posts/AddPosts";
	/**
	 * 社区帖子详情
	 */
	String GETPOSTSDETIAL = "/api/services/app/posts/GetPostsDetial?objectId=";
	/**
	 * 朋友圈详情
	 */
	String GETCRICLEPOSTSDETIAL = "/api/services/app/posts/GetCriclePostsDetial?cricleGuid=";
	/**
	 * 社区帖子举报
	 */
	String ADDREPORT = "/api/services/app/posts/AddReport";
	/**
	 * 社区帖子收藏
	 */
	String OPERATEPOSTADD = "/api/services/app/posts/OperatePostsFavorite";
	/**
	 * 批量删除社区帖子收藏
	 */
	String CANCELPOSTSFAVORITE = "/api/services/app/posts/CancelPostsFavorite";
	/**
	 * 社区帖子是否收藏
	 */
	String ISPOSTSFAVAOR = "/api/services/app/posts/IsPostsFavaorite";
	/**
	 * 帖子收藏列表
	 */
	String GETCOLLECTPOSTSLIST = "/api/services/app/posts/GetCollectPostsList?";
	/**
	 * 是否点赞帖子
	 */
	String ISPOSTSGOOD = "/api/services/app/postsGood/IsPostsGood?";
	/**
	 * 添加帖子点赞
	 */
	String SAVEPOSTSGOOD = "/api/services/app/postsGood/SavePostsGood";
	/**
	 * 删除帖子
	 */
	String POSTREMOVE = "/api/services/app/posts/PostsRemove";
	/**
	 * 批量删除帖子
	 */
	String POSTMOREREMOVE = "/api/services/app/posts/PostsMoreRemove";
	/**
	 * 帖子点赞列表
	 */
	String GETPOSTSGOOD = "/api/services/app/postsGood/GetPostsGoodPage?";
	/**
	 * 获取系统未读消息
	 */
	String ORDER_GetMessageList = "/api/services/app/message/GetMessagePage?";
	/**
	 * 获取是否有未读消息/订单消息GET
	 */
	String GetIsNoReadMessage = "/api/services/app/message/GetAllNoRead?";
	/**
	 * PUT 更改系统消息为已读
	 */
	String UpdateMessageRead = "/api/services/app/message/UpdateMeaasgeRead";
	/**
	 * PUT 点赞已读
	 */
	String UpdatePOSTSRead = "/api/services/app/postsGood/UpdatePostsRead";
	/**
	 * PUT 评论已读
	 */
	String UpdateCommentRead = "/api/services/app/comment/UpdateCommentRead";
	/**
	 * 获取客服电弧
	 */
	String GETCUSTOMINfFO = "/api/ApplicationSite/GetCustomInfo";
	/**
	 * 获取环信好友列表
	 */
	String GETEASEFRIEND = "/api/services/app/easeFriend/GetEaseFriend?UserGuid=";
	/**
	 * 获取推荐好友
	 */
	String ADVISEEASEFRI = "/api/services/app/easeFriend/AdviseEaseFriend?UserGuid=";
	/**
	 * 获取推荐好友
	 */
	String SEARCHEASE = "/api/services/app/easeFriend/SearchEaseUser?searchEaseKeyWord=";
	/**
	 * 添加环信好友
	 */
	String ADDEASEFRIEND = "/api/services/app/easeFriend/AddEaseFriend";
	/**
	 * 删除环信好友
	 */
	String DELETAEASEFRIEND = "/api/services/app/easeFriend/DeleteEaseFriend";
	/**
	 * 校验是否是朋友关系
	 */
	String CHECKISFRIRND = "/api/services/app/easeFriend/CheckIsFriend?userGUID=";
	/**
	 * 获取推荐城市
	 */
	String GETRECOMMENDCITY = "/api/services/app/classArea/GetRecommendCity";
	/**
	 * 变更好友推荐提示 enumHint (integer, optional): [1:通讯提示,2:通讯录不提示,0:不再推荐] = ['0', '1', '2']
	 */
	String EDITHINFRIEND = "/api/services/app/easeFriend/EditHintFriend";
	/**
	 * 设置好友备注
	 */
	String EDITEASEFRIEND = "/api/services/app/easeFriend/EditEaseFriend";
	/**
	 * 标签新增
	 */
	String ADDEASELABLE = "/api/services/app/easeFriend/AddEaseLable";
	/**
	 * 获取标签及其标签下的好友
	 */
	String GETEASELABLE = "/api/services/app/easeFriend/GetEaseLableFrind?userGUID=";
	/**
	 * 删除标签
	 */
	String DELETEEASELABLE = "/api/services/app/easeFriend/DeleteEaseLable";
	/**
	 * 删除标签好友
	 */
	String DELETELABLEFRIEND = "/api/services/app/easeFriend/DeleteLableFriendRelation";
	/**
	 * 获取好友所有标签
	 */
	String GETFRIENDLABEL = "/api/services/app/easeFriend/GetFriendLabel?userGUID=";
	/**
	 * 新增多个标签
	 */
	String ADDMULTIPLEEASELABEL = "/api/services/app/easeFriend/AddMultipleEaseLabel";



	/**
	 * 全屋案例接口(楼上)  风格传	2 户型是 4
	 */
	String ATTRIBUTELIST = "http://app-api.bzw315.com/api/Common/SystemAttributeList";
//	String ATTRIBUTELIST = "http://api-test.bzw315.com/api/Common/SystemAttributeList";
	/**
	 * 全屋案例列表(楼上)
	 */
	String SEARCHLIST = "http://app-api.bzw315.com/api/Cases/SearchList";
//	String SEARCHLIST = "http://api-test.bzw315.com/api/Cases/SearchList";
}