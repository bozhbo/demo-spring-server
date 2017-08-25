package com.snail.webgame.game.protocal.fight.competition.info;


public class CompetitionAgainstInfo {

	private String roleName;// 角色名称
	private int level;// 角色等级
	private byte isWin; // 是否胜利 1-胜利 otherwise-失败
	private byte militaryRank;// 段位
	
	public byte getIsWin() {
		return isWin;
	}
	public void setIsWin(byte isWin) {
		this.isWin = isWin;
	}
	public byte getMilitaryRank() {
		return militaryRank;
	}
	public void setMilitaryRank(byte militaryRank) {
		this.militaryRank = militaryRank;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
}
