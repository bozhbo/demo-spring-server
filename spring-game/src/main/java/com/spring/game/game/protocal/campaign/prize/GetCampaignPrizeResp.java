package com.snail.webgame.game.protocal.campaign.prize;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class GetCampaignPrizeResp extends MessageBody {

	private int result;
	private int battleNo;// 关卡编号
	private byte isGetPrize;// 0-未领取 1-已领取

	// 奖励信息
	private int prizeNum;
	private List<BattlePrize> prize;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("battleNo", 0);
		ps.add("isGetPrize", 0);

		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getBattleNo() {
		return battleNo;
	}

	public void setBattleNo(int battleNo) {
		this.battleNo = battleNo;
	}

	public byte getIsGetPrize() {
		return isGetPrize;
	}

	public void setIsGetPrize(byte isGetPrize) {
		this.isGetPrize = isGetPrize;
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
