package com.baolinetworktechnology.shejiquan.constant;

/**
 * 存放Api url，常用的字符串
 * 
 * @author JiSheng.Guo
 * 
 */
public interface ApiUrl {
	int NEWS_SUBJECT = 60;// 专题活动
	int NEWS_SCHOOL = 92;// 学设计
	int NEWS_LINGAN = 142;// 灵感
	String CLIENT = "2004";// 安卓客户端
	String MD5 = "7A3C622164";
	String USER_JSON = "USER_JSON";
	String VERSION = "2.0";
	String APP_AGENT = "ANDROID_SHEJIQUAN_Ver.2.0";// Api 版本号
	
	
//	String API = "http://testappapi.sjq315.com";// http://testappapi.shejiquan.me
	String HTTP_HEAD = "http://testm.sjq315.com";
	String API = "http://appapi.sjq315.com";//http://appapi.sjq315.me
//	String API ="http://192.168.60.254:10087";
//	String API ="http://218.66.157.174:10087";
//	String API ="http://218.66.157.174:10087";
	//建龙
//	String API ="http://192.168.60.48:8085";
//	String HTTP_HEAD="http://m.sjq315.com";
	 

	/**
	 * 发布委托
	 */
	String ENTRUSTORDER = HTTP_HEAD
			+ "/activity/mobile/android/entrustorder?isAPP=1";

	String DESIGNER = "/api/Designer/Search?";
	
	

	// ************************News|Case*******************

	/**
	 * GET
	 */
	String NEWS_GET_CLASS = "/api/News/GetRecommendNews";

	/**
	 * 资讯详情
	 */
	String DETAIL_NEWS = "/api/News/Detail?Id=";
	/**
	 * 首页专题
	 */
	String HOME_NEWS = "/api/News/Search?ClassID=141";
	String NEWS_LIST = "/api/News/Search?";
	/**
	 * 装修攻略类别
	 */
	String GETDECORATIONTYPE= "/v3/Decoration/GetDecorationType";
	/**
	 * 装修攻略类别列表
	 */
	String DECORATIONGTLIST= "/v3/Decoration/GetList?ClassID=";
	/**
	 * 装修阶段类别列表
	 */
	String RECOMMENDDCOR_CLASS= "/v3/Recommend/RecommendDecorationClass?";
	/**
	 * 装修攻略详情
	 */
	String DETAILINFO= "/api/News/DetailInfo?Id=";
	/**
	 * 获取推荐到主页的每周精选GET
	 */
	String CASE_GET_WEEKLY = "/api/Case/GetWeeklySelected?PageSize=10";
	
	/**
	 * 获取你喜欢的类型的案例和攻略
	 */
	String CUESSYOULIKE = "/v3/Recommend/GuessYouLike?AttrHouseTypeID=";

	/**
	 * 案例详情lv2
	 */
	String DETAIL_CASE2 = "/api/Case/DetailV2?Id=";
	/**
	 * 案例分类列表
	 */
	String CASE_CLASS_LIST = "/api/Case/ClassList";

	String CASE_VERSION = "/api/Config/IosVersion";
	/**
	 * 作品列表
	 */
	String CASE_LISET = "/api/Case/Search?ClassID=0";
	String CASELISET = "/api/Case/Search?";
	
	/**
	 * 获取装修公司及账户信息GET
	 */
	String GET_ENTERPRI_INFO = "/api/User/GetUserInfoEnterprise?UserGUID=";
	
