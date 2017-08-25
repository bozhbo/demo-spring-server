package com.snail.webgame.game.protocal.item.sell;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SellItemReq extends MessageBody {

	private int itemId;// 物品Id
	private int itemNum;// 出售物品数量
	private byte itemType;// 物品类型

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("itemId", 0);
		ps.add("itemNum", 0);
		ps.add("itemType", 0);
	}

	public int getItemNum() {
		return itemNum;
	}


	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}


	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public byte getItemType() {
		return itemType;
	}

	public void setItemType(byte itemType) {
		this.itemType = itemType;
	}

}
