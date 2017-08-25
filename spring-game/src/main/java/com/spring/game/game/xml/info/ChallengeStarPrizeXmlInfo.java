package com.snail.webgame.game.xml.info;

import java.util.List;

public class ChallengeStarPrizeXmlInfo {

	private int prizeId;// 奖励编号
	private byte needStar;// 需要星数

	List<DropXMLInfo> prizes;	//奖励

	public ChallengeStarPrizeXmlInfo(int prizeId, byte needStar) {
		this.prizeId = prizeId;
		this.needStar = needStar;
	}

	public int getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}

	public byte getNeedStar() {
		return needStar;
	}

	public void setNeedStar(byte needStar) {
		this.needStar = needStar;
	}

	public List<DropXMLInfo> getPrizes() {
		return prizes;
	}

	public void setPrizes(List<DropXMLInfo> prizes) {
		this.prizes = prizes;
	}

}
