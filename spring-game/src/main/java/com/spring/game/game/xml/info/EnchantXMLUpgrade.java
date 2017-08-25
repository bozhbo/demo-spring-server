package com.snail.webgame.game.xml.info;

public class EnchantXMLUpgrade {

	private int no;// 等级
	private int enchantNum;// 需要消耗的附魔能量
	private int money;// 需要消耗的银子
	private int level;// 主将等级限制

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getEnchantNum() {
		return enchantNum;
	}

	public void setEnchantNum(int enchantNum) {
		this.enchantNum = enchantNum;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
