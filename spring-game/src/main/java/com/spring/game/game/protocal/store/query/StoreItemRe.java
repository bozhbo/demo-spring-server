package com.snail.webgame.game.protocal.store.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class StoreItemRe extends MessageBody {

	private int id;
	private int roleId;// 用户编号

	private int itemNo;// 道具编号
	private int itemNum;// 数量
	private int buyNum;// 已购数量

	private byte costType;// 商店货币类型 1:money银子 2:gold金子 3:sp 体力 4:soul 战魂 5:food
							// 粮草 6:tech 技能点(升级武将技能) 7:exp 玩家经验
							// 8:courage 竞技场货币 勇气点 9:justice 征战四方货币 正义点
							// 10:guildcurrency 工会币
							// 11:fb 副本-章节编号 12:pre 前置建筑 13:taskTime 时间区间
							// 14:task 前置完成任务 15:roleLv 玩家等级
	private int costNum;// shopType :1单个消耗的点数 2:多个消耗的点数
	private int position; //shop.xml的postion

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("id", 0);
		ps.add("roleId", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);
		ps.add("buyNum", 0);

		ps.add("costType", 0);
		ps.add("costNum", 0);
		ps.add("position", 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
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

	public byte getCostType() {
		return costType;
	}

	public void setCostType(byte costType) {
		this.costType = costType;
	}

	public int getCostNum() {
		return costNum;
	}

	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
}
