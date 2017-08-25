package com.snail.webgame.game.protocal.item.sell;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SellItemResp extends MessageBody {

	private int result;

	 private int itemId;// 物品Id
	 private int itemNo;// 物品编号
	 private int num;// 出售物品数量
	 
	 private byte sourceType;//1:银子	2:金子
	private int sourceChange;//资源变动数,正值为增加,负值为减少

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("itemId", 0);
		ps.add("itemNo", 0);
		ps.add("num", 0);
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}
	
	
}
