package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.SnatchMap;
import com.snail.webgame.game.xml.info.SnatchInfo;

public class LoadSnatchXML {

	/**
	 * Snatch.xml
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			SnatchInfo snatchInfo;
			List<?> itemEles = rootEle.elements("Item");
			if (itemEles != null) {
				for (int i = 0; i < itemEles.size(); i++) {
					Element itemElement = (Element) itemEles.get(i);
					int no = Integer.parseInt(itemElement.attributeValue("No").trim());
					int propNo = Integer.parseInt(itemElement.attributeValue("Prop").trim());
					if (SnatchMap.getByPropNo(propNo) != null && !modify) {
						throw new RuntimeException("Load Snatch.xml error! propNo:" + propNo + " repeat");
					}

					List<?> patchEles = itemElement.elements("Patch");
					if (patchEles != null) {
						for (int s = 0; s < patchEles.size(); s++) {
							Element patchElement = (Element) patchEles.get(s);
							int patchNo = Integer.parseInt(patchElement.attributeValue("Prop").trim());
							if (SnatchMap.get(patchNo) != null && !modify) {
								throw new RuntimeException("Load Snatch.xml error! patchNo:" + patchNo + " repeat");
							}

							snatchInfo = new SnatchInfo();
							snatchInfo.setNo(no);
							snatchInfo.setPropNo(propNo);
							snatchInfo.setPatchNo(patchNo);
							snatchInfo.setBag(patchElement.attributeValue("Bag").trim());
							snatchInfo.setCardBag(patchElement.attributeValue("CardBag").trim());
							snatchInfo.setChance(Float.parseFloat(patchElement.attributeValue("Chance").trim()));
							snatchInfo.setLimit(Integer.parseInt(patchElement.attributeValue("Limit").trim()));

							SnatchMap.addItem(snatchInfo);
						}
					}
				}
			}
		}
	}
}
