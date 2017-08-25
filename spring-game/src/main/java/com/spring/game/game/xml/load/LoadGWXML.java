package com.snail.webgame.game.xml.load;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.info.GWXMLInfo;

public class LoadGWXML {

	public static boolean loadGW(Element e,boolean modify) {
		GWXMLInfo xmlInfo = new GWXMLInfo();
		String no = e.attributeValue("No");
		if(no != null && no.length() != 0){
			xmlInfo.setNo(no);
			
			List<?> list = e.elements("NPC");
			if (list != null && list.size() > 0) {
				HashMap<Integer, String> dropMap = new HashMap<Integer, String>();
				for (int i = 0; i < list.size(); i++) {
					Element e2 = (Element) list.get(i);
					String NPCStr = e2.attributeValue("No");
					int NPCNo = 0;
					if(NPCStr != null && NPCStr.length() > 0){
						NPCNo = Integer.parseInt(NPCStr);
						String bag = e2.attributeValue("Bag");
						
						dropMap.put(NPCNo, bag);
					}
					xmlInfo.setDropMap(dropMap);
				}
			}
			GWXMLInfoMap.addNPCGWXMLInfo(xmlInfo);
			return true;
		}

		return false;
	}
	
}
