package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;

public class HeroImageInfo extends BaseTO {
	private int roleId;// 雇佣人
	private int hireType; // 雇佣兵使用的战场类型 0 - 攻城略地

	private int imageRoleId;//被雇佣的角色id
	private int heroId;
	private byte deployStatus;// 上阵位置
	private int heroNo; // xmlNo;
	private int level;
	private int intimacyLevel;// 亲密度等级
	private int soldierLevel = 1; // 士兵等级
	private int quality;// 英雄品质
	private int star;// 星级

	// 装备<equipType,equipNo>
	private Map<Integer, Integer> equipMap = new HashMap<Integer, Integer>();
	// 技能<skillNo,skillLevel>
	private Map<Integer, Integer> skillMap = new HashMap<Integer, Integer>();

	private byte heroStatus = 1;// 0-战死 1-残血
	private int cutHp = 0;// 减少的血量

	private int fightValue;// 战斗力
	private Timestamp time; // 雇佣时间;
	private int flag; // 未作标注,预留

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getHireType() {
		return hireType;
	}

	public void setHireType(int hireType) {
		this.hireType = hireType;
	}
	
	public int getImageRoleId() {
		return imageRoleId;
	}

	public void setImageRoleId(int imageRoleId) {
		this.imageRoleId = imageRoleId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public byte getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(byte deployStatus) {
		this.deployStatus = deployStatus;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIntimacyLevel() {
		return intimacyLevel;
	}

	public void setIntimacyLevel(int intimacyLevel) {
		this.intimacyLevel = intimacyLevel;
	}

	public int getSoldierLevel() {
		return soldierLevel;
	}

	public void setSoldierLevel(int soldierLevel) {
		this.soldierLevel = soldierLevel;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public Map<Integer, Integer> getEquipMap() {
		return equipMap;
	}

	public void setEquipMap(Map<Integer, Integer> equipMap) {
		this.equipMap = equipMap;
	}

	public Map<Integer, Integer> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<Integer, Integer> skillMap) {
		this.skillMap = skillMap;
	}

	public byte getHeroStatus() {
		return heroStatus;
	}

	public void setHeroStatus(byte heroStatus) {
		this.heroStatus = heroStatus;
	}

	public int getCutHp() {
		return cutHp;
	}

	public void setCutHp(int cutHp) {
		this.cutHp = cutHp;
	}

	public Timestamp getTime() {
		return time;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