	/**
	 * 修改装修公司信息POST
	 */
	String UPDATE_BUSINESS_DETAIL = "/v3/Business/UpdateBusinessDetail";
	/**
	 * 装修公司列表
	 */
	String BUSINESS="/v3/Business/GetList?";
	/**
	 * 装修详细页数据
	 */
	String BUSINESSDETAIL="/v3/Business/BusinessDetail?businessID=";	
	/**
	 * 装修公司案例信息
	 */
	String GETBUSINESSCASELIST="/v3/Business/GetBusinessCaseList?";
	/**
	 * 设计师案例信息
	 */
	String DESINGNERCASELIST="/v3/UserCenter/DesignerCaseList?";	
	/**
	 * 装修公司详细
	 */
	String BUSINESSINTRO="/v3/Business/BusinessIntro?businessID=";	
	/**
	 * 装修公司工商详细
	 */
	String BUSINESSREGISTER="/v3/Business/BusinessRegister?BusinessGUID=";
	/**
	 * 装修公司资质证书
	 */
	String BUSINESSCREDENTIAL="/v3/Business/BusinessCredential?BusinessGUID=";
	/**
	 * 装修公司搜索
	 */
	String BUSINESSSEARCH="/v3/Business/BusinessSearch?key=";
	/**
	 * 装修公司个人中心案例详情
	 */
	String BUSINESSCASELIST="/v3/UserCenter/BusinessCaseList?";
	/**
	 * 获取装修公司认证信息
	 */
	String BUSINESSCERTIFICATION="/v3/UserCenter/BusinessCertification?BusinessID=";
	/**
	 * 获取设计师认证信息
	 */
	String DESIGNERCERTIFICATION="/v3/UserCenter/DesignerCertification?DesignerID=";

	/**
	 * 获取一期一会数据
	 */
	String ExpectCover="/v3/ExpectCover/GetList?";

	/**
	 * 根据条件获取收藏资讯列表GETU
	 */
	String GET_NEWS_LIST = "/api/Favorite/GetNewsList?";
	/**
	 * GET根据条件获取收藏案例列表
	 */
	String GET_CASE_LIST = "/api/Favorite/GetCaseList?";
	/**
	 * 引导页推荐商家
	 */
	String RECOMMENDEDBUSSINESS = "/v3/GuidePages/RecommendedBusiness?";	
	/**
	 * 引导页推荐设计师
	 */
	String RECOMMENDEDSINGNER = "/v3/GuidePages/RecommendedDesigner?";
	/**
	 * 根据条件搜索资讯分类列表GET
	 */
	String NEWS_CLASS = "/api/News/ClassList?ParentID=";

	/**
	 * 添加文章POST
	 */
	String ADD_NEWS = "/api/News/SaveOrUpdate";
	/**
	 * 添加案例POST
	 */
	String ADD_CASE = "/api/Case/SaveOrUpdate";

	/**
	 * 删除POST UserGUID,InfoID
	 */
	String REMOVE = "/api/User/Remove";
	/**
	 * 获取用户案例GET
	 */

	String GET_USER_CACE = "/api/Case/GetUserList?";
	String GET_USER_NEWS = "/api/News/GetUserList?UserGUID=";
	String ADVERT = "/api/Advert/Detail?Id=";// 推广广告轮播图10000
	String GetRecommendDesigner = "/api/Designer/GetRecommendDesigner?CityID=";// 推广广告轮播图10000
	// ************************账户信息*****************`**

	/**
	 * 签到 POST
	 */
	String SIGNIN = "/api/User/SignIn";
	/**
	 * 第三方登录
	 */
	String AddUserThirdInfo = "/api/Account/AddUserThirdInfo";
	String ThirdLogin = "/api/Account/ThirdLogin";
	String ThirdRegister = "/api/Account/ThirdRegister";

	/**
	 * 更新名片模版
	 */
	String UpdateTemplate = "/api/User/UpdateTemplate";
	/**
	 * 账号登陆验证Token
	 */
	String CHECK_TOKEN = "/api/Account/LoginTokenChange";
	/**
	 * POST 修改账户身份信息
	 */
	String USER_CHANGE_IDENTITY = "/api/User/IdentityChange";
	/**
	 * 解绑第三方登录
	 */
	String CANCEL_THIRDINFO = "/api/Account/CancelUserThirdInfo";
	/**
	 * 账号登陆POST
	 */
	String LOGING = "/api/Account/Login";
	/**
	 * 手机注册POST
	 */
	String MOBILE_REGISTER = "/api/Account/MobileRegister";
	/**
	 * POST 重置密码
	 */
	String RESET_PASSWORD = "/api/Account/ResetPassword";
	/**
	 * 获取设计师及账户信息GET
	 */
	String GET_DESIGNER = "/api/User/GetUserInfoDesigner?";
	String GET_DESIGNER_INFO = "/api/User/GetUserInfoDesigner?UserGUID=";
	String GET_DESIGNER_INFO_ID = "/api/User/GetUserInfoDesigner?DesignerID=";

