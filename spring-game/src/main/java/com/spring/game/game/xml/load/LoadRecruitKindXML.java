package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.HeropropConfigInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.HeropropConfigInfo;
import com.snail.webgame.game.xml.info.RecruitKindXMLInfo;

public class LoadRecruitKindXML {

	/**
	 * 读取 ChestKind.xml
	 * @param xmlName
	 * @param rootEle
	 * @return
	 */
	public static RecruitKindXMLInfo load(String xmlName, Element rootEle) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			String conditions = rootEle.attributeValue("Condition").trim();
			int poolNum = Integer.valueOf(rootEle.attributeValue("PoolNum").trim());
			String first = rootEle.attributeValue("First").trim();
			String depotNoStr = rootEle.attributeValue("DepotNoStr").trim();
			String special = rootEle.attributeValue("Special").trim();

			RecruitKindXMLInfo info = new RecruitKindXMLInfo();
			info.setNo(no);
			info.setConditions(AbstractConditionCheck.generateConds(xmlName, conditions));
			info.setPoolNum(poolNum);
			info.setFirst(first);
			info.setDepotNoStr(depotNoStr);
			info.setSpecial(special);

			return info;
		}
		return null;
	}

	public static void loadRelatedHeropropXml(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int hero = Integer.valueOf(rootEle.attributeValue("Hero").trim());

			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(hero);
			if (heroXMLInfo == null) {
				throw new RuntimeException("HeropropXml error Hero " + hero + " not exit ");
			}
			if (heroXMLInfo.getChipNo() != no) {
				throw new RuntimeException("HeropropXml error no " + no + " not chip with heroNo" + hero);
			}

			if (PropXMLInfoMap.getPropXMLInfo(no) == null) {
				throw new RuntimeException("HeropropXml error no " + no + " not exit ");
			}

			HeropropConfigInfo info = new HeropropConfigInfo();
			info.setNo(no);
			info.setHero(hero);

			HeropropConfigInfoMap.addHeropropConfigInfo(info);
		}
	}

}
