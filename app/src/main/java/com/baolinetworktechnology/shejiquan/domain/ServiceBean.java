package com.baolinetworktechnology.shejiquan.domain;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ServiceBean {
    private Service Service;

    public ServiceBean.Service getService() {
        return Service;
    }

    public void setService(ServiceBean.Service service) {
        Service = service;
    }
    public class Service{
      private String Phone;
      private String QQ;

      public String getPhone() {
          return Phone;
      }

      public void setPhone(String phone) {
          Phone = phone;
      }

      public String getQQ() {
          return QQ;
      }

      public void setQQ(String QQ) {
          this.QQ = QQ;
      }
    }
}
