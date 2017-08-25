package com.snail.webgame.game.protocal.hero.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroInfoRe extends MessageBody {

	private int heroId;// 英雄ID

	private int heroNo;// xml编号
	private short deployStatus;// 状态 0-普通 1-主英雄

	private short heroLevel;// 等级
	private int heroExp;// 经验

	private short intimacyLevel;// 亲密度等级
	private int intimacyValue;// 亲密度

	private byte quality;// 英雄品质
	private byte star;// 星级
	private int fightValue;// 战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);

		ps.add("heroNo", 0);
		ps.add("deployStatus", 0);

		ps.add("heroLevel", 0);
		ps.add("heroExp", 0);

		ps.add("intimacyLevel", 0);
		ps.add("intimacyValue", 0);

		ps.add("quality", 0);
		ps.add("star", 0);
		ps.add("fightValue", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public short getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(short deployStatus) {
		this.deployStatus = deployStatus;
	}

	public short getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(short heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public void setHeroExp(int heroExp) {
		this.heroExp = heroExp;
	}

	public short getIntimacyLevel() {
		return intimacyLevel;
	}

	public void setIntimacyLevel(short intimacyLevel) {
		this.intimacyLevel = intimacyLevel;
	}

	public int getIntimacyValue() {
		return intimacyValue;
	}

	public void setIntimacyValue(int intimacyValue) {
		this.intimacyValue = intimacyValue;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
}
