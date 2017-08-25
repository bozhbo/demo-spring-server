package com.snail.webgame.game.xml.info;

public class RecruitItemXMLInfo {
	private String itemNo;//物品No
	private int num = 1;// 默认数量1
	private int rand;	//概率

	/**
	 * 缓存字段 抽卡用
	 * 
	 * @return
	 */
	private int minRand;//最小概率
	private int maxRand;//最大概率

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public int getRand() {
		return rand;
	}

	public void setRand(int rand) {
		this.rand = rand;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getMinRand() {
		return minRand;
	}

	public void setMinRand(int minRand) {
		this.minRand = minRand;
	}

	public int getMaxRand() {
		return maxRand;
	}

	public void setMaxRand(int maxRand) {
		this.maxRand = maxRand;
	}
}
