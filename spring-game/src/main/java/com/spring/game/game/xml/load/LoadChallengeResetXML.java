package com.snail.webgame.game.xml.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.ChallengeResetXmlInfoMap;
import com.snail.webgame.game.xml.info.ChallengeResetXMLInfo;

public class LoadChallengeResetXML {

	/**
	 * 加载ChallengeReset.xml
	 * @param xmlName
	 * @param e
	 */
	public static void loadXml(Element e,boolean modify) {
		byte chapterType = Byte.valueOf(e.attributeValue("No").trim());
		if(chapterType != 0){
			Map<Integer, ChallengeResetXMLInfo> map = new HashMap<Integer, ChallengeResetXMLInfo>();
			List<?> list = e.elements("Property");
			if(list != null && list.size() > 0){
				for (int j = 0; j < list.size(); j++) {
					Element e2 = (Element) list.get(j);
					int no = Integer.parseInt(e2.attributeValue("No"));
					int gold = Integer.parseInt(e2.attributeValue("Gold"));
					ChallengeResetXMLInfo info = new ChallengeResetXMLInfo();

					if(no != 0){
						info.setNo(no);
						info.setGold(gold);
						map.put(info.getNo(), info);
					}
				}
			}
			ChallengeResetXmlInfoMap.addChallengeRest(chapterType, map);
		}
	}

}
