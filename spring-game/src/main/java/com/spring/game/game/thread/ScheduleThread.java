package com.snail.webgame.game.thread;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.mina.common.IoSession;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.gmcc.message.GmccSendMessage;
import com.snail.webgame.game.cache.FightCompetitionRankList;
import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.charge.text.util.TextUtil;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.StageXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.GmccGameService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.core.TaskService;
import com.snail.webgame.game.dao.ActivityDao;
import com.snail.webgame.game.dao.CompetitionDao;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.dao.GameSettingDAO;
import com.snail.webgame.game.dao.RoleChargeDAO;
import com.snail.webgame.game.dao.RoleChargeErrorDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.CompetitionPersistentInfo;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleChargeInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.app.common.AppStoreExInfo;
import com.snail.webgame.game.protocal.app.common.EChargeState;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.arena.service.ArenaService;
import com.snail.webgame.game.protocal.checkIn.service.CheckInService;
import com.snail.webgame.game.protocal.fight.competition.service.CompetitionService;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualFightCountResp;
import com.snail.webgame.game.protocal.fight.team.refresh.Team3V3FightCountResp;
import com.snail.webgame.game.protocal.fight.team.refresh.TeamFightCountResp;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;
import com.snail.webgame.game.protocal.mine.service.MineService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreThread;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;
import com.snail.webgame.game.protocal.worldBoss.service.WorldBossMgtService;
import com.snail.webgame.game.protocal.worship.service.WorshipMgtService;

/**
 * 定时线程
 * 
 * @author zhoubo
 * @version 1.00
 * @since 2011-9-9
 */
public class ScheduleThread {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	// 设置三个线程池可以将有些活动错开放置,防止同一时间线种被占用导致处理不及时，各定时线程合理分配。 
	// 原则上对库进行批量操作的线程放到一起，防止出现数据库死锁。
	
	// 执行耗时的线程
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	
	// 执行快的线程,各线程时间冲突较少
	private static ScheduledExecutorService service1 = Executors.newScheduledThreadPool(1);
	
	// 定时对在线玩家或全服玩家更新库的操作
	private static ScheduledExecutorService service2 = Executors.newScheduledThreadPool(1);
	
	public static long SERVER_START_TIME = 0l;

	public static boolean start() {
		
		// 每月竞技场状态清除
		GameSettingInfo gameSettingInfo = GameSettingMap.getValue(GameSettingKey.SERVER_START_TIME);
		
		if (gameSettingInfo != null) {
			long monthSeconds = GameValue.COMPETION_FIGHT_CLEAN_DAYS * 24 * 3600; // 每月秒数
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			long serverStartTime = 0; // 服务器开服时间
			int passCounts = 0;// 已经结算过的次数
			
			try {
				Calendar c = Calendar.getInstance();
				c.setTime(df.parse(gameSettingInfo.getValue()));
				serverStartTime = c.getTimeInMillis();
				SERVER_START_TIME = serverStartTime;
				
				if (gameSettingInfo.getComment() != null && !"".equals(gameSettingInfo.getComment())) {
					passCounts = Integer.parseInt(gameSettingInfo.getComment());
				}
				
				// 应该结算的次数
				long counts = (System.currentTimeMillis() - serverStartTime) / (monthSeconds * 1000);
				
				if (counts > passCounts) {
					// 结算时间服务器没有开启，先进行一次结算
					service2.scheduleAtFixedRate(new CompetitionTask(), 1, monthSeconds, TimeUnit.SECONDS);
					
					logger.info("competition task will start after " + monthSeconds + " second");
				} else {
					long passTime = (System.currentTimeMillis() - serverStartTime) % (monthSeconds * 1000);
					service2.scheduleAtFixedRate(new CompetitionTask(), monthSeconds - passTime / 1000, monthSeconds, TimeUnit.SECONDS);
					
					logger.info("competition task will start after " + (monthSeconds - passTime / 1000) + " second");
				}
			} catch (ParseException e) {
				logger.error("SERVER_START_TIME value format error, value = " + GameSettingMap.getValue(GameSettingKey.SERVER_START_TIME), e);
			} catch (NumberFormatException e) {
				logger.error("SERVER_START_TIME value format error, comment = " + gameSettingInfo.getComment(), e);
			}
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("GameSetting table is not exist SERVER_START_TIME");
			}
		}
		
		// 循环查询队列里的礼物
		service2.scheduleAtFixedRate(new QueryChargeGiftTask(), 0, 5, TimeUnit.SECONDS);
		
		// 5分钟查询一次全服在线玩家的礼物
		service.scheduleAtFixedRate(new QueryChargeAllGiftTask(), 6, 10, TimeUnit.MINUTES);

