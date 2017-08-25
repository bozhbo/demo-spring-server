package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 魂匣
 * @author zhangyq
 */
public class HeroRetrInfo extends BaseTO {

	private int heroNo; // 编号
	private int heroType;// 状态 4-每周英雄 1,2,3-每日英雄

	private Timestamp lastReshTime;// 刷新时间

	private String heros;

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getHeroType() {
		return heroType;
	}

	public void setHeroType(int heroType) {
		this.heroType = heroType;
	}

	public Timestamp getLastReshTime() {
		if(lastReshTime == null)
		{
			return new Timestamp(0);
		}
		return lastReshTime;
	}

	public void setLastReshTime(Timestamp lastReshTime) {
		this.lastReshTime = lastReshTime;
	}

	public String getHeros() {
		return heros;
	}

	public void setHeros(String heros) {
		this.heros = heros;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
