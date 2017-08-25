package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class OrderDesigner {
	private int ID, Sex, DesignerProvinceId, DesignerCityId, IsCertification,ProvinceID,CityID,ServiceLevel;
	private String GUID, DesignerCost, DesignerMobile,
			DesignerName, Designerlogo, DesignerGUID,Logo,Name,Cost;
	private String FromCity;

	
	
	
	
	
	
	public String getDesignerGUID() {
		if(TextUtils.isEmpty(DesignerGUID))
			return GUID;
		return DesignerGUID;
	}

	public void setDesignerGUID(String designerGUID) {
		DesignerGUID = designerGUID;
	}

	public int getDesignerProvinceId() {
		if(DesignerProvinceId==0)
			return ProvinceID;
		return DesignerProvinceId;
	}

	public void setDesignerProvinceId(int designerProvinceId) {
		DesignerProvinceId = designerProvinceId;
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

	public int getDesignerCityId() {
		if(DesignerCityId==0)
			return CityID;
		return DesignerCityId;
	}

	public void setDesignerCityId(int designerCityId) {
		DesignerCityId = designerCityId;
	}

	public int getIsCertification() {
		return IsCertification;
	}

	public void setIsCertification(int isCertification) {
		IsCertification = isCertification;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}


	public int getServiceStatus() {
		return ServiceLevel;
	}

	public void setServiceStatus(int serviceStatus) {
		ServiceLevel = serviceStatus;
	}

	public String getDesignerCost() {
		if(TextUtils.isEmpty(DesignerCost))
			return Cost;
		return DesignerCost;
	}

	public void setDesignerCost(String designerCost) {
		DesignerCost = designerCost;
	}

	public String getDesignerMobile() {
		return DesignerMobile;
	}

	public void setDesignerMobile(String designerMobile) {
		DesignerMobile = designerMobile;
	}

	public String getDesignerName() {
		if(TextUtils.isEmpty(DesignerName))
			return Name;
		return DesignerName;
	}

	public void setDesignerName(String designerName) {
	
		DesignerName = designerName;
	}

	public String getDesignerlogo() {
		if(TextUtils.isEmpty(Designerlogo))
			return Logo;
		return Designerlogo;
	}

	public void setDesignerlogo(String designerlogo) {
		
		Designerlogo = designerlogo;
	}

	public String getFromCity() {
		return FromCity;
	}

	public void setFromCity(String fromCity) {
		FromCity = fromCity;
	}

}
