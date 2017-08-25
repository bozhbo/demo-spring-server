package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.xml.cache.WeaponExpXmlInfoMap;
import com.snail.webgame.game.xml.cache.WeaponSuitXmlInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;
import com.snail.webgame.game.xml.info.WeaponExpXmlInfo;
import com.snail.webgame.game.xml.info.WeaponLv;
import com.snail.webgame.game.xml.info.WeaponRefine;
import com.snail.webgame.game.xml.info.WeaponSpecial;
import com.snail.webgame.game.xml.info.WeaponSuitNum;
import com.snail.webgame.game.xml.info.WeaponSuitXmlInfo;
import com.snail.webgame.game.xml.info.WeaponXmlInfo;

/**
 * 神兵
 * @author xiasd
 */
public class LoadWeaponXML {
	/**
	 * Magic.xml
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int type = Integer.valueOf(rootEle.attributeValue("Type").trim());
			int exp = Integer.valueOf(rootEle.attributeValue("Exp").trim());
			int suit = Integer.valueOf(rootEle.attributeValue("Suit").trim());

			List<?> stepEles = rootEle.elements("LV");
			WeaponLv weaponLv = null;

			if (stepEles != null && stepEles.size() > 0) {
				Element tempElement = (Element) stepEles.get(0);
				weaponLv = new WeaponLv();// 只配了一个一级的基础属性，其他的根据公式走

				int hp = Integer.valueOf(tempElement.attributeValue("Hp").trim());
				int attack = Integer.valueOf(tempElement.attributeValue("Attack").trim());
				int ad = Integer.valueOf(tempElement.attributeValue("Ad").trim());
				int magicAttack = Integer.valueOf(tempElement.attributeValue("MagicAttack").trim());
				int attackDef = Integer.valueOf(tempElement.attributeValue("AttackDef").trim());
				int magicDef = Integer.valueOf(tempElement.attributeValue("MagicDef").trim());

				weaponLv.setHp(hp);
				weaponLv.setAttack(attack);
				weaponLv.setAd(ad);
				weaponLv.setAttackDef(attackDef);
				weaponLv.setMagicAttack(magicAttack);
				weaponLv.setMagicDef(magicDef);
			}
			
			List<?> upgradeEles = rootEle.elements("Update");
			WeaponLv upgradeWeaponLv = null;
			
			if (upgradeEles != null && upgradeEles.size() > 0) {
				Element tempElement = (Element) upgradeEles.get(0);
				upgradeWeaponLv = new WeaponLv();// 只配了一个一级的基础属性，其他的根据公式走
				
				int hp = Integer.valueOf(tempElement.attributeValue("Hp").trim());
				int attack = Integer.valueOf(tempElement.attributeValue("Attack").trim());
				int ad = Integer.valueOf(tempElement.attributeValue("Ad").trim());
				int magicAttack = Integer.valueOf(tempElement.attributeValue("MagicAttack").trim());
				int attackDef = Integer.valueOf(tempElement.attributeValue("AttackDef").trim());
				int magicDef = Integer.valueOf(tempElement.attributeValue("MagicDef").trim());
				
				upgradeWeaponLv.setHp(hp);
				upgradeWeaponLv.setAttack(attack);
				upgradeWeaponLv.setAd(ad);
				upgradeWeaponLv.setAttackDef(attackDef);
				upgradeWeaponLv.setMagicAttack(magicAttack);
				upgradeWeaponLv.setMagicDef(magicDef);
			}

			// 隐藏属性
			List<?> specialEles = rootEle.elements("Special");
			List<WeaponSpecial> specialList = new ArrayList<WeaponSpecial>();

			if (stepEles != null) {

				WeaponSpecial weaponSpecial;

				for (int i = 0; i < specialEles.size(); i++) {
					Element tempElement = (Element) specialEles.get(i);
					weaponSpecial = new WeaponSpecial();

					byte specialType = Byte.valueOf(tempElement.attributeValue("SpecialType").trim());
					int specialNum = Integer.valueOf(tempElement.attributeValue("SpecialNum").trim());
					int effectType = Integer.valueOf(tempElement.attributeValue("EffectType").trim());
					float effectNum = Float.valueOf(tempElement.attributeValue("EffectNum").trim());
					weaponSpecial.setEffectNum(effectNum);
					weaponSpecial.setEffectType(HeroProType.getHeroProType(effectType));
					if (weaponSpecial.getEffectType() == null) {
						throw new RuntimeException("Load Magic.xml error! EffectType = " + effectType
								+ " not exit in No = " + no);
					}
					weaponSpecial.setSpecialNum(specialNum);
					weaponSpecial.setSpecialType(specialType);

					specialList.add(weaponSpecial);
				}
			}

			// 核心神兵的数据
			List<?> refineEles = rootEle.elements("Refine");
			Map<Short, WeaponRefine> refineMap = new HashMap<Short, WeaponRefine>();// <level, WeaponRefine>

			if (stepEles != null) {

				WeaponRefine weaponRefine;

				for (int i = 0; i < refineEles.size(); i++) {
					Element tempElement = (Element) refineEles.get(i);
					weaponRefine = new WeaponRefine();

					short level = Short.valueOf(tempElement.attributeValue("LV").trim());
					byte consume = Byte.valueOf(tempElement.attributeValue("Consume").trim());
					String skill = tempElement.attributeValue("Skill").trim();
					short limitLv = Short.valueOf(tempElement.attributeValue("LimitLv").trim());

					weaponRefine.setCostWeaponsNum(consume);
					weaponRefine.setLevel(level);
					weaponRefine.setMainHeroLv(limitLv);
					weaponRefine.setSkill(skill);

					refineMap.put(level, weaponRefine);
				}
			}
			WeaponXmlInfo info = new WeaponXmlInfo();
			info.setNo(no);
			info.setName(rootEle.attributeValue("Name").trim());
			info.setExp(exp);
			info.setSuit(suit);
			info.setWeaponType(type);
			info.setWeaponLv(weaponLv);
			info.setUpdateWeaponLv(upgradeWeaponLv);
			info.setSpecialList(specialList);
			info.setRefineMap(refineMap);
			WeaponXmlInfoMap.addWeaponXmlInfo(info);
		}
	}

	/**
	 * MagicExp.xml
	 * @param rootEle
	 * @return
	 */
	public static void loadMagicExp(Element rootEle,boolean modify) {
		if (rootEle != null) {
			short lv = Short.valueOf(rootEle.attributeValue("Lv").trim());
			int exp = Integer.valueOf(rootEle.attributeValue("Exp").trim());

			WeaponExpXmlInfo info = new WeaponExpXmlInfo();
			info.setLevel(lv);
			info.setExp(exp);
			WeaponExpXmlInfoMap.addWeaponXmlInfo(info);
		}
	}

