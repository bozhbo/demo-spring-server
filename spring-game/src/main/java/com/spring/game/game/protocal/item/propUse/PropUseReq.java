package com.snail.webgame.game.protocal.item.propUse;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PropUseReq extends MessageBody {

	private int itemId;//道具Id
	private int itemNo;//道具编号
	private int itemNum;//道具数量
	private int shopRefreshFlag; //0 - 装备商城 1 - 星石商城
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		
		ps.add("itemId", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);
		ps.add("shopRefreshFlag", 0);

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
	public int getItemNum() {
		return itemNum;
	}
	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}
	public int getShopRefreshFlag() {
		return shopRefreshFlag;
	}
	public void setShopRefreshFlag(int shopRefreshFlag) {
		this.shopRefreshFlag = shopRefreshFlag;
	}
	
}
