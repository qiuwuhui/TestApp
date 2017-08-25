package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;
/**
 * 资讯详情
 * @author Administrator
 *
 */
public class NewsDetailBen extends Bean {

	public Data data;


	public class Data {
		public News NewsDetail;
		public List<News> RelatedList;

	}

	
}
