package com.snail.webgame.game.protocal.store.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.StoreItemInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.StoreItemDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.StoreItemInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.ShopXMLInfoMap;
import com.snail.webgame.game.xml.info.ShopXMLInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopItem;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopRoleLevels;

public class StoreService {

	/**
	 * 自动处理刷新商店物品信息
	 * @param roleInfo
	 * @param roleLoadInfo
	 * @param storeType
	 * @return
	 */
	public static int autoRefreshStoreItem(RoleInfo roleInfo, RoleLoadInfo roleLoadInfo, int storeType) {
		long now = System.currentTimeMillis();
		Timestamp lastRefreshTime = new Timestamp(now);
		ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(storeType);
		if (xmlInfo == null) {
			return ErrorCode.STORE_ERROR_1;
		}
		String autoRefreshTime = xmlInfo.getAutoRefreshTime();
		
		if(autoRefreshTime == null || "".equals(autoRefreshTime)){
			//商城不支持刷新
			Map<Integer, StoreItemInfo> roleMap = StoreItemInfoMap.getRoleMap(roleInfo);
			boolean hasItems = false; //是否有物品
			if (roleMap != null) {
				for (StoreItemInfo item : roleMap.values()) {
					if (item.getStoreType() == storeType) {
						hasItems = true;
						break;
					}
				}
			}
			
			if(!hasItems){
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}
				
				if (!StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					return ErrorCode.STORE_ERROR_3;
				}
				
				StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				
			}
			
			return 1;
		}
		
