package com.snail.webgame.game.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.LogMessage;
import com.snail.webgame.game.common.RoleOutLog;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.log.ConfigLog;
import com.snail.webgame.game.info.log.ConfigType;
import com.snail.webgame.game.info.log.ProgramLog;
import com.snail.webgame.game.info.log.StatLog;
import com.snail.webgame.game.thread.ExecuteLogsThread;
import com.snail.webgame.game.thread.TempExecuteLogsThread;

/**
 * @author hongfm
 *
 */
public class GameLogDAO extends SqlMapDaoSupport {
	
	/*private Calendar cal = Calendar.getInstance();
	private String date = format.format(System.currentTimeMillis());
	
	private int week = cal.get(Calendar.DAY_OF_WEEK)-1;*/
	
	
	private static final int newTabelWeek = 1;
	private String[] oldTables = new String[45];
	private String[] newTables = new String[45];
	
	/**
	 *  0 -palyAction
	 *  1 -money
	 *  2 -item
	 *  3 -roleUp
	 *  4 -heroUp
	 *  5 -weapUp
	 *  6 -equipUp
	 *  7 -storeBug
	 *  8 -biaoche
	 *  9 -soliderUp
	 *  10-heroSkillUp
	 *  11-goldBuy
	 *  12-equipResolve
	 *  13-snatch
	 *  14-mineGet
	 *  15-roleClub
	 *  16-roleRelation
	 *  17-active
	 *  18-roleCharge
	 *  19-task
	 *  20-instance
	 *  21-equipInfo
	 *  22-mail
	 *  23-challenge
	 *  24-pve
	 *  25-arena
	 *  26-defend
	 *  27-mineFigh
	 *  28-camp
	 *  29-competitive
	 *  30-pvp3
	 *  31-mapFight
	 *  32-punish
	 *  33-roleName
	 *  34-worldBoss
	 *  35-toolOperate
	 *  36-roleLogin
	 *  37-updaRoleLogin
	 *  38-newAccount
	 *  39-newCard
	 *  40-online
	 */
	private static String[] strSql=new String[45];
	
	/**
	 * 持久化失败写入文件句柄
	 */
	private PrintWriter pw = null;
	private String basePath = null;
	private long logStartTime;
	public static final long TWO_DAY = 48 * 3600 * 1000;
	private static boolean isShutDown = false;
	
	//create table TASK_LOG_20150921 like TASK_LOG;
	
	public static int logInsertNum = 10; // 日志批量操作數量
	private static final Logger logger = LoggerFactory.getLogger("logs");
	private List<RoleOutLog> roleOutLogList = new ArrayList<RoleOutLog>();
	private List<ProgramLog> programLogList = new ArrayList<ProgramLog>();
	
	//玩法日志
	public static StringBuffer palyActionLogBuffer = new StringBuffer();
	public static int actionLogCount = 0;
	private static String playActionSqlStr = "INSERT INTO GAME_PLAYER_ACTION_LOG " +
			"(S_ACCOUNT, S_ROLE_NAME, D_CREATE, S_ACT_ID, N_TYPE, S_ACT_VALUE) VALUES";
	
	//资源日志
	public static StringBuffer moneyLogBuffer = new StringBuffer();
	public static int moneyLogCount = 0;
	private static String moneyLogSqlStr = "INSERT INTO MONEY_LOG " +
			"(S_SEND_ACCOUNT,S_SEND_ROLE,D_CREATE,N_MONEY_TYPE,S_EVENT_ID,N_EVENT_TYPE,S_ITEM_ID,N_COUNT,N_MONEY," +
			"S_RECV_ACCOUNT,S_RECV_ROLE,S_SCENE_ID,S_GUILD_ID,S_STATE,N_ROLE_LEVEL,N_TOTAL_LOGTIME,N_BEFORE," +
			"N_AFTER,N_TRADE_ID,S_TRADE_FLAG,S_COMMENT) VALUES";
	
	//物品日志
	public static StringBuffer itemLogBuffer = new StringBuffer();
	public static int itemLogCount = 0;
	private static String itemLogSqlStr = "INSERT INTO ITEM_LOG " +
			"(S_SEND_ACCOUNT,S_SEND_ROLE,D_CREATE,S_EVENT_ID,N_EVENT_TYPE, S_ITEM_ID," +
			"N_COUNT,S_RECV_ACCOUNT,S_RECV_ROLE,S_SCENE_ID,S_GUILD_ID,S_STATE," +
			"N_BEFORE,N_AFTER,N_TRADE_ID,S_TRADE_FLAG,S_COMMENT) VALUES";
	
	//主武将升级
	public static StringBuffer roleUpLogBuffer = new StringBuffer();
	public static int roleUpLogCount = 0;
	private static String roleUpSqlStr = "INSERT INTO ROLE_UPGRADE_LOG 	" +
			"(S_SERIAL,D_TIME,S_ACCOUNT,S_ROLE,N_LOGTYPE,N_LEVEL_BEFORE,N_LEVEL_AFTER,S_SKILL_ID) VALUES";
	
	//武将升级
	public static StringBuffer heroUpLogBuffer = new StringBuffer();
	public static int heroUpLogCount = 0;
	private static String heroUpSqlStr = "INSERT INTO GAME_HERO_UP_LOG " +
	"(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_NO,	D_TIME,S_EVENT_ID,N_UP_TYPE,N_BEFORE,N_AFTER) VALUES";
	
	//神兵升级
	public static StringBuffer weapUpLogBuffer = new StringBuffer();
	public static int weapUpLogCount = 0;
	private static String weapUpSqlStr = "INSERT INTO GAME_WEAPON_UP_LOG " +
			"(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME,N_WEAPON_ID,N_WEAPON_NO,D_TIME,S_EVENT,N_UP_TYPE,N_BEFORE,N_AFTER) VALUES";
	
	//装备强化
	public static StringBuffer equipUpLogBuffer = new StringBuffer();
	public static int equipUpLogCount = 0;
	private static String equipUpSqlStr = "INSERT INTO GAME_EQUIP_UP_LOG " +
			"(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_ID,N_EQUIP_ID,N_EQUIP_NO,D_TIME,S_EVENT,N_BEFORE,N_AFTER) VALUES";
	
	//商店日志
	public static StringBuffer storeBugLogBuffer = new StringBuffer();
	public static int storeBugLogCount = 0;
	private static String storeBuySqlStr = "INSERT INTO GAME_STORE_BUY_ITEM_LOG " +
			"(S_ACCOUNT,S_ROLE_NAME, N_ROLE_ID,N_SOTRE_TYPE,N_ITEM_NO,N_ITEM_NUM,S_SOURCE_TYPE,N_USED_SOURCE,D_CREATE) VALUES";
	
	//镖车日志
	public static StringBuffer biaocheLogBuffer = new StringBuffer();
	public static int biaocheLogCount = 0;
	private static String biaocheSqlStr = "INSERT INTO GAME_BIAO_CHE_LOG " +
			"(S_ACCOUNT,S_ROLE_NAME, N_ROLE_ID,N_ACTION,N_NUM,N_SILVER_CHANGE,D_CREATE,S_CONTENT,N_RESULT,N_LOST_SILVER,N_USED_COIN,N_BIAO_CHE_TYPE) VALUES";
	
	//兵法升级
	public static StringBuffer soliderUpLogBuffer = new StringBuffer();
	public static int soliderUpLogCount = 0;
	private static String soliderUpSqlStr = "INSERT INTO GAME_SOLDIER_UP_LOG " +
			"(S_ACCOUNT,S_ROLE_NAME, N_ROLE_ID,N_SOLDIER_TYPE,N_IS_UP,N_BEFORE_LV,N_AFTER_LV,D_CREATE,S_CONTENT,N_USE_MONEY,N_USE_ITEM_NO,N_USE_ITEM_NUM) VALUES";
	
	//技能升级
	public static StringBuffer heroSkillUpLogBuffer = new StringBuffer();
	public static int heroSkillUpLogCount = 0;
	private static String skillSqlStr = "INSERT INTO GAME_HERO_SKILL_UP_LOG " +
			"(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_NO,	N_SKILL_NO, D_TIME, S_EVENT_ID, N_BEFORE, N_AFTER) VALUES";
	
	//体力，精力购买等
	public static StringBuffer goldBuyLogBuffer = new StringBuffer();
	public static int goldBuyLogCount = 0;
	private static String goldBuySqlStr = "INSERT INTO GAME_GOLD_BUY_LOG(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME," +
			" D_TIME, S_EVENT_ID,N_MONEY_TYPE,N_BEFORE, N_AFTER,N_COST,N_BEFORE_NUM, N_AFTER_NUM) VALUES";
	
	//装备熔炼
	public static StringBuffer equipResolveLogBuffer = new StringBuffer();
	public static int equipResolveLogCount = 0;
	private static String equipResolveSqlStr = "INSERT INTO GAME_EQUIP_RESOLVE_LOG(N_ROLE_ID,S_ACCOUNT, " +
			"S_ROLE_NAME, S_RESOLVE_EQUIPS, S_ADD_ITEMS, N_TYPE, D_TIME) VALUES";
	
