package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import android.text.TextUtils;

/**
 * 业主
 * 
 * @author JiSheng.Guo
 * 
 */
public class OwnerDetail extends Bean {
	private String Name;// 设计师名称
	private String Logo, FromCity;// 头像
	private int ID, CityID;// 设计师_ID
	private int Sex;
	// private String SexTitle;
	private String DecorState;//  装修状态
	private String Phone, Email, QQ, WeiXin, Mobile;
	private String School;// 毕业院校
	private String LiveCity;// 现居住地
	private String ServiceStatus;// 服务状态
	private String ServiceBegin;// 服务起始时间
	private String ServiceEnd;// 服务截止时间
	private int ProvinceID;
	private boolean IsCertification;
	private int NProvinceID;//装修资料省ID
	private int NCityID;//装修资料市ID
	private String HouseArea;//房屋面积
	private String Budget;//装修预算

	private String HouseType;//房屋类型
	private String LoveStyleTitle;//喜欢的装修风格文字
	private String LoveStyle;// 喜欢的装修风格ID

	public String getHouseType() {
		return HouseType;
	}

	public void setHouseType(String houseType) {
		HouseType = houseType;
	}

	public String getBudget() {
		return Budget;
	}

	public void setBudget(String budget) {
		Budget = budget;
	}

	public String getHouseArea() {
		return HouseArea;
	}

	public void setHouseArea(String houseArea) {
		HouseArea = houseArea;
	}

	public int getNCityID() {
		return NCityID;
	}

	public void setNCityID(int NCityID) {
		this.NCityID = NCityID;
	}

	public int getNProvinceID() {
		return NProvinceID;
	}

	public void setNProvinceID(int NProvinceID) {
		this.NProvinceID = NProvinceID;
	}


	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getFromCity() {
		return FromCity;
	}

	public void setFromCity(String fromCity) {
		FromCity = fromCity;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(String sex) {

		if ("0".equals(sex) || "女".equals(sex)) {
			Sex = 0;
		} else {
			Sex = 1;
		}

	}

	public int getCityID() {
		return CityID;
	}

	public void setCityID(int cityID) {
		CityID = cityID;
	}

	public String getPhone() {
		if (TextUtils.isEmpty(Phone)) {
			return Mobile;
		}
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getWeiXin() {
		return WeiXin;
	}

	public void setWeiXin(String weiXin) {
		WeiXin = weiXin;
	}

	public String getSchool() {
		return School;
	}

	public void setSchool(String school) {
		School = school;
	}

	public String getLiveCity() {
		return LiveCity;
	}

	public void setLiveCity(String liveCity) {
		LiveCity = liveCity;
	}

	public String getServiceStatus() {
		return ServiceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		ServiceStatus = serviceStatus;
	}

	public String getServiceBegin() {
		return ServiceBegin;
	}

	public void setServiceBegin(String serviceBegin) {
		ServiceBegin = serviceBegin;
	}

	public String getServiceEnd() {
		return ServiceEnd;
	}

	public void setServiceEnd(String serviceEnd) {
		ServiceEnd = serviceEnd;
	}

	public String getSexTitle() {
		if (0 == Sex) {
			return "女";
		} else {
			return "男";
		}

	}

	public String getMobile() {
		if(TextUtils.isEmpty(Mobile))
			return Phone;
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getLoveStyleTitle(List<CaseClass> classList) {
		if (!TextUtils.isEmpty(LoveStyleTitle)) {
			return LoveStyleTitle;
		}
		if(classList==null)
			return "";
		if (LoveStyle != null) {
			String[] datas = null;
			if (LoveStyle.indexOf(",") != -1) {
				datas = LoveStyle.split(",");
			}
			if (datas != null && datas.length >= 1) {
				StringBuffer title = new StringBuffer();
				for (int i = 0; i < datas.length; i++) {
					for (int j = 0; j < classList.size(); j++) {
						if (datas[i].equals(classList.get(j).id + "")) {
							title.append(classList.get(j).title);
							if (i != datas.length - 1) {
								title.append(",");
							}
							continue;
						}

					}
				}
				LoveStyleTitle = title.toString();
				return LoveStyleTitle;
			} else {
				for (int j = 0; j < classList.size(); j++) {
					if (LoveStyle.equals(classList.get(j).id + "")) {
						LoveStyleTitle = classList.get(j).title;
						return LoveStyleTitle;
					}

				}
			}

		}
		return "";
	}

	public void setLoveStyleTitle(String loveStyleTitle) {
		LoveStyleTitle = loveStyleTitle;
	}

	public String getLoveStyle() {
		return LoveStyle;
	}

	public void setLoveStyle(String loveStyle) {
		LoveStyle = loveStyle;
	}

	public String getDecorStateTitle() {
		if (DecorState == null)
			DecorState = "0";
		if (DecorState.equals("0")) {
			return "";

		}
		if ("1".equals(DecorState) || "NoHouse".equals(DecorState)) {
			return "还没买房";

		}
		if (DecorState.equals("2") || "NoPay".equals(DecorState)) {
			return "还没交房";
		}
		if (DecorState.equals("3") || "ReadyDecor".equals(DecorState)) {
			return "准备开始装修";
		}
		if (DecorState.equals("4") || "DecorIng".equals(DecorState)) {
			return "装修进行中";
		}
		if (DecorState.equals("5") || "DecorEnd".equals(DecorState)) {
			return "装修完成";
		}
		if (DecorState.equals("6") || "CheckIn".equals(DecorState)) {
			return "已入住新房";
		}
		if (DecorState.equals("7") || "OldHouse".equals(DecorState)) {
			return "老房准备改造";
		}
		return DecorState;
	}

	public String getDecorState() {
		if (TextUtils.isEmpty(DecorState)) {
			return "0";
		}
		return DecorState;
	}

	public void setDecorState(String decorState) {
		DecorState = decorState;
	}

	public int getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(int provinceID) {
		ProvinceID = provinceID;
	}

	public int getIsCertification() {
		if (IsCertification) {
			return 1;
		} else {
			return 0;
		}

	}

	public void setIsCertification(boolean isCertification) {
		IsCertification = isCertification;
	}

	// public String OfficeTime;// 从业时间
	// public UserInfo User;//账户对象
	// public int IsComment;//是否允许评论
	// public String WebUrl;

}
