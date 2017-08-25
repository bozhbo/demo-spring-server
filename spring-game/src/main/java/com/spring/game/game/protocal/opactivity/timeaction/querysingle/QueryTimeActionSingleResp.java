package com.snail.webgame.game.protocal.opactivity.timeaction.querysingle;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryTimeActionSingleResp extends MessageBody {

	private int result;
	private int actId;// 已开启的活动id
	private byte actType;// 活动类型 0:玩家领取奖励类型；1:双倍竞技场类型；2:双倍金币生存副本类型；3:限时武神类型
	
	private String actName;// 活动name
	private String actIntroduce;// 活动说明
	private int lotCost;// 限时武将的抽奖价格
	private int remainNum;// 必抽武将的剩余次数
	private String lotRewardStr;// 武将id,道具id,道具id
	
	private int count;
	private List<TimeActionRewardRe> rewards;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("actId", 0);
		ps.add("actType", 0);
		ps.addString("actName", "flashCode", 0);
		ps.addString("actIntroduce", "flashCode", 0);
		ps.add("lotCost", 0);
		ps.add("remainNum", 0);
		ps.addString("lotRewardStr", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("rewards", "com.snail.webgame.game.protocal.opactivity.timeaction.querysingle.TimeActionRewardRe", "count");
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public byte getActType() {
		return actType;
	}

	public void setActType(byte actType) {
		this.actType = actType;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActIntroduce() {
		return actIntroduce;
	}

	public void setActIntroduce(String actIntroduce) {
		this.actIntroduce = actIntroduce;
	}

	public int getLotCost() {
		return lotCost;
	}

	public void setLotCost(int lotCost) {
		this.lotCost = lotCost;
	}

	public int getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<TimeActionRewardRe> getRewards() {
		return rewards;
	}

	public void setRewards(List<TimeActionRewardRe> rewards) {
		this.rewards = rewards;
	}

	public String getLotRewardStr() {
		return lotRewardStr;
	}

	public void setLotRewardStr(String lotRewardStr) {
		this.lotRewardStr = lotRewardStr;
	}

}
