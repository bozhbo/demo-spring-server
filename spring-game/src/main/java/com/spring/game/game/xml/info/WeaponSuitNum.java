package com.snail.webgame.game.xml.info;

/**
 * 神兵套装 MagicSuit.xml
 * 
 * @author xiasd
 * 
 */
public class WeaponSuitNum {
	private byte num;
	private byte type;//101-附加技能 Other heroProType
	private String effect;

	public byte getNum() {
		return num;
	}

	public void setNum(byte num) {
		this.num = num;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

}
