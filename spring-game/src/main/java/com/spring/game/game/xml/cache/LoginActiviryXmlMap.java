package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.LoginActiveXMLInfo;

public class LoginActiviryXmlMap {
	
	public static HashMap<Integer,LoginActiveXMLInfo> map = new HashMap<Integer,LoginActiveXMLInfo>();
	
	public static void addXmlInfo(LoginActiveXMLInfo xmlInfo)
	{
		map.put(xmlInfo.getNo(), xmlInfo);
	}
	
	public static LoginActiveXMLInfo getXmlInfo(int no)
	{
		return map.get(no);
	}

}
