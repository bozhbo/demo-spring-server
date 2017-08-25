package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;

public class LoadPropBagXML {
	
	public static void loadPropBagXMLInfo(Element e,boolean modify) {

		String bagNo = e.attributeValue("Type");
		String type = e.attributeValue("DropType");
		

		List<?> list = e.elements("Item");
		if (list != null && list.size() > 0) {
			List<DropXMLInfo> dropXMLList = new ArrayList<DropXMLInfo>();
			for (int i = 0; i < list.size(); i++) {
				Element e2 = (Element) list.get(i);

				DropXMLInfo xmlInfo = new DropXMLInfo();
				String itemNo = e2.attributeValue("Item");
				xmlInfo.setItemNo(itemNo);

				/*if (itemNo.equals(BaseCondition.TYPE_COIN)) {
					// 游戏奖励掉落物品，不可配置金子
					String no = e.attributeValue("No");
					throw new RuntimeException("PropBag.xml,no = " + no + ",drop error!cant not drop" + itemNo);
				}*/

				xmlInfo.setItemMinNum(Integer.valueOf(e2.attributeValue("minNum")));
				xmlInfo.setItemMaxNum(Integer.valueOf(e2.attributeValue("maxNum")));
				
				xmlInfo.setMinRand(Integer.valueOf(e2.attributeValue("minRand")));
				xmlInfo.setMaxRand(Integer.valueOf(e2.attributeValue("maxRand")));
				if(type != null && type.length() > 0)
				{
					xmlInfo.setDropType(Integer.parseInt(type));
				}
				
				dropXMLList.add(xmlInfo);

			}
			PropBagXMLMap.addPropBagXMLList(bagNo, dropXMLList,modify);
		}

	}

}
