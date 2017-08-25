package com.snail.webgame.game.protocal.hero.skillUp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroSkillUpResp extends MessageBody {

	private int result;
	private int heroId;// 英雄编号
	private int fightValue;// 战斗力

	private int skillNo; // 技能编号
	private int skillLevel; // 技能等级

	private byte sourceType;// 1:银子 2:金子 3:体力 7:玩家经验 8:竞技场货币-勇气点 9:征战四方货币 正义点
							// 10:工会币 15:玩家等级 28:跨服币
	// 32:战功 34:历史战功 49:体力值购买次数 50:银子购买次数 51:经验活动剩余次数 52:金币活动剩余次数 53:用户名修改次数
	// 54:历史最高战斗力 55：精力
	private int sourceChange;// 资源变动数,正值为增加,负值为减少

	private int tech; // 当前最新的技能点
	private long lastRecoverTechTime;// 上次回复技能点时间

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("heroId", 0);
		ps.add("fightValue", 0);

		ps.add("skillNo", 0);
		ps.add("skillLevel", 0);

		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);

		ps.add("tech", 0);
		ps.add("lastRecoverTechTime", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
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

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}

	public int getTech() {
		return tech;
	}

	public void setTech(int tech) {
		this.tech = tech;
	}

	public long getLastRecoverTechTime() {
		return lastRecoverTechTime;
	}

	public void setLastRecoverTechTime(long lastRecoverTechTime) {
		this.lastRecoverTechTime = lastRecoverTechTime;
	}
}