	/**
	 * 获取个人及账户信息
	 */
	String GET_PERSONAL_INFO = "/api/User/GetUserInfoPersonal?UserGUID=";
	/**
	 * 获取账户信息GET
	 */
	String GET_USER_INFO = "/api/User/GetUserInfo?UserGUID=";
	/**
	 * POST 修改个人信息
	 */
	String UPDATE_PERSON_INFO = "/api/User/UpdatePersonInfo";

	/**
	 * 手机号绑定 POST
	 */
	String AddUpdateUserBindingMobile = "/api/Account/AddUpdateUserBindingMobile";	
	/**
	 * 建龙更换手机绑定
	 */
	String UPDATEBINDINGMOBILE = "/api/Account/UpdateBindingMobile";

	/**
	 * 修改设计师信息POST
	 */
	String UPDATE_DESIGNER_INFO = "/api/User/UpdateDesignerInfo";
	/**
	 * 修改头像
	 */
	String UPLOAD_LOGO = "/api/User/UpdateLogo";
	/**
	 * 身份认证POST
	 */
	String CardAuthentication = "/api/HouseOrder/CardAuthentication";

	/**
	 * 验证密码
	 */
	String CHECKUSERPWD = "/api/user/CheckUserPwd";

	/**
	 * 收藏POST 关注 ClassType=0 1 2 3 4 5 7 8/CASE NEWS PRODUCTS ACTIVITYORDER
	 * PERSONAL DESIGNER
	 */
	String FAVORITE_ADD = "/api/Favorite/Add";

	/**
	 * 取消收藏POST
	 */
	String FAVORITE_CANCEL = "/api/Favorite/Cancel";
	/**
	 * POST 是否收藏关注 ,ClassType 0=CASE;1=NEWS;5=DESIGNER;UserGUID, FromGUID
	 */
	String IS_Favorite = "/api/Favorite/IsFavorite";
	/**
	 * 根据条件获取收藏设计师列表
	 */
	String GET_DESIGNER_LIST = "/api/Favorite/GetDesignerList?UserGuid=";
	/**
	 * 根据条件获取关注装修公司列表
	 */
	String GET_BUSUINESS_LIST = "/api/Favorite/GetBusinessList?UserGuid=";
	/**
	 * 根据条件获取设计师粉丝列表GET
	 */
	String GET_FANSLIST = "/api/Favorite/GetDesignerFansList?";

	/**
	 * 根据条件获取个人用户粉丝列表
	 */
	String GET_USER_FANSLIST = "/api/Favorite/GetPersonerFansList?";
	/**
	 * 根据条件获取访客列表 GET(谁看了我)
	 */
	String GET_VISIT_LIST = "/api/Visit/GetVisitList?PageSize=10&Client=2004&UserGuid=";
	/**
	 * 根据条件获取访客列表 GET(我看了谁)
	 */
	String GET_ME_VISIT = "/api/Visit/GetUserVisitList?PageSize=10&Client=2004&UserGuid=";
	/**
	 * 添加访客 POST
	 */
	String ADD_VISIT = "/api/Visit/Add";

	/**
	 * 设计师被分享增加
	 */
	String UPDATEDESIGN = "/api/Designer/UpdateDesignerShareNum?guid=";

	/**
	 * 案例被分享增加
	 */
	String UPDATECASE = "/api/Case/UpdateCaseShareNum?ID=";

	/**
	 * 发送手机验证码GET
	 */
	String SEND_SMS_CODE = "/api/Sms/SendSmsCode?";
	/**
	 * 修改号码发送手机验证码GET
	 */
	String SEND_SMS_CODE_MO = "/api/Sms/SendSmsCode?mobile=";
	/**
	 * 验证修改手机号码的验证码是否正确
	 */
	String SENDSMSCODE_MOBI = "/api/user/VerifyMobile";
	/**
	 * 
	 * POST
	 */
	String SET_PASSWORD = "/api/Account/ResetOldPassword";
	/**
	 * 发送手机验证码GET MobileVerfyCode验证码
	 */
	/**
	 * 手机验证码快速登陆POST
	 */
	String MOBILE_LOGIN = "/api/Account/MobileLogin";

