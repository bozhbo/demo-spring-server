package com.snail.webgame.game.protocal.vipshop.querysalebox;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySaleBoxRe extends MessageBody {

	private int boxId;
	private String boxName;
	private int costPrice;// 
	private int itemSaleNum;// 商品售卖个数
	private String itemStr;// 道具奖励字符串
	private int boxQuality;// 礼包品阶 1-绿 2-蓝 3-紫 4-橙
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("boxId", 0);
		ps.addString("boxName", "flashCode", 0);
		ps.add("costPrice", 0);
		ps.add("itemSaleNum", 0);
		ps.addString("itemStr", "flashCode", 0);
		ps.add("boxQuality", 0);
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public int getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}

	public int getItemSaleNum() {
		return itemSaleNum;
	}

	public void setItemSaleNum(int itemSaleNum) {
		this.itemSaleNum = itemSaleNum;
	}

	public String getItemStr() {
		return itemStr;
	}

	public void setItemStr(String itemStr) {
		this.itemStr = itemStr;
	}

	public int getBoxQuality() {
		return boxQuality;
	}

	public void setBoxQuality(int boxQuality) {
		this.boxQuality = boxQuality;
	}

}
