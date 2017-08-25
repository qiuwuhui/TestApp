package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * 添加访客记录
 * @author JiSheng.Guo
 *
 */
public class NetAddRecord extends BaseNet {
	/**
	 * 添加访客记录
	 * @param designGuid 访客的设计师Guid
	 */
	public NetAddRecord(String designGuid) {
		if (SJQApp.user != null) {
			RequestParams params = getParams(ApiUrl.ADD_VISIT);
//			if(AppTag.MARKNAME_DESIGNER.equals(SJQApp.user.MarkName)){
//				params.addBodyParameter("ClassType", "100");
//			}else{
//				params.addBodyParameter("ClassType", "0");
//			}
			params.addBodyParameter("ClassType", "100");//设计师
			params.addBodyParameter("UserGUID", designGuid);
			params.addBodyParameter("Visitor", SJQApp.user.guid);
			params.addBodyParameter("Client", AppTag.CLIENT);
			if (!SJQApp.user.guid.equals(designGuid))//不是自己
				getHttpUtils().send(HttpMethod.POST,
						ApiUrl.API + ApiUrl.ADD_VISIT, params, null);
		}
	}
}
