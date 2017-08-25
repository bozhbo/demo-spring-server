package com.snail.webgame.game.xml.load;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.xml.cache.FixLotteryInfoMap;
import com.snail.webgame.game.xml.cache.RecruitDepotXMLInfoMap;
import com.snail.webgame.game.xml.info.FixLotteryConfigInfo;

public class LoadFixLotteryXML {
	
	public static void load(Element rootEle,boolean modify){
		if (rootEle == null) {
			throw new RuntimeException("Load FixLottery.xml error! no data!");
		}
		
		int roleType = Integer.parseInt(rootEle.attributeValue("RoleType"));
		
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(roleType);
		
		if(heroXMLInfo == null || heroXMLInfo.getInitial() != 1){
			throw new RuntimeException("Load FixLottery.xml error! roleType = " + roleType);
		}
		
		int costType = Integer.parseInt(rootEle.attributeValue("CostType"));
		
		if(costType != 1 && costType != 2 && costType != 3){
			throw new RuntimeException("Load FixLottery.xml error! costType = " + costType);
		}
		
		List<?> numberList = rootEle.elements("Number");
		loadNumber(roleType, costType, numberList,modify);
		
		
	}
	
	
	private static void loadNumber(int roleType, int costType, List<?> numberList,boolean modify) {
		if(numberList == null || numberList.size() <= 0){
			throw new RuntimeException("Load FixLottery.xml Number element no data!");
		}
		Element e = null;
		List<?> shopItemsList = null;
		for(int i = 0; i < numberList.size(); i++){
			e = (Element) numberList.get(i);
			int no = Integer.valueOf(e.attributeValue("No"));
			if(FixLotteryInfoMap.fixLotteryNoCheck(costType, roleType, no) && !modify){
				throw new RuntimeException("Load FixLottery.xml No element repeat!");
			}
			shopItemsList = e.elements("Items");
			loadShopItems(roleType, costType, no, shopItemsList);
			
		}
	}
	
	
	private static void loadShopItems(int roleType, int costType, int no, List<?> shopItemsList) {
		if(shopItemsList == null || shopItemsList.size() <= 0){
			throw new RuntimeException("Load FixLottery.xml Items element no data!");
		}
		
		if(shopItemsList.size() > 10){
			throw new RuntimeException("Load FixLottery.xml Items element overflow!");
		}
		
		Element e = null;
		FixLotteryConfigInfo info = null;
		List<FixLotteryConfigInfo> list = new ArrayList<FixLotteryConfigInfo>();
		for(int i = 0; i < shopItemsList.size(); i++){
			info = new FixLotteryConfigInfo();
			e = (Element) shopItemsList.get(i);
			
			String itemNo = e.attributeValue("ItemNo");
			if (!itemNo.startsWith(GameValue.EQUIP_N0) && !itemNo.startsWith(GameValue.PROP_N0)  && !itemNo.startsWith(GameValue.WEAPAN_NO)
					&& RecruitDepotXMLInfoMap.getItems(itemNo) == null) {
				throw new RuntimeException("Load FixLottery.xml error! No not startsWith " + GameValue.EQUIP_N0 + " or " + GameValue.PROP_N0 + " or " + GameValue.WEAPAN_NO + "ItemNo " + itemNo);
			}
			
			info.setNo(no);
			info.setItemNo(Integer.parseInt(itemNo));
			info.setNum(Integer.parseInt(e.attributeValue("Num")));
			list.add(info);
		}
		
		FixLotteryInfoMap.addFixLotteryInfoList(costType, roleType, no, list);
		
	}
}
