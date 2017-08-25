package com.baolinetworktechnology.shejiquan.domain;


/**
 * 订单日志
 * 
 * @author JiSheng.Guo
 * 
 */
public class OrderLog {
	private String ID, GUID, CreateTime, CreateUser, Title, Contents,
			DesignerGUID, orderNumber,FromGUID,MarkName;
	private String IsRead;
	private int MarkStatus;
	private int IsComments;
	private int LoggingType;
	
	public int getLoggingType() {
		return LoggingType;
	}

	public void setLoggingType(int loggingType) {
		LoggingType = loggingType;
	}

	// private int MarkStatus;
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	/**
	 * 创建人
	 * 
	 */
	public String getCreateUser() {
		return CreateUser;
	}

	public void setCreateUser(String createUser) {
		CreateUser = createUser;
	}

	/**
	 * 标题
	 * 
	 * @return
	 */
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	/**
	 * 内容
	 * 
	 * @param contents
	 */
	public String getContents() {
		return Contents;
	}

	/**
	 * 内容
	 * 
	 * @param contents
	 */
	public void setContents(String contents) {
		Contents = contents;
	}

	/**
	 * 设计师/业主ID
	 * 
	 * @return
	 */
	public String getDesignerGUID() {
		return DesignerGUID;
	}

	public void setDesignerGUID(String designerGUID) {
		DesignerGUID = designerGUID;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * 委托编号
	 * @param orderNumber
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * 是否被查阅
	 * 
	 * @return
	 */
	public String getIsRead() {
		return IsRead;
	}

	/**
	 * 0未
	 * 1已
	 * @param isRead
	 */
	public void setIsRead(String isRead) {
		IsRead = isRead;
	}

	/**
	 * 日志状态
	 * 
	 * @see IOrderMarkStatus
	 * @return
	 */
	public int getMarkStatus() {
		return MarkStatus;
	}

	public void setMarkStatus(int markStatus) {
		MarkStatus = markStatus;
	}

	/**
	 * 订单GUID
	 * @return
	 */
	public String getFromGUID() {
		return FromGUID;
	}

	public void setFromGUID(String fromGUID) {
		FromGUID = fromGUID;
	}

	public int getIsComments() {
		return IsComments;
	}

	public void setIsComments(int isComments) {
		IsComments = isComments;
	}
	public String getMarkName() {
		return MarkName;
	}

	public void setMarkName(String markName) {
		MarkName = markName;
	}


}
