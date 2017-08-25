package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

public class WonderXMLInfo {

	/**
	 * 精彩活动类型
	 */
	public static final int WONDER_TYPE_CHARGE = 1;
	public static final int WONDER_TYPE_COST = 2;
	public static final int WONDER_TYPE_PLAN = 3;
	
	private int no;
	private int wonderType;// 精彩活动类型 1-累计充值 2-累计消费 3-投资计划
	private int goal;// 目标值
	private String prizeNo;
	
	List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getWonderType() {
		return wonderType;
	}

	public void setWonderType(int wonderType) {
		this.wonderType = wonderType;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo;
	}

	public List<AbstractConditionCheck> getConds() {
		return conds;
	}

	public void setConds(List<AbstractConditionCheck> conds) {
		this.conds = conds;
	}

}
