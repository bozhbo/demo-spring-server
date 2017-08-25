package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

public class CampaignXMLInfo {

	public static final int CAMPAIGN_TYPE_1 = 1;// 宝物活动

	private int no;// 活动编号
	

	// 关卡信息
	private int fristBattleNo;// 第一关
	private int lastBattleNo;// 最后一关
	private Map<Integer, CampaignXMLBattle> battles = new HashMap<Integer, CampaignXMLBattle>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getFristBattleNo() {
		return fristBattleNo;
	}

	public void setFristBattleNo(int fristBattleNo) {
		this.fristBattleNo = fristBattleNo;
	}

	public int getLastBattleNo() {
		return lastBattleNo;
	}

	public void setLastBattleNo(int lastBattleNo) {
		this.lastBattleNo = lastBattleNo;
	}

	public Map<Integer, CampaignXMLBattle> getBattles() {
		return battles;
	}

	public void setBattles(Map<Integer, CampaignXMLBattle> battles) {
		this.battles = battles;
	}
}
