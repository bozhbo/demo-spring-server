package com.snail.webgame.game.xml.load;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.GoldBuyXMLInfoMap;
import com.snail.webgame.game.xml.info.GoldBuyXMLInfo;
import com.snail.webgame.game.xml.info.GoldBuyXMLPro;
import com.snail.webgame.game.xml.info.GoldBuyXMLRand;
import com.snail.webgame.game.xml.info.GoldBuyXMLRandItem;

public class LoadGoldBuyXML {

	/**
	 * 加载GoldBuy.xml
	 * @param xmlName
	 * @param e
	 */
	public static void loadXml(String xmlName, Element e,boolean modify) {
		int no = Integer.parseInt(e.attributeValue("No"));
		int type = Integer.parseInt(e.attributeValue("Type"));
		int fixed = Integer.parseInt(e.attributeValue("Fixed"));
		if (type != 1 && type != 2) {
			throw new RuntimeException("Load GoldBuy.xml error! Type: " + type + " error");
		}
		if (fixed != 0 && fixed != 1) {
			throw new RuntimeException("Load GoldBuy.xml error! Fixed: " + fixed + " error");
		}

		GoldBuyXMLInfo info = new GoldBuyXMLInfo();
		info.setNo(no);
		info.setType(type);
		info.setFixed(fixed);

		// 升级
		List<?> pros = e.elements("Property");
		proLoad(xmlName, pros, info);

		if (GoldBuyXMLInfoMap.getGoldBuyXMLInfo(no) != null && !modify) {
			throw new RuntimeException("Load GoldBuy.xml error! no: " + info.getNo() + " repeat");
		}
		GoldBuyXMLInfoMap.addGoldBuyXMLInfo(info);
	}

	/**
	 * 加载GoldBuy.xml Property
	 * @param xmlName
	 * @param pros
	 * @param info
	 */
	private static void proLoad(String xmlName, List<?> list, GoldBuyXMLInfo info) {
		if (list != null && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				Element e = (Element) list.get(j);
				int no = Integer.parseInt(e.attributeValue("No"));
				int gold = Integer.parseInt(e.attributeValue("Gold"));
				String condition = e.attributeValue("Condition");
				int gain = Integer.parseInt(e.attributeValue("Gain").trim());
				int mulRandNo = Integer.parseInt(e.attributeValue("MulRandNo").trim());

				GoldBuyXMLPro u = new GoldBuyXMLPro();
				u.setNo(no);
				u.setGold(gold);
				u.setConditions(AbstractConditionCheck.generateConds(xmlName, condition));
				u.setGain(gain);
				u.setMulRandNo(mulRandNo);

				if (info.getPros() == null) {
					info.setPros(new HashMap<Integer, GoldBuyXMLPro>());
				}
				if (info.getPros().containsKey(no)) {
					throw new RuntimeException("Load GoldBuy.xml error! no: " + info.getNo() + " Property no: " + no
							+ " repeat");
				}
				if (info.getMaxBuyNum() < no) {
					info.setMaxBuyNum(no);
				}

				info.getPros().put(no, u);
			}
		}
	}

	/**
	 * 加载 GoldBuyRand.xml
	 * @param e
	 */
	public static void loadRandXml(Element e,boolean modify) {
		int no = Integer.parseInt(e.attributeValue("No"));
		GoldBuyXMLRand info = new GoldBuyXMLRand();
		info.setNo(no);
		// 升级
		List<?> items = e.elements("Items");
		itemLoad(info, items);

		if (GoldBuyXMLInfoMap.getGoldBuyXMLRand(no) != null && !modify) {
			throw new RuntimeException("Load GoldBuyRand.xml error! no: " + info.getNo() + " repeat");
		}
		GoldBuyXMLInfoMap.addGoldBuyXMLRand(info);

	}

	/**
	 * 加载 GoldBuyRand.xml Items
	 * @param info
	 * @param items
	 */
	private static void itemLoad(GoldBuyXMLRand info, List<?> items) {
		if (items != null && items.size() > 0) {
			for (int j = 0; j < items.size(); j++) {
				Element e1 = (Element) items.get(j);
				float mul = Float.parseFloat(e1.attributeValue("Mul").trim());
				int minRand = Integer.parseInt(e1.attributeValue("minRand"));
				int maxRand = Integer.parseInt(e1.attributeValue("maxRand"));

				GoldBuyXMLRandItem item = new GoldBuyXMLRandItem();
				item.setMul(mul);
				item.setMinRand(minRand);
				item.setMaxRand(maxRand);

				if (info.getMulMap() == null) {
					info.setMulMap(new HashMap<Float, GoldBuyXMLRandItem>());
				}
				if (info.getMulMap().containsKey(mul)) {
					throw new RuntimeException("Load GoldBuyRand.xml error! no: " + info.getNo() + " Items Mul: " + mul
							+ " repeat");
				}
				info.getMulMap().put(mul, item);
			}
		}
	}

}
