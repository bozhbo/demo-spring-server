package com.snail.webgame.game.xml.info;

import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

public class GoldBuyXMLPro {
	private int no;// 购买次数
	private int gold;	//金子数量
	// 前置条件
	private List<AbstractConditionCheck> conditions;

	private int gain;// 获取值
	private int mulRandNo;// 倍数暴击概率编号

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public List<AbstractConditionCheck> getConditions() {
		return conditions;
	}

	public void setConditions(List<AbstractConditionCheck> conditions) {
		this.conditions = conditions;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}

	public int getMulRandNo() {
		return mulRandNo;
	}

	public void setMulRandNo(int mulRandNo) {
		this.mulRandNo = mulRandNo;
	}
}
