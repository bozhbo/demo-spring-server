package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.xml.info.MineXMLInfo;

/**
 * 矿类型信息
 * @author zenggang
 */
public class MineXMLInfoMap {

	public static Map<Integer, MineXMLInfo> map = new HashMap<Integer, MineXMLInfo>();

	public static void addMineXMLInfo(MineXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
	}

	public static Map<Integer, MineXMLInfo> getMap() {
		return map;
	}

	public static MineXMLInfo getMineXMLInfo(int no) {
		return map.get(no);
	}

	/**
	 * 矿固定数量
	 * @return
	 */
	public static int getMineNum() {
		return GameValue.SCENE_MINE_NUM_LIMIT;
	}

	/**
	 * 所有矿位置
	 * @return
	 */
	public static List<Integer> getPostions() {
		// 所有矿位置
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 101; i <= 100 + GameValue.SCENE_MINE_POSTION_LIMIT; i++) {
			list.add(i);
		}
		return list;
	}
}
