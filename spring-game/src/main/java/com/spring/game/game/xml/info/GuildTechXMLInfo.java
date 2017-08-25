package com.snail.webgame.game.xml.info;

import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

public class GuildTechXMLInfo {
	private int no;
	private String name;
	private int lv;
	private int buildType;
	private int needType;
	private int needLv;
	private int offical;
	private int addType;
	private int addNum;
	private int cost; //公会扩容时使用
	private List<AbstractConditionCheck> conditions;
	
	public List<AbstractConditionCheck> getConditions() {
		return conditions;
	}

	public void setConditions(List<AbstractConditionCheck> conditions) {
		this.conditions = conditions;
	}

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

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getNeedType() {
		return needType;
	}

	public void setNeedType(int needType) {
		this.needType = needType;
	}

	public int getNeedLv() {
		return needLv;
	}

	public void setNeedLv(int needLv) {
		this.needLv = needLv;
	}

	public int getOffical() {
		return offical;
	}

	public void setOffical(int offical) {
		this.offical = offical;
	}

	public int getAddType() {
		return addType;
	}

	public void setAddType(int addType) {
		this.addType = addType;
	}

	public int getAddNum() {
		return addNum;
	}

	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
