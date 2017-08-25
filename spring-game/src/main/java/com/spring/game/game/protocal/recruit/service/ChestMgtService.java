package com.snail.webgame.game.protocal.recruit.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.recruit.query.QueryChestResp;
import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;
import com.snail.webgame.game.protocal.recruit.recruit.ChestItemReq;
import com.snail.webgame.game.protocal.recruit.recruit.ChestItemResp;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.FixLotteryInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.RecruitDepotXMLInfoMap;
import com.snail.webgame.game.xml.cache.RecruitKindXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.FixLotteryConfigInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;
import com.snail.webgame.game.xml.info.RecruitKindXMLInfo;

public class ChestMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	private RoleDAO roleDAO = RoleDAO.getInstance();

	/**
	 * 查询抽卡信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryChestResp queryRecruit(int roleId) {
		QueryChestResp resp = new QueryChestResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_RECRUIT_ERROR_4);
			return resp;
		}
		synchronized (roleInfo) {
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_1);
				return resp;
			}
			
			// 每天抽卡次数
			int recruitMoneyNum = 0;
			long recruitMoneyTime = 0;
			long recruitCoinTime= 0;
			long recruitHeroTime= 0;
			if (roleLoadInfo.getLastRecruitMoneyTime() != null
					&& DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getLastRecruitMoneyTime().getTime())) {
				recruitMoneyNum = roleLoadInfo.getRecruitMoneyNum();
			}
			if (roleLoadInfo.getLastRecruitMoneyTime() != null) {
				recruitMoneyTime = roleLoadInfo.getLastRecruitMoneyTime().getTime();
			}
			if (roleLoadInfo.getLastRecruitCoinTime() != null) {
				recruitCoinTime = roleLoadInfo.getLastRecruitCoinTime().getTime();
			}
			if (roleLoadInfo.getLastRecruitHeroFreeTime() != null) {
				recruitHeroTime = roleLoadInfo.getLastRecruitHeroFreeTime().getTime();
			}
			resp.setRecruitMoneyNum((byte) recruitMoneyNum);
			resp.setLastRecruitMoneyTime(recruitMoneyTime);
			resp.setLastRecruitCoinTime(recruitCoinTime);
			resp.setLastRecruitheroTime(recruitHeroTime);
			
			resp.setRecruitLimit(GameValue.RECRUIT_FREE_lIMIT);
			resp.setRecruitMoneySpaceTime((byte) GameValue.RECRUIT_FREE_MONEY_TIME);
			resp.setRecruitCoinSpaceTime((byte) GameValue.RECRUIT_FREE_COIN_TIME);
			resp.setRecruitHeroSpaceTime((byte) GameValue.RECRUIT_FREE_HERO_TIME);
			
			if(roleLoadInfo.getOneRecruitCoinOpTimes() < 3){
				//第一次特定是三次
				resp.setOneRecruitCoinOpTimes((byte)(3 - roleLoadInfo.getOneRecruitCoinOpTimes()));
			}else{
				
				resp.setOneRecruitCoinOpTimes((byte) (GameValue.ONE_RECRUIT_FIX_REWARD_TIMES - (roleLoadInfo.getOneRecruitCoinOpTimes() % GameValue.ONE_RECRUIT_FIX_REWARD_TIMES)));
			}
			
			if(roleLoadInfo.getOneRecruitHeroNum() < 3){
				//第一次特定是三次
				resp.setOneRecrutiHeroNum((byte)(3 - roleLoadInfo.getOneRecruitHeroNum()));
			}else{
				resp.setOneRecrutiHeroNum((byte) (GameValue.HERO_RECRUIT_FIX_REWARD_TIMES - (roleLoadInfo.getOneRecruitHeroNum() % GameValue.HERO_RECRUIT_FIX_REWARD_TIMES)));
			}
			
			
			resp.setResult(1);

			return resp;
		}
	}

	/**
	 * 抽卡
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ChestItemResp recruitItem(int roleId, ChestItemReq req) {
		ChestItemResp resp = new ChestItemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CHEST_CARD_ERROR_3);
			return resp;
		}
		byte action = req.getAction();
		if (action < 0) {
			resp.setResult(ErrorCode.CHEST_CARD_ERROR_4);
			return resp;
		}
		synchronized (roleInfo) {
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_1);
				return resp;
			}
			
			RecruitKindXMLInfo kind = RecruitKindXMLInfoMap.getRecruitKindXMLInfo(action);
			if (kind == null) {
				resp.setResult(ErrorCode.CHEST_CARD_ERROR_1);
				return resp;
			}

			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//抽奖前先判断背包格子数量是否足够
			if(result != 1){
				resp.setResult(ErrorCode.CHEST_CARD_ERROR_5);
				return resp;
			}
			
			switch (action) {
			case 1:// 银子单抽
			case 3:// 装备单抽
			case 7:// 武将单抽
				resp = oneRecruitItem(roleInfo, kind);
				break;
			case 2:// 银子十连抽
			case 4:// 装备十连抽
			case 8:// 武将十连抽
				resp = recruitItemPool(roleInfo, kind);
				break;
			default:
				resp.setResult(ErrorCode.RECRUIE_TYPE_ERROR);
				return resp;
			}
			
			if(resp.getResult() == 1) {
//				List<ChestItemRe> chestList = resp.getList();
//				if(chestList.size() > 0) {
//					//控制客户端是否播放动画
//					HeropropConfigInfo heropropConfigInfo = null;
//					for(ChestItemRe re : chestList) {
//						if(String.valueOf(re.getItemNo()).startsWith(GameValue.PROP_STAR_N0)) {
//							
//							heropropConfigInfo = HeropropConfigInfoMap.getHeropropConfigInfo(re.getItemNo());
//							if(heropropConfigInfo == null) {
//								continue;
//							}
//							if(HeroInfoMap.getHeroInfoByNo(roleId, heropropConfigInfo.getHero()) == null) {
//								//角色没有对应的英雄存在
//								re.setItemType((byte)ChestItemRe.TYPE_STAR_PLAY);
//							}
//						}
//					}
//				}
				// 更新客户端刷新参数
				int recruitMoneyNum = 0;
				long recruitMoneyTime = 0;
				long recruitCoinTime = 0;
				long recruitHeroTime = 0;
				
				if (roleLoadInfo.getLastRecruitMoneyTime() != null
						&& DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getLastRecruitMoneyTime().getTime())) {
					recruitMoneyNum = roleLoadInfo.getRecruitMoneyNum();
				}

				if (roleLoadInfo.getLastRecruitMoneyTime() != null) {
					recruitMoneyTime = roleLoadInfo.getLastRecruitMoneyTime().getTime();
				}
				if (roleLoadInfo.getLastRecruitCoinTime() != null) {
					recruitCoinTime = roleLoadInfo.getLastRecruitCoinTime().getTime();
				}
				if (roleLoadInfo.getLastRecruitHeroFreeTime() != null) {
					recruitHeroTime = roleLoadInfo.getLastRecruitHeroFreeTime().getTime();
				}

				resp.setAction(req.getAction());
				resp.setRecruitLimit(GameValue.RECRUIT_FREE_lIMIT);
				resp.setRecruitMoneyNum((byte) recruitMoneyNum);
				resp.setLastRecruitMoneyTime(recruitMoneyTime);
				resp.setLastRecruitCoinTime(recruitCoinTime);
				resp.setLastRecruitHeroTime(recruitHeroTime);
				resp.setRecruitMoneySpaceTime((byte) GameValue.RECRUIT_FREE_MONEY_TIME);
				resp.setRecruitCoinSpaceTime((byte) GameValue.RECRUIT_FREE_COIN_TIME);
				resp.setRecruitHeroSpaceTime((byte) GameValue.RECRUIT_FREE_HERO_TIME);
				
				if(roleLoadInfo.getOneRecruitCoinOpTimes() < 3){
					//第一次特定是三次
					resp.setOneRecruitCoinOpTimes((byte)(3 - roleLoadInfo.getOneRecruitCoinOpTimes()));
				}else{
					
					resp.setOneRecruitCoinOpTimes((byte) (GameValue.ONE_RECRUIT_FIX_REWARD_TIMES - (roleLoadInfo.getOneRecruitCoinOpTimes() % GameValue.ONE_RECRUIT_FIX_REWARD_TIMES)));
				}
				
				if(roleLoadInfo.getOneRecruitHeroNum() < 3){
					//第一次特定是三次
					resp.setOneRecrutiHeroNum((byte)(3 - roleLoadInfo.getOneRecruitHeroNum()));
				}else{
					resp.setOneRecrutiHeroNum((byte) (GameValue.HERO_RECRUIT_FIX_REWARD_TIMES - (roleLoadInfo.getOneRecruitHeroNum() % GameValue.HERO_RECRUIT_FIX_REWARD_TIMES)));
				}
				
				// 行为日志
				int chestAction = ChestService.getChestNo(kind);
				String str = resp.getSourceType()+""+resp.getSourceChange()+":";
				List<ChestItemRe> list = resp.getList();
				for(ChestItemRe re : list)
				{
					if(re == null)
					{
						continue;
					}
					str = str +re.getItemNo()+"-"+re.getItemNum()+",";
				}
				GameLogService.insertPlayActionLog(roleInfo, chestAction, str.substring(0, str.length()-1));
			}
			
			
			return resp;
		}
	}

	/**
	 * 单抽
	 * 
	 * @param roleInfo
	 * @param kind
	 * @return
	 */
	private ChestItemResp oneRecruitItem(RoleInfo roleInfo, RecruitKindXMLInfo kind) {
		ChestItemResp resp = new ChestItemResp();
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.SYSTEM_ERROR);
			return resp;
		}
		if (kind == null) {
			resp.setResult(ErrorCode.SYSTEM_ERROR);
			return resp;
		}
		int action = ChestService.getChestNo(kind);
		if (action == 0) {
			resp.setResult(ErrorCode.SYSTEM_ERROR);
			return resp;
		}
		DropInfo dropInfo = null;
		long now = System.currentTimeMillis();
		boolean isFree = false;
		switch (kind.getNo()) {
		case 1:// 银子单抽
			byte recruitNum = roleLoadInfo.getTodayRecruitMoneyNum();
			if (roleLoadInfo.getLastRecruitMoneyTime() == null) {
				isFree = true;// 第一次免费
				dropInfo = new DropInfo("" + GameValue.FIRST_REQUEST_MONEY_HERO, 1);
			} else {
				if (recruitNum < GameValue.RECRUIT_FREE_lIMIT
						&& now - roleLoadInfo.getLastRecruitMoneyTime().getTime() >= GameValue.RECRUIT_FREE_MONEY_TIME * 60 * 1000L) {
					isFree = true;
				}
				dropInfo = getRecruitItemNo(kind.getDepotNoStr());
				if (dropInfo == null) {
					resp.setResult(ErrorCode.RECRUIE_PROP_ERROR);
					return resp;
				}
			}
			if (isFree) {// 免费
				recruitNum = (byte) (recruitNum + 1);
				if (roleDAO.updateRoleRecruitMoney(roleInfo.getId(), recruitNum, new Timestamp(now))) {
					roleLoadInfo.setRecruitMoneyNum(recruitNum);
					roleLoadInfo.setLastRecruitMoneyTime(new Timestamp(now));
				} else {
					resp.setResult(ErrorCode.HERO_RECRUIE_DAY_ERROR_7);
					return resp;
				}
			}
			
			if (!isFree) {// 非免费
				int check = AbstractConditionCheck.checkCondition(roleInfo, kind.getConditions());
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
			}

			break;
		case 3:// 装备单抽
			int freeTime = GameValue.RECRUIT_FREE_COIN_TIME;// 金子抽卡免费间隔时间
			int recruitTimes = roleLoadInfo.getOneRecruitCoinOpTimes() + 1;
			if (roleLoadInfo.getLastRecruitCoinTime() == null) {
				// 第一次免费
				isFree = true;
				dropInfo = getRecruitItemNo(kind.getDepotNoStr());
			} else {
				if (roleLoadInfo.getLastRecruitCoinTime().getTime() + freeTime * 60 * 60 * 1000L <= now) {
					isFree = true;
				}
				
				// 正常掉落
				dropInfo = getRecruitItemNo(kind.getDepotNoStr());
				
				//装备逢十次必出物品修改
				for(int i = 1 ; i<Integer.MAX_VALUE ; i++)
				{
					if(GameValue.ONE_RECRUIT_FIX_REWARD_TIMES * i == recruitTimes)
					{
						dropInfo = getRecruitItemNo(GameValue.FIX_REWARD_NOSTR);
						break;
					}
					
					if(GameValue.ONE_RECRUIT_FIX_REWARD_TIMES * i > recruitTimes)
					{
						break;
					}
				}
				//特殊情况,基本不会超出
				if(recruitTimes>=Integer.MAX_VALUE)
				{
					recruitTimes = Integer.MAX_VALUE-1;
				}
				
				// 特定次数的奖励
				if(RecruitDepotXMLInfoMap.getGoldRecruitPrizeMap().containsKey(recruitTimes))
				{
					String dropNo = RecruitDepotXMLInfoMap.getGoldRecruitPrizeMap().get(recruitTimes);
					dropInfo = getRecruitItemNo(dropNo);
				}
				
				if (dropInfo == null) {
					resp.setResult(ErrorCode.RECRUIE_PROP_ERROR);
					return resp;
				}
			}
			if (isFree) {// 免费
				if (roleDAO.updateRoleRecruitCoin(roleInfo.getId(), new Timestamp(now))) {
					roleLoadInfo.setLastRecruitCoinTime(new Timestamp(now));
				} else {
					resp.setResult(ErrorCode.RECRUIE_PROP_ERROR_1);
					return resp;
				}
			}
			if (!isFree) {// 非免费
				int check = AbstractConditionCheck.checkCondition(roleInfo, kind.getConditions());
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
			}
			
			if(RoleDAO.getInstance().updateOneRecruitCoinOpTimes(roleInfo.getId(), recruitTimes)){
				roleLoadInfo.setOneRecruitCoinOpTimes(recruitTimes);
			}
			
			break;
		case 7:// 武将单抽
			int heroRecruitNum = roleLoadInfo.getOneRecruitHeroNum() + 1;
			if (roleLoadInfo.getLastRecruitHeroFreeTime() == null) {
				// 第一次免费
				isFree = true;
				dropInfo = new DropInfo("" + GameValue.FIRST_REQUEST_GOLD_HERO, 1);
			} else {
				if (roleLoadInfo.getLastRecruitHeroFreeTime().getTime() + GameValue.RECRUIT_FREE_HERO_TIME * 3600 * 1000L <= now) {
					isFree = true;
				}
				
				// 正常掉落
				dropInfo = getRecruitItemNo(kind.getDepotNoStr());
				
				//武将单抽逢十次必出物品修改
				for(int i = 1 ; i<Integer.MAX_VALUE ; i++)
				{
					if(GameValue.HERO_RECRUIT_FIX_REWARD_TIMES * i == heroRecruitNum)
					{
						dropInfo = getRecruitItemNo(GameValue.FIX_HERO_REWARD_NOSTR);
						break;
					}
					
					if(GameValue.HERO_RECRUIT_FIX_REWARD_TIMES * i > heroRecruitNum)
					{
						break;
					}
				}
				
				//特殊情况,基本不会超出
				if(heroRecruitNum>=Integer.MAX_VALUE)
				{
					heroRecruitNum = Integer.MAX_VALUE-1;
				}
				
				// 特定次数的奖励
				if(RecruitDepotXMLInfoMap.getHeroRecruitPrizeMap().containsKey(heroRecruitNum))
				{
					String dropNo = RecruitDepotXMLInfoMap.getHeroRecruitPrizeMap().get(heroRecruitNum);
					dropInfo = getRecruitItemNo(dropNo);
				}
				
				if (dropInfo == null) {
					resp.setResult(ErrorCode.RECRUIE_PROP_ERROR);
					return resp;
				}
			}
			if (isFree) {
				// 免费
				if (roleDAO.updateOneRecruitHeroFreeTime(roleInfo.getId(), new Timestamp(now))) {
					roleLoadInfo.setLastRecruitHeroFreeTime(new Timestamp(now));
				} else {
					resp.setResult(ErrorCode.RECRUIE_PROP_ERROR_1);
					return resp;
				}
			}
			if (!isFree) {// 非免费
				int check = AbstractConditionCheck.checkCondition(roleInfo, kind.getConditions());
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
			}
			
			if(RoleDAO.getInstance().updateOneRecruitHeroNum(roleInfo.getId(), heroRecruitNum)){
				roleLoadInfo.setOneRecruitHeroNum(heroRecruitNum);
			}
			
			break;
		default:
			break;
		}

		if (!isFree) {
			// 扣去消耗资源
			if (RoleService.subRoleResource(action, roleInfo, kind.getConditions() , null)) {
				String updateSourceStr = RoleService.returnResourceChange(kind.getConditions());
				if (updateSourceStr != null) {
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1) {
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
			} else {
				resp.setResult(ErrorCode.RECRUIE_CUT_PROP_ERROR_2);
				return resp;
			}
		}

		// 保存物品
		List<DropInfo> drops = new ArrayList<DropInfo>();
		//公告list
		List<DropInfo> gonggao = new ArrayList<DropInfo>();
		drops.add(dropInfo);
		gonggao.add(dropInfo);
		// 抽奖获得
		List<ChestItemRe> list = new ArrayList<ChestItemRe>();
		List<Integer> heroIds=new ArrayList<Integer>();
		
		int save = ItemService.addPrize(action, roleInfo, drops, null, null, null, null, null, heroIds, null, null, list,
				false);

		if (save != 1) {
			resp.setResult(save);
			return resp;
		}
		ItemService.prizeRefesh(roleInfo, null, null, null, heroIds, null);

		// 任务 红点
		QuestService.checkQuest(roleInfo, action, 1, true, false);
		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
				RedPointMgtService.LISTENING_CHEST_CHANGE_TYPES);
			RedPointMgtService.pop(roleInfo.getId());
		
		//公告
		sendChatMessage(roleInfo, gonggao); //获得特定物品消息推送
		
		//判断新手引导是否要更新
		if(kind.getNo()==1){
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_NORMAL_LOTTERY);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}
		}else if(kind.getNo()==7){
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_GOLD_LOTTERY);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}
		}
		
		if(kind.getNo() == 7 || kind.getNo() == 3){
			//金子单抽银子奖励
			RoleService.addRoleRoleResource(action, roleInfo, ConditionType.TYPE_MONEY, GameValue.ONE_CHEST_REWARD_MONEY,null);
		}
		
		if (action == ActionType.action68.getType() || action == ActionType.action437.getType()) {
			// 装备单抽、武将单抽 时限活动检测
			OpActivityService.dealOpActProInfoCheck(roleInfo, action, null, true);
		}

		resp.setResult(1);
		resp.setAction((byte) kind.getNo());
		resp.setList(list);
		resp.setCount(list.size());
		
		return resp;
	}

	/**
	 * 十连抽
	 * 
	 * @param roleInfo
	 * @param kind
	 * @return
	 */
	private ChestItemResp recruitItemPool(RoleInfo roleInfo, RecruitKindXMLInfo kind) {
		ChestItemResp resp = new ChestItemResp();
		int action = ChestService.getChestNo(kind);
		if(action == 0) {
			resp.setResult(ErrorCode.SYSTEM_ERROR);
			return resp;
		}

		List<ChestItemRe> list = new ArrayList<ChestItemRe>();
		int result = 1;
		int poolNum = kind.getPoolNum();
		//十连抽计算
		result = recruitItemPoolNum2(roleInfo, kind, poolNum, list, resp);
		if (result != 1) {
			resp.setResult(result);
			return resp;
		}

		// 任务
		QuestService.checkQuest(roleInfo, action, poolNum, true, true);
		
		if (action == ActionType.action69.getType() || action == ActionType.action438.getType()) {
			// 金币10连抽、武将10连抽、装备10连抽 时限活动检测
			OpActivityService.dealOpActProInfoCheck(roleInfo, action, null, true);
		}

		resp.setResult(1);
		resp.setAction((byte) kind.getNo());
		resp.setCount(list.size());
		resp.setList(list);
		
		return resp;
	}

	/**
	 * 十连抽 逻辑
	 * 
	 * @param roleInfo
	 * @param kind xml相关
	 * @param poolNum 抽取次数
	 * @param list 获得物品记录
	 * @return
	 */
	private int recruitItemPoolNum2(RoleInfo roleInfo, RecruitKindXMLInfo kind, int poolNum, List<ChestItemRe> chestItemList,ChestItemResp resp) {

		// 记录抽到的奖励
		List<DropInfo> dropInfos = null;
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if(roleLoadInfo == null){
			return ErrorCode.SYSTEM_ERROR;
		}
		
		int action = ChestService.getChestNo(kind);
		if(action == 0)
		{
			return ErrorCode.SYSTEM_ERROR;
		}
		int time = 0;
		PropXMLInfo freePropXML = null;
		boolean subFreeProp = false;
		int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
		if(kind.getNo() == 2){
			// 检测前置条件
			int check = AbstractConditionCheck.checkCondition(roleInfo, kind.getConditions());
			if (check != 1) {
				return check;
			}
			//银子十连抽固定奖励处理
			int times = roleLoadInfo.getTenRecruitMoneyStats() + 1;
			if(times > Integer.MAX_VALUE)
			{
				times = Integer.MAX_VALUE - 2;
				
			}
			
			time = times % GameValue.MONEY_FIX_LOTTERY_TIMES == 0 ? 1 : times % GameValue.MONEY_FIX_LOTTERY_TIMES;
			
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if(heroInfo == null){
				return ErrorCode.HERO_UP_ERROR_8;
			}
			
			//计算抽卡获得
			dropInfos =  getFirstTenFixLotteryRecruit(time , 1, heroInfo.getHeroNo());
			
			if(dropInfos == null || dropInfos.size() != 10){
				return ErrorCode.CHEST_CARD_ERROR_1;
			}
			//存储数据库修改缓存
			if(roleDAO.updateTenRecruit(roleInfo.getId(), times, roleLoadInfo.getTenRecruitCoinStatus())){
				roleLoadInfo.setTenRecruitMoneyStats(times);
			}
			
		}else if(kind.getNo() == 4){
			freePropXML = PropXMLInfoMap.getPropXMLInfo(38800001);
			if(freePropXML != null && mainHeroLv >= freePropXML.getLevel()
					&& BagItemMap.checkBagItemNum(roleInfo, freePropXML.getNo(), 1)){
				// 检测前置条件
				subFreeProp = true;			
			}
			if(!subFreeProp){
				// 检测前置条件
				int check = AbstractConditionCheck.checkCondition(roleInfo, kind.getConditions());
				if (check != 1) {
					return check;
				}
			}
			
			//装备十连抽固定奖励处理
			int recruitCounter = roleLoadInfo.getOneRecruitCoinOpTimes() + roleLoadInfo.getTenRecruitCoinStatus() * 10; // 用于判断单抽是否要替换奖励
			int times = roleLoadInfo.getTenRecruitCoinStatus() + 1;
			if(times == 1 && roleLoadInfo.getOneRecruitCoinOpTimes() >= 10){
				//单抽次数大于10次 十连抽没有抽过 第一次时候直接跳过十连抽第一次抽奖
				times++;
			}
			
			if(times > Integer.MAX_VALUE)
			{
				times = Integer.MAX_VALUE - 2;
				
			}
			
			
			time = times % GameValue.COIN_FIX_LOTTERY_TIMES == 0 ? 1 : times % GameValue.COIN_FIX_LOTTERY_TIMES;
			
			if(time == 1 && roleLoadInfo.getTenRecruitCoinStatus() > 0){
				time = GameValue.COIN_FIX_LOTTERY_TIMES;
			}
			
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if(heroInfo == null){
				return ErrorCode.HERO_UP_ERROR_8;
			}
			
			dropInfos =  getFirstTenFixLotteryRecruit(time, 2, heroInfo.getHeroNo());

			if(dropInfos == null || dropInfos.size() != 10){
				return ErrorCode.CHEST_CARD_ERROR_1;
			}
			
			//去掉单抽10次内已经获取过的特定高级奖励
			if(recruitCounter >= 3 && recruitCounter < 10){
				// 只替换第三次的奖励
				EquipXMLInfo xmlInfo = null;
				for(DropInfo di : dropInfos){
					if(di == null || di.getItemNo() == null)
					{
						logger.error("##########chest item error===1");
						continue;
					}
					if(di.getItemNo().startsWith("360")){
						xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(Integer.parseInt(di.getItemNo()));
						if(xmlInfo == null){
							continue;
						}
						
						if(xmlInfo.getCharacter() == 22){
							//替换
							DropInfo replce = getRecruitItemNo(GameValue.REPLACE_EQUIP_REWARD_NOSTR);
							if(replce == null){
								continue;
							}
							di.setItemNo(replce.getItemNo());
							di.setItemNum(replce.getItemNum());
						}
						
					}
				}
				
			}
//			else if(recruitCounter >= 7 && recruitCounter < 10){
//				// 情况2 已经获取第三次和 第七次奖励 替换两个
//				EquipXMLInfo xmlInfo = null;
//				int i = 0;
//				for(DropInfo di : dropInfos){
//					if(di == null || di.getItemNo() == null)
//					{
//						logger.error("##########chest item error===2");
//						continue;
//					}
//					if(di.getItemNo().startsWith("360")){
//						xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(Integer.parseInt(di.getItemNo()));
//						if(xmlInfo == null){
//							continue;
//						}
//						
//						if(xmlInfo.getCharacter() == 22){
//							//品质22的替换
//							DropInfo replce = getRecruitItemNo(GameValue.REPLACE_EQUIP_REWARD_NOSTR);
//							if(replce == null){
//								continue;
//							}
//							di.setItemNo(replce.getItemNo());
//							di.setItemNum(replce.getItemNum());
//							i++;
//						}
//						
//						if(i == 2){
//							break;
//						}
//					}
//				}
//				
//			}
			
			//存储数据库修改缓存
			if(roleDAO.updateTenRecruit(roleInfo.getId(), roleLoadInfo.getTenRecruitMoneyStats(), times)){
				roleLoadInfo.setTenRecruitCoinStatus(times);
			}
			
		}else if(kind.getNo() == 8){
			freePropXML = PropXMLInfoMap.getPropXMLInfo(38800002);
			if(freePropXML != null && mainHeroLv >= freePropXML.getLevel()
					&& BagItemMap.checkBagItemNum(roleInfo, freePropXML.getNo(), 1)){
				// 检测前置条件
				subFreeProp = true;			
			}
			if(!subFreeProp){
				// 检测前置条件
				int check = AbstractConditionCheck.checkCondition(roleInfo, kind.getConditions());
				if (check != 1) {
					return check;
				}
			}
						
			//武将十连抽固定奖励处理
			int oneRecruitHeroNum = roleLoadInfo.getOneRecruitHeroNum() + roleLoadInfo.getTenRecruitHeroNum() * 10;
			int num = roleLoadInfo.getTenRecruitHeroNum() + 1;
			if(num == 1 && roleLoadInfo.getOneRecruitHeroNum() >= 10){
				//单抽次数大于10次 十连抽没有抽过 第一次时候直接跳过十连抽第一次抽奖
				num++;
			}
			
			if(num > Integer.MAX_VALUE)
			{
				num = Integer.MAX_VALUE -2;
			}
			
			time = num % GameValue.HERO_FIX_LOTTERY_TIMES == 0 ? 1 : num % GameValue.HERO_FIX_LOTTERY_TIMES;
			
			if(time == 1 && roleLoadInfo.getTenRecruitHeroNum() > 0){
				time = GameValue.HERO_FIX_LOTTERY_TIMES;
			}
			
			
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if(heroInfo == null){
				return ErrorCode.HERO_UP_ERROR_8;
			}
			
			dropInfos =  getFirstTenFixLotteryRecruit(time, 3, heroInfo.getHeroNo());

			if(dropInfos == null || dropInfos.size() != 10){
				return ErrorCode.CHEST_CARD_ERROR_1;
			}
			
			
			//去掉单抽10次内已经获取过的特定高级奖励
			if(oneRecruitHeroNum >= 3 && oneRecruitHeroNum < 10){
				// 情况1 只替换第三次的
				HeroXMLInfo xmlInfo = null;
				for(DropInfo di : dropInfos){
					if(di == null || di.getItemNo() == null)
					{
						logger.error("##########chest item error===3");
						continue;
					}
					if(di.getItemNo().startsWith("350")){
						xmlInfo = HeroXMLInfoMap.getHeroXMLInfo(Integer.parseInt(di.getItemNo()));
						if(xmlInfo == null){
							continue;
						}
						
						if(xmlInfo.getStar() == 3){
							//替换三星武将
							DropInfo replce = getRecruitItemNo(GameValue.REPLACE_HERO_REWARD_NOSTR);
							if(replce == null){
								continue;
							}
							di.setItemNo(replce.getItemNo());
							di.setItemNum(replce.getItemNum());

							break;
						}
						
					}
				}
				
			}
//			else if(oneRecruitHeroNum >= 7 && oneRecruitHeroNum < 10){
//				// 情况2 已经获取第三次和 第七次奖励 替换两个
//				HeroXMLInfo xmlInfo = null;
//				int i = 0;
//				for(DropInfo di : dropInfos){
//					if(di == null || di.getItemNo() == null)
//					{
//						logger.error("##########chest item error===4");
//						continue;
//					}
//					if(di.getItemNo().startsWith("350")){
//						xmlInfo = HeroXMLInfoMap.getHeroXMLInfo(Integer.parseInt(di.getItemNo()));
//						if(xmlInfo == null){
//							continue;
//						}
//						
//						if(xmlInfo.getStar() == 3){
//							//替换三星武将
//							DropInfo replce = getRecruitItemNo(GameValue.REPLACE_HERO_REWARD_NOSTR);
//							if(replce == null){
//								continue;
//							}
//							di.setItemNo(replce.getItemNo());
//							di.setItemNum(replce.getItemNum());
//
//							i++;
//						}
//						
//						if(i == 2){
//							break;
//						}
//						
//					}
//				}
//				
//			}
			
			
			//十连抽规定次数内 把已经抽到的三星武将替换掉
			if(roleLoadInfo.getTenRecruitHeroNum() > 0 && roleLoadInfo.getTenRecruitHeroNum() <= GameValue.TEN_HERO_CHEST_REWARD_REPLACE_TIME){
				HeroXMLInfo heroXMLInfo = null;
				
				Set<String> set = new HashSet<String>();
				
				for(Integer heroId : roleInfo.getHeroMap().keySet()){
					heroInfo = roleInfo.getHeroMap().get(heroId);
					
					if(heroInfo == null || heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
						continue;
					}
					
					heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
					if(heroXMLInfo == null){
						continue;
					}
					
					if(heroXMLInfo.getStar() == 3){
						//已经有的三星武将
						set.add(heroXMLInfo.getNo() + "");
					}
					
				}
				
				for(DropInfo di : dropInfos){
					if(di.getItemNo().startsWith("350")){
						heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(Integer.parseInt(di.getItemNo()));
						if(heroXMLInfo == null){
							continue;
						}
						
						if(heroXMLInfo.getStar() == 3){
							int i = 0;
							while(set.contains(di.getItemNo())){
								if(i++ == 100){
									//避免死循环
									break;
								}
								//替换三星武将
								DropInfo replce = getRecruitItemNo(GameValue.REPLACE_HERO_REWARD_NOSTR);
								if(replce == null){
									continue;
								}
								
								di.setItemNo(replce.getItemNo());
								di.setItemNum(replce.getItemNum());
								
							}
							
							set.add(di.getItemNo()); //添加已经获取的

						}
						
					}
					
				}
				
				
			}
			
			
			//存储数据库修改缓存
			if(roleDAO.updateTenRecruitHeroNum(roleInfo.getId(), num)){
				roleLoadInfo.setTenRecruitHeroNum(num);
			}
			
		}
		
		if(dropInfos == null || dropInfos.size() <= 0)
		{
			return ErrorCode.CHEST_CARD_ERROR_1;
		}
		//公告list
		List<DropInfo> gonggao = new ArrayList<DropInfo>();
		for(DropInfo info : dropInfos)
		{
			if(info != null && info.getItemNo() != null) 
			{
				if((info.getItemNo()).startsWith("350") || (info.getItemNo()).startsWith("360"))
				{
					gonggao.add(info);
				}
			}
		}
		if(subFreeProp){
			Map<Integer,Integer> delMap = new HashMap<Integer,Integer>();
			delMap.put(freePropXML.getNo(), 1);
			int result = ItemService.bagItemDel(action, roleInfo, delMap);
			if(result!=1){
				return result;
			}		
		}else{
			// 扣去消耗资源
			if (RoleService.subRoleResource(action, roleInfo, kind.getConditions() , null)) 
			{
				String updateSourceStr = RoleService.returnResourceChange(kind.getConditions());
				if(updateSourceStr != null)
				{
					String[] sourceStr = updateSourceStr.split(",");
					if(sourceStr != null && sourceStr.length > 1)
					{
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
			} 
			else 
			{
				return ErrorCode.RECRUIE_CUT_PROP_ERROR_4;
			}
		}

		// 保存物品
		List<Integer> heroIds = new ArrayList<Integer>();
		int save = ItemService.addPrize(action, roleInfo, dropInfos, null,
				null, null, null, null, heroIds, null, null, chestItemList, false);
		if (save != 1)
		{
			return save;
		}
		ItemService.prizeRefesh(roleInfo, null, null, null, heroIds, null);
		
		// 任务 红点
		boolean isRedQuest = QuestService.checkQuest(roleInfo, action, 1, true, false);
		boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, RedPointMgtService.LISTENING_CHEST_CHANGE_TYPES);
		
		if(isRedQuest || isRed){
			RedPointMgtService.pop(roleInfo.getId());
		}

		//公告
		sendChatMessage(roleInfo, gonggao); //获得特定物品消息推送
		
		// 随机打乱list顺序
		Collections.shuffle(chestItemList);
		
		if(kind.getNo() == 4 || kind.getNo() == 8){
			//金子十连抽的金子奖励
			RoleService.addRoleRoleResource(action, roleInfo, ConditionType.TYPE_MONEY, GameValue.TEN_CHEST_REWARD_MONEY,null);
		}
		
		return 1;
	}

	/**
	 * 获取奖励返回结果，并且剔除之前汇总进去的星石
	 * 
	 * @param dropInfo
	 * @param list
	 * @return
	 */
	public static List<ChestItemRe> getRecruits(DropInfo dropInfo, List<ChestItemRe> list) {
		if (dropInfo != null) 
		{
			ChestItemRe re = new ChestItemRe();
			//获得道具类型
			String no = String.valueOf(dropInfo.getItemNo());
			int itemType = 0;
			if (no.startsWith(GameValue.EQUIP_N0)) {
				itemType = ChestItemRe.TYPE_EQUIP;
			} else if (no.startsWith(GameValue.PROP_N0)) {
				itemType = ChestItemRe.TYPE_ITEM;
			}else if (no.startsWith(GameValue.WEAPAN_NO)) {
				itemType = ChestItemRe.TYPE_WEAP;
			}

			re.setItemType((byte) itemType);
			//道具记录
			re.setItemNo(Integer.parseInt(dropInfo.getItemNo()));
			re.setItemNum((byte) dropInfo.getItemNum());
			
			list.add(re);
		}
		return list;
	}
	
	/**
	 * 获取概率选中物品并验证物品
	 * 
	 * @param noStr
	 * @return
	 */
	private DropInfo getRecruitItemNo(String noStr)
	{
		//非英雄编号，正常计算获得的物品
		if (noStr.length() > 0 && !noStr.startsWith(GameValue.HERO_N0) && !noStr.startsWith(GameValue.EQUIP_N0) ) {
			//从xml计算
			List<RecruitItemXMLInfo> items = RecruitDepotXMLInfoMap.getItems(noStr);
			if (items != null) {
				int sumRand = 0;
				for (RecruitItemXMLInfo item : items) {
					item.setMinRand(sumRand);
					item.setMaxRand(sumRand + item.getRand());
					sumRand = item.getMaxRand();
				}
				int rand = RandomUtil.getRandom(0, sumRand);
				for (RecruitItemXMLInfo item : items) {
					if (item.getMinRand() <= rand && rand < item.getMaxRand()) {
						String itemNo = item.getItemNo();
						
						if(itemNo == null || "".equals(itemNo))
						{
							logger.error("######getRecruitItemNo is error,noStr="+noStr);
							continue;
						}

						if (AbstractConditionCheck.isResourceType(itemNo) || itemNo.startsWith(GameValue.EQUIP_N0)
								|| itemNo.startsWith(GameValue.PROP_N0) || itemNo.startsWith(GameValue.HERO_N0)
								|| itemNo.startsWith(GameValue.WEAPAN_NO)) {
							DropInfo dropInfo = new DropInfo();
							dropInfo.setItemNo(itemNo);
							dropInfo.setItemNum(item.getNum());
							dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
							return dropInfo;
						}
						return null;
					}
				}
			}
		} else if (noStr.startsWith(GameValue.HERO_N0) || noStr.startsWith(GameValue.EQUIP_N0)) {
			//直接获得
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(noStr);
			dropInfo.setItemNum(1);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			return dropInfo;
		}
		return null;
	}
	
	/**
	 * 十连抽
	 * @param no 第几次抽奖
	 * @param costType 1 - 银子 2 - 装备  3 - 武将
	 * @param roleType 主将ID 对应 Hero.xml
	 * @return
	 */
	public List<DropInfo> getFirstTenFixLotteryRecruit(int no, int costType, int roleType)
	{
		List<DropInfo> list = new ArrayList<DropInfo>();
		List<FixLotteryConfigInfo> xmlList = FixLotteryInfoMap.getFixLotteryInfoList(costType, roleType, no);
		if(xmlList == null || xmlList.size() == 0){
			return null;
		}
		
		Set<String> set = new HashSet<String>(); //用于保存costType为3的时候 没有重复的武将
		
		DropInfo dropInfo = null;
		for(FixLotteryConfigInfo info : xmlList){
			
			dropInfo = new DropInfo();
			if(!(info.getItemNo()+"").startsWith(GameValue.EQUIP_N0) && !(info.getItemNo()+"").startsWith(GameValue.PROP_N0)
					&&!(info.getItemNo()+"").startsWith(GameValue.WEAPAN_NO) && !(info.getItemNo()+"").startsWith(GameValue.HERO_N0))
			{
				//又是一个随机物品,扯蛋的功能
				if(costType == 3){
					//确保此次抽出的武将都是不重复的
					dropInfo = getRecruitItemNo(info.getItemNo()+"");
					if (dropInfo == null) {
						logger.error("#####this ten fix lotteryRecruit is error,no="+no+",costType="+costType+",roleType="+roleType);
						continue;
					}
					if(set.contains(dropInfo.getItemNo())){
						while(set.contains(dropInfo.getItemNo())){
							dropInfo = getRecruitItemNo(info.getItemNo()+"");
						}
					}
					
					set.add(dropInfo.getItemNo());
					
				}else{
					dropInfo = getRecruitItemNo(info.getItemNo()+"");
				}
				
			}
			else
			{
				dropInfo.setItemNo(info.getItemNo() + "");
				dropInfo.setItemNum(info.getNum());
			}
			
			list.add(dropInfo);
		}
		
		return list;
	}

	/**
	 * 系统广播抽到特定奖励
	 * @param roleInfo
	 * @param drops
	 */
	private void sendChatMessage(RoleInfo roleInfo, List<DropInfo> list) {
		if (list == null || list.size() <= 0) {
			return;
		}
		
		for (DropInfo re : list) {
			HeroXMLInfo info = null;
			EquipXMLInfo equipXMLInfo = null;
			StringBuilder builder = new StringBuilder();
			HeroXMLInfo heroxmlInfo = null;
			if(re == null || re.getItemNo() == null)
			{
				continue;
			}
			if((re.getItemNo()).startsWith("350")){
				//是武将
				int itemNo = Integer.parseInt(re.getItemNo());
				info = HeroXMLInfoMap.getHeroXMLInfo(itemNo);
				if(info == null){
					continue;
				}
				
				if(info.getXiyou() > 0){
					//稀有武将 牛B
					builder.append(Resource.getMessage("game", "RECRUIT_XIYOU_NOTICE_CONTENT"));
				}else{
					builder.append(Resource.getMessage("game", "RECRUIT_NOTICE_CONTENT"));
				}
				
				
				builder.append(",").append(roleInfo.getRoleName()).append(",").append(info.getName());
				GmccMgtService.sendChatMessage(builder.toString());
			
			}else if((re.getItemNo()).startsWith("360")){
				//装备
				equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(Integer.parseInt(re.getItemNo()));
				if (equipXMLInfo == null){
					continue;
				}
				
				if(equipXMLInfo.getCharacter() == 22 || equipXMLInfo.getCharacter() == 28){
					//高品质
					
					heroxmlInfo = HeroXMLInfoMap.getHeroXMLInfo(HeroInfoMap.getMainHeroInfo(roleInfo).getHeroNo());
					if(heroxmlInfo == null){
						continue;
					}
					
					builder.append(Resource.getMessage("game", "RECRUIT_EQUIP_NOTICE_CONTENT").toString());
					builder.append(",").append(roleInfo.getRoleName()).append(",").append(equipXMLInfo.getNo()).append("#").append(heroxmlInfo.getHeroType());
					
					GmccMgtService.sendChatMessage(builder.toString());
				}
				
			}
			
		}
	}

}
