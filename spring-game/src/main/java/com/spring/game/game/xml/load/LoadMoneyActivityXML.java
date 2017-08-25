package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.MoneyActivityMap;
import com.snail.webgame.game.xml.info.MoneyActivity;

public class LoadMoneyActivityXML {

	/**
	 * Money.xml
	 * 
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			String level = rootEle.attributeValue("Lv").trim();
			String bag = rootEle.attributeValue("Bag").trim();

			MoneyActivity moneyActivity = new MoneyActivity();
			moneyActivity.setReward(Integer.parseInt(bag));
			moneyActivity.setLevel(Integer.parseInt(level));

			MoneyActivityMap.addMoneyActivity(moneyActivity);
		}
	}
}
