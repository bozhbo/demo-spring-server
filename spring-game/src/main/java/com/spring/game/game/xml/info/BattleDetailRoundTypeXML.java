package com.snail.webgame.game.xml.info;

import java.util.HashMap;

import com.snail.webgame.game.common.GameValue;

public class BattleDetailRoundTypeXML {

	private int roundType;// 本回合类型(默认1,有多个时候可供玩家选择某一模式)
							// 1.突袭敌军军营，2.从沼泽地绕行，3，从树林绕行
	
	private int roundFightType;// 回合战斗类型 1-进攻,2-防守
	
	private int endTime=GameValue.PER_FIGHT_TIME;//0 不开启时间判断, N 开启时间判断，并且判断时间为N秒
	private int endTimeType=1;//1 对方全灭 ,2 我方未全灭

	// <waveNo,GW>
	private int fristWaveNo;
	private int lastWaveNo;
	private HashMap<Integer, BattleDetailWave> waveMap;

	public int getRoundType() {
		return roundType;
	}

	public void setRoundType(int roundType) {
		this.roundType = roundType;
	}

	public int getRoundFightType() {
		return roundFightType;
	}

	public void setRoundFightType(int roundFightType) {
		this.roundFightType = roundFightType;
	}

	public int getFristWaveNo() {
		return fristWaveNo;
	}

	public void setFristWaveNo(int fristWaveNo) {
		this.fristWaveNo = fristWaveNo;
	}

	public int getLastWaveNo() {
		return lastWaveNo;
	}

	public void setLastWaveNo(int lastWaveNo) {
		this.lastWaveNo = lastWaveNo;
	}

	public HashMap<Integer, BattleDetailWave> getWaveMap() {
		return waveMap;
	}

	public void setWaveMap(HashMap<Integer, BattleDetailWave> waveMap) {
		this.waveMap = waveMap;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getEndTimeType() {
		return endTimeType;
	}

	public void setEndTimeType(int endTimeType) {
		this.endTimeType = endTimeType;
	}

}
