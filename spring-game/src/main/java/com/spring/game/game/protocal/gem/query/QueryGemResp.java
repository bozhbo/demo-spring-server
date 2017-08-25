package com.snail.webgame.game.protocal.gem.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryGemResp extends MessageBody {

	private int result;
	private int fightNum;// 剩余战斗次数
	private int resetNum;// 当日重置次数
	private int resetLimit;// 当日可重置上限
	private int buyNum;// 当日购买次数
	private int lastFightBattleNo;// 最后战斗关卡编号
	private int lastFightResult;// 1-胜 2-败
	private int maxFightBattleNo;// 历史通过最高关卡

	private String prizeBattleNos;// 可领取奖励的关卡（，）

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("fightNum", 0);
		ps.add("resetNum", 0);
		ps.add("resetLimit", 0);
		ps.add("buyNum", 0);
		ps.add("lastFightBattleNo", 0);
		ps.add("lastFightResult", 0);
		ps.add("maxFightBattleNo", 0);

		ps.addString("prizeBattleNos", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFightNum() {
		return fightNum;
	}

	public void setFightNum(int fightNum) {
		this.fightNum = fightNum;
	}

	public int getResetNum() {
		return resetNum;
	}

	public void setResetNum(int resetNum) {
		this.resetNum = resetNum;
	}

	public int getResetLimit() {
		return resetLimit;
	}

	public void setResetLimit(int resetLimit) {
		this.resetLimit = resetLimit;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getLastFightBattleNo() {
		return lastFightBattleNo;
	}

	public void setLastFightBattleNo(int lastFightBattleNo) {
		this.lastFightBattleNo = lastFightBattleNo;
	}

	public int getLastFightResult() {
		return lastFightResult;
	}

	public void setLastFightResult(int lastFightResult) {
		this.lastFightResult = lastFightResult;
	}

	public int getMaxFightBattleNo() {
		return maxFightBattleNo;
	}

	public void setMaxFightBattleNo(int maxFightBattleNo) {
		this.maxFightBattleNo = maxFightBattleNo;
	}

	public String getPrizeBattleNos() {
		return prizeBattleNos;
	}

	public void setPrizeBattleNos(String prizeBattleNos) {
		this.prizeBattleNos = prizeBattleNos;
	}
}
