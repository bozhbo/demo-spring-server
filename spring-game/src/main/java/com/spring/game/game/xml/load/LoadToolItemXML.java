package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.cache.ToolItemMap;

public class LoadToolItemXML {

	/**
	 * ToolItem.xml
	 * 
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			String itemNo = rootEle.attributeValue("TypeNo").trim();

			ToolItemMap.addToolItem(no, itemNo);
		}
	}
}
