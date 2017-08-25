package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class ChallengeUpdateInfo extends BaseTO {
	
	private int fightNum;
	private int goldNum;
	private Timestamp fightTime;//该副本上次攻击时间

	public int getFightNum() {
		return fightNum;
	}

	public void setFightNum(int fightNum) {
		this.fightNum = fightNum;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public Timestamp getFightTime() {
		return fightTime;
	}

	public void setFightTime(Timestamp fightTime) {
		this.fightTime = fightTime;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}
}
