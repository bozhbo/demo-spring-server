package com.snail.webgame.game.xml.info;

public class FixLotteryConfigInfo {
	private int no; //第几次固定奖励
	private int itemNo; // 物品ID
	private int num; // 物品数量

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

}