		// 0:00 刷新活动信息
		service2.scheduleAtFixedRate(new DayRefresh(),getEveryDayTimePoint(0,0), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 检测礼包开启和关闭
		service1.scheduleAtFixedRate(new ToolBoxTask(), 1, 1, TimeUnit.MINUTES);
		
		// 检测运营时限活动开启和关闭
		service.scheduleAtFixedRate(new ToolOpActTask(), 1, 1, TimeUnit.MINUTES);
				
		// 查询运营活动是否有变化
		service.scheduleAtFixedRate(new checkToolProgramTask(), 0, 10, TimeUnit.SECONDS);
		
		//每30分钟更新一次排行榜
		service1.scheduleAtFixedRate(new Rank(), 0, 30, TimeUnit.MINUTES);
		
		//大R每日定时自动增加膜拜值
		service1.scheduleAtFixedRate(new SuperRPlusRandom(), 0, GameValue.BETWEEN_SUPERR_PLUSBASE_TIME, TimeUnit.MINUTES);
		
		// 每天 21：00 发放竞技场排名奖励
		service.scheduleAtFixedRate(new ArenaPrizeTask(), getEveryDayHMTimePoint(GameValue.ARENA_SEND_PLACE_TIME),
				24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 23：00 发放跨服竞技场排名奖励
		service.scheduleAtFixedRate(new KuafuPrizeTask(), getEveryDayHMTimePoint(GameValue.KUAFU_SEND_PLACE_TIME),
				24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 00:00发放大R被膜拜奖励并刷新可膜拜数和被膜拜数
		service2.scheduleAtFixedRate(new SuperRTask(), getEveryDayHMTimePoint(GameValue.BE_WORSHIP_PRIZE_SEND_TIME),
				24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 12：00推送午餐任务
		service1.scheduleAtFixedRate(new SpDailyQuestStart(GameValue.LUNCH_QUEST_PROTO＿NO), getEveryDayTimePoint(12,0), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 14：00 推送午餐结束
		service1.scheduleAtFixedRate(new SpDailyQuestEnd(GameValue.LUNCH_QUEST_PROTO＿NO), getEveryDayTimePoint(14,0), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 18：00推送晚餐任务
		service1.scheduleAtFixedRate(new SpDailyQuestStart(GameValue.NIGHT_QUEST_PROTO＿NO), getEveryDayTimePoint(18,0), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 20：00 推送晚餐结束
		service1.scheduleAtFixedRate(new SpDailyQuestEnd(GameValue.NIGHT_QUEST_PROTO＿NO), getEveryDayTimePoint(20,0), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天 05：00 清理过期充值订单信息
		service1.scheduleAtFixedRate(new ClearChargeOrderInfo(), getEveryDayTimePoint(5, 0), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		//每天刷新推送第一场世界BOSS
		service1.scheduleAtFixedRate(new AddWorldBoss(), getEveryDayTimePoint(Integer.valueOf(GameValue.WORLD_BOSS_BEGIN1.split(":")[0]),Integer.valueOf(GameValue.WORLD_BOSS_BEGIN1.split(":")[1])), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		//每天删除推送第一场世界BOSS
		service1.scheduleAtFixedRate(new DELWorldBoss(), getEveryDayTimePoint(Integer.valueOf(GameValue.WORLD_BOSS_END1.split(":")[0]),Integer.valueOf(GameValue.WORLD_BOSS_END1.split(":")[1])), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		//每天刷新推送第二场世界BOSS
		service1.scheduleAtFixedRate(new AddWorldBoss(), getEveryDayTimePoint(Integer.valueOf(GameValue.WORLD_BOSS_BEGIN2.split(":")[0]),Integer.valueOf(GameValue.WORLD_BOSS_BEGIN2.split(":")[1])), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		//每天删除推送第二场世界BOSS
		service1.scheduleAtFixedRate(new DELWorldBoss(), getEveryDayTimePoint(Integer.valueOf(GameValue.WORLD_BOSS_END2.split(":")[0]),Integer.valueOf(GameValue.WORLD_BOSS_END2.split(":")[1])), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		// 每天定时发送世界BOSS排行奖励
		service2.scheduleAtFixedRate(new SendWorldBossReward(),getEveryDayTimePoint(Integer.valueOf(GameValue.WORLD_BOSS_ONE_REWARD.split(":")[0]),Integer.valueOf(GameValue.WORLD_BOSS_ONE_REWARD.split(":")[1])), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		service2.scheduleAtFixedRate(new SendWorldBossReward(),getEveryDayTimePoint(Integer.valueOf(GameValue.WORLD_BOSS_TWO_REWARD.split(":")[0]),Integer.valueOf(GameValue.WORLD_BOSS_TWO_REWARD.split(":")[1])), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		
		Calendar c = Calendar.getInstance();
		c.setTime(GameValue.COMPETITION_DOUBLE_START_TIME);
		service.scheduleAtFixedRate(new KfArenaFightStartBrocast(),getEveryDayTimePoint(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		// 统计10分钟注册、10分钟在线、10分钟开卡
		service1.scheduleAtFixedRate(new CollectionUserInfoTask(), 10, 10, TimeUnit.MINUTES);
		
		// 清除下线用户缓存 1小时
		service.scheduleAtFixedRate(new clearRoleDataInactive(), 3600, 3600, TimeUnit.SECONDS);
		
		// 计费服务器在线人数统计
		service1.scheduleAtFixedRate(new onlineRoleNumToCharge(), 600, 600, TimeUnit.SECONDS);
		
		//每20分钟更新角色的登陆日志
		//service.scheduleAtFixedRate(new updateRoleLoginLog(), 20, 20, TimeUnit.MINUTES);
		
		//每30分钟所有日志入库
		service.scheduleAtFixedRate(new updateAllLeftLog(), 17, 30, TimeUnit.MINUTES);
		
		//每10分钟检测服务器各线程情况
		service2.scheduleAtFixedRate(new ServerCheck(), 10, 10, TimeUnit.MINUTES);
				
		c.setTime(GameValue.MUTUAL_FIGHT_START_TIME_1);
		
		//对攻战战斗开始时间1
		service1.scheduleAtFixedRate(new MutualFightStartBrocast(), getEveryDayTimePoint(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		c.setTime(GameValue.MUTUAL_FIGHT_START_TIME_2);
		
		//对攻战战斗开始时间2
		service1.scheduleAtFixedRate(new MutualFightStartBrocast(), getEveryDayTimePoint(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);

		c.setTime(GameValue.TEAM_FIGHT_START_TIME_1);
		
		//对攻战战斗开始时间1
		service1.scheduleAtFixedRate(new Team3V3FightStartBrocast(), getEveryDayTimePoint(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		c.setTime(GameValue.TEAM_FIGHT_START_TIME_2);
		
		//对攻战战斗开始时间2
		service1.scheduleAtFixedRate(new Team3V3FightStartBrocast(), getEveryDayTimePoint(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
		
		String[] yabiaoTimes = GameValue.BIAO_CHE_TIME.split(",");
		try {
			for(String yabiaoTimeStr : yabiaoTimes){
				String[] yabiaoTime = yabiaoTimeStr.split("-");
				String startHour = yabiaoTime[0].split(":")[0];
				String endHour = yabiaoTime[1].split(":")[0];
				String startMin = yabiaoTime[0].split(":")[1];
				String endMin = yabiaoTime[1].split(":")[1];
				
				//押镖双倍活动开始广播
				service1.scheduleAtFixedRate(new BiaocheDoubleStartBrocast(), getEveryDayTimePoint(Integer.valueOf(startHour), Integer.valueOf(startMin)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
				
				//押镖双倍活动结束广播
				service1.scheduleAtFixedRate(new BiaocheDoubleEndBrocast(), getEveryDayTimePoint(Integer.valueOf(endHour), Integer.valueOf(endMin)), 24 * 3600 * 1000, TimeUnit.MILLISECONDS);
			}
		} catch (Exception e) {
			logger.warn("ScheduleThread start biaoche error ! check start time parameter ...");
		}
		
		//每30分钟更新在线玩家的战斗力
		service2.scheduleAtFixedRate(new updateRoleFightValue(), 30, 30, TimeUnit.MINUTES);
		
		// 每5分钟检测大地图矿变化
		service.scheduleAtFixedRate(new checkMine(), 3, 5, TimeUnit.MINUTES);
		
		// 每1分钟检测GMCC连接
		if(GameValue.GAME_GMCC_FLAG == 1)
		{
			service.scheduleAtFixedRate(new checkGMCC(), 1, 1, TimeUnit.MINUTES);
		}

		// 检测需要重新补发的单据
		service1.scheduleAtFixedRate(new checkOrderTask(), 1, 1, TimeUnit.MINUTES);
		
		return true;
	}

	public static void shutdown() {
		if (!service.isShutdown()) {
			service.shutdown();
		}
		
		if (!service1.isShutdown()) {
			service1.shutdown();
		}
		
		if (!service2.isShutdown()) {
			service2.shutdown();
		}
	}

	/**
	 * 取得到达下一周时间点的时间间隔(每周任务)
	 * 
	 * @param weekPoint
	 *            星期几(考虑国家的不同以星期日为每周第一天)
	 * @param timePoint
	 *            时间点
	 * @return long 时间间隔
	 */
/*	private static long getEveryWeekTimePoint(int weekPoint, int timePoint) {
		long delayTime = 0;

		Calendar curTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		endTime.set(Calendar.DAY_OF_WEEK, weekPoint + 1);
		endTime.set(Calendar.HOUR_OF_DAY, timePoint);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);
		endTime.set(Calendar.MILLISECOND, 0);

		long lCurTime = curTime.getTimeInMillis();
		long lEndTime = endTime.getTimeInMillis();

		if (lCurTime > lEndTime) {
			endTime.add(Calendar.DAY_OF_MONTH, 7);
			lEndTime = endTime.getTimeInMillis();
			delayTime = lEndTime - lCurTime;
		} else {
			delayTime = lEndTime - lCurTime;
		}
		return delayTime;
	}*/
	
	/**
	 * 取得到达下一时间点的时间间隔(每日任务)
	 * @param timePoint
	 * @param minute
	 * @return
	 */
	public static long getEveryDayTimePoint(int timePoint, int minute) {
		long delayTime = 0;

		Calendar curTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		endTime.set(Calendar.HOUR_OF_DAY, timePoint);
		endTime.set(Calendar.MINUTE, minute);
		endTime.set(Calendar.SECOND, 0);
		endTime.set(Calendar.MILLISECOND, 0);

		if (curTime.getTimeInMillis() < endTime.getTimeInMillis()) {
			delayTime = endTime.getTimeInMillis() - curTime.getTimeInMillis();
		} else {
			endTime.add(Calendar.DAY_OF_MONTH, 1);
			delayTime = endTime.getTimeInMillis() - curTime.getTimeInMillis();
		}

		return delayTime;
	}
	
	/**
	 * 取得到达下一时间点的时间间隔(每日任务)
	 * @param times（hh:mm）
	 * @return
	 */
	public static long getEveryDayHMTimePoint(String times) {
		long delayTime = 0;
		long now = System.currentTimeMillis();
		long endTime = DateUtil.getTodayHMTime(times);
		if (now <= endTime) {
			//当日没到点 到点间隔
			delayTime = endTime - now;
		} else {
			//当日过点 第二天到点间隔
			endTime += 24 * 3600 * 1000;
			delayTime = endTime - now;
		}
		return delayTime;
	}
}

/**
 * 
 * 类介绍:竞技场定时类，用于清除跨服战斗状态
 *
 * @author zhoubo
 * @2014-11-28
 */
class CompetitionTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public void run() {
		try {
			int batchSize = 200; // 匹配更新数据库条数
			
			Set<Entry<Integer, RoleInfo>> set = RoleInfoMap.getMap().entrySet();
			List<CompetitionPersistentInfo> list = new ArrayList<CompetitionPersistentInfo>();
			int index = 0;
			
			for (Entry<Integer, RoleInfo> entry : set) {
				RoleInfo roleInfo = entry.getValue();

				if (roleInfo != null) {
					synchronized (roleInfo) {
						RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
						
						if (roleLoadInfo != null) {
							roleLoadInfo.setWinTimes(0);
							roleLoadInfo.setLoseTimes(0);
							roleLoadInfo.setStageWinTimes((byte)0);
							roleLoadInfo.setStageLoseTimes((byte)0);
							roleLoadInfo.setStageState((byte)0);
							roleLoadInfo.setCompetitionAward(StageXMLInfoMap.minStageXMLInfo.getStageBit());
						}
						
						roleInfo.setTorrentTimes((byte)0);
						roleInfo.setCompetitionValue(0);
						roleInfo.setCompetitionStage(StageXMLInfoMap.minStageXMLInfo.getId());
						roleInfo.setCompetitionTime(0);
						roleInfo.setStageAward(StageXMLInfoMap.minStageXMLInfo.getStageBit());
						roleInfo.setCompetitionAgainstInfoList(null);
						
						
						CompetitionPersistentInfo competitionPersistentInfo = new CompetitionPersistentInfo();
						competitionPersistentInfo.setWinTimes(0);
						competitionPersistentInfo.setLoseTimes(0);
						competitionPersistentInfo.setStageWinTimes((byte)0);
						competitionPersistentInfo.setStageLoseTimes((byte)0);
						competitionPersistentInfo.setStageState((byte)0);
						competitionPersistentInfo.setCompetitionStage(StageXMLInfoMap.minStageXMLInfo.getId());
						competitionPersistentInfo.setCompetitionValue(0);
						competitionPersistentInfo.setCompetitionTime(0);
						competitionPersistentInfo.setCompetitionAward(StageXMLInfoMap.minStageXMLInfo.getStageBit());
						competitionPersistentInfo.setStageAward(StageXMLInfoMap.minStageXMLInfo.getStageBit()); 
						competitionPersistentInfo.setId(roleInfo.getId());
						
						list.add(competitionPersistentInfo);
					}
				}
				
				if (index++ % 1000 == 0) {
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			// 全局排名清空
			FightCompetitionRankList.cleanRank();
			
			if (list.size() > 0) {
				// 需要更新
				int subs = list.size() / batchSize;
				int mod = list.size() % batchSize;
				
				if (subs == 0) {
					CompetitionDao.getInstance().updateRolesCompetitionInitValue(list);
				} else {
					int lastIndex = 0;
					
					for (int i = 0; i < subs; i++) {
						try{
							CompetitionDao.getInstance().updateRolesCompetitionInitValue(list.subList(batchSize * i, batchSize * i + batchSize));
							
							try {
								TimeUnit.SECONDS.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							lastIndex = batchSize * (i + 1);
						}
						catch(Exception e1)
						{
							logger.error("CompetitionTask CompetitionDao error", e1);
							continue;
						}
					}
					
					if (mod > 0) {
						CompetitionDao.getInstance().updateRolesCompetitionInitValue(list.subList(lastIndex, lastIndex + mod));
					}
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("CompetitionTask error", e);
			}
		} finally {
			GameSettingInfo gameSettingInfo = GameSettingMap.getValue(GameSettingKey.SERVER_START_TIME);
			if(gameSettingInfo != null){
				try {
					String comment = gameSettingInfo.getComment();
					if (comment == null || "".equals(comment)) {
						comment = "1";
					} else {
						int passCounts = Integer.parseInt(comment);
						comment = String.valueOf((passCounts + 1));
					}
					if(GameSettingDAO.getInstance().updateGameSettingComment(GameSettingKey.SERVER_START_TIME, comment)){
						gameSettingInfo.setComment(comment);
					}
				} catch (Exception e2) {
					if (logger.isErrorEnabled()) {
						logger.error("CompetitionTask error", e2);
					}
				}
			}
		}
		
		logger.info("------ScheduleThread CompetitionTask,time="+new Timestamp(System.currentTimeMillis()));
	}
}

class QueryChargeGiftTask implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public void run() {
		try {
			TaskService.queryChargeGift();
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error(e.getMessage());
			}
		}
		
		//logger.info("------ScheduleThread QueryChargeGiftTask,time="+new Timestamp(System.currentTimeMillis()));
	}
	
}

class ToolBoxTask implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public void run() {
		try {
			VipShopMgtService.checkToolBoxStartAndEnd();
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error(e.getMessage());
			}
		}
		
		//logger.info("------ScheduleThread ToolBoxTask,time="+new Timestamp(System.currentTimeMillis()));
	}
	
}

class checkOrderTask implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	private static final RoleChargeErrorDao dao = new RoleChargeErrorDao();
	
	public void run() {
		try {
			RoleChargeInfo roleChargeInfo;
			AppStoreExInfo info;
			long currTime = System.currentTimeMillis();
			
			for(Entry<String, RoleChargeInfo> entry : RoleChargeMap.getMap().entrySet()){
				roleChargeInfo = entry.getValue();
				
				if(roleChargeInfo != null && 
						(roleChargeInfo.getState() == EChargeState.EXCEPTION_SEND_TO_TRANSIT_SERVER
						|| roleChargeInfo.getState() == EChargeState.HAS_SEND_TO_TRANSIT_SERVER
						|| roleChargeInfo.getState() == EChargeState.FAILED_SEND_TO_TRANSIT_SERVER
						|| (roleChargeInfo.getState() == EChargeState.RECEIVE_FROM_TRANSIT_SERVER_ERRORCODE && roleChargeInfo.getErrorCode() != 0))){
					
					// 如果状态为“已发中转服”，还没20秒的话，也不重发
					if(roleChargeInfo.getState() == EChargeState.HAS_SEND_TO_TRANSIT_SERVER && currTime - roleChargeInfo.getChargeTime().getTime() < 20000){
						continue;
					}
					
					if(roleChargeInfo.getReTryTimes() > 120){
						continue;
					} else {
						roleChargeInfo.setReTryTimes(roleChargeInfo.getReTryTimes() + 1);
					}
					
					info = dao.selectInfoById(roleChargeInfo.getSeqId());

					if (info != null) {
						String agentOrderId = info.getsAgentOrderId(); // 订单号
						String sImprestAccountIP = info.getsImprestAccountIP(); // 充值人IP
						String sCardType = info.getsCardType(); // 卡类型
						String totalValue = "" + info.getTotalValue(); // 总值(人民币数)
						String sUserName = info.getsUserName();
						String sAccountTypeID = "" + info.getsAccountTypeID(); // 账户类型(0-中心 // , 1- 分区)
						String sAreaID = "" + info.getsAreaID(); // 充入分区ＩＤ（如果有赠送道具，也赠入此分区ＩＤ）
						String jsonData = agentOrderId.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll("\f", "").replaceAll("\b", "");
						
						if(jsonData.indexOf("}") != -1){
							jsonData = jsonData.substring(0, jsonData.indexOf("}") + 1);
						}
						boolean send = TextUtil.sendMessage("AppleCharge", jsonData, sCardType, sUserName, sImprestAccountIP, 
								info.getAmount(), Integer.valueOf(totalValue), Integer.valueOf(sAccountTypeID), Integer.valueOf(sAreaID),"" , String.valueOf(roleChargeInfo.getSeqId()));
						
						if(!send){
							if(logger.isErrorEnabled()){
								logger.error("auto reSendOrder failure ... account = " + sUserName + ", seqId = " + roleChargeInfo.getSeqId());
							}
						}
					} else {
						if(logger.isErrorEnabled()){
							logger.error("auto reSendOrder failure ... seqId not exisit , seqId = " + roleChargeInfo.getSeqId());
						}
					}
					
				}
			}
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error(e.getMessage());
			}
		}
		
		//logger.info("------ScheduleThread ToolBoxTask,time="+new Timestamp(System.currentTimeMillis()));
	}
	
}

class ToolOpActTask implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public void run() {
		try {
			OpActivityService.checkOpActStartAndEnd();
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error(e.getMessage());
			}
		}
		
		//logger.info("------ScheduleThread ToolOpActTask,time="+new Timestamp(System.currentTimeMillis()));
	}
	
}

class checkToolProgramTask implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public void run() {
		try {
			long timeInMilles = System.currentTimeMillis();
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_EXP, timeInMilles)) {
				GameValue.GAME_TOOL_EXP_RAND = GameValue.GAME_TOOL_EXP_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_EXP_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_MONEY, timeInMilles)) {
				GameValue.GAME_TOOL_MONEY_RAND = GameValue.GAME_TOOL_MONEY_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_MONEY_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_COURAGE, timeInMilles)) {
				GameValue.GAME_TOOL_COURAGE_RAND = GameValue.GAME_TOOL_COURAGE_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_COURAGE_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_JUSTICE, timeInMilles)) {
				GameValue.GAME_TOOL_JUSTICE_RAND = GameValue.GAME_TOOL_JUSTICE_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_JUSTICE_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_KUAFUMONEY, timeInMilles)) {
				GameValue.GAME_TOOL_KUAFUMONEY_RAND = GameValue.GAME_TOOL_KUAFUMONEY_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_KUAFUMONEY_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_TEAMMONEY, timeInMilles)) {
				GameValue.GAME_TOOL_TEAMMONEY_RAND = GameValue.GAME_TOOL_TEAMMONEY_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_TEAMMONEY_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_EXPLOIT, timeInMilles)) {
				GameValue.GAME_TOOL_EXPLOIT_RAND = GameValue.GAME_TOOL_EXPLOIT_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_EXPLOIT_RAND = 1;
			}
			if (TaskService.checkProgramTime(GameValue.GAME_TOOL_ACTIVE_TYPE_HERO_BATTLE, timeInMilles)) {
				GameValue.GAME_TOOL_HERO_BATTLE_RAND = GameValue.GAME_TOOL_HERO_BATTLE_RAND_TEMP;
			} else {
				GameValue.GAME_TOOL_HERO_BATTLE_RAND = 1;
			}
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error(e.getMessage());
			}
		}
		
		//logger.info("------ScheduleThread checkToolProgramTask,time="+new Timestamp(System.currentTimeMillis()));
	}
	
}
/**
 * 0:00 刷新活动信息
 * @author zenggang
 */
class DayRefresh implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public void run() {
		try{
			Set<Entry<Integer, RoleInfo>> roleMap = RoleInfoMap.getRoleInfoEntrySet();
			int index = 0;			
			for (Entry<Integer, RoleInfo> entry : roleMap) 
			{
				RoleInfo roleInfo = entry.getValue();
				if(roleInfo == null){
					continue;
				}
				synchronized (roleInfo) {
					// 宝石活动
					//SceneService.sendRoleRefreshMsg(roleId, SceneService.REFRESH_TYPE_GEM, "");
					
					int LBCNum1 = GameValue.ACTIVITY_EXP_TIMES_1 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
					int LBCNum2 = GameValue.ACTIVITY_EXP_TIMES_2 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
					int LBCNum3 = GameValue.ACTIVITY_EXP_TIMES_3 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
					int LBCNum4 = GameValue.ACTIVITY_EXP_TIMES_4 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
					int LBCNum5 = GameValue.ACTIVITY_EXP_TIMES_5 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
					int LBCNum6 = GameValue.ACTIVITY_EXP_TIMES_6 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);

					// 经验活动(练兵场)，银币活动(暂时未开放)
					if(roleInfo.getExpLeftTimes1() != LBCNum1
							|| roleInfo.getExpLeftTimes2() != LBCNum2
							|| roleInfo.getExpLeftTimes3() != LBCNum3
							|| roleInfo.getExpLeftTimes4() != LBCNum4
							|| roleInfo.getExpLeftTimes5() != LBCNum5
							|| roleInfo.getExpLeftTimes6() != LBCNum6
							|| roleInfo.getMoneyLeftTimes() != GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES){
						ActivityDao.getInstance().updateExpMoneyActivityLeftTimes(roleInfo.getId(),
								(byte) LBCNum1,(byte) LBCNum2,(byte) LBCNum3,(byte) LBCNum4,(byte) LBCNum5,(byte) LBCNum6,
								GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES);
					}
					
					// 经验活动(练兵场)，银币活动(暂时未开放)
					roleInfo.setExpLeftTimes1((byte) LBCNum1);
					roleInfo.setExpLeftTimes2((byte) LBCNum2);
					roleInfo.setExpLeftTimes3((byte) LBCNum3);
					roleInfo.setExpLeftTimes4((byte) LBCNum4);
					roleInfo.setExpLeftTimes5((byte) LBCNum5);
					roleInfo.setExpLeftTimes6((byte) LBCNum6);
					roleInfo.setMoneyLeftTimes(GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES);

					// 组队副本奖励次数
					if(roleInfo.getRoleLoadInfo() != null && roleInfo.getRoleLoadInfo().getTeamChallengeTimes() != null){
						roleInfo.getRoleLoadInfo().setTeamChallengeTimes(null);
						TeamFightCountResp teamFightCountResp = new TeamFightCountResp();
						teamFightCountResp.setTeamChallengeCount(roleInfo.getRoleLoadInfo().getTeamChallengeAllTimesStr());
						// 刷新次数
						SceneService.sendRoleRefreshMsg(teamFightCountResp, roleInfo.getId(), Command.TEAM_FIGHT_COUNTS_RESP);
					}
					
					// 3V3奖励次数
					if(roleInfo.getRoleLoadInfo() != null && roleInfo.getRoleLoadInfo().getTeam3V3Times() != 0){
						roleInfo.getRoleLoadInfo().setTeam3V3Times((byte) 0);
						
						Team3V3FightCountResp teamFightCountResp = new Team3V3FightCountResp();
						teamFightCountResp.setTeam3V3Count((byte) 0);
						// 刷新次数
						SceneService.sendRoleRefreshMsg(teamFightCountResp, roleInfo.getId(), Command.TEAM_3V3_FIGHT_COUNTS_RESP);
					}

					// 长坂坡奖励次数
					if(roleInfo.getRoleLoadInfo() != null && roleInfo.getRoleLoadInfo().getMutualFightCount() != 0){
						roleInfo.getRoleLoadInfo().setMutualFightCount(0);
						
						MutualFightCountResp mutualFightCountResp = new MutualFightCountResp();
						mutualFightCountResp.setFightCount(GameValue.MUTUAL_DAILY_FIGHT_COUNTS);
						// 刷新次数
						SceneService.sendRoleRefreshMsg(mutualFightCountResp, roleInfo.getId(), Command.MUTUAL_FIGHT_COUNTS_RESP);
					}
					
					if(roleInfo.getLoginStatus() != 1)
					{
						continue;
					}
					
					// 宝物活动(攻城略地)
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CAMPAIGN, "");
					
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
					
					// 更新登录天数
					CheckInService.plusOneDayForCheckIn7Days(roleInfo, true);
					
					// 更新30日签到第几日(礼包活动)
					CheckInService.dealCheckInSync(roleInfo, true);
					
					// vip特权礼包0点处理
					VipShopMgtService.dealVipBuyAward(roleInfo, true);
					
					// 0点任务刷新
					QuestService.checkQuest(roleInfo, 0, null, true, false);
					
					OpActivityMgrService.dealOpActivityCheck(roleInfo, true);
					
					// 0：00 推送抢夺次数
					MineService.sendRoleRefreshMineNum(roleInfo);	
				}
				//红点检测
				RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true,
						RedPointMgtService.LISTENING_ZERO_HOUR);
				
				if (index++ > 200) {
					index = 0;
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						logger.error("TaskService Error", e);
					}
				}
			}
		}catch(Exception e){
			logger.error("DayRefresh Error", e);
		}
		logger.info("------ScheduleThread DayRefresh,time="+new Timestamp(System.currentTimeMillis()));
	}
}

/**
 * pve 竞技场 每日定时发放奖励
 * @author zenggang
 */
class ArenaPrizeTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void run() {

		try {
			long now = System.currentTimeMillis();
			ArenaService.sendPlaceRewardMailInfo();
			double costTime = (System.currentTimeMillis() - now) / 1000d;
			if (logger.isInfoEnabled()) {
				logger.info("[SYSTEM] Send Arena Place Reward thread cost time : " + costTime);
			}

		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("[SYSTEM] Send Arena Place Reward failure", e);
			}
		}

		logger.info("------ScheduleThread ArenaPrizeTask,time="+System.currentTimeMillis());
	}
}

/**
 * pvp 跨服竞技场 每日定时发放奖励
 * @author zenggang
 */
class KuafuPrizeTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void run() {

		try {
			long now = System.currentTimeMillis();
			CompetitionService.sendPlaceRewardMailInfo();
			double costTime = (System.currentTimeMillis() - now) / 1000d;
			if (logger.isInfoEnabled()) {
				logger.info("[SYSTEM] Send Kuafu Place Reward thread cost time : " + costTime);
			}

		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("[SYSTEM] Send Kuafu Place Reward failure", e);
			}
		}

		logger.info("------ScheduleThread KuafuPrizeTask,time="+System.currentTimeMillis());
	}
}

/**
 * 大R每日定时发放奖励 + 称号排名刷新
 * @author luwd
 */
class SuperRTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void run() {

