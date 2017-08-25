package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

/**
 * 
 * 类介绍:竞技场PVP战斗对象
 *
 * @author zhoubo
 * @2014-12-6
 */
public class CompetitionVo extends BaseRoomResp {

	private int level;
	private byte stage;
	private int stageValue;
	private int fightValue; // 战斗力
	private int winTimes;// 连续胜利次数
	private int loseTimes;// 连续失败次数
	private int vipLv;// 当前的vip等级
	private byte moneyRand;// 多倍跨服币发放
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, level);
		setByte(buffer, order, this.stage);
		setInt(buffer, order, this.stageValue);
		setInt(buffer, order, fightValue);
		setInt(buffer, order, winTimes);
		setInt(buffer, order, loseTimes);
		setInt(buffer, order, vipLv);
		setByte(buffer, order, moneyRand);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public byte getStage() {
		return stage;
	}

	public void setStage(byte stage) {
		this.stage = stage;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public int getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(int winTimes) {
		this.winTimes = winTimes;
	}

	public int getLoseTimes() {
		return loseTimes;
	}

	public void setLoseTimes(int loseTimes) {
		this.loseTimes = loseTimes;
	}

	public int getStageValue() {
		return stageValue;
	}

	public void setStageValue(int stageValue) {
		this.stageValue = stageValue;
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}

	public byte getMoneyRand() {
		return moneyRand;
	}

	public void setMoneyRand(byte moneyRand) {
		this.moneyRand = moneyRand;
	}
	
	
}
