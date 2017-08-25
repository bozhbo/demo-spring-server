package com.snail.webgame.game.protocal.item.compose;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ComposeItemReq extends MessageBody {

	private int itemNo;// 合成物品No
	private int flag; //0-合成一件 1-全部合成

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("itemNo", 0);
		ps.add("flag", 0);
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
