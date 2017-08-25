package com.snail.webgame.game.xml.load;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;

public class LoadChipComposeXML {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public static boolean load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			String itemNoStr = rootEle.attributeValue("No").trim();
			if (!itemNoStr.startsWith(GameValue.PROP_N0) && !itemNoStr.startsWith(GameValue.EQUIP_N0)
					&& !itemNoStr.startsWith(GameValue.WEAPAN_NO)) {
				throw new RuntimeException("Load ChipCompose.xml error! No = " + itemNoStr + " error !");
			}
			int itemNo = Integer.parseInt(itemNoStr);
			if (PropXMLInfoMap.getComposeXMLInfo(itemNo) != null && !modify) {
				if (logger.isErrorEnabled()) {
					logger.error("Load ChipCompose.xml error! no: " + itemNo + " repeat");
				}
				return false;
			}
			List<?> chipElems = rootEle.elements("Item");
			loadChipElems(itemNo, chipElems);
			return true;
		}
		return false;
	}

	private static void loadChipElems(int itemNo, List<?> chipElems) {
		if (chipElems != null && chipElems.size() > 0) {
			for (int i = 0; i < chipElems.size(); i++) {
				Element ele = (Element) chipElems.get(i);
				String chipNoStr = ele.attributeValue("ChipNo").trim();
				if (!chipNoStr.startsWith(GameValue.PROP_N0)) {
					throw new RuntimeException("Load ChipCompose.xml error! ChipNo = " + chipNoStr + " error no="
							+ itemNo + "!");
				}

				int chipNo = NumberUtils.toInt(chipNoStr);
				int chipNum = NumberUtils.toInt(ele.attributeValue("ChipCount").trim());
				PropXMLInfoMap.addComposeXMLInfo(itemNo, chipNo, chipNum);
			}
		}
	}
}
