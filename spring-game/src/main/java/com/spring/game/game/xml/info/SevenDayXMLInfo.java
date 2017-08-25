package com.snail.webgame.game.xml.info;


public class SevenDayXMLInfo {
	
	/**
	 * 7日活动类型 1 每日登陆  2 累计充值  3 半价礼包
	 */
	public static final int SEVEN_TYPE_1 = 1;
	public static final int SEVEN_TYPE_2 = 2;
	public static final int SEVEN_TYPE_3 = 3;
	
	private int day;
	private int subNo;
	private int type;
	private int needGold;
	private String prizeNo;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getSubNo() {
		return subNo;
	}

	public void setSubNo(int subNo) {
		this.subNo = subNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNeedGold() {
		return needGold;
	}

	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo;
	}

}
