package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.FormationXMLInfo;

/**
 * 阵型xml
 * 
 * @author tangjq
 * 
 */
public class FormationXMLInfoMap {

	private static Map<Integer, FormationXMLInfo> map = new HashMap<Integer, FormationXMLInfo>();

	public static Map<Integer, FormationXMLInfo> getMap() {
		return map;
	}

	/**
	 * 添加xml
	 * 
	 * @param info
	 */
	public static void addXml(FormationXMLInfo info) {
		map.put(info.getNo(), info);
	}

	/**
	 * 根据编号获取
	 * 
	 * @param no
	 * @return
	 */
	public static FormationXMLInfo getXmlByNo(int no) {
		return map.get(no);
	}
}
