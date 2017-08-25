package com.snail.webgame.game.xml.info;

public class OnlineGiftXMLInfo {

	private int no;
	private int needOnlineTime;// 需要在线时间(单位:分)
	private String prizeNo;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNeedOnlineTime() {
		return needOnlineTime;
	}

	public void setNeedOnlineTime(int needOnlineTime) {
		this.needOnlineTime = needOnlineTime;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo;
	}

}
