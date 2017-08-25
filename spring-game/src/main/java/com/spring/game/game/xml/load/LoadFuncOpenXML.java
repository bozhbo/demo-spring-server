package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.FuncOpenXMLInfoMap;
import com.snail.webgame.game.xml.info.FuncOpenXMLInfo;

public class LoadFuncOpenXML {

	public static void load(String xmlName, Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			String checlConds = rootEle.attributeValue("Condition").trim();

			FuncOpenXMLInfo xmlInfo = new FuncOpenXMLInfo();
			xmlInfo.setNo(no);
			xmlInfo.setCheckConds(AbstractConditionCheck.generateConds(xmlName, checlConds));

			List<DropInfo> list = new ArrayList<DropInfo>();
			List<?> givePropElems = rootEle.elements("GiveProp");
			if (givePropElems != null && givePropElems.size() > 0) {
				for (int i = 0; i < givePropElems.size(); i++) {
					Element ele = (Element) givePropElems.get(i);

					String itemNo = ele.attributeValue("No").trim();
					int itemNum = Integer.valueOf(ele.attributeValue("Num")
							.trim());
					DropInfo dropInfo = new DropInfo();
					dropInfo.setItemNo(itemNo);
					dropInfo.setItemNum(itemNum);
					list.add(dropInfo);
				}
			}

			if (list.size() > 0) {
				xmlInfo.setPrizes(list);
			}

			FuncOpenXMLInfoMap.addFuncOpenXMLInfo(xmlInfo);
		}
	}

}
