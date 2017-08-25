package com.snail.webgame.game.protocal.item.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BagItemRe extends MessageBody {

	private int itemId;// 道具Id
	private byte itemType;// 道具类型
	private int itemNo;// 道具编号
	private int itemNum;// 数量 0-删除
	private byte colour;// 颜色
	private byte level;// 等级（宝石、装备特有）

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("itemId", 0);
		ps.add("itemType", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);
		ps.add("colour", 0);
		ps.add("level", 0);
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

	public byte getColour() {
		return colour;
	}

	public void setColour(byte colour) {
		this.colour = colour;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

}
