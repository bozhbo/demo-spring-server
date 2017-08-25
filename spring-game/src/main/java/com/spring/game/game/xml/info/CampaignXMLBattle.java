package com.snail.webgame.game.xml.info;

public class CampaignXMLBattle {

	private int no;// 关编号
	private String name;// 关卡名称
	private String dropBag;// 掉落包
	private String caseDropBag;// 通关的宝箱奖励，为空则该关卡通关无奖励
	private String cardBag;// 翻牌掉落包
	private int lvMoney;// 额外银子奖励=玩家等级*系数

	private int min;// 战斗力下线
	private int max;// 战斗力上限
	private float ave;// 无敌人时镜像的战斗力

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDropBag() {
		return dropBag;
	}

	public void setDropBag(String dropBag) {
		this.dropBag = dropBag;
	}

	public String getCaseDropBag() {
		return caseDropBag;
	}

	public void setCaseDropBag(String caseDropBag) {
		this.caseDropBag = caseDropBag;
	}

	public String getCardBag() {
		return cardBag;
	}

	public void setCardBag(String cardBag) {
		this.cardBag = cardBag;
	}

	public int getLvMoney() {
		return lvMoney;
	}

	public void setLvMoney(int lvMoney) {
		this.lvMoney = lvMoney;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public float getAve() {
		return ave;
	}

	public void setAve(float ave) {
		this.ave = ave;
	}
}