	/**
	 * 验证手机号是否可用
	 */
	String VERIFY_USERNAME = "/api/Account/VerifyUserName?UserName=";
	/**
	 * 发送手机验证码GET MREG手机注册模板
	 */
	String SEND_SMS_CODE_MREG = "/api/Sms/SendSmsCode?&template=SheJiQuanRegister&mobile=";
	/**
	 * 发送手机验证码GET MLOGIN手机登陆模板
	 */
	String SEND_SMS_CODE_MLOGIN = "/api/Sms/SendSmsCode?&template=MLOGIN&mobile=";
	/**
	 * 发送手机验证码GET 重置
	 */
	String SEND_SMS_RESET_PASSWORD = "/api/Sms/SendSmsCode?&template=SheJiQuanCode&mobile=";

	/**
	 * 上传文件POST
	 */
	String UPLOAD_FILE = "/api/Interaction/UploadFile";
	
	/**
	 * 用户提交商家企业执照认证
	 */
	String UPDATE_BUSINESS = "/v3/Business/UpdateBusinessCertification";
	/***
	 * 错误日志
	 */
	String ERROR_LOG = "/api/Logger/LoggerAppError";
	
	/**
	 * POST 评论 Title Contents ,FromID=
	 */
	String GETALLCOMMENTLIST = "/api/Comment/GetAllCommentList?UserGuid=";
	/**
	 *  收到的评论
	 */
	String GETCOMMENTUSERLIST= "/api/Comment/GetCommentUserList?UserGuid=";	
	/**
	 *  发出的评论
	 */
	String GETCOMMENTSEARCHLIST= "/api/Comment/GetCommentSearchList?UserGuid=";
	/**
	 * 获取消息评论接口
	 */
	String COMMENT_SEND = "/api/Comment/Send";
	/**
	 * 根据条件搜索消息列表 GET
	 */
	String COMMENT_DESIGNER_LIST = "/api/Comment/Search?ClassType=5&FromGuid=";
	String COMMENT_LIST = "/api/Comment/Search?";
	/**
	 * 建议反馈POST
	 */
	String FEEDBACK = "/api/FeedBack/Send";
	// ************************委托单*******************

	/**
	 * POST 发布委托单到大厅（ExpectDesigner不为空发布给指定的设计师
	 */
	String ADD_HOUSE_ORDER = "/api/HouseOrderDelegation/AddHouseOrderDelegation";

	/**
	 * 搜索委托单
	 */
	String ORDER_SEARCH_LIST = "/api/HouseOrderDelegation/SearchHouseOrderList?";
	/**
	 * api/HouseOrder/SearchHouseOrderList? 新订单查询
	 */
	String SearchHouseOrderList = "/api/HouseOrder/SearchHouseOrderList?";
	/**
	 * GET 获取施工图，效果图，平面布置图
	 */
	String GetOrderDesignerFile = "/api/HouseOrder/GetOrderDesignerFile?OrderGuid=";
	/**
	 * GET 根据GUID获取委托单详情
	 */
	String HouseOrderInfo = "/api/HouseOrderDelegation/HouseOrderInfo?";

