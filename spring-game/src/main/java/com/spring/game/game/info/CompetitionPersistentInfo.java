package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

public class CompetitionPersistentInfo extends BaseTO {

	private int winTimes;// 胜利次数
	private int loseTimes;// 失败次数
	private byte stageWinTimes;// 段位赛胜利次数
	private byte stageLoseTimes;// 段位赛失败次数
	private byte stageState;// 段位赛胜利失败
	private byte competitionStage;// 竞技场段位
	private int competitionValue;// 竞技场积分
	private long competitionTime;// 竞技场积分时间 用于排行
	private int competitionAward;// 竞技场奖励领取状态(二进制表示) 1-已领取
	private int stageAward; // 段位奖励发送(二进制表示) 1-已发送
	
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

	public byte getStageWinTimes() {
		return stageWinTimes;
	}

	public void setStageWinTimes(byte stageWinTimes) {
		this.stageWinTimes = stageWinTimes;
	}

	public byte getStageLoseTimes() {
		return stageLoseTimes;
	}

	public void setStageLoseTimes(byte stageLoseTimes) {
		this.stageLoseTimes = stageLoseTimes;
	}

	public byte getStageState() {
		return stageState;
	}

	public void setStageState(byte stageState) {
		this.stageState = stageState;
	}

	public byte getCompetitionStage() {
		return competitionStage;
	}

	public void setCompetitionStage(byte competitionStage) {
		this.competitionStage = competitionStage;
	}

	public int getCompetitionValue() {
		return competitionValue;
	}

	public void setCompetitionValue(int competitionValue) {
		this.competitionValue = competitionValue;
	}

	public long getCompetitionTime() {
		return competitionTime;
	}

	public void setCompetitionTime(long competitionTime) {
		this.competitionTime = competitionTime;
	}

	public int getCompetitionAward() {
		return competitionAward;
	}

	public void setCompetitionAward(int competitionAward) {
		this.competitionAward = competitionAward;
	}

	public int getStageAward() {
		return stageAward;
	}

	public void setStageAward(int stageAward) {
		this.stageAward = stageAward;
	}

	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
