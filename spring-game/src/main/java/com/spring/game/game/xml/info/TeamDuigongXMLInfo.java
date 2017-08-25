package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TeamDuigongXMLInfo {

	public static Map<Integer, SubTeamDuigongXMLInfo> map = new HashMap<Integer, SubTeamDuigongXMLInfo>();
	
	public static SubTeamDuigongXMLInfo getSubTeamDuigongXMLInfo(int level) {
		Set<Entry<Integer, SubTeamDuigongXMLInfo>> set = map.entrySet();
		
		for (Entry<Integer, SubTeamDuigongXMLInfo> entry : set) {
			if (entry.getValue().getMinLevel() <= level && entry.getValue().getMaxLevel() >= level) {
				return entry.getValue();
			}
		}
		
		return null;
	}
}
