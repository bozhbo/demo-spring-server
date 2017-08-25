package com.snail.webgame.game.xml.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.xml.load.LoadHeroXML;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.info.EnchantXMLInfo;
import com.snail.webgame.game.xml.info.EnchantXMLUpgrade;
import com.snail.webgame.game.xml.info.EquipEffectConfigInfo;
import com.snail.webgame.game.xml.info.EquipExtraInfo;
import com.snail.webgame.game.xml.info.EquipRefineInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenConfigInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenInfo;
import com.snail.webgame.game.xml.info.EquipSuitConfigInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLUpgrade;

public class LoadEquipXML {

	/**
	 * 加载装备属性
	 * @param rootEle
	 */
	public static void loadEquip(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load Equip.xml error! no data!");
		}
		String itemNo = rootEle.attributeValue("No");
		if (!itemNo.startsWith(GameValue.EQUIP_N0)) {
			throw new RuntimeException("Load Equip.xml error! No not startsWith" + GameValue.EQUIP_N0);
		}

		EquipXMLInfo xmlInfo = new EquipXMLInfo();
		xmlInfo.setNo(Integer.parseInt(rootEle.attributeValue("No")));
		xmlInfo.setName(rootEle.attributeValue("Name"));
		xmlInfo.setHeroLevel(Integer.parseInt(rootEle.attributeValue("Level")));
		xmlInfo.setEquipType(Integer.parseInt(rootEle.attributeValue("Type")));
		xmlInfo.setMoney(Integer.parseInt(rootEle.attributeValue("Money")));
		xmlInfo.setSale(Integer.parseInt(rootEle.attributeValue("Sale")));
		xmlInfo.setQuality(Integer.parseInt(rootEle.attributeValue("Quality")));
		xmlInfo.setCharacter(Integer.parseInt(rootEle.attributeValue("Character")));
		xmlInfo.setResolve(Integer.parseInt(rootEle.attributeValue("Resolve")));
		xmlInfo.setResolveNum(Integer.parseInt(rootEle.attributeValue("Resolveno")));
		xmlInfo.setStrengthenExp(Integer.parseInt(rootEle.attributeValue("StrengthenExp")));
		xmlInfo.setSuitId(Integer.parseInt(rootEle.attributeValue("Suit")));
		xmlInfo.setResolmoney(Integer.parseInt(rootEle.attributeValue("Resolmoney")));
		xmlInfo.setShizhuangType(Integer.parseInt(rootEle.attributeValue("ShizhuangType")));
		
		if (Integer.parseInt(rootEle.attributeValue("Resolmoney")) > 0) {
			ConditionType type = ConditionType.attrParseType(Integer.parseInt(rootEle.attributeValue("Resolmoney")));

			if (type == null || !AbstractConditionCheck.isCurrencyType(type.getName())) {
				throw new RuntimeException("Load Equip.xml for resolve error! xmlInfo.getNo() " + xmlInfo.getNo());
			}
		}

		String composeEquipNo = rootEle.attributeValue("ComposeEquip");
		String gold = rootEle.attributeValue("Gold");
		if (composeEquipNo != null && composeEquipNo.length() > 0) {
			xmlInfo.setComposeEquipNo(Integer.parseInt(composeEquipNo));
		}

		if (gold != null && gold.length() > 0) {
			xmlInfo.setGold(Integer.parseInt(gold));
		}

		LoadHeroXML.loadHeroPro(xmlInfo, rootEle);

		List<?> itemList = rootEle.elements("Item");
		loadEquipItem(xmlInfo, itemList);

		List<?> refineList = rootEle.elements("Refine");
		loadEquipRefine(xmlInfo, refineList);

		List<?> strengthenList = rootEle.elements("Strengthen");
		loadEquipStrengthen(xmlInfo, strengthenList);

		List<?> extraList = rootEle.elements("Extra");
		loadEquipExtra(xmlInfo, extraList);

		List<?> enchantList = rootEle.elements("Enchant");
		loadEquipEnchant(xmlInfo, enchantList);

