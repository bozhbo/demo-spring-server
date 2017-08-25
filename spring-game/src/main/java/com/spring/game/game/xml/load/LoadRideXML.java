package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.xml.load.LoadHeroXML;
import com.snail.webgame.game.xml.cache.RideXMLInfoMap;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo.QualityInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo.LvUpInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo.UpCostInfo;

public class LoadRideXML {
	
	/**
	 * 加载坐骑信息
	 * 
	 * @param rootEle
	 */
	public static void loadRide(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load RideUp.xml error! no data!");
		}
		
		RideXMLInfo xmlInfo = new RideXMLInfo();
		xmlInfo.setNo(Integer.parseInt(rootEle.attributeValue("No")));
		xmlInfo.setRideChipNo(Integer.parseInt(rootEle.attributeValue("ChipNo")));
		xmlInfo.setCallChipCost(Integer.parseInt(rootEle.attributeValue("ChipCount")));
		xmlInfo.setInitQua(Integer.parseInt(rootEle.attributeValue("InitialColor")));

		int maxLv = Integer.valueOf(rootEle.attributeValue("TotalLevel"));
		loadRideLvUp(xmlInfo, maxLv, rootEle);

		if (RideXMLInfoMap.fetchRideXMLInfo(xmlInfo.getNo()) != null && !modify) {
			throw new RuntimeException("Load RideUp.xml error! there is no = " + xmlInfo.getNo() + " repeat!");
		}
		