		long todayRefreshTime = DateUtil.getTodayHMTime(autoRefreshTime);// HH：mm
		switch (storeType) {
		case StoreItemInfo.STORE_TYPE_1:
			if (roleLoadInfo.getLastCourageTime() == null
					|| roleLoadInfo.getLastCourageTime().getTime() == (new Timestamp(0)).getTime()
					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getLastCourageTime().getTime()) //
					|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getLastCourageTime().getTime())) //昨天刷新时间点未上线，今天上线后也可刷新
			{
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}
				// 更新竞技场商店最后更新时间
				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setLastCourageTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}

			break;
		case StoreItemInfo.STORE_TYPE_2:
			if (roleLoadInfo.getLastJusticeTime() == null
					|| roleLoadInfo.getLastJusticeTime().getTime() == (new Timestamp(0)).getTime()
					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getLastJusticeTime().getTime())
					|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getLastJusticeTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setLastJusticeTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_3:
			if (roleLoadInfo.getLastDevoteTime() == null// 新玩家
					|| roleLoadInfo.getLastDevoteTime().getTime() == (new Timestamp(0)).getTime()
					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getLastDevoteTime().getTime())
					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000 > roleLoadInfo.getLastDevoteTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setLastDevoteTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_4:
			String[] refreshStr = autoRefreshTime.split(",");
			long[] refreshTimes = new long[refreshStr.length];
			int num = 0;
			for (int i = 0; i < refreshStr.length; i++) {
				refreshTimes[i] = DateUtil.getTodayHMTime(refreshStr[i]);
				if (now >= DateUtil.getTodayHMTime(refreshStr[i])) {
					num++;
				}
			}

			if (roleLoadInfo.getLastNormalTime() == null // 新玩家
					|| (roleLoadInfo.getAutoRefreNum() < num && DateUtil.isSameDay(now, roleLoadInfo
							.getLastNormalTime().getTime())) // 当天刷新未满
					|| (roleLoadInfo.getAutoRefreNum() < refreshTimes.length && !DateUtil.isSameDay(now, roleLoadInfo
							.getLastNormalTime().getTime())) // 当天刷新未满,到第二天
					|| (roleLoadInfo.getAutoRefreNum() == refreshTimes.length && num >= 1 && !DateUtil.isSameDay(now,
							roleLoadInfo.getLastNormalTime().getTime())))// 当天刷新已满,到第二天
			{
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, num, lastRefreshTime)) {
					roleLoadInfo.setLastNormalTime(lastRefreshTime);
					roleLoadInfo.setAutoRefreNum(num);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_5:
			if (roleLoadInfo.getKuafuAutoTime() == null
					|| roleLoadInfo.getKuafuAutoTime().getTime() == (new Timestamp(0)).getTime()
					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getKuafuAutoTime().getTime())
					|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getKuafuAutoTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setKuafuAutoTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_6:
			if (roleLoadInfo.getExploitAutoTime() == null
					|| roleLoadInfo.getExploitAutoTime().getTime() == (new Timestamp(0)).getTime()
					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getExploitAutoTime().getTime())
					|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getExploitAutoTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setExploitAutoTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_7:
			if (roleLoadInfo.getIsBuyGoldShop() == 1	||roleLoadInfo.getGoldShopAutoTime() == null
					|| roleLoadInfo.getGoldShopAutoTime().getTime() == (new Timestamp(0)).getTime()
					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getGoldShopAutoTime().getTime()) 
					|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getGoldShopAutoTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setGoldShopAutoTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_8:
			String[] refreshTimeStr = autoRefreshTime.split(",");
			long[] refreshSplitTimes = new long[refreshTimeStr.length];
			int index = 0;
			for (int i = 0; i < refreshTimeStr.length; i++) {
				refreshSplitTimes[i] = DateUtil.getTodayHMTime(refreshTimeStr[i]);
				if (now >= DateUtil.getTodayHMTime(refreshTimeStr[i])) {
					index++;
				}
			}
			
			if (roleLoadInfo.getEquipShopAutoTime() == null // 新玩家
					|| (roleLoadInfo.getAutoRefreEquipShopNum() < index && DateUtil.isSameDay(now, roleLoadInfo
							.getEquipShopAutoTime().getTime())) // 当天刷新未满
					|| (roleLoadInfo.getAutoRefreEquipShopNum() < refreshSplitTimes.length && !DateUtil.isSameDay(now, roleLoadInfo
							.getEquipShopAutoTime().getTime())) // 当天刷新未满,到第二天
					|| (roleLoadInfo.getAutoRefreEquipShopNum() == refreshSplitTimes.length && index >= 1 && !DateUtil.isSameDay(now,
							roleLoadInfo.getEquipShopAutoTime().getTime())))// 当天刷新已满,到第二天
			{
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, index, lastRefreshTime)) {
					roleLoadInfo.setEquipShopAutoTime(lastRefreshTime);
					roleLoadInfo.setAutoRefreEquipShopNum(index);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_9: //异域商店
			if (roleLoadInfo.getIsBuyTurkShop() == 1||roleLoadInfo.getTurkShopAutoTime() == null
				|| roleLoadInfo.getTurkShopAutoTime().getTime() == (new Timestamp(0)).getTime()
				|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getTurkShopAutoTime().getTime()) 
				|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getTurkShopAutoTime().getTime())) {
					List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
					if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}
		
				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setTurkShopAutoTime(lastRefreshTime);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_10:
			
			if (roleLoadInfo.getPvp3ShopAutoTime() == null
			|| roleLoadInfo.getPvp3ShopAutoTime().getTime() == (new Timestamp(0)).getTime()
			|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getPvp3ShopAutoTime().getTime()) 
			|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getPvp3ShopAutoTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setPvp3ShopAutoTime(lastRefreshTime);
					roleLoadInfo.setPvp3ShopAutoRefreNum(0);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_11:
			String[] refreshTime = autoRefreshTime.split(",");
			long[] refreshSplitTime = new long[refreshTime.length];
			int count = 0;
			for (int i = 0; i < refreshTime.length; i++) {
				refreshSplitTime[i] = DateUtil.getTodayHMTime(refreshTime[i]);
				if (now >= DateUtil.getTodayHMTime(refreshTime[i])) {
					count++;
				}
			}
			
			if (roleLoadInfo.getStarShopAutoTime() == null // 新玩家
					|| (roleLoadInfo.getAutoRefreStarShopNum() < count && DateUtil.isSameDay(now, roleLoadInfo
							.getStarShopAutoTime().getTime())) // 当天刷新未满
					|| (roleLoadInfo.getAutoRefreStarShopNum() < refreshSplitTime.length && !DateUtil.isSameDay(now, roleLoadInfo
							.getStarShopAutoTime().getTime())) // 当天刷新未满,到第二天
					|| (roleLoadInfo.getAutoRefreStarShopNum() == refreshSplitTime.length && count >= 1 && !DateUtil.isSameDay(now,
							roleLoadInfo.getStarShopAutoTime().getTime())))// 当天刷新已满,到第二天
			{
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, count, lastRefreshTime)) {
					roleLoadInfo.setStarShopAutoTime(lastRefreshTime);
					roleLoadInfo.setAutoRefreStarShopNum(count);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;
		case StoreItemInfo.STORE_TYPE_12:
			
			if (roleLoadInfo.getTeamShopAutoTime() == null
			|| roleLoadInfo.getTeamShopAutoTime().getTime() == (new Timestamp(0)).getTime()
			|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getTeamShopAutoTime().getTime()) 
			|| (now < todayRefreshTime && todayRefreshTime-24*3600*1000l > roleLoadInfo.getTeamShopAutoTime().getTime())) {
				List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
				if (list == null) {
					return ErrorCode.STORE_ERROR_4;
				}

				if (RoleDAO.getInstance().updateLastRefreshTime(roleInfo.getId(), storeType, 0, lastRefreshTime)) {
					roleLoadInfo.setTeamShopAutoTime(lastRefreshTime);
					roleLoadInfo.setTeamShopAutoRefreNum(0);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
				// 刷新商店物品
				if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
					StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
				} else {
					return ErrorCode.STORE_ERROR_3;
				}
			}
			break;

		default:
			return ErrorCode.STORE_ERROR_1;
		}
		return 1;
	}

	/**
	 * 刷新商店物品信息
	 * @param roleInfo
	 * @param storeType
	 * @return
	 */
	public static int refreshStoreItem(RoleInfo roleInfo, int storeType) {

		List<StoreItemInfo> list = getStoreItembyXML(roleInfo, storeType);
		if (list == null) {
			return ErrorCode.STORE_ERROR_14;
		}

		if (StoreItemDAO.getInstance().refreshRoleStoreItem(roleInfo.getId(), storeType, list)) {
			StoreItemInfoMap.refreshStoreItem(roleInfo, storeType, list);
		} else {
			return ErrorCode.STORE_ERROR_15;
		}
		return 1;
	}

	/**
	 * 根据XML配置获取物品信息
	 * @param roleInfo
	 * @param storeType
	 * @param list
	 * @return
	 */
	private static List<StoreItemInfo> getStoreItembyXML(RoleInfo roleInfo, int storeType) {
		List<StoreItemInfo> list = new ArrayList<StoreItemInfo>();
		ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(storeType);
		if (xmlInfo == null) {
			return null;
		}
		ShopRoleLevels roleLevels = null;
		List<ShopRoleLevels> roleLvList = xmlInfo.getRoleLvList();
		if (roleLvList != null) {
			for (ShopRoleLevels shopRolelv : roleLvList) {
				if (shopRolelv.getMinRoleLv() <= HeroInfoMap.getMainHeroLv(roleInfo.getId())
						&& shopRolelv.getMaxRoleLv() >= HeroInfoMap.getMainHeroLv(roleInfo.getId())) {
					roleLevels = shopRolelv;
					break;
				}
			}
		}
		if (roleLevels == null) {
			return null;
		}

		Map<Integer, List<ShopItem>> shopItemMap = roleLevels.getItems();
		if (shopItemMap == null) {
			return null;
		}
		for (int position : shopItemMap.keySet()) {
			List<ShopItem> items = shopItemMap.get(position);
			if (items == null) {
				return null;
			}
			ShopItem item = getShopItembyRand(items);
			if (item == null) {
				return null;
			}
			StoreItemInfo storeItem = new StoreItemInfo();
			storeItem.setRoleId(roleInfo.getId());
			storeItem.setStoreType(storeType);
			storeItem.setPosition(position);
			storeItem.setItemNo(item.getItemNo());
			storeItem.setItemNum(item.getItemNum());
			storeItem.setBuyNum(0);
			storeItem.setCostType(item.getCostType());
			storeItem.setCostNum(item.getCost());
			storeItem.setCondition(item.getCondition());
			list.add(storeItem);
		}
		return list;
	}

	/**
	 * 根据概率获取值
	 * @param items
	 * @return
	 */
	private static ShopItem getShopItembyRand(List<ShopItem> items) {
		if (items != null) {
			int sumRand = 0;
			for (ShopItem item : items) {
				item.setMinRand(sumRand);
				item.setMaxRand(sumRand + item.getRand());
				sumRand = item.getMaxRand();
			}
			int rand = RandomUtil.getRandom(0, sumRand);
			for (ShopItem item : items) {
				if (rand >= item.getMinRand() && rand <= item.getMaxRand()) {
					int itemNo = item.getItemNo();
					if (String.valueOf(itemNo).startsWith(GameValue.PROP_N0)
							|| String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)
							|| String.valueOf(itemNo).startsWith(GameValue.WEAPAN_NO)) {
						return item;
					}
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 获取当日手动刷新次数
	 * @param roleInfo
	 * @param roleLoadInfo
	 * @param storeType
	 * @return
	 */
	public static int getBuyNum(RoleInfo roleInfo, RoleLoadInfo roleLoadInfo, int storeType) {
		int buyNum = 0;
		long now = System.currentTimeMillis();
		switch (storeType) {
		case StoreItemInfo.STORE_TYPE_1:
			if (roleLoadInfo.getLastBuyCourageTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyCourageTime().getTime())) {
				buyNum = roleLoadInfo.getBuyCourageNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_2:
			if (roleLoadInfo.getLastBuyJusticeTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyJusticeTime().getTime())) {
				buyNum = roleLoadInfo.getBuyJusticeNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_3:
			if (roleLoadInfo.getLastBuyDevoteTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyDevoteTime().getTime())) {
				buyNum = roleLoadInfo.getBuyDevoteNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_4:
			if (roleLoadInfo.getLastBuyNormalTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyNormalTime().getTime())) {
				buyNum = roleLoadInfo.getBuyNormalNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_5:
			if (roleLoadInfo.getLastBuyKuafuTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyKuafuTime().getTime())) {
				buyNum = roleLoadInfo.getBuyKuafuNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_6:
			if (roleLoadInfo.getLastBuyExploitTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyExploitTime().getTime())) {
				buyNum = roleLoadInfo.getBuyExploitShopNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_7:
			if (roleLoadInfo.getLastBuyGoldShopTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyGoldShopTime().getTime())) {
				buyNum = roleLoadInfo.getBuyGoldShopNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_8:
			if (roleLoadInfo.getLastBuyEquipTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyEquipTime().getTime())) {
				buyNum = roleLoadInfo.getBuyEquipShopNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_9:
			if (roleLoadInfo.getLastBuyTurkShopTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyTurkShopTime().getTime())) {
				buyNum = roleLoadInfo.getBuyTurkShopNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_10:
			if (roleLoadInfo.getPvp3ShopLastBuyTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getPvp3ShopLastBuyTime().getTime())) {
				buyNum = roleLoadInfo.getPvp3ShopBuyNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_11:
			if (roleLoadInfo.getLastBuyStarTime() != null
					&& DateUtil.isSameDay(now, roleLoadInfo.getLastBuyStarTime().getTime())) {
				buyNum = roleLoadInfo.getBuyStarShopNum();
			}
			break;
		case StoreItemInfo.STORE_TYPE_12:
			if (roleLoadInfo.getTeamShopLastBuyTime() != null
			&& DateUtil.isSameDay(now, roleLoadInfo.getTeamShopLastBuyTime().getTime())) {
				buyNum = roleLoadInfo.getTeamShopBuyNum();
			}
			break;
		default:
			break;
		}
		return buyNum;
	}

	/**
	 * 更新当日手动刷新次数
	 * @param roleInfo
	 * @param storeType
	 * @param currBuyNum
	 * @return
	 */
	public static int updateBuyNum(RoleInfo roleInfo, int storeType, int currBuyNum) {
		ConditionType resource_type = null;
		switch (storeType) {
		case StoreItemInfo.STORE_TYPE_1:
			resource_type = ConditionType.TYPE_COURAGE;
			break;
		case StoreItemInfo.STORE_TYPE_2:
			resource_type = ConditionType.TYPE_JUSTICE;
			break;
		case StoreItemInfo.STORE_TYPE_3:
//			resource_type = ConditionType.TYPE_DEVOTE;
			resource_type = ConditionType.TYPE_CLUB_CONTRIBUTION;
			break;
		case StoreItemInfo.STORE_TYPE_4:
			resource_type = ConditionType.TYPE_NORMAL_SHOP_FRE;
			break;
		case StoreItemInfo.STORE_TYPE_5:
			resource_type = ConditionType.TYPE_KUAFU_SHOP_FRE;
			break;
		case StoreItemInfo.STORE_TYPE_6:
			resource_type = ConditionType.TYPE_EXPLOIT_SHOP_FRE;
			break;
		case StoreItemInfo.STORE_TYPE_7:
			resource_type = ConditionType.TYPE_GOLD_SHOP_REF;
			break;
		case StoreItemInfo.STORE_TYPE_8:
			resource_type = ConditionType.TYPE_EQUIP;
			break;
		case StoreItemInfo.STORE_TYPE_9:
			resource_type = ConditionType.TYPE_TURK_SHOP_REF;
			break;
		case StoreItemInfo.STORE_TYPE_10:
			resource_type = ConditionType.TYPE_PVP_3_SHOP_REF;
			break;
		case StoreItemInfo.STORE_TYPE_11:
			resource_type = ConditionType.TYPE_STAR_MONEY;
			break;
		case StoreItemInfo.STORE_TYPE_12:
			resource_type = ConditionType.TYPE_TEAM_SHOP_FRE;
			break;

		default:
			return ErrorCode.STORE_ERROR_7;
		}

		Timestamp buyTime = new Timestamp(System.currentTimeMillis());
		return RoleService.updateRoleResourceBuyNum(roleInfo, resource_type, currBuyNum, buyTime);
	}
	
	/**
	 * 是否刷新商店
	 * 1-黑市商店 2-异域商店
	 * @return
	 */
	public static boolean refreshShop(int flag)
	{
		int rand = RandomUtil.getRandom(1, 100);
		
		if(flag == 1) //黑市商店
		{
			if(rand <= GameValue.GOLE_SHOP_RANDOM)
			{
				return true;
			}
		}
		else if(flag == 2) //异域商店
		{
			if(rand <= GameValue.TURK_SHOP_RANDOM)
			{
				return true;
			}
		}
		return false;
	}
	
}
