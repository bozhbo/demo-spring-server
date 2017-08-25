package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;

import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.xml.cache.HeroRelationShipXMLInfoMap;
import com.snail.webgame.game.xml.info.HeroRelationShipXMLInfo;
import com.snail.webgame.game.xml.info.HeroRelationShipXMLInfo.RelationShipXMLInfo;

public class LoadHeroRelationShipXML {

	public static boolean load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			HeroRelationShipXMLInfo info = new HeroRelationShipXMLInfo();
			info.setHeroNo(Integer.parseInt(rootEle.attributeValue("No").trim()));
//			if (!HeroXMLInfoMap.isExitHero(info.getHeroNo())) {
//				throw new RuntimeException("Load RelationShip.xml error!  heroNo :" + info.getHeroNo() + "  not exit");
//			}
			List<?> relationShipElems = rootEle.elements("Relation");
			List<RelationShipXMLInfo> relationShipList = new ArrayList<HeroRelationShipXMLInfo.RelationShipXMLInfo>();
			if (relationShipElems != null && relationShipElems.size() > 0) {
				for (int i = 0; i < relationShipElems.size(); i++) {
					Element ele = (Element) relationShipElems.get(i);
					RelationShipXMLInfo relationShipXMLInfo = info.new RelationShipXMLInfo();
					relationShipXMLInfo.setRelationNo(Integer.parseInt(ele.attributeValue("RelationNo").trim()));
					int buffType = Integer.parseInt(ele.attributeValue("RelationshipBuffType").trim());
					if (buffType >= 100) {
						relationShipXMLInfo.setBuffType(HeroProType.getHeroProType(buffType - 100));
						relationShipXMLInfo.setAddRate(true);
					} else {
						relationShipXMLInfo.setBuffType(HeroProType.getHeroProType(buffType));
						relationShipXMLInfo.setAddRate(false);
					}					
					if (relationShipXMLInfo.getBuffType() == null) {
						throw new RuntimeException("Load EquipSuit.xml error Type:" + buffType + "not exit in No "
								+ relationShipXMLInfo.getRelationNo());
					}
					
					relationShipXMLInfo.setBuffAdd(Double.parseDouble(ele.attributeValue("RelationshipBuffAdd").trim()));
					String heroNos = ele.attributeValue("HeroNo").trim();			
					if (relationShipXMLInfo.getHeroNo() == null) {
						relationShipXMLInfo.setHeroNo(new ArrayList<Integer>());
					}
					for (String heroNoStr : heroNos.split(",")) {
						int rlheroNo = NumberUtils.toInt(heroNoStr);
						if (info.getHeroNo() == rlheroNo) {
							throw new RuntimeException("Load RelationShip.xml error!  heroNo :" + info.getHeroNo()
									+ " , " + relationShipXMLInfo.getRelationNo() + "hero self exit");
						}
						if(!relationShipXMLInfo.getHeroNo().contains(rlheroNo)){
							relationShipXMLInfo.getHeroNo().add(rlheroNo);
						}					
					}
					if (relationShipXMLInfo.getHeroNo().size() <= 0) {
						throw new RuntimeException("Load RelationShip.xml error!  heroNo :" + info.getHeroNo() + " , "
								+ relationShipXMLInfo.getRelationNo() + "hero Relation error");
					}

					relationShipList.add(relationShipXMLInfo);
				}
			}

			if (relationShipList.size() > 0) {
				info.setRelationShipList(relationShipList);
			}
			if (HeroRelationShipXMLInfoMap.getHeroRelationShipXMLInfo(info.getHeroNo()) != null && !modify) {
				throw new RuntimeException("Load RelationShip.xml error!  heroNo :" + info.getHeroNo() + " repeat");
			}
			HeroRelationShipXMLInfoMap.addHeroRelationShipXMLInfo(info);
			return true;
		}
		return false;
	}
}
