package com.snail.webgame.game.protocal.checkIn.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.checkIn.execute.ExecuteCheckInReq;
import com.snail.webgame.game.protocal.checkIn.execute.ExecuteCheckInResp;
import com.snail.webgame.game.protocal.checkIn.queryList.QueryCheckInListItemRe;
import com.snail.webgame.game.protocal.checkIn.queryList.QueryCheckInListResp;
import com.snail.webgame.game.protocal.checkIn7Day.getReward.RewardCheckIn7DayReq;
import com.snail.webgame.game.protocal.checkIn7Day.getReward.RewardCheckIn7DayResp;
import com.snail.webgame.game.protocal.checkIn7Day.queryList.QueryCheckIn7DayListResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.levelgift.query.QueryLevelGiftListRe;
import com.snail.webgame.game.protocal.levelgift.query.QueryLevelGiftListResp;
import com.snail.webgame.game.protocal.levelgift.reward.GetLevelGiftRewardReq;
import com.snail.webgame.game.protocal.levelgift.reward.GetLevelGiftRewardResp;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.CheckInPrizeXmlMap;
import com.snail.webgame.game.xml.cache.LevelGiftXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.CheckInPrizeXMLInfo;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.LevelGiftXMLInfo;

/**
 * 登陆签到功能类
 * @author lixiaojun
 */
public class CheckInMgtService {

