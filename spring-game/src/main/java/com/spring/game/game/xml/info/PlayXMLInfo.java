package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

public class PlayXMLInfo {

	public static final int PLAY_TYPE_1 = 1;// 宝石活动

	private int no;// 活动编号
	private int resetTimes;// 每天可重置次数 -1-无次数限
	private int challengeTimes;// 挑战次数

	// 关卡信息
	private int fristBattleNo;// 第一关
	private int lastBattleNo;// 最后一关
	private Map<Integer, PlayXMLBattle> battles = new HashMap<Integer, PlayXMLBattle>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getResetTimes() {
		return resetTimes;
	}

	public void setResetTimes(int resetTimes) {
		this.resetTimes = resetTimes;
	}

	public int getChallengeTimes() {
		return challengeTimes;
	}

	public void setChallengeTimes(int challengeTimes) {
		this.challengeTimes = challengeTimes;
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

	public Map<Integer, PlayXMLBattle> getBattles() {
		return battles;
	}

	public void setBattles(Map<Integer, PlayXMLBattle> battles) {
		this.battles = battles;
	}
}
