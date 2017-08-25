package com.snail.webgame.game.protocal.campaign.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryCampaignHeroResp extends MessageBody {

	private int result;
	private String heroIdStr;// 变化的英雄id

	// 战死残血和上阵的英雄信息
	private int count;
	private List<CampaignHeroRe> list;

	// 变化的关卡信息
	private CampaignBattleRe battleInfo = new CampaignBattleRe();
	
	private String hireHeroIds;//已雇佣的武将id（，）

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("heroIdStr", "flashCode", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.campaign.query.CampaignHeroRe", "count");

		ps.addObject("battleInfo");
		
		ps.addString("hireHeroIds", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getHeroIdStr() {
		return heroIdStr;
	}

	public void setHeroIdStr(String heroIdStr) {
		this.heroIdStr = heroIdStr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<CampaignHeroRe> getList() {
		return list;
	}

	public void setList(List<CampaignHeroRe> list) {
		this.list = list;
	}

	public CampaignBattleRe getBattleInfo() {
		return battleInfo;
	}

	public void setBattleInfo(CampaignBattleRe battleInfo) {
		this.battleInfo = battleInfo;
	}

	public String getHireHeroIds() {
		return hireHeroIds;
	}

	public void setHireHeroIds(String hireHeroIds) {
		this.hireHeroIds = hireHeroIds;
	}
}
