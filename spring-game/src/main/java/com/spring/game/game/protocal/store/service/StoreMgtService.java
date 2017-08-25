package com.snail.webgame.game.protocal.store.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.StoreItemInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.ContributionCond;
import com.snail.webgame.game.condtion.conds.CourageCond;
import com.snail.webgame.game.condtion.conds.EquipCond;
import com.snail.webgame.game.condtion.conds.ExploitCond;
import com.snail.webgame.game.condtion.conds.JusticeCond;
import com.snail.webgame.game.condtion.conds.KuafuMoneyCond;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.condtion.conds.Pvp3MoneyCond;
import com.snail.webgame.game.condtion.conds.StarMoneyCond;
import com.snail.webgame.game.condtion.conds.TeamMoneyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.StoreItemDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.StoreItemInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.shizhuang.service.ShizhuangService;
import com.snail.webgame.game.protocal.store.buy.BuyReq;
import com.snail.webgame.game.protocal.store.buy.BuyResp;
import com.snail.webgame.game.protocal.store.buyShop.BuyShopReq;
import com.snail.webgame.game.protocal.store.buyShop.BuyShopResp;
import com.snail.webgame.game.protocal.store.query.QueryStoreReq;
import com.snail.webgame.game.protocal.store.query.QueryStoreResp;
import com.snail.webgame.game.protocal.store.query.StoreItemRe;
import com.snail.webgame.game.protocal.store.queryShop.QueryShopResp;
import com.snail.webgame.game.protocal.store.refresh.RefreshStoreReq;
import com.snail.webgame.game.protocal.store.refresh.RefreshStoreResp;
import com.snail.webgame.game.xml.cache.GuildConstructionXmlInfoMap;
import com.snail.webgame.game.xml.cache.GuildUpgradeXmlInfoMap;
import com.snail.webgame.game.xml.cache.ShopXMLInfoMap;
import com.snail.webgame.game.xml.info.GuildShopXmlInfo;
import com.snail.webgame.game.xml.info.GuildUpgradeXmlInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopRefresh;

public class StoreMgtService {

	private StoreItemDAO storeItemDAO = StoreItemDAO.getInstance();

