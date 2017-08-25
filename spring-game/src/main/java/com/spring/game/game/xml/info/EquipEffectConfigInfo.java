package com.snail.webgame.game.xml.info;

public class EquipEffectConfigInfo {
	private int type; // 1武器,2盔甲,3头盔,4护腕,5披风,6战靴
	private int hp;
	private int attack;
	private int magicAttack;
	private int attackDef;
	private int magicDef;
	private int ad;// 普通攻击

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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
		return ad;
	}

	public void setAd(int ad) {
		this.ad = ad;
	}

}
