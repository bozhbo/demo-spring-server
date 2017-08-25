package com.snail.webgame.game.xml.info;

public class DropXMLInfo {

	private String itemNo;	//物品No
	private int itemMinNum;	//物品最小数量
	private int itemMaxNum;	//物品最大数量
	private int minRand;		//物品最小概率
	private int maxRand;		//物品最大概率
	
	private int dropType;	//概率判断类型

	private String param;// 保留字段（itemNo是英雄no，param表示星数）

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public int getItemMinNum() {
		return itemMinNum;
	}

	public void setItemMinNum(int itemMinNum) {
		this.itemMinNum = itemMinNum;
	}

	public int getItemMaxNum() {
		return itemMaxNum;
	}

	public void setItemMaxNum(int itemMaxNum) {
		this.itemMaxNum = itemMaxNum;
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

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getDropType() {
		return dropType;
	}

	public void setDropType(int dropType) {
		this.dropType = dropType;
	}

}
