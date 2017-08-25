package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;


public class RecruitKindXMLInfo {

	private int no; //No 1-银子单抽 2-银子十抽 3-装备单抽 4-装备十抽 5-魂匣抽 6-限时武将 7-武将单抽 8-武将十连抽
	// 条件
	private List<AbstractConditionCheck> conditions = new ArrayList<AbstractConditionCheck>();
	private int poolNum;// 连抽次数
	private String first;// 第一次非免费抽卡库编号
	private String depotNoStr;// 抽卡掉落库编号
	private String special;// 当连抽次数大于1时有一次从该库中抽

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

	public int getPoolNum() {
		return poolNum;
	}

	public void setPoolNum(int poolNum) {
		this.poolNum = poolNum;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getDepotNoStr() {
		return depotNoStr;
	}

	public void setDepotNoStr(String depotNoStr) {
		this.depotNoStr = depotNoStr;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}
	
}
