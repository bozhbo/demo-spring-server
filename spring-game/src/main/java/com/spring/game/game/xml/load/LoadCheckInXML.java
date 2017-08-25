package com.snail.webgame.game.xml.load;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.CheckInPrizeXmlMap;
import com.snail.webgame.game.xml.cache.LoginActiviryXmlMap;
import com.snail.webgame.game.xml.info.CheckInPrizeXMLInfo;
import com.snail.webgame.game.xml.info.LoginActiveXMLInfo;
import com.snail.webgame.game.xml.info.LoginActiveXMLItemInfo;

public class LoadCheckInXML {
	public static void loadCheckInPrize(Element rootEle,boolean modify) {
		if (rootEle != null) {
			CheckInPrizeXMLInfo info = new CheckInPrizeXMLInfo();
			info.setNo(Integer.valueOf(rootEle.attributeValue("No").trim()));
			info.setBagNoStr(rootEle.attributeValue("PrizeNo").trim());
			
			if (rootEle.attributeValue("PrizeNo1") != null && !"".equals(rootEle.attributeValue("PrizeNo1").trim())) {
				info.setVipBagNoStr(rootEle.attributeValue("PrizeNo1"));
			}
			
			if (rootEle.attributeValue("VipLv") != null && !"".equals(rootEle.attributeValue("VipLv").trim())) {
				info.setNeedVipLv(Integer.valueOf(rootEle.attributeValue("VipLv")));
			}
			
			CheckInPrizeXmlMap.addXmlInfo(info);
		}
	}
	
	public static void loadLoginActive(Element rootEle,boolean modify){
		if (rootEle != null) {
			LoginActiveXMLInfo info = new LoginActiveXMLInfo();
			info.setNo(Integer.valueOf(rootEle.attributeValue("No").trim()));
			info.setType(Integer.valueOf(rootEle.attributeValue("Type").trim()));
			info.setActionType(Integer.valueOf(rootEle.attributeValue("ActionType").trim()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				info.setStartTime(sdf.parse(rootEle.attributeValue("StartTime").trim()).getTime());
				info.setEndTime(sdf.parse(rootEle.attributeValue("EndTime").trim()).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			List<?> loginElems = rootEle.elements("Login");
			if (loginElems != null && loginElems.size() > 0) {
				for (int i = 0; i < loginElems.size(); i++) {
					Element loginEle = (Element) loginElems.get(i);
					LoginActiveXMLItemInfo loginActiveXMLItemInfo = new LoginActiveXMLItemInfo();
					loginActiveXMLItemInfo.setNo(Integer.parseInt(loginEle.attributeValue("No").trim()));
					loginActiveXMLItemInfo.setNeedNum(Integer.parseInt(loginEle.attributeValue("NeedNum").trim()));
					loginActiveXMLItemInfo.setLoginTime(Integer.parseInt(loginEle.attributeValue("LoginTime").trim()));
					
					List<?> prizeElems = loginEle.elements("Prize");
					if(prizeElems != null && prizeElems.size() > 0){
						String[] prizeStrs = new String[prizeElems.size()];
						for (int s = 0; s < prizeElems.size(); s++) {
							Element prizeEle = (Element) prizeElems.get(s);
							prizeStrs[s] = prizeEle.attributeValue("ItemNo").trim()+","+prizeEle.attributeValue("ItemNum").trim();
						}
						loginActiveXMLItemInfo.setItems(prizeStrs);
					}
					info.getActiveXMLItemInfos().add(loginActiveXMLItemInfo);
				}
				//按照No升序排列
				Collections.sort(info.getActiveXMLItemInfos());
			}
			LoginActiviryXmlMap.addXmlInfo(info);
		}
	}
}
