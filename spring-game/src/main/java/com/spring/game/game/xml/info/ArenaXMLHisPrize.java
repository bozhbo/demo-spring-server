package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

/**
 * 历史最高排名提升奖励
 * @author zenggang
 */
public class ArenaXMLHisPrize {

	private int no;
	private int minPlace;// 排名区间
	private int maxPlace;// 排名区间 0-表示无限大
	private float goldParam;// 掉落参数

	private int heroNum;// 武将数量
	private Map<Byte, Integer> npcNos = new HashMap<Byte, Integer>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getMinPlace() {
		return minPlace;
	}

	public void setMinPlace(int minPlace) {
		this.minPlace = minPlace;
	}

	public int getMaxPlace() {
		return maxPlace;
	}

	public void setMaxPlace(int maxPlace) {
		this.maxPlace = maxPlace;
	}

	public float getGoldParam() {
		return goldParam;
	}

	public void setGoldParam(float goldParam) {
		this.goldParam = goldParam;
	}

	public int getHeroNum() {
		return heroNum;
	}

	public void setHeroNum(int heroNum) {
		this.heroNum = heroNum;
	}

	public Map<Byte, Integer> getNpcNos() {
		return npcNos;
	}

	public void setNpcNos(Map<Byte, Integer> npcNos) {
		this.npcNos = npcNos;
	}

	public Integer getNpcNo(byte type) {
		return npcNos.get(type);
	}
}
