package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.util.RandomUtil;

/**
 * 矿类型
 * @author zenggang
 */
public class MineXMLInfo {

	private int no;// 编号
	private String name;// 名称
	private String outType;// 产出类型 money gold
	private int outPeriod;// 产出频率（秒）
	private int outTimeAdd;// 单位产出数量（单位时间）
	private int mineTime;// （此参数作废）:矿可开采时间（分钟）
	private int newMineTime;// 可开采时间（分钟）（矿不消失，玩家可采满8/16/24小时）
	private int maxMiners;// 最大同时开采人数
	private int guardNum;// 最大防守人数

	private int guardTime;// 抢夺保护时间（分钟）（刚抢到矿的保护时间）
	private int resreshCD;// 矿位置冷却时间（分钟）

	private int maxNum;// 最大地图数量

	private int gold;// 金币掉落数量
	private int goldDropChance;// 概率 N/10000 万分之N

	// 默认防守阵形
	private String gw[];

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutType() {
		return outType;
	}

	public void setOutType(String outType) {
		this.outType = outType;
	}

	public int getOutPeriod() {
		return outPeriod;
	}

	public void setOutPeriod(int outPeriod) {
		this.outPeriod = outPeriod;
	}

	public int getOutTimeAdd() {
		return outTimeAdd;
	}

	public void setOutTimeAdd(int outTimeAdd) {
		this.outTimeAdd = outTimeAdd;
	}

	public int getMineTime() {
		return mineTime;
	}

	public void setMineTime(int mineTime) {
		this.mineTime = mineTime;
	}

	public int getNewMineTime() {
		return newMineTime;
	}

	public void setNewMineTime(int newMineTime) {
		this.newMineTime = newMineTime;
	}

	public int getMaxMiners() {
		return maxMiners;
	}

	public void setMaxMiners(int maxMiners) {
		this.maxMiners = maxMiners;
	}

	public int getGuardNum() {
		return guardNum;
	}

	public void setGuardNum(int guardNum) {
		this.guardNum = guardNum;
	}

	public int getGuardTime() {
		return guardTime;
	}

	public void setGuardTime(int guardTime) {
		this.guardTime = guardTime;
	}

	public int getResreshCD() {
		return resreshCD;
	}

	public void setResreshCD(int resreshCD) {
		this.resreshCD = resreshCD;
	}

	public int getGold() {
		return gold;
	}

	public int getGoldDropChance() {
		return goldDropChance;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getDropGold() {
		int random = RandomUtil.getRandom(0, 10000);
		if (random <= goldDropChance) {
			return gold;
		}
		return 0;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public void setGoldDropChance(int goldDropChance) {
		this.goldDropChance = goldDropChance;
	}

	public String[] getGw() {
		return gw;
	}

	public void setGw(String[] gw) {
		this.gw = gw;
	}

	public String getGWNo() {
		if (gw == null) {
			return "";
		}
		if (gw.length > 1) {
			int random = RandomUtil.getRandom(0, gw.length - 1);
			return gw[random];
		} else {
			return gw[0];
		}
	}
}
