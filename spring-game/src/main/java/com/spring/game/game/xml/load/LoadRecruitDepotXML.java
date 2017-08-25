package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.RecruitDepotXMLInfoMap;
import com.snail.webgame.game.xml.info.RecruitDepotXMLInfo;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;

public class LoadRecruitDepotXML {
	/**
	 * 读取 RecruitDepot.xml
	 * @param xmlName
	 * @param rootEle
	 * @return
	 */
	public static RecruitDepotXMLInfo load(String xmlName, Element rootEle) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			String name = rootEle.attributeValue("Name").trim();

			RecruitDepotXMLInfo info = new RecruitDepotXMLInfo();
			info.setNo(no);
			info.setName(name);

			// 升级
			List<?> items = rootEle.elements("Item");
			itemLoad(xmlName, items, info);

			return info;
		}
		return null;
	}

	/**
	 * 读取 RecruitDepot.xml Item
	 * 
	 * @param list
	 * @param info
	 */
	private static void itemLoad(String xmlName, List<?> list, RecruitDepotXMLInfo info) {
		if (list != null && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				Element e = (Element) list.get(j);
				String itemNo = e.attributeValue("ItemNo").trim();
				int num = Integer.valueOf(e.attributeValue("Num").trim());
				int rand = Integer.valueOf(e.attributeValue("Rand").trim());

				RecruitItemXMLInfo u = new RecruitItemXMLInfo();
				u.setItemNo(itemNo);
				u.setNum(num);
				u.setRand(rand);

				info.getItems().add(u);
			}
		}
	}
	
	/**
	 * 读取Broadcast.xml
	 * @param element
	 */
	public static void loadBroadcast(Element element,boolean modify){
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		int no = Integer.parseInt(element.attributeValue("No"));
		String name = element.attributeValue("Name");
		
		RecruitDepotXMLInfoMap.addBroadcast(no, name);
	}

}
