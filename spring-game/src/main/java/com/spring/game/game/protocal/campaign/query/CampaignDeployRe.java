package com.snail.webgame.game.protocal.campaign.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CampaignDeployRe extends MessageBody {

	private int heroNo;// xml编号

	private byte deployPos;// 布阵位置
	private int heroLevel;// 等级
	private int star;// 星数
	private int quality;// 英雄品质
	private int fightValue;// 战斗力

	private int hp;// 总血量
	private int currHp;// 当前血量

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroNo", 0);

		ps.add("deployPos", 0);
		ps.add("heroLevel", 0);
		ps.add("star", 0);
		ps.add("quality", 0);
		ps.add("fightValue", 0);

		ps.add("hp", 0);
		ps.add("currHp", 0);
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

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getCurrHp() {
		return currHp;
	}

	public void setCurrHp(int currHp) {
		this.currHp = currHp;
	}
}
