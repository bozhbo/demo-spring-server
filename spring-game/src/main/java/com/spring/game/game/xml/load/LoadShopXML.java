package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.ShopXMLInfoMap;
import com.snail.webgame.game.xml.info.ShopXMLInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopItem;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopRefresh;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopRoleLevels;

public class LoadShopXML {

	/**
	 * 加载Shop.xml
	 * @param rootEle
	 */
	public static void loadShop(String xmlName, Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load Shop.xml error! no data!");
		}
		int no = Integer.parseInt(rootEle.attributeValue("No").trim());
		String costType = rootEle.attributeValue("CostType").trim();
		int shopType = Integer.parseInt(rootEle.attributeValue("Buy").trim());

		if (!AbstractConditionCheck.isCurrencyType(costType)) {
			throw new RuntimeException("Load Shop.xml CostType:" + costType + " error! ");
		}

		if (shopType != ShopXMLInfo.SHOP_TYPE_1 && shopType != ShopXMLInfo.SHOP_TYPE_2) {
			throw new RuntimeException("Load Shop.xml Buy:" + shopType + " error! ");
		}

		ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(no);
		if (xmlInfo == null) {
			xmlInfo = new ShopXMLInfo();
			xmlInfo.setNo(no);
			ShopXMLInfoMap.addShopXMLInfo(xmlInfo);
		}
		xmlInfo.setCostType(costType);
		xmlInfo.setShopType(shopType);

