package com.snail.webgame.game.cache;

import java.util.Map;

import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

/**
 * 道具
 * 
 * @author zhangyq
 * 
 */
public class BagItemMap {

	/**
	 * 添加道具
	 * 
	 * @param bagItem
	 */
	public static void addBagItem(RoleInfo roleInfo, BagItemInfo bagItemInfo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			roleInfo.getRoleLoadInfo().getBagItemMap().put(bagItemInfo.getId(), bagItemInfo);
			SnatchPatchMap.addRivalInfo(bagItemInfo.getItemNo(), roleInfo.getId());
		}
	}

	/**
	 * 从背包中去除某道具
	 * 
	 * @param bagId
	 * @param itemNo
	 * @return
	 */
	public static void removeBagItem(RoleInfo roleInfo, int itemId) {
		BagItemInfo item = getBagItemById(roleInfo, itemId);
		if (item != null) {
			roleInfo.getRoleLoadInfo().getBagItemMap().remove(itemId);
			SnatchPatchMap.removeRivalInfo(item.getItemNo(), roleInfo.getId());
		}
	}

	/**
	 * 获取背包物品数量
	 * 
	 * @param bagId
	 * @return
	 */
	public static int getBagItemNum(RoleInfo roleInfo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			int size = roleLoadInfo.getBagEquipMap().size() + roleLoadInfo.getBagItemMap().size();
			return size;
		}

		return 0;
	}

	/**
	 * 检测背包中中是否有某道具
	 * 
	 * @param bagId
	 * @param itemNo
	 * @return
	 */
	public static boolean checkBagItemNum(RoleInfo roleInfo, int itemNo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			Map<Integer, BagItemInfo> map = roleInfo.getRoleLoadInfo().getBagItemMap();

			for (BagItemInfo bagItem : map.values()) {
				if (bagItem.getItemNo() == itemNo) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 检测背包中中道具是否足够
	 * 
	 * @param bagId
	 * @param itemNo
	 * @param itemNum
	 * @return
	 */
	public static boolean checkBagItemNum(RoleInfo roleInfo, int itemNo, int itemNum) {
		if (roleInfo.getRoleLoadInfo() != null) {
			Map<Integer, BagItemInfo> map = roleInfo.getRoleLoadInfo().getBagItemMap();

			for (BagItemInfo bagItem : map.values()) {
				if (bagItem.getItemNo() == itemNo && bagItem.getNum() >= itemNum) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 检测背包中某道具(返回道具)
	 * 
	 * @param bagId
	 * @param itemNo
	 * @return
	 */
	public static BagItemInfo checkBagItem(RoleInfo roleInfo, int itemNo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			Map<Integer, BagItemInfo> map = roleInfo.getRoleLoadInfo().getBagItemMap();

			for (BagItemInfo bagItem : map.values()) {
				if (bagItem.getItemNo() == itemNo) {
					return bagItem;
				}
			}
		}

		return null;
	}

	/**
	 * 获取背包物品
	 * 
	 * @param bagId
	 * @return
	 */
	public static Map<Integer, BagItemInfo> getBagItem(RoleInfo roleInfo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			return roleInfo.getRoleLoadInfo().getBagItemMap();
		}

		return null;
	}

	/**
	 * 获取背包物品
	 * 
	 * @param bagId
	 * @param itemId
	 * @return
	 */
	public static BagItemInfo getBagItemById(RoleInfo roleInfo, int itemId) {
		if (roleInfo.getRoleLoadInfo() != null) {
			return roleInfo.getRoleLoadInfo().getBagItemMap().get(itemId);
		}

		return null;
	}

	/**
	 * 获取背包物品
	 * 
	 * @param itemId
	 * @return
	 */
	public static BagItemInfo getBagItembyNo(RoleInfo roleInfo, int itemNo) {
		if (roleInfo.getRoleLoadInfo() != null) {
			Map<Integer, BagItemInfo> map = roleInfo.getRoleLoadInfo().getBagItemMap();

			for (BagItemInfo bagItem : map.values()) {
				if (bagItem.getItemNo() == itemNo) {
					return bagItem;
				}
			}
		}

		return null;
	}

}
