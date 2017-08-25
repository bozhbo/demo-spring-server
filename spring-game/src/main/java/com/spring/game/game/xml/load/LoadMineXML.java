package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.info.MineXMLInfo;

/**
 * 加载矿信息
 * @author zenggang
 */
public class LoadMineXML {

	/**
	 * 加载
	 * @param rootEle
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			MineXMLInfo info = new MineXMLInfo();
			info.setNo(Integer.parseInt(rootEle.attributeValue("No").trim()));
			info.setName(rootEle.attributeValue("Name").trim());
			info.setOutType(rootEle.attributeValue("OutType").trim());
			info.setOutPeriod(Integer.parseInt(rootEle.attributeValue("OutPeriod").trim()));
			info.setOutTimeAdd(Integer.parseInt(rootEle.attributeValue("OutTimeAdd").trim()));
			info.setMineTime(Integer.parseInt(rootEle.attributeValue("MineTime").trim()));
			info.setNewMineTime(Integer.parseInt(rootEle.attributeValue("NewMineTime").trim()));
			info.setMaxMiners(Integer.parseInt(rootEle.attributeValue("MaxMiners").trim()));
			info.setGuardNum(Integer.parseInt(rootEle.attributeValue("GuardNo").trim()));

			info.setGuardTime(Integer.parseInt(rootEle.attributeValue("GuardTime").trim()));
			info.setResreshCD(Integer.parseInt(rootEle.attributeValue("ResreshCD").trim()));

			info.setMaxNum(Integer.parseInt(rootEle.attributeValue("MaxNo").trim()));

			info.setGold(Integer.parseInt(rootEle.attributeValue("Gold").trim()));
			info.setGoldDropChance(Integer.parseInt(rootEle.attributeValue("GoldDropChance").trim()));

			// 默认敌军（读GW表）
			String gwStr = rootEle.attributeValue("GW").trim();
			if (gwStr == null || gwStr.length() <= 0) {
				throw new RuntimeException("Load Mine.xml error!  No :" + info.getNo() + " GW null");
			}

			info.setGw(gwStr.split(","));
			for (String gwNo : info.getGw()) {
				if (GWXMLInfoMap.getNPCGWXMLInfo(gwNo) == null) {
					throw new RuntimeException("Load Mine.xml error!  No :" + info.getNo() + " GW error");
				}
			}

			if (MineXMLInfoMap.getMineXMLInfo(info.getNo()) != null && !modify) {
				throw new RuntimeException("Load Mine.xml error!  No :" + info.getNo() + " repeat");
			}
			MineXMLInfoMap.addMineXMLInfo(info);
		}

	}
}
