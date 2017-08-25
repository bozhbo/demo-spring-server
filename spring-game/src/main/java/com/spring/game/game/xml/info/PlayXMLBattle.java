package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

public class PlayXMLBattle {

	private int no;// 关编号
	private int battleCondition;// 前置副本ID
	private String dropBag;// 掉落包
	private String caseDropBag;// 通关的宝箱奖励，为空则该关卡通关无奖励
	// 活动怪信息
	private List<String> gwNos = new ArrayList<String>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getBattleCondition() {
		return battleCondition;
	}

	public void setBattleCondition(int battleCondition) {
		this.battleCondition = battleCondition;
	}

	public String getDropBag() {
		return dropBag;
	}

	public void setDropBag(String dropBag) {
		this.dropBag = dropBag;
	}

	public String getCaseDropBag() {
		return caseDropBag;
	}

	public void setCaseDropBag(String caseDropBag) {
		this.caseDropBag = caseDropBag;
	}

	public List<String> getGwNos() {
		return gwNos;
	}

	public void setGwNos(List<String> gwNos) {
		this.gwNos = gwNos;
	}
}
