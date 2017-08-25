package com.snail.webgame.game.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.RoleInfo;

/**
 * 用户信息缓存
 * @author cici
 */
public class RoleInfoMap {

	// <roleId,RoleInfo>
	private static ConcurrentHashMap<Integer, RoleInfo> map = new ConcurrentHashMap<Integer, RoleInfo>();

	// <account,<roleId,RoleInfo>>
	private static ConcurrentHashMap<String, HashMap<Integer, RoleInfo>> accMap = new ConcurrentHashMap<String, HashMap<Integer, RoleInfo>>();

	// <roleName,RoleInfo>
	private static ConcurrentHashMap<String, RoleInfo> nameMap = new ConcurrentHashMap<String, RoleInfo>();
		
	// 最大在线人数
	private static int maxOnlineNum = 0;
	
	public static int registerNum = 0;
	
	public static long chestCoinCost;//金子抽奖总消耗(非功能)

	public static Map<Integer, RoleInfo> getMap() {
		return map;
	}

	public static RoleInfo getRoleInfo(int roleId) {
		return map.get(roleId);
	}

	public static void addRoleInfo(RoleInfo info) {
		map.put(info.getId(), info);
		String account = info.getAccount().toUpperCase();
		if (!accMap.containsKey(account)) {
			accMap.putIfAbsent(account, new HashMap<Integer, RoleInfo>());
		}
		Map<Integer, RoleInfo> acc = accMap.get(account);
		acc.put(info.getId(), info);

		nameMap.put(info.getRoleName().trim().toUpperCase(), info);		
	}

	public static void removeRoleInfo(int roleId) {
		RoleInfo info = map.get(roleId);
		if (info != null) {
			map.remove(roleId);
			Map<Integer, RoleInfo> acc = accMap.get(info.getAccount().toUpperCase());
			if (acc != null) {
				acc.remove(roleId);
			}
			nameMap.remove(info.getRoleName().trim().toUpperCase());
		}
	}

	public static void changeRoleName(RoleInfo info, String oldName) {
		nameMap.remove(oldName.trim().toUpperCase());
		nameMap.put(info.getRoleName().trim().toUpperCase(), info);
	}

	public static HashMap<Integer, RoleInfo> getRoleMapByAccount(String account) {
		return accMap.get(account.toUpperCase());
	}

	public static Set<Integer> getRoleInfoSet() {
		Set<Integer> set = map.keySet();
		// 放到一个新的set中，防止在遍历时，另一个线程改变map大小，引起并发异常
		Set<Integer> set1 = new HashSet<Integer>();
		set1.addAll(set);
		return set1;
	}

	/**
	 * 记录最大在线人数
	 */
	public static void recalMaxOnlineNum() {
		int k = getOnlineSize();
		if (maxOnlineNum < k) {
			maxOnlineNum = k;
		}
	}

	public static int getMaxOnlineSize() {
		return maxOnlineNum;
	}

	/**
	 * 游戏中的角色
	 * @return
	 */
	public static int getOnlineSize() {
		return (int) RoleLoginMap.getSize();
	}

	/**
	 * 角色名查找角色
	 * @param roleAcount
	 * @return
	 */
	public static RoleInfo getRoleInfoByName(String roleName) {
		return nameMap.get(roleName.trim().toUpperCase());
	}

	public static Set<Entry<Integer, RoleInfo>> getRoleInfoEntrySet() {
		return map.entrySet();
	}
}
