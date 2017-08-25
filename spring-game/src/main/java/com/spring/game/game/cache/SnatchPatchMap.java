package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.xml.cache.SnatchMap;

/**
 * 夺宝活动 人员石头信息
 * @author zenggang
 */
public class SnatchPatchMap {

	// 夺宝活动石头信息 <itemNo,List<RoleId>>
	private static ConcurrentHashMap<Integer, Set<Integer>> rivalMap = new ConcurrentHashMap<Integer, Set<Integer>>();

	public static void addRivalInfo(int itemNo, int roleId) {
		if (SnatchMap.get(itemNo) == null) {
			return;
		}

		Set<Integer> list = new HashSet<Integer>();
		Set<Integer> tempList = rivalMap.putIfAbsent(itemNo, list);

		if (tempList != null) {
			list = tempList;
		}

		synchronized (list) {
			list.add(roleId);
		}
	}

	public static void removeRivalInfo(int itemNo, int roleId) {
		Set<Integer> list = rivalMap.get(itemNo);
		if (list != null) {
			synchronized (list) {
				list.remove((Integer) roleId);
			}
		}
	}

	/**
	 * 随机取值
	 * @param itemNo
	 * @param radomSize
	 * @return
	 */
	public static List<Integer> getRadomRivalList(int itemNo, int radomSize) {
		// 无序取值
		Set<Integer> list = rivalMap.get(itemNo);
		if (list != null) {
			synchronized (list) {
				List<Integer> result = null;
				if (list.size() <= radomSize) {
					result = new ArrayList<Integer>(list);
					Collections.shuffle(result);
				} else {
					List<Integer> all = new ArrayList<Integer>(list);
					result = new ArrayList<Integer>();
					int num = 0;
					while (result.size() < radomSize && num < 200) {
						num++;
						int index = RandomUtil.getRandom(0, all.size() - 1);
						int roleId = all.get(index);
						if (!result.contains(roleId)) {
							result.add(roleId);
						}
					}
				}
				return result;
			}
		}
		return null;
	}
}
