package com.snail.webgame.game.protocal.activity.buytimes;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 购买经验，银币活动次数响应
 * 
 * @author xiasd
 *
 */
public class BuyTimesResp extends MessageBody{

	private int result;// 是否购买成功
	private int type;// 1-经验活动次数  2-银币活动次数
	private int hasBuyedTimes;// 已经购买了次数
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("type", 0);
		ps.add("hasBuyedTimes", 0);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHasBuyedTimes() {
		return hasBuyedTimes;
	}

	public void setHasBuyedTimes(int hasBuyedTimes) {
		this.hasBuyedTimes = hasBuyedTimes;
	}

}
