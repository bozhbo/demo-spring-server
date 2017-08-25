package com.snail.webgame.game.protocal.appellation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.FightCompetitionRankList;
import com.snail.webgame.game.cache.FightMutualRankList;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.InnerSort;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.appellation.entity.ResetTitleInfosResp;
import com.snail.webgame.game.protocal.arena.service.ArenaService;
import com.snail.webgame.game.protocal.fight.mutual.rank.MutualQueryRankRes;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.rolemgt.detail.RoleDetailResp;
import com.snail.webgame.game.xml.cache.ChenghaoXMLInfoMap;
import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;

public class TitleService {
	//该类中的排行榜除WorldBoss其他都只在12点刷新排行榜
	//竞技场前3
	public static List<Integer> jjcTop = new ArrayList<Integer>(); //No.1
	//跨服竞技场前3
	public static List<Integer> kfjjcTop = new ArrayList<Integer>(); //No.2
	//等级前3
	public static List<Integer> lvTop = new ArrayList<Integer>(); //No.3
	//战斗力前3
	public static List<Integer> fightValueTop = new ArrayList<Integer>(); //No.4
	//武将数量前3
	public static List<Integer> heroNumTop = new ArrayList<Integer>(); //No.6
	//长坂坡前3
	public static List<Integer> cbpTop = new ArrayList<Integer>(); //No.7
	//世界BOSS前3 只在世界BOSS发奖励后统计
	public static List<Integer> bossTop = new ArrayList<Integer>(); //No.5
	