		RideXMLInfoMap.addRideXMLInfo(xmlInfo);
	}
	
	/**
	 * 加载坐骑进阶信息
	 * 
	 * @param rootEle
	 */
	public static void loadRideQua(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load RideColor.xml error! no data!");
		}
		
		RideQuaXMLInfo xmlInfo = new RideQuaXMLInfo();
		xmlInfo.setNo(Integer.parseInt(rootEle.attributeValue("No")));

		List<?> quaUpList = rootEle.elements("ColorUp");
		loadRideQuaUp(xmlInfo, quaUpList);

		if (RideXMLInfoMap.fetchRideQuaXMLInfo(xmlInfo.getNo()) != null && !modify) {
			throw new RuntimeException("Load RideColor.xml error! there is no = " + xmlInfo.getNo() + " repeat!");
		}
		
		RideXMLInfoMap.addRideQuaXMLInfo(xmlInfo);
	}
	
	private static void loadRideLvUp(RideXMLInfo xmlInfo, int maxLv, Element rootEle) {
		HeroPropertyInfo base = new HeroPropertyInfo();
		LoadHeroXML.loadHeroPro(base, rootEle);
		
		HeroPropertyInfo each = new HeroPropertyInfo();
		loadEachPro(each, rootEle);
		
		LvUpInfo info = null;
		for (int lv = 1; lv <= maxLv; lv ++) {
			info = new LvUpInfo();
				
			info.setLv(lv);
			
			loadLvPro(info, base, each);
			
			xmlInfo.getRideLvUpMap().put(info.getLv(), info);
		}
	}
	
	private static void loadRideQuaUp(RideQuaXMLInfo xmlInfo, List<?> quaUpList) {
		if (quaUpList != null && quaUpList.size() > 0) {
			QualityInfo info = null;
			Element e = null;
			for (int i = 0; i < quaUpList.size(); i++) {
				info = new QualityInfo();
				e = (Element) quaUpList.get(i);
				info.setQuality(Integer.valueOf(e.attributeValue("Color")));
				info.setAddAttrRate(Float.valueOf(e.attributeValue("AddAttribute")));
				
				xmlInfo.getRideQuaMap().put(info.getQuality(), info);
			}
		}
	}

	public static void loadRideLvUpCost(Element rootEle,boolean modify) {
		int itemNo = Integer.parseInt(rootEle.attributeValue("PropNo"));
		List<?> lvUpList = rootEle.elements("levelUp");
		if (lvUpList != null && lvUpList.size() > 0) {
			UpCostInfo info = null;
			Element e = null;
			for (int i = 0; i < lvUpList.size(); i++) {
				info = new UpCostInfo();
				e = (Element) lvUpList.get(i);
				
				info.setItemNo(itemNo);
				info.setLvOrQua(Integer.valueOf(e.attributeValue("Level")));
				info.setItemNum(Integer.valueOf(e.attributeValue("PropCount")));
				info.setMoneyCost(Integer.valueOf(e.attributeValue("Silver")));
				
				RideXMLInfoMap.addLvUpCostInfo(info);
			}
		}
	}
	
	public static void loadRideQuaUpCost(Element rootEle,boolean modify) {
		int itemNo = Integer.parseInt(rootEle.attributeValue("PropNo"));
		List<?> quaUpList = rootEle.elements("ColorUp");
		if (quaUpList != null && quaUpList.size() > 0) {
			UpCostInfo info = null;
			Element e = null;
			for (int i = 0; i < quaUpList.size(); i++) {
				info = new UpCostInfo();
				e = (Element) quaUpList.get(i);
				
				info.setItemNo(itemNo);
				info.setLvOrQua(Integer.valueOf(e.attributeValue("Color")));
				info.setItemNum(Integer.valueOf(e.attributeValue("PropCount")));
				info.setMoneyCost(Integer.valueOf(e.attributeValue("Silver")));
				
				RideXMLInfoMap.addQuaUpCostInfo(info);
			}
		}
	}
	
	/**
	 * 
	 * @param xmlInfo
	 * @param rootEle
	 */
	public static void loadEachPro(HeroPropertyInfo xmlInfo, Element rootEle) {
		xmlInfo.setForce(Integer.parseInt(rootEle.attributeValue("EachForce")));
		xmlInfo.setWit(Integer.parseInt(rootEle.attributeValue("EachIntelligence")));
		xmlInfo.setTroops(Integer.parseInt(rootEle.attributeValue("EachTroops")));
		xmlInfo.setHp(Integer.parseInt(rootEle.attributeValue("EachHp")));
		xmlInfo.setAd(Integer.parseInt(rootEle.attributeValue("EachAd")));

		xmlInfo.setAttack(Integer.parseInt(rootEle.attributeValue("EachAttack")));
		xmlInfo.setMagicAttack(Integer.parseInt(rootEle.attributeValue("EachMagicAttack")));

		xmlInfo.setCrit(Integer.parseInt(rootEle.attributeValue("EachCrit")));
		xmlInfo.setCritAvo(Integer.parseInt(rootEle.attributeValue("EachCritAvo")));

		xmlInfo.setSkillCrit(Integer.parseInt(rootEle.attributeValue("EachSkillCrit")));
		xmlInfo.setSkillCritAvo(Integer.parseInt(rootEle.attributeValue("EachSkillCritAvo")));

		xmlInfo.setIgnorAttackAvo(Integer.parseInt(rootEle.attributeValue("EachIgnorAttackAvo")));
		xmlInfo.setIgnorMagicAvo(Integer.parseInt(rootEle.attributeValue("EachIgnorMagicAvo")));

		xmlInfo.setMiss(Integer.parseInt(rootEle.attributeValue("EachMiss")));
		xmlInfo.setHit(Integer.parseInt(rootEle.attributeValue("EachHit")));

		String moveSpeed = rootEle.attributeValue("EachSpeed");
		if (moveSpeed != null) {
			xmlInfo.setMoveSpeed(Float.parseFloat(moveSpeed));
		}

		String cutCD = rootEle.attributeValue("EachCutCD");
		if (cutCD != null) {
			xmlInfo.setCutCD(Float.parseFloat(cutCD));
		}

		String hpBack = rootEle.attributeValue("EachHpBack");
		if (hpBack != null) {
			xmlInfo.setHpBack(Float.parseFloat(hpBack));
		}

		String cureUp = rootEle.attributeValue("EachCureUp");
		if (cureUp != null) {
			xmlInfo.setCureUp(Float.parseFloat(cureUp));
		}

		String attackSpeed = rootEle.attributeValue("EachAttackSpeed");
		if (attackSpeed != null) {
			xmlInfo.setAttackSpeed(Float.parseFloat(attackSpeed));
		}

		String attackDef = rootEle.attributeValue("EachAttackDef");
		if (attackDef != null) {
			xmlInfo.setAttackDef(Integer.parseInt(attackDef));
		}
		String magicDef = rootEle.attributeValue("EachMagicDef");
		if (magicDef != null) {
			xmlInfo.setMagicDef(Integer.parseInt(magicDef));
		}

		String attackRange = rootEle.attributeValue("EachAttackRange");
		if (attackRange != null) {
			xmlInfo.setAttackRange(Integer.parseInt(attackRange));
		}

		String radius = rootEle.attributeValue("EachRadius");
		if (radius != null) {
			xmlInfo.setRadius(Integer.parseInt(radius));
		}

		String lookRange = rootEle.attributeValue("EachLookRange");
		if (lookRange != null) {
			xmlInfo.setLookRange(Float.parseFloat(lookRange));
		}

		String bulletSpeed = rootEle.attributeValue("EachBulletSpeed");
		if (bulletSpeed != null) {
			xmlInfo.setBulletSpeed(Integer.parseInt(bulletSpeed));
		}

		xmlInfo.setAd(Integer.parseInt(rootEle.attributeValue("EachAd")));

		String critMore = rootEle.attributeValue("EachCritMore");
		if (critMore != null) {
			xmlInfo.setCritMore(Float.parseFloat(critMore));
		}

		String critLess = rootEle.attributeValue("EachCritLess");
		if (critLess != null) {
			xmlInfo.setCritLess(Float.parseFloat(critLess));
		}

		String soldierHp = rootEle.attributeValue("EachSoldierHp");
		if (soldierHp != null) {
			xmlInfo.setSoldierHp(Integer.parseInt(soldierHp));
		}

		String breakSoldierDef = rootEle.attributeValue("EachBreakSoldierDef");
		if (breakSoldierDef != null) {
			xmlInfo.setBreakSoldierDef(Float.parseFloat(breakSoldierDef));
		}
		
		String reduceDamage = rootEle.attributeValue("EachReduceDamage");
		if (reduceDamage != null) {
			xmlInfo.setReduceDamage(Integer.parseInt(reduceDamage));
		}
		
		String immunityDamage = rootEle.attributeValue("EachImmunityDamage");
		if (immunityDamage != null) {
			xmlInfo.setImmunityDamage(Float.parseFloat(immunityDamage));
		}
		
		String rideHp = rootEle.attributeValue("EachRideHp");
		if (rideHp != null) {
			xmlInfo.setRideHp(Integer.parseInt(rideHp));
		}
	}
	
	private static void loadLvPro(LvUpInfo info, HeroPropertyInfo base,
			HeroPropertyInfo each) {
		info.setForce(base.getForce() + (info.getLv() - 1) * each.getForce());
		info.setWit(base.getWit() + (info.getLv() - 1) * each.getWit());
		info.setTroops(base.getTroops() + (info.getLv() - 1) * each.getTroops());
		info.setHp(base.getHp() + (info.getLv() - 1) * each.getHp());
		info.setAd(base.getAd() + (info.getLv() - 1) * each.getAd());
		info.setAttack(base.getAttack() + (info.getLv() - 1) * each.getAttack());
		info.setMagicAttack(base.getMagicAttack() + (info.getLv() - 1) * each.getMagicAttack());
		info.setCrit(base.getCrit() + (info.getLv() - 1) * each.getCrit());
		info.setCritAvo(base.getCritAvo() + (info.getLv() - 1) * each.getCritAvo());
		info.setSkillCrit(base.getSkillCrit() + (info.getLv() - 1) * each.getSkillCrit());
		info.setSkillCritAvo(base.getSkillCritAvo() + (info.getLv() - 1) * each.getSkillCritAvo());
		info.setIgnorAttackAvo(base.getIgnorAttackAvo() + (info.getLv() - 1) * each.getIgnorAttackAvo());
		info.setIgnorMagicAvo(base.getIgnorMagicAvo() + (info.getLv() - 1) * each.getIgnorMagicAvo());
		info.setMiss(base.getMiss() + (info.getLv() - 1) * each.getMiss());
		info.setHit(base.getHit() + (info.getLv() - 1) * each.getHit());
		info.setMoveSpeed(base.getMoveSpeed() + (info.getLv() - 1) * each.getMoveSpeed());
		info.setCutCD(base.getCutCD() + (info.getLv() - 1) * each.getCutCD());
		info.setHpBack(base.getHpBack() + (info.getLv() - 1) * each.getHpBack());
		info.setCureUp(base.getCureUp() + (info.getLv() - 1) * each.getCureUp());
		info.setAttackSpeed(base.getAttackSpeed() + (info.getLv() - 1) * each.getAttackSpeed());
		info.setAttackDef(base.getAttackDef() + (info.getLv() - 1) * each.getAttackDef());
		info.setMagicDef(base.getMagicDef() + (info.getLv() - 1) * each.getMagicDef());
		info.setAttackRange(base.getAttackRange() + (info.getLv() - 1) * each.getAttackRange());
		info.setRadius(base.getRadius() + (info.getLv() - 1) * each.getRadius());
		info.setLookRange(base.getLookRange() + (info.getLv() - 1) * each.getLookRange());
		info.setBulletSpeed(base.getBulletSpeed() + (info.getLv() - 1) * each.getBulletSpeed());
		info.setCritMore(base.getCritMore() + (info.getLv() - 1) * each.getCritMore());
		info.setCritLess(base.getCritLess() + (info.getLv() - 1) * each.getCritLess());
		info.setSoldierHp(base.getSoldierHp() + (info.getLv() - 1) * each.getSoldierHp());
		info.setBreakSoldierDef(base.getBreakSoldierDef() + (info.getLv() - 1) * each.getBreakSoldierDef());
		info.setReduceDamage(base.getReduceDamage() + (info.getLv() - 1) * each.getReduceDamage());
		info.setImmunityDamage(base.getImmunityDamage() + (info.getLv() - 1) * each.getImmunityDamage());
		info.setRideHp(base.getRideHp() + (info.getLv() - 1) * each.getRideHp());
	}
}
