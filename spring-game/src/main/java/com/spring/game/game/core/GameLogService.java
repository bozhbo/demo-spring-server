package com.snail.webgame.game.core;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.fightdata.ArmyFightingInfo;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MailInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.log.ChallengeLog;
import com.snail.webgame.game.info.log.CompetitiveLog;
import com.snail.webgame.game.info.log.ConfigType;
import com.snail.webgame.game.info.log.DefendFightLog;
import com.snail.webgame.game.info.log.GamePVELog;
import com.snail.webgame.game.info.log.GamePvp3Log;
import com.snail.webgame.game.info.log.MapFightLog;
import com.snail.webgame.game.info.log.MineFightLog;
import com.snail.webgame.game.info.log.RoleArenaLog;
import com.snail.webgame.game.info.log.RoleCampLog;
import com.snail.webgame.game.info.log.StatLog;
import com.snail.webgame.game.info.log.WorldBossFightLog;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;

public class GameLogService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static GameLogDAO gameLogDAO = GameLogDAO.getInstance();

	/**
	 * 行为日志(默认完成)
	 * @param roleInfo
	 * @param gameAction
	 * @param actValue
	 * @return
	 */
	public static void insertPlayActionLog(RoleInfo roleInfo, int gameAction, String actValue) {
		insertPlayActionLog(roleInfo, gameAction, 1, actValue);
	}

	/**
	 * 行为日志
	 * @param roleInfo
	 * @param gameAction
	 * @param type 0-开始，1-完成，2-未完成
	 * @param actValue
	 * @return
	 */
	public static void insertPlayActionLog(RoleInfo roleInfo, int gameAction, int type, String actValue) {
		/*PlayActionLog log = new PlayActionLog();
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setRoleName(log.getRoleName().replace("\\", ""));
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		log.setActId(String.valueOf(gameAction));
		log.setType(type);
		log.setActValue(actValue);
		gameLogDAO.insertPlayActionLog(log);*/
		
		
		StringBuffer buffer = GameLogDAO.palyActionLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(String.valueOf(gameAction));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(type);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(actValue);
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.actionLogCount++;
			
			gameLogDAO.insertPlayActionLog();
		}
	}

	/**
	 * 用户登录 登出日志
	 * @param roleInfo
	 * @param ip
	 * @param mac
	 * @param comment
	 * @return
	 */
	public static void insertRoleLog(RoleInfo roleInfo, String ip, String mac, String osType, Timestamp loginTime,
			Timestamp logoutTime, String comment) {
		/*RoleLog log = new RoleLog();
		String ser = UUID.randomUUID().toString();
		roleInfo.setLoginId(UUID.randomUUID().toString().replace("-", ""));
		log.setSerial(ser.replace("-", ""));
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());*/
		roleInfo.setLoginLogId(UUID.randomUUID().toString().replace("-", ""));
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		//INSERT INTO ROLE_LOG
		//(S_SERIAL,S_ACCOUNT,N_HERO_NO,S_ROLE,S_IP,D_LOGINTIME,D_LOGOUTTIME, S_MAC,S_OS_TYPE, S_COMMENT)VALUES
		
		/*log.setHeroNo(heroInfo == null ? 0 : heroInfo.getHeroNo());
		log.setIp(ip);
		log.setLoginTime(loginTime);
		log.setLogoutTime(logoutTime);
		log.setMac(mac);
		log.setOsType(osType);
		log.setComment(comment);
		gameLogDAO.insertRoleLog(roleInfo, log);*/
		
		StringBuffer buffer = GameLogDAO.roleLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getLoginLogId());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(heroInfo == null ? 0 : heroInfo.getHeroNo());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(ip);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(loginTime);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(logoutTime);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(mac);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(osType);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(comment);
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.roleLogCount++;
			
			gameLogDAO.insertRoleLogin();
		}
	}
	
	/**
	 * 任务日志
	 * @param roleInfo
	 * @param info
	 * @param actType 承接:0，完成:1，放弃:2
	 * @return
	 */
	public static void insertTaskLog(RoleInfo roleInfo, QuestInProgressInfo info, int actType) {
		/*List<TaskLog> logs = new ArrayList<TaskLog>();
		TaskLog log = new TaskLog();
		log.setTaskId(ConfigType.task.getType()+"-"+info.getQuestProtoNo());
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setActType(actType);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		logs.add(log);*/
		
		
		//INSERT INTO TASK_LOG (S_TASK_ID,S_ACCOUNT,S_ROLE_NAME,N_ACT_TYPE,D_CREATE)
		StringBuffer buffer = GameLogDAO.taskLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(ConfigType.task.getType()+"-"+info.getQuestProtoNo());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(actType);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.taskLogCount++;
			
			gameLogDAO.insertTaskLog();
		}
		
	}

	/**
	 * 任务日志
	 * @param roleInfo
	 * @param info
	 * @param actType 承接:0，完成:1，放弃:2
	 * @return
	 */
	public static void insertTaskLog(RoleInfo roleInfo, List<QuestInProgressInfo> list, int actType) {
		StringBuffer buffer = GameLogDAO.taskLogBuffer;
		synchronized(buffer)
		{
			for (QuestInProgressInfo info : list) {
				/*TaskLog log = new TaskLog();
				log.setTaskId(ConfigType.task.getType()+"-"+info.getQuestProtoNo());
				log.setAccount(roleInfo.getAccount());
				log.setRoleName(roleInfo.getRoleName());
				log.setActType(actType);
				log.setCreateTime(new Timestamp(System.currentTimeMillis()));
				logs.add(log);*/
				
				
				//INSERT INTO TASK_LOG (S_TASK_ID,S_ACCOUNT,S_ROLE_NAME,N_ACT_TYPE,D_CREATE)
				buffer.append("(");
				
				buffer.append("'");
				buffer.append(ConfigType.task.getType()+"-"+info.getQuestProtoNo());
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(roleInfo.getAccount());
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(roleInfo.getRoleName().replace("\\", ""));
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append(actType);
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(new Timestamp(System.currentTimeMillis()));
				buffer.append("'");
				
				buffer.append("),");
				
				GameLogDAO.taskLogCount++;
			}
			gameLogDAO.insertTaskLog();
		}
	}

	/**
	 * 用户资源变动日志
	 * @param roleInfo
	 * @param gameAction
	 * @param type money银子 gold金子 sp体力 soul战魂 tech科技点 exp角色经验
	 * @param eventType 获得0,失去1
	 * @param before
	 * @param after
	 * @param drop 消耗资源未涉及到购买物品，则置空
	 * @return
	 */
	public static void insertMoneyLog(RoleInfo roleInfo, int gameAction, ConditionType type, int eventType, int before,
			int after,DropInfo drop) {
		int moneyType = 0;
		if (AbstractConditionCheck.isResourceType(type.getName())) {
			moneyType = type.getType();
		} else {
			logger.error("it is not exit resource:" + type.getName());
			return;
		}

		/*MoneyLog info = new MoneyLog();
		info.setAccount(roleInfo.getAccount());
		info.setRoleName(roleInfo.getRoleName());
		info.setRoleName(info.getRoleName().replace("\\", ""));
		info.setCreateTime(new Timestamp(System.currentTimeMillis()));
		info.setMoneyType(moneyType);
		info.setEventId(String.valueOf(gameAction));
		info.setEventType(eventType);
		info.setMoney(Math.abs((int) after - (int) before));
		info.setRecvAccount("");
		info.setRecvRoleName("");
		info.setSceneId("");
		info.setGuildId("");
		info.setState("1");// 异动状态 失败:0，成功:1
		info.setRoleLevel(HeroInfoMap.getMainHeroLv(roleInfo.getId()));
		info.setTotalLogtime(0);
		info.setBefore(before);
		info.setAfter(after);
		info.setTradeId(0);
		info.setTradeFlag("0");// 是否交易 不是:0，是:1
		info.setComment("");
		if(drop==null){
			info.setItemId("");
			info.setCount(0);
		}else{
			info.setItemId(drop.getItemNo());
			info.setCount(drop.getItemNum());
		}*/
		
		StringBuffer buffer = GameLogDAO.moneyLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(moneyType);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(String.valueOf(gameAction));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(eventType);			
			buffer.append(",");
			
			if(drop==null){
				buffer.append("'");
				buffer.append("");
				buffer.append("'");
				buffer.append(",");
				
				buffer.append(0);
				buffer.append(",");
			}else{
				buffer.append("'");
				buffer.append(drop.getItemNo());
				buffer.append("'");
				buffer.append(",");
				
				buffer.append(drop.getItemNum());
				buffer.append(",");
			}
			
			buffer.append(Math.abs((int) after - (int) before));			
			buffer.append(",");

			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("1");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(HeroInfoMap.getMainHeroLv(roleInfo.getId()));			
			buffer.append(",");
			
			buffer.append(0);
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			buffer.append(",");
			
			buffer.append(0);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("0");
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.moneyLogCount++;
			
			gameLogDAO.insertMoneyLog();
		}
	}

	/**
	 * 用户背包变动日志
	 * @param roleInfo
	 * @param gameAction
	 * @param eventType 获得0,失去1
	 * @param itemNo
	 * @param before
	 * @param after
	 * @return
	 */
	public static void insertItemLog(RoleInfo roleInfo, int gameAction, int eventType, int itemNo, int before, int after) {
		/*ItemLog log = new ItemLog();
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setRoleName(log.getRoleName().replace("\\", ""));
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		log.setEventId(gameAction + "");
		log.setEventType(eventType);
		log.setItemId(String.valueOf(itemNo));
		log.setCount(Math.abs(after - before));
		log.setRecvAccount("");
		log.setRecvRoleName("");
		log.setSceneId("");
		log.setGuildId("");
		log.setState("1");
		log.setBefore(before);
		log.setAfter(after);
		log.setTradeId(0);
		log.setTradeFlag("0");// 是否交易 不是:0，是:1
		log.setComment("");*/
		
		StringBuffer buffer = GameLogDAO.itemLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(gameAction);			
			buffer.append(",");
			
			buffer.append(eventType);		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(itemNo);
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(Math.abs(after - before));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(1);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			buffer.append(",");
			
			buffer.append(0);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(0);
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.itemLogCount++;
			gameLogDAO.insertItemLog();
		}
	}

	/**
	 * 玩家升级日志表
	 * @param roleInfo
	 * @param logType
	 * @param before
	 * @param after
	 * @param skillId
	 */
	public static void insertRoleUpgradeLog(RoleInfo roleInfo, int logType, int before, int after, int skillId) {
		/*RoleUpgradeLog log = new RoleUpgradeLog();
		String ser = UUID.randomUUID().toString();
		log.setSerial(ser.replace("-", ""));
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setLogType(logType);
		log.setBefore(before);
		log.setAfter(after);
		log.setSkillId(skillId);*/
		
		//S_SERIAL,D_TIME,S_ACCOUNT,S_ROLE,N_LOGTYPE,N_LEVEL_BEFORE,N_LEVEL_AFTER,S_SKILL_ID
		
		StringBuffer buffer = GameLogDAO.roleUpLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(UUID.randomUUID().toString().replace("-", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(logType);			
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			buffer.append(",");
			
			buffer.append(skillId);
			
			buffer.append("),");
			
			GameLogDAO.roleUpLogCount++;
			
			gameLogDAO.insertRoleUpgradeLog();
		}
	}

	/**
	 * 副本日志
	 * @param roleId
	 * @param instanceId 副本实例的ID (fightId)
	 * @param instanceTypeId 副本id (fightType+"+"+defendStr)
	 * @param startTime 实例创建时间
	 * @param endTime 实例结束时间
	 * @param troopCount1 副本创建时组队人数
	 * @param troopCount2 副本完成时组队人数
	 */
	public static void insertInstanceLog(int roleId, FightInfo fightInfo) {
		/*long now = System.currentTimeMillis();
		InstanceLog instanceLog = new InstanceLog();
		instanceLog.setSerial(UUID.randomUUID().toString().replace("-", ""));
		instanceLog.setRoleId(roleId);
		instanceLog.setInstanceId(String.valueOf(fightInfo.getFightId()));
		instanceLog.setInstanceTypeId(fightInfo.getInstanceTypeId());
		instanceLog.setStartTime(new Timestamp(fightInfo.getFightTime()));
		instanceLog.setEndTime(new Timestamp(System.currentTimeMillis()));
		instanceLog.setDuration((int) (now - fightInfo.getFightTime()) / 1000);*/
		int troopCount1 = 0;
		if (fightInfo.getFightDataList() != null) {
			for (FightSideData fightSide : fightInfo.getFightDataList()) {
				if (fightSide.getSideId() == 0) {
					for (FightArmyDataInfo army : fightSide.getArmyInfos()) {
						if (army.getCurrHp() > 0) {
							troopCount1++;
						}
					}
					for (FightArmyDataInfo army : fightSide.getAddArmyInfos()) {
						if (army.getCurrHp() > 0) {
							troopCount1++;
						}
					}
				}
			}
		}

		//instanceLog.setTroopCount1(troopCount1);

		int troopCount2 = 0;
		if (fightInfo.getArmyFightingInfos() != null) {
			for (ArmyFightingInfo army : fightInfo.getArmyFightingInfos()) {
				if (army.getSideId() == 0 && army.getCurrentHp() > 0) {
					troopCount2++;
				}
			}
		}
		//instanceLog.setTroopCount2(troopCount2);
		
		
		
		//INSERT INTO INSTANCE_LOG 
		//(S_SERIAL,N_ROLE_ID,S_INSTANCEID,S_INSTANCETYPEID,D_STARTTIME,D_ENDTIME,N_DURATION,N_TROOP_COUNT1,N_TROOP_COUNT2) 
		StringBuffer buffer = GameLogDAO.instanceLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(UUID.randomUUID().toString().replace("-", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(roleId);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(String.valueOf(fightInfo.getFightId()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(fightInfo.getInstanceTypeId());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(fightInfo.getFightTime()));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append((int) (System.currentTimeMillis() - fightInfo.getFightTime()) / 1000);
			buffer.append(",");
			
			buffer.append(troopCount1);
			buffer.append(",");
			
			buffer.append(troopCount2);
			
			buffer.append("),");
			
			GameLogDAO.instanceLogCount++;
			
			gameLogDAO.insertInstanceLog();
		}
	}

	/**
	 * 记录惩罚玩家日志
	 * @param roleInfo
	 * @param punishType 0-解禁 1-禁言 2-冻结
	 * @param minutes 被禁言或冻结的分钟数
	 * @param operator
	 */
	public static void insertPunishLog(RoleInfo roleInfo, byte punishType, int minutes, String operator) {
		/*PunishLog log = new PunishLog();
		String ser = UUID.randomUUID().toString();
		log.setSerial(ser.replace("-", ""));
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		log.setOperator(operator);
		log.setRoleName(roleInfo.getRoleName());
		log.setAccount(roleInfo.getAccount());
		log.setMinutes(minutes);
		log.setPunishType(punishType);*/
		
		
		
		//INSERT INTO PUNISH_LOG(S_SERIAL,D_TIME,S_OPERATOR,S_ROLE,S_ACCOUNT,N_MINUTES,N_PUNISH_TYPE)
		StringBuffer buffer = GameLogDAO.punishLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(UUID.randomUUID().toString().replace("-", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(operator);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(minutes);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(punishType);
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.punishLogCount++;
			
			gameLogDAO.insertPunishLog();
		}
	}

	/**
	 * 英雄觉醒动日志
	 * @param roleInfo
	 * @param heroId
	 * @param gameAction
	 * @param upType 变动类型 0-获得 1-等级 2-星级 3-品阶 4-亲密度
	 * @param before
	 * @param after
	 * @return
	 */
	public static void insertHeroUpLog(RoleInfo roleInfo,int heroNo, int gameAction, int upType, int before,
			int after) {
		/*HeroUpLog log = new HeroUpLog();
		log.setRoleId(roleInfo.getId());
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setHeroNo(heroNo);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setEventId(gameAction + "");
		log.setUpType(upType);// 1-等级 2-星级
		log.setBefore(before);
		log.setAfter(after);*/
		
		
		//INSERT INTO GAME_HERO_UP_LOG(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_NO,	D_TIME,S_EVENT_ID,N_UP_TYPE,N_BEFORE,N_AFTER)
		
		StringBuffer buffer = GameLogDAO.heroUpLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(heroNo);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(gameAction);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(upType);
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			
			buffer.append("),");
			
			GameLogDAO.heroUpLogCount++;
			
			gameLogDAO.insertHeroUpLog();
		}
	}
	
	/**
	 * 神兵升级日志
	 * @param roleInfo
	 * @param heroId
	 * @param gameAction
	 * @param upType 0-神兵升级
	 * @param before
	 * @param after
	 * @return
	 */
	public static void insertWeaponUpLog(RoleInfo roleInfo, int gameAction, int upType, int before,
			int after, int weaponId, int weaponNo) {
		/*WeaponUpLog log = new WeaponUpLog();
		log.setRoleId(roleInfo.getId());
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setWeaponId(weaponId);
		log.setWeaponNo(weaponNo);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setEventId(gameAction + "");
		log.setUpType(upType);
		log.setBefore(before);
		log.setAfter(after);*/
		
		//INSERT INTO GAME_WEAPON_UP_LOG(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME,N_WEAPON_ID,N_WEAPON_NO,D_TIME,S_EVENT,N_UP_TYPE,N_BEFORE,N_AFTER)
		StringBuffer buffer = GameLogDAO.weapUpLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(weaponId);			
			buffer.append(",");
			
			buffer.append(weaponNo);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(gameAction);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(upType);
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			
			buffer.append("),");
			
			GameLogDAO.weapUpLogCount++;
			
			
			gameLogDAO.insertWeaponUpLog();
		}
	}

	/**
	 * 英雄装备变动日志
	 * @param roleInfo
	 * @param bagItem
	 * @param gameAction
	 * @param eventType 获得0,失去1
	 * @return
	 */
	public static void insertEquipInfoLog(RoleInfo roleInfo, EquipInfo heroEquipInfo, int gameAction, int eventType) {
		if (heroEquipInfo != null) {
			/*EquipInfoLog log = new EquipInfoLog();
			log.setRoleId(roleInfo.getId());
			log.setAccount(roleInfo.getAccount());
			log.setRoleName(roleInfo.getRoleName());
			log.setRoleName(log.getRoleName().replace("\\", ""));
			// log.setHeroId(heroEquipInfo.getHeroId());
			log.setItemId(heroEquipInfo.getId());
			log.setItemNo(heroEquipInfo.getEquipNo());
			log.setItemLevel(heroEquipInfo.getLevel());
			log.setItemStar(0);
			log.setExp(0);
			log.setColour(0);
			log.setTime(new Timestamp(System.currentTimeMillis()));
			log.setEventId(String.valueOf(gameAction));
			log.setEventType(eventType);
			log.setState("1");
			log.setComment("");*/
			
			
			//INSERT INTO GAME_EQUIP_INFO_LOG
			//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_ID,N_ITEM_ID,N_ITEM_NO,N_ITEM_LEVEL, N_ITEM_STAR,
			//N_EXP,N_COLOUR,D_TIME,S_EVENT_ID,N_EVENT_TYPE,S_STATE,S_COMMENT)
			StringBuffer buffer = GameLogDAO.equipInfoLogBuffer;
			synchronized(buffer)
			{
				buffer.append("(");
				
				buffer.append(roleInfo.getId());
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(roleInfo.getAccount());
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(roleInfo.getRoleName().replace("\\", ""));
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append(0);		
				buffer.append(",");
				
				buffer.append(heroEquipInfo.getId());		
				buffer.append(",");
				
				buffer.append(heroEquipInfo.getEquipNo());
				buffer.append(",");
				
				buffer.append(heroEquipInfo.getLevel());
				buffer.append(",");
				
				buffer.append(0);
				buffer.append(",");
				
				buffer.append(0);
				buffer.append(",");
				
				buffer.append(0);
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(new Timestamp(System.currentTimeMillis()));
				buffer.append("'");
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(gameAction);
				buffer.append("'");
				buffer.append(",");
				
				buffer.append(eventType);
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(1);
				buffer.append("'");
				buffer.append(",");
				
				buffer.append("'");
				buffer.append("");
				buffer.append("'");
				
				buffer.append("),");
				
				GameLogDAO.equipInfoLogCount++;
				
				gameLogDAO.insertEquipInfoLog();
			}
		}
	}

	/**
	 * 装备升级日志
	 * @param roleInfo
	 * @param gameAction
	 * @param heroId
	 * @param equipId
	 * @param equipNo
	 * @param before
	 * @param after
	 * @return
	 */
	public static void insertEquipUpLog(RoleInfo roleInfo, int gameAction, long heroId, long equipId, int equipNo,
			int before, int after) {
		/*EquipUpLog log = new EquipUpLog();
		log.setRoleId(roleInfo.getId());
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setHeroId(heroId);
		log.setItemId(equipId);
		log.setEquipNo(equipNo);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setEventId(gameAction + "");
		log.setBefore(before);
		log.setAfter(after);*/
		
		//INSERT INTO GAME_EQUIP_UP_LOG(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_ID,N_EQUIP_ID,N_EQUIP_NO,D_TIME,S_EVENT,N_BEFORE,N_AFTER)
		StringBuffer buffer = GameLogDAO.equipUpLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(heroId);			
			buffer.append(",");
			
			buffer.append(equipId);
			buffer.append(",");
			
			buffer.append(equipNo);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(gameAction);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			
			buffer.append("),");
			
			GameLogDAO.equipUpLogCount++;
			
			gameLogDAO.insertEquipUpLog();
		}
	}

	/**
	 * 记录极效日志
	 * @param method
	 * @param guid
	 * @param targetRoleName
	 * @param targetRoleAcc
	 * @param xmlIdStr
	 * @param msgContent
	 * @param operator
	 * @param successful
	 * @return
	 */
	public static void insertToolOperatLog(String method, String guid, String targetRoleName, String targetRoleAcc,
			String xmlIdStr, String msgContent, String operator, String successful) {

		/*ToolOperateLog log = new ToolOperateLog();

		log.setMsgType(method);
		log.setGuid(guid);
		log.setTargetRoleName(targetRoleName);
		log.setTargetRoleAcc(targetRoleAcc);
		log.setXmlIdStr(xmlIdStr);
		log.setMsgContent(msgContent);
		log.setOperator(operator);
		log.setReserve("");
		log.setOperateTime(new Timestamp(System.currentTimeMillis()));
		log.setSuccess("1".equals(successful));*/

		
		
		
		//INSERT INTOGAME_TOOL_OPERATE_LOG
		//(S_MESSAGE_TYPE, S_GUID, S_TARGET_ROLE_NAME, S_TARGET_ROLE_ACCOUNT,S_XML_NAME_STR, S_MESSAGE_CONTENT, S_OPERATOR_ACCOUNT, 
		//D_OPERATE_TIME, N_IS_SUCC, S_RESERVE)
		StringBuffer buffer = GameLogDAO.toolOperateLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(method);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(guid);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(targetRoleName.replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(targetRoleAcc);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(xmlIdStr);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			logger.warn("method="+method+",guid="+guid+",msgContent="+msgContent);
			
			buffer.append("'");
			buffer.append(operator);
			buffer.append("'");
			buffer.append(",");

			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("1".equals(successful));
			buffer.append(",");
			
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.toolOperateLogCount++;
			
			gameLogDAO.insertGameToolOperateLog();
		}
		
	}

	/**
	 * 保存邮件日志
	 * @param list
	 * @param type 0-领取附件 1-发送附件 2-删除邮件
	 * @return
	 */
	public static void saveMailInfoLog(List<MailInfo> list, int type) {
		if(list == null){
			return;
		}
		
		//List<MailInfoLog> logs = new ArrayList<MailInfoLog>();
		//MailInfoLog log = null;
		StringBuffer buffer = GameLogDAO.mailLogBuffer;
		synchronized(buffer)
		{
			for (MailInfo info : list) {
				/*log = new MailInfoLog();
				log.setMailId(info.getId());
				log.setSendRoleName(info.getSendRoleName());
				log.setReceiveRoleName(info.getReceiveRoleName());
				log.setTime(new Timestamp(System.currentTimeMillis()));
				log.setActType(0);
				log.setAttachment(info.getAttachmentStr());
				log.setRecAcc(info.getRecAcc());
				log.setRecId(info.getReceiveRoleId());
				logs.add(log);*/
				
				//INSERT INTO GAME_MAIL_LOG(N_ID,S_SENDNAME,S_RECEIVENAME,D_DEAL_TIME,N_TYPE,S_ATTACHMENT,S_REC_ACC)
				
				
				buffer.append("(");
				
				buffer.append(info.getId());
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getSendRoleName().replace("\\", ""));
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getReceiveRoleName().replace("\\", ""));
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(new Timestamp(System.currentTimeMillis()));
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append(0);			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getAttachmentStr());
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getRecAcc());
				buffer.append("'");
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getMailTitle());
				buffer.append("'");
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getMailContent());
				buffer.append("'");
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(info.getReserve());
				buffer.append("'");
				
				buffer.append("),");
				
				GameLogDAO.mailLogCount++;
			}
			gameLogDAO.insertMailInfoLog();
		}
		
	}

	/**
	 * 记录在线用户日志
	 * @param nAmount
	 * @param time
	 */
	public static void addStatOnline(int nAmount, long time) {
		StatLog statOnlineLog = new StatLog();
		statOnlineLog.setnAmount(nAmount);
		statOnlineLog.setTimestamp(new Timestamp(time));
		gameLogDAO.addStatOnline(statOnlineLog);
	}

	/**
	 * 记录注册用户日志
	 * @param nAmount
	 * @param time
	 */
	public static void addStatNewAccount(int nAmount, long time) {

		StatLog statOnlineLog = new StatLog();
		statOnlineLog.setnAmount(nAmount);
		statOnlineLog.setTimestamp(new Timestamp(time));
		gameLogDAO.addStatNewAccount(statOnlineLog);
	}

	/**
	 * 记录开卡用户日志
	 * @param nAmount
	 * @param time
	 */
	public static void addStatNewCard(int nAmount, long time) {

		StatLog statOnlineLog = new StatLog();
		statOnlineLog.setnAmount(nAmount);
		statOnlineLog.setTimestamp(new Timestamp(time));
		gameLogDAO.addStatNewCard(statOnlineLog);
	}

	
	/**
	 * 记录角色名变动日志
	 * @param roleInfo
	 * @param before
	 * @param after
	 */
	public static void insertRoleNameLog(RoleInfo roleInfo, String before, String after) {
		//INSERT INTO GAME_ROLE_NAME_LOG
		//(N_ROLE_ID,S_U_ID,S_ACCOUNT, S_BEFORE,S_AFTER, D_CREATE)VALUES
		
		/*RoleNameLog log = new RoleNameLog();
		log.setRoleId(roleInfo.getId());
		log.setAccount(roleInfo.getAccount());
		log.setUid(roleInfo.getUid());
		log.setBefore(before);
		log.setAfter(after);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));*/
		
		StringBuffer buffer = GameLogDAO.roleNameLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getUid());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(before.replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(after.replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.roleNameLogCount++;
			
			gameLogDAO.insertRoleNameLog();
		}
	}
	

	/**
	 * 商店物品购买日志
	 * @param roleInfo
	 * @param itemNo
	 * @param itemNum
	 * @param storeType
	 */
	public static void insertStoreBuyItemLog(RoleInfo roleInfo,byte storeType,int itemNo,int itemNum,String sourceType,int sourceNum) {
		/*StoreBuyItemLog log = new StoreBuyItemLog();
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setRoleId(roleInfo.getId());
		log.setStoreType(storeType);
		log.setItemNo(itemNo);
		log.setItemNum(itemNum);
		log.setSourceType(sourceType);
		log.setSourceNum(sourceNum);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));*/
		
		
		//INSERT INTO GAME_STORE_BUY_ITEM_LOG(S_ACCOUNT,S_ROLE_NAME, N_ROLE_ID,N_SOTRE_TYPE,N_ITEM_NO,N_ITEM_NUM,S_SOURCE_TYPE,N_USED_SOURCE,D_CREATE)
		StringBuffer buffer = GameLogDAO.storeBugLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append(storeType);			
			buffer.append(",");
			
			buffer.append(itemNo);
			buffer.append(",");
			
			buffer.append(itemNum);
			buffer.append(",");
			
			buffer.append("'");	
			buffer.append(sourceType);
			buffer.append("'");	
			buffer.append(",");
			
			buffer.append(sourceNum);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.storeBugLogCount++;
			

			gameLogDAO.insertStoreBuyItemLog();
		}
		
	}
	
	/**
	 * 镖车日志
	 * @param roleInfo
	 * @param itemNo
	 * @param itemNum
	 * @param storeType
	 */
	public static void insertBiaocheLog(RoleInfo roleInfo,int action,int num,int getSilver,int lostSilver,int usedCoin,byte biaocheType,int result) {
		/*BiaocheLog log = new BiaocheLog();
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setRoleId(roleInfo.getId());
		log.setAction(action);
		log.setNum(num);
		log.setSilverChange(getSilver);
		log.setLostSilver(lostSilver);
		log.setUsedCoin(usedCoin);
		log.setBiaocheType(biaocheType);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setResult((byte) result);*/
		
		
		//INSERT INTO GAME_BIAO_CHE_LOG
		//(S_ACCOUNT,S_ROLE_NAME, N_ROLE_ID,N_ACTION,N_NUM,N_SILVER_CHANGE,D_CREATE,S_CONTENT,N_RESULT,N_LOST_SILVER,N_USED_COIN,N_BIAO_CHE_TYPE)
		StringBuffer buffer = GameLogDAO.biaocheLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append(action);			
			buffer.append(",");
			
			buffer.append(num);
			buffer.append(",");
			
			buffer.append(getSilver);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(result);
			buffer.append(",");
			
			buffer.append(lostSilver);
			buffer.append(",");
			
			buffer.append(usedCoin);
			buffer.append(",");
			
			buffer.append(biaocheType);
			buffer.append("),");
			
			GameLogDAO.biaocheLogCount++;

			gameLogDAO.insertBiaocheLog();
		}
		
	}
	
	/**
	 * 兵种升级日志
	 * @param roleInfo
	 * @param itemNo
	 * @param itemNum
	 * @param storeType
	 */
	public static void insertSoliderUpLog(RoleInfo roleInfo,byte soliderType,byte isUp,int beforeLv,int afterLv,int useMoney,int useItemNo,int useItemNum) {
		/*SoliderUpLog log = new SoliderUpLog();
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setRoleId(roleInfo.getId());
		log.setSoliderType(soliderType);
		log.setIsUp(isUp);
		log.setBeforeLv(beforeLv);
		log.setAtterLv(afterLv);
		log.setUseMoney(useMoney);
		log.setUseItemNo(useItemNo);
		log.setUseItemNum(useItemNum);
		log.setTime(new Timestamp(System.currentTimeMillis()));*/
		
		
		//INSERT INTO GAME_SOLDIER_UP_LOG
		//(S_ACCOUNT,S_ROLE_NAME, N_ROLE_ID,N_SOLDIER_TYPE,N_IS_UP,N_BEFORE_LV,N_AFTER_LV,D_CREATE,S_CONTENT,N_USE_MONEY,N_USE_ITEM_NO,N_USE_ITEM_NUM)
		StringBuffer buffer = GameLogDAO.soliderUpLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append(soliderType);			
			buffer.append(",");
			
			buffer.append(isUp);
			buffer.append(",");
			
			buffer.append(beforeLv);
			buffer.append(",");
			
			buffer.append(afterLv);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(useMoney);
			buffer.append(",");
			
			buffer.append(useItemNo);
			buffer.append(",");
			
			buffer.append(useItemNum);
			
			buffer.append("),");
			
			GameLogDAO.soliderUpLogCount++;

			gameLogDAO.insertSoliderUpLog();
		}
	}
	
	/**
	 * 记录技能升级
	 * @param roleInfo
	 * @param id
	 * @param gameHeroAction5
	 * @param i
	 * @param currLv
	 * @param nextLv
	 */
	public static void insertHeroSkillUpLog(RoleInfo roleInfo, int heroNo, int skillNo, int action, int currLv,
			int nextLv) {
		/*HeroSkillUpLog log = new HeroSkillUpLog();
		log.setRoleId(roleInfo.getId());
		log.setRoleName(roleInfo.getRoleName());
		log.setAccount(roleInfo.getAccount());
		log.setHeroNo(heroNo);
		log.setSkillNo(skillNo);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setEventId(action + "");
		log.setBefore(currLv);
		log.setAfter(nextLv);*/
		
		//INSERT INTO GAME_HERO_SKILL_UP_LOG
		//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_HERO_NO,	N_SKILL_NO, D_TIME, S_EVENT_ID, N_BEFORE, N_AFTER)
		StringBuffer buffer = GameLogDAO.heroSkillUpLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(heroNo);			
			buffer.append(",");
			
			buffer.append(skillNo);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(action);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(currLv);
			buffer.append(",");
			
			buffer.append(nextLv);
			
			buffer.append("),");
			
			
			GameLogDAO.heroSkillUpLogCount++;

			gameLogDAO.insertHeroSkillUpLog();
		}
		
	}
	
	/**
	 * 记录点金手日志
	 * @param log
	 */
	public static void insertGoldBuyLog(RoleInfo roleInfo,int action,ConditionType moneyType,int before,int after,int cost,int beforeNum,int afterNum){
		/*GoldBuyLog log = new GoldBuyLog();
		log.setRoleId(roleInfo.getId());
		log.setAccount(roleInfo.getAccount());
		log.setRoleName(roleInfo.getRoleName());
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setEventId(action + "");
		log.setMoneyType(moneyType.getType());
		log.setBefore(before);
		log.setAfter(after);
		log.setCost(cost);
		log.setBeforeNum(beforeNum);
		log.setAfterNum(afterNum);*/
		
		
		//INSERT INTO GAME_GOLD_BUY_LOG
		//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, D_TIME, S_EVENT_ID,N_MONEY_TYPE,N_BEFORE, N_AFTER,N_COST,N_BEFORE_NUM, N_AFTER_NUM)
		StringBuffer buffer = GameLogDAO.goldBuyLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(action);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(moneyType.getType());			
			buffer.append(",");
			
			buffer.append(before);
			buffer.append(",");
			
			buffer.append(after);
			buffer.append(",");
			
			buffer.append(cost);
			buffer.append(",");
			
			buffer.append(beforeNum);
			buffer.append(",");
			
			buffer.append(afterNum);
			
			buffer.append("),");
			
			GameLogDAO.goldBuyLogCount++;
			
			gameLogDAO.insertGoldBuyLog();
		}
		
	}
	/**
	 * 好友关系
	 * @param roleInfo
	 * @param relRoleId
	 * @param relation
	 * @param time
	 */
	public static void insertRoleRelationLog(RoleInfo roleInfo, int relRoleId, int relation, long time) {
		/*RoleRelationLog to = new RoleRelationLog();
		to.setAccount(roleInfo.getAccount());
		to.setRoleId(roleInfo.getId());
		to.setRoleName(roleInfo.getRoleName());
		to.setRelRoleId(relRoleId);
		to.setTime(new Timestamp(time));
		to.setRelation(relation);*/
		
		
		
		//INSERT INTO GAME_ROLE_RELATION_LOG
		//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_REL_ROLE_ID, N_RELATION,D_TIME)
		StringBuffer buffer = GameLogDAO.roleRelationLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(relRoleId);			
			buffer.append(",");
			
			buffer.append(relation);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.roleRelationLogCount++;
			
			gameLogDAO.insertRoleRelationLog();
		}
		
	}
	
	/**
	 * 公会日志
	 * @param roleInfo
	 * @param clubId
	 * @param type
	 * @param time
	 */
	public static void insertRoleClubLog(RoleInfo roleInfo, int clubId, int type) {
		/*RoleClubLog logTo = new RoleClubLog();
		logTo.setRoleId(roleInfo.getId());
		logTo.setAccount(roleInfo.getAccount());
		logTo.setRoleName(roleInfo.getRoleName());
		logTo.setClubId(clubId);
		logTo.setType(type);
		logTo.setTime(new Timestamp(System.currentTimeMillis()));*/
		//INSERT INTO GAME_ROLE_CLUB_LOG
		//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_CLUB_ID, N_TYPE, D_TIME)
		StringBuffer buffer = GameLogDAO.roleClubLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(clubId);			
			buffer.append(",");
			
			buffer.append(type);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.roleClubLogCount++;
			
			gameLogDAO.insertRoleClubLog();
		}
	}
	
	/**
	 * 装备熔炼
	 * @param roleInfo
	 * @param clubId
	 * @param type
	 * @param time
	 */
	public static void insertEquipResolveLog(RoleInfo roleInfo, String resolveEquips, String addItems, int type, long time) {
		/*EquipResolveLog to = new EquipResolveLog();
		to.setRoleId(roleInfo.getId());
		to.setAccount(roleInfo.getAccount());
		to.setRoleName(roleInfo.getRoleName());
		to.setResolveEquips(resolveEquips);
		to.setAddItems(addItems);
		to.setType(type);
		to.setTime(new Timestamp(time));*/
		
		
		//INSERT INTO GAME_EQUIP_RESOLVE_LOG
		//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, S_RESOLVE_EQUIPS, S_ADD_ITEMS, N_TYPE, D_TIME)
		StringBuffer buffer = GameLogDAO.equipResolveLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(resolveEquips);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(addItems);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(type);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.equipResolveLogCount++;
			
			gameLogDAO.insertEquipResolveLog();
		}
	}
	
	/**
	 *练兵场日志
	 * @param roleId
	 * @param activityTypeId  练兵场No
	 * @param startTime 实例创建时间
	 * @param endTime 实例结束时间
	 * @param pos 布阵
	 * @param drop 掉落
	 */
	public static void insertActivityLog(RoleInfo roleInfo, FightInfo fightInfo, List<BattlePrize> dropList) 
	{
		/*ActivityLog activityLog = new ActivityLog();
		activityLog.setAccount(roleInfo.getAccount());
		activityLog.setRoleName(roleInfo.getRoleName());
		activityLog.setRoleId(roleInfo.getId());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo != null)
		{
			activityLog.setHeroNo(heroInfo.getHeroNo());
		}
		activityLog.setActivityTypeId(fightInfo.getDefendStr());
		activityLog.setStartTime(new Timestamp(fightInfo.getFightTime()));
		activityLog.setEndTime(new Timestamp(System.currentTimeMillis()));*/
		String pos = "";
		List<StartFightPosInfo> chgposList = fightInfo.getChgPosInfos();
		if(chgposList != null && chgposList.size() > 0)
		{
			for(StartFightPosInfo info : chgposList)
			{
				HeroInfo heroInfo1 = HeroInfoMap.getHeroInfo(roleInfo.getId(), info.getHeroId());
				if(heroInfo1 == null || heroInfo1.getDeployStatus() == 1)
				{
					continue;
				}
				
				pos = pos + info.getDeployPos() + "," + heroInfo1.getHeroNo()+ ";";
			}
		}
		/*if(pos != null && pos.length() > 0)
		{
			activityLog.setPos(pos.substring(0, pos.length()-1));
		}*/
		
		String drop = "";
		if(dropList != null && dropList.size() > 0)
		{
			for(BattlePrize prize : dropList)
			{
				drop = drop + prize.getNo() + "," + prize.getNum() +";";
			}
		}
		/*if(drop != null && drop.length() > 0)
		{
			activityLog.setDrop(drop.substring(0, drop.length()-1));
		}*/
		

		
		
		//INSERT INTO ACTIVITY_LOG 
		//(S_ACCOUNT,S_NAME,N_HERO_NO,N_ROLE_ID,S_ACTIVITY,D_STARTTIME,D_ENDTIME,S_POS,S_DROP) 
		StringBuffer buffer = GameLogDAO.activeLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			HeroInfo heroInfo1 = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo1 != null)
			{
				buffer.append(heroInfo1.getHeroNo());			
				buffer.append(",");
			}
			else
			{
				buffer.append(0);			
				buffer.append(",");
			}
			
			
			buffer.append(roleInfo.getId());			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(fightInfo.getDefendStr());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(fightInfo.getFightTime()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			if(pos != null && pos.length() > 0)
			{
				buffer.append("'");
				buffer.append(pos.substring(0, pos.length()-1));
				buffer.append("'");			
				buffer.append(",");
			}
			else
			{
				buffer.append("'");
				buffer.append("");
				buffer.append("'");			
				buffer.append(",");
			}
			
			if(drop != null && drop.length() > 0)
			{
				buffer.append("'");
				buffer.append(drop.substring(0, drop.length()-1));
				buffer.append("'");
			}
			else
			{
				buffer.append("'");
				buffer.append("");
				buffer.append("'");	
			}
			
			buffer.append("),");
			
			GameLogDAO.activeLogCount++;
			
			gameLogDAO.insertActivityLog();
		}
		
	}
	
	
	/**
	 * 你争我夺日志
	 * @param to
	 */
	public static void insertSnatchLog(RoleInfo roleInfo, int defendRoleId, String defendRoleName, int stoneNo,
			byte lootSuccess, int lootTimes, String getItem, int useEnergy) {
		/*SnatchLog to = new SnatchLog();
		to.setRoleId(roleInfo.getId());
		to.setAccount(roleInfo.getAccount());
		to.setRoleName(roleInfo.getRoleName());
		to.setRoleName(to.getRoleName().replace("\\", ""));

		to.setDefendRoleId(defendRoleId);
		to.setDefendRoleName(defendRoleName);
		to.setDefendRoleName(to.getDefendRoleName().replace("\\", ""));

		to.setStoneNo(stoneNo);
		to.setLootSuccess(lootSuccess);
		to.setLootTimes(lootTimes);

		to.setGetItem(getItem);
		to.setUseEnergy(useEnergy);
		
		to.setComment("");
		to.setCreateTime(new Timestamp(System.currentTimeMillis()));*/
		
		
		//INSERT INTO GAME_SNATCH_LOG
		//(S_ACCOUNT, S_ROLE_NAME, N_ROLE_ID,S_DEFEND_NAME,N_DEFEND_ID,N_STONE_NO,N_LOOT_SUCCESS,N_LOOT_TIMES,S_GET_ITEM,N_USE_ENGER,S_CONTENT,D_TIME)
		StringBuffer buffer = GameLogDAO.snatchLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(roleInfo.getId());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(defendRoleName.replace("\\", ""));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(defendRoleId);
			buffer.append(",");
			
			buffer.append(stoneNo);
			buffer.append(",");
			
			buffer.append(lootSuccess);
			buffer.append(",");
			
			buffer.append(lootTimes);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(getItem);
			buffer.append("'");		
			buffer.append(",");
			
			buffer.append(useEnergy);		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append("");
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");		
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.snatchLogCount++;

			GameLogDAO.getInstance().insertSnatchLog();
		}
		
	}
	
	/**
	 * 矿收益奖励
	 * @param roleInfo
	 * @param action
	 * @param prizeList
	 */
	public static void insertMineGetLog(RoleInfo roleInfo, int action, List<BattlePrize> prizeList) {
		//MineGetLog log = null;
		for (BattlePrize prize : prizeList) {
			/*log = new MineGetLog();
			log.setAccount(roleInfo.getAccount());
			log.setRoleName(roleInfo.getRoleName());
			log.setEventId(action + "");
			log.setItemNo(prize.getNo());
			log.setItemNum(prize.getNum());
			log.setState((byte) 1);
			log.setTime(new Timestamp(System.currentTimeMillis()));*/
			
			
			//INSERT INTO MINE_GET_LOG 
			//(S_ACCOUNT,S_ROLE_NAME,S_EVENT_ID,S_ITEM_NO,N_ITEM_NUM,N_STATE,D_TIME) 
			StringBuffer buffer = GameLogDAO.mineGetLogBuffer;
			synchronized(buffer){
				buffer.append("(");
				
				buffer.append("'");
				buffer.append(roleInfo.getAccount());
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(roleInfo.getRoleName().replace("\\", ""));
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(action);
				buffer.append("'");			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(prize.getNo());
				buffer.append("'");
				buffer.append(",");
				
				buffer.append(prize.getNum());			
				buffer.append(",");
				
				buffer.append(1);			
				buffer.append(",");
				
				buffer.append("'");
				buffer.append(new Timestamp(System.currentTimeMillis()));
				buffer.append("'");
				
				buffer.append("),");
				
				GameLogDAO.mineGetLogCount++;
				
				gameLogDAO.insertMineGetLog();
			}
			
		}
	}
	
	/**
	 * 充值日志
	 * @param to
	 */
	public static void insertRoleChargeLog(String roleAcc, String roleName, String orderId, int chargeType, int chargeEvent) {
		/*RoleChargeLog to = new RoleChargeLog();
		to.setRoleAcc(roleAcc);
		to.setRoleName(roleName);
		to.setOrderId(orderId);
		to.setChargeType(chargeType);
		to.setChargeEvent(chargeEvent);
		to.setCreateTime(new Timestamp(System.currentTimeMillis()));*/

		
		//INSERT INTO ROLE_CHARGE_LOG 
		//(S_ROLE_ACC,S_ROLE_NAME,S_ORDER_ID,N_CHARGE_TYPE,N_CHARGE_EVENT,D_CREATE_TIME) 
		StringBuffer buffer = GameLogDAO.roleChargeLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleAcc);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleName.replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(orderId);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(chargeType);			
			buffer.append(",");
			
			buffer.append(chargeEvent);			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			
			buffer.append("),");
			
			GameLogDAO.roleChargeLogCount++;
			
			gameLogDAO.insertRoleChargeLog();
		}
		
	}
	
	/**
	 * 副本日志
	 * @param to
	 */
	public static void insertChallengeLog(ChallengeLog log) {
		//INSERT INTO CHALLENGE_LOG	(N_ROLE_ID,S_ROLE_NAME,S_ACCOUNT, N_ACT_TYPE, S_CHALLENGE_NO, D_TIME, N_STAR,D_START_TIME)
		StringBuffer buffer = GameLogDAO.challengeLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getAction());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getChallengeNO());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getTime());
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(log.getStar());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getStartTime());
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.challengeLogCount++;
			
			gameLogDAO.insertChallengeLog();
		}
		
	}
	
	/**
	 * 副本异常日志
	 * @param roleId
	 * @param roleName
	 * @param account
	 * @param challengeNo
	 * @param startTime
	 * @param endTime
	 * @param star  1-检验次数异常(20%异常) 2-未发检验次数（外挂可能屏蔽或秒结算） 3-副本时间异常  4-副本,世界BOSS属性被篡改
	 * @param comment
	 */
	public static void insertChallengeUnusualLog(int roleId,String roleName,String account,String challengeNo,long startTime,long endTime,int star,String comment) {
		//INSERT INTO CHALLENGE_UNUSUAL_LOG	(N_ROLE_ID,S_ROLE_NAME,S_ACCOUNT, S_CHALLENGE_NO,D_START_TIME, D_TIME, N_STAR,S_COMMENT)
		StringBuffer buffer = GameLogDAO.challengeUnusualLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append(roleId);		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleName.replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(account);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(challengeNo);
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(startTime));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(endTime));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(star);
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(comment);
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.challengeUnusualLogCount++;
			
			gameLogDAO.insertChallengeUnusualLog();
		}
	}
	
	/**
	 * 狭路相逢
	 * @param to
	 */
	public static void insertPVELog(GamePVELog log) {
		//INSERT INTO GAME_PVE_LOG
		//(N_ROLE_ID,S_ACCOUNT, S_ROLE_NAME, N_DEFENSE_ID,S_DEFENSE_NAME,S_REWARD,D_START_TIME,D_ENDTIME,N_BATTLE_RESULT)
		StringBuffer buffer = GameLogDAO.pveLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getDefenseId());		
			buffer.append(",");
			
			buffer.append("'");
			if(log.getDefenseName() != null){
				buffer.append(log.getDefenseName().replace("\\", ""));
			}
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getReward());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getStartTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getEndTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getFightResult());
			
			buffer.append("),");
			
			GameLogDAO.pveLogCount++;
			
			gameLogDAO.insertGamePVELog();
		}
		
	}
	
	/**
	 * 竞技场
	 * @param to
	 */
	public static void insertArenaLog(RoleArenaLog log) {
		//INSERT INTO GAME_ROLE_ARENA_LOG
		//(S_ACCOUNT, S_ROLE_NAME, N_ROLE_ID,S_DEFEND_NAME,N_DEFEND_ID,N_BATTLE_RESULT,
		// N_BEFORE_PLACE,N_AFTER_PLACE,S_GET_ITEM,N_USE_ENGER,S_CONTENT,D_TIME,
		// N_ROLE_MAIN_HERO,S_ROLE_HEROS,N_DEFEND_MAIN_HERO,S_DEFEND_HEROS)
		//		VALUES
		StringBuffer buffer = GameLogDAO.arenaLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getDefendRoleName() == null ? null : log.getDefendRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getDefendRoleId());		
			buffer.append(",");
			
			buffer.append(log.getBattleResult());		
			buffer.append(",");
			
			buffer.append(log.getBeforePlace());		
			buffer.append(",");
			
			buffer.append(log.getAfterPlace());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getGetItem());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getUseEnergy());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getComment());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(System.currentTimeMillis()));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleMainHero());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleHeros());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getDefendMainHero());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getDefendHeros());
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.arenaLogCount++;
			
			gameLogDAO.insertRoleAreanLog();
		}
		
	}
	
	/**
	 * 兵来将挡
	 * @param to
	 */
	public static void insertDefendLog(DefendFightLog log) {
		//INSERT INTO DEFEND_FIGHT_LOG 
		//(S_ACCOUNT,S_NAME,N_ROLE_ID,N_HERO_NO,N_FIGHT_LV,D_STARTTIME,D_ENDTIME,S_POS,S_DROP,N_FIGHT_RESULT) alues 
		StringBuffer buffer = GameLogDAO.defendLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append(log.getMainHeroId());		
			buffer.append(",");
			
			buffer.append(log.getBattleResult());		
			buffer.append(",");
			
			buffer.append(log.getFightLv());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getBeginTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getEndTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getPos());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getDrop());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getBattleResult());
			
			buffer.append("),");
			
			GameLogDAO.defendLogCount++;
			
			gameLogDAO.insertDefendFightLog();
		}
		
	}
	
	/**
	 * 世界打野
	 * @param to
	 */
	public static void insertMapFightLog(MapFightLog log) {
		//INSERT INTO MAP_FIGHT_LOG 
		//(S_ACCOUNT,S_NAME,N_ROLE_ID,N_HERO_NO,N_NPC_ID,N_COME_ROLEID,D_STARTTIME,D_ENDTIME,S_POS,S_DROP,N_FIGHT_RESULT) values
		StringBuffer buffer = GameLogDAO.mapFightLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append(log.getMainHeroId());		
			buffer.append(",");
			
			buffer.append(log.getNpcId());		
			buffer.append(",");
			
			buffer.append(log.getNpcId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getBeginTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getEndTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getPos());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getDrop());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getFightResult());
			
			buffer.append("),");
			
			GameLogDAO.mapFightLogCount++;
			
			gameLogDAO.insertMapFightLog();
		}
		
	}
	
	/**
	 * 采矿战斗
	 * @param to
	 */
	public static void insertMineFightLog(MineFightLog log) {
		//INSERT INTO MINE_FIGHT_LOG 
		//(N_POSITION,N_MINE_NO,N_ROLE_ID,S_ROLE_NAME,S_ACCOUNT,N_ROLE_HERO_NO,N_ROLE_LEVEL,
		//N_ATTACK_ROLE_ID,S_ATTACK_ROLE_NAME,S_ATTACK_ACCOUNT,N_ATTACK_ROLE_HERO_NO,N_ATTACK_ROLE_LEVEL,
		//N_FIGHT_RESULT,D_TIME) values 
		StringBuffer buffer = GameLogDAO.mineFightLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append(log.getPosition());		
			buffer.append(",");
			
			buffer.append(log.getMineNo());		
			buffer.append(",");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleHeroNo());		
			buffer.append(",");
			
			buffer.append(log.getRoleLevel());		
			buffer.append(",");
			
			buffer.append(log.getAttackRoleId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getAttackRoleName() == null ? null : log.getAttackRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getAttackAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getAttackRoleHeroNo());		
			buffer.append(",");
			
			buffer.append(log.getAttackRoleLevel());		
			buffer.append(",");
			
			buffer.append(log.getFightResult());
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getTime());
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.mineFightLogCount++;
			
			gameLogDAO.insertMineFightLog();
		}
	}
	
	/**
	 * 攻城略地
	 * @param to
	 */
	public static void insertCampLog(RoleCampLog log) {
		//INSERT INTO GAME_CAMP_LOG
		//(S_ACCOUNT, S_ROLE_NAME, N_ROLE_ID,N_CAMP_NO,N_BATTLE_RESULT,S_PRIZE,	D_START_TIME,D_END_TIME,S_COMMENT)VALUES
		StringBuffer buffer = GameLogDAO.campLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append(log.getCampNo());		
			buffer.append(",");
			
			buffer.append(log.getBattleResult());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getPrize());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getStartTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getEndTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getComent());
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.campLogCount++;
			
			gameLogDAO.insertRoleCampLog();
		}
	}

	
	/**
	 * 记录PVP竞技场日志
	 * @param to
	 */
	public static void insertCompetitiveLog(CompetitiveLog log) {
		//INSERT INTO COMPETITIVE_LOG
		//(S_SEND_ACCOUNT,S_SEND_ROLE,N_ROLE_ID,N_HERO_NO,D_CREATE,N_BEFORE_STAGE_VALUE,N_AFTER_STAGE_VALUE,
		// N_BEFORE_STAGE,N_AFTER_STAGE,N_BEFORE_RANK,N_AFTER_RANK,S_GET_ITEM,N_USE_ENERGY,S_MATCH_ROLE,
		//  N_TARGET_HERO_NO,S_COMMENT,N_BATTLE_RESULT,S_HERO_IDS)VALUES
		StringBuffer buffer = GameLogDAO.competitiveLogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append(log.getHeroNo());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getCreateTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getBeforeStageValue());		
			buffer.append(",");
			
			buffer.append(log.getAfterStageValue());	
			buffer.append(",");
			
			buffer.append(log.getBeforeStage());	
			buffer.append(",");
			
			buffer.append(log.getAfterStage());	
			buffer.append(",");
			
			buffer.append(log.getBeforeRank());	
			buffer.append(",");
			
			buffer.append(log.getAfterRank());	
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getGetItem());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getUseEnergy());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getMatchRole());
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(log.getTargetHeroNo());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getComment());
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(log.getBattleResult());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getHeroNos());
			buffer.append("'");
			
			buffer.append("),");
			
			GameLogDAO.competitiveLogCount++;
			
			gameLogDAO.insertCompetitiveLog();
		}
	}
	
	/**
	 * 记录多人PVP日志
	 * @param to
	 */
	public static void insertPVP3Log(GamePvp3Log log) {
		//INSERT INTO GAME_PVP_3_LOG
		//(N_ROLE_ID,S_ACCOUNT,S_ROLE_NAME,N_RESULT,N_POINT,N_BEFORE_POINT,N_AFTER_POINT,D_START_TIME,D_END_TIME)	VALUES
		StringBuffer buffer = GameLogDAO.pvp3LogBuffer;
		synchronized(buffer){
			buffer.append("(");
			
			buffer.append(log.getRoleId());		
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append(log.getResult());		
			buffer.append(",");
			
			buffer.append(log.getPoint());		
			buffer.append(",");
			
			buffer.append(log.getBeforePoint());		
			buffer.append(",");
			
			buffer.append(log.getAfterPoint());	
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getStartTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(log.getEndTime());
			buffer.append("'");			
			
			buffer.append("),");
			
			GameLogDAO.pvp3LogCount++;
			
			gameLogDAO.insertPvp3Log();
		}
	}
	
	
	/**
	 * 世界BOSS日志
	 * @param roleInfo
	 * @param before
	 * @param after
	 */
	public static void insertWorldBossLog(WorldBossFightLog bossLog) {
		//INSERT INTO WORLD_BOSS_FIGHT_LOG
		//(S_ACCOUNT, S_ROLENAME, D_BEGINTIME,D_ENGTIME,N_HURT)VALUES
		
		StringBuffer buffer = GameLogDAO.worldBossLogBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(bossLog.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(bossLog.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(bossLog.getBeginTime());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(bossLog.getEndTime());
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(bossLog.getHurt());
			
			buffer.append("),");
			
			GameLogDAO.worldBossLogCount++;
			
			gameLogDAO.insertWorldBossFightLog();
		}
	}
	
	/**
	 * 获取掉落str
	 * @param prizeList
	 * @param fpPrizeList
	 * @return
	 */
	public static String getItem(List<BattlePrize> prizeList, List<BattlePrize> fpPrizeList) {
		Map<String, Integer> getMap = new HashMap<String, Integer>();
		if (prizeList != null && prizeList.size() > 0) {
			for (BattlePrize prize : prizeList) {
				Integer oldValue = getMap.get(prize.getNo().trim());
				if (oldValue == null) {
					getMap.put(prize.getNo().trim(), prize.getNum());
				} else {
					getMap.put(prize.getNo().trim(), prize.getNum() + oldValue);
				}
			}
		}
		if (fpPrizeList != null && fpPrizeList.size() > 0) {
			Integer oldValue = getMap.get(fpPrizeList.get(0).getNo().trim());
			if (oldValue == null) {
				getMap.put(fpPrizeList.get(0).getNo().trim(), fpPrizeList.get(0).getNum());
			} else {
				getMap.put(fpPrizeList.get(0).getNo().trim(), fpPrizeList.get(0).getNum() + oldValue);
			}
		}
		StringBuffer getItem = new StringBuffer();
		for (String itemNo : getMap.keySet()) {
			if (getItem.length() > 0) {
				getItem.append(",");
			}
			getItem.append(itemNo);
			getItem.append(":");
			getItem.append(getMap.get(itemNo));
		}
		return getItem.toString();
	}
	
	/**
	 * 记录加速玩家日志
	 * @param roleInfo
	 * @param before
	 * @param after
	 */
	public static void insertCheckTimeLog(RoleInfo roleInfo,Long curTime,Long preTime,int num) {
		//INSERT INTO CHECK_TIME_LOG(S_ACCOUNT, S_ROLENAME, D_CUR_TIME,D_PRE_TIME,N_NUM) VALUES
		
		StringBuffer buffer = GameLogDAO.checkTimeBuffer;
		synchronized(buffer)
		{
			buffer.append("(");
			
			buffer.append("'");
			buffer.append(roleInfo.getAccount());
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(roleInfo.getRoleName().replace("\\", ""));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(curTime));
			buffer.append("'");			
			buffer.append(",");
			
			buffer.append("'");
			buffer.append(new Timestamp(preTime));
			buffer.append("'");
			buffer.append(",");
			
			buffer.append(num);
			
			buffer.append("),");
			
			GameLogDAO.checkTimeLogCount++;
			
			gameLogDAO.insertCheckTimeLog();
		}
	}
}
