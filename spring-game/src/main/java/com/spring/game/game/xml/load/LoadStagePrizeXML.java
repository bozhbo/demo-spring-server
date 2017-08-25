package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.KuafuPrizeXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.KuafuXMLPrize;


public class LoadStagePrizeXML {
	public static boolean load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			int minPlace = Integer.parseInt(rootEle.attributeValue("MinPlace").trim());
			int maxPlace = Integer.parseInt(rootEle.attributeValue("MaxPlace").trim());
			String placeDropNo = rootEle.attributeValue("PlaceDropNo").trim();
			KuafuXMLPrize info = new KuafuXMLPrize();
			info.setNo(no);
			info.setMinPlace(minPlace);
			info.setMaxPlace(maxPlace);
			info.setPlaceDropNoStr(placeDropNo);

			// 记录已经使用的bagNO
			PropBagXMLMap.addUsedBagNo(placeDropNo);

			if (KuafuPrizeXMLInfoMap.getKuafuXMLPrize(no) != null && !modify) {
				throw new RuntimeException("Load ArenaPrize.xml error! no: " + no + " repeat");
			}
			KuafuPrizeXMLInfoMap.addKuafuXMLPrize(info);
			return true;
		}
		return false;
	}
}
