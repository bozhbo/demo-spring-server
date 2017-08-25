package com.snail.webgame.game.xml.info;


public class CheckInPrizeXMLInfo {
	private int no; //
	private String bagNoStr;// 奖励库no
	
	private String vipBagNoStr;// vip签到奖励
	private int needVipLv;// vipLv条件 99-表示当前无vip奖励

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getBagNoStr() {
		return bagNoStr;
	}

	public void setBagNoStr(String bagNoStr) {
		this.bagNoStr = bagNoStr;
	}

	public String getVipBagNoStr() {
		return vipBagNoStr;
	}

	public void setVipBagNoStr(String vipBagNoStr) {
		this.vipBagNoStr = vipBagNoStr;
	}

	public int getNeedVipLv() {
		return needVipLv;
	}

	public void setNeedVipLv(int needVipLv) {
		this.needVipLv = needVipLv;
	}

}
