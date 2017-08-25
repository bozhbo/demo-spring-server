package com.snail.webgame.game.protocal.hero.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;

public class HeroDetailRe extends MessageBody {

	private int heroId;// 英雄ID

	private HeroInfoRe heroInfo = new HeroInfoRe();// 属性

	// 技能
	private int skillCount;
	private List<HeroSkillRe> skillList = new ArrayList<HeroSkillRe>();

	// 装备
	private int equipCount;
	private List<EquipDetailRe> equipList = new ArrayList<EquipDetailRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.addObject("heroInfo");

		ps.add("skillCount", 0);
		ps.addObjectArray("skillList", "com.snail.webgame.game.protocal.hero.query.HeroSkillRe", "skillCount");

		ps.add("equipCount", 0);
		ps.addObjectArray("equipList", "com.snail.webgame.game.protocal.equip.query.EquipDetailRe", "equipCount");
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public HeroInfoRe getHeroInfo() {
		return heroInfo;
	}

	public void setHeroInfo(HeroInfoRe heroInfo) {
		this.heroInfo = heroInfo;
	}

	public int getSkillCount() {
		return skillCount;
	}

	public void setSkillCount(int skillCount) {
		this.skillCount = skillCount;
	}

	public List<HeroSkillRe> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<HeroSkillRe> skillList) {
		this.skillList = skillList;
	}

	public int getEquipCount() {
		return equipCount;
	}

	public void setEquipCount(int equipCount) {
		this.equipCount = equipCount;
	}

	public List<EquipDetailRe> getEquipList() {
		return equipList;
	}

	public void setEquipList(List<EquipDetailRe> equipList) {
		this.equipList = equipList;
	}
}