	/**
	 * 修改委托状态流程POST
	 */
	String ORDER_CHANGE_STATUS = "/api/HouseOrderDelegation/ChangeDelegationStatus";
	/**
	 * 派单的设计师列表
	 */
	String SEND_DESIGNER = "/api/HouseOrderApply/SendDesignerList?";// OrderGUID={OrderGUID}
	/**
	 * 设计师接单
	 */
	String DesignerApplyOrder = "/api/HouseOrderApply/DesignerApplyOrder?";
	/**
	 * 设计师是否接单
	 */
	String DesignerChoiceOrder = "/api/HouseOrderApply/DesignerChoiceOrder?";// OrderGuid={OrderGuid}&DesignerGuid={DesignerGuid}&MarkStatus={MarkStatus}"
	/**
	 * 业主选择设计师
	 */
	String PersonChoiceDesigner = "/api/HouseOrderDelegation/PersonChoiceDesigner?";
	/**
	 * 获取委托(LoggingType=1)/订单日志状态(LoggingType=2)
	 */
	String GetLoggingList = "/api/HouseOrderDelegation/GetLoggingList?";
	/**
	 * 业主对设计师提交作品的意见/投诉/评价设计师 POST CommentType OrderGuid 订单Guid UserGUID 业主的GUID
	 * 
	 * DesignerGuid 设计师Guid
	 * 
	 * Remark 选择项
	 * 
	 * Content 内容
	 */
	String PersonSubmitAgreed = "/api/HouseOrder/PersonSubmitAgreed";
	/**
	 * GET UserGuid={UserGuid}&OrderGuid={OrderGuid}&CommentType={CommentType}&
	 * PageIndex={PageIndex}&PageSize={PageSize}
	 */
	String PersonAgreedList = "/api/HouseOrder/PersonAgreedList?";
	/**
	 * 图库
	 */
	String GetHomePictureList = "/api/Case/GetHomePictureList?";
	/**
	 * APP首页滚动委托单信息 GET
	 */
	String RollDelegationList = "/api/HouseOrderDelegation/RollDelegationList";
	/**
	 * 
	 * 查看评价
	 */
	String SeeEvaluate = "/api/HouseOrder/SeeEvaluate?";
	/**
	 * 上传订单文件POST
	 */
	String UPLOAD_FILE_ORDER = "/api/Interaction/UploadOrderFile";
	/**
	 * GET删除图片
	 */
	String DeleteDelegationFile = "/api/HouseOrderDelegation/DeleteDelegationFile?FileGuid=";
	/**
	 * 读取委托单业主上传文件 Get OrderGuid={OrderGuid}&UserGuid={UserGuid}
	 */
	String GetOrderDelegationFileItem = "/api/HouseOrderDelegation/GetOrderDelegationFileItem?";
	String GetOrderDelegationFile = "/api/HouseOrderDelegation/GetOrderDelegationFile?";
	// ************************订单*******************

	/**
	 * GET 设计师我申请的订单列表
	 */
	String GetOrderApplyList = "/api/HouseOrderApply/GetOrderApplyList?";
	/**
	 * 改变订单状态
	 */
	String ChangeOrderStatus = "/api/HouseOrder/ChangeOrderStatus";
	/**
	 * POST 业主验收订单
	 */
	String PersonCheckOrder = "/api/HouseOrder/PersonCheckOrder";
	/**
	 * 业主获取订单详情GET
	 */
	String GetDetailsOrderFromPerson = "/api/HouseOrder/GetDetailsOrderFromPerson?OrderGuid=";// {OrderGuid}
	/**
	 * 设计师读取订单详情GET
	 */
	String GetDetailsOrderFromDesigner = "/api/HouseOrder/GetDetailsOrderFromDesigner?OrderGuid=";// {OrderGuid}

	/**
	 * 业主恢复订单 ?OrderGuid={OrderGuid}&UserGuid={UserGuid}
	 */

	String PersonRestOrder = "/api/HouseOrder/PersonRestOrder?OrderGuid=";

	/**
	 * GET 获取订单分配的设计师列表（候选设计师）
	 */
	String GetAssignDesigner = "/api/HouseOrder/GetAssignDesigner?";

	/**
	 * POST 给设计师发送站内消息
	 */
	String SendDesignMessage = "/api/HouseOrder/SendDesignMessage";
	/**
	 * 随机推荐设计师
	 */
	String ORDER_RANDOM = "/api/HouseOrder/RecommendedDesignerList?";
	/**
	 * POST 业主更换设计师
	 */
	String OrderChangeDesigner = "/api/HouseOrder/OrderChangeDesigner";

	/**
	 * GET 获取对设计师完成委托后的评论详情
	 */
	String GET_DESIGNER_COMMENT = "/api/HouseOrder/GetDesignerComment?";
	/**
	 * POST呼叫
	 */
	String MakeCallCenter = "/api/HouseOrder/MakeCallCenter";

