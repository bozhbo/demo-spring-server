package com.snail.webgame.game.xml.info;

/**
 * 你争我夺配置
 * @author zenggang
 */
public class SnatchInfo {
	private int no; // 被合成神兵的序列编号（用于判断是不是第一个）
	private int propNo; // 被合成星石编号
	private int patchNo; // 合成星石碎片编号
	private String bag; // 抢夺胜利掉落奖励
	private String cardBag; // 抢夺胜利翻牌奖励
	private float chance;// 抢夺成功概率
	private int limit;// 抢夺最大次数,跟碎片绑定抢夺成功后清零，达到此次数，必定成功。

	public int getPropNo() {
		return propNo;
	}

	public void setPropNo(int propNo) {
		this.propNo = propNo;
	}

	public int getPatchNo() {
		return patchNo;
	}

	public void setPatchNo(int patchNo) {
		this.patchNo = patchNo;
	}

	public String getBag() {
		return bag;
	}

	public void setBag(String bag) {
		this.bag = bag;
	}

	public String getCardBag() {
		return cardBag;
	}

	public void setCardBag(String cardBag) {
		this.cardBag = cardBag;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public float getChance() {
		return chance;
	}

	public void setChance(float chance) {
		this.chance = chance;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
