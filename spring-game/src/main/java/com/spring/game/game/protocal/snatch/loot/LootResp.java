package com.snail.webgame.game.protocal.snatch.loot;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


public class LootResp extends MessageBody {
	private int result;
	private byte lootSuccess;	//抢夺状态(1成功，2失败)
	private int lootTimes;//抢夺次数
	
	private int prizeListSize;	//prizeList长度
	private List<LootPrizeRe> prizeList = new ArrayList<LootPrizeRe>();	//获得奖励
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("lootSuccess", 0);
		ps.add("lootTimes", 0);
		ps.add("prizeListSize", 0);
		ps.addObjectArray("prizeList", "com.snail.webgame.game.protocal.snatch.loot.LootPrizeRe", "prizeListSize");
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public byte getLootSuccess() {
		return lootSuccess;
	}
	public void setLootSuccess(byte lootSuccess) {
		this.lootSuccess = lootSuccess;
	}
	public int getPrizeListSize() {
		return prizeListSize;
	}
	public void setPrizeListSize(int prizeListSize) {
		this.prizeListSize = prizeListSize;
	}
	public List<LootPrizeRe> getPrizeList() {
		return prizeList;
	}
	public void setPrizeList(List<LootPrizeRe> prizeList) {
		this.prizeList = prizeList;
	}
	public int getLootTimes() {
		return lootTimes;
	}
	public void setLootTimes(int lootTimes) {
		this.lootTimes = lootTimes;
	}
}