		// RoleLevels
		List<?> roleLevels = rootEle.elements("RoleLevels");
		roleLevelsLoad(xmlName, roleLevels, xmlInfo);

	}

	/**
	 * 加载Shop.xml RoleLevels
	 * @param xmlName
	 * @param roleLevels
	 * @param xmlInfo
	 */
	private static void roleLevelsLoad(String xmlName, List<?> roleLevels, ShopXMLInfo xmlInfo) {
		if (roleLevels != null && roleLevels.size() > 0) {
			for (int j = 0; j < roleLevels.size(); j++) {
				Element e = (Element) roleLevels.get(j);
				int no = Integer.parseInt(e.attributeValue("No").trim());
				String level = e.attributeValue("Level").trim();

				String[] lvs = level.split("-");
				if (lvs.length != 2) {
					throw new RuntimeException("Load Shop.xml RoleLevels No:" + no + " Level error!");
				}
				int minRoleLv = Integer.parseInt(lvs[0]);
				int maxRoleLv = Integer.parseInt(lvs[1]);

				ShopRoleLevels shopRoleLevels = new ShopRoleLevels();
				shopRoleLevels.setNo(no);
				shopRoleLevels.setMinRoleLv(minRoleLv);
				shopRoleLevels.setMaxRoleLv(maxRoleLv);

				// RoleLevels
				List<?> ShopItems = e.elements("ShopItems");
				shopItemsLoad(xmlName, ShopItems, shopRoleLevels);

				if (xmlInfo.getRoleLvList() == null) {
					xmlInfo.setRoleLvList(new ArrayList<ShopRoleLevels>());
				}
				xmlInfo.getRoleLvList().add(shopRoleLevels);
			}
		}
	}

	/**
	 * 加载Shop.xml RoleLevels ShopItems
	 * @param xmlName
	 * @param shopItems
	 * @param shopRoleLevels
	 */
	private static void shopItemsLoad(String xmlName, List<?> shopItems, ShopRoleLevels shopRoleLevels) {
		if (shopItems != null && shopItems.size() > 0) {
			for (int j = 0; j < shopItems.size(); j++) {
				Element e = (Element) shopItems.get(j);
				int position = Integer.parseInt(e.attributeValue("PositionNo").trim());
				// RoleLevels
				List<?> items = e.elements("Item");
				List<ShopItem> shopItems1 = itemsLoad(xmlName, items);

				if (shopRoleLevels.getItems() == null) {
					shopRoleLevels.setItems(new HashMap<Integer, List<ShopItem>>());
				}
				if (shopRoleLevels.getItems().containsKey(position)) {
					throw new RuntimeException("Load Shop.xml RoleLevels ShopItems PositionNo:" + position + " repeat!");
				}
				shopRoleLevels.getItems().put(position, shopItems1);
			}
		}
	}

	/**
	 * 加载Shop.xml RoleLevels ShopItems Item
	 * @param xmlName
	 * @param items
	 * @param shopRoleLevels
	 * @param position
	 */
	private static List<ShopItem> itemsLoad(String xmlName, List<?> items) {
		List<ShopItem> result = new ArrayList<ShopItem>();
		if (items != null && items.size() > 0) {
			for (int j = 0; j < items.size(); j++) {
				Element e = (Element) items.get(j);
				int itemNo = Integer.parseInt(e.attributeValue("ProNo").trim());
				int itemNum = Integer.parseInt(e.attributeValue("Num").trim());
				String costType = e.attributeValue("CostType").trim();
				int cost = Integer.parseInt(e.attributeValue("Cost").trim());
				int rand = Integer.parseInt(e.attributeValue("Rand").trim());
				
				if (!AbstractConditionCheck.isCurrencyType(costType)) {
					throw new RuntimeException("Load Shop.xml item CostType:" + costType + " error! ");
				}

				if (!String.valueOf(itemNo).startsWith(GameValue.PROP_N0)
						&& !String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)
						&& !String.valueOf(itemNo).startsWith(GameValue.WEAPAN_NO)) {
					throw new RuntimeException("Load Shop.xml RoleLevels ShopItems Item ProNo:" + itemNo + " error!");
				}
				
				String condition = e.attributeValue("Condition").trim();

				ShopItem item = new ShopItem();
				item.setItemNo(itemNo);
				item.setItemNum(itemNum);
				item.setCostType(costType);
				item.setCost(cost);
				item.setRand(rand);
				result.add(item);
				if(condition != null && condition.length() > 0 && !condition.equalsIgnoreCase("0"))
				{
					item.setCondition(condition);
				}
			}
		}
		return result;
	}

	/**
	 * 加载ShopBuy.xml
	 * @param rootEle
	 */
	public static void loadShopBuy(String xmlName, Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load ShopBuy.xml error! no data!");
		}
		int no = Integer.parseInt(rootEle.attributeValue("No").trim());
		String autoRefreshTime = rootEle.attributeValue("AutoRefreshTime").trim();
		if(autoRefreshTime == null){
			autoRefreshTime = "";
		}
		int fixed = Integer.parseInt(rootEle.attributeValue("Fixed"));
		
		ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(no);
		if (xmlInfo == null) {
			xmlInfo = new ShopXMLInfo();
			xmlInfo.setNo(no);
			ShopXMLInfoMap.addShopXMLInfo(xmlInfo);
		}
		if (!"".equals(autoRefreshTime) && !DateUtil.verifyHMTime(autoRefreshTime)) {
			throw new RuntimeException("Load ShopBuy.xml AutoRefreshTime error! no ：" + no);
		}

		xmlInfo.setAutoRefreshTime(autoRefreshTime);
		xmlInfo.setFixed(fixed);

		// RoleLevels
		List<?> refreshs = rootEle.elements("Refresh");
		refreshsLoad(xmlName, refreshs, xmlInfo,modify);
	}

	/**
	 * 加载ShopBuy.xml Refresh
	 * @param xmlName
	 * @param refreshs
	 * @param xmlInfo
	 */
	private static void refreshsLoad(String xmlName, List<?> refreshs, ShopXMLInfo xmlInfo,boolean modify) {
		if (refreshs != null && refreshs.size() > 0) {
			for (int j = 0; j < refreshs.size(); j++) {
				Element e = (Element) refreshs.get(j);
				int no = Integer.parseInt(e.attributeValue("No").trim());
				String condition = e.attributeValue("Condition").trim();

				ShopRefresh refresh = new ShopRefresh();
				refresh.setNo(no);
				refresh.setConditions(AbstractConditionCheck.generateConds(xmlName, condition));

				if (xmlInfo.getShopRefresh() == null) {
					xmlInfo.setShopRefresh(new HashMap<Integer, ShopRefresh>());
				}
				if (xmlInfo.getShopRefresh().containsKey(no) && !modify) {
					throw new RuntimeException("Load ShopBuy.xml Refresh No:" + no + " repeat!");
				}
				xmlInfo.getShopRefresh().put(no, refresh);
			}
		}

	}

}
