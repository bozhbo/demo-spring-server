package com.snail.webgame.game.protocal.fight.competition.end;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

/**
 * 
 * 类介绍:竞技场跨服站结算Response
 *
 * @author zhoubo
 * @2014-12-4
 */
public class EndFightResp extends MessageBody {

	private int competitionValue;// 竞技场积分
	private byte competitionStage;// 竞技场段位
	private byte stageWinTimes;// 段位赛胜利次数
	private byte stageLoseTimes;// 段位赛失败次数
	private byte stageState;// 段位赛胜利失败 （二进制表示法 1为胜利）
	private byte isWin; // 是否胜利 1-胜利 0-失败
	private byte targetRank;// 对方段位
	private int targetLevel;// 对方等级
	private String targetRoleName;// 对方角色名称

	// 翻牌获得奖励（第一个为获得奖励）
	private int fpPrizeNum;
	private List<BattlePrize> fpPrize;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("competitionValue", 0);
		ps.add("competitionStage", 0);
		ps.add("stageWinTimes", 0);
		ps.add("stageLoseTimes", 0);
		ps.add("stageState", 0);
		ps.add("isWin", 0);
		ps.add("targetRank", 0);
		ps.add("targetLevel", 0);
		ps.addString("targetRoleName", "flashCode", 0);
		ps.add("fpPrizeNum", 0);
		ps.addObjectArray("fpPrize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "fpPrizeNum");
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

	public byte getIsWin() {
		return isWin;
	}

	public void setIsWin(byte isWin) {
		this.isWin = isWin;
	}

	public byte getTargetRank() {
		return targetRank;
	}

	public void setTargetRank(byte targetRank) {
		this.targetRank = targetRank;
	}

	public String getTargetRoleName() {
		return targetRoleName;
	}

	public void setTargetRoleName(String targetRoleName) {
		this.targetRoleName = targetRoleName;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(int targetLevel) {
		this.targetLevel = targetLevel;
	}

	public int getFpPrizeNum() {
		return fpPrizeNum;
	}

	public void setFpPrizeNum(int fpPrizeNum) {
		this.fpPrizeNum = fpPrizeNum;
	}

	public List<BattlePrize> getFpPrize() {
		return fpPrize;
	}

	public void setFpPrize(List<BattlePrize> fpPrize) {
		this.fpPrize = fpPrize;
	}

}
