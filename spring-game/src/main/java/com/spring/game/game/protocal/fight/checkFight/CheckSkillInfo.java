package com.snail.webgame.game.protocal.fight.checkFight;

public class CheckSkillInfo {

	private int skillNo;
	private int skillLv;
	private long releaseTime;//释放时间
	private long hurtTime;//作用时间
	private int releaseNum;
	private long armyId;
	private long heroId;//英雄Id
	private byte status;//1-使用中 2-使用过
	
	public CheckSkillInfo(int skillNo,int releaseNum) {
		this.skillNo = skillNo;
		this.releaseNum = releaseNum;
	}
	
	public int getSkillNo() {
		return skillNo;
	}
	public void setSkillNo(int skillNo) {
		this.skillNo = skillNo;
	}
	
	public long getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

	public long getHurtTime() {
		return hurtTime;
	}

	public void setHurtTime(long hurtTime) {
		this.hurtTime = hurtTime;
	}

	public int getReleaseNum() {
		return releaseNum;
	}
	public void setReleaseNum(int releaseNum) {
		this.releaseNum = releaseNum;
	}

	public int getSkillLv() {
		return skillLv;
	}

	public void setSkillLv(int skillLv) {
		this.skillLv = skillLv;
	}

	public long getArmyId() {
		return armyId;
	}

	public void setArmyId(long armyId) {
		this.armyId = armyId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public long getHeroId() {
		return heroId;
	}

	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}
	
	

}
