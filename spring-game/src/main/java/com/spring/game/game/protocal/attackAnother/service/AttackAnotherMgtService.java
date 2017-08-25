package com.snail.webgame.game.protocal.attackAnother.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.FightDeployInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.GamePVELog;
import com.snail.webgame.game.protocal.attackAnother.match.AttackAnotherMatchReq;
import com.snail.webgame.game.protocal.attackAnother.match.AttackAnotherMatchResp;
import com.snail.webgame.game.protocal.attackAnother.sweep.AttackAnotherSweepReq;
import com.snail.webgame.game.protocal.attackAnother.sweep.AttackAnotherSweepResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.xml.cache.AttackAnotherXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.AttackAnotherXMLInfo;
import com.snail.webgame.game.xml.info.DropXMLInfo;

/**
 * @author wanglinhui
 *
 */
public class AttackAnotherMgtService {
	//private static GameLogDAO gameLogDAO = GameLogDAO.getInstance();
	/**
	 * 狭路相逢匹配
	 * @param roleId
	 * @return  AttackAnotherMatchResp
	 */
	public AttackAnotherMatchResp attackAnotherMatch(int roleId, AttackAnotherMatchReq req){
		
		AttackAnotherMatchResp resp = new AttackAnotherMatchResp();
		
		int attType = req.getAttType();

		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_1);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null)
		{
			resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_1);
			return resp;
		}
		if(GameValue.ATTACK_ANOTHER_SWITCH!=1){//功能开发
			resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_4);
			return resp;
		}
		if(FightInfoMap.getFightInfoByRoleId(roleId)!=null){
			resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_14);
			return resp;
		}
		int XLXFNum = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXFDB_NUM)+GameValue.ATTACK_ANOTHER_EVERY_DAY_TIME;
		if(roleLoadInfo.getLastAttackAnotherTime()==null){//没玩过对攻
			resp.setRemainTime((byte)XLXFNum);
		}else{
			if(!DateUtil.isSameDay(roleLoadInfo.getLastAttackAnotherTime().getTime(), System.currentTimeMillis())&&
					roleLoadInfo.getAttackAnotherTime()>0){
				//隔天了并且有玩的次数就重置玩的次数
				synchronized (roleInfo) {
					Timestamp now = new Timestamp(System.currentTimeMillis());
					if(RoleDAO.getInstance().updateRoleAttackAnotherInfo(roleId, (byte)0, now)){
						roleLoadInfo.setAttackAnotherTime((byte)0);
						roleLoadInfo.setLastAttackAnotherTime(new Timestamp(System.currentTimeMillis()));
						
					}else{
						resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_5);
						return resp;
					}
				}
			}
			resp.setRemainTime((byte)(XLXFNum - roleLoadInfo.getAttackAnotherTime()));
		}
		
		if(roleInfo.getFightValue()==0){
			resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_3);
			return resp;
		}
		RoleInfo sideRoleInfo = null;
		List<RankInfo> sortFightValuelist = RankService.getFightValueRank();
		if(sortFightValuelist.size() <= 0)
		{
			resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_17);
			return resp;
		}
		List<RoleInfo> tempList = new ArrayList<RoleInfo>();
		synchronized (sortFightValuelist) {
			long startTime = System.currentTimeMillis();
			int roleIndex = roleInfo.getRankFightValue();// 玩家在战斗力列表中的索引
			int fightValue = roleInfo.getFightValue();
			if (roleIndex < 0) {
				resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_15);
				return resp;
			}
			RankInfo tempRoleRank;
			RoleInfo tempRoleInfo;
			
			int minValue = 0;
			int maxValue = 0;
			//根据难度获取相对应的随机玩家
			//计算出区间
			if(attType == 1) //简单 比自己战斗力低很多的
			{
				minValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_EASY_DOWN);
				maxValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_EASY_UPP);
				
				int minPlace = RankService.getRoleIndexbyShellSort(minValue) + 200;
				minPlace = minPlace >= sortFightValuelist.size() ? sortFightValuelist.size() : minPlace;
				int maxPlace = RankService.getRoleIndexbyShellSort(maxValue)-200;
				maxPlace = (maxPlace < 0) ? 0 : maxPlace; 
				
				//取比玩家战斗力低的
				// (从高战力开始取)
				for(int place = maxPlace ; place <= minPlace ; place++)
				{
					if(place < 0 || place >= sortFightValuelist.size())
					{
						break;
					}
					
					tempRoleRank = sortFightValuelist.get(place);
					if(tempRoleRank == null){
						continue;
					}
					tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
					if(tempRoleInfo == null){
						continue;
					}
					if(tempList.contains(tempRoleInfo))
					{
						continue;
					}
					if(tempRoleInfo.getId() == roleInfo.getId())
					{
						continue;
					}
					if(tempRoleInfo.getFightValue() < maxValue && tempRoleInfo.getFightValue() > minValue)
					{
						tempList.add(tempRoleInfo);
					}
					if(tempRoleInfo.getFightValue() < minValue && tempList.size() < 1)
					{
						tempList.add(tempRoleInfo);
					}
					if(tempList.size() >= 40)
					{
						break;
					}
				}
			}
			else if(attType == 2) //普通 战斗力差不多的
			{
				minValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_GENERAL_DOWN);
				maxValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_GENERAL_UPP);
				
				// 取比玩家战斗力低的25个人和战斗力高的25个人
				int minPlace = RankService.getRoleIndexbyShellSort(minValue)+200;
				minPlace = minPlace >= sortFightValuelist.size() ? sortFightValuelist.size() : minPlace;
				int maxPlace = RankService.getRoleIndexbyShellSort(maxValue)-200;
				maxPlace = (maxPlace < 0) ? 0 : maxPlace;
				// (从低战力开始取)
				for(int place = minPlace ; place >= maxPlace ; place--)
				{
					if(place < 0 || place >= sortFightValuelist.size())
					{
						break;
					}
					
					tempRoleRank = sortFightValuelist.get(place);
					if(tempRoleRank == null){
						continue;
					}
					tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
					if(tempRoleInfo == null){
						continue;
					}
					if(tempList.contains(tempRoleInfo))
					{
						continue;
					}
					if(tempRoleInfo.getId() == roleInfo.getId())
					{
						continue;
					}
					if(tempRoleInfo.getFightValue() < maxValue && tempRoleInfo.getFightValue() > minValue)
					{
						tempList.add(tempRoleInfo);
					}
					
					if(tempList.size() >= 20)
					{
						break;
					}
				}
				// (从高战力开始取)
				for(int place = maxPlace ; place <= minPlace ; place++)
				{
					if(place < 0 || place >= sortFightValuelist.size())
					{
						break;
					}
					
					tempRoleRank = sortFightValuelist.get(place);
					if(tempRoleRank == null){
						continue;
					}
					tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
					if(tempRoleInfo == null){
						continue;
					}
					if(tempList.contains(tempRoleInfo))
					{
						continue;
					}
					if(tempRoleInfo.getId() == roleInfo.getId())
					{
						continue;
					}
					if(tempRoleInfo.getFightValue() < maxValue && tempRoleInfo.getFightValue() > minValue)
					{
						tempList.add(tempRoleInfo);
					}
					
					if(tempList.size() >= 40)
					{
						break;
					}
				}
			}
			else if(attType == 3) //精英 战斗力高很多的
			{
				minValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_DIFFICUTL_DOWN);
				maxValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_DIFFICUTL_UPP);
				
				int minPlace = RankService.getRoleIndexbyShellSort(minValue) + 200;
				minPlace = minPlace >= sortFightValuelist.size() ? sortFightValuelist.size() : minPlace;
				int maxPlace = RankService.getRoleIndexbyShellSort(maxValue)-200;
				maxPlace = (maxPlace < 0) ? 0 : maxPlace; 
				
				//比自己战斗力高的取50个(从高战力开始取)
				for(int place = maxPlace ; place <= minPlace ; place++)
				{
					if(place < 0 || place >= sortFightValuelist.size())
					{
						break;
					}
					
					tempRoleRank = sortFightValuelist.get(place);
					if(tempRoleRank == null){
						continue;
					}
					tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
					if(tempRoleInfo == null){
						continue;
					}
					if(tempList.contains(tempRoleInfo))
					{
						continue;
					}
					if(tempRoleInfo.getId() == roleInfo.getId())
					{
						continue;
					}
					if(tempRoleInfo.getFightValue() < maxValue && tempRoleInfo.getFightValue() > minValue)
					{
						tempList.add(tempRoleInfo);
					}
					if(tempRoleRank.getFightValue() < minValue && tempList.size() < 1)
					{
						tempList.add(tempRoleInfo);
					}
					if(tempList.size() >= 40)
					{
						break;
					}
				}
			}
			
			System.out.println("-----tempListSize1="+tempList.size()+",attType="+attType);
			
			if(tempList.size() < 1)
			{
				if(attType == 3)
				{
					minValue = fightValue;
					maxValue = (int)(fightValue * GameValue.ATTACK_ANOTHER_DIFFICUTL_DOWN);
					
					int minPlace = RankService.getRoleIndexbyShellSort(minValue);
					minPlace = minPlace >= sortFightValuelist.size() ? sortFightValuelist.size() : minPlace;
					int maxPlace = RankService.getRoleIndexbyShellSort(maxValue);
					maxPlace = (maxPlace < 0) ? 0 : maxPlace; 
					
					//比自己战斗力高的取1个
					for(int place = maxPlace ; place <= minPlace ; place++)
					{
						if(place < 0 || place >= sortFightValuelist.size())
						{
							break;
						}
						
						tempRoleRank = sortFightValuelist.get(place);
						if(tempRoleRank == null){
							continue;
						}
						tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
						if(tempRoleInfo == null){
							continue;
						}
						if(tempList.contains(tempRoleInfo))
						{
							continue;
						}
						if(tempRoleInfo.getId() == roleInfo.getId())
						{
							continue;
						}
						if(tempRoleInfo.getFightValue() < maxValue && tempRoleInfo.getFightValue() > minValue)
						{
							tempList.add(tempRoleInfo);
						}
						if(tempList.size() >= 1)
						{
							break;
						}
					}
					System.out.println("-----tempListSize2="+tempList.size());
				} else if(attType == 2) {

					int rank = roleInfo.getRankFightValue();
					// 取比玩家战斗力低的1个人
					while (rank < sortFightValuelist.size() - 1 &&  tempList.size() < 1) {
						tempRoleRank = sortFightValuelist.get(rank);
						rank++;
						if(tempRoleRank == null){
							continue;
						}
						tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
						if(tempRoleInfo == null){
							continue;
						}
						if(tempRoleInfo.getId() == roleInfo.getId()){
							continue;
						}
						if(!tempList.contains(tempRoleInfo))
						{
							tempList.add(tempRoleInfo);	
						}
					}
				}
			}
			
			
			if(tempList.size() < 1)
			{
				int rank = sortFightValuelist.size() - 1;// 玩家在战斗力列表中的索引
				// 取比玩家战斗力低的1个人
				while (rank > 1 &&  tempList.size() < 1) {
					tempRoleRank = sortFightValuelist.get(rank);
					rank--;
					if(tempRoleRank == null){
						continue;
					}
					tempRoleInfo = RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId());
					if(tempRoleInfo == null){
						continue;
					}
					if(tempRoleInfo.getId() == roleInfo.getId()){
						continue;
					}
					if(!tempList.contains(tempRoleInfo))
					{
						tempList.add(tempRoleInfo);	
					}
				}
				
				if(tempList.size() < 1 && sortFightValuelist.size() > 0)
				{
					tempRoleRank = sortFightValuelist.get(new Random().nextInt(sortFightValuelist.size()));
					if(tempRoleRank != null)
					{
						tempList.add(RoleInfoMap.getRoleInfo(tempRoleRank.getRoleId()));
					}
					else
					{
						resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_17);
						return resp;
					}
				}
			}
			
			if(tempList.size() <= 0)
			{
				resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_17);
				return resp;
			}
			Collections.shuffle(tempList);//打乱用来随机取值			
			sideRoleInfo = tempList.get(0);

			if(sideRoleInfo != null){
				resp.setSideRoleName(sideRoleInfo.getRoleName());
				resp.setSildeRoleLevel(HeroInfoMap.getMainHeroLv(sideRoleInfo.getId()));
				List<FightDeployDetailRe> deployHeros = FightDeployService.getFightDeployDetailTrueList(sideRoleInfo);
				resp.setSideRoleId(sideRoleInfo.getId());
				resp.getMatchHeroList().addAll(deployHeros);
				resp.setMatchHeroListSize(resp.getMatchHeroList().size());
				resp.setSideRoleFightValue(sideRoleInfo.getFightValue());
			}
			System.out.println("=======this get coust time="+(System.currentTimeMillis()-startTime)+",roleId="+roleId);
		}
		synchronized (roleInfo) 
		{
			roleLoadInfo.setAttackAnotherType((byte)attType);
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 对攻战开始战斗
	 * @param roleInfo
	 * @param fightInfo
	 * @param defendRoleId
	 * @return
	 */
	public static int dealStartFight(RoleInfo roleInfo, FightInfo fightInfo,int defendRoleId){
		
		//判断背包
		int itemCheck = ItemService.addItemAndEquipCheck(roleInfo);
		if(itemCheck!=1){
			return itemCheck;
		}
		//判断次数是否足够
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo==null){
			return ErrorCode.ATTACK_ANOTHER_ERROR_10;
		}
		if(roleLoadInfo.getAttackAnotherType() < 1 || roleLoadInfo.getAttackAnotherType() > 3){
			return ErrorCode.ATTACK_ANOTHER_ERROR_13;
		}
		RoleInfo defendRoleInfo = RoleInfoMap.getRoleInfo(defendRoleId);
		if(defendRoleInfo==null){
			return ErrorCode.ATTACK_ANOTHER_ERROR_13;
		}
		if(roleLoadInfo.getLastAttackAnotherTime()!=null){//没玩过对攻不用判断
			if(!DateUtil.isSameDay(roleLoadInfo.getLastAttackAnotherTime().getTime(), System.currentTimeMillis())&&
					roleLoadInfo.getAttackAnotherTime()>0){
				//隔天了并且有玩的次数就重置玩的次数(防止打开界面的时候刚好跨天了)
				Timestamp now = new Timestamp(System.currentTimeMillis());
				if(RoleDAO.getInstance().updateRoleAttackAnotherInfo(roleInfo.getId(), (byte)0, now)){
					roleLoadInfo.setAttackAnotherTime((byte)0);
					roleLoadInfo.setLastAttackAnotherTime(new Timestamp(System.currentTimeMillis()));
				}else{
					return ErrorCode.ATTACK_ANOTHER_ERROR_10;
				}
			}
		}
		int XLXFNum = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXFDB_NUM)+GameValue.ATTACK_ANOTHER_EVERY_DAY_TIME;
		if(roleLoadInfo.getAttackAnotherTime()>=XLXFNum&&
				DateUtil.isSameDay(roleLoadInfo.getLastAttackAnotherTime().getTime(), System.currentTimeMillis())){
			return ErrorCode.ATTACK_ANOTHER_ERROR_9;
		}
		
		FightSideData defendSide = getDefendFightSideData(defendRoleInfo, fightInfo.getFightType());
		if (defendSide == null) {
			return ErrorCode.ARENA_FIGHT_START_ERROR_5;
		}
		fightInfo.getFightDataList().add(defendSide);
		return 1;
	}
	
	/**
	 * 获取对攻战防守方数据
	 * @param fightArena
	 * @return
	 */
	public static FightSideData getDefendFightSideData(RoleInfo defendRole, FightType fightType) 
	{
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightType);
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightType);		
		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_1);
		sideDate.setSideRoleId(defendRole.getId());
		sideDate.setSideName(defendRole.getRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());
		
		List<FightArmyDataInfo> addArmyInfos = sideDate.getAddArmyInfos();
		
		FightArmyDataInfo armyData = null;
		List<FightDeployInfo> fightDeployList = FightDeployService.getRoleFightDeployBy(defendRole.getId());
		for (FightDeployInfo info : fightDeployList) {
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(info.getRoleId(), info.getHeroId());
			if (heroInfo == null) {
				return null;
			}
			double baseAddRate = GameValue.FIGHT_TYPE_11_BASE_ADD_RATE;
			armyData = FightService.getFightArmyDatabyHeroInfo(defendRole, heroInfo, info.getDeployPos(),
					sideDate.getSideId(),mainRate,otherRate,(byte)2,baseAddRate);
			if (armyData == null) {
				continue;
			}
			if (armyData.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
				armyData.setLookRange(GameValue.ARENA_LOOK_RANGE);
			}
			sideDate.getArmyInfos().add(armyData);
			FightArmyDataInfo addArmyData = FightService.getFightArmyDatabyHeroInfo(defendRole, heroInfo, info.getDeployPos(),
					sideDate.getSideId(),mainRate,otherRate,(byte)3,baseAddRate);
			if(addArmyData==null){
				continue;
			}
			addArmyInfos.add(addArmyData);
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return null;
		}
		sideDate.setFightAddArmyNum(addArmyInfos.size());
		return sideDate;
	}
	
	/**
	 * 处理对攻战战斗结束(狭路相逢)
	 * @param fightResult
	 * @param roleInfo
	 * @return
	 */
	public static int dealFightEnd(int fightResult, RoleInfo roleInfo, List<BattlePrize> prizeList,int defendRoleId,long fightStartTime){
		
		AttackAnotherXMLInfo anotherXMLInfo = AttackAnotherXMLInfoMap.getAttackAnotherXMLInfoByLevel(
				HeroInfoMap.getMainHeroLv(roleInfo.getId()));
		if(anotherXMLInfo==null){
			return ErrorCode.ATTACK_ANOTHER_ERROR_6;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo==null){
			return ErrorCode.ATTACK_ANOTHER_ERROR_7;
		}
		byte attNum = roleLoadInfo.getAttackAnotherTime();
		// 次数是否足够
		int XLXFNum = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXFDB_NUM)+GameValue.ATTACK_ANOTHER_EVERY_DAY_TIME;
		if(attNum >= XLXFNum){
			return ErrorCode.ATTACK_ANOTHER_ERROR_12;
		}
		List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
		if(fightResult==1){
			//胜利则发送奖励
			String bag = "";			
			if(roleLoadInfo.getAttackAnotherType() == 1)
			{
				bag = anotherXMLInfo.getEasyBag();
			}
			else if(roleLoadInfo.getAttackAnotherType() == 2)
			{
				bag = anotherXMLInfo.getNormalBag();
			}
			else if(roleLoadInfo.getAttackAnotherType() == 3)
			{
				bag = anotherXMLInfo.getHardBag();
			}
			if(bag.length() > 0)
			{
				list = PropBagXMLMap.getPropBagXMLList(bag);
			}
			if (list == null || list.size() <= 0) {
				return ErrorCode.ATTACK_ANOTHER_ERROR_16;
			}
			attNum = (byte)(attNum + 1);
		}

		// 扣次数，更新玩的时间（狭路相逢次数推送 在获得奖励-战功推送）
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(RoleDAO.getInstance().updateRoleAttackAnotherInfo(roleInfo.getId(), attNum, now)){
			roleLoadInfo.setAttackAnotherTime(attNum);
			roleLoadInfo.setLastAttackAnotherTime(now);
			roleLoadInfo.setAttackAnotherType((byte)0);
		}else{
			return ErrorCode.ATTACK_ANOTHER_ERROR_8;
		}
		
		// 掉落奖励
		if (list != null && list.size() > 0) {
			int check = ItemService.addPrizeForPropBag(ActionType.action364.getType(), roleInfo, list, null,
					prizeList, null, null, null, false);
			
			if (check != 1) {
				return check;
			}
		}
				
		// 任务检测
		boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action364.getType(), null, true, false);
		boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, RedPointMgtService.LISTENING_ATT_FIGHT_END);
		//红点推送
		if(isRedPointQuest || isRedPointMonth){
			RedPointMgtService.pop(roleInfo.getId());
		}
		//日志记录
		StringBuilder reward = new StringBuilder();
		RoleInfo defendRoleInfo = RoleInfoMap.getRoleInfo(defendRoleId);
		for(BattlePrize en : prizeList){
			reward.append(en.getNo()).append("_").append(en.getNum()).append(",");
		}
		if(reward.length()!=0){
			reward.deleteCharAt(reward.length()-1);
		}

		GamePVELog log = new GamePVELog();
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setRoleId(roleInfo.getId());
		log.setReward(reward.toString());
		log.setStartTime(new Timestamp(fightStartTime));
		log.setEndTime(new Timestamp(System.currentTimeMillis()));
		if(defendRoleInfo==null){
			log.setDefenseId(0);
			log.setDefenseName(null);
		}else{
			log.setDefenseId(defendRoleInfo.getId());
			log.setDefenseName(defendRoleInfo.getRoleName());
		}
		log.setFightResult(fightResult);
		GameLogService.insertPVELog(log);
		return 1;
	}
	
	/**
	 * 扫荡
	 * @param roleId
	 * @param req
	 * @return
	 */
	public AttackAnotherSweepResp attackAnotherSweep(int roleId, AttackAnotherSweepReq req) {
		AttackAnotherSweepResp resp = new AttackAnotherSweepResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ATTACK_ANOTHER_SWEEP_ERROR_1);
			return resp;
		}
		if (GameValue.ATTACK_ANOTHER_SWITCH != 1) {// 功能开发
			resp.setResult(ErrorCode.ATTACK_ANOTHER_SWEEP_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			int isOpen = 0;
			int attackAnotherType = req.getAttType();
			switch (attackAnotherType) {
			case 1:
				isOpen = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXF_1);
				break;
			case 2:
				isOpen = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXF_2);
				break;
			case 3:
				isOpen = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXF_3);
				break;
			default:
				resp.setResult(ErrorCode.ATTACK_ANOTHER_SWEEP_ERROR_3);
				return resp;
			}
			if(isOpen < 1) {
				//VIP等级不足
				resp.setResult(ErrorCode.ATTACK_ANOTHER_SWEEP_ERROR_4);
				return resp;
			}
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo==null){
				resp.setResult(ErrorCode.ATTACK_ANOTHER_SWEEP_ERROR_5);
				return resp;
			}			
			roleLoadInfo.setAttackAnotherType((byte)attackAnotherType);
			
			//判断背包
			int itemCheck = ItemService.addItemAndEquipCheck(roleInfo);
			if(itemCheck!=1){
				resp.setResult(itemCheck);
				return resp;
			}
					
			List<BattlePrize> prizeList = new ArrayList<BattlePrize>();
			long fightStartTime = System.currentTimeMillis();
			int result = dealFightEnd(1, roleInfo, prizeList, 0, fightStartTime);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			resp.setResult(1);
			resp.setPrize(prizeList);
			resp.setPrizeNum(prizeList.size());
		}
		return resp;
	}

}
