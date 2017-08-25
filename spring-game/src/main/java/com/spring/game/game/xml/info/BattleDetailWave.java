package com.snail.webgame.game.xml.info;

public class BattleDetailWave {
	
	private int waveNo;
	private String gw;
	private int addWaveRound;//1-血量 2-时间
	private float addNextWaveNum;//AddWaveRound-1时为血量低于多少时刷下一波部队,AddWaveRound-2时为多少秒后刷下一波部队
	public int getWaveNo() {
		return waveNo;
	}
	public void setWaveNo(int waveNo) {
		this.waveNo = waveNo;
	}
	public String getGw() {
		return gw;
	}
	public void setGw(String gw) {
		this.gw = gw;
	}
	public int getAddWaveRound() {
		return addWaveRound;
	}
	public void setAddWaveRound(int addWaveRound) {
		this.addWaveRound = addWaveRound;
	}
	public float getAddNextWaveNum() {
		return addNextWaveNum;
	}
	public void setAddNextWaveNum(float addNextWaveNum) {
		this.addNextWaveNum = addNextWaveNum;
	}

	
}
