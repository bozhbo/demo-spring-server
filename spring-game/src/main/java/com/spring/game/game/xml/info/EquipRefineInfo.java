package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.HeroProType;

public class EquipRefineInfo {
	private int level; // 精炼等级
	private int hp; // 血量
	private int attack; // 攻击力
	private int magicAttack; // 魔法攻击力
	private int attackDef; // 物理防御
	private int magicDef; // 魔法抗性
	
	private boolean addRate = false;// true:加百分比未除100,false : 加值
	private HeroProType refineType; // 精炼类型 99表示无效
	private int effect; // 效果值
	
	private int consume; // 精炼消耗数量
	private int prop; // 精炼消耗的道具ID
	private int num; // 道具ID消耗数量
	private int ad; // 普通攻击
	private int limitLv; // 等级限制
	private int demoney; //精炼消耗的银子

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public boolean isAddRate() {
		return addRate;
	}

	public void setAddRate(boolean addRate) {
		this.addRate = addRate;
	}

	public HeroProType getRefineType() {
		return refineType;
	}

	public void setRefineType(HeroProType refineType) {
		this.refineType = refineType;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	public int getConsume() {
		return consume;
	}

	public void setConsume(int consume) {
		this.consume = consume;
	}

	public int getProp() {
		return prop;
	}

	public void setProp(int prop) {
		this.prop = prop;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getAd() {
		return ad;
	}

	public void setAd(int ad) {
		this.ad = ad;
	}

	public int getLimitLv() {
		return limitLv;
	}

	public void setLimitLv(int limitLv) {
		this.limitLv = limitLv;
	}

	public int getDemoney() {
		return demoney;
	}

	public void setDemoney(int demoney) {
		this.demoney = demoney;
	}

}
