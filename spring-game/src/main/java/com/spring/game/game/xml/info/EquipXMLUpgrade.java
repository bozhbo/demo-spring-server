package com.snail.webgame.game.xml.info;

public class EquipXMLUpgrade {

	private int level;// 等级
	private int heroLevel;// 需要英雄等级
	private int exp;// 升级经验

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

}
