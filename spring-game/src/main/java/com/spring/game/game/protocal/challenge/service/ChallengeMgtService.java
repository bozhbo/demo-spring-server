package com.snail.webgame.game.protocal.challenge.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.AbstractConditionCheck.BaseSubResource;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.ChallengeUpdateInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.info.StoreItemInfo;
import com.snail.webgame.game.protocal.challenge.fightNum.FightNumReq;
import com.snail.webgame.game.protocal.challenge.fightNum.FightNumResp;
import com.snail.webgame.game.protocal.challenge.getprize.GetPrizeReq;
import com.snail.webgame.game.protocal.challenge.getprize.GetPrizeResp;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.QueryBattleDetailResp;
import com.snail.webgame.game.protocal.challenge.sweep.SweepReq;
import com.snail.webgame.game.protocal.challenge.sweep.SweepResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.store.service.StoreService;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.ChallengeResetXmlInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.ChapterInfo;
import com.snail.webgame.game.xml.info.ChallengeResetXMLInfo;
import com.snail.webgame.game.xml.info.DropXMLInfo;

public class ChallengeMgtService {
	/**
	 * 查询玩家已通过的副本及可打的副本
	 * @param roleId
	 * @return
	 */
	public QueryBattleDetailResp queryDetail(int roleId) {
		QueryBattleDetailResp resp = new QueryBattleDetailResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_12);
			return resp;
		}
		synchronized (roleInfo) {

			List<BattleDetailRe> list = new ArrayList<BattleDetailRe>();
			List<String> newList =  new ArrayList<String>();

			List<ChallengeUpdateInfo> updateList = new ArrayList<ChallengeUpdateInfo>();
			
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			// 玩家可打的副本信息
			List<BattleDetail> details = ChallengeBattleXmlInfoMap.getTotals();
			if (details != null) {
				for (BattleDetail detail : details) {
					ChallengeUpdateInfo updateInfo = new ChallengeUpdateInfo();
					if (mainHero == null || mainHero.getHeroLevel() < detail.getUnLockLv()) {
						continue;
					}
					//获得玩家副本信息
					ChallengeBattleInfo info = ChallengeBattleInfoMap.getBattleInfo(roleId, (byte)detail.getChapterType(), detail.getChapterNo(), detail.getBattleNo());
					if (info != null) {
						//已攻打副本
						ChallengeService.oldChallengeAdd(roleInfo, list, info, detail,updateInfo);
						if(updateInfo.getId() > 0)
						{
							updateList.add(updateInfo);
						}
					} else {
						//新副本（判断是否可攻打）
						if(roleInfo.getRoleLoadInfo().getChallengeOpen() != 1){
							int condCheck = AbstractConditionCheck.checkCondition(roleInfo,detail.getConds());
							if (condCheck == 1) {
								ChallengeService.newChallengeAdd(roleInfo, list, detail);
								newList.add(detail.getBattleNo()+"");
							}
						} else {
							ChallengeService.newChallengeAdd(roleInfo, list, detail);
							newList.add(detail.getBattleNo()+"");
						}
					}
				}
				
				if(updateList.size() > 0)
				{
					ChallengeBattleDAO.getInstance().updateChallengeAttackNumBatch(updateList);
				}
			}

			resp.setResult(1);
			resp.setList(list);
			resp.setCount(list.size());
			return resp;
		}
	}

	/**
	 * 副本扫荡
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public SweepResp sweepChapter(int roleId, SweepReq req) {
		//扫荡的副本
		byte challengeType = req.getChapterType();
		int chapterNo = req.getChapterNo();
		int battleNo = req.getBattleId();
		//扫荡次数
		int times = req.getTimes();

		SweepResp resp = new SweepResp();
		
		if(GameValue.GAME_CHALLENGE_OPEN != 1){
			resp.setResult(ErrorCode.CHALLENGE_ERROR_9);
			return resp;
		}
		if (times <= 0 || times > 5) {
			resp.setResult(ErrorCode.CHALL_ENGE_ERROR_16);
			return resp;
		}
		if (chapterNo <= 0 || battleNo <= 0) {
			resp.setResult(ErrorCode.CHALL_ENGE_ERROR_17);
			return resp;
		}


		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CHALL_ENGE_ERROR_18);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.CHALL_ENGE_ERROR_18);
			return resp;
		}

		synchronized (roleInfo) {
			// 副本xml信息
			ChallengeBattleXmlInfo challengeXmlInfo = ChallengeBattleXmlInfoMap.getInfoByNo(challengeType);
			if (challengeXmlInfo == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_19);
				return resp;
			}
			// 副本章节信息
			ChapterInfo chapterXmlInfo = challengeXmlInfo.getChapterInfoByNo(chapterNo);
			if (chapterXmlInfo == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_20);
				return resp;
			}
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(checkItem);
				return resp;
			}

			// 判断战场星级是否可以扫荡
			ChallengeBattleInfo battle = ChallengeBattleInfoMap.getBattleInfo(roleId, challengeType, chapterNo,
					battleNo);
			if (battle == null) 
			{
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_22);
				return resp;
			}
			
			if(times > 1 && VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.WLSD) < 1)
			{
				//五连扫只有VIP才能开启
				resp.setResult(ErrorCode.CHALL_SWAPP_ERROR_1);
				return resp;
			}
			else if(battle.getChallengeType() == 1 && battle.getStars() < GameValue.CAN_SWEEP_STAR_NUM && VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.CHALLENGE_SWEEP) < 1)
			{
				// 剧情本-VIP等级足够时无视副本星级可直接开启扫荡
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_28);
				return resp;
			}
			else if(battle.getChallengeType() == 2 && battle.getStars() < GameValue.CAN_SWEEP_STAR_NUM && VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.YSFBSD) < 1)
			{
				// 英雄本-VIP等级足够时无视副本星级可直接开启扫荡
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_28);
				return resp;
			}
			
			int action = ActionType.action104.getType();
			if(battle.getChallengeType() == ChallengeBattleXmlInfo.TYPE_NO_2){
				action = ActionType.action106.getType();
			}
			
			//判断次数
			int canFightNum = battle.getCanFightNum();
			if(canFightNum == 0){
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_24);
				return resp;
			}
			
			if(canFightNum < times){
				times = canFightNum;
			}

			// 战场信息
			BattleDetail battleDetail = chapterXmlInfo.getBattleDetail(battleNo);
			if (battleDetail == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_21);
				return resp;
			}
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			if (battleDetail.getConds() != null) {
				conds.addAll(battleDetail.getConds());
			}
			
			// 因为现在体力不是实时计算的，所以每用到体力的一刻一定需要把玩家之前时间回复的先加上
			if (roleInfo.getSp() < roleInfo.getSpRecoverLimit()) {
				RoleService.timerRecoverSp(roleInfo);
			}
			
			//体力判断
			BaseSubResource sub = AbstractConditionCheck.subCondition(battleDetail.getConds2());
			int sp = sub.upSp*times;
			
			if(roleInfo.getSp() < sp){
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_23);
				return resp;
			}
			
			// 判断是否可以打
			int condCheck = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (condCheck != 1) {
				resp.setResult(condCheck);
				return resp;
			}
			

			// 扣除消耗
			if(!RoleService.subRoleRoleResource(action, roleInfo, ConditionType.TYPE_SP, sp , null)){
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_23);
				return resp;
			}
			//记录副本信息
			if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(battle.getId(),
				battle.getCanFightNum() - times, new Timestamp(System.currentTimeMillis()))) {
				battle.setCanFightNum(battle.getCanFightNum() - times);
				battle.setFightTime(new Timestamp(System.currentTimeMillis()));
			}
			
			//获得奖励
			List<DropXMLInfo> drops = new ArrayList<DropXMLInfo>();
			
			if(battleDetail.getBag() != null && battleDetail.getBag().length() > 0){
				List<DropXMLInfo> drop1 = PropBagXMLMap.getPropBagXMLList(battleDetail.getBag());
				if(drop1 != null && drop1.size() > 0){
					drops.addAll(drop1);
				}
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if (heroInfo == null) {
				resp.setResult(ErrorCode.ADD_ERP_ERROR_1);
				return resp;
			}
			
			int oldLv = heroInfo.getHeroLevel();
			List<BattlePrize> prizeList = new ArrayList<BattlePrize>();
			List<DropInfo> allPrize = new ArrayList<DropInfo>();//奖励
			String common = "";
			//必掉物品
			String itemNo = battleDetail.getItemNo();

			for(int i=1;i<=times;i++)
			{
				List<DropXMLInfo>  prizesList = new ArrayList<DropXMLInfo>();
				//处理经验掉落
				int level = heroInfo.getHeroLevel();
				int addExp = 0;
				int pzMaxLv = HeroXMLInfoMap.getMaxMainLv();
				if(level < pzMaxLv)
				{
					if(battleDetail.getChapterType() == 1){
						addExp = level*GameValue.EXP_VALUE + GameValue.EXP_ADD;
						DropXMLInfo dropXmlInfo =  new DropXMLInfo();
						dropXmlInfo.setItemNo(ConditionType.TYPE_EXP.getName());
						dropXmlInfo.setItemMinNum(addExp);
						dropXmlInfo.setItemMaxNum(addExp);
						dropXmlInfo.setMinRand(1);
						dropXmlInfo.setMaxRand(10000);
						prizesList.add(dropXmlInfo);
					} else if(battleDetail.getChapterType() == 2) {
						addExp = level*GameValue.EXP_VALUE_1 + GameValue.EXP_ADD_1;
						DropXMLInfo dropXmlInfo =  new DropXMLInfo();
						dropXmlInfo.setItemNo(ConditionType.TYPE_EXP.getName());
						dropXmlInfo.setItemMinNum(addExp);
						dropXmlInfo.setItemMaxNum(addExp);
						dropXmlInfo.setMinRand(1);
						dropXmlInfo.setMaxRand(10000);
						prizesList.add(dropXmlInfo);
					}
				}
				prizesList.addAll(drops);
				List<DropInfo> addList = new ArrayList<DropInfo>();//奖励
				
				// 计算获取的奖励
				ItemService.getDropXMLInfo(roleInfo, prizesList, addList, (byte)i);
				List<DropInfo> addList1 = ChallengeService.checkDrop(battleDetail, (byte)i);
				addList.addAll(addList1);
				
				//计算翻牌
				List<BattlePrize> fpPrizeList = new ArrayList<BattlePrize>();
				String cardBag = "";
				if(battleDetail.getCardBag() != null && battleDetail.getCardBag().length() > 0)
				{
					cardBag = battleDetail.getCardBag();
				}
				if(cardBag != null && cardBag.length() > 0)
				{
					//翻牌操作
					ItemService.getFpPrizeList(cardBag, fpPrizeList);
				}

				if(fpPrizeList.size() > 0)
				{
					// 增加翻牌奖励
					DropInfo fpDropInfo  = new DropInfo(fpPrizeList.get(0).getNo(), fpPrizeList.get(0).getNum());
					fpDropInfo.setItemType(ItemService.checkItemType(fpDropInfo.getItemNo()));
					fpDropInfo.setSweep((byte)i);
					if(fpDropInfo != null)
					{
						boolean newItem = true;
						for(int a = 0 ; a < addList.size() ; a++)
						{
							DropInfo dropInfo1 = addList.get(a);
							if(dropInfo1 != null && dropInfo1.getItemNo().equalsIgnoreCase(fpDropInfo.getItemNo()))
							{
								dropInfo1.setItemNum(dropInfo1.getItemNum() + fpDropInfo.getItemNum());
								newItem = false;
								break;
							}
						}
						
						if(newItem)
						{
							addList.add(fpDropInfo);
						}
					}
				}
				
				
				//必出奖励获取计算
				if(itemNo != null && itemNo.length() > 0)
				{
					int itemCount = battleDetail.getItemNo().split(",").length;
					String item = "";
					boolean flag1 = false;
					List<String> dropItemNo = battle.getItemByNo();
					
					if(battle.getItemByNum() == (GameValue.CHALLENGE_ITEM_BY-1))
					{
						if(dropItemNo == null) //一件都没掉
						{
							item = itemNo;
							flag1 = true;
						}
						else if(dropItemNo != null && itemCount > dropItemNo.size())
						{
							for(String a : dropItemNo)
							{
								itemNo = itemNo.replaceAll(a, "");
							}
							item = itemNo;
							flag1 = true;
						}
					}
					
					//掉落必掉物品
					if(flag1)
					{
						//必掉物品所在的bag
						List<DropXMLInfo> bagList = PropBagXMLMap.getPropBagXMLList(battleDetail.getBagView());
						
						Iterator<DropInfo> iter = addList.iterator();
						while(iter.hasNext())
						{
							DropInfo dropInfo = iter.next();
							if(item.contains(dropInfo.getItemNo() + ","))
							{
								iter.remove();
							}
						}
						
						for(String a : item.split(","))
						{
							DropInfo dropInfo = new DropInfo();
							int num = 1;
							boolean isFind = false;
							for(DropXMLInfo info : bagList)
							{
								if(info.getItemNo().equals(a))
								{
									isFind = true;
									num = RandomUtil.getRandom(info.getItemMinNum(), info.getItemMaxNum());

									dropInfo.setItemNo(info.getItemNo());
									dropInfo.setItemNum(num);
									dropInfo.setItemType(ItemService.checkItemType(info.getItemNo()));

									break;
								}
							}
							if(!isFind)
							{
								dropInfo.setItemNo(a);
								dropInfo.setItemNum(num);
								dropInfo.setItemType(ItemService.checkItemType(a));
							}
							addList.add(dropInfo);
						}
					}
				}
				
				//检测是否掉落必掉品
				if(battleDetail.getItemNo() != null && battleDetail.getItemNo().length() > 0){
					//必掉物品计数
					battle.setItemByNum(battle.getItemByNum() + 1);
					if(battle.getItemByNum() >= GameValue.CHALLENGE_ITEM_NUM){
						battle.setItemByNum(0);
						battle.getItemByNo().clear();
					}
					if(addList != null && addList.size() > 0){
						//已记录的必掉品
						List<String> itemNoList = battle.getItemByNo();
						
						//已掉落必掉品，添加标记
						if(battle.getItemByNum() <= GameValue.CHALLENGE_ITEM_BY){
							for(DropInfo prize : addList){
								String prizeNo = prize.getItemNo()+",";
								if(battleDetail.getItemNo().contains(prizeNo) && !itemNoList.contains(prizeNo)){
									itemNoList.add(prizeNo);
								}
							}
						}
					}
				}
				
				if(addList != null && addList.size() > 0){
					allPrize.addAll(addList);
				}	
			}

			
			Map<String, Integer> getResourceNum = new HashMap<String, Integer>();
			Map<Integer, Integer> getItemNum = new HashMap<Integer, Integer>();
			Map<Integer, Integer> addEquipIds = new HashMap<Integer, Integer>();
			List<Integer> heroIds = new ArrayList<Integer>();
			List<RoleWeaponInfo> weapList = new ArrayList<RoleWeaponInfo>();
			
			int result = ItemService.addPrize(action, roleInfo, allPrize, null, 
					getResourceNum, getItemNum,
					addEquipIds, weapList, heroIds, 
					prizeList, null, null, false);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			if(oldLv < heroInfo.getHeroLevel())
			{
				common = "lvUp";
			}
			
			ItemService.prizeRefesh(roleInfo, getResourceNum, getItemNum, addEquipIds, heroIds, weapList);
			
			if (prizeList != null && prizeList.size() > 0) {
				resp.setPrizeNum(prizeList.size());
				resp.setPrize(prizeList);
			}
			
			
			//判断是否刷新商店
			int goldShop = 0;
			int turkShop = 0;
			int refreshShop = 0;
			
			//判断之前先更新商店状态
			long now = System.currentTimeMillis();
			Timestamp goldTime = roleLoadInfo.getRefreshGoldShopTime();
			Timestamp turkTime = roleLoadInfo.getRefreshTurkShopTime();
			int refreshGoldShop = roleLoadInfo.getIsRefreshGoldShop();
			int refreshTurkShop = roleLoadInfo.getIsRefreshTurkShop();
			
			if(refreshGoldShop == 1)
			{
				if(goldTime != null && now > goldTime.getTime()){
					if(RoleDAO.getInstance().shop(roleId, 0, 2,null))
					{
						roleLoadInfo.setIsRefreshGoldShop(0);
						roleLoadInfo.setRefreshGoldShopTime(null);
					}
				}
			}
			if( refreshTurkShop == 1)
			{
				if(turkTime != null && now > turkTime.getTime())
				{
					if(RoleDAO.getInstance().shop(roleId, 0, 4,null))
					{
						roleLoadInfo.setIsRefreshTurkShop(0);
						roleLoadInfo.setRefreshTurkShopTime(null);
					}
				}
			}
			//开始判断
			//黑市商店
			if(roleLoadInfo.getIsBuyGoldShopForVip() == 0 && roleLoadInfo.getIsRefreshGoldShop() != 1)
			{
				if(oldLv ==  heroInfo.getHeroLevel() //本次不升级才触发
						&& heroInfo.getHeroLevel() >= GameValue.BLACK_SHOP_OPEN_LV
						&&StoreService.refreshShop(1))
				{
					Timestamp refreshTime = new Timestamp(System.currentTimeMillis() + (60 * 60 *1000));
					StoreService.refreshStoreItem(roleInfo, StoreItemInfo.STORE_TYPE_7);
					//刷新黑市商店
					if(RoleDAO.getInstance().shop(roleId, 1, 2 ,refreshTime))
					{
						roleLoadInfo.setIsRefreshGoldShop(1);
						roleLoadInfo.setRefreshGoldShopTime(refreshTime);
						//通知客户端 黑市商店已刷新
						goldShop = 1;
					}
				}
			}
			//异域商店
			if(roleLoadInfo.getIsBuyTurkShopForVip() == 0 && roleLoadInfo.getIsRefreshTurkShop() != 1)
			{
				if(oldLv ==  heroInfo.getHeroLevel() //本次不升级才触发
						&& heroInfo.getHeroLevel() >= GameValue.TURK_SHOP_OPEN_LV
						&&StoreService.refreshShop(2))
				{
					Timestamp refreshTime = new Timestamp(System.currentTimeMillis() + (60 * 60 *1000));
					StoreService.refreshStoreItem(roleInfo, StoreItemInfo.STORE_TYPE_9);
					//刷新异域商店
					if(RoleDAO.getInstance().shop(roleId, 1, 4,refreshTime))
					{
						roleLoadInfo.setIsRefreshTurkShop(1);
						roleLoadInfo.setRefreshTurkShopTime(refreshTime);
						//通知客户端 异域商店已刷新
						turkShop = 1;
					}
				}
			}
			if(goldShop == 1 && turkShop == 0)
			{
				refreshShop =1;
			}
			else if(goldShop == 0 && turkShop == 1)
			{
				refreshShop =2;
			}
			else if(goldShop == 1 && turkShop == 1)
			{
				refreshShop =3;
			}
			//== 商店 END
			
			// 任务检测
			battleDetail.setSweepNum(times);
			boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action104.getType(), battleDetail, true, true);
			boolean isRedPoint = RedPointMgtService.check2PopRedPoint(roleId, common, false, RedPointMgtService.LISTENING_CHALLENGE_FIGHT_END);
			//红点推送
			if(isRedPointQuest || isRedPoint){
				RedPointMgtService.pop(roleInfo.getId());
			}
			
			resp.setChapterType((byte)challengeType);
			resp.setChapterNo(battle.getChapterNo());
			resp.setBattleNo(battleNo);
			resp.setCanFightNum(battle.getCanFightNum());
			resp.setSweep(times);
			resp.setRefreshShop(refreshShop);
			resp.setResult(1);
			GameLogService.insertPlayActionLog(roleInfo, action, battleNo+"");
		}
		return resp;
	}

	/**
	 * 领取宝箱
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetPrizeResp getPrize(int roleId, GetPrizeReq req) {
		byte challengeType = req.getChapterType();
		int chapterNo = req.getChapterNo();
		int prizeNo1 = (int)req.getPrizeNo(); //领取的宝箱编号
		
		GetPrizeResp resp = new GetPrizeResp();
		
		if(GameValue.GAME_CHALLENGE_OPEN != 1){
			resp.setResult(ErrorCode.CHALLENGE_ERROR_9);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_5);
			return resp;
		}

		synchronized (roleInfo) {
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_26);
				return resp;
			}
			
			// 副本xml信息
			ChallengeBattleXmlInfo challengeXmlInfo = ChallengeBattleXmlInfoMap.getInfoByNo(challengeType);
			if (challengeXmlInfo == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_2);
				return resp;
			}

			// 副本章节信息
			ChapterInfo chapterXmlInfo = challengeXmlInfo.getChapterInfoByNo(chapterNo);
			if (chapterXmlInfo == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_2);
				return resp;
			}
			
			if(prizeNo1 == 1){
				 if(chapterXmlInfo.getChest1() == null ||  chapterXmlInfo.getChest1().length() == 0){
						resp.setResult(ErrorCode.CHALL_ENGE_ERROR_5);
						return resp; 
				 }
			} else if (prizeNo1 == 2) {
				 if(chapterXmlInfo.getChest2() == null ||  chapterXmlInfo.getChest2().length() == 0){
						resp.setResult(ErrorCode.CHALL_ENGE_ERROR_5);
						return resp; 
				 }
			}  else if (prizeNo1 == 3) {
				 if(chapterXmlInfo.getChest3() == null ||  chapterXmlInfo.getChest3().length() == 0){
						resp.setResult(ErrorCode.CHALL_ENGE_ERROR_5);
						return resp; 
				 }
			} else {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_5);
				return resp; 
			}
			
			Map<Integer, BattleDetail> battleMap = ChallengeBattleXmlInfoMap.getBattleChapter(challengeType, chapterNo);
			if(battleMap == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_2);
				return resp;
			}

			// 章节战斗记录
			Map<Integer, ChallengeBattleInfo> roleChallengeBattleInfos = ChallengeBattleInfoMap.getByRoleIdAndTypeNoAndChapter(roleId, challengeType, chapterNo);
			if (roleChallengeBattleInfos == null) {
				resp.setResult(ErrorCode.CHALL_ENGE_ERROR_4);
				return resp;
			}

			int hasGetStar = 0;//拥有的总星数
			String bag = "";
			for (ChallengeBattleInfo battleInfo : roleChallengeBattleInfos.values()) {
				hasGetStar += battleInfo.getStars();
			}

			//判断星数
			if (prizeNo1 == 1 && hasGetStar >= chapterXmlInfo.getStar1()) {
				bag = chapterXmlInfo.getChest1();
			} else if (prizeNo1 == 2 && hasGetStar >= chapterXmlInfo.getStar2()) {
				bag = chapterXmlInfo.getChest2();
			} else if (prizeNo1 == 3 && hasGetStar >= chapterXmlInfo.getStar3()) {
				bag = chapterXmlInfo.getChest3();
			} else {
				resp.setResult(ErrorCode.ROLE_FB_ERROR_5);
				return resp;
			}
			if (bag.length() <= 0) {
				resp.setResult(ErrorCode.ROLE_FB_ERROR_6);
				return resp;
			}

			
			// 获取领奖信息
			Map<Integer, List<Integer>> prizeMap = ChallengeService.getPrizeMap(roleInfo, challengeType);
			List<Integer> prizeNos = prizeMap.get(chapterNo);
			if (prizeNos == null) {
				prizeNos = new ArrayList<Integer>();
				prizeMap.put(chapterNo, prizeNos);
			}
			if (prizeNos.contains(prizeNo1)) {
				resp.setResult(ErrorCode.ROLE_FB_ERROR_4);
				return resp;
			}

			if (bag.length() > 0) {
				
				// 记录领取的奖励
				prizeNos.add(prizeNo1);
				String challengePrize = ChallengeService.getPrizeStr(prizeMap);
				if(challengeType == 1){
					// 标记奖励已经领取
					if (RoleDAO.getInstance().updateChallengePrize(roleId, challengePrize)) {
						// 标记奖励已经领取
						roleLoadInfo.setChallengePrize(challengePrize);
					} else {
						resp.setResult(ErrorCode.CHALL_ENGE_ERROR_3);
						return resp;
					}
				} else if(challengeType == 2){
					// 标记奖励已经领取
					if (RoleDAO.getInstance().updateChallengePrize2(roleId, challengePrize)) {
						// 标记奖励已经领取
						roleLoadInfo.setChallengePrize2(challengePrize);
					} else {
						resp.setResult(ErrorCode.CHALL_ENGE_ERROR_3);
						return resp;
					}
				}
				
				// 领取奖励
				List<BattlePrize> prizeList = new ArrayList<BattlePrize>();
				List<DropXMLInfo> propBagList = PropBagXMLMap.getPropBagXMLListbyStr(bag);
				if (propBagList != null && propBagList.size() > 0) {
					int result = ItemService.addPrizeForPropBag(ActionType.action102.getType(), roleInfo,propBagList, null,
							prizeList, null,null, null, false);
					
					if (result != 1) {
						resp.setResult(result);
						return resp;
					}

				} else {
					resp.setResult(ErrorCode.ROLE_FB_ERROR_3);
					return resp;
				}

				// 输出
				resp.setNum(prizeList.size());
				resp.setList(prizeList);
				// 记录已领取的宝箱
				resp.setChapterDetailRe(ChallengeService.getChapterDetailRe((byte)challengeType, chapterNo, prizeNos));
			} else {
				resp.setResult(ErrorCode.ROLE_FB_ERROR_2);
				return resp;
			}
			//判断新手引导是否要更新
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_GET_PRIZE);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}
		}
		
		boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action102.getType(), null, true, false);
		boolean isRedPoint = RedPointMgtService.check2PopRedPoint(roleId, null, false, RedPointMgtService.LISTENING_CHALLENGE_PRIZE);
		//红点推送
		if(isRedPointQuest || isRedPoint){
			RedPointMgtService.pop(roleInfo.getId());
		}
		
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action102.getType(), req.getChapterType()+","+req.getChapterNo()+","+req.getPrizeNo());
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 金币购买副本挑战次数
	 * @param roleId
	 * @param req
	 * @return
	 */
	public FightNumResp goleBuy(int roleId, FightNumReq req) {
		FightNumResp resp = new FightNumResp();
		byte challengeType = req.getChapterType();
		int chapterNo = req.getChapterNo();
		int battleNo = req.getBattleNo();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_5);
			return resp;
		}
		synchronized (roleInfo){
			int gold = 0;
			//获得副本xml
			BattleDetail battleDetail = ChallengeBattleXmlInfoMap.getBattleDetail(challengeType, chapterNo, battleNo);
			if(battleDetail != null){
				int fightNum = battleDetail.getBattleNum();//挑战次数
				int goldNum = 0;//金币数量
				ChallengeBattleInfo battleInfo = ChallengeBattleInfoMap.getBattleInfo(roleId, challengeType, chapterNo, battleNo);
				if(battleInfo != null){
					if(battleInfo.getCanFightNum() > 0){
						resp.setResult(ErrorCode.CHALLENGE_BUY_ERROR_4);
						return resp;
					}
					goldNum = battleInfo.getGoldNum()+1;
					//英雄本-VIP计算可购买次数
					if(battleDetail.getChapterType() == 2)
					{
						int num = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.HERO_FB);
						if(goldNum > num)
						{
							resp.setResult(ErrorCode.CHALLENGE_BUY_ERROR_3);
							return resp;
						}
					}
					ChallengeResetXMLInfo info = ChallengeResetXmlInfoMap.getInfoByNo(challengeType, goldNum);
					if(info != null){
						gold = info.getGold();
						if (gold > roleInfo.getCoin()) {
							resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
							return resp;
						}
					} else {
						resp.setResult(ErrorCode.CHALLENGE_BUY_ERROR_3);
						return resp;
					}
					// 扣除消耗
					if (RoleService.subRoleRoleResource(ActionType.action105.getType(), roleInfo, ConditionType.TYPE_COIN, gold , null)) {
						resp.setSourceType((byte)ConditionType.TYPE_COIN.getType());
						resp.setSourceChange((int)-gold);
						//SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
					} else {
						resp.setResult(ErrorCode.SUB_RESOURCE_ERROR_1);
						return resp;
					}
					//记录副本信息
					if (ChallengeBattleDAO.getInstance().updateChallengeTimes(battleInfo.getId(),fightNum)) {
						battleInfo.setCanFightNum(fightNum);
					}
					if(ChallengeBattleDAO.getInstance().updateGoldBuy(battleInfo.getId(), goldNum)){
						battleInfo.setGoldNum(goldNum);
					}
					resp.setGoldNum((byte)goldNum);
					resp.setResult(1);
				} else {
					resp.setResult(ErrorCode.CHALLENGE_BUY_ERROR_1);
					return resp;
				}
			} else {
				resp.setResult(ErrorCode.CHALLENGE_BUY_ERROR_2);
				return resp;
			}
		}
		return resp;
	}
	
}
