package com.snail.webgame.game.protocal.hero.skillUp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroSkillUpReq extends MessageBody {

	private int heroId;// 英雄编号
	private int skillNo;// 装备编号
	private byte upType;// 0-升1级，1-max

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("skillNo", 0);
		ps.add("upType", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(int skillNo) {
		this.skillNo = skillNo;
	}

	public byte getUpType() {
		return upType;
	}

	public void setUpType(byte upType) {
		this.upType = upType;
	}
}
