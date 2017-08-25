package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.InstanceStarXMLMap;
import com.snail.webgame.game.xml.info.InstanceStarXMLInfo;

public class LoadInstanceStarXML {
	
	public static void loadInstanceStarInfo(Element e,boolean modify) {

		InstanceStarXMLInfo xmlInfo = new InstanceStarXMLInfo();
		int no = Integer.parseInt(e.attributeValue("No"));
		int type = Integer.parseInt(e.attributeValue("Type"));
		
		String param = e.attributeValue("Param");
		String numStr = e.attributeValue("Number");
		String valStr = e.attributeValue("Value");

			
		xmlInfo.setNo(no);
		xmlInfo.setType(type);
		if(param != null && param.length() > 0){
			xmlInfo.setParam(param);
		}
		if(numStr != null && numStr.length() > 0){
			xmlInfo.setNum(Integer.parseInt(numStr));
		}
		if(valStr != null && valStr.length() > 0){
			xmlInfo.setValue(Integer.parseInt(valStr));
		}
		InstanceStarXMLMap.addInstance(no, xmlInfo,modify);
		

	}

}
