package com.snail.webgame.game.thread;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.LogMessage;
import com.snail.webgame.game.common.RoleOutLog;
import com.snail.webgame.game.common.TimeMessage;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.log.ActivityLog;
import com.snail.webgame.game.info.log.BiaocheLog;
import com.snail.webgame.game.info.log.ChallengeLog;
import com.snail.webgame.game.info.log.CompetitiveLog;
import com.snail.webgame.game.info.log.DefendFightLog;
import com.snail.webgame.game.info.log.EquipInfoLog;
import com.snail.webgame.game.info.log.EquipResolveLog;
import com.snail.webgame.game.info.log.EquipUpLog;
import com.snail.webgame.game.info.log.GamePVELog;
import com.snail.webgame.game.info.log.GamePvp3Log;
import com.snail.webgame.game.info.log.GoldBuyLog;
import com.snail.webgame.game.info.log.HeroSkillUpLog;
import com.snail.webgame.game.info.log.HeroUpLog;
import com.snail.webgame.game.info.log.InstanceLog;
import com.snail.webgame.game.info.log.ItemLog;
import com.snail.webgame.game.info.log.MailInfoLog;
import com.snail.webgame.game.info.log.MapFightLog;
import com.snail.webgame.game.info.log.MineFightLog;
import com.snail.webgame.game.info.log.MineGetLog;
import com.snail.webgame.game.info.log.MoneyLog;
import com.snail.webgame.game.info.log.PlayActionLog;
import com.snail.webgame.game.info.log.ProgramLog;
import com.snail.webgame.game.info.log.PunishLog;
import com.snail.webgame.game.info.log.RoleArenaLog;
import com.snail.webgame.game.info.log.RoleCampLog;
import com.snail.webgame.game.info.log.RoleChargeLog;
import com.snail.webgame.game.info.log.RoleClubLog;
import com.snail.webgame.game.info.log.RoleRelationLog;
import com.snail.webgame.game.info.log.RoleUpgradeLog;
import com.snail.webgame.game.info.log.SceneLog;
import com.snail.webgame.game.info.log.SnatchLog;
import com.snail.webgame.game.info.log.SoliderUpLog;
import com.snail.webgame.game.info.log.StoreBuyItemLog;
import com.snail.webgame.game.info.log.TaskLog;
import com.snail.webgame.game.info.log.WeaponUpLog;
import com.snail.webgame.game.info.log.WorldBossFightLog;

/**
 * 处理可以慢慢处理的事件，异步
 * 
 * @author xiasd
 *
 */