	/**
	 * 查询商店物品信息
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryStoreResp queryStore(int roleId, QueryStoreReq req) {
		QueryStoreResp resp = new QueryStoreResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_18);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_18);
			return resp;
		}
		synchronized (roleInfo) {
			long now = System.currentTimeMillis();
			byte storeType = req.getStoreType();
			ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(storeType);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.STORE_ERROR_2);
				return resp;
			}
			String refreshTimeStr = xmlInfo.getAutoRefreshTime();
			String currencyType = xmlInfo.getCostType();
			long currencyNum = 0;
			if (currencyType.equals(ConditionType.TYPE_MONEY.getName())) {
				currencyNum = roleInfo.getMoney();
			} else if (currencyType.equals(ConditionType.TYPE_COURAGE.getName())) {
				currencyNum = roleLoadInfo.getCourage();
			} else if (currencyType.equals(ConditionType.TYPE_JUSTICE.getName())) {
				currencyNum = roleLoadInfo.getJustice();
//			} else if (currencyType.equals(ConditionType.TYPE_DEVOTE.getName())) {
//				currencyNum = roleLoadInfo.getDevotePoint();
			} else if (currencyType.equals(ConditionType.TYPE_KUAFU_MONEY.getName())) {
				currencyNum = roleLoadInfo.getKuafuMoney();
			} else if (currencyType.equals(ConditionType.TYPE_TEAM_MONEY.getName())) {
				currencyNum = roleLoadInfo.getTeamMoney();
			} else if (currencyType.equals(ConditionType.TYPE_EXPLOIT.getName())) {
				currencyNum = roleLoadInfo.getExploit();
			} else if (currencyType.equals(ConditionType.TYPE_COIN.getName())) {
				currencyNum = roleInfo.getCoin();
			} else if (currencyType.equals(ConditionType.TYPE_EQUIP.getName())) {
				currencyNum = roleLoadInfo.getEquip();
			} else if (currencyType.equals(ConditionType.TYPE_PVP_3_MONEY.getName())) {
				currencyNum = roleLoadInfo.getPvp3Money();
			} else if (currencyType.equals(ConditionType.TYPE_CLUB_CONTRIBUTION.getName())) {
				currencyNum = roleInfo.getClubContribution();
			} else if (currencyType.equals(ConditionType.TYPE_STAR_MONEY.getName())) {
				currencyNum = roleLoadInfo.getStarMoney();
			} else {
				resp.setResult(ErrorCode.STORE_ERROR_1);
				return resp;
			}

			// 自动处理刷新商店物品信息
			int result = StoreService.autoRefreshStoreItem(roleInfo, roleLoadInfo, storeType);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			List<StoreItemRe> storeList = new ArrayList<StoreItemRe>();
			Map<Integer, StoreItemInfo> roleMap = StoreItemInfoMap.getRoleMap(roleInfo);
			if (roleMap != null) {
				for (StoreItemInfo item : roleMap.values()) {
					if (item.getStoreType() == storeType) {
						storeList.add(getStoreItemRe(item));
					}
				}
			}
			
			
			if(storeType == StoreItemInfo.STORE_TYPE_3){
				//公会商店 过滤数据
				RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
				if(roleClubInfo == null){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_54);
					return resp;
				}
				
				
				int displayLv = roleClubInfo.getLevel() + 1; //显示比公会多一级
				
				GuildUpgradeXmlInfo upgradeXmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(displayLv);
				if(upgradeXmlInfo != null){
					//没有满级 过滤掉数据
					StoreItemRe storeItemRe = null;
					GuildShopXmlInfo guildShopXmlInfo = null;
					for(int i = 0; i < storeList.size(); i++){
						storeItemRe = storeList.get(i);
						if(storeItemRe == null){
							continue;
						}
						
						guildShopXmlInfo = GuildConstructionXmlInfoMap.getGuildShopXmlInfo(storeItemRe.getPosition());
						if(guildShopXmlInfo == null){
							continue;
						}
						
						if(guildShopXmlInfo.getLv() > displayLv){
							storeList.remove(i);
							i--;
						}
						
					}
				}
			}
			
			resp.setList(storeList);
			resp.setResult(1);
			resp.setStoreType(storeType);
			resp.setCurrencyType((byte) ConditionType.attrParseName(currencyType).getType());
			resp.setCurrencyNum(currencyNum);
			long refreshTime = 0;
			if (storeType == StoreItemInfo.STORE_TYPE_4 || storeType == StoreItemInfo.STORE_TYPE_8 
					|| storeType == StoreItemInfo.STORE_TYPE_10 || storeType == StoreItemInfo.STORE_TYPE_11
					|| storeType == StoreItemInfo.STORE_TYPE_12) {
				// 普通商店间隔时间刷新
				String[] refreshStr = refreshTimeStr.split(",");
				long[] refreshTimes = new long[refreshStr.length];

				boolean todayRefreTime = false;
				refreshTime = DateUtil.getTomorrowHMTime(refreshStr[0]);
				// 检测当天刷新时间是否完
				for (int i = 0; i < refreshStr.length; i++) {
					refreshTimes[i] = DateUtil.getTodayHMTime(refreshStr[i]);
					if (now < DateUtil.getTodayHMTime(refreshStr[i])
							&& refreshTime > DateUtil.getTodayHMTime(refreshStr[i])) {
						refreshTime = DateUtil.getTodayHMTime(refreshStr[i]);
						todayRefreTime = true;
					}
				}

				if (!todayRefreTime) {
					// 当天刷新时间已结束,取第二天最近的刷新时间
					for (int i = 1; i < refreshStr.length; i++) {
						if (refreshTime > DateUtil.getTomorrowHMTime(refreshStr[i])) {
							refreshTime = DateUtil.getTomorrowHMTime(refreshStr[i]);
						}
					}
				}
			}
			else if(storeType == StoreItemInfo.STORE_TYPE_7)
			{
				if(roleLoadInfo.getIsRefreshGoldShop() == 1)
				{
					Timestamp leaveTime = roleLoadInfo.getRefreshGoldShopTime();
					if(leaveTime != null && now < leaveTime.getTime())
					{
						resp.setLeaveTime(leaveTime.getTime());
					}
					else
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 2,null))
						{
							roleLoadInfo.setIsRefreshGoldShop(0);
							roleLoadInfo.setRefreshGoldShopTime(null);
						}
					}
				}else if(roleLoadInfo.getIsBuyGoldShopForVip()==1){
					resp.setLeaveTime(0);
					String[] refreshStr = refreshTimeStr.split(",");
					long[] refreshTimes = new long[refreshStr.length];

					boolean todayRefreTime = false;
					refreshTime = DateUtil.getTomorrowHMTime(refreshStr[0]);
					// 检测当天刷新时间是否完
					for (int i = 0; i < refreshStr.length; i++) {
						refreshTimes[i] = DateUtil.getTodayHMTime(refreshStr[i]);
						if (now < DateUtil.getTodayHMTime(refreshStr[i])
								&& refreshTime > DateUtil.getTodayHMTime(refreshStr[i])) {
							refreshTime = DateUtil.getTodayHMTime(refreshStr[i]);
							todayRefreTime = true;
						}
					}

					if (!todayRefreTime) {
						// 当天刷新时间已结束,取第二天最近的刷新时间
						for (int i = 1; i < refreshStr.length; i++) {
							if (refreshTime > DateUtil.getTomorrowHMTime(refreshStr[i])) {
								refreshTime = DateUtil.getTomorrowHMTime(refreshStr[i]);
							}
						}
					}
				}
			}
			else if(storeType == StoreItemInfo.STORE_TYPE_9)
			{
				if(roleLoadInfo.getIsRefreshTurkShop() == 1)
				{
					Timestamp leaveTime = roleLoadInfo.getRefreshTurkShopTime();
					if(leaveTime != null && now < leaveTime.getTime())
					{
						resp.setLeaveTime(leaveTime.getTime());
					}
					else
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 4,null))
						{
							roleLoadInfo.setIsRefreshTurkShop(0);
							roleLoadInfo.setRefreshTurkShopTime(null);
						}
					}
				}else if(roleLoadInfo.getIsBuyTurkShopForVip()==1){
					resp.setLeaveTime(0);
					String[] refreshStr = refreshTimeStr.split(",");
					long[] refreshTimes = new long[refreshStr.length];

					boolean todayRefreTime = false;
					refreshTime = DateUtil.getTomorrowHMTime(refreshStr[0]);
					// 检测当天刷新时间是否完
					for (int i = 0; i < refreshStr.length; i++) {
						refreshTimes[i] = DateUtil.getTodayHMTime(refreshStr[i]);
						if (now < DateUtil.getTodayHMTime(refreshStr[i])
								&& refreshTime > DateUtil.getTodayHMTime(refreshStr[i])) {
							refreshTime = DateUtil.getTodayHMTime(refreshStr[i]);
							todayRefreTime = true;
						}
					}

					if (!todayRefreTime) {
						// 当天刷新时间已结束,取第二天最近的刷新时间
						for (int i = 1; i < refreshStr.length; i++) {
							if (refreshTime > DateUtil.getTomorrowHMTime(refreshStr[i])) {
								refreshTime = DateUtil.getTomorrowHMTime(refreshStr[i]);
							}
						}
					}
				}
			}
			else 
			{
				refreshTime = DateUtil.getTodayHMTime(refreshTimeStr);
				if (refreshTime <= now) {
					refreshTime = DateUtil.getTomorrowHMTime(refreshTimeStr);
				}
			}
			resp.setRefreshTime(refreshTime);
			resp.setBuyNum(StoreService.getBuyNum(roleInfo, roleLoadInfo, storeType));
			resp.setCount(resp.getList().size());
			
			return resp;
		}
	}

	/**
	 * 获取 StoreItemRe
	 * @param item
	 * @return
	 */
	private StoreItemRe getStoreItemRe(StoreItemInfo item) {
		StoreItemRe re = new StoreItemRe();
		re.setId((int) item.getId());
		re.setRoleId((int) item.getRoleId());
		re.setItemNo(item.getItemNo());
		re.setItemNum(item.getItemNum());
		re.setBuyNum(item.getBuyNum());
		re.setCostType((byte) ConditionType.attrParseName(item.getCostType()).getType());
		re.setCostNum(item.getCostNum());
		re.setPosition(item.getPosition());
		
		return re;
	}

