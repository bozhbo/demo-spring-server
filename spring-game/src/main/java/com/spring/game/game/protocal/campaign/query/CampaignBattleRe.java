package com.snail.webgame.game.protocal.campaign.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CampaignBattleRe extends MessageBody {

	private int battleNo;
	private byte isGetPrize;// 0-未领取 1-已领取
	private String defendRoleName;// 角色名称

	private int size;
	private List<CampaignDeployRe> heros = new ArrayList<CampaignDeployRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("battleNo", 0);
		ps.add("isGetPrize", 0);

		ps.addString("defendRoleName", "flashCode", 0);

		ps.add("size", 0);
		ps.addObjectArray("heros", "com.snail.webgame.game.protocal.campaign.query.CampaignDeployRe", "size");
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

	public String getDefendRoleName() {
		return defendRoleName;
	}

	public void setDefendRoleName(String defendRoleName) {
		this.defendRoleName = defendRoleName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<CampaignDeployRe> getHeros() {
		return heros;
	}

	public void setHeros(List<CampaignDeployRe> heros) {
		this.heros = heros;
	}
}