		if (EquipXMLInfoMap.getEquipXMLInfo(xmlInfo.getNo()) != null) {
			throw new RuntimeException("Load Equip.xml error! there is no = " + xmlInfo.getNo() + " repeat!");
		}
		EquipXMLInfoMap.addEquipXMLInfo(xmlInfo);
		//时装map处理
		if(xmlInfo.getEquipType() == 9 || xmlInfo.getEquipType() == 10){
			EquipXMLInfoMap.addShizhuang(xmlInfo);
		}
	}

	/**
	 * 装备附魔属性
	 * @param xmlInfo
	 * @param enchantList
	 */
	private static void loadEquipEnchant(EquipXMLInfo xmlInfo, List<?> enchantList) {
		if (enchantList != null && enchantList.size() > 0) {
			Element e = null;
			for (int i = 0; i < enchantList.size(); i++) {
				e = (Element) enchantList.get(i);
				if (e != null) {
					int lv = Integer.valueOf(e.attributeValue("Lv"));
					if (xmlInfo.getEnchantMap().containsKey(lv)) {
						throw new RuntimeException("Load Equip.xml Enchant error!  " + "there is equipNo = "
								+ xmlInfo.getNo() + " Enchant Lv = " + lv + " repeat!");
					}
					Map<HeroProType, Float> proMap = new HashMap<HeroProType, Float>();
					List<?> proList = e.elements("Effect");
					loadEquipEnchantLv(proMap, proList);

					xmlInfo.addEnchantMap(lv, proMap);
				}
			}
		}

	}

	private static void loadEquipEnchantLv(Map<HeroProType, Float> proMap, List<?> proList) {
		if (proList != null && proList.size() > 0) {
			Element e = null;
			HeroProType heroProType = null;
			for (int i = 0; i < proList.size(); i++) {
				e = (Element) proList.get(i);
				if (e != null) {
					int proType = Integer.valueOf(e.attributeValue("ProType"));
					float value = Integer.valueOf(e.attributeValue("Value"));
					heroProType = HeroProType.getHeroProType(proType);
					if (heroProType == null) {
						throw new RuntimeException("Load Equip.xml Enchant error proType:" + proType + "not exit");
					}
					
					Float val = proMap.get(heroProType);
					if(val == null){
						proMap.put(heroProType, value);
					} else {
						proMap.put(heroProType, val + value);
					}
				}
			}
		}
	}

	/**
	 * 加载装备属性 Item
	 * @param xmlInfo
	 * @param itemList
	 */
	private static void loadEquipItem(EquipXMLInfo xmlInfo, List<?> itemList) {
		if (itemList != null && itemList.size() > 0) {
			Element e = null;
			for (int i = 0; i < itemList.size(); i++) {
				e = (Element) itemList.get(i);
				if (e != null) {
					int itemNo = Integer.valueOf(e.attributeValue("ItemNo"));
					int itemNum = Integer.valueOf(e.attributeValue("Num"));
					if (xmlInfo.getItemMap().containsKey(itemNo)) {
						throw new RuntimeException("Load Equip.xml Item error!  " + "there is equipNo = "
								+ xmlInfo.getNo() + " itemNo = " + itemNo + " repeat!");
					}
					xmlInfo.getItemMap().put(itemNo, itemNum);
				}
			}
		}
	}

	/**
	 * 加载装备强化属性
	 * @param rootEle
	 */
	public static void loadEquipUp(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load Equip.xml error! no data!");
		}

		EquipStrengthenConfigInfo equipStrengthenConfigInfo = new EquipStrengthenConfigInfo();

		equipStrengthenConfigInfo.setQuality(Integer.parseInt(rootEle.attributeValue("Quality")));

		List<?> equipExpList = rootEle.elements("EquipExp");
		if (equipExpList != null && equipExpList.size() > 0) {
			EquipXMLUpgrade xmlInfo = null;
			Element e = null;
			for (int i = 0; i < equipExpList.size(); i++) {
				e = (Element) equipExpList.get(i);
				xmlInfo = new EquipXMLUpgrade();
				xmlInfo.setLevel(Integer.parseInt(e.attributeValue("Level")));
				xmlInfo.setHeroLevel(Integer.parseInt(e.attributeValue("HeroLevel")));
				xmlInfo.setExp(Integer.parseInt(e.attributeValue("Exp")));

				if (equipStrengthenConfigInfo.getEquipXMLUpgradeMap().get(xmlInfo.getLevel()) != null) {
					throw new RuntimeException("Load EquipStrengthen.xml error! there is quality = "
							+ equipStrengthenConfigInfo.getQuality() + "level " + xmlInfo.getLevel() + " repeat!");
				}

				equipStrengthenConfigInfo.getEquipXMLUpgradeMap().put(xmlInfo.getLevel(), xmlInfo);
			}
		}

		if (EquipXMLInfoMap.getEquipStrengthenConfigInfo(equipStrengthenConfigInfo.getQuality()) != null && !modify) {
			throw new RuntimeException("Load EquipStrengthen.xml error! there is quality = "
					+ equipStrengthenConfigInfo.getQuality() + " repeat!");
		}

		EquipXMLInfoMap.addEquipStrengthenConfigInfo(equipStrengthenConfigInfo);
	}

	/**
	 * 加载精炼基础配置
	 * @param xmlInfo
	 * @param itemList
	 */
	private static void loadEquipRefine(EquipXMLInfo xmlInfo, List<?> refineList) {
		if (refineList != null && refineList.size() > 0) {
			EquipRefineInfo info = null;
			Element e = null;
			for (int i = 0; i < refineList.size(); i++) {
				e = (Element) refineList.get(i);
				if (e != null) {
					info = new EquipRefineInfo();
					info.setLevel(Integer.valueOf(e.attributeValue("Lv")));
					info.setHp(Integer.valueOf(e.attributeValue("Hp")));
					info.setAttack(Integer.valueOf(e.attributeValue("Attack") == null ? "0" : e
							.attributeValue("Attack")));
					info.setAd(Integer.valueOf(e.attributeValue("Ad")));
					info.setMagicAttack(Integer.valueOf(e.attributeValue("MagicAttack")));
					info.setAttackDef(Integer.valueOf(e.attributeValue("AttackDef")));
					info.setMagicDef(Integer.valueOf(e.attributeValue("MagicDef")));
					int refineType = Integer.valueOf(e.attributeValue("RefineType"));
					if (refineType != 99) {
						if (refineType >= 100) {
							refineType = refineType - 100;
							info.setAddRate(true);
						}
						info.setRefineType(HeroProType.getHeroProType(refineType));
						if (info.getRefineType() == null) {
							throw new RuntimeException("Load EquipEffect.xml error RefineType :" + refineType
									+ "not exit in EquipNo: " + xmlInfo.getNo());
						}
					}

					info.setEffect(Integer.valueOf(e.attributeValue("Effect")));
					info.setConsume(Integer.valueOf(e.attributeValue("Consume")));
					info.setProp(Integer.valueOf(e.attributeValue("Prop")));
					info.setNum(Integer.valueOf(e.attributeValue("Num")));
					info.setLimitLv(Integer.valueOf(e.attributeValue("LimitLv")));
					info.setDemoney(Integer.valueOf(e.attributeValue("Demoney")));

					xmlInfo.getRefineMap().put(info.getLevel(), info);
				}
			}
		}
	}

	private static void loadEquipStrengthen(EquipXMLInfo xmlInfo, List<?> strengthenList) {
		if (strengthenList != null && strengthenList.size() > 0) {
			EquipStrengthenInfo info = null;
			Element e = null;
			for (int i = 0; i < strengthenList.size(); i++) {
				info = new EquipStrengthenInfo();
				e = (Element) strengthenList.get(i);
				info.setLevel(Integer.valueOf(e.attributeValue("Lv")));
				int refineType = Integer.valueOf(e.attributeValue("RefineType"));
				if (refineType >= 100) {
					refineType = refineType - 100;
					info.setAddRate(true);
				}
				info.setRefineType(HeroProType.getHeroProType(refineType));
				if (info.getRefineType() == null) {
					throw new RuntimeException("Load EquipStrengthen.xml error RefineType:" + refineType
							+ "not exit in EquipNo " + xmlInfo.getNo());
				}
				info.setEffect(Float.valueOf(e.attributeValue("Effect")));
				xmlInfo.getStrengthenList().add(info);
			}
		}
	}

	private static void loadEquipExtra(EquipXMLInfo xmlInfo, List<?> extraList) {
		if (extraList != null && extraList.size() > 0) {
			EquipExtraInfo info = null;
			Element e = null;
			for (int i = 0; i < extraList.size(); i++) {
				info = new EquipExtraInfo();
				e = (Element) extraList.get(i);
				int refineType = Integer.valueOf(e.attributeValue("ExtraType"));
				if (refineType == 99) {
					continue;
				}
				if (refineType >= 100) {
					refineType = refineType - 100;
					info.setAddRate(true);
				}

				info.setRefineType(HeroProType.getHeroProType(refineType));
				if (info.getRefineType() == null) {
					throw new RuntimeException("Load Equip.xml error ExtraType:" + refineType + "not exit in EquipNo "
							+ xmlInfo.getNo());
				}
				info.setEffect(Double.valueOf(e.attributeValue("Effect")));
				xmlInfo.getExtraList().add(info);
			}
		}
	}

	public static void loadEquipEffect(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load EquipEffect.xml error! no data!");
		}

		EquipEffectConfigInfo xmlInfo = null;
		int character = Integer.valueOf(rootEle.attributeValue("No"));
		List<?> eleList = rootEle.elements("Effect");

		if (eleList != null && eleList.size() > 0) {
			Element e = null;
			for (int i = 0; i < eleList.size(); i++) {
				e = (Element) eleList.get(i);
				xmlInfo = new EquipEffectConfigInfo();
				xmlInfo.setType(Integer.parseInt(e.attributeValue("Type")));
				xmlInfo.setHp(Integer.parseInt(e.attributeValue("Hp")));
				xmlInfo.setAttack(Integer.parseInt(e.attributeValue("Attack")));
				xmlInfo.setAd(Integer.parseInt(e.attributeValue("Ad")));
				xmlInfo.setMagicAttack(Integer.parseInt(e.attributeValue("MagicAttack")));
				xmlInfo.setAttackDef(Integer.parseInt(e.attributeValue("AttackDef")));
				xmlInfo.setMagicDef(Integer.parseInt(e.attributeValue("MagicDef")));

				Map<Integer, EquipEffectConfigInfo> map = EquipXMLInfoMap.getEquipEffectConfigInfoMap(character);
				if (map != null) {
					if (map.get(xmlInfo.getType()) != null && !modify)
						throw new RuntimeException("Load EquipEffect.xml error! there is Character = " + character
								+ "Type " + xmlInfo.getType() + " repeat!");
				}

				EquipXMLInfoMap.addEquipEffectConfigInfo(character, xmlInfo);
			}
		}

	}

	public static void loadEquipSuit(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load EquipEffect.xml error! no data!");
		}

		EquipSuitConfigInfo xmlInfo = null;

		int no = (Integer.parseInt(rootEle.attributeValue("No")));

		List<?> eleList = rootEle.elements("Item");

		if (eleList != null && eleList.size() > 0) {
			Element e = null;

			for (int i = 0; i < eleList.size(); i++) {
				xmlInfo = new EquipSuitConfigInfo();
				e = (Element) eleList.get(i);
				xmlInfo.setNo(Integer.parseInt(e.attributeValue("No")));
				xmlInfo.setNum(Integer.parseInt(e.attributeValue("Num")));

				int type = Integer.valueOf(e.attributeValue("Type"));
				if (type >= 100) {
					type = type - 100;
					xmlInfo.setAddRate(true);
				}
				xmlInfo.setType(HeroProType.getHeroProType(type));
				if (xmlInfo.getType() == null) {
					throw new RuntimeException("Load EquipSuit.xml error Type:" + type + "not exit in No "
							+ xmlInfo.getNo());
				}
				xmlInfo.setEffect(Integer.parseInt(e.attributeValue("Effect")));

				EquipXMLInfoMap.addEquipSuitConfigInfo(no, xmlInfo);
			}
		}
	}

	public static void loadEnchant(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load Enchant.xml error! no data!");
		}
		EnchantXMLInfo xmlInfo = new EnchantXMLInfo();
		xmlInfo.setQuailty(Integer.parseInt(rootEle.attributeValue("No").trim()));

		List<?> lvList = rootEle.elements("Level");
		loadEnchantlv(xmlInfo, lvList);
		if (EquipXMLInfoMap.getEnchantXMLInfo(xmlInfo.getQuailty()) != null && !modify) {
			throw new RuntimeException("Load Enchant.xml error! there is no = " + xmlInfo.getQuailty() + " repeat!");
		}
		EquipXMLInfoMap.addEnchantXMLInfo(xmlInfo);
	}

	private static void loadEnchantlv(EnchantXMLInfo xmlInfo, List<?> lvList) {
		if (lvList != null && lvList.size() > 0) {
			Element e = null;
			EnchantXMLUpgrade upgrade = null;
			for (int i = 0; i < lvList.size(); i++) {
				e = (Element) lvList.get(i);
				if (e != null) {
					upgrade = new EnchantXMLUpgrade();
					upgrade.setNo(Integer.parseInt(e.attributeValue("No").trim()));
					upgrade.setEnchantNum(Integer.parseInt(e.attributeValue("EnchantNum").trim()));
					upgrade.setMoney(Integer.parseInt(e.attributeValue("Money").trim()));
					upgrade.setLevel(Integer.parseInt(e.attributeValue("Lv").trim()));
					if (xmlInfo.getEnchantXMLUpgrade(upgrade.getNo()) != null) {
						throw new RuntimeException("Load Enchant.xml Level error level:" + upgrade.getNo() + "repeat");
					}
					xmlInfo.addEnchantXMLUpgrade(upgrade);
				}
			}
		}
	}
}
