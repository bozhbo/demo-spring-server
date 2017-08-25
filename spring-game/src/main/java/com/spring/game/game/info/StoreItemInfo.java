package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

public class StoreItemInfo extends BaseTO {
	
	public static final int STORE_TYPE_1 = 1;// 积分排名赛（新竞技场） 商店
	public static final int STORE_TYPE_2 = 2;// 征战四方 商店
	public static final int STORE_TYPE_3 = 3;// 工会商店
	public static final int STORE_TYPE_4 = 4;// 普通商店
	public static final int STORE_TYPE_5 = 5;// 跨服商店
	public static final int STORE_TYPE_6 = 6;// 战功商店
	public static final int STORE_TYPE_7 = 7;// 黑市商店
	public static final int STORE_TYPE_8 = 8;// 装备商店
	public static final int STORE_TYPE_9 = 9;//异域商店
	public static final int STORE_TYPE_10 = 10;// 组队PVP商店
	public static final int STORE_TYPE_11 = 11;// 星石商店
	public static final int STORE_TYPE_12 = 12;// 斩将令商店


	private int roleId;// 用户编号
	private int storeType;// 1-新竞技场商店

	private int position;// 商店位置

	private int itemNo;// 道具编号
	private int itemNum;// 数量
	private int buyNum;// 已购买数量

	private String costType;// money银子 gold金子 courage 勇气值
	private int costNum;// 消耗的点数
	private String condition;

	public int getStoreType() {
		return storeType;
	}

	public void setStoreType(int storeType) {
		this.storeType = storeType;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
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

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public int getCostNum() {
		return costNum;
	}

	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
}
