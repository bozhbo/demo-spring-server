package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.PushXMLInfoMap;
import com.snail.webgame.game.xml.info.PushXMLInfo;

public class LoadPushXML {
	
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int pushType = Integer.valueOf(rootEle.attributeValue("NotifyType").trim());
			String title = rootEle.attributeValue("Title").trim();
			String content = rootEle.attributeValue("Context").trim();

			PushXMLInfo info = new PushXMLInfo();
			info.setNo(no);
			info.setPushType(pushType);
			info.setTitle(title);
			info.setContent(content);
			
			PushXMLInfoMap.addPushXMLInfo(info);
		}
	}
}