		try {
			long now = System.currentTimeMillis();
			WorshipMgtService.sendPlaceReward();
			
			TitleService.getNewRanks();
			double costTime = (System.currentTimeMillis() - now) / 1000d;
			if (logger.isInfoEnabled()) {
				logger.info("[SYSTEM] Send SuperR prize thread cost time : " + costTime);
			}

		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("[SYSTEM] Send SuperR prize failure", e);
			}
		}

		logger.info("------ScheduleThread SuperRTask,time="+System.currentTimeMillis());
	}
}

/**
 * 大R定时增加被膜拜次数
 * @author luwd
 */
class SuperRPlusRandom implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void run() {

		try {
			long now = System.currentTimeMillis();
			WorshipMgtService.plusSuperRRandomValue();
			
			double costTime = (System.currentTimeMillis() - now) / 1000d;
			if (logger.isInfoEnabled()) {
				logger.info("[SYSTEM] Send SuperR plus random value thread cost time : " + costTime);
			}

		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("[SYSTEM] Send SuperR plus random value failure", e);
			}
		}

		logger.info("------ScheduleThread SuperRPlusRandom,time="+System.currentTimeMillis());
	}
}

/**
 * 统计10分钟注册、10分钟在线、10分钟开卡
 */
class CollectionUserInfoTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	public void run() {
		try {
			long now = System.currentTimeMillis();
			//在线
			GameLogService.addStatOnline((int) RoleLoginMap.getSize(), now);
			
			//注册人数
			GameLogService.addStatNewAccount(RoleInfoMap.registerNum, now);
			RoleInfoMap.registerNum = 0;
		} catch (Exception e) {
			logger.error("CollectionUserInfoTask error", e);
			e.printStackTrace();
		}
		
