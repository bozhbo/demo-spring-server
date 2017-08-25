package com.snail.webgame.game.protocal.snatch.loot;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class LootPrizeRe extends MessageBody {
	private byte fightSuccess;	//战斗状态(1成功，2失败)
	//获得奖励
	private int prizeSize;	//prize长度
	private List<BattlePrize> prize;	//战斗胜利奖励
	//翻牌奖励
	private int cardPrizeSize;	//cardPrize长度
	private List<BattlePrize> cardPrize;	//翻牌奖励（第一个为获得奖励）
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("fightSuccess", 0);
		ps.add("prizeSize", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeSize");
		ps.add("cardPrizeSize", 0);
		ps.addObjectArray("cardPrize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "cardPrizeSize");
	}
	public int getPrizeSize() {
		return prizeSize;
	}
	public void setPrizeSize(int prizeSize) {
		this.prizeSize = prizeSize;
	}
	public List<BattlePrize> getPrize() {
		return prize;
	}
	public void setPrize(List<BattlePrize> prize) {
		this.prize = prize;
	}
	public int getCardPrizeSize() {
		return cardPrizeSize;
	}
	public void setCardPrizeSize(int cardPrizeSize) {
		this.cardPrizeSize = cardPrizeSize;
	}
	public List<BattlePrize> getCardPrize() {
		return cardPrize;
	}
	public void setCardPrize(List<BattlePrize> cardPrize) {
		this.cardPrize = cardPrize;
	}
	public byte getFightSuccess() {
		return fightSuccess;
	}
	public void setFightSuccess(byte fightSuccess) {
		this.fightSuccess = fightSuccess;
	}
}
