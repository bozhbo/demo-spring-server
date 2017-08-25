package com.snail.webgame.game.protocal.item.compose;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ComposeItemResp extends MessageBody {

	private int result;

	private int itemId;// 扣除物品Id
	private int itemNo;// 扣除物品No
	private short itemNum;// 扣除物品数量

	private String getItem; // No+","+ID+","+NUM+","No+","+ID+","+NUM -- 道具ID传0

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);

		ps.add("itemId", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);

		ps.addString("getItem", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public short getItemNum() {
		return itemNum;
	}

	public void setItemNum(short itemNum) {
		this.itemNum = itemNum;
	}

	public String getGetItem() {
		return getItem;
	}

	public void setGetItem(String getItem) {
		this.getItem = getItem;
	}

}
