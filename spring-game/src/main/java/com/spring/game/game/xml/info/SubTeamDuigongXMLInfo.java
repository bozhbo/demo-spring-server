package com.snail.webgame.game.xml.info;

public class SubTeamDuigongXMLInfo {

	private int id;
	private int minLevel;	// 最低等级
	private int maxLevel;	// 最高等级
	private String victoryBag; // 胜利奖励
	private String failureBag;	// 失败奖励
	private String drawBag; // 平局奖励
	private String cardBag; // 胜利翻牌
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getVictoryBag() {
		return victoryBag;
	}
	public void setVictoryBag(String victoryBag) {
		this.victoryBag = victoryBag;
	}
	public String getFailureBag() {
		return failureBag;
	}
	public void setFailureBag(String failureBag) {
		this.failureBag = failureBag;
	}
	public String getDrawBag() {
		return drawBag;
	}
	public void setDrawBag(String drawBag) {
		this.drawBag = drawBag;
	}
	public String getCardBag() {
		return cardBag;
	}
	public void setCardBag(String cardBag) {
		this.cardBag = cardBag;
	}
	
}