public class ExecuteLogsThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static BlockingQueue<TimeMessage> queue = new LinkedBlockingQueue<TimeMessage>(Integer.MAX_VALUE);

	private volatile boolean flag = false;
	
	public ExecuteLogsThread() {
		this.setName("ExecuteLogsThread");
	}
	@Override
	public void run() {
		TimeMessage timeMessage = null;
		
		while (!flag) {
			try {
				if(timeMessage == null){
					timeMessage = queue.take();
				}

				if (timeMessage != null && timeMessage.getType() == ETimeMessageType.EXECUTE_BATCH_LOGS) {
					long start = System.currentTimeMillis();
					
					executeBatchLogs((LogMessage) timeMessage);
					
					logger.warn("executeBatchLogs time = " + (System.currentTimeMillis() - start));
				}

				timeMessage = null;
			} catch (Exception e) {
				logger.error("ExecuteLogsThread, takeMessage error", e);
				
				if (timeMessage != null && timeMessage.getReTryCount() < 3) {
					timeMessage.setReTryCount(timeMessage.getReTryCount() + 1);
				} else {
					if (timeMessage != null) {
						logger.error("ExecuteLogsThread, Failed " + timeMessage.getType());
					}
					timeMessage = null;
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		logger.info("ExecuteLogsThread exited");
	}
	
	/**
	 * 处理日志
	 * 
	 * @param timeMessage
	 */
	private void executeBatchLogs(LogMessage timeMessage) {
		if(timeMessage != null){
			List<?> list = timeMessage.getLogList();
			
			if(list != null && list.size() > 0){
				Object log = list.get(0);

				SqlMapClient client = null;
				
				try {
					client = SqlMapClientFactory.getSqlMapClient(DbConstants.GAME_LOG_DB, ExecutorType.BATCH, false);
					String sqlKey = null;
					
					if(client != null){
						if(log instanceof MoneyLog){
							sqlKey = "insertMoneyLogBatch";
						} else if(log instanceof TaskLog){
							sqlKey = "insertTaskLogBatch";
						} else if(log instanceof ItemLog){
							sqlKey = "insertItemLogBatch";
						} else if(log instanceof RoleChargeLog){
							sqlKey = "insertRoleChargeLogListBathch";
						} else if(log instanceof MineGetLog){
							sqlKey = "insertMineGetLogListBathch";
						} else if(log instanceof MineFightLog){
							sqlKey = "insertMineFightLogListBathch";
						} else if(log instanceof MapFightLog){
							sqlKey = "insertMapFightLogListBathch";
						} else if(log instanceof DefendFightLog){
							sqlKey = "insertDefendLogFightLogListBathch";
						} else if(log instanceof ActivityLog){
							sqlKey = "insertActivityLogBatch";
						} else if(log instanceof WorldBossFightLog){
							sqlKey = "insertWorldBossFightLogBathch";
						} else if(log instanceof SnatchLog){
							sqlKey = "insertSnatchLogBathch";
						} else if(log instanceof RoleCampLog){
							sqlKey = "insertRoleCampLogBathch";
						} else if(log instanceof RoleArenaLog){
							sqlKey = "insertRoleAreanLogBathch";
						} else if(log instanceof EquipResolveLog){
							sqlKey = "insertEquipResolveLog";
						} else if(log instanceof RoleClubLog){
							sqlKey = "insertRoleClubLog";
						} else if(log instanceof RoleRelationLog){
							sqlKey = "insertRoleRelationLog";
						} else if(log instanceof GamePVELog){
							sqlKey = "insertPlayerPVELog";
						} else if(log instanceof GoldBuyLog){
							sqlKey = "insertGoldBuyLogBatch";
						} else if(log instanceof HeroSkillUpLog){
							sqlKey = "insertHeroSkillUpLogBatch";
						} else if(log instanceof ChallengeLog){
							sqlKey = "insertChallengeLogBatch";
						} else if(log instanceof SoliderUpLog){
							sqlKey = "insertSoliderUpLogBatch";
						} else if(log instanceof BiaocheLog){
							sqlKey = "insertBiaocheLogBatch";
						} else if(log instanceof StoreBuyItemLog){
							sqlKey = "insertStoreBuyItemLogBatch";
						} else if(log instanceof GamePvp3Log){
							sqlKey = "insertPvp3LogBatch";
						} else if(log instanceof CompetitiveLog){
							sqlKey = "insertCompetitiveLogBatch";
						} else if(log instanceof ProgramLog){
							sqlKey = "insertProgramLogBatch";
						} else if(log instanceof SceneLog){
							sqlKey = "insertSceneLogBatch";
						} else if(log instanceof InstanceLog){
							sqlKey = "insertInstanceLogBatch";
						} else if(log instanceof HeroUpLog){
							sqlKey = "insertHeroUpLogBatch";
						} else if(log instanceof WeaponUpLog){
							sqlKey = "insertWeaponUpLogBatch";
						} else if(log instanceof EquipInfoLog){
							sqlKey = "insertEquipInfoLogBatch";
						} else if(log instanceof EquipUpLog){
							sqlKey = "insertEquipUpLogBatch";
						} else if(log instanceof MailInfoLog){
							sqlKey = "insertMailInfoLogBatch";
						} else if(log instanceof PlayActionLog){
							sqlKey = "insertPlayActionLogBatch";
						} else if(log instanceof RoleOutLog){
							sqlKey = "updateRoleLog";
						} else if(log instanceof RoleUpgradeLog){
							sqlKey = "insertRoleUpgradeLogBatch";
						} else if(log instanceof PunishLog){
							sqlKey = "insertPunishLogBatch";
						}
					}
					if(sqlKey != null){
						if (logger.isInfoEnabled()) {
							logger.info(sqlKey);
						}
						
						if(sqlKey == "updateRoleLog")
						{
							for (Object outLog : list) {
								client.update("updateRoleLog", outLog);
							}
						}
						else
						{
							client.insertBatch(sqlKey, list);
						}
					}
					
					if(client != null){
						client.commit();
					}
				} catch (Exception e) {
					if (logger.isErrorEnabled()){
						logger.error("", e);
					}
					if(client != null){
						client.rollback();
					}
				} finally {					
					list.clear();
					list = null;
				}
			}
		}
	}
	/**
	 * 添加消息
	 * 
	 * @param sendMessageInfo
	 */
	public void addMessage(TimeMessage timeMessage) {
		try {
			queue.put(timeMessage);
		} catch (InterruptedException e) {
			logger.error("ExecuteLogsThread addMessage error", e);
		}
	}
	
	public void cancel() {
		this.flag = true;
	}
}
