package com.snail.webgame.game.xml.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.xml.info.StoneComeXML;

public class StoneCompXMLMap {

	private static ConcurrentHashMap<Integer, StoneComeXML> map = new ConcurrentHashMap<Integer, StoneComeXML>();

	public static void addXmlInfo(StoneComeXML xmlInfo)
	{
		map.put(xmlInfo.getNo(), xmlInfo);
	}
	
	public static StoneComeXML getXml(int no)
	{
		return map.get(no);
	}
}
