package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

import java.util.List;

public class DetailsModel {
  private int id;
  private String title;
  private String contents;
  private String images;
  private List<paramsMoel> params;
  private List<productItem> productItem;

  public List<productItem> getProductItem() {
    return productItem;
  }

  public void setProductItem(List<productItem> productItem) {
    this.productItem = productItem;
  }
  public List<paramsMoel> getParams() {
    return params;
  }

  public void setParams(List<paramsMoel> params) {
    this.params = params;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public String getImages() {
    return images;
  }

  public void setImages(String images) {
    this.images = images;
  }

  //图片剪切
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
    if (images != null && images.length() > 5) {
      String temp = images.substring(images.length() - 6);
      String[] tems = temp.split("\\.");
      if (tems != null && tems.length > 1) {
        String suffix = tems[1];

        return images.replace("." + suffix, clip) + suffix;

      } else {
        return images;
      }
    } else {
      return images;
    }
  }
}