	/**
	 * 手动刷新商店物品信息
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RefreshStoreResp refreshStore(int roleId, RefreshStoreReq req) {
		RefreshStoreResp resp = new RefreshStoreResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.STORE_ERROR_5);
			return resp;
		}

		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.STORE_ERROR_5);
			return resp;
		}
		synchronized (roleInfo) {
			int storeType = req.getStoreType();
			long now = System.currentTimeMillis();
			if(storeType == StoreItemInfo.STORE_TYPE_7)
			{
				if(roleLoadInfo.getIsRefreshGoldShop() != 1 && roleLoadInfo.getIsBuyGoldShopForVip() == 0)
				{
					//无法刷新
					resp.setResult(ErrorCode.STORE_ERROR_18);
					return resp;
				}
				else if(roleLoadInfo.getIsRefreshGoldShop() == 1)
				{
					if(roleLoadInfo.getRefreshGoldShopTime()!=null && now > roleLoadInfo.getRefreshGoldShopTime().getTime())
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 2,null))
						{
							roleLoadInfo.setIsRefreshGoldShop(0);
							roleLoadInfo.setRefreshGoldShopTime(null);
						}
						//无法刷新
						resp.setResult(ErrorCode.STORE_ERROR_18);
						return resp;
					}
				}
			}
			if(storeType == StoreItemInfo.STORE_TYPE_9)
			{
				if(roleLoadInfo.getIsRefreshTurkShop() != 1 && roleLoadInfo.getIsBuyTurkShopForVip()==0)
				{
					//无法刷新
					resp.setResult(ErrorCode.STORE_ERROR_18);
					return resp;
				}
				else if(roleLoadInfo.getIsRefreshTurkShop() == 1)
				{
					if(roleLoadInfo.getRefreshGoldShopTime()!=null && now > roleLoadInfo.getRefreshGoldShopTime().getTime())
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 4,null))
						{
							roleLoadInfo.setIsRefreshTurkShop(0);
							roleLoadInfo.setRefreshTurkShopTime(null);
						}
						//无法刷新
						resp.setResult(ErrorCode.STORE_ERROR_18);
						return resp;
					}
				}
			}
			
			ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(storeType);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.STORE_ERROR_6);
				return resp;
			}
			
			if(xmlInfo.getAutoRefreshTime() == null || "".equals(xmlInfo.getAutoRefreshTime())){
				//没有刷新
				resp.setResult(1);
				return resp;
			}
			
			int buyNum = StoreService.getBuyNum(roleInfo, roleLoadInfo, storeType);
			int maxBuyNum = ShopXMLInfoMap.getMaxRefreshNum(storeType);
			int currBuyNum = buyNum + 1;
			ShopRefresh shopRefresh = null;
			if (buyNum < maxBuyNum) {
				shopRefresh = ShopXMLInfoMap.getShopRefresh(storeType, currBuyNum);
			} else {
				if (xmlInfo.getFixed() == 0) {
					resp.setResult(ErrorCode.ROLE_STORE_ERROR_1);
					return resp;
				} else {
					shopRefresh = ShopXMLInfoMap.getShopRefresh(storeType, maxBuyNum);
				}
			}
			if (shopRefresh == null) {
				resp.setResult(ErrorCode.ROLE_STORE_ERROR_1);
				return resp;
			}
			// 检测购买条件
			List<AbstractConditionCheck> conds = shopRefresh.getConditions();
			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			// 扣去消耗资源
			if (RoleService.subRoleResource(ActionType.action221.getType(), roleInfo, conds , null)) {
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null) {
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1) {
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}

				// SceneService.sendRoleRefreshMsg(roleInfo.getId(),
				// SceneService.REFESH_TYPE_ROLE, "");
			} else {
				resp.setResult(ErrorCode.STORE_ERROR_8);
				return resp;
			}

			// 更新当日手动刷新次数
			int result = StoreService.updateBuyNum(roleInfo, storeType, currBuyNum);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			//红点检测
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, 
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			if(isRed){
				RedPointMgtService.pop(roleInfo.getId());
			}
			
			// 刷新商店物品信息
			result = StoreService.refreshStoreItem(roleInfo, storeType);
			if (result == 1) {
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_STORE_ITEM, storeType);
				GameLogService.insertPlayActionLog(roleInfo, ActionType.action221.getType(), storeType+","+resp.getSourceType()+","+resp.getSourceChange());
			}
			resp.setResult(result);
			resp.setStoreType(storeType);
			return resp;
		}
	}

	/**
	 * 购买
	 * @param roleId
	 * @param req
	 * @return
	 */
	public BuyResp buyItem(int roleId, BuyReq req) {
		BuyResp resp = new BuyResp();
		
		resp.setSourceType(req.getStoreType());
		resp.setTeamChallenge(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.STORE_ERROR_9);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.STORE_ERROR_9);
			return resp;
		}
		synchronized (roleInfo) {
			byte storeType = req.getStoreType();
			int storeItemId = req.getStoreItemId();
			long now = System.currentTimeMillis();
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.STORE_ERROR_16);
				return resp;
			}
			
			ShopXMLInfo shopXMLInfo = ShopXMLInfoMap.getShopXMLInfo(storeType);
			if (shopXMLInfo == null) {
				resp.setResult(ErrorCode.STORE_ERROR_10);
				return resp;
			}
			if(storeType == StoreItemInfo.STORE_TYPE_7)
			{
				if(roleLoadInfo.getIsRefreshGoldShop() != 1 && roleLoadInfo.getIsBuyGoldShopForVip()==0)
				{
					//无法刷新
					resp.setResult(ErrorCode.ROLE_STORE_ERROR_10);
					return resp;
				}
				else if(roleLoadInfo.getIsRefreshGoldShop() == 1)
				{
					if(roleLoadInfo.getRefreshGoldShopTime()!=null && now > roleLoadInfo.getRefreshGoldShopTime().getTime())
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 2,null))
						{
							roleLoadInfo.setIsRefreshGoldShop(0);
							roleLoadInfo.setRefreshGoldShopTime(null);
						}
						//无法刷新
						resp.setResult(ErrorCode.ROLE_STORE_ERROR_10);
						return resp;
					}
				}
			}
			if(storeType == StoreItemInfo.STORE_TYPE_9)
			{
				if(roleLoadInfo.getIsRefreshTurkShop() != 1 && roleLoadInfo.getIsBuyTurkShopForVip() == 0)
				{
					//无法刷新
					resp.setResult(ErrorCode.ROLE_STORE_ERROR_10);
					return resp;
				}
				else if(roleLoadInfo.getIsRefreshTurkShop() == 1)
				{
					if(roleLoadInfo.getRefreshGoldShopTime()!=null && now > roleLoadInfo.getRefreshGoldShopTime().getTime())
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 4,null))
						{
							roleLoadInfo.setIsRefreshTurkShop(0);
							roleLoadInfo.setRefreshTurkShopTime(null);
						}
						//无法刷新
						resp.setResult(ErrorCode.ROLE_STORE_ERROR_10);
						return resp;
					}
				}
			}

			StoreItemInfo itemInfo = StoreItemInfoMap.getStoreItemInfo(roleInfo, storeType, storeItemId);
			if (itemInfo == null) {
				resp.setResult(ErrorCode.STORE_ERROR_10);
				return resp;
			}
					
			if(storeType == StoreItemInfo.STORE_TYPE_3){
				int result = clubShopCheck(roleInfo, itemInfo.getPosition());
				if(result != 1){
					resp.setResult(result);
					return resp;
				}
			}

			
			if (itemInfo.getBuyNum() >= itemInfo.getItemNum()) {
				resp.setResult(ErrorCode.ROLE_STORE_ERROR_2);
				return resp;
			}
			
			int getNum = 0;// 购买获得数量
			int cost = 0;// 消耗的资源
			int currBuyNum = 0;
			if (shopXMLInfo.getShopType() == ShopXMLInfo.SHOP_TYPE_1) {
				getNum = 1;
				cost = getNum * itemInfo.getCostNum();
				currBuyNum = getNum + itemInfo.getBuyNum();
			} else if (shopXMLInfo.getShopType() == ShopXMLInfo.SHOP_TYPE_2) {
				getNum = itemInfo.getItemNum();
				cost = itemInfo.getCostNum();
				currBuyNum = itemInfo.getItemNum();
			}
			
			BagItemInfo bagItem = BagItemMap.getBagItembyNo(roleInfo, itemInfo.getItemNo());
			if (bagItem != null && bagItem.getNum() > Integer.MAX_VALUE - getNum) {
				resp.setResult(ErrorCode.ITEM_ADD_ERROR_3);
				return resp;
			}

			// 检测购买条件
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			if (itemInfo.getCostType().equals(ConditionType.TYPE_COURAGE.getName())) {
				conds.add(new CourageCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_JUSTICE.getName())) {
				conds.add(new JusticeCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_MONEY.getName())) {
				conds.add(new MoneyCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_COIN.getName())) {
				conds.add(new CoinCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_KUAFU_MONEY.getName())) {
				conds.add(new KuafuMoneyCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_TEAM_MONEY.getName())) {
				conds.add(new TeamMoneyCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_EXPLOIT.getName())) {
				conds.add(new ExploitCond(cost));
			} else if (itemInfo.getCostType().equals(ConditionType.TYPE_EQUIP.getName())) {
				conds.add(new EquipCond(cost));
			}else if (itemInfo.getCostType().equals(ConditionType.TYPE_PVP_3_MONEY.getName())) {
				conds.add(new Pvp3MoneyCond(cost));
			}else if (itemInfo.getCostType().equals(ConditionType.TYPE_CLUB_CONTRIBUTION.getName())) {
				conds.add(new ContributionCond(cost));
//			}else if (itemInfo.getCostType().equals(ConditionType.TYPE_DEVOTE.getName())) {
//				conds.add(new DevoteCond(cost));
			}else if(itemInfo.getCostType().equals(ConditionType.TYPE_STAR_MONEY.getName())){
				conds.add(new StarMoneyCond(cost));
			}else {
				resp.setResult(ErrorCode.STORE_ERROR_11);
				return resp;
			}
			

			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}
			
			if(storeType == StoreItemInfo.STORE_TYPE_12)
			{
				if(itemInfo.getCondition() != null && !"".equalsIgnoreCase(itemInfo.getCondition()) && !"0".equalsIgnoreCase(itemInfo.getCondition()))
				{
					if(roleLoadInfo.getTeamChallengeStr() == null || roleLoadInfo.getTeamChallengeStr().indexOf(itemInfo.getCondition()) == -1)
					{
						resp.setTeamChallenge(Integer.parseInt(itemInfo.getCondition()));
						resp.setResult(ErrorCode.STORE_ERROR_30);
						return resp;
					}
				}
			}
			
			check = ShizhuangService.checkIsShizhuangItemNo(roleInfo, String.valueOf(itemInfo.getItemNo()), getNum);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			// 扣去消耗资源
			if (RoleService.subRoleResource(ActionType.action222.getType(), roleInfo, conds , new DropInfo(String.valueOf(itemInfo.getItemNo()), getNum) )) {

				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null) {
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1) {
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
				// SceneService.sendRoleRefreshMsg(roleInfo.getId(),
				// SceneService.REFESH_TYPE_ROLE, "");
			} else {
				resp.setResult(ErrorCode.STORE_ERROR_12);
				return resp;
			}

			// 更新状态
			if (storeItemDAO.updateStoreItemStatus(itemInfo.getId(), currBuyNum)) {
				itemInfo.setBuyNum(currBuyNum);
			} else {
				resp.setResult(ErrorCode.STORE_ERROR_13);
				return resp;
			}

			// 物品掉落
			List<DropInfo> itemList = new ArrayList<DropInfo>();// 道具
			itemList.add(new DropInfo(String.valueOf(itemInfo.getItemNo()), getNum));

			int result = ItemService.itemAdd(ActionType.action222.getType(), roleInfo, itemList, null, null, null, null,true);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			resp.setResult(1);
			resp.setStoreType(storeType);
			resp.setStoreItemId(storeItemId);
			resp.setBuyNum(getNum);
			
			// 红点监听
			boolean redFlag = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			if(redFlag){
				RedPointMgtService.pop(roleId);
			}
			
			//商店物品出售日志
			GameLogService.insertStoreBuyItemLog(roleInfo,storeType,itemInfo.getItemNo(),getNum,itemInfo.getCostType(),cost);
			
			return resp;
		}
	}
	
	/**
	 * 查询商店
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryShopResp queryShop(int roleId) {
		QueryShopResp resp = new QueryShopResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_18);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_18);
			return resp;
		}
		synchronized (roleInfo) 
		{
			int goldShop = 0;
			int turkShop = 0;
			long now = System.currentTimeMillis();
			Timestamp goldTime = roleLoadInfo.getRefreshGoldShopTime();
			Timestamp turkTime = roleLoadInfo.getRefreshTurkShopTime();
			int refreshGoldShop = roleLoadInfo.getIsRefreshGoldShop();
			int refreshTurkShop = roleLoadInfo.getIsRefreshTurkShop();
			
			if(refreshGoldShop == 1)
			{
				if(goldTime != null && now < goldTime.getTime())
				{
					goldShop = 1;
				}
				else
				{
					if(RoleDAO.getInstance().shop(roleId, 0, 2,null))
					{
						roleLoadInfo.setIsRefreshGoldShop(0);
						roleLoadInfo.setRefreshGoldShopTime(null);
					}
				}
			}
			if( refreshTurkShop == 1)
			{
				if(turkTime != null && now < turkTime.getTime())
				{
					turkShop = 1;
				}
				else
				{
					if(RoleDAO.getInstance().shop(roleId, 0, 4,null))
					{
						roleLoadInfo.setIsRefreshTurkShop(0);
						roleLoadInfo.setRefreshTurkShopTime(null);
					}
				}
			}
			resp.setIsBuyShop(roleLoadInfo.getIsBuyGoldShopForVip());
			resp.setIsBuyTurkShop(roleLoadInfo.getIsBuyTurkShopForVip());
			resp.setResult(1);
			resp.setGoldShop(goldShop);
			resp.setTurkShop(turkShop);
			
		}
		return resp;
	}
	/**
	 * VIP购买黑市或者异域商城永久使用权限
	 * @param roleId
	 * @param bype
	 */
	public BuyShopResp buyShopForVip(int roleId,BuyShopReq req){
		BuyShopResp resp = new BuyShopResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_18);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_18);
			return resp;
		}
		if(req.getShopType()!=1 && req.getShopType()!=2){
			resp.setResult(ErrorCode.STORE_ERROR_20);
			return resp;
		}
		synchronized (roleInfo) {
			if(req.getShopType()==1){
				if(VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.SHOP_7_OPEN)==0 || roleLoadInfo.getIsBuyGoldShopForVip()==1){
					resp.setResult(ErrorCode.STORE_ERROR_21);
					return resp;
				}else{
					// 判断钱是否足够
					if (roleInfo.getCoin() < GameValue.BUY_BLACK_SHOP_PRICE) {
						resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
						return resp;
					}
					// 扣钱
					if (!RoleService.subRoleRoleResource(ActionType.action386.getType(), roleInfo, ConditionType.TYPE_COIN,GameValue.BUY_BLACK_SHOP_PRICE , null)){
						resp.setResult(ErrorCode.STORE_ERROR_20);
						return resp;
					}
					if(RoleDAO.getInstance().shop(roleId, 1, 5,null))
					{
						roleLoadInfo.setIsBuyGoldShopForVip(1);
					}
					resp.setSourceChange(-GameValue.BUY_BLACK_SHOP_PRICE);
				}
			}else{
				if(VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.SHOP_9_OPEN)==0 ||roleLoadInfo.getIsBuyTurkShopForVip()==1){
					resp.setResult(ErrorCode.STORE_ERROR_21);
					return resp;
				}else{
					// 判断钱是否足够
					if (roleInfo.getCoin() < GameValue.BUY_TURK_SHOP_PRICE) {
						resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
						return resp;
					}
					// 扣钱
					if (!RoleService.subRoleRoleResource(ActionType.action387.getType(), roleInfo, ConditionType.TYPE_COIN,GameValue.BUY_TURK_SHOP_PRICE , null)){
						resp.setResult(ErrorCode.STORE_ERROR_20);
						return resp;
					}
					if(RoleDAO.getInstance().shop(roleId, 1, 6,null))
					{
						roleLoadInfo.setIsBuyTurkShopForVip(1);
					}
					resp.setSourceChange(-GameValue.BUY_TURK_SHOP_PRICE);
				}
			}
		}
		resp.setResult(1);
		resp.setSourceType((byte)(ConditionType.TYPE_COIN.getType()));
		resp.setShopType(req.getShopType());
		return resp;
	}
	
	/**
	 * 
	 * @param roleInfo
	 * @param position shop.xml的positionNo
	 * @return
	 */
	private int clubShopCheck(RoleInfo roleInfo, int position){
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(roleClubInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_54;
		}
		
		GuildShopXmlInfo xmlInfo = GuildConstructionXmlInfoMap.getGuildShopXmlInfo(position);
		if(xmlInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_49;
		}
		
		if(roleClubInfo.getLevel() < xmlInfo.getLv()){
			return ErrorCode.ROLE_CLUB_ERROR_55;
		}
		
		return 1;
	}
}