	/**
	 * GET UserGuid={UserGuid}获取设计师/业主未读订单
	 */
	String GetHouseOrderLoggerList = "/api/HouseOrder/GetHouseOrderLoggerList?";

	/**
	 * 设计师同意业主申请取消订单?OrderGuid={OrderGuid}&DesignerGuid={DesignerGuid}
	 */
	String DesignerAgreeCancelOrder = "/api/HouseOrder/DesignerAgreeCancelOrder?OrderGuid=";

	/**
	 * POST 发布招标
	 */
	String ACTIVITY_ADD = "/api/Activity/Add";
	/**
	 * GET api/Advert/Detail?Id={Id}广告详情
	 */
	String AddDetail = "/api/Advert/Detail?Id=";
	/**
	 * 获取用户委托GET
	 */
	String GET_ENTRUST_LIST = "/api/Activity/GetEntrustList?DesignerGUID=";
	/**
	 * 获取用户评论GET
	 */
	String COMMENT_USER = "/api/Comment/GetUserList?UserGuid=";
	/**
	 * 招标详情GET
	 */
	String ACTIVITY_DETAIL = "/api/Activity/Detail?ActivityGUID=";// 订单GUID

	/**
	 * 获取是否有未读消息/订单消息GET
	 */
	String GetIsNoReadMessage = "/api/HouseOrder/GetIsNoReadMessage?";

	/**
	 * 搜索订单
	 */
	String ORDER_SEARCH_DESIGN_SQ_LIST = "/api/HouseOrderApply/GetOrderApplyList?";
	/**
	 * GET获取订单流程日志
	 */
	String ORDER_GET_LOGGER = "/api/HouseOrder/GetOrderLoggerList?";

	/**
	 * 根据条件搜索消息列表GET
	 */
	String MessageSearch = "/api/Message/Search?";
	/**
	 * 获取系统未读消息
	 */
	String ORDER_GetMessageList = "/api/HouseOrder/GetMessageList?";
	/**
	 * POST更改，清空（订单、消息）的状态为删除状态 -1
	 */
	String UpdateMessageStatus = "/api/HouseOrder/UpdateMessageStatus";
	/**
	 * POST 更改订单为已读,
	 * UserGUID\UpdateType更改设计师的未读订单（UpdateType==Designer），更改业主的未读订单
	 * （UpdateType==Person）
	 */
	String ChangeOrderIsRead = "/api/HouseOrder/ChangeOrderIsRead";
	/**
	 * GET 更改订单为已读
	 */
	String UpdateMessageRead = "/api/HouseOrder/UpdateMessageRead?";
	/**
	 * GET 更改评论为已读
	 */
	String UpdateCommendRead = "/api/Comment/UpdateCommendRead?userGuid=";
	/**
	 * 新更改未读的订单消息为已读GET
	 */
	String UpdateAllOrderIsRead = "/api/HouseOrder/UpdateAllOrderIsRead?UserGUID=";

	/**
	 * 发送站内消息和发送短信POST
	 */
	String SendUserMessage = "/api/HouseOrder/SendUserMessage";

	// ************************推广页*******************
	/**
	 * 获取热门城市
	 */
	String CITY_GET_HOT = "/api/Config/OpenAreaList";
	/**
	 * 下载app
	 */
	String SHARE_SHEJIQUAN = "http://a.app.qq.com/o/simple.jsp?pkgname=com.baolinetworktechnology.shejiquan";
	/**
	 * 招标
	 */
	// String APP_DESIGNER =
	// "http://m.sjq315.com/activity/android/app/index.html";
	/**
	 * 更换设计师
	 */
	String PersonUpdateDesigner = "/api/HouseOrder/PersonUpdateDesigner?OrderGuid=";

	/**
	 * 我的积分
	 */
	String GETSIGNINList= "/v3/UserCenter/GetSigninList?";
	/**
	 * http://testm.sjq315.com/
	 */

	/**
	 * 获取设计师咨询数
	 */
	String GETCONSULNUM= "/V3/Designer/GetConsultNum?DesignerGuid=";

	/**
	 * 设计师咨询添加
	 */
	String SAVECONSULT= "/V3/Designer/SaveConsult?";
}
