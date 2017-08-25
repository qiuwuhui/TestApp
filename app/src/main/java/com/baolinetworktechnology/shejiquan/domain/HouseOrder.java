package com.baolinetworktechnology.shejiquan.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.interfaces.IEntrustType;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.view.TextSwitcherView.ISwitcher;
import com.google.gson.Gson;

/**
 * 订单
 * 
 * @author JiSheng.Guo
 * 
 */
public class HouseOrder implements ISwitcher {
	int ID;// 订单_ID
	int Sex;// 性别,0，女，1男
	int IsComments;// 是否有评论（1-有评论，0-未评论）
	int HouseArea, HouseType, IsApplyOrder;

	/**
	 * 大于0 已经接单
	 * 
	 * @return
	 */
	public int getIsApplyOrder() {
		return IsApplyOrder;
	}

	public void setIsApplyOrder(int isApplyOrder) {
		IsApplyOrder = isApplyOrder;
	}

	int ProvinceID, CityID, AreaID;// 省、城、县
	int IsCertification;
	int DesignerProvinceId, DesignerCityId;
	int MarkStatus;
	String Addres, HouseTypeTitle = "", HouseAreaTitle = "", Remark,
			ApplyDesigner,UpdateTime, BespeakTime, Mobile,
			UserGUID, UserLogo;

	public String getUserLogo() {
		return UserLogo;
	}

	public void setUserLogo(String userLogo) {
		UserLogo = userLogo;
	}

	String Designerlogo, DesignerName, DesignerMobile, DesignerCost, FromCity;

	int ServiceLevel;

	public int getServiceStatus() {
		return ServiceLevel;
	}

	public void setServiceStatus(int serviceStatus) {
		ServiceLevel = serviceStatus;
	}

	String ExpectDesigner;// 期望设计师GUID
	String EntrustType, OrderId;// 装修类型
	String GUID;// 订单GUID;
	String CreateTime;// 创建时间
	String CreateUser, Name;// 发布者
	String DesignerMarkStatus;
	String OverTime;
	int IsOpinion;

	// private ArrayList<OrderDesigner> designerInfos;

	public int getIsOpinion() {
		return IsOpinion;
	}

	public void setIsOpinion(int isOpinion) {
		IsOpinion = isOpinion;
	}

	public String getOverTime() {
		return OverTime;
	}

	public void setOverTime(String overTime) {
		OverTime = overTime;
	}

	public int getDesignerMarkStatu() {
		int i = 0;
		try {
			i = Integer.parseInt(DesignerMarkStatus);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return i;

	}

	public void setDesignerMarkStatu(int i) {
		DesignerMarkStatus = i + "";

	}

	ArrayList<OrderDesigner> designerInfos;

	public ArrayList<OrderDesigner> getDesignerInfos() {
		return designerInfos;
	}

	/**
	 * （1-有评论，0-未评论）
	 * 
	 * @return
	 */
	public int getIsComments() {
		return IsComments;
	}

	/**
	 * （1-有评论，0-未评论）
	 * 
	 * @param isComments
	 */
	public void setIsComments(int isComments) {
		IsComments = isComments;
	}

	/**
	 * 业主GUID
	 * 
	 * @return
	 */
	public String getUserGUID() {
		return UserGUID;
	}

	/**
	 * 签到合同的设计师GUID
	 * 
	 * @return
	 */
	public String getApplyDesigner() {
		return ApplyDesigner;
	}

	public void setApplyDesigner(String applyDesigner) {
		ApplyDesigner = applyDesigner;
	}

	public void setUserGUID(String userGUID) {
		UserGUID = userGUID;
	}

	public int getDesignerProvinceId() {
		return DesignerProvinceId;
	}

	public void setDesignerProvinceId(int designerProvinceId) {
		DesignerProvinceId = designerProvinceId;
	}

	public int getDesignerCityId() {
		return DesignerCityId;
	}

	public void setDesignerCityId(int designerCityId) {
		DesignerCityId = designerCityId;
	}

	/**
	 * 业主电话
	 * 
	 * @return
	 */
	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	/**
	 * 量房时间
	 * 
	 * @return
	 */
	public String getBespeakTime() {

		return BespeakTime;
	}

	public void setBespeakTime(String bespeakTime) {
		BespeakTime = bespeakTime;
	}

	/**
	 * 修改时间
	 * 
	 * @return String
	 */
	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}


