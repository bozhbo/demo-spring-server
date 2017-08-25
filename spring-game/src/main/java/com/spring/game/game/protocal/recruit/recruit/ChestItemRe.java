package com.snail.webgame.game.protocal.recruit.recruit;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChestItemRe extends MessageBody {

	public static final byte TYPE_ITEM = 1;// 1道具
	public static final byte TYPE_EQUIP = 2;// 2装备
	public static final byte TYPE_STAR_PLAY = 4;// 4武将  不含有该英雄 播放动画
	public static final byte TYPE_WEAP = 7;// 神兵

	private int id; // 数据库ID 用于客户端更新缓存使用
	private int itemNo;// 抽取的物品编号
	private byte itemNum;// 抽取的物品数量
	private byte itemType;// 抽取的物品类型 1道具 2装备 3英雄
	
	
	public ChestItemRe(){
		
	}
	
	public ChestItemRe(byte itemType){
		this.itemType = itemType;
	}

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("id", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);

		ps.add("itemType", 0);
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public byte getItemNum() {
		return itemNum;
	}

	public void setItemNum(byte itemNum) {
		this.itemNum = itemNum;
	}

	public byte getItemType() {
		return itemType;
	}

	public void setItemType(byte itemType) {
		this.itemType = itemType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
