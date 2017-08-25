package com.snail.webgame.game.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.snail.webgame.game.info.FightArenaInfo;

public class FightArenaInfoMap {

	// 最大排名
	private static AtomicInteger maxPlace = new AtomicInteger();

	// roleId 排名《roleId，info》
	private static ConcurrentHashMap<Integer, FightArenaInfo> roleMap = new ConcurrentHashMap<Integer, FightArenaInfo>();
	// 所有排名 《arenaId，info》
	private static ConcurrentHashMap<Integer, FightArenaInfo> totalMap = new ConcurrentHashMap<Integer, FightArenaInfo>();
	// 所有排名 《place，info》
	private static ConcurrentHashMap<Integer, FightArenaInfo> placeMap = new ConcurrentHashMap<Integer, FightArenaInfo>();

	/**
	 * 添加竞技场信息
	 * @param info
	 */
	public static void addFightArenaInfo(FightArenaInfo info) {
		if (info.getRoleId() != 0) {
			roleMap.put(info.getRoleId(), info);
		}
		totalMap.put(info.getId(), info);
		placeMap.put(info.getPlace(), info);
		if (info.getPlace() > maxPlace.intValue()) {
			maxPlace.set(info.getPlace());
		}
	}

	/**
	 * 交换排名
	 * @param arenaInfo
	 * @param defendArena
	 */
	public static void changePlace(FightArenaInfo arenaInfo, FightArenaInfo defendArena) {
		placeMap.put(arenaInfo.getPlace(), arenaInfo);
		placeMap.put(defendArena.getPlace(), defendArena);
	}

	/**
	 * 获取竞技场信息
	 * @param info
	 */
	public static FightArenaInfo getFightArenaInfobyArenaId(int arenaId) {
		return totalMap.get(arenaId);
	}

	/**
	 * 获取用户竞技场信息
	 * @param info
	 */
	public static FightArenaInfo getFightArenaInfo(int roleId) {
		return roleMap.get(roleId);
	}

	/**
	 * 获取竞技场信息
	 * @param place
	 * @return
	 */
	public static FightArenaInfo getFightArenaInfobyPlace(int place) {
		return placeMap.get(place);
	}

	/**
	 * 获取最大排名+1
	 * @return
	 */
	public static int getMaxPlaceAndIncrement() {
		maxPlace.getAndIncrement();
		return maxPlace.intValue();
	}

	/**
	 * 获取用户信息
	 * @return
	 */
	public static Map<Integer, FightArenaInfo> getRoleMap() {
		return roleMap;
	}

	/**
	 * 获取用户竞技场信息
	 * @param place
	 * @return
	 */
	public static FightArenaInfo getRolePlaceArenaInfo(int place) {
		FightArenaInfo info = placeMap.get(place);
		if (info != null && info.getRoleId() != 0) {
			return info;
		}
		return null;
	}
}
