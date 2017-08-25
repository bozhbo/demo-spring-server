package com.snail.webgame.game.protocal.activity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.DropBagInfo;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.fightdata.ServerFightEndReq;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ActivityDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.activity.buytimes.BuyTimesReq;
import com.snail.webgame.game.protocal.activity.buytimes.BuyTimesResp;
import com.snail.webgame.game.protocal.activity.saodang.ActivitySaodangReq;
import com.snail.webgame.game.protocal.activity.saodang.ActivitySaodangResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.CreateFightInfoService;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.xml.cache.ExpActivityMap;
import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.MoneyActivityMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.ExpActivity;
import com.snail.webgame.game.xml.info.GWXMLInfo;
import com.snail.webgame.game.xml.info.MoneyActivity;

/**
 * 活动业务处理类
 * 
 * @author xiasd
 * 
 */
public class ActivityService {
	// private static Logger logger = LoggerFactory.getLogger("logs");

	public BuyTimesResp buyTimes(int roleId, BuyTimesReq req) {

		return null;
	}
	/**
	 * 当锁定时装map空时.将String转成map使用
	 */
	public static void StringToMapUtil(RoleLoadInfo roleLoadInfo){
		if (roleLoadInfo == null) return;
		roleLoadInfo.allExpActionMaxMap.clear();
		String all = roleLoadInfo.getAllExpActionMax();
		if(all!=null){
			String[] allString = all.split(",");
			for(String a : allString){
				String[] b = a.split("_");
				if(b.length!=2) continue;
				roleLoadInfo.allExpActionMaxMap.put(Integer.valueOf(b[0]), Integer.valueOf(b[1]));
			}
		}
	}
	/**
	 * 改变锁定时装map之后同步DB修改字符串
	 */
	public static String MapToStringUtil(RoleLoadInfo roleLoadInfo){
		StringBuilder sb = new StringBuilder();
		if(roleLoadInfo.allExpActionMaxMap.size() > 0){
			for(Entry<Integer, Integer>  en: roleLoadInfo.allExpActionMaxMap.entrySet()){
				sb.append(en.getKey()).append("_").append(en.getValue()).append(",");
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
			}
		}
		return sb.toString();
	}
	/**
	 * 练兵场扫荡
	 */
	public static ActivitySaodangResp saodangExpAction(int roleId , ActivitySaodangReq req){
		ActivitySaodangResp resp = new ActivitySaodangResp();
		ExpActivity activity = ExpActivityMap.getExpActivity(req.getSaodangType());
		if (activity == null) {
			resp.setResult(ErrorCode.ACTIVITY_NOT_EXISIT);
			return resp;
		}
		int type = req.getSaodangType() / 10000; // 等级于类型拼接的4位数，首位为类型
		int level = req.getSaodangType() % 10000;
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_14);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_14);
			return resp;
		}
		if(roleLoadInfo.allExpActionMaxMap.size() == 0)
		{
			if(roleLoadInfo.getAllExpActionMax() != null && roleLoadInfo.getAllExpActionMax().length() > 0)
			{
				StringToMapUtil(roleLoadInfo);
			}
		}
		if(!roleLoadInfo.allExpActionMaxMap.containsKey(req.getSaodangType())){
			resp.setResult(ErrorCode.SAODANG_ERROR);
			return resp;
		}
		if((type == 1 && roleInfo.getExpLeftTimes1() <= 0)
				|| (type == 2 && roleInfo.getExpLeftTimes2() <= 0)
				|| (type == 3 && roleInfo.getExpLeftTimes3() <= 0)
				|| (type == 4 && roleInfo.getExpLeftTimes4() <= 0)
				|| (type == 5 && roleInfo.getExpLeftTimes5() <= 0)
				|| (type == 6 && roleInfo.getExpLeftTimes6() <= 0)){
			resp.setResult(ErrorCode.TIMES_HAS_FINISHED_2);
			return resp;
		}
		int dayofWeek = DateUtil.getDayofWeek();
		if(dayofWeek == 1){
			dayofWeek = 7;
		} else {
			dayofWeek--;
		}
		// 判断是否活动开启
		if(type == 1 && !GameValue.ACTIVITY_EXP_TYPE1.contains(String.valueOf(dayofWeek))
				|| type == 2 && !GameValue.ACTIVITY_EXP_TYPE2.contains(String.valueOf(dayofWeek))
				|| type == 3 && !GameValue.ACTIVITY_EXP_TYPE3.contains(String.valueOf(dayofWeek))
				|| type == 4 && !GameValue.ACTIVITY_EXP_TYPE4.contains(String.valueOf(dayofWeek))
				|| type == 5 && !GameValue.ACTIVITY_EXP_TYPE5.contains(String.valueOf(dayofWeek))
				|| type == 6 && !GameValue.ACTIVITY_EXP_TYPE6.contains(String.valueOf(dayofWeek))){
			resp.setResult(ErrorCode.ACTIVITY_NOT_OPEN);
			return resp;
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null){
			resp.setResult(ErrorCode.ACTIVITY_ERROR_2);
			return resp;
		}
		if (heroInfo.getHeroLevel() < level) {
			resp.setResult(ErrorCode.ROLE_LEVEL_NOT_ENOUGH);
			return resp;
		}
		String common = "";
		int oldLv = heroInfo.getHeroLevel();
		//添加掉落
		HashMap<Integer, BattleDetailPoint> pointMap = activity.getPointsMap();
		int bingNum = roleLoadInfo.allExpActionMaxMap.get(req.getSaodangType());
		int itemNum = bingNum * activity.getNpcReward();
		int big = bingNum / 5;
		int small = bingNum % 5;
		// 物品掉落
		List<DropInfo> addList = new ArrayList<DropInfo>();
		DropInfo dropInfo = new DropInfo();
		dropInfo.setItemNo(activity.getItemNo());
		dropInfo.setItemNum(itemNum);
		dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
		addList.add(dropInfo);
		List<DropInfo> addList2  = new ArrayList<DropInfo>();;
		//掉落
		if(pointMap != null && pointMap.size() > 0){
			for(Entry<Integer, BattleDetailPoint> bagInfo : pointMap.entrySet()){
				if(bagInfo.getKey() <= big+1){
					String NPC = bagInfo.getValue().getGw();
					if(NPC != null && NPC.length() > 0){
						GWXMLInfo gwXmlInfo =  GWXMLInfoMap.getNPCGWXMLInfo(NPC);
						if(gwXmlInfo != null){
							HashMap<Integer, String> dropMap = gwXmlInfo.getDropMap();
							for (int no : dropMap.keySet()) {
								if(bagInfo.getKey() == big+1 && small < no) continue;
								String bag = dropMap.get(no);
								List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(bag);
								// 计算
								ItemService.getDropXMLInfo(null, list, addList2);
							}
						}
					}
				}
			}
		}
		if(addList2.size()>0){
			addList.addAll(addList2);
		}
		// 输赢都扣次数
		if(type == 1)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes1() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes1(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes1(expLeftTimes);
			}
		}
		else if(type == 2)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes2() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes2(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes2(expLeftTimes);
			}
		}
		else if(type == 3)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes3() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes3(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes3(expLeftTimes);
			}
		}
		else if(type == 4)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes4() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes4(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes4(expLeftTimes);
			}
		}
		else if(type == 5)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes5() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes5(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes5(expLeftTimes);
			}
		}
		else if(type == 6)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes6() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes6(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes6(expLeftTimes);
			}
		}
		else
		{
			resp.setResult(ErrorCode.ACTIVITY_EXP_REQ_ERROR_1);
			return resp;
		}
		
		// 添加物品
		String bag = activity.getBag();
		List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
		if(bag != null && bag.length() > 0)
		{
			list = PropBagXMLMap.getPropBagXMLList(bag);
		}
		if(list.size() > 0)
		{
			ItemService.getDropXMLInfo(roleInfo, list,addList);
		}
		List<BattlePrize> dropList = new ArrayList<BattlePrize>();
		int result = ItemService.addPrize(ActionType.action321.getType(), roleInfo, addList,null,
				null, null, null, null, null, dropList, null, null, true);
		if (result != 1) {
			resp.setResult(ErrorCode.ACTIVITY_EXP_REQ_ERROR_1);
			return resp;
		}
		if (dropList != null && dropList.size() > 0) {
			resp.setPrizeNum(dropList.size());
			resp.setPrize(dropList);
		}
		if(oldLv < heroInfo.getHeroLevel())
		{
			common = "lvUp";
		}
		// 任务检测		
		boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action321.getType(), null, true, false);
		boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), common, false, GameValue.RED_POINT_TYPE_EXPERIENCE_4);
		//红点推送
		if(isRedQuest || isRed){
			RedPointMgtService.pop(roleInfo.getId());
		}
		//日志记录
		StringBuilder reward = new StringBuilder();
		for(DropInfo en : addList){
			reward.append(en.getItemNo()).append("_").append(en.getItemNum()).append(",");
		}
		if(reward.length()!=0){
			reward.deleteCharAt(reward.length()-1);
		}
		resp.setResult(1);
		return resp;
	}
	/**
	 * 处理经验活动战斗开启
	 * 
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealExpFightStart(RoleInfo roleInfo, FightInfo fightInfo) {
		if(GameValue.GAME_SOLIDER_OPEN != 1){
			return ErrorCode.NOT_OPEN_ERROR;
		}
		int reqLevelType = 0;

		try {
			reqLevelType = Integer.valueOf(fightInfo.getDefendStr());
		} catch (Exception e) {
			return ErrorCode.ACTIVITY_EXP_REQ_ERROR;
		}

		ExpActivity activity = ExpActivityMap.getExpActivity(reqLevelType);

		if (activity == null) {
			return ErrorCode.ACTIVITY_NOT_EXISIT;
		}
		
		int type = reqLevelType / 10000; // 等级于类型拼接的4位数，首位为类型
		int level = reqLevelType % 10000;
		
		// 判断次数是否用完
		if((type == 1 && roleInfo.getExpLeftTimes1() <= 0)
				|| (type == 2 && roleInfo.getExpLeftTimes2() <= 0)
				|| (type == 3 && roleInfo.getExpLeftTimes3() <= 0)
				|| (type == 4 && roleInfo.getExpLeftTimes4() <= 0)
				|| (type == 5 && roleInfo.getExpLeftTimes5() <= 0)
				|| (type == 6 && roleInfo.getExpLeftTimes6() <= 0)){
			return ErrorCode.TIMES_HAS_FINISHED_2;
		}
		int dayofWeek = DateUtil.getDayofWeek();
		if(dayofWeek == 1){
			dayofWeek = 7;
		} else {
			dayofWeek--;
		}
		// 判断是否活动开启
		if(type == 1 && !GameValue.ACTIVITY_EXP_TYPE1.contains(String.valueOf(dayofWeek))
				|| type == 2 && !GameValue.ACTIVITY_EXP_TYPE2.contains(String.valueOf(dayofWeek))
				|| type == 3 && !GameValue.ACTIVITY_EXP_TYPE3.contains(String.valueOf(dayofWeek))
				|| type == 4 && !GameValue.ACTIVITY_EXP_TYPE4.contains(String.valueOf(dayofWeek))
				|| type == 5 && !GameValue.ACTIVITY_EXP_TYPE5.contains(String.valueOf(dayofWeek))
				|| type == 6 && !GameValue.ACTIVITY_EXP_TYPE6.contains(String.valueOf(dayofWeek))){
			return ErrorCode.ACTIVITY_NOT_OPEN;
		}
		
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null){
			return ErrorCode.ACTIVITY_ERROR_2;
		}
		if (heroInfo.getHeroLevel() < level) {
			return ErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		
		//添加掉落
		HashMap<Integer, BattleDetailPoint> pointMap = activity.getPointsMap();
		Map<Integer, DropBagInfo> dropBagMap = fightDrop(pointMap);
		if(dropBagMap != null && dropBagMap.size() > 0)
		{
			fightInfo.setDropMap(dropBagMap);
		}
		
		int check = fightSideData(roleInfo, fightInfo, type);
		if(check != 1)
		{
			return check;
		}
		
		return 1;
	}

	/**
	 * 处理银币活动战斗开启
	 * 
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealMoneyFightStart(RoleInfo roleInfo, FightInfo fightInfo) {
		if (roleInfo.getMoneyLeftTimes() <= 0) {
			return ErrorCode.TIMES_HAS_FINISHED_1;
		}
		int reqLevelType = 0;

		try {
			reqLevelType = Integer.valueOf(fightInfo.getDefendStr());
		} catch (Exception e) {
			return ErrorCode.ACTIVITY_MONEY_REQ_ERROR;
		}

		MoneyActivity activity = MoneyActivityMap.getMoneyActivity(reqLevelType);

		if (activity == null) {
			return ErrorCode.ACTIVITY_NOT_EXISIT;
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(heroInfo == null){
			return ErrorCode.ACTIVITY_ERROR_4;
		}
		if (heroInfo.getHeroLevel() < reqLevelType) {
			return ErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		return 1;
	}

	/**
	 * 经验活动战斗结束处理（完全信任客户端战斗数据）
	 * 
	 * @param fightResult
	 * @param roleInfo
	 * @param fightInfo
	 * @param fightEndReq
	 * @param prizeList
	 * @return
	 */
	public static int dealExpFightEnd(int fightResult, RoleInfo roleInfo, FightInfo fightInfo, ServerFightEndReq fightEndReq,List<BattlePrize> prizeList) {
		int reqLevelType = 0;

		try {
			reqLevelType = Integer.valueOf(fightInfo.getStartRespDefendStr());
		} catch (Exception e) {
			return ErrorCode.ACTIVITY_EXP_REQ_ERROR;
		}
		// 判断玩家等级是否达到
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(heroInfo == null){
			return ErrorCode.ACTIVITY_ERROR_6;
		}
		String common = "";
		int oldLv = heroInfo.getHeroLevel();
		int type = reqLevelType / 10000; // 等级于类型拼接的4位数，首位为类型
		int level = reqLevelType % 10000;
		
		if (heroInfo.getHeroLevel() < level) {
			return ErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		// 判断次数是否用完
		if((type == 1 && roleInfo.getExpLeftTimes1() <= 0)
				|| (type == 2 && roleInfo.getExpLeftTimes2() <= 0)
				|| (type == 3 && roleInfo.getExpLeftTimes3() <= 0)
				|| (type == 4 && roleInfo.getExpLeftTimes4() <= 0)
				|| (type == 5 && roleInfo.getExpLeftTimes5() <= 0)
				|| (type == 6 && roleInfo.getExpLeftTimes6() <= 0)){
			return ErrorCode.TIMES_HAS_FINISHED_2;
		}
		

		ExpActivity activity = ExpActivityMap.getExpActivity(reqLevelType);
		if (activity == null) {
			return ErrorCode.ACTIVITY_NOT_EXISIT;
		}
		// 最多有2个BOSS
		String counts = fightEndReq.getReserve();// 格式     bossCount,enemyCount
		int killedBossCount = 0;
		int killedEnemyCount = 0;
		
		try {
			String[] countArr = counts.split(",");
			
			if(countArr.length != 2){
				return ErrorCode.ACTIVITY_EXP_REQ_ERROR;
			}
			killedBossCount = Integer.valueOf(countArr[0]);
			killedEnemyCount = Integer.valueOf(countArr[1]);
		} catch (Exception e) {
			return ErrorCode.ACTIVITY_EXP_REQ_ERROR;
		}
		
		if (killedBossCount > 2 || killedBossCount < 0 
				|| killedEnemyCount < 0 || killedEnemyCount > activity.getQuantity()) {
			return ErrorCode.NUM_ERROR;
		}
		int itemNum = killedBossCount * activity.getBossReward() + killedEnemyCount * activity.getNpcReward();

		// 物品掉落
		List<DropInfo> addList = new ArrayList<DropInfo>();
		DropInfo dropInfo = new DropInfo();
		dropInfo.setItemNo(activity.getItemNo());
		dropInfo.setItemNum(itemNum);
		dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
		addList.add(dropInfo);
		
		//掉落
		List<DropBagInfo> dropBagList = fightEndReq.getDropList();

		//验证掉落
		if(dropBagList != null && dropBagList.size() > 0){
			if(CreateFightInfoService.checkDrop(dropBagList, fightInfo)){
				for(DropBagInfo bagInfo : dropBagList){
					addList.addAll(bagInfo.getDrop());
				}
			} else {
				return ErrorCode.CHALLENGE_ERROR_6;
			}
		}
		
		
		
		if(oldLv < heroInfo.getHeroLevel())
		{
			common = "lvUp";
		}
		// 输赢都扣次数
		if(type == 1)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes1() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes1(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes1(expLeftTimes);
			}
		}
		else if(type == 2)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes2() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes2(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes2(expLeftTimes);
			}
		}
		else if(type == 3)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes3() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes3(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes3(expLeftTimes);
			}
		}
		else if(type == 4)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes4() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes4(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes4(expLeftTimes);
			}
		}
		else if(type == 5)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes5() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes5(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes5(expLeftTimes);
			}
		}
		else if(type == 6)
		{
			byte expLeftTimes = (byte) (roleInfo.getExpLeftTimes6() - 1);
			if (ActivityDao.getInstance().updateExpActivityLeftTimes6(expLeftTimes, (int)roleInfo.getId())) {
				roleInfo.setExpLeftTimes6(expLeftTimes);
			}
		}
		else
		{
			return ErrorCode.ACTIVITY_EXP_REQ_ERROR_1;
		}
		
		// 添加物品
		String bag = activity.getBag();
		List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
		if(bag != null && bag.length() > 0)
		{
			list = PropBagXMLMap.getPropBagXMLList(bag);
		}
		if(list.size() > 0)
		{
			ItemService.getDropXMLInfo(roleInfo, list,addList);
		}
		int result = ItemService.addPrize(ActionType.action321.getType(), roleInfo, addList,null,
				null, null, null, null, null, prizeList, null, null, true);
		if (result != 1) {
			return result;
		}
		//更新扫荡关数
		boolean flag = false;
		//检查扫荡数据
		if(roleLoadInfo.allExpActionMaxMap.size() == 0)
		{
			if(roleLoadInfo.getAllExpActionMax() != null && roleLoadInfo.getAllExpActionMax().length() > 0)
			{
				StringToMapUtil(roleLoadInfo);
			}
		}
		if(roleLoadInfo.allExpActionMaxMap.containsKey(reqLevelType)){
			if(killedEnemyCount > roleLoadInfo.allExpActionMaxMap.get(reqLevelType)){
				roleLoadInfo.allExpActionMaxMap.put(reqLevelType, killedEnemyCount);
				flag = true;
			}
		}else{
			roleLoadInfo.allExpActionMaxMap.put(reqLevelType, killedEnemyCount);
			flag = true;
		}
		if(flag){
			if(RoleDAO.getInstance().updateAllExpAction(roleInfo.getId(), MapToStringUtil(roleLoadInfo))){
				roleLoadInfo.setAllExpActionMax(MapToStringUtil(roleLoadInfo));
			}
		}
		// 任务检测
		boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action321.getType(), null, true, false);
		boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), common, false, GameValue.RED_POINT_TYPE_EXPERIENCE_4);
		//红点推送
		if(isRedQuest || isRed){
			RedPointMgtService.pop(roleInfo.getId());
		}
		//日志记录
		StringBuilder reward = new StringBuilder();
		for(DropInfo en : addList){
			reward.append(en.getItemNo()).append("_").append(en.getItemNum()).append(",");
		}
		if(reward.length()!=0){
			reward.deleteCharAt(reward.length()-1);
		}
		//记录练兵日志
		GameLogService.insertActivityLog(roleInfo, fightInfo, prizeList);
		return 1;
	}

	/**
	 * 银币活动战斗将诶数处理（完全信任客户端数据）
	 * 
	 * @param fightResult
	 * @param roleInfo
	 * @param fightInfo
	 * @param prizeList
	 * @return
	 */
	public static int dealMoneyFightEnd(int fightResult, RoleInfo roleInfo, FightInfo fightInfo,List<BattlePrize> prizeList) {
		// 判断次数是否用完
		if (roleInfo.getMoneyLeftTimes() <= 0) {
			return ErrorCode.TIMES_HAS_FINISHED_3;
		}
		int reqLevelType = 0;

		try {
			reqLevelType = Integer.valueOf(fightInfo.getStartRespDefendStr());
		} catch (Exception e) {
			return ErrorCode.ACTIVITY_MONEY_REQ_ERROR;
		}
		// 判断玩家等级是否达到
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(heroInfo == null){
			return ErrorCode.ACTIVITY_ERROR_10;
		}
		if (heroInfo.getHeroLevel() < reqLevelType) {
			return ErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		MoneyActivity activity = MoneyActivityMap.getMoneyActivity(reqLevelType);

		if (activity == null) {
			return ErrorCode.ACTIVITY_NOT_EXISIT;
		}
		
		// 输赢都扣次数
		byte moneyLeftTimes = (byte) (roleInfo.getMoneyLeftTimes() - 1);

		if (ActivityDao.getInstance().updateMoneyActivityLeftTimes(moneyLeftTimes, (int)roleInfo.getId())) {
			roleInfo.setMoneyLeftTimes(moneyLeftTimes);
		}
		
		// 赢了发奖励，只发金币
		if(fightResult == 1){
			List<DropInfo> notItemList = new ArrayList<DropInfo>();
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum((int) (activity.getReward() * heroInfo.getHeroLevel()));
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			notItemList.add(dropInfo);
			
			// 只添加银币
			int result = ItemService.addPrize(ActionType.action322.getType(), roleInfo, notItemList, null,
					null, null, null, null, null, prizeList, null, null, true);
			
			if (result != 1) {
				return result;
			}
		}
		
		// 任务检测
		boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action322.getType(), null, true, false);
		boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, GameValue.RED_POINT_TYPE_EXPERIENCE_3);
		//红点推送 
		if(isRedQuest || isRed){
			RedPointMgtService.pop(roleInfo.getId());
		}
		
		return 1;
	}
	
	
	/**
	 * 掉落计算 （练兵专用）
	 * @param pointMap
	 * @return
	 */
	private static Map<Integer, DropBagInfo> fightDrop(HashMap<Integer, BattleDetailPoint> pointMap)
	{
		Map<Integer, DropBagInfo> dropBagMap = new HashMap<Integer, DropBagInfo>();
		for(BattleDetailPoint point : pointMap.values()){
			int pointNo = point.getPointNo() * 100;
			String NPC = point.getGw();
			if(NPC != null && NPC.length() > 0){
				GWXMLInfo gwXmlInfo =  GWXMLInfoMap.getNPCGWXMLInfo(NPC);
				if(gwXmlInfo != null){
					HashMap<Integer, String> dropMap = gwXmlInfo.getDropMap();
					boolean flag = false;
					for (int no : dropMap.keySet()) {
						int npcNo = pointNo + no;
						DropBagInfo dropBagInfo = new DropBagInfo();
						String bag = dropMap.get(no);
						
						dropBagInfo.setNo(npcNo);
						List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(bag);
						List<DropInfo> addList = new ArrayList<DropInfo>();//奖励
						// 计算
						ItemService.getDropXMLInfo(null, list, addList);
						if(addList.size() > 0)
						{
							dropBagInfo.setDrop(addList);
							dropBagInfo.setDropNum(addList.size());
							
							dropBagMap.put(dropBagInfo.getNo(), dropBagInfo);
							flag = true;
						}
					}
					if(!flag)
					{
						//如果没有掉落 随机获得物品
						
					}
				}
			}
		}
		return dropBagMap;
	}
	
	/**
	 * 验证请求信息，并获取攻击方战斗数据
	 * @param roleInfo
	 * @param deployType
	 * @param list
	 * @return
	 */
	private static int fightSideData(RoleInfo roleInfo , FightInfo fightInfo, int type) 
	{
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());
		
		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_0);
		sideDate.setSideRoleId(roleInfo.getId());
		sideDate.setSideName(roleInfo.getRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());
		
		
		List<StartFightPosInfo> list = fightInfo.getChgPosInfos();
		if (list == null || list.size() <= 0) {
			return ErrorCode.CAMPAIGN_POS_ERROR_1;
		}
		
		
		boolean havMainHero = false;// 验证是否有主武将
		List<Integer> heroIds = new ArrayList<Integer>();// 验证英雄id
		List<Byte> deployPoss = new ArrayList<Byte>();// 验证布阵位置
		HeroInfo heroInfo = null;
		List<Integer> jbHeroNoList = new ArrayList<Integer>();//羁绊武将
		boolean flag = false; //副将是否符合
		
		if(type != 6)
		{
			for (StartFightPosInfo re : list) {
				byte deployPos = re.getDeployPos();
				int heroId = re.getHeroId();
				if (deployPos <= 0) {
					return ErrorCode.ACTIVITY_POS_ERROR_2;
				}
				if (deployPoss.contains(deployPos)) {
					// 判断上阵位置是否重复
					return ErrorCode.ACTIVITY_POS_ERROR_3;
				} else {
					deployPoss.add(deployPos);// 记录heroId
				}

				heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroId);
				if (heroInfo == null) {
					return ErrorCode.ACTIVITY_POS_ERROR_4;
				}
				if (deployPos == HeroInfo.DEPLOY_TYPE_MAIN) {
					if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
						havMainHero = true;
					} else {
						// 主英雄判断
						return ErrorCode.ACTIVITY_POS_ERROR_5;
					}
				}
				if (heroIds.contains(heroId)) {
					// 判断heroId是否重复
					return ErrorCode.ACTIVITY_POS_ERROR_6;
				} else {
					heroIds.add(heroId);// 记录heroId
				}
				
				if(!FightDeployService.checkDeployPosOpen(roleInfo, deployPos)){
					return ErrorCode.ACTIVITY_POS_ERROR_8;
				}

				if (deployPos != HeroInfo.DEPLOY_TYPE_COMM
						&& deployPos != HeroInfo.DEPLOY_TYPE_MAIN
						&& deployPos <= GameValue.FIGHT_ARMY_LIMIT) {
					HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
					if(type != heroXMLInfo.getHeroType())
					{
						flag = true;
						break;
					}
				}
				
				if (deployPos > GameValue.FIGHT_ARMY_LIMIT) {
					jbHeroNoList.add(heroInfo.getHeroNo());
				}
			}
			if (!havMainHero)
			{
				// 没有主武将
				return ErrorCode.ACTIVITY_POS_ERROR_7;
			}
			if(flag)
			{
				//开战失败，所带兵种无法在此模式开战
				return ErrorCode.ACTIVITY_NOT_START_FIGHT;
			}
		}
		
		HeroRecord heroRecord = null;
		Map<Byte, HeroRecord> rocordMap = new HashMap<Byte, HeroRecord>();
		for (StartFightPosInfo re : list) {
			byte deployPos = re.getDeployPos();
			int heroId = re.getHeroId();
			
			if (deployPos != HeroInfo.DEPLOY_TYPE_COMM) {
				heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroId);
				if (heroInfo == null) {
					return ErrorCode.ACTIVITY_POS_ERROR_4;
				}
				heroRecord = HeroRecordService.getDeployHeroRecord(heroInfo, deployPos);
				if (heroRecord == null) {
					continue;
				}
				heroRecord.setCutHp(0);
				heroRecord.setHeroStatus((byte)1);
				if (deployPos == HeroInfo.DEPLOY_TYPE_MAIN) {
					heroRecord.setJbHeroNoList(jbHeroNoList);
				}
				rocordMap.put(deployPos, heroRecord);
			}
		}
		
		FightArmyDataInfo armyData = null;
		for (HeroRecord record : rocordMap.values()) {
			armyData = FightService.getFightArmyDatabyHeroRecord(rocordMap, record, sideDate.getSideId(), mainRate ,otherRate);
			if (armyData == null) {
				continue;
			}
			sideDate.getArmyInfos().add(armyData);
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return ErrorCode.ACTIVITY_POS_ERROR_1;
		}
		
		fightInfo.getFightDataList().add(sideDate);
		
		return 1;
	}

	public static void resNum(RoleInfo roleInfo, int oldVIPLv, int newVIPLv)
	{
		int oldNum = VipXMLInfoMap.getVipVal(oldVIPLv, VipType.LBCDB_NUM);
		int newNum = VipXMLInfoMap.getVipVal(newVIPLv, VipType.LBCDB_NUM);
		int LBCNum1 = newNum - oldNum + roleInfo.getExpLeftTimes1();
		int LBCNum2 = newNum - oldNum + roleInfo.getExpLeftTimes2();
		int LBCNum3 = newNum - oldNum + roleInfo.getExpLeftTimes3();
		int LBCNum4 = newNum - oldNum + roleInfo.getExpLeftTimes4();
		int LBCNum5 = newNum - oldNum + roleInfo.getExpLeftTimes5();
		int LBCNum6 = newNum - oldNum + roleInfo.getExpLeftTimes6();
		
		LBCNum1 = LBCNum1 < 0 ? 0 : LBCNum1;
		LBCNum2 = LBCNum2 < 0 ? 0 : LBCNum2;
		LBCNum3 = LBCNum3 < 0 ? 0 : LBCNum3;
		LBCNum4 = LBCNum4 < 0 ? 0 : LBCNum4;
		LBCNum5 = LBCNum5 < 0 ? 0 : LBCNum5;
		LBCNum6 = LBCNum6 < 0 ? 0 : LBCNum6;
		
		if(ActivityDao.getInstance().updateAllExpActivityLeftTimes(LBCNum1, LBCNum2, LBCNum3, LBCNum4, LBCNum5, LBCNum6, (int)roleInfo.getId()))
		{
			roleInfo.setExpLeftTimes1((byte)LBCNum1);
			roleInfo.setExpLeftTimes2((byte)LBCNum2);
			roleInfo.setExpLeftTimes3((byte)LBCNum3);
			roleInfo.setExpLeftTimes4((byte)LBCNum4);
			roleInfo.setExpLeftTimes5((byte)LBCNum5);
			roleInfo.setExpLeftTimes6((byte)LBCNum6);
			// 刷新次数
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
		}
	}
}