		//开卡
		logger.info("------ScheduleThread CollectionUserInfoTask,time="+new Timestamp(System.currentTimeMillis()));
	}
}



	/**
	 * 清除下线用户缓存
	 * @author hongfm
	 *
	 */
	class clearRoleDataInactive implements Runnable 
	{
		private static final Logger logger = LoggerFactory.getLogger("logs");
		public void run()
		{
			int count = 0;
			
			try
			{
				// 获取矿上阵武将
				Set<Integer> heroIds = MineInfoMap.getMineHeros();
				long nowTime = System.currentTimeMillis();
				Set<Entry<Integer, RoleInfo>> set = RoleInfoMap.getRoleInfoEntrySet();
				for (Entry<Integer, RoleInfo> entry : set)
				{
					RoleInfo roleInfo = entry.getValue();
					
					if (roleInfo.getRoleLoadInfo() != null) {
						count++;
					}
					
					if (roleInfo.getLoginStatus() == 0 && roleInfo.getServerStatus() == 1 && roleInfo.getRoleLoadInfo() != null)
					{
						synchronized (roleInfo) 
						{
							if (nowTime - roleInfo.getLogoutTime().getTime() > 1800 * 1000)
							{
								// 大于1小时,清除缓存
								roleInfo.setRoleLoadInfo(null);
								roleInfo.setServerStatus((byte) 0);
								roleInfo.setWeaponFlag(0);								
								Iterator<HeroInfo> heroIter = roleInfo.getHeroMap().values().iterator();
								while(heroIter.hasNext())
								{
									HeroInfo heroInfo = heroIter.next();
									if(heroInfo == null)
									{
										continue;
									}
									if(heroInfo.getDeployStatus() > HeroInfo.DEPLOY_TYPE_COMM)
									{
										continue;
									}
									if(heroIds.contains(heroInfo.getId())){
										continue;
									}
									if(roleInfo.getHireHeroMap().containsKey(heroInfo.getId())){
										continue;
									}
									heroIter.remove();
								}
								logger.info("####clear role cache,roleId="+roleInfo.getId()+",account="+roleInfo.getAccount());
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				logger.error("clear loginout role hero cache", e);
				e.printStackTrace();
			} finally {
				logger.info("------ScheduleThread clearRoleDataInactive,time="+new Timestamp(System.currentTimeMillis()) + ", remain roleLoadinfos = " + count);
				
				if (count > 20000) {
					logger.warn("RoleLoadInfo count is too more");
				}
			}
		}
	}

	


	/**
	 * 定时发送在线人数到计费服务器
	 * @author hongfm
	 *
	 */
	class onlineRoleNumToCharge implements Runnable 
	{
		private static final Logger logger = LoggerFactory.getLogger("logs");
		@Override
		public void run() 
		{
			try 
			{
				if (ChargeGameService.getChargeService() != null)
				{
					ChargeGameService.getChargeService().sendTotalOnline(RoleInfoMap.getOnlineSize());
				}
			} catch (Exception e) {
				logger.error("sendTotalOnline error", e);
				e.printStackTrace();
			}
			
			logger.info("------ScheduleThread onlineRoleNumToCharge,time="+new Timestamp(System.currentTimeMillis()));
		}
	}
	/**
	 * 发送世界BOSS奖励
	 * 
	 */
	class SendWorldBossReward implements Runnable {
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {
			try {
				WorldBossMgtService.sendWorldBossPrize();
			} catch (Exception e) {
				logger.error("SendWorldBossReward error", e);
				e.printStackTrace();
			}
			
			logger.info("------ScheduleThread SendWorldBossReward,time=" + new Timestamp(System.currentTimeMillis()));
		}
	}
	/**
	 * 每日任务体力任务的刷新推送
	 * 
	 * @author nijp
	 *
	 */
	class SpDailyQuestStart implements Runnable {
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		private int questProtoNo;
		
		public SpDailyQuestStart() {
			
		}
		
		public SpDailyQuestStart(int questProtoNo) {
			this.questProtoNo = questProtoNo;
		}
		
		@Override
		public void run() {
			try {
				QuestService.dealSpQuestChg(questProtoNo, true);
			} catch (Exception e) {
				logger.error("SpDailyQuestStart error", e);
				e.printStackTrace();
			}
			
			logger.info("------ScheduleThread SpDailyQuestStart,time=" + new Timestamp(System.currentTimeMillis()));
		}
	}
	
	/**
	 * 每日任务体力任务的结束推送
	 * 
	 * @author nijp
	 *
	 */
	class SpDailyQuestEnd implements Runnable {
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		private int questProtoNo;
		
		public SpDailyQuestEnd() {
			
		}
		
		public SpDailyQuestEnd(int questProtoNo) {
			this.questProtoNo = questProtoNo;
		}
		
		@Override
		public void run() {
			try {
				QuestService.dealSpQuestChg(questProtoNo, false);
			} catch (Exception e) {
				logger.error("SpDailyQuestEnd error", e);
				e.printStackTrace();
			}
			
			logger.info("------ScheduleThread SpDailyQuestEnd,time=" + new Timestamp(System.currentTimeMillis()));
		}
	}
	
	/**
	 * 每5分钟更新角色的登陆日志
	 * @author wanglinhui
	 *
	 */
	/*class updateRoleLoginLog implements Runnable{

		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {
			try
			{
				List<String> loginIdList = new ArrayList<String>();
				Set<Integer> set = RoleLoginMap.getSet();
				for(Integer roleId:set)
				{
					RoleInfo roleInfo =  RoleLoginMap.getRoleInfo(roleId);
					if(roleInfo!=null)
					{
						loginIdList.add(roleInfo.getLoginLogId());
					}
				}
				Timestamp now = new Timestamp(System.currentTimeMillis());
				
				GameLogDAO.getInstance().updateRoleLog(loginIdList, now);
				
				if(logger.isInfoEnabled()){
					logger.info("updateRoleLoginLog successful!");
				}
			}catch(Exception e){
				if(logger.isInfoEnabled()){
					logger.info("updateRoleLoginLog failer!"+e);
				}
			}
			
			
		}
		
	}*/
	
	/**
	 * 每30分钟所有日志入库
	 * @author wanglinhui
	 *
	 */
	class updateAllLeftLog implements Runnable{
		
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {
			try{
				GameLogDAO.getInstance().shutDown();
				
				if(logger.isInfoEnabled()){
					logger.info("updateAllLeftLog successful!");
				}
			}catch(Exception e){
				logger.info("updateAllLeftLog error!"+e.getMessage());
			}
			
		}
		
	}
	
	/**
	 * 查询全服礼物，用于EAI发道具
	 * @author wanglinhui
	 *
	 */
	class QueryChargeAllGiftTask implements Runnable{

		private static final Logger logger = LoggerFactory.getLogger("logs");
		@Override
		public void run() {
			
			try {
				TaskService.queryChargeAllGift();
			} catch (Exception e) {
				if(logger.isInfoEnabled()){
					logger.info("queryChargeAllGift exception: ",e);
				}
			}
		}
		
		
	}
	
	/**
	 * 每10分钟检测服务器各线程池情况
	 * @author hongfm
	 *
	 */
	class ServerCheck implements Runnable{

		private static final Logger logger = LoggerFactory.getLogger("logs");
		@Override
		public void run() {
			// 场景移动,世界地图移动
			logger.warn("SceneRefreThread: poolSize = " + SceneRefreThread.getInstance().getThreadPool().getPoolSize()
					+":maxPoolSize="+SceneRefreThread.getInstance().getThreadPool().getMaximumPoolSize()
					+":queueSize="+SceneRefreThread.getInstance().getThreadPool().getQueue().size());
			
			// 刷新推送
			logger.warn("ScreanNotifyThread: poolSize = " + ScreanNotifyThread.getInstance().getThreadPool().getPoolSize()
					+":maxPoolSize="+ScreanNotifyThread.getInstance().getThreadPool().getMaximumPoolSize()
					+":queueSize="+ScreanNotifyThread.getInstance().getThreadPool().getQueue().size());
			
			// 在线人数
			logger.warn("onlineNum="+RoleInfoMap.getOnlineSize());
			
			logger.warn("PushServerSendMsgThread queue size="+PushServerSendMsgThread.getQueue().size());
			
			//日志数量
			logger.warn("TempExecuteLogsThread queue size="+TempExecuteLogsThread.getQueue().size());
			
			//游戏线程池接收发送情况
			ThreadPoolExecutor gamePool= ThreadPoolManager.getInstance().Pool("webgame");
			if(gamePool != null)
			{
				logger.warn("gamePool: poolsize="+gamePool.getPoolSize()
						+",maxPoolSize="+gamePool.getMaximumPoolSize()
						+",queuePoolSize="+gamePool.getQueue().size()
						+",lastPoolSize="+gamePool.getLargestPoolSize());
			}
			
			ThreadPoolExecutor receivePool= ThreadPoolManager.getInstance().Pool("receivePool");
			if(receivePool != null)
			{
				logger.warn("receivePool: poolsize="+receivePool.getPoolSize()
						+",maxPoolSize="+receivePool.getMaximumPoolSize()
						+",queuePoolSize="+receivePool.getQueue().size()
						+",lastPoolSize="+receivePool.getLargestPoolSize());
			}
			
			ThreadPoolExecutor sendPool= ThreadPoolManager.getInstance().Pool("sendPool");
			if(sendPool != null)
			{
				logger.warn("sendPool: poolsize="+sendPool.getPoolSize()
						+",maxPoolSize="+sendPool.getMaximumPoolSize()
						+",queuePoolSize="+sendPool.getQueue().size()
						+",lastPoolSize="+sendPool.getLargestPoolSize());
			}
			
			ThreadPoolExecutor snailcharge= ThreadPoolManager.getInstance().Pool("snailcharge");
			if(snailcharge != null)
			{
				logger.warn("snailcharge: poolsize="+snailcharge.getPoolSize()
						+",maxPoolSize="+snailcharge.getMaximumPoolSize()
						+",queuePoolSize="+snailcharge.getQueue().size()
						+",lastPoolSize="+snailcharge.getLargestPoolSize());
			}
			
			//缓存不足1G时手动清除下线超时玩家
			long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
			if(freeMemory < 1204)
			{
				new clearRoleDataInactive().run();
				logger.warn("the game memory not enough,clear the logout role,freeMemory="+freeMemory);
			}
			
			logger.warn("##############chest cost coin ="+RoleInfoMap.chestCoinCost);
		}
		
		
	}
	
	/**
	 * 排行榜计算
	 * @author zhangyq
	 *
	 */
	//TODO
	class Rank implements Runnable{
		
		private static final Logger logger = LoggerFactory.getLogger("logs");
		@Override
		public void run() {
			try {
				RankService.rank();
			} catch(Exception e) {
				if(logger.isInfoEnabled()){
					logger.info("rank exception: "+e.getMessage());
				}
			}
		}
		
	}
	
	/**
	 * 世界BOSS每日刷新
	 * @author zhangyq
	 */
	class AddWorldBoss implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");

		@Override
		public void run() {

			try {
				long now = System.currentTimeMillis();
				WorldBossMgtService.sendWorldBoss();
				double costTime = (System.currentTimeMillis() - now) / 1000d;
				if (logger.isInfoEnabled()) {
					logger.info("[SYSTEM] Send Add World BOSS thread cost time : " + costTime);
				}

			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] Send Add World BOSS failure", e);
				}
			}

			logger.info("------ScheduleThread Add World BOSS,time="+System.currentTimeMillis());
		}
	}
	
	/**
	 * 世界BOSS每日结束
	 * @author zhangyq
	 */
	class DELWorldBoss implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");

		@Override
		public void run() {

			try {
				long now = System.currentTimeMillis();
				WorldBossMgtService.clearWorldBoss();
				double costTime = (System.currentTimeMillis() - now) / 1000d;
				if (logger.isInfoEnabled()) {
					logger.info("[SYSTEM] Del World BOSS thread cost time : " + costTime);
				}

			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] Del World BOSS failure", e);
				}
			}

			logger.info("------ScheduleThread Del WORLD BOSS,time="+System.currentTimeMillis());
		}
	}
	
	/**
	 * 跨服竞技场双倍时间开始广播
	 * @author hongfm
	 */
	class KfArenaFightStartBrocast implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");

		@Override
		public void run() {

			try {
				String content = Resource.getMessage("game", "KFJJC_START") + "%" + GameValue.KF_JJC_DOUBLE;
				GmccMgtService.sendChatMessage(content);

			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] KfArenaFightStartBrocast failure", e);
				}
			}

			logger.info("------ScheduleThread KfArenaFightStartBrocast,time="+System.currentTimeMillis());
		}
	}
	
	/**
	 * 多人组队开始广播
	 * @author hongfm
	 */
	class MutualFightStartBrocast implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");

		@Override
		public void run() {

			try {
				String content = Resource.getMessage("game", "MUTUAL_FIGHT_START_CONTENT")  + "%" + GameValue.CBP_DOUBLE;
				GmccMgtService.sendChatMessage(content);

			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] MutualFightStartBrocast failure", e);
				}
			}

			logger.info("------ScheduleThread MutualFightStartBrocast,time="+System.currentTimeMillis());
		}
	}

	/**
	 * 3V3开始广播
	 * @author hongfm
	 */
	class Team3V3FightStartBrocast implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");

		@Override
		public void run() {

			try {
				String content = Resource.getMessage("game", "TEAM_3V3_FIGHT_START_CONTENT")  + "%" + GameValue.TEAM_3V3;
				GmccMgtService.sendChatMessage(content);

			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] Team3V3FightStartBrocast failure", e);
				}
			}

			logger.info("------ScheduleThread Team3V3FightStartBrocast,time="+System.currentTimeMillis());
		}
	}
	
	/**
	 * 镖车双倍活动开始广播
	 * @author xiasd
	 */
	class BiaocheDoubleStartBrocast implements Runnable {
		
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {
			
			try {
				String content = Resource.getMessage("game", "BIAO_CHE_DOUBLE_START")  + "%" + GameValue.YB_DOUBLE;
				GmccMgtService.sendChatMessage(content);
				
			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] MutualFightStartBrocast failure", e);
				}
			}
			
			logger.info("------ScheduleThread BiaocheDoubleStartBrocast,time="+System.currentTimeMillis());
		}
	}
	
	/**
	 * 镖车双倍活动结束广播
	 * @author xiasd
	 */
	class BiaocheDoubleEndBrocast implements Runnable {
		
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {
			
			try {
				String content = Resource.getMessage("game", "BIAO_CHE_DOUBLE_END");
				GmccMgtService.sendChatMessage(content);
				
			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("[SYSTEM] BiaocheDoubleEndBrocast failure", e);
				}
			}
			
			logger.info("------ScheduleThread BiaocheDoubleEndBrocast,time="+System.currentTimeMillis());
		}
	}
	
	/**
	 * 每30分钟更新在线玩家的战斗力
	 * @author zenggang
	 */
	class updateRoleFightValue implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");

		@Override
		public void run() {
			
			try {
				Map<Integer, Integer> fightValueMap = new HashMap<Integer, Integer>();
				Set<Integer> set = RoleLoginMap.getSet();
				for (Integer roleId : set) {
					RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
					if (roleInfo != null) {
						fightValueMap.put(roleInfo.getId(), roleInfo.getFightValue());
					}
				}
				RoleDAO.getInstance().updateRoleFightValue(fightValueMap);
			} catch (Exception e) {
				logger.error("updateRoleFightValue error", e);
				e.printStackTrace();
			}

			if (logger.isInfoEnabled()) {
				logger.info("updateRoleFightValue successful!");
			}
		}
	}
	
	/**
	 * 每5分钟检测大地图矿变化
	 * @author zenggang
	 */
	class checkMine implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {			
			try {
				// 检测需要添加的矿
				MineService.checkForAddMine(true);
			} catch(Exception e) {
				if(logger.isErrorEnabled()){
					logger.error("checkMine exception: "+e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 清理过期充值订单
	 * 
	 * @author nijp
	 *
	 */
	class ClearChargeOrderInfo implements Runnable {
		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		public static final int ORDER_KEEP_DAY = 30;// 订单保存天数
		
		@Override
		public void run() {
			try {
				long now = System.currentTimeMillis();
				
				List<RoleChargeInfo> clearInfos = new ArrayList<RoleChargeInfo>();
				for (RoleChargeInfo info : RoleChargeMap.getMap().values()) {
					if (now - info.getChargeTime().getTime() >= ORDER_KEEP_DAY * DateUtil.ONE_DAY_MILLIS) {
						clearInfos.add(info);
					}
				}
				
				if (clearInfos.size() > 0) {
					if (RoleChargeDAO.getInstance().clearRoleChargeInfo(clearInfos)) {
						for (RoleChargeInfo info : clearInfos) {
							RoleChargeMap.removeRoleChargeInfo(info.getOrderStr());
						}
					}
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("ClearChargeOrderInfo exception: " + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 每5分钟检测GMCC连接
	 * @author zhangyq
	 */
	class checkGMCC implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger("logs");
		
		@Override
		public void run() {			
			try {
				IoSession session =	GmccSendMessage.getInstance().getSession();
				if(session != null && session.isConnected())
				{
					int i = 0;
					//连接正常
					//如果在线玩家没有发送 发送
					Set<Integer> set = RoleLoginMap.getSet();
					for (Integer roleId : set) {
						RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
						if (roleInfo != null) {
							if(!roleInfo.isSendGmccFlag())
							{
								GmccGameService.getService().sendRoleLogin((int) roleInfo.getId(), roleInfo.getAccount(),
										roleInfo.getRoleName());
								roleInfo.setSendGmccFlag(true);
								i++;
							}
						}
						if(i>0 && (i%1000 == 0))
						{
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				else
				{
					//int i = 0;
					//连接不正常
					//改变所有在线玩家的发送状态
					Set<Integer> set = RoleLoginMap.getSet();
					for (Integer roleId : set) {
						RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
						if (roleInfo != null) {
							roleInfo.setSendGmccFlag(false);
							//i++;
						}
					}
				}
			} catch(Exception e) {
				if(logger.isErrorEnabled()){
					logger.error("checkGMCC exception: "+e.getMessage());
				}
			}
		}
	}
