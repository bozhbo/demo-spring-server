package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class ToolBoxInfo extends BaseTO {
	
	public static final int TYPE_BOX_CHARGE = 1;// 充值礼包
	public static final int TYPE_BOX_GOLD = 2;// 金币礼包
	
	private int guid;// 礼包的guid(所有服通用，用于极效全服更新使用)
	private int boxType;// 1-充值礼包 2-金币礼包
	private int chargeNo;// 充值礼包对应的配置表中的no
	
	private String boxName;
	private int costPrice;// 金币礼包价格
	private int itemSaleNum;// 礼包商品售卖个数
	
	// 礼包版本
	private int boxVersion;// 版本号变化清零玩家购买次数
	
	private int boxQuality;// 金子礼包品阶 1-绿 2-蓝 3-紫 4-橙
	
	private String itemStr;// 道具奖励字符串 格式：数量:道具ID; 数量:道具ID;数量:道具ID
	private Timestamp startTime;// 
	private Timestamp endTime;// 
	
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

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getBoxType() {
		return boxType;
	}

	public void setBoxType(int boxType) {
		this.boxType = boxType;
	}

	public int getChargeNo() {
		return chargeNo;
	}

	public void setChargeNo(int chargeNo) {
		this.chargeNo = chargeNo;
	}

	public int getBoxQuality() {
		return boxQuality;
	}

	public void setBoxQuality(int boxQuality) {
		this.boxQuality = boxQuality;
	}

	public int getBoxVersion() {
		return boxVersion;
	}

	public void setBoxVersion(int boxVersion) {
		this.boxVersion = boxVersion;
	}

	public int getGuid() {
		return guid;
	}

	public void setGuid(int guid) {
		this.guid = guid;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}
}
