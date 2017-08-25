package com.snail.webgame.game.xml.info;


public class TeamChallengeXmlInfo {
	
	/**
	 * 关卡编号
	 */
	private int no;
	/**
	 * 最低等级
	 */
	private int lv;
	/**
	 * 荣耀等级
	 */
	private int maxLv;
	/**
	 * 每日活动奖励次数
	 */
	private int times;
	/**
	 * 战斗时间
	 */
	private int endTime;
	/**
	 * 掉落
	 */
	private String bag;
	
	/**
	 * 副本名称
	 */
	private String name;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getMaxLv() {
		return maxLv;
	}
	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public String getBag() {
		return bag;
	}
	public void setBag(String bag) {
		this.bag = bag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
