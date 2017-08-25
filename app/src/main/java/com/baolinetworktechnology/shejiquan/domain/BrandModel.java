package com.baolinetworktechnology.shejiquan.domain;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;

import java.util.List;

public class BrandModel {
  private int id;
  private String guid;
  private String brandLogo;
  private String brandName;
  private String brandIntro;
  private String brandSlogan;
  private List<brandSidle> brandSidle;
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

  public String getBrandLogo() {
    return brandLogo;
  }

  public void setBrandLogo(String brandLogo) {
    this.brandLogo = brandLogo;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getBrandIntro() {
    return brandIntro;
  }

  public void setBrandIntro(String brandIntro) {
    this.brandIntro = brandIntro;
  }

  public String getBrandSlogan() {
    return brandSlogan;
  }

  public void setBrandSlogan(String brandSlogan) {
    this.brandSlogan = brandSlogan;
  }

  public List<brandSidle> getBrandSidle() {
    return brandSidle;
  }

  public void setBrandSidle(List<brandSidle> brandSidle) {
    this.brandSidle = brandSidle;
  }
}
