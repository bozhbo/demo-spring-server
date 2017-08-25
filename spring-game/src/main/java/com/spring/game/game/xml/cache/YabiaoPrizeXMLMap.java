package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.snail.webgame.game.xml.info.YabiaoPrizeXMLInfo;

public class YabiaoPrizeXMLMap {

	private static HashMap<Integer, YabiaoPrizeXMLInfo> map = new HashMap<Integer, YabiaoPrizeXMLInfo>();

	public static void addXml(YabiaoPrizeXMLInfo xmlInfo) {
		map.put(xmlInfo.getBiaocheType(), xmlInfo);
	}

	public static YabiaoPrizeXMLInfo getPlayXMLInfo(int no) {
		return map.get(no);
	}
	
	/**
	 * 刷新镖车，获得新镖车，不能与之前的镖车相同
	 * @param beforeType 以前的镖车类型
	 * @return
	 */
	public static int getBiaocheType(int beforeType)
	{
		List<YabiaoPrizeXMLInfo> list = new ArrayList<YabiaoPrizeXMLInfo>();
		int num = 0;
		
		for(int biaocheType : map.keySet()){
			if(biaocheType == beforeType){
				continue;
			}
			YabiaoPrizeXMLInfo xml = map.get(biaocheType);
			
			if(xml.getMaxRand() < xml.getMinRand()){
				continue;
			}
			
			YabiaoPrizeXMLInfo info = new YabiaoPrizeXMLInfo();
			info.setBiaocheType(biaocheType);
			
			if(list.size() <= 0){
				info.setMinRand(1);
			} else {
				YabiaoPrizeXMLInfo yabiaoPrizeXMLInfo = list.get(list.size() - 1);
				info.setMinRand(yabiaoPrizeXMLInfo.getMaxRand() + 1);
			}
			int value = xml.getMaxRand() - xml.getMinRand();
			num += value + 1;
			info.setMaxRand(info.getMinRand() + value);
			list.add(info);
		}
		int random = new Random().nextInt(num)+1;

		for(YabiaoPrizeXMLInfo info : list){
			if(info.getMinRand() <= random && random <= info.getMaxRand()){
				return info.getBiaocheType();
			}
		}
		
		return 2;
	}

}
