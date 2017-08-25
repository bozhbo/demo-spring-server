package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.info.MineHelpRole;
import com.snail.webgame.game.info.MineInfo;
import com.snail.webgame.game.info.MinePrize;
import com.snail.webgame.game.info.MineRole;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;

public class MineInfoMap {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	// <mineId,MineInfo>
	private static Map<Integer, MineInfo> map = new ConcurrentHashMap<Integer, MineInfo>();

	// <roleId,<minePointId,MinePrize>>
	private static Map<Integer, Map<Integer, MinePrize>> prizeMap = new ConcurrentHashMap<Integer, Map<Integer, MinePrize>>();

	public static void addMineInfo(MineInfo info) {
		if (MineXMLInfoMap.getMineXMLInfo(info.getMineNo()) == null) {
			if (logger.isInfoEnabled()) {
				logger.info("MineId:" + info.getId() + " MineNo:" + info.getMineNo() + " not exit!");
			}
		}
		map.put(info.getId(), info);
	}

	public static void addMineRole(MineRole info) {
		MineInfo mineInfo = map.get(info.getMineId());
		if (mineInfo != null) {
			mineInfo.addMineRole(info);
		}
	}

	public static void addMinePrize(MinePrize info) {
		Map<Integer, MinePrize> prize = null;
		if (info.getStatus() == MineRole.NOT_GET_PRIZE) {
			prize = prizeMap.get(info.getRoleId());
			if (prize == null) {
				prize = new HashMap<Integer, MinePrize>();
				prizeMap.put(info.getRoleId(), prize);
			}
			prize.put(info.getMinePointId(), info);
		}
		for (MineHelpRole helpRole : info.getHelpRoles().values()) {
			if (helpRole.getStatus() == MineRole.NOT_GET_PRIZE) {
				prize = prizeMap.get(helpRole.getRoleId());
				if (prize == null) {
					prize = new HashMap<Integer, MinePrize>();
					prizeMap.put(helpRole.getRoleId(), prize);
				}
				prize.put(info.getMinePointId(), info);
			}
		}
	}

	public static Map<Integer, MinePrize> getMinePrizeMap(int roleId) {
		return prizeMap.get(roleId);
	}

	public static MinePrize getMinePrize(int roleId, int minePointId) {
		Map<Integer, MinePrize> prize = prizeMap.get(roleId);
		if (prize != null) {
			return prize.get(minePointId);
		}
		return null;
	}

	public static Map<Integer, MineInfo> getMineMap() {
		return map;
	}

	/**
	 * 获取角色矿占领（协助）数量
	 * @param roleId
	 * @return
	 */
	public static int getZLAndHelpNum(int roleId) {
		int num = 0;
		for (MineInfo info : map.values()) {
			synchronized (info) {
				if (info.isClosed()) {
					continue;
				}
				if (info.getMineRoleZLAndHelpbyRoleId(roleId) != null) {
					num++;
				}
			}
		}
		return num;
	}

	/**
	 * 获取角色矿占领（协助）List（正在开采）
	 * @param roleId
	 * @return
	 */
	public static List<MineRole> getZLAndHelpList(int roleId) {
		List<MineRole> list = new ArrayList<MineRole>();
		for (MineInfo info : map.values()) {
			synchronized (info) {
				if (info.isClosed()) {
					continue;
				}
				MineRole mineRole = info.getMineRoleZLAndHelpbyRoleId(roleId);
				if (mineRole != null) {
					list.add(mineRole);
				}
			}
		}
		return list;
	}

	/**
	 * 获取角色矿上阵武将
	 * @param roleId
	 * @return
	 */
	public static Set<Integer> getMineHerosbyRoleId(int roleId) {
		Set<Integer> heroIds = new HashSet<Integer>();
		MineHelpRole helpRole = null;
		for (MineInfo info : map.values()) {
			synchronized (info) {
				if (info.isClosed()) {
					continue;
				}
				for (MineRole mineRole : info.getRoles().values()) {
					if (mineRole.isEnd()) {
						continue;
					}
					if (mineRole.getRoleId() == roleId) {
						heroIds.addAll(mineRole.getHeroMap().values());
					}
					helpRole = mineRole.getHelpRoles(roleId);
					if (helpRole != null) {
						heroIds.addAll(helpRole.getHeroMap().values());
					}
				}
			}
		}
		return heroIds;
	}

	/**
	 * 获取矿上阵武将
	 * @return
	 */
	public static Set<Integer> getMineHeros() {
		Set<Integer> heroIds = new HashSet<Integer>();
		for (MineInfo info : map.values()) {
			synchronized (info) {
				if (info.isClosed()) {
					continue;
				}
				for (MineRole mineRole : info.getRoles().values()) {
					if (mineRole.isEnd()) {
						continue;
					}
					heroIds.addAll(mineRole.getHeroMap().values());
					for (MineHelpRole helpRole : mineRole.getHelpRoles().values()) {
						heroIds.addAll(helpRole.getHeroMap().values());
					}
				}
			}
		}
		return heroIds;
	}

	public static MineInfo getMineInfo(int mineId) {
		return map.get(mineId);
	}

	public static void removeMine(int mineId) {
		map.remove(mineId);
	}

	public static void removeFightInfo(int roleId) {
		for (MineInfo info : map.values()) {
			info.removeFightInfo(roleId);
		}
	}
	
	public static void removeMinePrize(int roleId, int minePointId) {
		Map<Integer, MinePrize> prize = prizeMap.get(roleId);
		if (prize != null) {
			prize.remove(minePointId);
		}
	}
}
