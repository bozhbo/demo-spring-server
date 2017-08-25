package com.snail.webgame.game.protocal.item.propUse;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PropUseResp extends MessageBody {

	private int result;
	private int itemId; //道具Id
	private int itemNo;//道具No
	private int itemNum;//道具数量
	
	private int itemId1; //道具Id1
	private int itemNo1;//道具No1
	private int itemNum1;//道具数量1
	
	private String getItem; //No+","+ID+","+NUM+","No+","+ID+","+NUM  -- 道具ID传0
	
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		
		ps.add("result", 0);
		ps.add("itemId", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);
		
		ps.add("itemId1", 0);
//		ps.add("itemNo1", 0);
//		ps.add("itemNum1", 0);
		
		ps.addString("getItem", "flashCode", 0);

	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getItemNo() {
		return itemNo;
	}
	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getItemNum() {
		return itemNum;
	}
	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}
	public String getGetItem() {
		return getItem;
	}
	public void setGetItem(String getItem) {
		this.getItem = getItem;
	}
	public int getItemId1() {
		return itemId1;
	}
	public void setItemId1(int itemId1) {
		this.itemId1 = itemId1;
	}
	public int getItemNo1() {
		return itemNo1;
	}
	public void setItemNo1(int itemNo1) {
		this.itemNo1 = itemNo1;
	}
	public int getItemNum1() {
		return itemNum1;
	}
	public void setItemNum1(int itemNum1) {
		this.itemNum1 = itemNum1;
	}
	
}
