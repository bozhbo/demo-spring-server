package com.snail.webgame.game.protocal.campaign.sweep;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.campaign.query.QueryCampaignResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class SweepCampaignResp extends MessageBody {

	private int result;
	private QueryCampaignResp campaign;

	// 奖励信息
	private int prizeNum;
	private List<BattlePrize> prize;
	
	private int sweepNum;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("campaign");

		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
		ps.add("sweepNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public QueryCampaignResp getCampaign() {
		return campaign;
	}

	public void setCampaign(QueryCampaignResp campaign) {
		this.campaign = campaign;
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

	public int getSweepNum() {
		return sweepNum;
	}

	public void setSweepNum(int sweepNum) {
		this.sweepNum = sweepNum;
	}
}