	/**
	 * 查询签到列表
	 * @param roleId
	 * @return
	 */
	public QueryCheckInListResp queryList(int roleId) {
		QueryCheckInListResp resp = new QueryCheckInListResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_14);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_14);
				return resp;
			}
			
			if (roleLoadInfo.getLastCheckInTime() == null) {
				// 第一次打开界面默认签到功能此时开启
				roleLoadInfo.setLastCheckInTime(new Timestamp(System.currentTimeMillis()));
				roleLoadInfo.setCurrCheckDay(1);
				
				// 更新数据库
				RoleDAO.getInstance().updateRoleCheckIn(roleLoadInfo);
			}
			
			// 获取当前已签到的天数
			Map<Integer,Integer> tempMap = CheckInService.generateCheckedMap(roleLoadInfo.getCheckInStr());
			Map<Integer,Integer> vipTempMap = CheckInService.generateCheckedMap(roleLoadInfo.getVipCheckInStr());
			
			List<QueryCheckInListItemRe> checkInListItemRes = new ArrayList<QueryCheckInListItemRe>();
			QueryCheckInListItemRe checkInListItemRe = null;
			for (CheckInPrizeXMLInfo xmlInfo : CheckInPrizeXmlMap.getMap().values()) {
				
				checkInListItemRe = new QueryCheckInListItemRe();
				checkInListItemRe.setDay(xmlInfo.getNo());
				//普通签到奖励信息
				String itemStr = "";
				String bagNoStr = xmlInfo.getBagNoStr();
				if(bagNoStr != null && bagNoStr.length() > 0)
				{
					itemStr = PropBagXMLMap.getPropBagXMLByStr(bagNoStr, GameValue.CHECK_IN_PRIZE_HERO);
				}
				checkInListItemRe.setPrizeNo(itemStr);
				
				// 设置已签到标记
				if (tempMap.containsKey(xmlInfo.getNo())) {
					checkInListItemRe.setCheckIn((byte) 1);
				}
				
				// vip签到奖励信息
				checkInListItemRe.setNeedVipLv((byte) xmlInfo.getNeedVipLv());
				String vipItemStr = "";
				String vipBagNoStr = xmlInfo.getVipBagNoStr();
				if(vipBagNoStr != null && vipBagNoStr.length() > 0)
				{
					vipItemStr = PropBagXMLMap.getPropBagXMLByStr(vipBagNoStr, GameValue.CHECK_IN_PRIZE_HERO);
				}
				checkInListItemRe.setVipPrizeNo(vipItemStr);
				
				if (vipTempMap.containsKey(xmlInfo.getNo())) {
					checkInListItemRe.setIsGetVip((byte) 1);
				}
				
				checkInListItemRes.add(checkInListItemRe);
			}
			
			resp.setToday(roleLoadInfo.getCurrCheckDay());
			resp.setCheckInListItemList(checkInListItemRes);
			resp.setCheckInListItemListSize(checkInListItemRes.size());
		}
		
		resp.setRemedyCost(CheckInPrizeXmlMap.CHECK_REMEDY_COST);
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 执行签到
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ExecuteCheckInResp executeCheckIn(int roleId, ExecuteCheckInReq req) {
		ExecuteCheckInResp resp = new ExecuteCheckInResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_3);
			return resp;
		}
		
		int checkDay = req.getDay();
		// 获取配置
		CheckInPrizeXMLInfo checkInPrizeXMLInfo = CheckInPrizeXmlMap.getXmlInfo(checkDay);
		if (checkInPrizeXMLInfo == null) {
			resp.setResult(ErrorCode.QUEST_ERROR_7);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.QUEST_ERROR_7);
				return resp;
			}
			
			Map<Integer,Integer> checkedMap = CheckInService.generateCheckedMap(roleLoadInfo.getCheckInStr());
			Map<Integer,Integer> vipCheckedMap = CheckInService.generateCheckedMap(roleLoadInfo.getVipCheckInStr());
			if (req.getIsGetVip() == 1) {
				// 领取签到vip奖励
				// 判断是否已领过了
				if (vipCheckedMap.containsKey(checkDay)) {
					resp.setResult(ErrorCode.QUEST_ERROR_7);
					return resp;
				}
				
				// 是否满足vipLv要求
				if (roleInfo.getVipLv() < checkInPrizeXMLInfo.getNeedVipLv()) {
					resp.setResult(ErrorCode.QUEST_ERROR_7);
					return resp;
				}
				
				// 判断今天是否可签到
				if (checkDay != CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO && checkDay > roleLoadInfo.getCurrCheckDay()) {
					resp.setResult(ErrorCode.QUEST_ERROR_7);
					return resp;
				}
				
				// 往日没签的vip奖励也不能领
				if (checkDay < roleLoadInfo.getCurrCheckDay() && !checkedMap.containsKey(checkDay)) {
					resp.setResult(ErrorCode.QUEST_ERROR_11);
					return resp;
				}
				
				// 是否是月末大奖
				if (checkDay == CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO) {
					// 判断是否可以领,本月全部签过才能领
					if(checkedMap.size() < 30 || roleLoadInfo.getCurrCheckDay() != 30 
							|| vipCheckedMap.containsKey(CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO)) {
						resp.setResult(ErrorCode.QUEST_ERROR_10);
						return resp;
					}
				}
				
			} else {
				// 正常签到功能
				// 判断是否已签过了
				if (checkedMap.containsKey(checkDay)) {
					resp.setResult(ErrorCode.QUEST_ERROR_7);
					return resp;
				}
				
				// 判断是否是补签
				if (req.getIsRemedy() == 1) {
					// 判断是否符合补签
					if (checkDay >= roleLoadInfo.getCurrCheckDay()) {
						resp.setResult(ErrorCode.QUEST_ERROR_7);
						return resp;
					}
					
					// 判断补签消耗
					if (roleInfo.getCoin() < CheckInPrizeXmlMap.CHECK_REMEDY_COST) {
						resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
						return resp;
					}
				} else {
					// 不是补签判断今天是否可签到
					if (checkDay != CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO && checkDay != roleLoadInfo.getCurrCheckDay()) {
						resp.setResult(ErrorCode.QUEST_ERROR_7);
						return resp;
					}
				}
				
				// 是否是月末大奖
				if (checkDay == CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO) {
					// 判断是否可以领,本月全部签过才能领
					if(checkedMap.size() < 30 || roleLoadInfo.getCurrCheckDay() != 30 
							|| checkedMap.containsKey(CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO)) {
						resp.setResult(ErrorCode.QUEST_ERROR_10);
						return resp;
					}
				}
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_25);
				return resp;
			}
			
			if (req.getIsGetVip() != 1 && req.getIsRemedy() == 1) {
				// 补签扣除花费
				if (!RoleService.subRoleRoleResource(ActionType.action32.getType(), roleInfo,
						ConditionType.TYPE_COIN, CheckInPrizeXmlMap.CHECK_REMEDY_COST ,null)) {
					resp.setResult(ErrorCode.QUEST_ERROR_14);
					return resp;
				}
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, null);
			}
			
			// 发放物品
			List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
			if (req.getIsGetVip() == 1) {
				list.addAll(PropBagXMLMap.getPropBagXMLListbyStr(checkInPrizeXMLInfo.getVipBagNoStr(), GameValue.CHECK_IN_PRIZE_HERO));
			} else {
				list.addAll(PropBagXMLMap.getPropBagXMLListbyStr(checkInPrizeXMLInfo.getBagNoStr(), GameValue.CHECK_IN_PRIZE_HERO));
			}
			
			List<BattlePrize> dropList = new ArrayList<BattlePrize>();	
			ItemService.addPrizeForPropBag(ActionType.action31.getType(), roleInfo, list, null,
					dropList, null, null, null, false);
			
			// 缓存及数据库更新
			if (req.getIsGetVip() == 1) {
				vipCheckedMap.put(checkDay, 1);
				roleLoadInfo.setVipCheckInStr(CheckInService.generateNewCheckedStr(vipCheckedMap));
			} else {
				checkedMap.put(checkDay, 1);
				roleLoadInfo.setCheckInStr(CheckInService.generateNewCheckedStr(checkedMap));
			}
			RoleDAO.getInstance().updateRoleCheckIn(roleLoadInfo);
		}
		
		resp.setResult(1);
		resp.setDay(req.getDay());
		resp.setIsGetVip(req.getIsGetVip());
		
		// 任务触发检测
		// 红点触发检测	
		boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action31.getType(), null, true, false);
		boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleId, null, false, RedPointMgtService.LISTEN_CHECK_IN_TYPES);
		//红点推送
		if(isRedPointQuest || isRedPointMonth){
			RedPointMgtService.pop(roleInfo.getId());
		}
		
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action31.getType(), req.getDay()+","+req.getIsRemedy()+","+req.getIsGetVip());
		return resp;
	}
	
	/**
	 * 查询7日连续签到活动
	 * @param roleId
	 * @return
	 */
	public QueryCheckIn7DayListResp query7DayList(int roleId) {
		QueryCheckIn7DayListResp resp = new QueryCheckIn7DayListResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_4);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_4);
			return resp;
		}
		synchronized (roleInfo) {
			resp.setCheckInListItemList(CheckInService.getCheckIn7DayListItem(roleInfo));
			if(resp.getCheckInListItemList() != null){
				resp.setCheckInListItemListSize(resp.getCheckInListItemList().size());
			}
			resp.setMaxDay(roleLoadInfo.getCheckIn7DayMaxLoginDays());
			resp.setToday(roleLoadInfo.getCheckIn7DayCurrentLoginDays());
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 领取7日连续签到活动物品
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RewardCheckIn7DayResp checkInReward(int roleId, RewardCheckIn7DayReq req){
		RewardCheckIn7DayResp resp = new RewardCheckIn7DayResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			resp.setResult(CheckInService.checkInReward(roleInfo, req.getNo()));
		}
		resp.setNo(req.getNo());
		//红点检测
		RedPointMgtService.check2PopRedPoint(roleId, null, true, RedPointMgtService.LISTEN_CHECK_IN_TYPES);
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action33.getType(), req.getNo()+"");
		return resp;
	}

	/**
	 * 查询等级礼包列表
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryLevelGiftListResp queryLevelGiftList(int roleId) {
		QueryLevelGiftListResp resp = new QueryLevelGiftListResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_2);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_LOAD_ERROR_3);
			return resp;
		}
		
		// 获取已领取过的等级礼包信息
		Map<Integer,Integer> tempMap = new HashMap<Integer,Integer>();
		if (roleLoadInfo.getDrawLevelGiftStr() != null && roleLoadInfo.getDrawLevelGiftStr().length() > 0) {
			String[] subArr = roleLoadInfo.getDrawLevelGiftStr().split(",");
			for (String noStr : subArr) {
				// 已领取的礼包no包装成map的形式
				tempMap.put(Integer.valueOf(noStr), 1);
			}
		}
		
		// 所有等级礼包列表
		List<QueryLevelGiftListRe> giftLists = new ArrayList<QueryLevelGiftListRe>();
		QueryLevelGiftListRe re;
		for (LevelGiftXMLInfo xmlInfo : LevelGiftXMLInfoMap.getMap().values()) {
			re = new QueryLevelGiftListRe();
			re.setNo(xmlInfo.getNo());
			
			if (tempMap.containsKey(xmlInfo.getNo())) {
				re.setIsGet((byte) 1);
			} else {
				re.setIsGet((byte) 0);
			}
			
			giftLists.add(re);
		}
		
		resp.setResult(1);
		resp.setQueryListSize(giftLists.size());
		resp.setQueryList(giftLists);
		return resp;
	}
	
	/**
	 * 检测是否有礼包可领取
	 * 
	 * @param roleInfo
	 * @param roleLoadInfo
	 * @return
	 */
	public static boolean checkIsGetLevelGift(RoleInfo roleInfo, RoleLoadInfo roleLoadInfo) {
		// 获取已领取过的等级礼包信息
		List<Integer> temp = new ArrayList<Integer>();
		if (roleLoadInfo.getDrawLevelGiftStr() != null && roleLoadInfo.getDrawLevelGiftStr().length() > 0) {
			String[] subArr = roleLoadInfo.getDrawLevelGiftStr().split(",");
			for (String noStr : subArr) {
				temp.add(Integer.parseInt(noStr));
			}
		}
		
		// 检测是否有可领取礼包
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		for (LevelGiftXMLInfo xmlInfo : LevelGiftXMLInfoMap.getMap().values()) {
			if (!temp.contains(xmlInfo.getNo()) && heroInfo.getHeroLevel() >= xmlInfo.getNeedLv()) {
				return true;
			} 
		}
		
		return false;
	}

	/**
	 * 领取等级礼包奖励
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetLevelGiftRewardResp getLevelGiftReward(int roleId, GetLevelGiftRewardReq req) {
		GetLevelGiftRewardResp resp = new GetLevelGiftRewardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_2);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_LOAD_ERROR_3);
			return resp;
		}
		
		// 是否存在该礼包
		LevelGiftXMLInfo levelGiftXMLInfo = LevelGiftXMLInfoMap.fetchXMLInfoByNo(req.getNo());
		if (levelGiftXMLInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_2);
			return resp;
		}
		
		synchronized (roleInfo) {
			// 是否达到可领取等级要求
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null || heroInfo.getHeroLevel() < levelGiftXMLInfo.getNeedLv()) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_2);
				return resp;
			}
			
			// 获取已领取过的等级礼包信息
			Map<Integer,Integer> tempMap = new HashMap<Integer,Integer>();
			if (roleLoadInfo.getDrawLevelGiftStr() != null && roleLoadInfo.getDrawLevelGiftStr().length() > 0) {
				String[] subArr = roleLoadInfo.getDrawLevelGiftStr().split(",");
				for (String noStr : subArr) {
					// 已领取的礼包no包装成map的形式
					tempMap.put(Integer.valueOf(noStr), 1);
				}
			}
			
			// 礼包是否已领取过
			if (tempMap.containsKey(req.getNo())) {
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_3);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}			
			// 记录已领取编号
			tempMap.put(req.getNo(), 1);			
			String sb = "";
			for (int giftNo : tempMap.keySet()) {
				if(sb.length() == 0){
					sb = sb + giftNo;
				} else {
					sb = sb + "," + giftNo;
				}
			}
			
			// 更新数据库
			RoleDAO.getInstance().updateLevelGiftNo(roleId, sb);
						
			// 修改缓存
			roleLoadInfo.setDrawLevelGiftStr(sb.toString());
			
			// 添加奖励
			String prizeNo = levelGiftXMLInfo.getPrizeNo();
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			if (drops != null) {
				List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
				int result1 = ItemService.addPrizeForPropBag(ActionType.action34.getType(), roleInfo, drops,null, 
						getPropList,null, null, null, false);
				
				if (result1 != 1) {
					resp.setResult(result1);
					return resp;
				}
				
				if(levelGiftXMLInfo.getNeedLv() == 35){
					OpActivityMgrService.appCommentNotify(roleInfo);
				}
			}
						
			//判断新手引导是否要更新
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo,  UserGuideNode.GAME_GUIDE_GIFT);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTEN_CHECK_IN_TYPES);
		}
		
		resp.setResult(1);
		resp.setNo(req.getNo());
		return resp;
	}
}
