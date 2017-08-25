package com.snail.webgame.game.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

/**
 * 武将布阵记录
 * @author zenggang
 */
public class HeroRecord {

	private int id;// id;
	private int heroNo; // 编号
	private byte deployStatus;// 状态 0-普通 1-主英雄

	private int soldierLevel = 1;// 兵种等级
	private int heroLevel; // 等级
	private int intimacyLevel;// 亲密度等级
	private int quality;// 英雄品质 1:普通 2：真 3：鬼 4：神
	private int star;// 星级

	// 装备<equipType,EquipRecord>
	private Map<Integer, EquipRecord> equipMap = new HashMap<Integer, EquipRecord>();
	// 技能<skillNo,skillLevel>
	private Map<Integer, Integer> skillMap = new HashMap<Integer, Integer>();

	private byte heroStatus = 1;// 0-战死 1-残血
	private int cutHp = 0;// （远征）减少的血量

	// 主武将 附加属性
	// 神兵
	private List<WeaponRecord> weaponList = new ArrayList<WeaponRecord>();
	// 所有羁绊武将No
	private List<Integer> jbHeroNoList = new ArrayList<Integer>();
	// 兵种等级
	private Map<Byte, Integer> soldierMap = new HashMap<Byte, Integer>();
	// 背包锁定时装 <equipType,EquipRecord>
	private Map<Integer, EquipRecord> shizhuangMap = new HashMap<Integer, EquipRecord>();
	private RideRecord rideRecord = null;// 主武将坐骑
	private int chenhaoNo;// 称号
	// 工会科技 <buildType,lv>
	private Map<Integer, Integer> clubTechMap = new HashMap<Integer, Integer>();

	// 缓存 不序列化
	private int totalHp = 0;
	private int fightValue = 0;

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public byte getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(byte deployStatus) {
		this.deployStatus = deployStatus;
	}

	public int getSoldierLevel() {
		return soldierLevel;
	}

	public void setSoldierLevel(int soldierLevel) {
		this.soldierLevel = soldierLevel;
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

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public Map<Integer, EquipRecord> getEquipMap() {
		return equipMap;
	}

	public void setEquipMap(Map<Integer, EquipRecord> equipMap) {
		this.equipMap = equipMap;
	}

	public EquipRecord getEquipRecordbyEquipType(int equipType) {
		EquipXMLInfo xmlInfo = null;
		for (EquipRecord equipRecord : equipMap.values()) {
			xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equipRecord.getEquipNo());
			if (xmlInfo != null && xmlInfo.getEquipType() == equipType) {
				return equipRecord;
			}
		}
		return null;
	}

	public Map<Integer, EquipRecord> getShizhuangMap() {
		return shizhuangMap;
	}

	public void setShizhuangMap(Map<Integer, EquipRecord> shizhuangMap) {
		this.shizhuangMap = shizhuangMap;
	}

	public Map<Integer, Integer> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<Integer, Integer> skillMap) {
		this.skillMap = skillMap;
	}

	public List<WeaponRecord> getWeaponList() {
		return weaponList;
	}

	public void setWeaponList(List<WeaponRecord> weaponList) {
		this.weaponList = weaponList;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getJbHeroNoList() {
		return jbHeroNoList;
	}

	public void setJbHeroNoList(List<Integer> jbHeroNoList) {
		this.jbHeroNoList = jbHeroNoList;
	}

	public Map<Byte, Integer> getSoldierMap() {
		return soldierMap;
	}

	public void setSoldierMap(Map<Byte, Integer> soldierMap) {
		this.soldierMap = soldierMap;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public RideRecord getRideRecord() {
		return rideRecord;
	}

	public void setRideRecord(RideRecord rideRecord) {
		this.rideRecord = rideRecord;
	}

	public int getChenhaoNo() {
		return chenhaoNo;
	}

	public void setChenhaoNo(int chenhaoNo) {
		this.chenhaoNo = chenhaoNo;
	}

	public Map<Integer, Integer> getClubTechMap() {
		return clubTechMap;
	}

	public void setClubTechMap(Map<Integer, Integer> clubTechMap) {
		this.clubTechMap = clubTechMap;
	}

	@JsonIgnore
	public int getTotalHp() {
		return totalHp;
	}

	public void setTotalHp(int totalHp) {
		this.totalHp = totalHp;
	}

	@JsonIgnore
	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

}
