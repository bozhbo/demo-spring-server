package com.snail.webgame.game.protocal.activity.saodang;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

/**
 * 练兵场扫荡响应
 * 
 * @author nijy
 *
 */
public class ActivitySaodangResp extends MessageBody{

	private int result;
	//获得的奖励
	private int prizeNum;
	private List<BattlePrize> prize;
	private int saodangType;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
		ps.add("saodangType", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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

	public int getSaodangType() {
		return saodangType;
	}

	public void setSaodangType(int saodangType) {
		this.saodangType = saodangType;
	}

}
