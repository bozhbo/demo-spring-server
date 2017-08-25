package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.StoreItemInfo;

public class StoreItemInfoMap {

	public static void addStoreItemInfo(RoleLoadInfo loadInfo, StoreItemInfo info) {
		if (loadInfo != null) {
			loadInfo.getStoreItemInfoMap().put(info.getId(), info);
		}
	}

	public static Map<Integer, StoreItemInfo> getRoleMap(RoleInfo roleInfo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			return roleInfo.getRoleLoadInfo().getStoreItemInfoMap();
		}
		
		return null;
	}

	public static StoreItemInfo getStoreItemInfo(RoleInfo roleInfo, int itemId) {
		if (roleInfo.getRoleLoadInfo() != null) {
			return roleInfo.getRoleLoadInfo().getStoreItemInfoMap().get(itemId);
		}
		
		return null;
	}

	public static StoreItemInfo getStoreItemInfo(RoleInfo roleInfo, int storeType, int itemId) {
		if (roleInfo.getRoleLoadInfo() != null) {
			StoreItemInfo item = roleInfo.getRoleLoadInfo().getStoreItemInfoMap().get(itemId);
			
			if (item != null && item.getStoreType() == storeType) {
				return item;
			}
		}

		return null;
	}

	public static List<StoreItemInfo> getStoreItemList(RoleInfo roleInfo, int storeType) {
		List<StoreItemInfo> list = new ArrayList<StoreItemInfo>();
		Map<Integer, StoreItemInfo> roleMap = null;
		
		if (roleInfo.getRoleLoadInfo() != null) {
			roleMap = roleInfo.getRoleLoadInfo().getStoreItemInfoMap();
		}
		
		if (roleMap != null) {
			for (StoreItemInfo info : roleMap.values()) {
				if (info.getStoreType() == storeType) {
					list.add(info);
				}
			}
		}
		return list;
	}

	public static void refreshStoreItem(RoleInfo roleInfo, int storeType, List<StoreItemInfo> list) {
		Map<Integer, StoreItemInfo> roleMap = null;
		
		if (roleInfo.getRoleLoadInfo() != null) {
			roleMap = roleInfo.getRoleLoadInfo().getStoreItemInfoMap();
		}
		
		if (roleMap != null) {
			List<StoreItemInfo> old = getStoreItemList(roleInfo, storeType);
			if (old != null) {
				for (StoreItemInfo info : old) {
					if (info.getStoreType() == storeType) {
						roleMap.remove(info.getId());
					}
				}
			}
		}
		for (StoreItemInfo info : list) {
			addStoreItemInfo(roleInfo.getRoleLoadInfo(), info);
		}
	}

}
