package com.snail.webgame.game.protocal.opactivity.weixin.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryWeiXinResp extends MessageBody {

	private int result;
	private byte firstFlag;// 首次奖励标记 0-未分享 1-可领取奖励 2-已领奖
	private byte dailyFlag;// 每日奖励标记 0-未分享 1-可领取奖励 2-已领奖

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("firstFlag", 0);
		ps.add("dailyFlag", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getFirstFlag() {
		return firstFlag;
	}

	public void setFirstFlag(byte firstFlag) {
		this.firstFlag = firstFlag;
	}

	public byte getDailyFlag() {
		return dailyFlag;
	}

	public void setDailyFlag(byte dailyFlag) {
		this.dailyFlag = dailyFlag;
	}

}
