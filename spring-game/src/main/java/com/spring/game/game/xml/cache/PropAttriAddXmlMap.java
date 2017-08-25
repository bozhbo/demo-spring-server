package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.PropAttriAddXml;

public class PropAttriAddXmlMap {
	
	public static HashMap<Integer,PropAttriAddXml> attriMap = new HashMap<Integer,PropAttriAddXml>();
	
	public static void addXmlInfo(PropAttriAddXml xmlInfo)
	{
		attriMap.put(xmlInfo.getNo(), xmlInfo);
	}
	
	public static PropAttriAddXml getXmlInfo(int no)
	{
		return attriMap.get(no);
	}

}
