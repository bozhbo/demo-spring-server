package com.snail.webgame.game.protocal.vipshop.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChargeBoxRe extends MessageBody {

	private int chargeBoxNo;
	private String sellItemStr;// 格式：数量:道具ID;数量:道具ID
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chargeBoxNo", 0);
		ps.addString("sellItemStr", "flashCode", 0);
	}

	public int getChargeBoxNo() {
		return chargeBoxNo;
	}

	public void setChargeBoxNo(int chargeBoxNo) {
		this.chargeBoxNo = chargeBoxNo;
	}

	public String getSellItemStr() {
		return sellItemStr;
	}

	public void setSellItemStr(String sellItemStr) {
		this.sellItemStr = sellItemStr;
	}

	
}