	//你争我夺
	public static StringBuffer snatchLogBuffer = new StringBuffer();
	public static int snatchLogCount = 0;
	private static String snatchSqlStr = "INSERT INTO GAME_SNATCH_LOG(S_ACCOUNT, S_ROLE_NAME, " +
			"N_ROLE_ID,S_DEFEND_NAME,N_DEFEND_ID,N_STONE_NO,N_LOOT_SUCCESS,N_LOOT_TIMES,S_GET_ITEM,N_USE_ENGER,S_CONTENT,D_TIME) VALUES";
	
	//矿采集
	public static StringBuffer mineGetLogBuffer = new StringBuffer();
	public static int mineGetLogCount = 0;
	private static String mineGetSqlStr = "INSERT INTO MINE_GET_LOG (S_ACCOUNT,S_ROLE_NAME,S_EVENT_ID,S_ITEM_NO,N_ITEM_NUM,N_STATE,D_TIME) VALUES";
	
	//公会日志
	public static StringBuffer roleClubLogBuffer = new StringBuffer();
	public static int roleClubLogCount = 0;
	private static String roleClubSqlStr = "INSERT INTO GAME_ROLE_CLUB_LOG(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_CLUB_ID, N_TYPE, D_TIME) VALUES";
	
	//好友关系
	public static StringBuffer roleRelationLogBuffer = new StringBuffer();
	public static int roleRelationLogCount = 0;
	private static String roleRelationSqlStr = "INSERT INTO GAME_ROLE_RELATION_LOG(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_REL_ROLE_ID, N_RELATION,D_TIME) VALUES";
	
	// 练兵场日志
	public static StringBuffer activeLogBuffer = new StringBuffer();
	public static int activeLogCount = 0;
	private static String activeSqlStr = "INSERT INTO ACTIVITY_LOG (S_ACCOUNT,S_NAME,N_HERO_NO,N_ROLE_ID,S_ACTIVITY,D_STARTTIME,D_ENDTIME,S_POS,S_DROP) VALUES ";
	
	//充值日志
	public static StringBuffer roleChargeLogBuffer = new StringBuffer();
	public static int roleChargeLogCount = 0;
	private static String chargeSqlStr = "INSERT INTO ROLE_CHARGE_LOG (S_ROLE_ACC,S_ROLE_NAME,S_ORDER_ID,N_CHARGE_TYPE,N_CHARGE_EVENT,D_CREATE_TIME) VALUES";
	
	// 任务日志
	public static StringBuffer taskLogBuffer = new StringBuffer();
	public static int taskLogCount = 0;
	private static String taskSqlStr = "INSERT INTO TASK_LOG (S_TASK_ID,S_ACCOUNT,S_ROLE_NAME,N_ACT_TYPE,D_CREATE) VALUES";
	
	public static StringBuffer instanceLogBuffer = new StringBuffer();
	public static int instanceLogCount = 0;
	private static String instanceSqlStr = "INSERT INTO INSTANCE_LOG(S_SERIAL,N_ROLE_ID,S_INSTANCEID,S_INSTANCETYPEID,D_STARTTIME," +
			"D_ENDTIME,N_DURATION,N_TROOP_COUNT1,N_TROOP_COUNT2) VALUES ";
	
	public static StringBuffer punishLogBuffer = new StringBuffer();
	public static int punishLogCount = 0;
	private static String punishSqlStr = "INSERT INTO PUNISH_LOG(S_SERIAL,D_TIME,S_OPERATOR,S_ROLE,S_ACCOUNT,N_MINUTES,N_PUNISH_TYPE) VALUES";
	
	// 英雄装备变动日志
	public static StringBuffer equipInfoLogBuffer = new StringBuffer();
	public static int equipInfoLogCount = 0;
	private static String equipSqlStr = "INSERT INTO GAME_EQUIP_INFO_LOG (" +
			"N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_ID,N_ITEM_ID,N_ITEM_NO,N_ITEM_LEVEL, N_ITEM_STAR,N_EXP,N_COLOUR," +			
			"D_TIME,S_EVENT_ID,N_EVENT_TYPE,S_STATE,S_COMMENT) VALUES ";
	
	public static StringBuffer toolOperateLogBuffer = new StringBuffer();
	public static int toolOperateLogCount = 0;
	private static String toolOperateSqlStr = "INSERT INTO GAME_TOOL_OPERATE_LOG(" +
			"S_MESSAGE_TYPE, S_GUID, S_TARGET_ROLE_NAME, S_TARGET_ROLE_ACCOUNT,S_XML_NAME_STR," +
			" S_MESSAGE_CONTENT, S_OPERATOR_ACCOUNT, D_OPERATE_TIME, N_IS_SUCC, S_RESERVE) VALUES";
	
	public static StringBuffer mailLogBuffer = new StringBuffer();
	public static int mailLogCount = 0;
	private static String mailSqlStr = "INSERT INTO GAME_MAIL_LOG(N_ID,S_SENDNAME,S_RECEIVENAME,D_DEAL_TIME,N_TYPE,S_ATTACHMENT,S_REC_ACC,S_MAIL_TITLE,S_MAIL_CONTENT,S_RESERVE) VALUES";
	
	// 副本
	public static StringBuffer challengeLogBuffer = new StringBuffer();
	public static int challengeLogCount = 0;
	private static String challengeSqlStr = "INSERT INTO CHALLENGE_LOG(N_ROLE_ID,S_ROLE_NAME,S_ACCOUNT, N_ACT_TYPE, S_CHALLENGE_NO, D_TIME, N_STAR,D_START_TIME)VALUES";
	
	// 副本异常日志
	public static StringBuffer challengeUnusualLogBuffer = new StringBuffer();
	public static int challengeUnusualLogCount = 0;
	private static String challengeUnusualSqlStr = "INSERT INTO CHALLENGE_UNUSUAL_LOG(N_ROLE_ID,S_ROLE_NAME,S_ACCOUNT, S_CHALLENGE_NO, D_START_TIME,D_TIME, N_STAR,S_COMMENT)VALUES";
	
	
	// 狭路相逢
	public static StringBuffer pveLogBuffer = new StringBuffer();
	public static int pveLogCount = 0;
	private static String pveSqlStr = "INSERT INTO GAME_PVE_LOG" +
			"(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_DEFENSE_ID,S_DEFENSE_NAME,S_REWARD,D_START_TIME,D_ENDTIME,N_BATTLE_RESULT) VALUES";
	
	// 竞技场
	public static StringBuffer arenaLogBuffer = new StringBuffer();
	public static int arenaLogCount = 0;
	private static String arenaSqlStr = "INSERT INTO GAME_ROLE_ARENA_LOG(" +
			"S_ACCOUNT, S_ROLE_NAME, N_ROLE_ID,S_DEFEND_NAME,N_DEFEND_ID,N_BATTLE_RESULT," +
			"N_BEFORE_PLACE,N_AFTER_PLACE,S_GET_ITEM,N_USE_ENGER,S_CONTENT,D_TIME," +
			"N_ROLE_MAIN_HERO,S_ROLE_HEROS,N_DEFEND_MAIN_HERO,S_DEFEND_HEROS) VALUES ";
	
	// 兵来将挡战斗日志
	public static StringBuffer defendLogBuffer = new StringBuffer();
	public static int defendLogCount = 0;
	private static String defendSqlStr = "INSERT INTO DEFEND_FIGHT_LOG " +
			"(S_ACCOUNT,S_NAME,N_ROLE_ID,N_HERO_NO,N_FIGHT_LV,D_STARTTIME,D_ENDTIME,S_POS,S_DROP,N_FIGHT_RESULT) VALUES ";
	
	// 大地图战斗日志
	public static StringBuffer mapFightLogBuffer = new StringBuffer();
	public static int mapFightLogCount = 0;
	private static String mapFightSqlStr = "INSERT INTO MAP_FIGHT_LOG " +
			"(S_ACCOUNT,S_NAME,N_ROLE_ID,N_HERO_NO,N_NPC_ID,N_COME_ROLEID,D_STARTTIME,D_ENDTIME,S_POS,S_DROP,N_FIGHT_RESULT) VALUES";
	
	// 采矿战斗
	public static StringBuffer mineFightLogBuffer = new StringBuffer();
	public static int mineFightLogCount = 0;
	private static String mineFightSqlStr = "INSERT INTO MINE_FIGHT_LOG " +
			"(N_POSITION,N_MINE_NO,N_ROLE_ID,S_ROLE_NAME,S_ACCOUNT,N_ROLE_HERO_NO,N_ROLE_LEVEL," +
			"N_ATTACK_ROLE_ID,S_ATTACK_ROLE_NAME,S_ATTACK_ACCOUNT,N_ATTACK_ROLE_HERO_NO,N_ATTACK_ROLE_LEVEL,N_FIGHT_RESULT,D_TIME) " +
			"VALUES ";
	
