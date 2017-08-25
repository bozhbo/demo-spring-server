package com.snail.webgame.game.protocal.worldBoss.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.WorldBossMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.DiffMail;
import com.snail.webgame.game.common.DiffMailMessage;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.ArmyFightingInfo;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.fightdata.ServerFightEndReq;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.WorldBossDAO;
import com.snail.webgame.game.info.BossInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.WorldBossFightLog;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.CreateFightInfoService;
import com.snail.webgame.game.protocal.fight.service.FightIdGen;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.protocal.worldBoss.query.QueryWorldBossResp;
import com.snail.webgame.game.protocal.worldBoss.refresh.refreshTimeResp;
import com.snail.webgame.game.thread.SendServerMsgThread;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.WorldBossXMLInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.WorldBossXMLInfo;

/**
 * @author zhangyq
 *
 */
public class WorldBossMgtService {
	//private static GameLogDAO gameLogDAO = GameLogDAO.getInstance();
	private static Logger logger = LoggerFactory.getLogger("logs");
	/**
	 * 查询世界boss个人信息
	 * @param roleId
	 * @return
	 */
	public QueryWorldBossResp query(int roleId)
	{
		QueryWorldBossResp resp = new QueryWorldBossResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.WORLD_BOSS_4);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null)
		{
			resp.setResult(ErrorCode.WORLD_BOSS_4);
			return resp;
		}
		synchronized (roleInfo) 
		{
			List<RoleInfo> bossList = WorldBossMap.getBossList();
			int rank = bossList.contains(roleInfo) ? bossList.indexOf(roleInfo)+1 : 0;
			long bestAttHp = roleInfo.getBestFightBossHp();
			long todayAttHp = roleInfo.getFightWorldBossHp();
			if(roleInfo.getLastWorldBossFightTime()==null){
				//新建假的上次攻击时间.多扣2000毫秒避免玩家在1秒内点击造成误差 921440
				Timestamp nowTime = new Timestamp(System.currentTimeMillis() - (GameValue.WORLD_BOSS_REFRESH_TIME*60*1000l) - 2000l);
				roleInfo.setLastWorldBossFightTime(nowTime);
			}
			long nextTime = roleInfo.getLastWorldBossFightTime().getTime()+GameValue.WORLD_BOSS_REFRESH_TIME*60*1000;
			resp.setRank(rank);
			resp.setBestAttHp(bestAttHp);
			resp.setTodayAttHp(todayAttHp);
			resp.setNextTime(nextTime);
			List<BossInfo> list = MapRoleInfoMap.getWorldBoss();
			BossInfo bossInfo = new BossInfo();
			if(list != null && list.size() == 1)
			{
				bossInfo = list.get(0);
			}
			resp.setAllHp(bossInfo.getAllHP());
			resp.setNowHp(bossInfo.getCurrHP());
			resp.setBossLv(bossInfo.getBosslevel());
		}
		resp.setResult(1);
		return resp;
		
	}
	
	/**
	 * 刷新冷却时间
	 * @param roleId
	 * @return
	 */
	public refreshTimeResp refresh(int roleId)
	{
		refreshTimeResp resp = new refreshTimeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.WORLD_BOSS_7);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null)
		{
			resp.setResult(ErrorCode.WORLD_BOSS_7);
			return resp;
		}
		int mainHeroLv = HeroInfoMap.getMainHeroLv(roleId);
		if(mainHeroLv < GameValue.WORLD_BOSS_NEED_LV){
			resp.setResult(ErrorCode.HERO_UP_ERROR_9);
			return resp;
		}
		synchronized (roleInfo) 
		{
			//判断是否有冷却时间 
			Timestamp lastTime = roleInfo.getLastWorldBossFightTime();
			long now = System.currentTimeMillis();
			if(lastTime == null || (lastTime.getTime()+GameValue.WORLD_BOSS_REFRESH_TIME*60*1000) <= now)
			{
				resp.setResult(ErrorCode.WORLD_BOSS_8);
				return resp;
			}
			
			//判断金币是否足够
			int gold = GameValue.WORLD_BOSS_REFRESH_GOLD;
			if(gold >  roleInfo.getCoin())
			{
				resp.setResult(ErrorCode.WORLD_BOSS_9);
				return resp;
			}
			//扣除金币刷新冷却时间
			// 扣除消耗
			if (RoleService.subRoleRoleResource(ActionType.action367.getType(), roleInfo, ConditionType.TYPE_COIN, gold , null)) {
				resp.setSourceType((byte)ConditionType.TYPE_COIN.getType());
				resp.setSourceChange((int)-gold);

				Timestamp nowTime = new Timestamp(System.currentTimeMillis() - (GameValue.WORLD_BOSS_REFRESH_TIME*60*1000l) - 2000l);
				roleInfo.setLastWorldBossFightTime(nowTime);
			} else {
				resp.setResult(ErrorCode.SUB_RESOURCE_ERROR_1);
				return resp;
			}
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action368.getType(), gold+"");
		}
		resp.setResult(1);
		return resp;
		
	}
	
	/**
	 * BOSS战开始战斗
	 * @param roleInfo
	 * @param fightInfo
	 * @param defendRoleId
	 * @return
	 */
	public static int dealStartFight(RoleInfo roleInfo, FightInfo fightInfo){
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null) {
			//系统错误
			return ErrorCode.WORLD_BOSS_5;
		}
		
		MapRolePointInfo  pointInfo = MapRoleInfoMap.getMapPointInfo((int)roleInfo.getId());
		if(pointInfo == null)
		{
			return ErrorCode.WORLD_BOSS_ERROR_8;
		}
		
		//判断是否有世界BOSS
		List<BossInfo> bossList = MapRoleInfoMap.getWorldBoss();
		if(bossList == null || bossList.size() == 0) 
		{
			//boss不存在,无法攻打
			return ErrorCode.WORLD_BOSS_10;
		}
		//判断冷却时间
		long now = System.currentTimeMillis();
		Timestamp time = roleInfo.getLastWorldBossFightTime();
		//还在冷却
		if(time != null && now < (time.getTime() + GameValue.WORLD_BOSS_REFRESH_TIME * 60 * 1000l))
		{
			//开战失败，冷却时间没到
			return ErrorCode.WORLD_BOSS_6;
		}
		//判断玩家等级是否足够
		int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
		if(mainHeroLv < GameValue.WORLD_BOSS_NEED_LV){
			return ErrorCode.HERO_UP_ERROR_9;
		}
		//判断本日是否已经参与
		int week = DateUtil.getWeekDay_Hour(1);
		//验证基础数据
		WorldBossXMLInfo xmlInfo = WorldBossXMLInfoMap.getGoldBuyXMLInfo(week);
		if(xmlInfo==null || xmlInfo.getBeginTime()==null || xmlInfo.getBeginTime2()==null ||
				xmlInfo.getEndTime()==null || xmlInfo.getEndTime2()==null){
			return ErrorCode.WORLD_BOSS_5;
		}
		Calendar c = Calendar.getInstance();
		//验证距离上次打BOSS是否已跨天
		if (roleInfo.getLastWorldBossFightTime() != null && !DateUtil.isSameDay(System.currentTimeMillis(), roleInfo.getLastWorldBossFightTime().getTime())) {
			if(RoleDAO.getInstance().updateIsJoinWorldBoss(roleInfo.getId(),(byte)0))
			{
				roleLoadInfo.setIsJoinBoss((byte)0);
			}
		}
		//如果将要战斗是第二场.验证玩家是否已参与过第一场
		if(c.get(Calendar.HOUR_OF_DAY) > Integer.valueOf(xmlInfo.getBeginTime2().split(":")[0]) 
				||(c.get(Calendar.HOUR_OF_DAY) == Integer.valueOf(xmlInfo.getBeginTime2().split(":")[0]) && c.get(Calendar.MINUTE)>=Integer.valueOf(xmlInfo.getBeginTime2().split(":")[1]))) {
			if(roleLoadInfo.getIsJoinBoss()==(byte)1) return ErrorCode.WORLD_BOSS_ERROR_7;
		}
		//BOSS的战斗信息获得

		String noStr = "";
		synchronized(bossList)
		{
			BossInfo bossInfo = bossList.get(0);
			if(bossInfo == null || bossInfo.getCurrHP() <=0)
			{
				return ErrorCode.WORLD_BOSS_ERROR_6;
			}
			noStr = bossInfo.getBossNo()+","+bossInfo.getCurrHP()+","+bossInfo.getBosslevel();
			
			CreateFightInfoService.generateWorldBossProp(fightInfo,bossInfo.getCurrHP());
		}
		fightInfo.setStartRespDefendStr(noStr);
		fightInfo.setFightId(FightIdGen.getSequenceId());
		//如果本次战斗打的是第一场BOSS.改变状态
		if((c.get(Calendar.HOUR_OF_DAY) >= Integer.valueOf(xmlInfo.getBeginTime().split(":")[0]) && c.get(Calendar.HOUR_OF_DAY) <= Integer.valueOf(xmlInfo.getEndTime().split(":")[0]))
				&& roleLoadInfo.getIsJoinBoss()==0){
			if(RoleDAO.getInstance().updateIsJoinWorldBoss(roleInfo.getId(),(byte)1))
			{
				roleLoadInfo.setIsJoinBoss((byte)1);
			}
		}
		
		pointInfo.setStatus((byte) 1);
		// 大地图上战斗，广播给可见自己的其它人
		SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
		fightInfo.setCheckFlag((byte) 1);
		
		return 1;
	}
	
	/**
	 * 世界BOSS战斗结束处理
	 * @param roleInfo
	 * @param fightEndReq 战后信息
	 * @param fightInfo 战斗信息
	 * @param prizeList 奖励
	 * @return
	 */
	public static int worldBossEnd(RoleInfo roleInfo, ServerFightEndReq fightEndReq, FightInfo fightInfo, List<BattlePrize> prizeList) 
	{
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null)
		{
			return ErrorCode.ROLE_LOAD_ERROR_3;
		}
		MapRolePointInfo  pointInfo = MapRoleInfoMap.getMapPointInfo((int)roleInfo.getId());
		if(pointInfo == null)
		{
			return ErrorCode.WORLD_BOSS_ERROR_8;
		}
		//是否击杀
		boolean iskill = false;
			
		//计算所造成的伤害
		List<ArmyFightingInfo> fightEndReqSide = fightEndReq.getArmyFightingInfos();	// 战斗结束传过来的部队信息
		if (fightEndReqSide == null || fightEndReqSide.size() <= 0)
		{
			//没有部队信息
			return ErrorCode.WORLD_BOSS_1;
		}
		List<ArmyFightingInfo> fightEndSideList1 = new ArrayList<ArmyFightingInfo>();//攻方
		List<ArmyFightingInfo> fightEndSideList2 = new ArrayList<ArmyFightingInfo>();//守方
		for (ArmyFightingInfo endSide : fightEndReqSide) 
		{
			if (endSide.getSideId() == FightType.FIGHT_SIDE_0) 
			{ // 攻方
				fightEndSideList1.add(endSide);
			}
			else if (endSide.getSideId() == FightType.FIGHT_SIDE_1) 
			{ // 守方
				fightEndSideList2.add(endSide);
			}
		}
		
		//计算boss受到的伤害
		String NPCNo = "";
		long NPChp = 0;
		long bossHP = 0; //计算boss受到的伤害
		String defendStr = fightInfo.getStartRespDefendStr();
		if(defendStr != null && defendStr.length() > 0)
		{
			String[] str = defendStr.split(",");
			NPCNo = str[0];
			NPChp = Long.parseLong(str[1]);
		}
		if(fightEndSideList2 != null && fightEndSideList2.size() > 0)
		{
			for(ArmyFightingInfo info : fightEndSideList2)
			{
				if(info.getNPCno().equalsIgnoreCase(NPCNo))
				{
					if(info.getCurrentHp() < 0)
					{
						logger.error("worldBossEnd error,this role game have problem,acc="+roleInfo.getAccount()+",name="+roleInfo.getRoleName()+",curHp="+info.getCurrentHp());
						break;
					}
					bossHP = NPChp-(long)info.getCurrentHp();
					break;
				}
			}
		}
		
		// 本地估计BOSS伤害
		int serverCalBossHurt = calWorldBossHurt(fightInfo);
		logger.info("world boss hurt,roleId="+roleInfo.getId()+",roleName="+roleInfo.getRoleName()+",server cal hurt="+serverCalBossHurt+",bossHP="+bossHP);
		if(bossHP > serverCalBossHurt)
		{
			logger.warn("worldBossEnd warn,please check this role,acc="+roleInfo.getAccount()+",name="+roleInfo.getRoleName()
					+",NPChp="+NPChp+",bossHP="+bossHP);
			bossHP = serverCalBossHurt;
			
		}
		
		//获取世界boss血量，判断是否死亡，计算获得的奖励（总伤害/10000）
		String prize = "";	//奖励
		int type = 0;
		List<BossInfo> bossList = MapRoleInfoMap.getWorldBoss();
		if(bossList != null && bossList.size() > 0)
		{
			synchronized(bossList)
			{
				for(BossInfo bossInfo : bossList)
				{
					if(bossInfo.getBossNo() == Integer.parseInt(NPCNo))
					{
						long oldBossHP = bossInfo.getCurrHP();
						type = bossInfo.getBossType();
						if(oldBossHP > 0)
						{
							if(oldBossHP <= bossHP)	//已击杀 给予击杀奖励 删除boss
							{
								iskill = true;
								bossInfo.setCurrHP(0);
								prize = GameValue.WORLD_BOSS_PRIZE_NUM;
								if(WorldBossDAO.getInstance().updateWorldBoss(type, bossInfo.getBosslevel()+1, 
										bossInfo.getBossNo(), bossInfo.getAllHP(), 0, bossInfo.getRate()))
								{
									BossInfo boss = WorldBossMap.getWorldBoss(type);
									boss.setBosslevel(boss.getBosslevel()+1);
									boss.setCurrHP(0);
								}
								else
								{
									//存储失败
									return ErrorCode.WORLD_BOSS_3;
								}
							}
							else
							{
								bossInfo.setCurrHP(oldBossHP-bossHP);
								if(WorldBossDAO.getInstance().updateWorldBossHP(type, bossInfo.getCurrHP()))
								{
									BossInfo boss = WorldBossMap.getWorldBoss(type);
									boss.setCurrHP(bossInfo.getCurrHP());
								}
								else 
								{
									//存储失败
									return ErrorCode.WORLD_BOSS_3;
								}
							}
						}
						break;
					}
				}
			}
		}
		
		//存储攻打boss的时间，总伤害累加存储
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		long hp = roleInfo.getFightWorldBossHp()+bossHP;
		long bestHp = roleInfo.getBestFightBossHp();
		long thisBest = roleInfo.getThisBossBest();
		if(bestHp < hp)
		{
			bestHp = hp;
		}
		if(thisBest < bossHP)
		{
			thisBest = bossHP;
		}
		if(RoleDAO.getInstance().updateFightWorldBoss(nowTime, hp, bestHp,roleInfo.getId(),thisBest))
		{
			WorldBossMap.addbossList(roleInfo,nowTime,hp,bestHp,thisBest);
		}
		else 
		{
			//存储失败
			return ErrorCode.WORLD_BOSS_2;
		}
		
		if(iskill){
			//BOSS击杀广播
			StringBuilder sb = new StringBuilder();
			int week = DateUtil.getWeekDay_Hour(1);
			WorldBossXMLInfo xmlInfo = WorldBossXMLInfoMap.getGoldBuyXMLInfo(week);
			sb.append(Resource.getMessage("game", "WorldbSoss_Die")).append("_").append(xmlInfo.getNPCNo()).append(",").append(roleInfo.getRoleName());
			GmccMgtService.sendChatMessage(sb.toString());
			
			
			List<DiffMail> diffMailList = new ArrayList<DiffMail>();
			List<DropXMLInfo> propBagList = PropBagXMLMap.getPropBagXMLListbyStr(prize);
			// 奖励
			List<DropInfo> addList = new ArrayList<DropInfo>();
			ItemService.getDropXMLInfo(roleInfo, propBagList, addList);
			List<MailAttachment> attachments = new ArrayList<MailAttachment>();
			MailAttachment att = null;
			for (DropInfo drop : addList) {
				att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
				attachments.add(att);
			}
			String attachment = MailDAO.encoderAttachment(attachments);
			String title = Resource.getMessage("game", "BOSS_PRIZE_TITLE2");
			String content = Resource.getMessage("game", "BOSS_PRIZE_CONTENT2")+","+xmlInfo.getNPCNo();
			SimpleDateFormat time = new SimpleDateFormat("HH:mm");
			String reserve = time.format(System.currentTimeMillis());
			DiffMail diffmail = new DiffMail(roleInfo.getId(),attachment,content,title,reserve);
			if(diffmail != null)
			{
				diffMailList.add(diffmail);
			}
			//发送邮件
			DiffMailMessage diffMailMessage = new DiffMailMessage(ETimeMessageType.SEND_BATCH_DIFF_MAIL,diffMailList);
			SendServerMsgThread.addMessage(diffMailMessage);
			//删除世界boss
			List<MapRolePointInfo> list = MapRoleInfoMap.mapToList();
			if(list != null && list.size() > 0)
			{
				for(MapRolePointInfo info : list)
				{
					int roleId = info.getRoleId();
					RoleInfo roleInfo1 = RoleInfoMap.getRoleInfo(roleId);
					if (roleInfo1 == null) 
					{
						continue;
					}
					SceneService1.sendWorldBossDelMessage(roleInfo1);
				}
			}
			
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action478.getType(), 1, attachment);
		}
		
		//战斗结束玩家大地图坐标变化
