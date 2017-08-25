package com.snail.webgame.game.xml.load;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.snail.webgame.game.xml.info.TeamChallengeXmlInfo;

public class TeamChallengeXmlLoader {
	
	private static Map<Integer, TeamChallengeXmlInfo> teamChallengeMap = new HashMap<Integer, TeamChallengeXmlInfo>();

	public static void loadFromEle(Element element,boolean modify) {
		
		// =========  Property  ============
		
		Element el1 = element;
		TeamChallengeXmlInfo xmlInfo = new TeamChallengeXmlInfo();
		// No
		String noStr = el1.attributeValue("No");
		if (noStr == null || "".equals(noStr)) {
			throw new IllegalStateException("Failed to read TeamChallenge.xml file,Cause No attribute is null!") ;
		}
		int no = Integer.valueOf(noStr);
		xmlInfo.setNo(no);
		
		String name = el1.attributeValue("Name");
		xmlInfo.setName(name);
		
		// Lv
		String lvStr = el1.attributeValue("Lv");
		if (lvStr == null || "".equals(lvStr)) {
			throw new IllegalStateException("Failed to read TeamChallenge.xml file,Cause Lv attribute is null!") ;
		}
		int lv = Integer.valueOf(lvStr);
		xmlInfo.setLv(lv);
		
		// EndTime
		String endTimeStr = el1.attributeValue("EndTime");
		if (endTimeStr == null || "".equals(endTimeStr)) {
			throw new IllegalStateException("Failed to read TeamChallenge.xml file,Cause EndTime attribute is null!") ;
		}
		int endTime = Integer.valueOf(endTimeStr);
		xmlInfo.setEndTime(endTime);
		
		// MaxLv
		String maxLvStr = el1.attributeValue("MaxLv");
		if (maxLvStr == null || "".equals(maxLvStr)) {
			throw new IllegalStateException("Failed to read TeamChallenge.xml file,Cause maxLv attribute is null!") ;
		}
		int maxLv = Integer.valueOf(maxLvStr);
		xmlInfo.setMaxLv(maxLv);
		
		// MaxLv
		String timesStr = el1.attributeValue("Restrict");
		if (timesStr == null || "".equals(timesStr)) {
			throw new IllegalStateException("Failed to read TeamChallenge.xml file,Cause Restrict attribute is null!") ;
		}
		int times = Integer.valueOf(timesStr);
		xmlInfo.setTimes(times);
		
		// MaxLv
		String bagStr = el1.attributeValue("Bag");
		if (bagStr == null || "".equals(bagStr)) {
			throw new IllegalStateException("Failed to read TeamChallenge.xml file,Cause Bag attribute is null!") ;
		}
		xmlInfo.setBag(bagStr);
		
		teamChallengeMap.put(no, xmlInfo);
	}
	
	public static Map<Integer, TeamChallengeXmlInfo> getTeamChallengeMap() {
		return teamChallengeMap;
	}
	
	public static void setTeamChallengeMap(
			Map<Integer, TeamChallengeXmlInfo> teamChallengeMap) {
		TeamChallengeXmlLoader.teamChallengeMap = teamChallengeMap;
	}
	
	public static TeamChallengeXmlInfo getTeamChallenge(int no){
		return teamChallengeMap.get(no);
	}
}
