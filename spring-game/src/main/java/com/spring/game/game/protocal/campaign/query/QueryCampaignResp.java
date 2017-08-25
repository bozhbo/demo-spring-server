package com.snail.webgame.game.protocal.campaign.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryCampaignResp extends MessageBody {

	private int result;	
	private int resetNum;// 当日重置次数
	private int lastFightBattleNo;// 最后战斗关卡编号,-1:全部通关

	// 战死残血和上阵的英雄信息
	private int count;
	private List<CampaignHeroRe> list;

	// 关卡信息
	private int battleCount;
	private List<CampaignBattleRe> battleList;
	
	private byte reviceNum;// 复活次数
	private int hisFightBattleNo;// 最后战斗关卡编号,-1:全部通关
	
	private String hireHeroIds;//已雇佣的武将id（，）
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("resetNum", 0);
		ps.add("lastFightBattleNo", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.campaign.query.CampaignHeroRe", "count");

		ps.add("battleCount", 0);
		ps.addObjectArray("battleList", "com.snail.webgame.game.protocal.campaign.query.CampaignBattleRe",
				"battleCount");
		
		ps.add("reviceNum", 0);		
		ps.add("hisFightBattleNo", 0);
		
		ps.addString("hireHeroIds", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getResetNum() {
		return resetNum;
	}

	public void setResetNum(int resetNum) {
		this.resetNum = resetNum;
	}

	public int getLastFightBattleNo() {
		return lastFightBattleNo;
	}

	public void setLastFightBattleNo(int lastFightBattleNo) {
		this.lastFightBattleNo = lastFightBattleNo;
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

	public int getBattleCount() {
		return battleCount;
	}

	public void setBattleCount(int battleCount) {
		this.battleCount = battleCount;
	}

	public List<CampaignBattleRe> getBattleList() {
		return battleList;
	}

	public void setBattleList(List<CampaignBattleRe> battleList) {
		this.battleList = battleList;
	}

	public byte getReviceNum() {
		return reviceNum;
	}

	public void setReviceNum(byte reviceNum) {
		this.reviceNum = reviceNum;
	}

	public int getHisFightBattleNo() {
		return hisFightBattleNo;
	}

	public void setHisFightBattleNo(int hisFightBattleNo) {
		this.hisFightBattleNo = hisFightBattleNo;
	}

	public String getHireHeroIds() {
		return hireHeroIds;
	}

	public void setHireHeroIds(String hireHeroIds) {
		this.hireHeroIds = hireHeroIds;
	}
}
