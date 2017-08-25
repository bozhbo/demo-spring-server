package com.snail.webgame.game.info;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 英雄信息
 */
public class HeroInfo extends BaseTO {

	public static final byte DEPLOY_TYPE_COMM = 0;// 普通
	public static final byte DEPLOY_TYPE_MAIN = 1;// 主英雄

	private int roleId; // 所属角色id
	private int heroNo; // 编号
	private byte deployStatus;// 上阵位置 1-主英雄 2-1号副将 3-二号副将 。。。

	private int heroLevel; // 等级
	private int heroExp;// 经验

	private int intimacyLevel;// 亲密度等级
	private int intimacyValue;// 亲密度

	private int quality;// 英雄品质 1:普通 2：真 3：鬼 4：神
	private int star;// 星级
	private String skillStr;// skillNo,skillLv;skillNo,skillLv;

	// 缓存
	private int fightValue;// 战斗力
	// 装备<equipId,EquipInfo>（战斗数据异步操作）
	private Map<Integer, EquipInfo> equipMap;

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

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public void setHeroExp(int heroExp) {
		this.heroExp = heroExp;
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

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public Map<Integer, EquipInfo> getEquipMap() {
		if (equipMap == null) {
			equipMap = new HashMap<Integer, EquipInfo>();
		}
		return equipMap;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setEquipMap(Map<Integer, EquipInfo> equipMap) {
		this.equipMap = equipMap;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getSkillStr() {
		return skillStr;
	}

	public void setSkillStr(String skillStr) {
		this.skillStr = skillStr;
	}

	public int getIntimacyLevel() {
		return intimacyLevel;
	}

	public void setIntimacyLevel(int intimacyLevel) {
		this.intimacyLevel = intimacyLevel;
	}

	public int getIntimacyValue() {
		return intimacyValue;
	}

	public void setIntimacyValue(int intimacyValue) {
		this.intimacyValue = intimacyValue;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}
}
