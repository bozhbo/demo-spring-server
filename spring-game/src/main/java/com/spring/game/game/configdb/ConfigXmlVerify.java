package com.snail.webgame.game.configdb;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.SoldierXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCost;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCostItem;
import com.snail.webgame.game.common.xml.info.HeroColourXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.ShopXMLInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopItem;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopRoleLevels;

public class ConfigXmlVerify {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 验证xml数据
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyAllConfigXMLData() {
		return verifyBase() // 验证Base.xml
				&& verifyHero()// 验证英雄库
				&& verifyEquip() // 验证装备库
				&& verifyShop();// 验证Shop.xml
	}

	/**
	 * 验证Base.xml
	 * @return
	 */
	private static boolean verifyBase() {
		if (EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_BAG_EQUIP) == null) {
			logger.error("Base.xml no:Condition005 error");
			return false;
		}
		for (int propNo : GameValue.NEW_BAG_PROP) {
			if (PropXMLInfoMap.getPropXMLInfo(propNo) == null) {
				logger.error("Base.xml no:Condition226 error");
				return false;
			}
		}
		EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_HERO_WEAPON);
		if (equipXMLInfo == null || equipXMLInfo.getEquipType() != 1) {
			logger.error("Base.xml no:Condition115 error");
			return false;
		}
		equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_HERO_CLOTH);
		if (equipXMLInfo == null || equipXMLInfo.getEquipType() != 2) {
			logger.error("Base.xml no:Condition116 error");
			return false;
		}
		equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_HERO_HELMET);
		if (equipXMLInfo == null || equipXMLInfo.getEquipType() != 3) {
			logger.error("Base.xml no:Condition117 error");
			return false;
		}
		equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_HERO_CUFF);
		if (equipXMLInfo == null || equipXMLInfo.getEquipType() != 4) {
			logger.error("Base.xml no:Condition118 error");
			return false;
		}
		equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_HERO_SHOE);
		if (equipXMLInfo == null || equipXMLInfo.getEquipType() != 6) {
			logger.error("Base.xml no:Condition119 error");
			return false;
		}
		equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_HERO_CLOAK);
		if (equipXMLInfo == null || equipXMLInfo.getEquipType() != 5) {
			logger.error("Base.xml no:Condition120 error");
			return false;
		}

		List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLList(GameValue.ARENA_FIGHT_FP_BAG);
		if (list == null || list.size() <= 0) {
			logger.error("Base.xml no:Condition142 error");
			return false;
		}

		return true;
	}

	/**
	 * 验证英雄
	 * @return
	 */
	private static boolean verifyHero() {
		int maxMainLv = HeroXMLInfoMap.getMaxMainLv();
		for (int i = 1; i <= maxMainLv; i++) {
			if (HeroXMLInfoMap.getMainHeroLevelXMLUpgrade(i) == null) {
				logger.error("HeroUp.xml No:1 level:" + i + " not exit");
				return false;
			}
		}
		int maxOtherLv = HeroXMLInfoMap.getMaxOtherLv();
		for (int i = 1; i <= maxOtherLv; i++) {
			if (HeroXMLInfoMap.getOtherHeroLevelXMLUpgrade(i) == null) {
				logger.error("HeroUp.xml No:2 level:" + i + " not exit");
				return false;
			}
		}
		int maxColor = HeroXMLInfoMap.getMaxColor();
		HeroColorXMLUpCost upCost = null;
		for (int i = 1; i <= maxColor; i++) {
			upCost = HeroXMLInfoMap.getHeroColorXMLUpCost(i);
			if (upCost == null) {
				logger.error("HeroUpCost.xml No:" + i + " not exit");
				return false;
			}
			for (Map<Integer, HeroColorXMLUpCostItem> itemMap : upCost.getItemMap().values()) {
				for (HeroColorXMLUpCostItem item : itemMap.values()) {
					int itemNo = item.getItemNo();
					if (String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)) {
						if (EquipXMLInfoMap.getEquipXMLInfo(itemNo) == null) {
							logger.error("HeroUpCost.xml NeedMateral no:" + itemNo + " error");
							return false;
						}
					} else if (String.valueOf(itemNo).startsWith(GameValue.PROP_N0)) {
						if (PropXMLInfoMap.getPropXMLInfo(itemNo) == null) {
							logger.error("HeroUpCost.xml NeedMateral no:" + itemNo + " error");
							return false;
						}
					} else {
						logger.error("HeroUpCost.xml NeedMateral no:" + itemNo + " error");
						return false;
					}
				}
			}
		}

		for (HeroSkillXMLInfo skillInfo : HeroXMLInfoMap.getSkillMap().values()) {
			for (int i = 1; i <= skillInfo.getMaxSkillLv(); i++) {
				if (skillInfo.getHeroSkillXMLUpgrade(i) == null) {
					logger.error("SkillUp.xml No:" + skillInfo.getNo() + " Level:" + i + " not exit");
					return false;
				}
			}
		}

		// 验证HeroCloseCost
		int maxIntimacy = HeroXMLInfoMap.getMaxIntimacy();
		for (int i = 1; i <= maxIntimacy; i++) {
			if (HeroXMLInfoMap.getIntimacyCost(i) == null) {
				logger.error("HeroCloseCost.xml No:" + i + " not exit");
				return false;
			}
		}

		// 验证heroClose
		for (int closeType : HeroXMLInfoMap.getIntimacyTypes()) {
			for (int i = 1; i <= maxIntimacy; i++) {
				if (HeroXMLInfoMap.getHeroIntimacyXMLUpgrade(closeType, i) == null) {
					logger.error("heroClose.xml no:" + closeType + " level:" + i + " not exit ");
					return false;
				}
			}
		}

		Map<Integer, HeroXMLInfo> heroXmlMap = HeroXMLInfoMap.getHeroMap();
		for (HeroXMLInfo heroXmlInfo : heroXmlMap.values()) {
			int heroType = heroXmlInfo.getHeroType();
			if (!SoldierXMLInfoMap.containsKey((byte) heroType)) {
				logger.error("Hero.xml no:" + heroXmlInfo.getNo() + " HeroType:" + heroType + "not exit in Soldier.xml");
				return false;
			}

			if (heroXmlInfo.getInitial() == 0) {
				// 验证HeroClose.xml
				if (!HeroXMLInfoMap.isExitIntimacyType(heroXmlInfo.getCloseType())) {
					logger.error("Hero.xml no:" + heroXmlInfo.getNo() + " CloseType:" + heroXmlInfo.getCloseType()
							+ " not exit");
					return false;
				}
				// 验证武将星石编号
				int chipNo = heroXmlInfo.getChipNo();
				PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(chipNo);
				if (propXml == null) {
					logger.error("Hero.xml ChipNo:" + chipNo + " not exit in " + heroXmlInfo.getNo());
					return false;
				}

				// 验证HeroUpColour.xml
				if (heroXmlInfo.getColourMap() == null || heroXmlInfo.getColourMap().size() <= 0) {
					logger.error("Hero.xml no:" + heroXmlInfo.getNo() + " not having colour info");
					return false;
				}
				for (int color = 1; color <= maxColor; color++) {
					HeroColourXMLUpgrade colourMap = heroXmlInfo.getColourMap().get(color);
					if (colourMap == null) {
						logger.error("HeroUpColour.xml Upgrade No:" + color + " not all in " + heroXmlInfo.getNo());
						return false;
					}
					for (int equipType : HeroXMLInfoMap.getHeroColorXMLUpCost(color).getItemMap().keySet()) {
						if (!colourMap.getCostEquipMap().containsKey(equipType)) {
							logger.error("HeroUpColour.xml Upgrade No:" + color + " NeedMateral Type:" + equipType
									+ " EquipType not exit in " + heroXmlInfo.getNo());
							return false;
						}
					}
				}

			}
			// 验证HeroSkill.xml
			if (heroXmlInfo.getSkillMap() == null || heroXmlInfo.getSkillMap().size() <= 0) {
				logger.error("Hero.xml no:" + heroXmlInfo.getNo() + " not having skill info");
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证装备库
	 * @return
	 */
	private static boolean verifyEquip() {
		Map<Integer, EquipXMLInfo> equipMap = EquipXMLInfoMap.getEquipMap();
		for (EquipXMLInfo equipXML : equipMap.values()) {
			int composeNo = equipXML.getComposeEquipNo();
			if (composeNo != 0) {
				EquipXMLInfo composeXML = EquipXMLInfoMap.getEquipXMLInfo(composeNo);
				if (composeXML == null) {
					logger.error("equip.xml ComposeEquip can not find in " + equipXML.getNo());
					return false;
				}
				if (composeXML.getEquipType() != equipXML.getEquipType()) {
					logger.error("equip.xml ComposeEquip type error in " + equipXML.getNo());
					return false;
				}
			}
			Map<Integer, Integer> itemMap = equipXML.getItemMap();
			for (Integer itemNo : itemMap.keySet()) {
				PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(itemNo);
				if (propXml == null) {
					logger.error("equip.xml ItemNo=" + itemNo + " error in " + equipXML.getNo());
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * 验证Shop.xml
	 * @return
	 */
	private static boolean verifyShop() {
		Map<Integer, ShopXMLInfo> map = ShopXMLInfoMap.getMap();
		if (map != null) {
			for (ShopXMLInfo xmlInfo : map.values()) {
				List<ShopRoleLevels> roleLvList = xmlInfo.getRoleLvList();
				if (roleLvList != null) {
					for (ShopRoleLevels roleLv : roleLvList) {
						Map<Integer, List<ShopItem>> items = roleLv.getItems();
						if (items != null) {
							for (Integer postion : items.keySet()) {
								List<ShopItem> shopItems = items.get(postion);
								if (shopItems != null) {
									for (ShopItem item : shopItems) {
										int itemNo = item.getItemNo();
										if (String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)) {
											if (EquipXMLInfoMap.getEquipXMLInfo(itemNo) == null) {
												logger.error("Shop.xml ItemNo:{},can not find ", itemNo);
												return false;
											}
										} else if (String.valueOf(itemNo).startsWith(GameValue.PROP_N0)) {
											if (PropXMLInfoMap.getPropXMLInfo(itemNo) == null) {
												logger.error("Shop.xml ItemNo:{},can not find ", itemNo);
												return false;
											}
										} else if (String.valueOf(itemNo).startsWith(GameValue.WEAPAN_NO)) {
											if (WeaponXmlInfoMap.getWeaponXmlInfoByNo(itemNo) == null) {
												logger.error("Shop.xml ItemNo:{},can not find ", itemNo);
												return false;
											}
										} else {
											logger.error("Shop.xml ItemNo:{},can not find ", itemNo);
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * 验证RecruitDepot
	 * @return
	 */
	// private static boolean verifyRecruitDepot() {
	// HashMap<Integer, RecruitDepotXMLInfo> map =
	// RecruitDepotXMLInfoMap.getMap();
	// for (RecruitDepotXMLInfo info : map.values()) {
	// for (RecruitItemXMLInfo item : info.getItems()) {
	// if (!verifyItemNo(item.getItemNo())) {
	// logger.error("RecruitDepot.xml no:" + info.getNo() +
	// " ItemNo:{},can not find ", item.getItemNo());
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	public static boolean verifyItemNo(String itemNo) {
		if (AbstractConditionCheck.isResourceType(itemNo)) {

		} else if (itemNo.startsWith(GameValue.PROP_N0)) {
			if (PropXMLInfoMap.getPropXMLInfo(Integer.parseInt(itemNo)) == null) {
				return false;
			}
		} else if (itemNo.startsWith(GameValue.EQUIP_N0)) {
			if (EquipXMLInfoMap.getEquipXMLInfo(Integer.parseInt(itemNo)) == null) {
				return false;
			}
		} else if (itemNo.startsWith(GameValue.WEAPAN_NO)) {
			if (WeaponXmlInfoMap.getWeaponXmlInfoByNo(Integer.parseInt(itemNo)) == null) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
