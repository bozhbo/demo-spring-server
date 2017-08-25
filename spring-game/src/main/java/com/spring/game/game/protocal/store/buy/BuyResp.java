package com.snail.webgame.game.protocal.store.buy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyResp extends MessageBody {

	private int result;
	private byte storeType;// 1-竞技场商店 2-征战四方商店 3-公会商店 4-普通商店
	private int storeItemId;// 商品Id
	private int buyNum;// 当前购买的数量
	private byte sourceType;// 1:银子 2:金子 3:体力 7:玩家经验 8:竞技场货币-勇气点 9:征战四方货币 正义点
							// 10:工会币 15:玩家等级 28:跨服币
	// 32:战功 34:历史战功 49:体力值购买次数 50:银子购买次数 51:经验活动剩余次数 52:金币活动剩余次数 53:用户名修改次数
	// 54:历史最高战斗力 55：精力
	private int sourceChange;// 资源变动数,正值为增加,负值为减少
	
	private int teamChallenge; //斩将令商店专用 （传送不符合条件的副本ID,默认为1）

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("storeType", 0);
		ps.add("storeItemId", 0);
		ps.add("buyNum", 0);
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
		ps.add("teamChallenge", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getStoreType() {
		return storeType;
	}

	public void setStoreType(byte storeType) {
		this.storeType = storeType;
	}

	public int getStoreItemId() {
		return storeItemId;
	}

	public void setStoreItemId(int storeItemId) {
		this.storeItemId = storeItemId;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}

	public int getTeamChallenge() {
		return teamChallenge;
	}

	public void setTeamChallenge(int teamChallenge) {
		this.teamChallenge = teamChallenge;
	}

}