	/**
	 * MagicSuit.xml
	 * @param rootEle
	 * @return
	 */
	public static void loadMagicSuit(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());

			List<?> stepEles = rootEle.elements("Item");
			Map<Byte, WeaponSuitNum> map = new HashMap<Byte, WeaponSuitNum>();// 套装效果 key-套装数量（suitNum）

			if (stepEles != null) {
				WeaponSuitNum weaponSuitNum;

				for (int i = 0; i < stepEles.size(); i++) {
					Element tempElement = (Element) stepEles.get(i);
					weaponSuitNum = new WeaponSuitNum();

					byte num = Byte.valueOf(tempElement.attributeValue("Num").trim());
					byte type = Byte.valueOf(tempElement.attributeValue("Type").trim());
					String effect = tempElement.attributeValue("Effect").trim();

					weaponSuitNum.setNum(num);
					weaponSuitNum.setEffect(effect);
					weaponSuitNum.setType(type);
					if (type != 101 && HeroProType.getHeroProType(type) == null) {
						throw new RuntimeException("Load MagicSuit.xml error! Type = " + type + " not exit in No = "
								+ no);
					}

					map.put(num, weaponSuitNum);
				}
			}

			WeaponSuitXmlInfo info = new WeaponSuitXmlInfo();
			info.setNo(no);
			info.setMap(map);
			WeaponSuitXmlInfoMap.addWeaponSuitXmlInfo(info);
		}
	}
}
