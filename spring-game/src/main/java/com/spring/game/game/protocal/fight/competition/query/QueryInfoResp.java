package com.snail.webgame.game.protocal.fight.competition.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryInfoResp extends MessageBody {

	private int result;
	private int winTimes;// 胜利次数
	private int loseTimes;// 失败次数
	private int competitionValue;// 竞技场积分
	private byte competitionStage;// 竞技场段位
	private byte stageWinTimes;// 段位赛胜利次数
	private byte stageLoseTimes;// 段位赛失败次数
	private byte stageState;// 段位赛胜利失败 （二进制表示法 1为胜利）
	private int rankNum;// 排名 0表示无排名
	private String competitionAward;// 竞技场奖励可领取段位Id(1,2)
	private byte stageAward;// 竞技场奖励Id(1,2)
	private byte remainCleanDays; // 剩余结算天数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("winTimes", 0);
		ps.add("loseTimes", 0);
		ps.add("competitionValue", 0);
		ps.add("competitionStage", 0);
		ps.add("stageWinTimes", 0);
		ps.add("stageLoseTimes", 0);
		ps.add("stageState", 0);
		ps.add("rankNum", 0);
		ps.addString("competitionAward", "flashCode", 0);
		ps.add("stageAward", 0);
		ps.add("remainCleanDays", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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

	public int getCompetitionValue() {
		return competitionValue;
	}

	public void setCompetitionValue(int competitionValue) {
		this.competitionValue = competitionValue;
	}

	public byte getCompetitionStage() {
		return competitionStage;
	}

	public void setCompetitionStage(byte competitionStage) {
		this.competitionStage = competitionStage;
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

	public int getRankNum() {
		return rankNum;
	}

	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}

	public String getCompetitionAward() {
		return competitionAward;
	}

	public void setCompetitionAward(String competitionAward) {
		this.competitionAward = competitionAward;
	}

	public byte getStageAward() {
		return stageAward;
	}

	public void setStageAward(byte stageAward) {
		this.stageAward = stageAward;
	}

	public byte getRemainCleanDays() {
		return remainCleanDays;
	}

	public void setRemainCleanDays(byte remainCleanDays) {
		this.remainCleanDays = remainCleanDays;
	}
	
}
