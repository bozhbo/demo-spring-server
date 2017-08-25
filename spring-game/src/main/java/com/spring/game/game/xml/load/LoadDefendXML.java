package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.xml.cache.DefendXMLInfoMap;
import com.snail.webgame.game.xml.info.DefendXMLInfo;
import com.snail.webgame.game.xml.info.DefendXMLInfo.DefendFightXMLInfo;

/**
 * 加载防守玩法信息
 * @author wanglinhui
 *
 */
public class LoadDefendXML {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public static boolean load(Element rootEle,boolean modify) {

		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			int needLevel = Integer.parseInt(rootEle.attributeValue("Lv").trim());
			DefendXMLInfo defendXMLInfo = new DefendXMLInfo();
			defendXMLInfo.setNo(no);
			defendXMLInfo.setName(rootEle.attributeValue("Name").trim());
			defendXMLInfo.setNeedLevel(needLevel);

			List<?> npcElems = rootEle.elements("Npc");
			loadDefendNPC(defendXMLInfo, npcElems);

			if (DefendXMLInfoMap.getDefendXMLInfo(no) != null && !modify) {
				if (logger.isErrorEnabled()) {
					logger.error("Load Defend.xml error! no: " + no + " repeat");
				}
				return false;
			}

			DefendXMLInfoMap.addDefendXMLInfo(defendXMLInfo);
			return true;
		}
		return false;
	}

	private static void loadDefendNPC(DefendXMLInfo defendXMLInfo, List<?> npcElems) {
		if (npcElems != null && npcElems.size() > 0) {
			for (int i = 0; i < npcElems.size(); i++) {
				Element ele = (Element) npcElems.get(i);
				DefendFightXMLInfo defendFightXMLInfo = defendXMLInfo.new DefendFightXMLInfo();
				defendFightXMLInfo.setNo(Integer.parseInt(ele.attributeValue("No").trim()));

				List<?> pointElems = ele.elements("Point");
				loadDefendNPCPoint(defendFightXMLInfo, pointElems);

				if (defendXMLInfo.getDefendFightList() == null) {
					defendXMLInfo.setDefendFightList(new ArrayList<DefendXMLInfo.DefendFightXMLInfo>());
				}
				defendXMLInfo.getDefendFightList().add(defendFightXMLInfo);
			}
		}
	}

	private static void loadDefendNPCPoint(DefendFightXMLInfo defendFightXMLInfo, List<?> pointElems) {
		if (pointElems != null && pointElems.size() > 0) {
			for (int i = 0; i < pointElems.size(); i++) {
				Element ele = (Element) pointElems.get(i);
				int no = Integer.parseInt(ele.attributeValue("No").trim());
				String bag = ele.attributeValue("Bag").trim();

				if (defendFightXMLInfo.getDropBagNo() == null) {
					defendFightXMLInfo.setDropBagNo(new HashMap<Integer, String>());
				}
				if (defendFightXMLInfo.getDropBagNo().containsKey(no)) {
					throw new RuntimeException("Load Defend.xml error! point = " + no + " repeat npc no="
							+ defendFightXMLInfo.getNo() + "!");
				}
				defendFightXMLInfo.getDropBagNo().put(no, bag);
			}
		}

	}
}
