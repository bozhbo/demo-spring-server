package com.snail.webgame.game.protocal.activity.buytimes;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 购买经验，银币活动次数响应
 * 
 * @author xiasd
 *
 */
public class BuyTimesReq extends MessageBody {

	private int buyType;// 1-经验活动次数 2-银币活动次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("buyType", 0);
	}

	public int getBuyType() {
		return buyType;
	}

	public void setBuyType(int buyType) {
		this.buyType = buyType;
	}

}
