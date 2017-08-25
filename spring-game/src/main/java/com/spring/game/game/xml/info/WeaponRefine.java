package com.snail.webgame.game.xml.info;

/**
 * 核心神兵
 * 
 * @author xiasd
 *
 */
public class WeaponRefine {
	private short level;// 等级
	private short mainHeroLv;// 主将等级
	private byte costWeaponsNum;// 消耗同样的神兵的数量
	private String skill;
	public short getLevel() {
		return level;
	}
	public void setLevel(short level) {
		this.level = level;
	}
	public short getMainHeroLv() {
		return mainHeroLv;
	}
	public void setMainHeroLv(short mainHeroLv) {
		this.mainHeroLv = mainHeroLv;
	}
	public byte getCostWeaponsNum() {
		return costWeaponsNum;
	}
	public void setCostWeaponsNum(byte costWeaponsNum) {
		this.costWeaponsNum = costWeaponsNum;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	
	
}
