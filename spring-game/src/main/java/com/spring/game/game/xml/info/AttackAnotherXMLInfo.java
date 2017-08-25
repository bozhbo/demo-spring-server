package com.snail.webgame.game.xml.info;

/**
 * 对攻战配置信息
 * @author wanglinhui
 *
 */
public class AttackAnotherXMLInfo {
	
	private int no;
	private int minLevel;//最低等级
	private int maxLevel;//最高等级
	private String easyBag;//掉落的物品
	private String NormalBag;//掉落的物品
	private String hardBag;//掉落的物品
	
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getEasyBag() {
		return easyBag;
	}
	public void setEasyBag(String easyBag) {
		this.easyBag = easyBag;
	}
	public String getNormalBag() {
		return NormalBag;
	}
	public void setNormalBag(String normalBag) {
		NormalBag = normalBag;
	}
	public String getHardBag() {
		return hardBag;
	}
	public void setHardBag(String hardBag) {
		this.hardBag = hardBag;
	}
	public int getMinLevel() {
		return minLevel;
	}
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

}
