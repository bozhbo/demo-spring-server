package com.snail.webgame.game.protocal.worship.doWorship;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DoWorshipResp extends MessageBody{

	private int result;//结果
	private int beWorShipNum;//被膜拜次数
	private String item;//膜拜者获得物品
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("beWorShipNum", 0);
		ps.addString("item",  "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getBeWorShipNum() {
		return beWorShipNum;
	}

	public void setBeWorShipNum(int beWorShipNum) {
		this.beWorShipNum = beWorShipNum;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	
	

	
}
