package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;

public class EquipXMLInfo extends HeroPropertyInfo {

	private int no;// 编号
	private String name;// 名称
	private int heroLevel;// 装备需要武将等级
	private int equipType;// 1武器,2盔甲,3头盔,4护腕,5披风,6战靴,7护符,8战旗
	private int money;// 合成需要的银子
	private int sale;// 出售价格
	private int quality;// 装备颜色 1:白 2:绿 3:蓝 4:紫 5:橙
	private int character; // 品质
	private int composeEquipNo;// 合成后的装备
	private int resolve; // 熔炼获得的道具ID
	private int ResolveNum; // 熔炼获得的道具数量
	private int StrengthenExp;// 强化分解装备的等级
	private int suitId;
	private int gold; // 分解消化的金币
	private int resolmoney;
	private int shizhuangType; //时装大类型

	// 合成材料
	private Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();

	private Map<Integer, EquipRefineInfo> refineMap = new HashMap<Integer, EquipRefineInfo>();// key-EquipRefineInfo的level
																								// :
																								// value-EquipRefineInfo

	private List<EquipStrengthenInfo> strengthenList = new ArrayList<EquipStrengthenInfo>();

	private List<EquipExtraInfo> extraList = new ArrayList<EquipExtraInfo>();// 装备上附带的额外属性
	
	// 附魔属性<等级 <附加的属性值>>
	private int maxEnchantLv = 0;
	private Map<Integer, Map<HeroProType, Float>> enchantMap = new HashMap<Integer, Map<HeroProType, Float>>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getSale() {
		return sale;
	}

	public void setSale(int sale) {
		this.sale = sale;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getComposeEquipNo() {
		return composeEquipNo;
	}

	public void setComposeEquipNo(int composeEquipNo) {
		this.composeEquipNo = composeEquipNo;
	}

	public Map<Integer, Integer> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<Integer, Integer> itemMap) {
		this.itemMap = itemMap;
	}

	public int getResolve() {
		return resolve;
	}

	public void setResolve(int resolve) {
		this.resolve = resolve;
	}

	public int getResolveNum() {
		return ResolveNum;
	}

	public void setResolveNum(int resolveNum) {
		ResolveNum = resolveNum;
	}

	public int getStrengthenExp() {
		return StrengthenExp;
	}

	public void setStrengthenExp(int strengthenExp) {
		StrengthenExp = strengthenExp;
	}

	public Map<Integer, EquipRefineInfo> getRefineMap() {
		return refineMap;
	}

	public void setRefineMap(Map<Integer, EquipRefineInfo> refineMap) {
		this.refineMap = refineMap;
	}

	public List<EquipStrengthenInfo> getStrengthenList() {
		return strengthenList;
	}

	public void setStrengthenList(List<EquipStrengthenInfo> strengthenList) {
		this.strengthenList = strengthenList;
	}

	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getResolmoney() {
		return resolmoney;
	}

	public void setResolmoney(int resolmoney) {
		this.resolmoney = resolmoney;
	}

	public List<EquipExtraInfo> getExtraList() {
		return extraList;
	}

	public void setExtraList(List<EquipExtraInfo> extraList) {
		this.extraList = extraList;
	}

	public int getShizhuangType() {
		return shizhuangType;
	}

	public void setShizhuangType(int shizhuangType) {
		this.shizhuangType = shizhuangType;
	}


	/**
	 * 找出精炼的最大等级
	 * @return
	 */
	public int getRefineMaxLevel() {
		int level = 0;
		for (Map.Entry<Integer, EquipRefineInfo> m : refineMap.entrySet()) {
			if (m.getKey() > level) {
				level = m.getKey();
			}
		}
		return level;
	}

	public Map<Integer, Map<HeroProType, Float>> getEnchantMap() {
		return enchantMap;
	}

	public void addEnchantMap(int lv, Map<HeroProType, Float> proMap) {
		if (lv > maxEnchantLv) {
			maxEnchantLv = lv;
		}
		enchantMap.put(lv, proMap);
	}

	public int getMaxEnchantLv() {
		return maxEnchantLv;
	}
}
