package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.xml.info.StageMoneyXMLInfo;

public class StageMoneyXMLInfoMap {

	private static List<StageMoneyXMLInfo> list = new ArrayList<StageMoneyXMLInfo>();
	
	public static void addStageMoneyXMLInfo(StageMoneyXMLInfo xmlInfo) {
		list.add(xmlInfo);
	}

	public static List<StageMoneyXMLInfo> getStageMoneyXMLInfo() {
		return list;
	}
}
