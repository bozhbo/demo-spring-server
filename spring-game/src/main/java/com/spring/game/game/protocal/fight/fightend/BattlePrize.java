package com.snail.webgame.game.protocal.fight.fightend;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 战斗奖励
 * @author zhangyq
 *
 */
public class BattlePrize extends MessageBody {
	
	private String no;//物品编号
	private int num;	//数量
	private byte itemType;// 抽取的物品类型 1资源 2经验 3道具 4装备 5武将 6神兵 
	
	private byte sweep; //第几次扫荡获得
	
	public BattlePrize() {
		super();
	}
	
	public BattlePrize(String no, int num,byte itemType, byte sweep) {
		super();
		this.no = no;
		this.num = num;
		this.itemType = itemType;
		this.sweep = sweep;
	}

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("no", "flashCode", 0);
		ps.add("num", 0);
		ps.add("itemType", 0);
		ps.add("sweep", 0);
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public byte getItemType() {
		return itemType;
	}

	public void setItemType(byte itemType) {
		this.itemType = itemType;
	}

	public byte getSweep() {
		return sweep;
	}

	public void setSweep(byte sweep) {
		this.sweep = sweep;
	}
	
}
