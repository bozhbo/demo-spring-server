package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.CampaignXMLInfo;

public class CampaignXMLInfoMap {

	private static HashMap<Integer, CampaignXMLInfo> map = new HashMap<Integer, CampaignXMLInfo>();

	public static void addCampaignXMLInfo(CampaignXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
	}

	public static CampaignXMLInfo getCampaignXMLInfo(int no) {
		return map.get(no);
	}

}
