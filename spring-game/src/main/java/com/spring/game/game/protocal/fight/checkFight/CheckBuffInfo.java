package com.snail.webgame.game.protocal.fight.checkFight;

import com.snail.webgame.game.common.xml.info.SkillBuffFunTypeEnum;

public class CheckBuffInfo {
	
	private int buffNo;
	private long buffReleaseTime;
	private long armyId;
	private long duration;//持续时间
	
	/**
	 * buff功能枚举
	 */
	SkillBuffFunTypeEnum funEnum;
	
	private int buffLv;
	
	public int getBuffNo() {
		return buffNo;
	}
	public void setBuffNo(int buffNo) {
		this.buffNo = buffNo;
	}
	public long getBuffReleaseTime() {
		return buffReleaseTime;
	}
	public void setBuffReleaseTime(long buffReleaseTime) {
		this.buffReleaseTime = buffReleaseTime;
	}
	public long getArmyId() {
		return armyId;
	}
	public void setArmyId(long armyId) {
		this.armyId = armyId;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public SkillBuffFunTypeEnum getFunEnum() {
		return funEnum;
	}
	public void setFunEnum(SkillBuffFunTypeEnum funEnum) {
		this.funEnum = funEnum;
	}
	public int getBuffLv() {
		return buffLv;
	}
	public void setBuffLv(int buffLv) {
		this.buffLv = buffLv;
	}

	
	
	

}
