package com.snail.webgame.game.protocal.club.hire.entity;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.hero.query.HeroSkillRe;

public class HireHeroInfoRe extends MessageBody {
	private int roleId;
	private String roleName;
	private short level;
	private int price; // 雇佣的价格
	private int heroXmlNo;
	private long time; // 自己派出佣兵的时间
	private int income; // 自己佣兵的当前收益
	private byte quality;// 英雄品质
	private byte heroStar;// 武将星级
	private int heroId;// 英雄ID
	private int fightValue;
	private int sum; //收入总和
	// 装备
	private int equipCount;
	private List<EquipDetailRe> equipList = new ArrayList<EquipDetailRe>();
	
	// 技能
	private int skillCount;
	private List<HeroSkillRe> skillList = new ArrayList<HeroSkillRe>();
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("level", 0);
		ps.add("price", 0);
		ps.add("heroXmlNo", 0);
		ps.add("time", 0);
		ps.add("income", 0);
		ps.add("quality", 0);
		ps.add("heroStar", 0);
		ps.add("heroId", 0);
		ps.add("fightValue", 0);
		ps.add("sum", 0);
		ps.add("equipCount", 0);
		ps.addObjectArray("equipList", "com.snail.webgame.game.protocal.equip.query.EquipDetailRe", "equipCount");
		ps.add("skillCount", 0);
		ps.addObjectArray("skillList", "com.snail.webgame.game.protocal.hero.query.HeroSkillRe", "skillCount");

	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getHeroXmlNo() {
		return heroXmlNo;
	}

	public void setHeroXmlNo(int heroXmlNo) {
		this.heroXmlNo = heroXmlNo;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public byte getHeroStar() {
		return heroStar;
	}

	public void setHeroStar(byte heroStar) {
		this.heroStar = heroStar;
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

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

}
