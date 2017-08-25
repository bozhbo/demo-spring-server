package com.snail.webgame.game.protocal.soldier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.soldier.query.QuerySoldierInfoRe;

public class SoldierService {
	private static final String SOLDIER_INIT_DATA = "1,1,1,1,1";

	/**
	 * 获取武将等级信息
	 * @param roleLoadInfo
	 * @return
	 */
	public static List<QuerySoldierInfoRe> getSoldierInfoRe(RoleInfo roleInfo) {
		String[] soldierInfos = getSoldierInfos(roleInfo);
		if (soldierInfos == null) {
			return null;
		}
		List<QuerySoldierInfoRe> querySoldierInfoRes = new ArrayList<QuerySoldierInfoRe>();
		QuerySoldierInfoRe querySoldierInfoRe = null;
		for (int i = 0; i < soldierInfos.length; i++) {
			querySoldierInfoRe = new QuerySoldierInfoRe();
			querySoldierInfoRe.setSoldierType(Byte.parseByte(String.valueOf(i + 1)));
			querySoldierInfoRe.setSoldierLevel(Integer.parseInt(soldierInfos[i]));
			querySoldierInfoRes.add(querySoldierInfoRe);
		}
		return querySoldierInfoRes;
	}

	/**
	 * 获取士兵等级
	 * @param roleInfo
	 * @param soldierType
	 * @return
	 */
	public static int getSoldierLevel(RoleInfo roleInfo, byte soldierType) {
		String[] soldierInfos = getSoldierInfos(roleInfo);
		if (soldierInfos == null) {
			return 0;
		}
		// 验证类型是否存在
		if (soldierType > soldierInfos.length) {
			return 0;
		}
		return Integer.parseInt(soldierInfos[soldierType - 1]);
	}

	public static Map<Byte, Integer> getSoldierMap(RoleInfo roleInfo) {
		String[] soldierInfos = getSoldierInfos(roleInfo);
		if (soldierInfos == null) {
			return null;
		}
		Map<Byte, Integer> soldierMap = new HashMap<Byte, Integer>();
		for (int i = 0; i < soldierInfos.length; i++) {
			byte soldierType = (byte) (i + 1);
			int soldierLevel = Integer.parseInt(soldierInfos[i]);
			soldierMap.put(soldierType, soldierLevel);
		}
		return soldierMap;
	}

	/**
	 * 获取分割好的士兵等级信息
	 * @param roleLoadInfo
	 * @return
	 */
	public static String[] getSoldierInfos(RoleInfo roleInfo) {
		if (roleInfo == null) {
			return null;
		}
		String soldierInfo = roleInfo.getSoldierInfo();
		if (soldierInfo == null || StringUtils.isBlank(soldierInfo)) {
			soldierInfo = SOLDIER_INIT_DATA;
		}
		String[] soldierInfos = soldierInfo.split(",");
		if (soldierInfos.length != SOLDIER_INIT_DATA.split(",").length) {
			soldierInfo = SOLDIER_INIT_DATA;
			soldierInfos = soldierInfo.split(",");
		}
		return soldierInfos;
	}

	/**
	 * 获取士兵升级次数
	 * @param roleLoadInfo
	 * @return
	 */
	public static Map<Byte, Byte> getSoldierUpCounterInfoMap(RoleLoadInfo roleLoadInfo) {
		String str = roleLoadInfo.getSoldierUpCounterInfo();
		Map<Byte, Byte> map = new HashMap<Byte, Byte>();

		if (null == str || StringUtils.isBlank(str)) {
			return map;
		}

		String[] strArray = str.split(";");
		String[] infos = null;
		for (String array : strArray) {
			infos = array.split(":");
			try {
				byte type = Byte.parseByte(infos[0]);
				byte counter = Byte.parseByte(infos[1]);

				map.put(type, counter);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return map;
	}

	/**
	 * 获取士兵升级次数的字符串
	 * @param map
	 * @return
	 */
	public static String getSoldierUpCounterInfo(Map<Byte, Byte> map) {
		if (null == map || map.size() <= 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		for (Map.Entry<Byte, Byte> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
		}

		return sb.substring(0, sb.length() - 1).toString();
	}
}
