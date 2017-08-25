package com.snail.webgame.game.info;


public class TeamChallengeRecordSub {
	
    private String name;// 名字
    private int roleId;// 角色Id
    private int heroId;// 英雄编号
    private int heroLevel; // 英雄等级
    private long firstTime;// 首杀时间
    private long quickTime;// 速杀时间
    private int fightValue;// 战斗力

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public long getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(long firstTime) {
		this.firstTime = firstTime;
	}

	public long getQuickTime() {
		return quickTime;
	}

	public void setQuickTime(long quickTime) {
		this.quickTime = quickTime;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

}
