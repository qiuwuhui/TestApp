package com.baolinetworktechnology.shejiquan.domain;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;

import java.util.List;

public class SwDesignerDetail {
  public int id;
  public String guid;
  public String name;
  public String mobile;
  public String sex;
  public String  workCompany;
  public String blogName;
  public String profession;
  public String logo;
  public String gisLng;
  public String gisLat;
  public String school;
  public String workYear;
  public String address;
  public String desStyle;
  public String desStyleName;
  public String desArea;
  public String desAreaName;
  public String greetings;
  public int provinceID;
  public int cityID;
  public int areaID;
  public int isCertification;
  public String comment;
  public String cost;
  public int costId;
  public String designing;
  public String liveCity;
  public String serviceStatus;
  private int numShare;
  public int numFavorite;
  public int numComment;
  public int numFans;
  public int numNew;
  public int numCase;
  public int numConcer;
  public String officeTime;
  public Double starLevel;
  public String style;
  private int numVisit;
  public int hitNum;
  public int numOrder;
  public int serviceLevel;
  public String fromCityName;
  public String cityName;
  public String renzheng;
	public String getCost() {
		if (TextUtils.isEmpty(cost) || "不限".equals(cost))
          cost = "面议";
		String str=cost.substring(cost.length() - 2);
		if(str.equals("m2")|| str.equals("m²")){
			StringBuffer buffer=new StringBuffer();
            buffer.append("¥ ");
			buffer.append(cost.substring(0,cost.length() - 2));
			buffer.append("㎡");
			cost=buffer.toString();
		}
		return cost;
	}
    public int getNumCase() {
        if (numCase < 0)
            numCase = 0;
        return numCase;
    }
    public int getNumShare() {
        if (numShare < 0)
            numShare = 0;
        return numShare;
    }
    public int getNumFans() {
        if (numFans < 0)
            numFans = 0;
        return numFans;
    }
  public String getRenzheng() {
    if(isCertification==0){
      return renzheng="未认证";
    }else if(isCertification==1){
      return renzheng="未认证";
    }else if(isCertification==2){
      return renzheng="审核中";
    }else if(isCertification==3){
      return renzheng="已认证";
    }
    return renzheng;
  }
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBlogName() {
    return blogName;
  }

  public void setBlogName(String blogName) {
    this.blogName = blogName;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getGisLng() {
    return gisLng;
  }

  public void setGisLng(String gisLng) {
    this.gisLng = gisLng;
  }

  public String getGisLat() {
    return gisLat;
  }

  public void setGisLat(String gisLat) {
    this.gisLat = gisLat;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
  }

  public String getWorkYear() {
    return workYear;
  }

  public void setWorkYear(String workYear) {
    this.workYear = workYear;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDesStyle() {
    return desStyle;
  }

  public void setDesStyle(String desStyle) {
    this.desStyle = desStyle;
  }

  public String getDesArea() {
    return desArea;
  }

  public void setDesArea(String desArea) {
    this.desArea = desArea;
  }

  public int getProvinceID() {
    return provinceID;
  }

  public void setProvinceID(int provinceID) {
    this.provinceID = provinceID;
  }

  public int getCityID() {
    return cityID;
  }

  public void setCityID(int cityID) {
    this.cityID = cityID;
  }

  public int getAreaID() {
    return areaID;
  }

  public void setAreaID(int areaID) {
    this.areaID = areaID;
  }

  public int getIsCertification() {
    return isCertification;
  }

  public void setIsCertification(int isCertification) {
    this.isCertification = isCertification;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setCost(String cost) {
    this.cost = cost;
  }

  public int getCostId() {
    return costId;
  }

  public void setCostId(int costId) {
    this.costId = costId;
  }

  public String getDesigning() {
    return designing;
  }

  public void setDesigning(String designing) {
    this.designing = designing;
  }

  public String getLiveCity() {
    return liveCity;
  }

  public void setLiveCity(String liveCity) {
    this.liveCity = liveCity;
  }

  public String getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(String serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public void setNumShare(int numShare) {
    this.numShare = numShare;
  }

  public int getNumView() {
    return hitNum;
  }

  public void setNumView(int numView) {
    this.hitNum = numView;
  }

  public int getNumFavorite() {
    return numFavorite;
  }

  public void setNumFavorite(int numFavorite) {
    this.numFavorite = numFavorite;
  }

  public int getNumComment() {
    return numComment;
  }

  public void setNumComment(int numComment) {
    this.numComment = numComment;
  }

  public void setNumFans(int numFans) {
    this.numFans = numFans;
  }

  public int getNumNew() {
    return numNew;
  }

  public void setNumNew(int numNew) {
    this.numNew = numNew;
  }

  public void setNumCase(int numCase) {
    this.numCase = numCase;
  }

  public int getNumConcer() {
    return numConcer;
  }

  public void setNumConcer(int numConcer) {
    this.numConcer = numConcer;
  }

  public String getOfficeTime() {
    return officeTime;
  }

  public void setOfficeTime(String officeTime) {
    this.officeTime = officeTime;
  }

  public Double getStarLevel() {
    return starLevel;
  }

  public void setStarLevel(Double starLevel) {
    this.starLevel = starLevel;
  }

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public int getNumVisit() {
    return numVisit;
  }

  public void setNumVisit(int numVisit) {
    this.numVisit = numVisit;
  }

  public int getHitNum() {
    return hitNum;
  }

  public void setHitNum(int hitNum) {
    this.hitNum = hitNum;
  }

  public int getNumOrder() {
    return numOrder;
  }

  public void setNumOrder(int numOrder) {
    this.numOrder = numOrder;
  }

  public int getServiceLevel() {
    return serviceLevel;
  }

  public void setServiceLevel(int serviceLevel) {
    this.serviceLevel = serviceLevel;
  }

  public String getFromCityName() {
    return fromCityName;
  }

  public void setFromCityName(String fromCityName) {
    this.fromCityName = fromCityName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public void setRenzheng(String renzheng) {
    this.renzheng = renzheng;
  }
  public String getWorkCompany() {
    return workCompany;
  }
  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }
  public void setWorkCompany(String workCompany) {
    this.workCompany = workCompany;
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
    if (TextUtils.isEmpty(desStyle)) {
      return "未填写";
    } else {
      String[] s = desStyle.split(",");
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
  //获取擅长领域
  public  String getStrArea(Context mContex){
    List<CaseClass> list;
    if (SJQApp.getClassMap() != null) {
      list = SJQApp.getClassMap().getList("擅长空间");
    } else {
      list = new GetClassList(mContex).getList("擅长空间");
    }
    if (list == null)
      return "未填写";
    if (TextUtils.isEmpty(desArea)) {
      return "未填写";
    } else {
      String[] s = desArea.split(",");
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
