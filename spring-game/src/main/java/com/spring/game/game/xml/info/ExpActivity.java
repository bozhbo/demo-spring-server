package com.snail.webgame.game.xml.info;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;

/**
 * 经验活动配置文件类
 * @author xiasd
 */
public class ExpActivity {
	private int level;
	private String name;
	private int quantity;
	private int bossReward;// boss奖励的经验
	private int npcReward;// 小怪奖励的经验
	private String itemNo;// 掉落的道具编号
	private String bag;//掉落
	
	
	//怪点位
	private HashMap<Integer,BattleDetailPoint> pointsMap = new HashMap<Integer,BattleDetailPoint>();

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getBossReward() {
		return bossReward;
	}

	public void setBossReward(int bossReward) {
		this.bossReward = bossReward;
	}

	public int getNpcReward() {
		return npcReward;
	}

	public void setNpcReward(int npcReward) {
		this.npcReward = npcReward;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public HashMap<Integer, BattleDetailPoint> getPointsMap() {
		return pointsMap;
	}

	public void setPointsMap(HashMap<Integer, BattleDetailPoint> pointsMap) {
		this.pointsMap = pointsMap;
	}

	public String getBag() {
		return bag;
	}

	public void setBag(String bag) {
		this.bag = bag;
	}

}
