package com.snail.webgame.game.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.Flag;
import com.snail.webgame.engine.common.util.Sequence;
import com.snail.webgame.engine.component.charge.facade.ChargeService;
import com.snail.webgame.game.cache.QueryChargePointList;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.cache.TempMsgrMap;
import com.snail.webgame.game.cache.ToolProgramList;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.ToolProgram;
import com.snail.webgame.game.info.RoleInfo;

public class TaskService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 查询队列里的礼物
	 */
	public static void queryChargeGift(){
		
		if(Flag.flag==1){
			return;
		}
		int num = 0;
		ChargeService chargeService = ChargeGameService.getChargeService();
		Set<Integer> roleGiftList = QueryChargePointList.getGiftRoleList();
		for(Integer roleId:roleGiftList)
		{
			RoleInfo roleInfo =  RoleInfoMap.getRoleInfo(roleId);
			//兑换成功的话就去查询礼物
			if(roleInfo!=null){
				
				int sequenceId = Sequence.getSequenceId();
				GameMessageHead gameMessageHead = new GameMessageHead();
				gameMessageHead.setUserID0((int)roleInfo.getId());
				Message message =  new Message();
				message.setHeader(gameMessageHead);
				try {
					if (roleInfo.getRoleLoadInfo() != null) {
						TempMsgrMap.addMessage(sequenceId,message);
						chargeService.sendQueryGift(sequenceId, roleInfo.getAccountId());
						if (logger.isInfoEnabled()) {
							logger.info("#####queryChargeGift" + ",sequenceId=" + sequenceId+",accountId="+roleInfo.getAccountId()
									+",account="+roleInfo.getAccount());
						}
						num++;
					}
				} catch (Exception e) {
					if(logger.isInfoEnabled()){
						logger.error("",e);
					}
				}
			}
			if (num == 10) {
				num = 0;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					if(logger.isInfoEnabled()){
						logger.info("",e);
					}
				}
			}
		}
		
		if(roleGiftList.size() > 0){
			if(logger.isInfoEnabled()){
				logger.info("queryChargeGift successful!");
			}
		}
	}
	
	/**
	 * 查询在线玩家的礼物
	 */
	public static void queryChargeAllGift(){
		
		if(Flag.flag==1){
			return;
		}
		int num = 0;
		ChargeService chargeService = ChargeGameService.getChargeService();
		for(Entry<Integer, RoleInfo> entry:RoleLoginMap.getMap().entrySet()){
			RoleInfo roleInfo =  entry.getValue();
			if(roleInfo!=null){
				
				int sequenceId = Sequence.getSequenceId();
				GameMessageHead gameMessageHead = new GameMessageHead();
				gameMessageHead.setUserID0((int)roleInfo.getId());
				gameMessageHead.setUserID2(1);//用于标示是全服在线查询
				Message message =  new Message();
				message.setHeader(gameMessageHead);
				try {
					if (roleInfo.getRoleLoadInfo() != null) {
						TempMsgrMap.addMessage(sequenceId,message);
						chargeService.sendQueryGift(sequenceId, roleInfo.getAccountId());
						if (logger.isInfoEnabled()) {
							logger.info("#####queryChargeAllGift" + ",sequenceId=" + sequenceId+",accountId="+roleInfo.getAccountId()
									+",account="+roleInfo.getAccount());
						}
						num++;
					}
				} catch (Exception e) {
					if(logger.isInfoEnabled()){
						logger.error("",e);
					}
				}
				
				if(roleInfo.getRoleLoadInfo() != null && roleInfo.getDisconnectPhase() != 1)
				{
					//长时间(20秒,应该是2秒检验一次)未发时间检测,可能外挂屏蔽了,日志记录
					if((System.currentTimeMillis() - roleInfo.getRoleLoadInfo().getServerCheckTime() > 20000))
					{
						GameLogService.insertCheckTimeLog(roleInfo, System.currentTimeMillis(), roleInfo.getRoleLoadInfo().getServerCheckTime(),
								roleInfo.getRoleLoadInfo().getServerCheckTimeNum());
					}
				}
			}
			if (num == 10) {
				num = 0;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					if(logger.isInfoEnabled()){
						logger.info("",e);
					}
				}
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("queryChargeAllGift successful!");
		}
	}
	
	/**
	 * 当前时间是否在双倍经验活动时间范围内
	 * @return
	 */
	public static boolean checkProgramTime(byte programId, long timeInMilles)
	{
		boolean flag = false;
		List<ToolProgram> list = null;
		if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_EXP)
		{
			list = ToolProgramList.getDoubleExpList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_MONEY)
		{
			list = ToolProgramList.getDoubleMoneyList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_COURAGE)
		{
			list = ToolProgramList.getDoubleCourageList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_JUSTICE)
		{
			list = ToolProgramList.getDoubleJusticeList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_KUAFUMONEY)
		{
			list = ToolProgramList.getDoubleKuafuMoneyList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_TEAMMONEY)
		{
			list = ToolProgramList.getDoubleTeamMoneyList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_EXPLOIT)
		{
			list = ToolProgramList.getDoubleExploitList();
		}else if(programId == GameValue.GAME_TOOL_ACTIVE_TYPE_HERO_BATTLE)
		{
			list = ToolProgramList.getDoubleHeroBattleList();
		}
		if(list==null)
		{
			return false;
		}
		Date today = new Date(timeInMilles);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(int i=0;i<list.size();i++)
		{
			ToolProgram toolProgram = list.get(i);
			Date startDate = null;
			Date endDate = null;
			long todayStartTime = 0;
			long todayEndTime = 0;
			String startTime = sdf1.format(today) + " " + toolProgram.getStartTime().trim();
			String endTime = sdf1.format(today) + " " + toolProgram.getEndTime().trim();
			try {
				startDate = sdf1.parse(toolProgram.getStartDate().trim());
				endDate = sdf1.parse(toolProgram.getEndDate().trim());
				
				todayStartTime = sdf2.parse(startTime).getTime();
				todayEndTime = sdf2.parse(endTime).getTime();
			} catch (ParseException e) {
				if(logger.isErrorEnabled()){
					logger.error("",e);
				}
				
				return false;
			}
			//日期不符合，继续
			if(!today.after(startDate)||!today.before(endDate))//日期在时间段之外
			{
				String todayStr = sdf1.format(today);
				String startDateStr = sdf1.format(startDate);
				String endDateStr = sdf1.format(endDate);
				if(!startDateStr.equals(todayStr)&&!endDateStr.equals(todayStr))//当天也表示日期符合
				{
					continue;
				}
			}
			//时间不符合，继续
			if(today.getTime()<todayStartTime||today.getTime()>todayEndTime)
			{
				continue;
			}
			Calendar rightNow = Calendar.getInstance();
			int week = rightNow.get(Calendar.DAY_OF_WEEK) - 1;//表示星期几(1-星期一...)
			String weekTimes = toolProgram.getWeekTime();
			String [] weekTime = weekTimes.split("#");
			//星期符合，标记置为true
			for(int j=0;j<weekTime.length;j++)
			{
				if(week==j+1&&weekTime[j].equals("1"))
				{
					flag = true;
				}else if(week==0&&weekTime[6].equals("1"))
				{
					flag = true;
				}
			}
			if(flag)
			{
				break;
			}
		}
		
		return flag;
	}
}
