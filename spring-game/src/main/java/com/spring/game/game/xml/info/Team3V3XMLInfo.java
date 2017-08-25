package com.snail.webgame.game.xml.info;

public class Team3V3XMLInfo {
	private static String victoryBag; // 胜利奖励
	private static String failureBag;	// 失败奖励
	private static String drawBag; // 平局奖励
	private static String cardBag; // 胜利翻牌
	
	public static String getVictoryBag() {
		return victoryBag;
	}
	public static void setVictoryBag(String victoryBag) {
		Team3V3XMLInfo.victoryBag = victoryBag;
	}
	public static String getFailureBag() {
		return failureBag;
	}
	public static void setFailureBag(String failureBag) {
		Team3V3XMLInfo.failureBag = failureBag;
	}
	public static String getDrawBag() {
		return drawBag;
	}
	public static void setDrawBag(String drawBag) {
		Team3V3XMLInfo.drawBag = drawBag;
	}
	public static String getCardBag() {
		return cardBag;
	}
	public static void setCardBag(String cardBag) {
		Team3V3XMLInfo.cardBag = cardBag;
	}
	
	
}
