package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

public class ArenaXMLBuy {

	private int no;// 购买次数
	// 购买条件
	private List<AbstractConditionCheck> conditions = new ArrayList<AbstractConditionCheck>();
	private int fightNum;// 购买战斗次数

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public List<AbstractConditionCheck> getConditions() {
		return conditions;
	}

	public void setConditions(List<AbstractConditionCheck> conditions) {
		this.conditions = conditions;
	}

	public int getFightNum() {
		return fightNum;
	}

	public void setFightNum(int fightNum) {
		this.fightNum = fightNum;
	}
}
