package com.snail.webgame.game.tool.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * RoleData修改角色信息
 * 
 * @author lixiaojun
 */
public class RoleVo {
	private String roleName; // 角色名称
	private byte roleRace; // 从属势力
	private long money; // 银子
	private long coin; // 金子
	private short sp; // 行动值
	private long courage; // 勇气点
	private long justice; // 正义点
	private int kuafuMoney; // 跨服币
	private int exploit; // 战功
	private int guide;// 新手引导标记 现在默认45是跳过新手引导
	private int rankShow;// 排行榜是否显示 1-不显示 其他-显示
	private int isAdvert;// 是否是代言人 1-是 其他-不是
	private String chgName;// 改名
	
	private int equip;//钢之魂魄
	private int equipStrengeh;//装备碎片
	private int starMoney;//武将魂魄
	
	private String allTitles;//所有称号
	
	private List<HeroVo> heroList = new ArrayList<HeroVo>();
	
	// 主角技能修改
	private List<SkillVo> skillList = new ArrayList<SkillVo>();
	
	// 道具删除
	private List<DeleteVo> itemList = new ArrayList<DeleteVo>();
	
	// 装备删除
	private List<DeleteVo> equipList = new ArrayList<DeleteVo>();

	
	
	public String getAllTitles() {
		return allTitles;
	}

	public void setAllTitles(String allTitles) {
		this.allTitles = allTitles;
	}

	public int getEquip() {
		return equip;
	}

	public void setEquip(int equip) {
		this.equip = equip;
	}

	public int getEquipStrengeh() {
		return equipStrengeh;
	}

	public void setEquipStrengeh(int equipStrengeh) {
		this.equipStrengeh = equipStrengeh;
	}

	public int getStarMoney() {
		return starMoney;
	}

	public void setStarMoney(int starMoney) {
		this.starMoney = starMoney;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public byte getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(byte roleRace) {
		this.roleRace = roleRace;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getCoin() {
		return coin;
	}

	public void setCoin(long coin) {
		this.coin = coin;
	}

	public short getSp() {
		return sp;
	}

	public void setSp(short sp) {
		this.sp = sp;
	}

	public long getCourage() {
		return courage;
	}

	public void setCourage(long courage) {
		this.courage = courage;
	}

	public long getJustice() {
		return justice;
	}

	public void setJustice(long justice) {
		this.justice = justice;
	}

	public int getKuafuMoney() {
		return kuafuMoney;
	}

	public void setKuafuMoney(int kuafuMoney) {
		this.kuafuMoney = kuafuMoney;
	}

	public int getExploit() {
		return exploit;
	}

	public void setExploit(int exploit) {
		this.exploit = exploit;
	}

	public List<HeroVo> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<HeroVo> heroList) {
		this.heroList = heroList;
	}

	public int getGuide() {
		return guide;
	}

	public void setGuide(int guide) {
		this.guide = guide;
	}

	public int getRankShow() {
		return rankShow;
	}

	public void setRankShow(int rankShow) {
		this.rankShow = rankShow;
	}

	public int getIsAdvert() {
		return isAdvert;
	}

	public void setIsAdvert(int isAdvert) {
		this.isAdvert = isAdvert;
	}

	public String getChgName() {
		return chgName;
	}

	public void setChgName(String chgName) {
		this.chgName = chgName;
	}

	public List<SkillVo> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<SkillVo> skillList) {
		this.skillList = skillList;
	}

	public List<DeleteVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<DeleteVo> itemList) {
		this.itemList = itemList;
	}

	public List<DeleteVo> getEquipList() {
		return equipList;
	}

	public void setEquipList(List<DeleteVo> equipList) {
		this.equipList = equipList;
	}

}
