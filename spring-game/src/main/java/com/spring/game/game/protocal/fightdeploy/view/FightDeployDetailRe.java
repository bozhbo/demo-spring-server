package com.snail.webgame.game.protocal.fightdeploy.view;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightDeployDetailRe extends MessageBody {

	private int heroNo;// xml编号

	private byte deployPos;// 布阵位置
	private int heroLevel;// 等级
	private int intimacyLevel;// 亲密度等级
	private int star;// 星数
	private int quality;// 英雄品质
	private int fightValue;// 战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroNo", 0);

		ps.add("deployPos", 0);
		ps.add("heroLevel", 0);
		ps.add("intimacyLevel", 0);
		ps.add("star", 0);
		ps.add("quality", 0);
		ps.add("fightValue", 0);
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public byte getDeployPos() {
		return deployPos;
	}

	public void setDeployPos(byte deployPos) {
		this.deployPos = deployPos;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getIntimacyLevel() {
		return intimacyLevel;
	}

	public void setIntimacyLevel(int intimacyLevel) {
		this.intimacyLevel = intimacyLevel;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
}