	/**
	 * 备注
	 * 
	 * @return
	 */
	public String getRemark() {
		if (TextUtils.isEmpty(Remark))
			Remark = "【暂无】";
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	/**
	 * 设计类型
	 * 
	 * @param context
	 * @return
	 */
	public String getHouseTypeTitle(Context context) {
		if (TextUtils.isEmpty(HouseTypeTitle)) {
			List<CaseClass> list;
			if (SJQApp.getClassMap() != null) {
				list = SJQApp.getClassMap().getList("设计类型");
			} else {
				list = new GetClassList(context).getList("设计类型");
			}
			List<CaseClass> list1;
			if (SJQApp.getClassMap() != null) {
				list1 = SJQApp.getClassMap().getList("擅长空间");
			} else {
				list1 = new GetClassList(context).getList("擅长空间");
			}
			if (list == null)
				return "未填写";
			for (int i = 0; i < list1.size(); i++) {

				if (HouseType == list1.get(i).id) {
					HouseTypeTitle = list1.get(i).title;
					return HouseTypeTitle;
				}
			}
			if (list == null)
				return "未填写";
			for (int i = 0; i < list.size(); i++) {

				if (HouseType == list.get(i).id) {
					HouseTypeTitle = list.get(i).title;
					return HouseTypeTitle;
				}
			}

			HouseTypeTitle = "未填写";
		}
		return HouseTypeTitle;
	}

	/**
	 * 中文-面积
	 * 
	 * @param context
	 * @return
	 */
	public String getHouseAreaTitle(Context context) {

		if (TextUtils.isEmpty(HouseAreaTitle)) {
			List<CaseClass> list;
			if (SJQApp.getClassMap() != null) {
				list = SJQApp.getClassMap().getList("面积");
			} else {
				list = new GetClassList(context).getList("面积");
			}
			if (list == null)
				return "未填写";
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).id == HouseArea) {
					HouseAreaTitle = list.get(i).title;
					return HouseAreaTitle;
				}
			}
			HouseAreaTitle = "未填写";
		}
		return HouseAreaTitle;
	}

	/**
	 * 业主名字
	 * 
	 * @return
	 */
	public String getCreateUser() {
		return CreateUser;
	}

	public void setCreateUser(String createUser) {
		CreateUser = createUser;
	}

	public int getHouseType() {
		return HouseType;
	}

	public void setHouseType(int houseType) {
		HouseType = houseType;
	}

	/**
	 * 订单状态
	 * 
	 * @return
	 */
	public int getMarkStatus() {
		return MarkStatus;
	}

	public void setMarkStatus(int markStatus) {
		MarkStatus = markStatus;
	}

	public String getDesignerlogo() {
		return Designerlogo;
	}

	public void setDesignerlogo(String designerlogo) {
		Designerlogo = designerlogo;
	}

	public String getDesignerName() {

		return DesignerName;
	}

	public void setDesignerName(String designerName) {
		DesignerName = designerName;
	}

	public String getDesignerMobile() {
		return DesignerMobile;
	}

	public void setDesignerMobile(String designerMobile) {
		DesignerMobile = designerMobile;
	}

	public String getDesignerCost() {
		if (TextUtils.isEmpty(DesignerCost)) {
			return "面议";
		}

		return DesignerCost;
	}

	public void setDesignerCost(String designerCost) {
		DesignerCost = designerCost;
	}

	/**
	 * 中文-地址
	 * 
	 * @param context
	 * @return
	 */
	public String getFromCity(Context context) {
		if (TextUtils.isEmpty(FromCity)) {
			StringBuffer sb = new StringBuffer();
			City city1 = CityService.getInstance(context).getCityDB()
					.getCityID(DesignerProvinceId + "");

			City city2 = CityService.getInstance(context).getCityDB()
					.getCityID(DesignerCityId + "");

			if (city1 != null) {
				sb.append(city1.Title);
			}

			if (city2 != null) {
				sb.append("-" + city2.Title);
			}

			FromCity = sb.toString();
		}
		return FromCity;
	}

	public void setFromCity(String fromCity) {
		FromCity = fromCity;
	}

	public int getIsCertification() {
		return IsCertification;
	}

	public void setIsCertification(int isCertification) {
		IsCertification = isCertification;
	}

	public String getAddres() {
		return Addres;
	}

	/**
	 * 中文-地址
	 * 
	 * @param context
	 * @return
	 */
	public String getAddres(Context context) {
		if (TextUtils.isEmpty(Addres)) {
			if (CityService.getInstance(context) == null
					|| CityService.getInstance(context).getCityDB() == null)
				return "";
			StringBuffer sb = new StringBuffer();
			City city1 = CityService.getInstance(context).getCityDB()
					.getCityID(ProvinceID + "");

			City city2 = CityService.getInstance(context).getCityDB()
					.getCityID(CityID + "");

			City city3 = CityService.getInstance(context).getCityDB()
					.getCityID(AreaID + "");

			if (city1 != null) {
				if (!"全国".equals(city1.Title))
					sb.append(city1.Title);
			}

			if (city2 != null) {
				sb.append(city2.Title);
			}
			if (city3 != null) {
				if (AreaID != CityID)
					sb.append(city3.Title);
			}
			Addres = sb.toString();
			if (TextUtils.isEmpty(Addres)) {
				return "未填写";
			}
		}
		return Addres;
	}

	String cityTitle;

	public String getCityTitle(Context context) {
		if (CityService.getInstance(context) == null
				|| CityService.getInstance(context).getCityDB() == null)
			return "";
		if (TextUtils.isEmpty(cityTitle)) {
			City city2 = CityService.getInstance(context).getCityDB()
					.getCityID(CityID + "");

			if (city2 != null) {
				cityTitle = city2.Title;
			} else {
				cityTitle = "";
			}
		}
		return cityTitle;
	}

	public void setAddres(String addres) {
		Addres = addres;
	}

	public int getHouseArea() {
		return HouseArea;
	}

	public void setHouseArea(int houseArea) {
		HouseArea = houseArea;
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

	public void setSex(int sex) {
		Sex = sex;
	}

	public int getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(int provinceID) {
		ProvinceID = provinceID;
	}

	public int getCityID() {
		return CityID;
	}

	public void setCityID(int cityID) {
		CityID = cityID;
	}

	public int getAreaID() {
		return AreaID;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	/**
	 * 
	 * 
	 * @return 订单GUID
	 */
	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	/**
	 * 创建日期
	 * 
	 * @return
	 */
	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	/**
	 * 发布者
	 * 
	 * @return
	 */
	public String getName() {
		if (TextUtils.isEmpty(Name)) {
			Name = CreateUser;
		}

		return Name;
	}

	public void setName(String name) {
		CreateUser = name;
	}

	/**
	 * 设计师GUID
	 * 
	 * @return
	 */
	public String getExpectDesigner() {
		if (ExpectDesigner == null || ExpectDesigner.length() < 2) {
			ExpectDesigner = ApplyDesigner;
		}
		return ExpectDesigner;
	}

	public void setExpectDesigner(String expectDesigner) {
		ExpectDesigner = expectDesigner;
	}

	public String getDecorateTypeWay() {
		return EntrustType;
	}

	public String getDecorateTypeWayTitle() {
		if ((IEntrustType.DESINGER + "").equals(EntrustType)) {
			return "设计";
		}
		return "设计装修";
	}

	public void setDecorateTypeWay(String decorateType) {
		EntrustType = decorateType;
	}

	/**
	 * 订单编号
	 * 
	 * @return
	 */
	public String getOrderId() {
		return OrderId;
	}

	String OrderGUID;

	public String getOrderGUID() {
		return OrderGUID;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}

	String Title;

	@Override
	public String getTitle() {
		if (TextUtils.isEmpty(Title)) {
		}
		return Title;
	}

	String Budget, BudgetTitle;

	/**
	 * 预算
	 * 
	 * @param context
	 * @return
	 */
	public String getBudgetTitle(Context context) {
		if (TextUtils.isEmpty(BudgetTitle)) {
			List<CaseClass> list;
			if (SJQApp.getClassMap() != null) {
				list = SJQApp.getClassMap().getList("预算");
			} else {
				list = new GetClassList(context).getList("预算");
			}
			if (list == null || TextUtils.isEmpty(Budget))
				return "暂无";
			int BudgetID = -11;
			try {
				BudgetID = Integer.parseInt(Budget);
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (BudgetID == -11) {
				return "暂无";
			}

			for (int i = 0; i < list.size(); i++) {
				if (BudgetID == list.get(i).id) {
					BudgetTitle = list.get(i).title;
					return BudgetTitle;
				}
			}
			BudgetTitle = "未填写";
		}
		return BudgetTitle;
	}

	public String getLoveStyleTitle(Context context, String DecorateStyle) {
		List<CaseClass> list;
		if (SJQApp.getClassMap() != null) {
			list = SJQApp.getClassMap().getList("风格");
		} else {	
			list = new GetClassList(context).getList("风格");
		}
		if (list == null)
			return "未填写";
		if (TextUtils.isEmpty(DecorateStyle)) {
			return "未填写";
		} else {
			String[] s = DecorateStyle.split(",");
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < s.length; j++) {
				for (int i = 0; i < list.size(); i++) {
					if (Integer.valueOf(s[j]).intValue() == list.get(i).id) {
						buffer.append(list.get(i).title).append(" ");
						break;
					}
				}
			}
			return buffer.toString();
		}
	}

	String ApartmentAddress;// 小区地址
	String Apartment;// 小区名称
	String DecorateStyle;// 喜欢风格

	public String getDecorateStyle() {
		return DecorateStyle;
	}

	public void setDecorateStyle(String decorateStyle) {
		DecorateStyle = decorateStyle;
	}

	public String Content;

	/**
	 * 小区地址
	 * 
	 * @return 小区地址
	 */
	public String getApartmentAddress() {
		if (TextUtils.isEmpty(ApartmentAddress))
			return "未填写";
		return ApartmentAddress;
	}

	public void setApartment(String adres) {
		Apartment = adres;
	}

	public void setApartmentAddress(String adres) {
		ApartmentAddress = adres;
	}

	/**
	 * 
	 * @return 小区名称
	 */
	public String getApartment() {
		if (TextUtils.isEmpty(Apartment))
			return "未填写";
		return Apartment;
	}

	public String getContent() {
		if (Content == null || "".equals(Content))
			return "暂无";
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	String CommentRemark;

	public String getCommentRemark() {
		if (CommentRemark == null)
			return "";
		return CommentRemark;
	}
}
