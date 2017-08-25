package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.HeroProType;

/**
 * 神兵隐藏属性
 * 
 * @author xiasd
 * 
 */
public class WeaponSpecial {
	private byte specialType;// 1-装备强化等级 2-装备精练等级
	private int specialNum;// 对应specialType,强化达到的等级或精炼达到等级，达到了，就出发效果
	private HeroProType effectType;// 同装备
	private float effectNum;// 同装备

	public byte getSpecialType() {
		return specialType;
	}

	public void setSpecialType(byte specialType) {
		this.specialType = specialType;
	}

	public int getSpecialNum() {
		return specialNum;
	}

	public void setSpecialNum(int specialNum) {
		this.specialNum = specialNum;
	}

	public HeroProType getEffectType() {
		return effectType;
	}

	public void setEffectType(HeroProType effectType) {
		this.effectType = effectType;
	}

	public float getEffectNum() {
		return effectNum;
	}

	public void setEffectNum(float effectNum) {
		this.effectNum = effectNum;
	}

}
