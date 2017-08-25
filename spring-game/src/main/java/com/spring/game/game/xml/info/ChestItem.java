package com.snail.webgame.game.xml.info;

/**
 * 宝箱内物品
 * @author caowl
 *
 */
public class ChestItem {

	private String itemNo;	//物品No
	private int itemNum;	//物品Num
	private int rand;			//概率
	private int type; //对应XML中父标签No
	
	public ChestItem(String itemNo, int itemNum, int rand, int type) {
		this.itemNo = itemNo;
		this.itemNum = itemNum;
		this.rand = rand;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public int getRand() {
		return rand;
	}

	public void setRand(int rand) {
		this.rand = rand;
	}
}