	// 攻城略地
	public static StringBuffer campLogBuffer = new StringBuffer();
	public static int campLogCount = 0;
	private static String campSqlStr = "INSERT INTO GAME_CAMP_LOG" +
			"(S_ACCOUNT, S_ROLE_NAME, N_ROLE_ID,N_CAMP_NO,N_BATTLE_RESULT,S_PRIZE,D_START_TIME,D_END_TIME,S_COMMENT) VALUES";
	
	// 跨服竞技场
	public static StringBuffer competitiveLogBuffer = new StringBuffer();
	public static int competitiveLogCount = 0;
	private static String competitiveLogSqlStr = "INSERT INTO COMPETITIVE_LOG" +
			"(S_SEND_ACCOUNT,S_SEND_ROLE,N_ROLE_ID,N_HERO_NO,D_CREATE,N_BEFORE_STAGE_VALUE,N_AFTER_STAGE_VALUE," +
			"N_BEFORE_STAGE,N_AFTER_STAGE,N_BEFORE_RANK,N_AFTER_RANK,S_GET_ITEM,N_USE_ENERGY,S_MATCH_ROLE," +
			"N_TARGET_HERO_NO,S_COMMENT,N_BATTLE_RESULT,S_HERO_IDS) VALUES ";
	
	// 组队PVP
	public static StringBuffer pvp3LogBuffer = new StringBuffer();
	public static int pvp3LogCount = 0;
	private static String pvp3LogSqlStr = "INSERT INTO GAME_PVP_3_LOG" +
			"(N_ROLE_ID,S_ACCOUNT,S_ROLE_NAME,N_RESULT,N_POINT,N_BEFORE_POINT,N_AFTER_POINT,D_START_TIME,D_END_TIME) VALUES";
	
	//用户改名日志
	public static StringBuffer roleNameLogBuffer = new StringBuffer();
	public static int roleNameLogCount = 0;
	private static String roleNameSqlStr = "INSERT INTO GAME_ROLE_NAME_LOG(N_ROLE_ID,S_U_ID,S_ACCOUNT, S_BEFORE,S_AFTER, D_CREATE) VALUES";
	
	//世界BOSS日志
	public static StringBuffer worldBossLogBuffer = new StringBuffer();
	public static int worldBossLogCount = 0;
	private static String worldBossSqlStr = "INSERT INTO WORLD_BOSS_FIGHT_LOG(S_ACCOUNT, S_ROLENAME, D_BEGINTIME,D_ENGTIME,N_HURT) VALUES";
	
	//角色上线日志
	public static StringBuffer roleLogBuffer = new StringBuffer();
	public static int roleLogCount = 0;
	private static String roleSqlStr = "INSERT INTO ROLE_LOG" +
			"(S_SERIAL,S_ACCOUNT,N_HERO_NO,S_ROLE,S_IP,D_LOGINTIME,D_LOGOUTTIME, S_MAC,S_OS_TYPE, S_COMMENT) VALUES";
	
	//加速器检测
	public static StringBuffer checkTimeBuffer = new StringBuffer();
	public static int checkTimeLogCount = 0;
	private static String checkTimeSqlStr = "INSERT INTO CHECK_TIME_LOG(S_ACCOUNT, S_ROLE_NAME, D_CUR_TIME,D_PRE_TIME,N_NUM) VALUES";
	
	//角色下线
	private static String updaRoleSqlStr = "UPDATE ROLE_LOG SET D_LOGOUTTIME = ?  WHERE S_SERIAL = ?";
	
	private static String newAccountSqlStr= "INSERT INTO STAT_NEWACCOUNT (N_AMOUNT, D_TIME) VALUES ";
	
	private static String newCardSqlStr = "INSERT INTO STAT_NEWCARD (N_AMOUNT, D_TIME) VALUES ";
	
	private static String onlineSqlStr = "INSERT INTO STAT_ONLINE (N_AMOUNT, D_TIME) VALUES ";

	private static class InternalClass {
		public final static GameLogDAO instance = new GameLogDAO();
	}

