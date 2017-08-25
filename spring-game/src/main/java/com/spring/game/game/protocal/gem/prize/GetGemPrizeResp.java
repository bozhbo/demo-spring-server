package com.snail.webgame.game.protocal.gem.prize;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class GetGemPrizeResp extends MessageBody {

	private int result;
	private String prizeBattleNos;// 可领取奖励的关卡（，）

	// 奖励信息
	private int prizeNum;
	private List<BattlePrize> prize;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("prizeBattleNos", "flashCode", 0);

		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getPrizeBattleNos() {
		return prizeBattleNos;
	}

	public void setPrizeBattleNos(String prizeBattleNos) {
		this.prizeBattleNos = prizeBattleNos;
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
