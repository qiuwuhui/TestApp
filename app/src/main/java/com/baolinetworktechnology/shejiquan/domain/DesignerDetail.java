package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.google.gson.Gson;

import android.content.Context;
import android.text.TextUtils;

public class DesignerDetail {
	public String BlogName;
	public String Phone;   //电话
	public String Mobile;  //电话
	public String Email;   //邮箱
	public String Fax;   // 传真
	public String School;  //学校
	public String Address;  //工作地址
	public String DesStyleTitle; ;// 擅长风格
	public int IsComment; //是否允许评论
	public int NumShare;  //分享数
	public String Comment;  //个人简介
	public String Designing;  // 设计理念
	public String LiveCity;   // 现居住地
	public String ServiceStatus; // 服务状态
	public String ServiceBegin;  // 服务起始时间
	public String ServiceEnd;   // 服务截止时间
	public String OfficeTime;   // 从业时间
	public String WebUrl;      //个人主页网络地址
	public String LoginName;	//登录账号
	public String NickName;  //聊天昵称
	public String UserLogo;  //聊天头像
	public String Template;
	public int ID;             //设计师ID
	public String GUID;        //设计师GUID
	public String CreateTime;  //创建时间
	public String WorkYear;    // 工作年限
	public String Sex;         //性别
	public int ProvinceID;     //省Id
	public int CityID;         //市ID
	public int AreaID;         //区DI
	public String Name;        //设计师姓名
	public String Logo;		 //设计师头像
	public String Profession;  //职业身份
	public int NumFavorite;    // 收藏数量
	public int NumComment;     // 评论统计数量
	public int NumView;        // 查阅次数
	public int NumCase;        // 案例数
	public int NumFans;        // 粉丝统计数量
	public int NumConcer;      // 关注数
	public String GisLng;      // 经度
	public String GisLat;      // 纬度
	public double StarLevel;      // 星级
	public String FromCity;    // 城市归属
	public String Cost;        //设计费用
	public String WorkCompany;   // 就职公司
	public int IsCertification;  // 1未认证  2审核中 3认证
	public int NumVisit;      // 访客
	public String QQ;         //QQ号码
	public int NumNew;        // 案例数
	public int NumOrder;
	public String DesArea;     // 擅长领域
	public String DesStyle;    // 擅长风格
	public int ServiceLevel;   //设计等级
	public String Greetings;   //问候语
	private Integer TemplateInt;// 微名片模版类型
	private String renzheng;

	public String getRenzheng() {
		if(IsCertification==0){
			return renzheng="未认证";
		}else if(IsCertification==1){
			return renzheng="未认证";
		}else if(IsCertification==2){
			return renzheng="审核中";
		}else if(IsCertification==3){
			return renzheng="已认证";
		}
		return renzheng;
	}

	public String getUrl() {
		if (!TextUtils.isEmpty(WebUrl)) {
			Matcher compile = Pattern.compile(
					"guid=[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}")
					.matcher(WebUrl);
			if (compile != null)
				return compile.replaceAll("");
			return WebUrl;
		} else {
			return "";
		}

	}

	public Integer getTemplate() {
		if (Template == null || Template.equals("")) {
			return 2;
		}
		TemplateInt = Integer.parseInt(Template);
		if (TemplateInt == null)
			return 2;
		return TemplateInt;
	}

	public void setTemplate(Integer template) {
		if (template == null) {
			template = 2;
		}
		Template = template.intValue() + "";
		TemplateInt = template;
	}

	public String getPhone(){
		if(TextUtils.isEmpty(Phone)){
			return Mobile;

		}
		return Phone;
	}
	public int getNumCase() {
		if (NumCase < 0)
			NumCase = 0;
		return NumCase;
	}
	public int getNumShare() {
		if (NumShare < 0)
			NumShare = 0;
		return NumShare;
	}
	public int getNumComment() {
		if (NumComment < 0)
			NumComment = 0;
		return NumComment;
	}
	public int getNumFans() {
		if (NumFans < 0)
			NumFans = 0;
		return NumFans;
	}
	public int getNumConcer() {
		if (NumConcer < 0)
			NumConcer = 0;
		return NumConcer;
	}

	public Integer getNumOrder() {
		if (NumOrder < 0)
			NumOrder = 0;

		return NumOrder;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}

	public String getCost() {
		if (TextUtils.isEmpty(Cost) || "不限".equals(Cost))
			Cost = "面议";
		String str=Cost.substring(Cost.length() - 2);
		if(str.equals("m2")|| str.equals("m²")){
			StringBuffer buffer=new StringBuffer();
			buffer.append(Cost.substring(0,Cost.length() - 2));
			buffer.append("㎡");
			Cost=buffer.toString();
		}
		return Cost;
	}

	private String SmallImages;

	/**
	 * _500_250.
	 *
	 * @param clip
	 * @return
	 */
	public String getSmallImages(String clip) {
		if (TextUtils.isEmpty(SmallImages)) {
			SmallImages = getSuffix(clip);
			return SmallImages;
		}
		return SmallImages;
	}

	/**
	 * _500_250.
	 *
	 * @return
	 */
	public String getSmallImages() {

		return getSmallImages("_500_250.");
	}

	private String getSuffix(String clip) {
		if (Logo != null && Logo.length() > 5) {
			String temp = Logo.substring(Logo.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return Logo.replace("." + suffix, clip) + suffix;

			} else {
				return Logo;
			}
		} else {
			return Logo;
		}

	}

	public String getProfession() {
		return Profession;
	}

	public void setProfession(String profession) {
		Profession = profession;
	}

	public int getServiceStatus() {
		return ServiceLevel;
	}

	public void setServiceStatus(int serviceStatus) {
		ServiceLevel = serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		if (!TextUtils.isEmpty(serviceStatus)) {
			try {
				ServiceLevel = Integer.parseInt(serviceStatus);
			} catch (Exception e) {
				ServiceLevel = 0;
			}

		}
	}

	public String getStrStyle(Context mContex) {
		List<CaseClass> list;
		if (SJQApp.getClassMap() != null) {
			list = SJQApp.getClassMap().getList("风格");
		} else {
			list = new GetClassList(mContex).getList("风格");
		}
		if (list == null)
			return "未填写";
		if (TextUtils.isEmpty(DesStyle)) {
			return "未填写";
		} else {
			String[] s = DesStyle.split(",");
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < s.length; j++) {
				for (int i = 0; i < list.size(); i++) {
					if (Integer.valueOf(s[j]).intValue() == list.get(i).id) {
						if(j==s.length-1){
							buffer.append(list.get(i).title);
						}else{
							buffer.append(list.get(i).title).append("、");
						}
						break;
					}
				}
			}
			return buffer.toString();
		}
	}
}
