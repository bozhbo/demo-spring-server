package com.snail.webgame.game.xml.info;

/**
 * 金币活动配置文件类
 * 
 * @author xiasd
 *
 */
public class MoneyActivity {
	private int level;// 等级
	private int reward;// boss奖励的经验

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

}
