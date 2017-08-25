package com.snail.webgame.game.xml.info;

/**
 * 等级礼包
 * 
 * @author nijp
 * 
 */
public class LevelGiftXMLInfo {

	private int no;
	private int needLv;// 等级要求
	private String prizeNo;// 掉落库no

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNeedLv() {
		return needLv;
	}

	public void setNeedLv(int needLv) {
		this.needLv = needLv;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo;
	}

}