//		if(pointInfo != null) {
//			String[] xy = GameValue.WORLD_BOSS_END_FIGHT_XY.split(",");
//			if(xy.length==2){
//				pointInfo.setPointX(Float.valueOf(xy[0]));
//				pointInfo.setPointZ(Float.valueOf(xy[1]));
//			}
//		}
		
		// 任务检测
		QuestService.checkQuest(roleInfo, ActionType.action367.getType(), null, true, false);
		
		//发放战斗结束奖励
		List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLList(GameValue.WORLD_EVERY_FIGHT_REWARD);
		int check = ItemService.addPrizeForPropBag(ActionType.action369.getType(), roleInfo, list,null, prizeList, null, null, null, true);
		if (check != 1) return check;
			
		//计入boss表日志
		WorldBossFightLog bossLog = new WorldBossFightLog();
		bossLog.setAccount(roleInfo.getAccount());
		bossLog.setRoleName(roleInfo.getRoleName());
		bossLog.setBeginTime(new Timestamp(fightInfo.getFightTime()));
		bossLog.setEndTime(new Timestamp(System.currentTimeMillis()));
		bossLog.setHurt(bossHP);
		GameLogService.insertWorldBossLog(bossLog);
		return 1;
	}
	
	/**
	 * 每天 21：05 刷新世界BOSS
	 * @return
	 */
	public static void sendWorldBoss() 
	{
		int week = DateUtil.getWeekDay_Hour(1);
		MapRoleInfoMap.removeBoss();
		WorldBossXMLInfo xmlInfo = WorldBossXMLInfoMap.getGoldBuyXMLInfo(week);
		if(xmlInfo != null) 
		{
			int no = xmlInfo.getNo();
			
			BossInfo bossInfo = WorldBossMap.getWorldBoss(no);
			if(bossInfo == null)	
			{
				//未打过此boss第一次刷新
				bossInfo = new BossInfo();
				bossInfo.setBossType(xmlInfo.getNo());
				bossInfo.setBosslevel(1);
				bossInfo.setBossNo(xmlInfo.getNPCNo());
				bossInfo.setAllHP(xmlInfo.getHp());
				bossInfo.setCurrHP(xmlInfo.getHp());
				bossInfo.setRate(xmlInfo.getRate());
				bossInfo.setMapNo(xmlInfo.getMapCityNo());
				
				if(WorldBossDAO.getInstance().insertWorldBoss(bossInfo))
				{
					WorldBossMap.addWordList(bossInfo);
					MapRoleInfoMap.addWorldBoss(bossInfo);
				}else{
					System.err.println("sendWorldBossError---Insert----"+no);
				}
			}
			else 
			{
				//计算hp
				int level = bossInfo.getBosslevel();
				float rate = xmlInfo.getRate() * level;
				long hp = (long)((1+rate) * xmlInfo.getHp());
				
				if(WorldBossDAO.getInstance().updateWorldBoss(bossInfo.getBossType(), 
						bossInfo.getBosslevel(), bossInfo.getBossNo(), hp, hp, rate))
				{
					bossInfo.setAllHP(hp);
					bossInfo.setCurrHP(hp);
					bossInfo.setRate(rate);
					
					MapRoleInfoMap.addWorldBoss(bossInfo);
				}else{
					System.err.println("sendWorldBossError---Update-----"+no);
				}
				
			}
			//清空前一天的历史数据(今日伤害，今日排行)
			if(RoleDAO.getInstance().clearFightWorldBoss())
			{
				Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
				if(roleMap != null && roleMap.size() > 0)
				{
					for(RoleInfo info : roleMap.values())
					{
						if(info != null)
						{
							info.setFightWorldBossHp(0);
							info.setThisBossBest(0);
							//info.setBossRank(0);
						}
					}
				}
				WorldBossMap.clearBossList();
			}
			
			//推送世界地图刷新 0xAC0C
			List<MapRolePointInfo> list = MapRoleInfoMap.mapToList();
			if(list != null && list.size() > 0)
			{
				for(MapRolePointInfo info : list)
				{
					int roleId = info.getRoleId();
					RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
					if (roleInfo == null) 
					{
						continue;
					}
					SceneService1.sendWorldBossAddMessage(roleInfo);
				}
			}else{
				System.err.println("sendWorldBossError----MapNoRole----"+no);
			}
			//广播公告
			StringBuilder sb = new StringBuilder();
			sb.append(Resource.getMessage("game", "WorldbSoss_Start")).append("_").append(xmlInfo.getNPCNo()).append("%").append(GameValue.WORLD_BOSS);
			GmccMgtService.sendChatMessage(sb.toString());
		}
	}
	
	/**
	 * 每天 21：35 删除
	 * @return
	 */
	public static void clearWorldBoss() 
	{
		List<BossInfo> bosslist = MapRoleInfoMap.getWorldBoss();
		//String mapNo = ""; //获取地图No
		if(bosslist != null && bosslist.size() > 0)
		{
			//删除世界boss
			List<MapRolePointInfo> list = MapRoleInfoMap.mapToList();
			if(list != null && list.size() > 0)
			{
				for(MapRolePointInfo info : list)
				{
					int roleId = info.getRoleId();
					RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
					if (roleInfo == null) 
					{
						continue;
					}
					SceneService1.sendWorldBossDelMessage(roleInfo);
				}
			}
			//如果BOSS未被击杀.----结束广播
			if(bosslist.get(0).getCurrHP() > 0){
				int week = DateUtil.getWeekDay_Hour(1);
				WorldBossXMLInfo xmlInfo = WorldBossXMLInfoMap.getGoldBuyXMLInfo(week);
				StringBuilder sb = new StringBuilder();
				sb.append(Resource.getMessage("game", "WorldbSoss_End")).append("_").append(xmlInfo.getNPCNo());
				GmccMgtService.sendChatMessage(sb.toString());
			}
			//删除boss缓存
			MapRoleInfoMap.removeBoss();
		}
	}
	
	/**
	 * 根据排行榜给予世界boss奖励
	 */
	public static void sendWorldBossPrize()
	{
		List<RoleInfo> list = WorldBossMap.getBossList();
		List<DiffMail> diffMailList = new ArrayList<DiffMail>();
		for (int i=0;i<list.size();i++) {
			RoleInfo info = list.get(i);
			int roleId = info.getId();
			int rank = i+1;
			String prize = "";
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			//根据排名rank给予奖励
			if (roleInfo != null) 
			{
				//获得奖励 把邮件存储到diffMailList
				prize = WorldBossXMLInfoMap.getPrize(rank);
				List<DropXMLInfo> dropList = PropBagXMLMap.getPropBagXMLListbyStr(prize);
				// 奖励
				List<DropInfo> addList = new ArrayList<DropInfo>();

				ItemService.getDropXMLInfo(roleInfo, dropList, addList);

				List<MailAttachment> attachments = new ArrayList<MailAttachment>();
				MailAttachment att = null;
				for (DropInfo drop : addList) {
					att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
					attachments.add(att);
				}
				String attachment = MailDAO.encoderAttachment(attachments);
				if (attachment.length() <= 0) {
					break;
				}

				String title = Resource.getMessage("game", "BOSS_PRIZE_TITLE1");
				String content = Resource.getMessage("game", "BOSS_PRIZE_CONTENT1");
				SimpleDateFormat time = new SimpleDateFormat("HH:mm");
				String reserve = time.format(System.currentTimeMillis()) + "," + (i+1);
				
				DiffMail diffmail = new DiffMail(roleInfo.getId(),attachment,content,title,reserve);
				
				if(diffmail != null)
				{
					diffMailList.add(diffmail);
				}
			}
		}
		
		TitleService.getNewRanks4WorldBoss(); //世界BOSS称号刷新
		
		//发送邮件
		DiffMailMessage diffMailMessage = new DiffMailMessage(ETimeMessageType.SEND_BATCH_DIFF_MAIL,diffMailList);
		SendServerMsgThread.addMessage(diffMailMessage);
	}

	/**
	 * 计算世界BOSS的伤害值
	 * @param roleInfo
	 * @return
	 */
	public static int calWorldBossHurt(FightInfo fightInfo)
	{
		// （攻击力+技能强度）*（1.5+暴伤加成/10000）*6.15*秒
		// (1+（（攻击方暴击+技能暴击）^0.001-1）*（攻击方暴击+技能暴击）^0.36*（1.5+暴伤加成/10000）)*（攻击力+技能强度）*6.15*秒

		float hurt = 0;
		float time = (System.currentTimeMillis() - fightInfo.getFightTime())/1000f;
		List<FightSideData> fightDataList = fightInfo.getFightDataList();
		for(FightSideData sideData : fightDataList)
		{
			if(sideData == null || sideData.getSideId() == 1)
			{
				continue;
			}
			List<FightArmyDataInfo> armyInfos = sideData.getArmyInfos();
			for(FightArmyDataInfo armyInfo : armyInfos)
			{
				if(armyInfo == null)
				{
					continue;
				}
				//hurt += ((armyInfo.getAttack() + armyInfo.getMagicAttack())*(1.5+armyInfo.getCritMore()/10000)*6.15*time);
				hurt += 
					(1+(Math.pow((armyInfo.getCrit() + armyInfo.getSkillCrit()),0.001)-1)
							*Math.pow((armyInfo.getCrit() + armyInfo.getSkillCrit()),0.36)*
							(1.5+armyInfo.getCritMore()/10000))*
					(armyInfo.getAd() + armyInfo.getMagicAttack())*3*time;
				
				logger.info("calWorldBossHurt===armyInfo.getCrit="+armyInfo.getCrit()+",armyInfo.getSkillCrit="+armyInfo.getSkillCrit()+
						",armyInfo.getCritMore="+armyInfo.getCritMore()+",armyInfo.getAttack="+armyInfo.getAd()+",armyInfo.getMagicAttack="+armyInfo.getMagicAttack()+
						",time="+time);
			}
			
			
		}
		
		return (int) hurt;
	}
}
