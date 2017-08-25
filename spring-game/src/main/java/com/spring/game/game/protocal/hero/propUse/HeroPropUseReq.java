package com.snail.webgame.game.protocal.hero.propUse;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroPropUseReq extends MessageBody {

	private byte action;// 0-正常使用道具 1-一键升级亲密度
	private int heroId;
	private int itemNo;
	private int itemNum;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("action", 0);
		
		ps.add("heroId", 0);
		ps.add("itemNo", 0);
		ps.add("itemNum", 0);
	}

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
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
}