	private static Set<Integer> resetTitleRoleIdSet = new HashSet<Integer>(); //需要刷新称号的角色Id
	
	
	/**
	 * 角色上线称号检查
	 * @param roleInfo
	 */
	public static void userLoginTitleCheck(RoleInfo roleInfo, RoleDetailResp resp){
		if(roleInfo == null){
			return;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			return;
		}
		
		if(!roleInfo.isTitleRepair()){
			//补获之前的称号
			if(titleRepair(roleInfo) == 1){
				roleInfo.setTitleRepair(true);
			}
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		
		titlesReset(roleInfo, map);//刷新称号
		
		String allTitles = CommonUtil.Map2String(map);
		
		resp.setTitles(allTitles);
		
		resp.setTitle(nowTitleCheck(roleInfo, map));
		
	}
	
	/**
	 * 获取称号
	 * @param list
	 * @param type
	 * @param roleInfo
	 * @param roleLoadInfo
	 */
	public static void getTitle(List<Integer> list, String type, int roleId, Map<Integer, Long> map){
		ChenghaoXMLInfo info = ChenghaoXMLInfoMap.getChenghaoXMLInfoByTypeAndNum(type, list.indexOf(roleId) + 1);
		if(info != null){
			long time = info.getKeepTime();
			if(time > 0){
				time = System.currentTimeMillis() + info.getKeepTime() * 1000;
			}
			
			map.put(info.getNo(), (long) time);
		}
	}

	/**
	 * 每日12点后刷新的排行榜
	 */
	public static void getNewRanks() {
		synchronized(resetTitleRoleIdSet){
			resetTitleRoleIdSet.clear();
			
			//竞技场
			resetRoleTitles(jjcTop, GameValue.APPELLATION_TYPE_JJC); //No.1
			//跨服竞技场
			resetRoleTitles(kfjjcTop, GameValue.APPELLATION_TYPE_KFJJC); //No.2
			//等级
			resetRoleTitles(lvTop, GameValue.APPELLATION_TYPE_LV); //No.3
			//战斗力
			resetRoleTitles(fightValueTop, GameValue.APPELLATION_TYPE_FIGHTVALUE); //No.4
			//武将数量
			resetRoleTitles(heroNumTop, GameValue.APPELLATION_TYPE_HERO_NUM); //No.6
			//长坂坡
			resetRoleTitles(cbpTop, GameValue.APPELLATION_TYPE_CBP); //No.7
			
			notifyClient4ResetTitles(); //通知客户端重置
		}
		
	}
	
	/**
	 * 世界BOSS变化 
	 * 一天两次，区别其他的，分开处理
	 */
	public static void getNewRanks4WorldBoss() {
		synchronized(resetTitleRoleIdSet){
			resetTitleRoleIdSet.clear();
			
			resetRoleTitles(bossTop, GameValue.APPELLATION_TYPE_WORLDBOSS);
			
			notifyClient4ResetTitles(); //通知客户端重置
		}
	}

	/**
	 * 更新排行榜后重置相关角色的称号
	 * @param topList 排行榜前N位
	 * @param type 排行榜类型
	 */
	public static void resetRoleTitles(List<Integer> topList, String type){
		List<Integer> beforeRankList = new ArrayList<Integer>(); //存在之前的名字
		beforeRankList.addAll(topList);
		
		topList.clear();
		
		//No.1
		if(type.equals(GameValue.APPELLATION_TYPE_JJC)){
			for (int place = 1; place <= 3; place++) {
				FightArenaInfo info = FightArenaInfoMap.getFightArenaInfobyPlace(place);
				if (info != null) {
					topList.add(ArenaService.getArenaRe(info).getRoleId());
				}
			}
		}
		
		//No.2
		if(type.equals(GameValue.APPELLATION_TYPE_KFJJC)){
			List<InnerSort> list = FightCompetitionRankList.getRank(0, 3);
			if(list!=null && list.size()>0){
				for(int i = 0 ; i < 3 && i < list.size();i++){
					topList.add(list.get(i).getId());
				}
			}
		}
		
		//No.3
		if(type.equals(GameValue.APPELLATION_TYPE_LV)){
			List<RankInfo> list = RankService.getRoleLevelRank(1, 3);
			if(list!=null && list.size()>0){
				for(RankInfo r : list){
					topList.add(r.getRoleId());
				}
			}
		}
		
		//No.4
		if(type.equals(GameValue.APPELLATION_TYPE_FIGHTVALUE)){
			List<RankInfo> list = RankService.getFightValueRank(1, 3);
			if(list!=null && list.size()>0){
				for(RankInfo r : list){
					topList.add(r.getRoleId());
				}
			}
		}
		
		//No.5
		if(type.equals(GameValue.APPELLATION_TYPE_WORLDBOSS)){
			List<RankInfo> list = RankService.getAttWorldBossRank(1, 3);
			if(list!=null && list.size()>0){
				for(RankInfo r : list){
					topList.add(r.getRoleId());
				}
			}
		}
		
		//No.6
		if(type.equals(GameValue.APPELLATION_TYPE_HERO_NUM)){
			List<RankInfo> list = RankService.getHeroNumRank(1, 3);
			if(list!=null && list.size()>0){
				for(RankInfo r : list){
					topList.add(r.getRoleId());
				}
			}
		}
		
		//No.7
		if(type.equals(GameValue.APPELLATION_TYPE_CBP)){
			List<MutualQueryRankRes> list = FightMutualRankList.getList();
			if(list!=null && list.size()>0){
				for(int i = 0 ; i < 3 && i < list.size();i++){
					topList.add(list.get(i).getRoleId());
				}
			}
		}
		
		resetTitleRoleIdSet.addAll(beforeRankList);
		resetTitleRoleIdSet.addAll(topList);
		
	}
	
	/**
	 * 通知在线的玩家重置称号
	 */
	private static void notifyClient4ResetTitles() {
		if(resetTitleRoleIdSet.size() <= 0){
			return;
		}

		RoleInfo roleInfo = null;
		
		for(Integer roleId : resetTitleRoleIdSet){
			roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(roleInfo == null || roleInfo.getLoginStatus() != 1){
				continue;
			}
			
			synchronized(roleInfo){
				if(roleInfo.getRoleLoadInfo() == null){
					continue;
				}
				
				ResetTitleInfosResp resp = new ResetTitleInfosResp();
				resp.setResult(1);
				Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
				
				titlesReset(roleInfo, map);//刷新称号
				//使用的称号未做检测
				
				resp.setTitles(CommonUtil.Map2String(map));
				
				resp.setTitle(nowTitleCheck(roleInfo, map));
				
				HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				
				if(mainHeroInfo != null){
					HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
					
					resp.setFightValue(mainHeroInfo.getFightValue());
				}
				
				SceneService.sendRoleRefreshMsg(resp, roleId, Command.RESET_TITLE_RESP);
				
			}
			
			
		}
		
		resetTitleRoleIdSet.clear();
	}
	
	/**
	 * 重新检测角色的称号
	 * @param roleInfo
	 * @param map
	 */
	public static void titlesReset(RoleInfo roleInfo, Map<Integer, Long> map){
		if(map == null){
			return;
		}
		Iterator<Integer> ite = map.keySet().iterator();
		
		long nowTime = System.currentTimeMillis();
		
		ChenghaoXMLInfo xmlInfo = null;
		while(ite.hasNext()){
			//检查过期的称号
			int xmlNo = ite.next();
			Long time = map.get(xmlNo);
			if(time == null || (time > 0 && (time - nowTime < GameValue.TITLE_EXP_RANGE))){
				//过期的删除
				ite.remove();
				continue;
			}
			
			xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(xmlNo);
			if(xmlInfo == null){
				ite.remove();
			}
			
			if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_JJC) //No.1
					|| xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_KFJJC) //No.2
					|| xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_LV) //No.3
					|| xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_FIGHTVALUE) //No.4
					|| xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_WORLDBOSS) //No.5
					|| xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_HERO_NUM) //No.6
					|| xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_CBP)){ //No.7
				//暂时默认去除,如果还在排行中会把称号加回来
				
				ite.remove();
			}
			
		}
		
		if(jjcTop.contains(roleInfo.getId())){
			//No.1
			getTitle(jjcTop, GameValue.APPELLATION_TYPE_JJC, roleInfo.getId(), map);
		}
		if(kfjjcTop.contains(roleInfo.getId())){
			//No.2
			getTitle(kfjjcTop, GameValue.APPELLATION_TYPE_KFJJC, roleInfo.getId(), map);
		}
		if(lvTop.contains(roleInfo.getId())){
			//No.3
			getTitle(lvTop, GameValue.APPELLATION_TYPE_LV, roleInfo.getId(), map);
		}
		if(fightValueTop.contains(roleInfo.getId())){
			//No.4
			getTitle(fightValueTop, GameValue.APPELLATION_TYPE_FIGHTVALUE, roleInfo.getId(), map);
		}
		if(bossTop.contains(roleInfo.getId())){
			//No.5
			getTitle(bossTop, GameValue.APPELLATION_TYPE_WORLDBOSS, roleInfo.getId(), map);
		}
		if(heroNumTop.contains(roleInfo.getId())){
			//No.6
			getTitle(heroNumTop, GameValue.APPELLATION_TYPE_HERO_NUM, roleInfo.getId(), map);
		}
		if(cbpTop.contains(roleInfo.getId())){
			//No.7
			getTitle(cbpTop, GameValue.APPELLATION_TYPE_CBP, roleInfo.getId(), map);
		}
		
		//更新现有的称号
		String allTitles = CommonUtil.Map2String(map);
		if(RoleDAO.getInstance().updateAllAppellation(roleInfo.getId(), allTitles)){
			roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
		}
		
	}
	
	/**
	 * 检测角色当前使用的称号是否过期
	 * @param roleInfo
	 * @param map 当前角色拥有的可使用称号
	 * @return
	 */
	public static int nowTitleCheck(RoleInfo roleInfo, Map<Integer, Long> map){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null || map.size() <= 0){
			return 0;
		}
		
		String titleInfo = roleInfo.getRoleLoadInfo().getNowAppellation();
		if(titleInfo == null || "".equals(titleInfo)){
			return 0;
		}
		
		String[] strs = titleInfo.split(":");
		
		int title = 0;
		long time = 0;
		
		try{
			title = Integer.parseInt(strs[0]);
			time = Long.parseLong(strs[1]);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!map.containsKey(title) || (time > 0 && (time - System.currentTimeMillis() < GameValue.TITLE_EXP_RANGE))){
			//已经没有称号或者称号过期
			map.remove(title);
			String allTitles = CommonUtil.Map2String(map);
			if(RoleDAO.getInstance().updateRoleTitleInfo(roleInfo.getId(), "", allTitles)){
				roleInfo.getRoleLoadInfo().setNowAppellation("");
				roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
			}
			return 0;
		}
		
		return title;
		
	}
	
	/**
	 * 成就称号检测
	 * @param type
	 * @param num type == null num is no
	 * @param roleId
	 */
	public static int achieveTitleCheck(String type, int num, RoleInfo roleInfo){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return ErrorCode.TITLE_ERROR_1;
		}
		
		ChenghaoXMLInfo xmlInfo = null;
		if(type == null){
			//根据xmlNo获得配置
			xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(num);
		}else{
			//根据类型 和num 获得
			xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByTypeAndNum(type, num);
		}
		
		if(xmlInfo == null){ 
			return ErrorCode.TITLE_ERROR_5;
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		
		if(map.containsKey(xmlInfo.getNo())){
			return ErrorCode.TITLE_ERROR_11;
		}
		
		long time = xmlInfo.getKeepTime();
		if(time > 0){
			time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
		}
		
		map.put(xmlInfo.getNo(), time);
		
		String allTitles = CommonUtil.Map2String(map);
		
		if(RoleDAO.getInstance().updateAllAppellation(roleInfo.getId(), allTitles)){
			roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
		}
		
		ResetTitleInfosResp resp = new ResetTitleInfosResp();
		resp.setResult(1);
		resp.setFlag(1);
		
		resp.setTitles(xmlInfo.getNo() + ":" + time);
		
		HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(mainHeroInfo != null){
			HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
			
			resp.setFightValue(mainHeroInfo.getFightValue());
		}
		
		SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.RESET_TITLE_RESP);
		
		return 1;
	}
	
	/**
	 * 补获之前没有得到的称号
	 * @param roleInfo
	 */
	private static int titleRepair(RoleInfo roleInfo){
		if(roleInfo == null){
			return 0;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			return 0;
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		//没有补过
		
		ChenghaoXMLInfo xmlInfo = null;
		for(int i = 0; i <= roleInfo.getVipLv(); i++){
			//补先前的VIP称号
			
			xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByTypeAndNum(GameValue.APPELLATION_TYPE_VIP, i);
			if(xmlInfo == null){
				continue;
			}
			
			if(map.containsKey(xmlInfo.getNo())){
				continue;
			}
			
			long time = xmlInfo.getKeepTime();
			if(time > 0){
				time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
			}
			
			map.put(xmlInfo.getNo(), time);
			
		}
		
		for(int i = 0; i <= roleInfo.getCompetitionStage(); i++){
			//先前的段位
			xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByTypeAndNum(GameValue.APPELLATION_TYPE_KFJJC_LV, i);
			if(xmlInfo == null){
				continue;
			}
			
			if(map.containsKey(xmlInfo.getNo())){
				continue;
			}
			
			long time = xmlInfo.getKeepTime();
			if(time > 0){
				time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
			}
			
			map.put(xmlInfo.getNo(), time);
			
		}
		
		//副本的称号
//		Map<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>> challengeMap = roleInfo.getRoleLoadInfo().getChallengeMap();
//		Map<Integer, Map<Integer, ChallengeBattleInfo>> subChallengeMap = null;
//		Map<Integer, ChallengeBattleInfo> lastChallengeMap = null;
//		if(challengeMap != null && challengeMap.size() > 0){
//			for(Byte type : challengeMap.keySet()){// 1st for
//				//获得副本类型
//				subChallengeMap = challengeMap.get(type);
//				if(subChallengeMap != null && subChallengeMap.size() > 0){
//					//获得副本章节
//					for(Integer chapter : subChallengeMap.keySet()){ //2ed for
//						lastChallengeMap = subChallengeMap.get(chapter);
//						if(lastChallengeMap != null && lastChallengeMap.size() > 0){
//							//获得副本编号
//							for(Integer battleNo : lastChallengeMap.keySet()){ //3rd for
//								if(lastChallengeMap.get(battleNo) == null){
//									continue;
//								}
//								
//								xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByTypeAndNum(GameValue.APPELLATION_TYPE_COPY, battleNo);
//								if(xmlInfo == null){
//									continue;
//								}
//								
//								if(map.containsKey(xmlInfo.getNo())){
//									continue;
//								}
//								
//								long time = xmlInfo.getKeepTime();
//								if(time > 0){
//									time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
//								}
//								
//								map.put(xmlInfo.getNo(), time);
//								
//							} //3rd for
//							
//						}
//						
//					} //2ed for
//				}
//				
//			} // 1st for
//			
//		}
		
		for(Integer battleNo :roleInfo.getRoleLoadInfo().getBattleList()){
			xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByTypeAndNum(GameValue.APPELLATION_TYPE_COPY, battleNo);
			if(xmlInfo == null){
				continue;
			}
			
			if(map.containsKey(xmlInfo.getNo())){
				continue;
			}
			
			long time = xmlInfo.getKeepTime();
			if(time > 0){
				time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
			}
			
			map.put(xmlInfo.getNo(), time);
		}
		
		String allTitles = CommonUtil.Map2String(map);
		
		if(!RoleDAO.getInstance().updateAllAppellation(roleInfo.getId(), allTitles)){
			return 0;
		}
		roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
		
		return 1;
	}
	
	/**
	 * 获取角色当前称号
	 * @param roleInfo
	 * @return
	 */
	public static int getNowTitle(RoleInfo roleInfo){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 0;
		}
		
		String titleInfo = roleInfo.getRoleLoadInfo().getNowAppellation();
		if(titleInfo == null || "".equals(titleInfo)){
			return 0;
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		
		String[] strs = titleInfo.split(":");
		
		int title = 0;
		long time = 0;
		
		try{
			title = Integer.parseInt(strs[0]);
			time = Long.parseLong(strs[1]);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!map.containsKey(title) || (time > 0 && (time - System.currentTimeMillis() < GameValue.TITLE_EXP_RANGE))){
			//已经没有称号或者称号过期
			map.remove(title);
			String allTitles = CommonUtil.Map2String(map);
			if(RoleDAO.getInstance().updateRoleTitleInfo(roleInfo.getId(), "", allTitles)){
				roleInfo.getRoleLoadInfo().setNowAppellation("");
				roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
			}
			return 0;
		}
		
		return title;
		
		
		
	}
	
}
