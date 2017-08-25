package com.snail.webgame.game.protocal.hero.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroSkillRe extends MessageBody{

	private int skillNo; //技能编号
	private int skillLevel; //技能等级
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("skillNo", 0);
		ps.add("skillLevel", 0);
	}

	public int getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(int skillNo) {
		this.skillNo = skillNo;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
}