	public static GameLogDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 删除老的类型表
	 * @param list
	 */
	public void delOldGameConfigType(){
		try {
			getSqlMapClient(DbConstants.GAME_LOG_DB).delete("deleteGameConfigType");
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("delOldGameConfigType error!!",e);
			}
		}
	}
	
	/**
	 * 删除老的枚举表
	 * @param list
	 */
	public void delOldGameConfig(){
		try {
			getSqlMapClient(DbConstants.GAME_LOG_DB).delete("deleteGameConfig");
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("delOldGameConfig error!!",e);
			}
		}
	}
	
	/**
	 * 保存枚举类型表
	 * @param list
	 */
	public void insertConfigTypeLog(List<ConfigType> list){
		SqlSession session = null;
		try {
			session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_LOG_DB).openSession(ExecutorType.BATCH, false);
			if(session!=null){
				Map<String, Object> to = new HashMap<String, Object>();
				for (ConfigType configType : list) {
					to.put("type", configType.getType());
					to.put("name", configType.getName());
					session.insert("insertConfigTypeLog", to);
				}
				session.commit();
				session.close();
			}
		} catch (Exception e) {
			if(session!=null){
				session.rollback();
				session.close();
			}
			if (logger.isErrorEnabled()) {
				logger.error("insertConfigTypeLog error!!", e);
			}
		}
	}
	
	/**
	 * 保存枚举表
	 * @param configs
	 */
	public void insertConfigLog(List<ConfigLog> configs) {
		SqlMapClient client = null;

		try {
			client = getSqlMapClient(DbConstants.GAME_LOG_DB, ExecutorType.BATCH, false);
			if(client!= null){
				for (ConfigLog configLog : configs) {
					client.insert("insertConfigLog", configLog);
				}
				client.commit();
			}
		} catch (Exception e) {
			if(client!= null){
				client.rollback();
			}
		}
	}

	/**
	 * 记录各种游戏玩法相关的日志
	 * @param info
	 * @return
	 */
	public void insertPlayActionLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[0].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[0],strSql[0],date))
			{
				String newTable = oldTables[0]+date;
				strSql[0] = strSql[0].replace(newTables[0], newTable);
				newTables[0] = newTable;
			}
		}
		
		if(actionLogCount > 0 &&  (actionLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[0]+palyActionLogBuffer.toString().substring(0, palyActionLogBuffer.length()-1));
			actionLogCount = 0;
			palyActionLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 保存用户资源变动日志
	 * @param info
	 * @return
	 */
	public void insertMoneyLog() {
	
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[1].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[1],strSql[1],date))
			{
				String newTable = oldTables[1]+date;
				strSql[1] = strSql[1].replace(newTables[1], newTable);
				newTables[1] = newTable;
			}
		}
		
		if(moneyLogCount > 0 && (moneyLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[1]+moneyLogBuffer.toString().substring(0, moneyLogBuffer.length()-1));
			moneyLogCount = 0;
			moneyLogBuffer.setLength(0);
		}
	}

	/**
	 * 用户背包变动日志（不包含玩家交易）
	 * @param info
	 * @return
	 */
	public void insertItemLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[2].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[2],strSql[2],date))
			{
				String newTable = oldTables[2]+date;
				strSql[2] = strSql[2].replace(newTables[2], newTable);
				newTables[2] = newTable;
			}
		}
		
		if(itemLogCount > 0 && (itemLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[2]+itemLogBuffer.toString().substring(0, itemLogBuffer.length()-1));
			itemLogCount = 0;
			itemLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 玩家升级日志表
	 * @param log
	 * @return
	 */
	public void insertRoleUpgradeLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[3].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[3],strSql[3],date))
			{
				String newTable = oldTables[3]+date;
				strSql[3] = strSql[3].replace(newTables[3], newTable);
				newTables[3] = newTable;
			}
		}
		
		if(roleUpLogCount > 0 && (roleUpLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[3]+roleUpLogBuffer.toString().substring(0, roleUpLogBuffer.length()-1));
			roleUpLogCount = 0;
			roleUpLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 记录英雄觉醒相关的日志
	 * @param log
	 * @return
	 */
	public void insertHeroUpLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[4].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[4],strSql[4],date))
			{
				String newTable = oldTables[4]+date;
				strSql[4] = strSql[4].replace(newTables[4], newTable);
				newTables[4] = newTable;
			}
		}
		
		if(heroUpLogCount > 0 && (heroUpLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[4]+heroUpLogBuffer.toString().substring(0, heroUpLogBuffer.length()-1));
			heroUpLogCount = 0;
			heroUpLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 记录神兵升级日志
	 * @param log
	 * @return
	 */
	public void insertWeaponUpLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[5].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[5],strSql[5],date))
			{
				String newTable = oldTables[5]+date;
				strSql[5] = strSql[5].replace(newTables[5], newTable);
				newTables[5] = newTable;
			}
		}
		
		if(weapUpLogCount > 0 && (weapUpLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[5]+weapUpLogBuffer.toString().substring(0, weapUpLogBuffer.length()-1));
			weapUpLogCount = 0;
			weapUpLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 插入装备升级日志
	 * @param instanceLog
	 * @return
	 */
	public void insertEquipUpLog()
	{
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[6].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[6],strSql[6],date))
			{
				String newTable = oldTables[6]+date;
				strSql[6] = strSql[6].replace(newTables[6], newTable);
				newTables[6] = newTable;
			}
		}
		
		if(equipUpLogCount > 0 && (equipUpLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[6]+equipUpLogBuffer.toString().substring(0, equipUpLogBuffer.length()-1));
			equipUpLogCount = 0;
			equipUpLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 各商店物品购买日志
	 * 
	 * @param info	日志信息
	 */
	public void insertStoreBuyItemLog() {
		
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[7].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[7],strSql[7],date))
			{
				String newTable = oldTables[7]+date;
				strSql[7] = strSql[7].replace(newTables[7], newTable);
				newTables[7] = newTable;
			}
		}
		
		if(storeBugLogCount > 0 && (storeBugLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[7]+storeBugLogBuffer.toString().substring(0, storeBugLogBuffer.length()-1));
			storeBugLogCount = 0;
			storeBugLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 镖车日志
	 * 
	 * @param info	日志信息
	 */
	public void insertBiaocheLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[8].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[8],strSql[8],date))
			{
				String newTable = oldTables[8]+date;
				strSql[8] = strSql[8].replace(newTables[8], newTable);
				newTables[8] = newTable;
			}
		}
		
		if(biaocheLogCount > 0 && (biaocheLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[8]+biaocheLogBuffer.toString().substring(0, biaocheLogBuffer.length()-1));
			biaocheLogCount = 0;
			biaocheLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 兵种升级日志
	 * 
	 * @param info	日志信息
	 */
	public void insertSoliderUpLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[9].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[9],strSql[9],date))
			{
				String newTable = oldTables[9]+date;
				strSql[9] = strSql[9].replace(newTables[9], newTable);
				newTables[9] = newTable;
			}
		}
		
		if(soliderUpLogCount > 0 && (soliderUpLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[9]+soliderUpLogBuffer.toString().substring(0, soliderUpLogBuffer.length()-1));
			soliderUpLogCount = 0;
			soliderUpLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 技能升级插入数据库
	 * @param log
	 * @return
	 */
	public void insertHeroSkillUpLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[10].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[10],strSql[10],date))
			{
				String newTable = oldTables[10]+date;
				strSql[10] = strSql[10].replace(newTables[10], newTable);
				newTables[10] = newTable;
			}
		}
		
		if (heroSkillUpLogCount > 0 && (heroSkillUpLogCount >= logInsertNum || isShutDown)) {
			writeStringLog(strSql[10]+heroSkillUpLogBuffer.toString().substring(0, heroSkillUpLogBuffer.length()-1));
			heroSkillUpLogCount = 0;
			heroSkillUpLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 记录点金手日志
	 * @param log
	 */
	public void insertGoldBuyLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[11].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[11],strSql[11],date))
			{
				String newTable = oldTables[11]+date;
				strSql[11] = strSql[11].replace(newTables[11], newTable);
				newTables[11] = newTable;
			}
		}
		
		if (goldBuyLogCount > 0 && (goldBuyLogCount >= logInsertNum || isShutDown)) {
			writeStringLog(strSql[11]+goldBuyLogBuffer.toString().substring(0, goldBuyLogBuffer.length()-1));
			goldBuyLogCount = 0;
			goldBuyLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 装备熔炼与重铸
	 * @param to
	 */
	public void insertEquipResolveLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[12].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[12],strSql[12],date))
			{
				String newTable = oldTables[12]+date;
				strSql[12] = strSql[12].replace(newTables[12], newTable);
				newTables[12] = newTable;
			}
		}
		
		if(equipResolveLogCount > 0 && (equipResolveLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[12]+equipResolveLogBuffer.toString().substring(0, equipResolveLogBuffer.length()-1));
			equipResolveLogCount = 0;
			equipResolveLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 你争我夺日志
	 * @param to
	 */
	public void insertSnatchLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[13].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[13],strSql[13],date))
			{
				String newTable = oldTables[13]+date;
				strSql[13] = strSql[13].replace(newTables[13], newTable);
				newTables[13] = newTable;
			}
		}
		
		if(snatchLogCount > 0 && (snatchLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[13]+snatchLogBuffer.toString().substring(0, snatchLogBuffer.length()-1));
			snatchLogCount = 0;
			snatchLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 矿收益
	 * @param log
	 */
	public  void insertMineGetLog(){		
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		if(week == newTabelWeek && strSql[14].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[14],strSql[14],date))
			{
				String newTable = oldTables[14]+date;
				strSql[14] = strSql[14].replace(newTables[14], newTable);
				newTables[14] = newTable;
			}
		}
		
		if(mineGetLogCount > 0 && (mineGetLogCount >= logInsertNum || isShutDown)){
			
			writeStringLog(strSql[14]+mineGetLogBuffer.toString().substring(0, mineGetLogBuffer.length()-1));
			mineGetLogCount = 0;
			mineGetLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 公会模块日志
	 * @param to
	 */
	public void insertRoleClubLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[15].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[15],strSql[15],date))
			{
				String newTable = oldTables[15]+date;
				strSql[15] = strSql[15].replace(newTables[15], newTable);
				newTables[15] = newTable;
			}
		}
		
		if(roleClubLogCount > 0 && (roleClubLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[15]+roleClubLogBuffer.toString().substring(0, roleClubLogBuffer.length()-1));
			roleClubLogCount = 0;
			roleClubLogBuffer.setLength(0);
		}
		
	}
	
	/**
	 * 好友模块日志
	 * @param to
	 */
	public  void insertRoleRelationLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[16].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[16],strSql[16],date))
			{
				String newTable = oldTables[16]+date;
				strSql[16] = strSql[16].replace(newTables[16], newTable);
				newTables[16] = newTable;
			}
		}
		
		if(roleRelationLogCount > 0 && (roleRelationLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[16]+roleRelationLogBuffer.toString().substring(0, roleRelationLogBuffer.length()-1));
			roleRelationLogCount = 0;
			roleRelationLogBuffer.setLength(0);
		}
		
	}
	
	/**
	 * 插入练兵场日志
	 * @param instanceLog
	 * @return
	 */
	public void insertActivityLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[17].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[17],strSql[17],date))
			{
				String newTable = oldTables[17]+date;
				strSql[17] = strSql[17].replace(newTables[17], newTable);
				newTables[17] = newTable;
					
			}
		}
		
		if(activeLogCount > 0 && (activeLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[17]+activeLogBuffer.toString().substring(0, activeLogBuffer.length()-1));
			activeLogCount = 0;
			activeLogBuffer.setLength(0);
		}
	}
	
	
	/**
	 * 充值日志
	 * 
	 * @param log
	 */
	public void insertRoleChargeLog() {		
		
		//每周一新建日志表
		
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[18].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[18],strSql[18],date))
			{
				String newTable = oldTables[18]+date;
				strSql[18] = strSql[18].replace(newTables[18], newTable);
				newTables[18] = newTable;
			}
		}
		
		if(roleChargeLogCount > 0 && (roleChargeLogCount >= logInsertNum || isShutDown)){
			
			writeStringLog(strSql[18]+roleChargeLogBuffer.toString().substring(0, roleChargeLogBuffer.length()-1));
			roleChargeLogCount = 0;
			roleChargeLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 任务日志
	 * @param log
	 * @return
	 */
	public void insertTaskLog() {

		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[19].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[19],strSql[19],date))
			{
				String newTable = oldTables[19]+date;
				strSql[19] = strSql[19].replace(newTables[19], newTable);
				newTables[19] = newTable;
			}
		}
		
		if(taskLogCount > 0 &&  (taskLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[19]+taskLogBuffer.toString().substring(0, taskLogBuffer.length()-1));
			taskLogCount = 0;
			taskLogBuffer.setLength(0);
		}
	}
	

	/**
	 * 插入副本日志
	 * @param instanceLog
	 * @return
	 */
	public void insertInstanceLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[20].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[20],strSql[20],date))
			{
				String newTable = oldTables[20]+date;
				strSql[20] = strSql[20].replace(newTables[20], newTable);
				newTables[20] = newTable;
			}
		}
		
		if(instanceLogCount > 0 && (instanceLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[20]+instanceLogBuffer.toString().substring(0, instanceLogBuffer.length()-1));
			instanceLogCount = 0;
			instanceLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 英雄装备变动日志
	 * @param log
	 * @return
	 */
	public void insertEquipInfoLog() {

		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[21].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[21],strSql[21],date))
			{
				String newTable = oldTables[21]+date;
				strSql[21] = strSql[21].replace(newTables[21], newTable);
				newTables[21] = newTable;
			}
		}
		
		if(equipInfoLogCount > 0 && (equipInfoLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[21]+equipInfoLogBuffer.toString().substring(0, equipInfoLogBuffer.length()-1));
			equipInfoLogCount = 0;
			equipInfoLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 邮件附件领取日志
	 * @param log
	 * @return
	 */
	public void insertMailInfoLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[22].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[22],strSql[22],date))
			{
				String newTable = oldTables[22]+date;
				strSql[22] = strSql[22].replace(newTables[22], newTable);
				newTables[22] = newTable;
			}
		}
		
		if(mailLogCount > 0 && (mailLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[22]+mailLogBuffer.toString().substring(0, mailLogBuffer.length()-1));
			mailLogCount = 0;
			mailLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 记录副本开战
	 * @param nAmount
	 * @param time
	 */
	public void insertChallengeLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[23].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[23],strSql[23],date))
			{
				String newTable = oldTables[23]+date;
				strSql[23] = strSql[23].replace(newTables[23], newTable);
				newTables[23] = newTable;
			}
		}
		
		if(challengeLogCount > 0 && (challengeLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[23]+challengeLogBuffer.toString().substring(0, challengeLogBuffer.length()-1));
			challengeLogCount = 0;
			challengeLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 对攻战日志
	 * 
	 * @param info	日志信息
	 */
	public void insertGamePVELog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[24].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[24],strSql[24],date))
			{
				String newTable = oldTables[24]+date;
				strSql[24] = strSql[24].replace(newTables[24], newTable);
				newTables[24] = newTable;
			}
		}
		
		if(pveLogCount > 0 && (pveLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[24]+pveLogBuffer.toString().substring(0, pveLogBuffer.length()-1));
			pveLogCount = 0;
			pveLogBuffer.setLength(0);
		}
	}


	/**
	 * 竞技场结算日志
	 * @param to
	 */
	public void insertRoleAreanLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[25].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[25],strSql[25],date))
			{
				String newTable = oldTables[25]+date;
				strSql[25] = strSql[25].replace(newTables[25], newTable);
				newTables[25] = newTable;
			}
		}
		
		if(arenaLogCount > 0 && (arenaLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[25]+arenaLogBuffer.toString().substring(0, arenaLogBuffer.length()-1));
			arenaLogCount = 0;
			arenaLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 兵来将挡战斗日志
	 * @param to
	 */
	public void insertDefendFightLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[26].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[26],strSql[26],date))
			{
				String newTable = oldTables[26]+date;
				strSql[26] = strSql[26].replace(newTables[26], newTable);
				newTables[26] = newTable;
			}
		}
		
		if(defendLogCount > 0 && (defendLogCount >= logInsertNum || isShutDown)){

			writeStringLog(strSql[26]+defendLogBuffer.toString().substring(0, defendLogBuffer.length()-1));
			defendLogCount = 0;
			defendLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 矿战斗日志
	 * @param log
	 */
	public void insertMineFightLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[27].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[27],strSql[27],date))
			{
				String newTable = oldTables[27]+date;
				strSql[27] = strSql[27].replace(newTables[27], newTable);
				newTables[27] = newTable;
			}
		}
		
		if(mineFightLogCount > 0 && (mineFightLogCount >= logInsertNum || isShutDown)){
			
			writeStringLog(strSql[27]+mineFightLogBuffer.toString().substring(0, mineFightLogBuffer.length()-1));
			mineFightLogCount = 0;
			mineFightLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 攻城略地日志
	 * @param to
	 */
	public void insertRoleCampLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[28].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[28],strSql[28],date))
			{
				String newTable = oldTables[28]+date;
				strSql[28] = strSql[28].replace(newTables[28], newTable);
				newTables[28] = newTable;
			}
		}
		
		if(campLogCount > 0 && (campLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[28]+campLogBuffer.toString().substring(0, campLogBuffer.length()-1));
			campLogCount = 0;
			campLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 记录PVP竞技场日志
	 * 
	 * @param info	日志信息
	 */
	public void insertCompetitiveLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[29].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[29],strSql[29],date))
			{
				String newTable = oldTables[29]+date;
				strSql[29] = strSql[29].replace(newTables[29], newTable);
				newTables[29] = newTable;
			}
		}
		
		if(competitiveLogCount > 0 && (competitiveLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[29]+competitiveLogBuffer.toString().substring(0, competitiveLogBuffer.length()-1));
			competitiveLogCount = 0;
			competitiveLogBuffer.setLength(0);
		}
	}

	public void insertPvp3Log() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[30].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[30],strSql[30],date))
			{
				String newTable = oldTables[30]+date;
				strSql[30] = strSql[30].replace(newTables[30], newTable);
				newTables[30] = newTable;
			}
		}
		
		if(pvp3LogCount > 0 && (pvp3LogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[30]+pvp3LogBuffer.toString().substring(0, pvp3LogBuffer.length()-1));
			pvp3LogCount = 0;
			pvp3LogBuffer.setLength(0);
		}
	}
	
	/**
	 * 大地图战斗日志
	 * @param to
	 */
	public void insertMapFightLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[31].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[31],strSql[31],date))
			{
				String newTable = oldTables[31]+date;
				strSql[31] = strSql[31].replace(newTables[31], newTable);
				newTables[31] = newTable;
			}
		}
		
		if(mapFightLogCount > 0 && (mapFightLogCount >= logInsertNum || isShutDown)){
			
			writeStringLog(strSql[31]+mapFightLogBuffer.toString().substring(0, mapFightLogBuffer.length()-1));
			mapFightLogCount = 0;
			mapFightLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 惩罚玩家日志
	 * @param log
	 * @return
	 */
	public void insertPunishLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[32].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[32],strSql[32],date))
			{
				String newTable = oldTables[32]+date;
				strSql[32] = strSql[32].replace(newTables[32], newTable);
				newTables[32] = newTable;
			}
		}
		
		if(punishLogCount > 0 && (punishLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[32]+punishLogBuffer.toString().substring(0, punishLogBuffer.length()-1));
			punishLogCount = 0;
			punishLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 记录角色名变动日志
	 * @param roleNameLog
	 */
	public void insertRoleNameLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[33].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[33],strSql[33],date))
			{
				String newTable = oldTables[33]+date;
				strSql[33] = strSql[33].replace(newTables[33], newTable);
				newTables[33] = newTable;
			}
		}
		
		if(roleNameLogCount > 0)
		{
			writeStringLog(strSql[33]+roleNameLogBuffer.toString().substring(0, roleNameLogBuffer.length()-1));
			roleNameLogCount = 0;
			roleNameLogBuffer.setLength(0);
		}
		
	}
	
	
	/**
	 * 世界BOSS战斗日志
	 * @param to
	 */
	public synchronized void insertWorldBossFightLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[34].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[34],strSql[34],date))
			{
				String newTable = oldTables[34]+date;
				strSql[34] = strSql[34].replace(newTables[34], newTable);
				newTables[34] = newTable;
			}
		}
		
		if(worldBossLogCount > 0 && (worldBossLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[34]+worldBossLogBuffer.toString().substring(0, worldBossLogBuffer.length()-1));
			worldBossLogCount = 0;
			worldBossLogBuffer.setLength(0);
		}
	}
	

	/**
	 * 记录极效操作日志
	 * @param log
	 * @return
	 */
	public void insertGameToolOperateLog() {
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[35].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[35],strSql[35],date))
			{
				String newTable = oldTables[35]+date;
				strSql[35] = strSql[35].replace(newTables[35], newTable);
				newTables[35] = newTable;
			}
		}
		
		if(toolOperateLogCount > 0 && (toolOperateLogCount >= logInsertNum || isShutDown))
		{
			writeStringLog(strSql[35]+toolOperateLogBuffer.toString().substring(0, toolOperateLogBuffer.length()-1));
			toolOperateLogCount = 0;
			toolOperateLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 保存角色上线的日志
	 * @param info
	 * @return
	 */
	public void insertRoleLogin() {
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[36].indexOf(date) == -1)
		{
			// 周一建新表前将当前在线玩家登出时间更新.
			List<String> loginIdList = new ArrayList<String>();
			Set<Integer> set = RoleLoginMap.getSet();
			for(Integer roleId:set)
			{
				RoleInfo roleInfo =  RoleLoginMap.getRoleInfo(roleId);
				if(roleInfo == null)
				{
					continue;
				}
				String loginLogId = roleInfo.getLoginLogId();
				if(loginLogId != null && loginLogId.length() > 0)
				{
					loginIdList.add(loginLogId);
					roleInfo.setLoginLogId("");
				}
			}
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			GameLogDAO.getInstance().updateRoleLog(loginIdList, now);
			
			//原角色登录日志更新
			synchronized(roleOutLogList)
			{
				updateRoleLogin(true);
			}
			
			// 键新表
			if(createNewLogTabel(oldTables[36],strSql[36],date))
			{
				String newTable = oldTables[36]+date;
				strSql[36] = strSql[36].replace(newTables[36], newTable);
				strSql[37] = strSql[37].replace(newTables[36], newTable);
				newTables[37] = newTable;
				newTables[36] = newTable;
			}
			
			//周一日志分表,在线玩家需在新表中插入登录时间,防止在线玩家跨周在线时日志丢失
			for(Integer roleId:set)
			{
				RoleInfo roleInfo =  RoleLoginMap.getRoleInfo(roleId);
				if(roleInfo!=null)
				{
					Timestamp now1 = new Timestamp(System.currentTimeMillis());
					GameLogService.insertRoleLog(roleInfo, roleInfo.getLoginIp(),roleInfo.getMac()+"",roleInfo.getClientType()+"", now1,now1,"");
				}
			}
		}
		
		if(roleLogCount > 0)
		{
			writeStringLog(strSql[36]+roleLogBuffer.toString().substring(0, roleLogBuffer.length()-1));
			roleLogCount = 0;
			roleLogBuffer.setLength(0);
		}
		
	}

	/**
	 * 记录角色下线日志
	 * @param loginLogId
	 * @param logoutime
	 * @return
	 */
	public void updateRoleLog(List<String> loginIdList, Timestamp logoutime) 
	{
		if(loginIdList != null && loginIdList.size() > 0)
		{
			synchronized(roleOutLogList)
			{
				for(String ser : loginIdList)
				{
					RoleOutLog log = new RoleOutLog();
					log.setSerial(ser);
					log.setLogoutTime(logoutime);
					roleOutLogList.add(log);
				}
				
				updateRoleLogin(false);
			}
		}
	}

	/**
	 * 更新角色下线时间
	 * @param loginLogId
	 * @param logoutime
	 * @return
	 */
	public void updateRoleLogin(boolean intoFlag) 
	{
		if(roleOutLogList != null && roleOutLogList.size() > 0 && (roleOutLogList.size() >= logInsertNum || isShutDown || intoFlag))
		{
			SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_LOG_DB).openSession(ExecutorType.BATCH,false);
			Connection conn = null;
			PreparedStatement psmt = null;
			try{
				conn = session.getConnection();
				psmt = conn.prepareStatement(strSql[37]);
				for(RoleOutLog outLog : roleOutLogList)
				{
					if(outLog == null)
					{
						continue;
					}
					psmt.setTimestamp(1, outLog.getLogoutTime());
					psmt.setString(2, outLog.getSerial());
					psmt.addBatch();
					
				}
				psmt.executeBatch();
				conn.commit();
				session.commit();
			}catch (SQLException e) {
				logger.error("updateRoleLogin error", e);
				try {
					conn.rollback();
					session.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}finally{
				try {
					if (psmt != null) {
						psmt.close();
					}
					
					if (conn != null) {
						conn.close();
					}
					
					if (session != null) {
						session.close();
					}
				} catch (SQLException e) {
					logger.error("updateRoleLogin 1 error", e);
				}
			
			}
			roleOutLogList.clear();
		}
	}

	/**
	 * 记录注册用户日志
	 * @param nAmount
	 * @param time
	 */
	public void addStatNewAccount(StatLog statOnlineLog) {
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[38].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[38],strSql[38],date))
			{
				String newTable = oldTables[38]+date;
				strSql[38] = strSql[38].replace(newTables[38], newTable);
				newTables[38] = newTable;
			}
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		
		buffer.append(statOnlineLog.getnAmount());
		buffer.append(",");
		
		buffer.append("'");
		buffer.append(statOnlineLog.getTimestamp());
		buffer.append("'");
		
		buffer.append(")");
		
		writeStringLog(strSql[38]+buffer.toString());
	}

	/**
	 * 记录开卡用户日志
	 * @param nAmount
	 * @param time
	 */
	public void addStatNewCard(StatLog statNewCardLog) {		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[39].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[39],strSql[39],date))
			{
				String newTable = oldTables[39]+date;
				strSql[39] = strSql[39].replace(newTables[39], newTable);
				newTables[39] = newTable;
			}
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		
		buffer.append(statNewCardLog.getnAmount());
		buffer.append(",");
		
		buffer.append("'");
		buffer.append(statNewCardLog.getTimestamp());
		buffer.append("'");
		
		buffer.append(")");
		
		
		writeStringLog(strSql[39]+buffer.toString());
	}
	
	/**
	 * 记录在线用户日志
	 * @param nAmount
	 * @param time
	 */
	public void addStatOnline(StatLog statOnlineLog) {		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[40].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[40],strSql[40],date))
			{
				String newTable = oldTables[40]+date;
				strSql[40] = strSql[40].replace(newTables[40], newTable);
				newTables[40] = newTable;
			}
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		
		buffer.append(statOnlineLog.getnAmount());
		buffer.append(",");
		
		buffer.append("'");
		buffer.append(statOnlineLog.getTimestamp());
		buffer.append("'");
		
		buffer.append(")");
		
		writeStringLog(strSql[40]+buffer.toString());
	}
	
	/**
	 * 记录加速玩家日志
	 * @param to
	 */
	public synchronized void insertCheckTimeLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[41].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[41],strSql[41],date))
			{
				String newTable = oldTables[41]+date;
				strSql[41] = strSql[41].replace(newTables[41], newTable);
				newTables[41] = newTable;
			}
		}
		
		if(checkTimeLogCount > 0 && (checkTimeLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[41]+checkTimeBuffer.toString().substring(0, checkTimeBuffer.length()-1));
			checkTimeLogCount = 0;
			checkTimeBuffer.setLength(0);
		}
	}
	
	/**
	 * 副本异常日志表
	 * @param to
	 */
	public synchronized void insertChallengeUnusualLog(){
		
		//每周一新建日志表
		int week = getWeekMonday();
		String date = gateDate();
		
		if(week == newTabelWeek && strSql[42].indexOf(date) == -1)
		{
			if(createNewLogTabel(oldTables[42],strSql[42],date))
			{
				String newTable = oldTables[42]+date;
				strSql[42] = strSql[42].replace(newTables[42], newTable);
				newTables[42] = newTable;
			}
		}
		
		if(challengeUnusualLogCount > 0 && (challengeUnusualLogCount >= logInsertNum || isShutDown)){
			writeStringLog(strSql[42]+challengeUnusualLogBuffer.toString().substring(0, challengeUnusualLogBuffer.length()-1));
			challengeUnusualLogCount = 0;
			challengeUnusualLogBuffer.setLength(0);
		}
	}
	
	/**
	 * 活动开启关闭日志
	 * @param log
	 * @return
	 */
	public synchronized void insertProgramLog(ProgramLog log) {
		if(log != null){
			programLogList.add(log);
		}
		if(programLogList != null && programLogList.size() > 0 && (programLogList.size() >= logInsertNum || isShutDown)){
			LogMessage logMessage = new LogMessage(ETimeMessageType.EXECUTE_BATCH_LOGS, programLogList);
			this.writeLog(logMessage);
			programLogList = new ArrayList<ProgramLog>();
		}
	}

	
	// 关服时,不满100数量的日志全部入库
	public void shutDown()
	{
		isShutDown = true;
		synchronized(palyActionLogBuffer){
			insertPlayActionLog();
		}
		synchronized(roleOutLogList){
			updateRoleLogin(true);
		}
		synchronized(taskLogBuffer){
			insertTaskLog();
		}
		synchronized(moneyLogBuffer){
			insertMoneyLog();
		}
		synchronized(itemLogBuffer){
			insertItemLog();
		}
		synchronized(roleUpLogBuffer){
			insertRoleUpgradeLog();
		}
		synchronized(instanceLogBuffer){
			insertInstanceLog();
		}
		synchronized(punishLogBuffer){
			insertPunishLog();
		}
		insertProgramLog(null);
		synchronized(heroUpLogBuffer){
			insertHeroUpLog();
		}
		synchronized(weapUpLogBuffer){
			insertWeaponUpLog();
		}
		synchronized(equipInfoLogBuffer){
			insertEquipInfoLog();
		}
		synchronized(equipUpLogBuffer){
			insertEquipUpLog();
		}
		synchronized(mailLogBuffer){
			insertMailInfoLog();
		}
		synchronized(storeBugLogBuffer){
			insertStoreBuyItemLog();
		}
		synchronized(biaocheLogBuffer){
			insertBiaocheLog();
		}
		synchronized(soliderUpLogBuffer){
			insertSoliderUpLog();
		}
		synchronized(challengeLogBuffer){
			insertChallengeLog();
		}
		synchronized(heroSkillUpLogBuffer){
			insertHeroSkillUpLog();
		}
		synchronized(goldBuyLogBuffer){
			insertGoldBuyLog();
		}
		synchronized(pveLogBuffer){
			insertGamePVELog();
		}
		synchronized(arenaLogBuffer){
			insertRoleAreanLog();
		}
		synchronized(roleRelationLogBuffer){
			insertRoleRelationLog();
		}
		synchronized(roleClubLogBuffer){
			insertRoleClubLog();
		}
		synchronized(equipResolveLogBuffer){
			insertEquipResolveLog();
		}
		synchronized(campLogBuffer){
			insertRoleCampLog();
		}
		synchronized(snatchLogBuffer){
			insertSnatchLog();
		}
		synchronized(worldBossLogBuffer)
		{
			insertWorldBossFightLog();
		}
		synchronized(activeLogBuffer){
			insertActivityLog();
		}
		synchronized(defendLogBuffer){
			insertDefendFightLog();
		}
		synchronized(pveLogBuffer){
			insertGamePVELog();
		}
		synchronized(mapFightLogBuffer){
			insertMapFightLog();
		}
		synchronized(mineFightLogBuffer){
			insertMineFightLog();
		}
		synchronized(mineGetLogBuffer){
			insertMineGetLog();
		}
		synchronized(roleChargeLogBuffer){
			insertRoleChargeLog();
		}
		synchronized(competitiveLogBuffer){
			insertCompetitiveLog();
		}
		synchronized(pvp3LogBuffer){
			insertPvp3Log();
		}
		synchronized(toolOperateLogBuffer)
		{
			insertGameToolOperateLog();
		}
		synchronized(roleNameLogBuffer)
		{
			insertRoleNameLog();
		}
		synchronized(roleLogBuffer)
		{
			insertRoleLogin();
		}

		synchronized(checkTimeBuffer)
		{
			insertCheckTimeLog();
		}
		
		synchronized(challengeUnusualLogBuffer)
		{
			insertChallengeUnusualLog();
		}
		
		
		isShutDown = false;
	}
	
	public void writeToFile(String sql){
		// 执行未成功,写入失败文件
		if (System.currentTimeMillis() - logStartTime > TWO_DAY) {
			logStartTime = System.currentTimeMillis();
			
			try {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(logStartTime);
				
				if (pw != null) {
					pw.flush();
					pw.close();
					
					File file = new File(basePath + File.separator + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + "failed.sql");
					
					if (!file.exists()) {
						file.createNewFile();
					}
					
					// 打开文件句柄，用于失败写入
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), Charset.forName("UTF-8")), 2048));
					pw.write("-- Start " + new Date() + System.getProperty("line.separator", "\n"));
					pw.flush();
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("GameLogDAO file error", e);
				}
			}
		}
		
		pw.write(sql);
		pw.write(System.getProperty("line.separator", "\n"));
		pw.flush();
		pw.close();
	}
	
	
	private void writeLog(LogMessage logMessage) {
		ExecuteLogsThread executeLogsThread = GameConfig.getInstance().getExecuteLogsThread();
		if(executeLogsThread != null)
		{
			logger.info("======================" + executeLogsThread.getName() + " execute");
			
			executeLogsThread.addMessage(logMessage);
		}
	}
	
	private void writeStringLog(String sql) {
		TempExecuteLogsThread tempExecuteLogsThread = GameConfig.getInstance().getTempExecuteLogsThread();
		if(tempExecuteLogsThread != null)
		{
			logger.info("======================" + tempExecuteLogsThread.getName() + " execute");
			
			tempExecuteLogsThread.addMessage(sql);
		}
	}
	
	
	/**
	 * 周一了检测是否要建新日志表
	 * 
	 * @param timeMessage
	 */
	private boolean createNewLogTabel(String table,String strSql,String date) {
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_LOG_DB).openSession(ExecutorType.SIMPLE,false);
		Connection conn = null;
		ResultSet rs = null;
		Statement psmt = null;
		try{
			conn = session.getConnection();
			String newTableName = table+date;
			
			rs = conn.getMetaData().getTables(null, null, newTableName,null);
			
			if(!rs.next())
			{
				psmt = conn.createStatement();
				String sql = "CREATE TABLE "+newTableName+" LIKE "+table;
				psmt.executeUpdate(sql);
			}
			conn.commit();
			session.commit();
		}catch (SQLException e) {
			logger.error("createNewLogTabel error", e);
			try {
				conn.rollback();
				session.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}finally{
			try {
				if (psmt != null) {
					psmt.close();
				}
				
				if (rs != null) {
					rs.close();
				}
				
				if (conn != null) {
					conn.close();
				}
				
				if (session != null) {
					session.close();
				}
			} catch (SQLException e) {
				logger.error("createNewLogTabel 1 error", e);
			}
		
		}
		
		return true;
	}
	
	
	/**
	 * 检测是否存在本周日志表。
	 * 
	 * @param timeMessage
	 */
	private boolean getLogTabel(String date1) {
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_LOG_DB).openSession(ExecutorType.SIMPLE,false);
		Connection conn = null;
		ResultSet rs = null;
		Statement psmt = null;
		try{
			conn = session.getConnection();
			
			for(int i = 0 ; i <oldTables.length ; i++)
			{
				if(oldTables[i] == null)
				{
					continue;
				}
				
				String newTableName = oldTables[i]+date1;
				
				rs = conn.getMetaData().getTables(null, null, newTableName,null);
				
				if(!rs.next())
				{
					psmt = conn.createStatement();
					String sql = "CREATE TABLE "+newTableName+" LIKE "+oldTables[i];
					psmt.executeUpdate(sql);
					
				}
				strSql[i] = strSql[i].replace(oldTables[i], newTableName);
				newTables[i] = newTableName;
			}
			conn.commit();
			session.commit();
		}catch (SQLException e) {
			logger.error("getLogTabel error", e);
			e.printStackTrace();
			try {
				conn.rollback();
				session.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}finally{
			try {
				if (psmt != null) {
					psmt.close();
				}
				
				if (rs != null) {
					rs.close();
				}
				
				if (conn != null) {
					conn.close();
				}
				
				if (session != null) {
					session.close();
				}
			} catch (SQLException e) {
				logger.error("getLogTabel 1 error", e);
			}
		}
		
		return true;
	}
	
	private String gateDate()
	{
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(System.currentTimeMillis());
		
		return date;
	}
	
	private int getWeekMonday()
	{
		Calendar cal = Calendar.getInstance();		
		int week = cal.get(Calendar.DAY_OF_WEEK)-1;
		return week;
	}
	
	//服务器启动检测本周日志表是否存在
	public boolean checkThisWeekLog()
	{
		String date1 = getMoneyDate();
		
		//String playActionNewTable = "GAME_PLAYER_ACTION_LOG_"+date1;
		//playActionSqlStr = playActionSqlStr.replace("GAME_PLAYER_ACTION_LOG", playActionNewTable);
		oldTables[0] = "GAME_PLAYER_ACTION_LOG";
		strSql[0] = playActionSqlStr;
		
		//String moneyNewTable = "GAME_SNATCH_LOG_"+date1;
		//moneyLogSqlStr = moneyLogSqlStr.replace("MONEY_LOG", moneyNewTable);
		oldTables[1] = "MONEY_LOG";
		strSql[1] = moneyLogSqlStr;
		
		//String itemNewTable = "ITEM_LOG_"+date1;
		//itemLogSqlStr = itemLogSqlStr.replace("ITEM_LOG", itemNewTable);
		oldTables[2] = "ITEM_LOG";
		strSql[2] = itemLogSqlStr;
		
		
		//String roleUpNewTable = "ROLE_UPGRADE_LOG_"+date1;
		//roleUpSqlStr = roleUpSqlStr.replace("ROLE_UPGRADE_LOG", roleUpNewTable);
		oldTables[3] = "ROLE_UPGRADE_LOG";
		strSql[3] = roleUpSqlStr;
		
		//String heroUpNewTable = "GAME_HERO_UP_LOG_"+date1;
		//heroUpSqlStr = heroUpSqlStr.replace("GAME_HERO_UP_LOG", heroUpNewTable);
		oldTables[4] = "GAME_HERO_UP_LOG";
		strSql[4] = heroUpSqlStr;
		
		//String weapUpNewTable = "GAME_WEAPON_UP_LOG_"+date1;
		//weapUpSqlStr = weapUpSqlStr.replace("GAME_WEAPON_UP_LOG", weapUpNewTable);
		oldTables[5] = "GAME_WEAPON_UP_LOG";
		strSql[5] = weapUpSqlStr;
		
		//String equipUpNewTable = "GAME_EQUIP_UP_LOG_"+date1;
		//equipUpSqlStr = equipUpSqlStr.replace("GAME_EQUIP_UP_LOG", equipUpNewTable);
		oldTables[6] = "GAME_EQUIP_UP_LOG";
		strSql[6] = equipUpSqlStr;
		
		//String storeBuyTable = "GAME_STORE_BUY_ITEM_LOG_"+date1;
		//storeBuySqlStr = storeBuySqlStr.replace("GAME_STORE_BUY_ITEM_LOG", storeBuyTable);
		oldTables[7] = "GAME_STORE_BUY_ITEM_LOG";
		strSql[7] = storeBuySqlStr;
		
		//String biaocheNewTable = "GAME_BIAO_CHE_LOG_"+date1;
		//biaocheSqlStr = biaocheSqlStr.replace("GAME_BIAO_CHE_LOG", biaocheNewTable);
		oldTables[8] = "GAME_BIAO_CHE_LOG";
		strSql[8] = biaocheSqlStr;
		
		//String soliderUpNewTable = "GAME_SOLDIER_UP_LOG_"+date1;
		//soliderUpSqlStr = soliderUpSqlStr.replace("GAME_SOLDIER_UP_LOG", soliderUpNewTable);
		oldTables[9] = "GAME_SOLDIER_UP_LOG";
		strSql[9] = soliderUpSqlStr;
		
		//String skillUpNewTable = "GAME_HERO_SKILL_UP_LOG_"+date1;
		//skillSqlStr = skillSqlStr.replace("GAME_HERO_SKILL_UP_LOG", skillUpNewTable);
		oldTables[10] = "GAME_HERO_SKILL_UP_LOG";
		strSql[10] = skillSqlStr;
		
		//String goldBuyTable = "GAME_GOLD_BUY_LOG_"+date1;
		//goldBuySqlStr = goldBuySqlStr.replace("GAME_GOLD_BUY_LOG", goldBuyTable);
		oldTables[11] = "GAME_GOLD_BUY_LOG";
		strSql[11] = goldBuySqlStr;
		
		//String equipResolveNewTable = "GAME_EQUIP_RESOLVE_LOG_"+date1;
		//equipResolveSqlStr = equipResolveSqlStr.replace("GAME_EQUIP_RESOLVE_LOG", equipResolveNewTable);
		oldTables[12] = "GAME_EQUIP_RESOLVE_LOG";
		strSql[12] = equipResolveSqlStr;
		
		//String snatchNewTable = "GAME_SNATCH_LOG_"+date1;
		//snatchSqlStr = snatchSqlStr.replace("GAME_SNATCH_LOG", snatchNewTable);
		oldTables[13] = "GAME_SNATCH_LOG";
		strSql[13] = snatchSqlStr;
		
		//String mineGetNewTable = "MINE_GET_LOG_"+date1;
		//mineGetSqlStr = mineGetSqlStr.replace("MINE_GET_LOG", mineGetNewTable);
		oldTables[14] = "MINE_GET_LOG";
		strSql[14] = mineGetSqlStr;
		
		//String roleClubNewTable = "GAME_ROLE_CLUB_LOG_"+date1;
		//roleClubSqlStr = roleClubSqlStr.replace("GAME_ROLE_CLUB_LOG", roleClubNewTable);
		oldTables[15] = "GAME_ROLE_CLUB_LOG";
		strSql[15] = roleClubSqlStr;
		
		//String roleRelationTableNewTable = "GAME_ROLE_RELATION_LOG_"+date1;
		//roleRelationSqlStr = roleRelationSqlStr.replace("GAME_ROLE_RELATION_LOG", roleRelationTableNewTable);
		oldTables[16] = "GAME_ROLE_RELATION_LOG";
		strSql[16] = roleRelationSqlStr;
		
		//String activeNewTable = "ACTIVITY_LOG_"+date1;
		//activeSqlStr = activeSqlStr.replace("ACTIVITY_LOG", activeNewTable);
		oldTables[17] = "ACTIVITY_LOG";
		strSql[17] = activeSqlStr;
		
		//String chargeNewTable = "ROLE_CHARGE_LOG_"+date1;
		//chargeSqlStr = chargeSqlStr.replace("ROLE_CHARGE_LOG", chargeNewTable);
		oldTables[18] = "ROLE_CHARGE_LOG";
		strSql[18] = chargeSqlStr;
		
		//String taskNewTable = "TASK_LOG_"+date1;
		//taskSqlStr = taskSqlStr.replace("TASK_LOG", taskNewTable);
		oldTables[19] = "TASK_LOG";
		strSql[19] = taskSqlStr;
		
		//String instanceNewTable = "INSTANCE_LOG_"+date1;
		//instanceSqlStr = instanceSqlStr.replace("INSTANCE_LOG", instanceNewTable);
		oldTables[20] = "INSTANCE_LOG";
		strSql[20] = instanceSqlStr;
		
		//String equipNewTable = "GAME_EQUIP_INFO_LOG_"+date1;
		//equipSqlStr = equipSqlStr.replace("GAME_EQUIP_INFO_LOG", equipNewTable);
		oldTables[21] = "GAME_EQUIP_INFO_LOG";
		strSql[21] = equipSqlStr;
		
		//String mailNewTable = "GAME_MAIL_LOG_"+date1;
		//mailSqlStr = mailSqlStr.replace("GAME_MAIL_LOG", mailNewTable);
		oldTables[22] = "GAME_MAIL_LOG";
		strSql[22] = mailSqlStr;
		
		//String challengeNewTable = "CHALLENGE_LOG_"+date1;
		//challengeSqlStr = challengeSqlStr.replace("CHALLENGE_LOG", challengeNewTable);
		oldTables[23] = "CHALLENGE_LOG";
		strSql[23] = challengeSqlStr;
		
		//String pveNewTable = "GAME_PVE_LOG_"+date1;
		//pveSqlStr = pveSqlStr.replace("GAME_PVE_LOG", pveNewTable);
		oldTables[24] = "GAME_PVE_LOG";
		strSql[24] = pveSqlStr;
		
		//String arenaNewTable = "GAME_ROLE_ARENA_LOG_"+date1;
		//arenaSqlStr = arenaSqlStr.replace("GAME_ROLE_ARENA_LOG", arenaNewTable);
		oldTables[25] = "GAME_ROLE_ARENA_LOG";
		strSql[25] = arenaSqlStr;
		
		//String defendNewTable = "DEFEND_FIGHT_LOG_"+date1;
		//defendSqlStr = defendSqlStr.replace("DEFEND_FIGHT_LOG", defendNewTable);
		oldTables[26] = "DEFEND_FIGHT_LOG";
		strSql[26] = defendSqlStr;
		
		//String mineFightNewTable = "MINE_FIGHT_LOG_"+date1;
		//mineFightSqlStr = mineFightSqlStr.replace("MINE_FIGHT_LOG", mineFightNewTable);
		oldTables[27] = "MINE_FIGHT_LOG";
		strSql[27] = mineFightSqlStr;
		
		//String campFightNewTable = "GAME_CAMP_LOG_"+date1;
		//campSqlStr = campSqlStr.replace("GAME_CAMP_LOG", campFightNewTable);
		oldTables[28] = "GAME_CAMP_LOG";
		strSql[28] = campSqlStr;
		
		//String competitveNewTable = "COMPETITIVE_LOG_"+date1;
		//competitiveLogSqlStr = competitiveLogSqlStr.replace("COMPETITIVE_LOG", competitveNewTable);
		oldTables[29] = "COMPETITIVE_LOG";
		strSql[29] = competitiveLogSqlStr;
		
		//String pvp3NewTable = "GAME_PVP_3_LOG_"+date1;
		//pvp3LogSqlStr = pvp3LogSqlStr.replace("GAME_PVP_3_LOG", pvp3NewTable);
		oldTables[30] = "GAME_PVP_3_LOG";
		strSql[30] = pvp3LogSqlStr;
		
		//String pvp3NewTable = "MAP_FIGHT_LOG_"+date1;
		//mapFightSqlStr = pvp3LogSqlStr.replace("MAP_FIGHT_LOG", pvp3NewTable);
		oldTables[31] = "MAP_FIGHT_LOG";
		strSql[31] = mapFightSqlStr;
		
		oldTables[32] = "PUNISH_LOG";
		strSql[32] = punishSqlStr;
		
		oldTables[33] = "GAME_ROLE_NAME_LOG";
		strSql[33] = roleNameSqlStr;
		
		oldTables[34] = "WORLD_BOSS_FIGHT_LOG";
		strSql[34] = worldBossSqlStr;
		
		oldTables[35] = "GAME_TOOL_OPERATE_LOG";
		strSql[35] = toolOperateSqlStr;
		
		oldTables[36] = "ROLE_LOG";
		strSql[36] = roleSqlStr;
		
		oldTables[37] = "ROLE_LOG";
		strSql[37] = updaRoleSqlStr;
		
		oldTables[38] = "STAT_NEWACCOUNT";
		strSql[38] = newAccountSqlStr;
		
		oldTables[39] = "STAT_NEWCARD";
		strSql[39] = newCardSqlStr;
		
		oldTables[40] = "STAT_ONLINE";
		strSql[40] = onlineSqlStr;
		
		oldTables[41] = "CHECK_TIME_LOG";
		strSql[41] = checkTimeSqlStr;
		
		oldTables[42] = "CHALLENGE_UNUSUAL_LOG";
		strSql[42] = challengeUnusualSqlStr;
		
		if(!getLogTabel(date1)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取每周的周一日期
	 * @return
	 */
	public String getMoneyDate()
	{
		Calendar cal = Calendar.getInstance();
		
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		
		Date time = new Timestamp(System.currentTimeMillis());
		
		cal.setTime(time);
		
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		if(1 == dayWeek)
		{
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		
		int day =  cal.get(Calendar.DAY_OF_WEEK);
		
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() -day);
		
		String monday = format.format(cal.getTime());
		
		return monday;
	}
}
