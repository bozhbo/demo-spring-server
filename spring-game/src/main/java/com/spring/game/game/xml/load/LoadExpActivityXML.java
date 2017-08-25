package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.ExpActivityMap;
import com.snail.webgame.game.xml.info.ExpActivity;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;

public class LoadExpActivityXML {

	/**
	 * Exp.xml
	 * 
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			String level = rootEle.attributeValue("Lv").trim();
			String quantity = rootEle.attributeValue("Quantity").trim();
			String npcBag = rootEle.attributeValue("NpcBag").trim();
			String bossBag = rootEle.attributeValue("BossBag").trim();
			String itemNo = rootEle.attributeValue("Item").trim();
			String bag = rootEle.attributeValue("Bag");

			ExpActivity expActivity = new ExpActivity();
			expActivity.setBossReward(Integer.parseInt(bossBag));
			expActivity.setName(rootEle.attributeValue("Name").trim());
			expActivity.setNpcReward(Integer.parseInt(npcBag));
			expActivity.setLevel(Integer.parseInt(level));
			expActivity.setQuantity(Integer.parseInt(quantity));
			expActivity.setItemNo(itemNo);
			if(bag != null && bag.length() > 0)
			{
				expActivity.setBag(bag.trim());
			}
			
			List<?>points = rootEle.elements("Point");
			if(points != null && points.size() > 0)
			{
				for(int z = 0; z < points.size(); z++){
					Element pointElemt = (Element) points.get(z);
					BattleDetailPoint point = new BattleDetailPoint();
					point.setPointNo(Integer.valueOf(pointElemt.attributeValue("No").trim()));
					point.setGw(pointElemt.attributeValue("NPC").trim());		
					if(point != null)
					{
						expActivity.getPointsMap().put(point.getPointNo(), point);
					}
				}
			}

			ExpActivityMap.addExpActivity(expActivity);
		}
	}
}
