package com.snail.webgame.game.xml.info;

/**
 * 神兵的各个技能等级以及升级消耗
 * 
 * @author xiasd
 * 
 */
public class WeaponLv {
	private int hp;// 血量
	private int attack;// 物理攻击
	private int Ad;// 普通攻击
	private int magicAttack;// 法术攻击
	private int attackDef;// 物理防御
	private int magicDef;// 法术防御

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int getAttackDef() {
		return attackDef;
	}

	public void setAttackDef(int attackDef) {
		this.attackDef = attackDef;
	}

	public int getMagicDef() {
		return magicDef;
	}

	public void setMagicDef(int magicDef) {
		this.magicDef = magicDef;
	}

	public int getAd() {
		return Ad;
	}

	public void setAd(int ad) {
		Ad = ad;
	}

}
