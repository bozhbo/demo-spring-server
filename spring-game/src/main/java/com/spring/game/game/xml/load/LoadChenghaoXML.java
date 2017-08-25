package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.ChenghaoXMLInfoMap;
import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;
import com.snail.webgame.game.xml.info.TitleValue;

public class LoadChenghaoXML {
	
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			String type = rootEle.attributeValue("Type").trim();
			int num = Integer.valueOf(rootEle.attributeValue("Num").trim());
			int keepTime = Integer.valueOf(rootEle.attributeValue("KeepTime").trim());
			
			ChenghaoXMLInfo info = new ChenghaoXMLInfo();
			info.setNo(no);
			info.setType(type);
			info.setNum(num);
			info.setKeepTime(keepTime);
			
			List<?> list = rootEle.elements("Attribute");
			if(list != null && list.size() > 0){
				Element e = null;
				TitleValue titleValue = null;
				for(int i = 0; i < list.size(); i++){
					e = (Element) list.get(i);
					titleValue = new TitleValue();
					titleValue.setAddType(Integer.valueOf(e.attributeValue("AddType")));
					titleValue.setAddNum(Integer.valueOf(e.attributeValue("AddNum")));
					info.getTitleValueList().add(titleValue);
				}
				
			}
			
			ChenghaoXMLInfoMap.addChenghaoXMLInfo(info);
		}
	}
}
