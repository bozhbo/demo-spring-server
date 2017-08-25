package com.snail.webgame.game.protocal.goldBuy.goldBuy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GoldBuyReq extends MessageBody {

	private byte buyType;// 购买类型 GoldBuy.xml no 1-银子 2-体力 3-科技点（暂时没用） 4-精力
	private byte repeat;// 0-单次 1-连续

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("buyType", 0);
		ps.add("repeat", 0);
	}

	public byte getBuyType() {
		return buyType;
	}

	public void setBuyType(byte buyType) {
		this.buyType = buyType;
	}

	public byte getRepeat() {
		return repeat;
	}

	public void setRepeat(byte repeat) {
		this.repeat = repeat;
	}
	
}
