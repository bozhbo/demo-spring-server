package com.snail.webgame.game.protocal.gem.sweep;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class SweepGemResp extends MessageBody {

	private int result;
	private int lastFightBattleNo;// 最后战斗关卡编号
	private int lastFightResult;// 1-胜 2-败

	private int prizeNum;
	private List<BattlePrize> prize;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("lastFightBattleNo", 0);
		ps.add("lastFightResult", 0);

		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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

	public int getPrizeNum() {
		return prizeNum;
	}

	public void setPrizeNum(int prizeNum) {
		this.prizeNum = prizeNum;
	}

	public List<BattlePrize> getPrize() {
		return prize;
	}

	public void setPrize(List<BattlePrize> prize) {
		this.prize = prize;
	}
}
