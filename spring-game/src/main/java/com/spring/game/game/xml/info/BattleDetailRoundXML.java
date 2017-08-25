package com.snail.webgame.game.xml.info;

import java.util.HashMap;

public class BattleDetailRoundXML {

	private int round;// 第几回合
	// <roundType,BattleDetailRoundTypeXML>
	private HashMap<Integer, BattleDetailRoundTypeXML> roundTypMap;

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public HashMap<Integer, BattleDetailRoundTypeXML> getRoundTypMap() {
		return roundTypMap;
	}

	public void setRoundTypMap(HashMap<Integer, BattleDetailRoundTypeXML> roundTypMap) {
		this.roundTypMap = roundTypMap;
	}
}
