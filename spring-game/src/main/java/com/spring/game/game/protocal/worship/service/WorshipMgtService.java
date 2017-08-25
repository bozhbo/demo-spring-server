package com.snail.webgame.game.protocal.worship.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.worship.doWorship.DoWorshipReq;
import com.snail.webgame.game.protocal.worship.doWorship.DoWorshipResp;

//膜拜大R逻辑处理类
public class WorshipMgtService {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static boolean plusFlag = false;
	/**
	 * 膜拜大R
	 * @param roleId
	 * @param req
	 * @return 
	 * @author luwd
	 */
	public DoWorshipResp doWorship(int roleId,DoWorshipReq req){
		int superRoleId = req.getSuperRoleId();
		DoWorshipResp resp = new DoWorshipResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		RoleInfo superRoleInfo = RoleInfoMap.getRoleInfo(superRoleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.WORSHIP_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.WORSHIP_ERROR_2);
			return resp;
		}
		
		if(superRoleInfo == null){
			resp.setResult(ErrorCode.WORSHIP_ERROR_5);
			return resp;
		}
		
		//获取原子锁，防止刷消息
		if(!WorshipMgtService.tryLock(roleInfo)){
			resp.setResult(ErrorCode.WORSHIP_ERROR_6);
			return resp;
		}
		
		//执行代码，不管结果如何释放原子锁
		try{
			synchronized (superRoleInfo) {
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if(heroInfo.getHeroLevel()<GameValue.WORSHIP_NEED_LEVEL){
					resp.setResult(ErrorCode.WORSHIP_ERROR_3);
					return resp;
				}
				if(roleInfo.getId() == superRoleInfo.getId()){
					resp.setResult(ErrorCode.WORSHIP_ERROR_8);
					return resp;
				}
				if(roleInfo.getWorshipCount() == 0){
					resp.setResult(ErrorCode.WORSHIP_ERROR_4);
					return resp;
				}
				//膜拜成功修改数据库和缓存
				if(!RoleDAO.getInstance().updateRoleWorshipCount(roleId, roleInfo.getWorshipCount()-1)||
						!RoleDAO.getInstance().updateRoleBeWorshipNum(superRoleId, superRoleInfo.getBeWorshipNum()+1)){
					logger.error("WorshipMgtService.doWorship() failure");
					resp.setResult(ErrorCode.WORSHIP_ERROR_7);
					return resp;
				}
				
				roleInfo.setWorshipCount(roleInfo.getWorshipCount()-1);
				resp.setBeWorShipNum(superRoleInfo.getBeWorshipNum()+1);
				superRoleInfo.setBeWorshipNum(superRoleInfo.getBeWorshipNum()+1);
			}
			//膜拜赠送物品（可扩展）
			String item = "sp:" + GameValue.WORSHIP_SEND_SP + ";" + "money:" + GameValue.WORSHIP_SILVER_SEND;
			resp.setItem(item);
			
			//膜拜赠送的体力和银两
			RoleService.addRoleRoleResource(ActionType.action470.getType(), roleInfo, ConditionType.TYPE_SP,
					GameValue.WORSHIP_SEND_SP,null);
			RoleService.addRoleRoleResource(ActionType.action470.getType(), roleInfo, ConditionType.TYPE_MONEY,
					GameValue.WORSHIP_SILVER_SEND,null);
			String spStr = roleInfo.getSp() + "," + roleInfo.getLastRecoverSPTime().getTime() + ","
					+ roleLoadInfo.getTodayBuySpNum() + "," + 0;
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SP, spStr);
			
			//记录日志
			//我的账号,我的名字:对方账号,对方名字:itemNo-itemNum,itemNo-itemNum
			String actValue = roleInfo.getAccount()+","+roleInfo.getRoleName()+":"+superRoleInfo.getAccount()+","+
					superRoleInfo.getRoleName()+":"+ConditionType.TYPE_SP.getType()+"-"+GameValue.WORSHIP_SEND_SP+
					","+ConditionType.TYPE_MONEY.getType()+"-"+GameValue.WORSHIP_SILVER_SEND; 
			
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action470.getType(), actValue);
			
			QuestService.checkQuest(roleInfo, ActionType.action470.getType(), null, true, false);
			
			resp.setResult(1);
			return resp;
		}finally{
			unLock(roleInfo);
		}
	}
	
	public static void plusSuperRRandomValue(){
		
		if(isMatchTime(GameValue.SUPERR_START_PLUS_TIME,GameValue.SUPERR_END_PLUS_TIME) ){
			//开服那次开启线程时不加，但以后都加
			if(!plusFlag){
				plusFlag = true;
				return;
			}
			
			//计算第一第二第三名每个需要加的随机值
			String perPlus1 = GameValue.SUPERR_PER_1;
			String perPlus2 = GameValue.SUPERR_PER_2;
			String perPlus3 = GameValue.SUPERR_PER_3;
			
			String[] perArr1 = perPlus1.split(":");
			int perPlusNumMin1 = Integer.parseInt(perArr1[0]);
			int perPlusNumMax1 = Integer.parseInt(perArr1[1]); 
			
			String[] perArr2 = perPlus2.split(":");
			int perPlusNumMin2 = Integer.parseInt(perArr2[0]);
			int perPlusNumMax2 = Integer.parseInt(perArr2[1]); 
			
			String[] perArr3 = perPlus3.split(":");
			int perPlusNumMin3 = Integer.parseInt(perArr3[0]);
			int perPlusNumMax3 = Integer.parseInt(perArr3[1]); 
			
			int per1 = (int) (perPlusNumMin1 + Math.random()*(perPlusNumMax1-perPlusNumMin1+1));
			int per2 = (int) (perPlusNumMin2 + Math.random()*(perPlusNumMax2-perPlusNumMin2+1));
			int per3 = (int) (perPlusNumMin3 + Math.random()*(perPlusNumMax3-perPlusNumMin3+1));
		
			int[] per = {per1,per2,per3};
			int[] max = {GameValue.SUPERR_BASE_1,GameValue.SUPERR_BASE_2,GameValue.SUPERR_BASE_3};
			
			List<RankInfo> list = RankService.getFightValueRank(1, 3);
			RoleInfo roleInfo = null;
			if(list == null || list.size() > 3){
				logger.info("plusSuperRRandomValue error ------- rankList is null or rankList.size>3");
			}else{
				for(int i = 0;i<list.size();i++){
					RankInfo rankInfo = list.get(i);
					int roleId = rankInfo.getRoleId();
					roleInfo = RoleInfoMap.getRoleInfo(roleId);
					synchronized(roleInfo){
						if(roleInfo.getTodayPlusWorshipNum() >= max[i]){
							logger.info("worship num is plus max,this time plus di" + i+1 +"ming" + "0");
							return;
						}
						
						int perNum = 0;
						
						if(roleInfo.getTodayPlusWorshipNum() + per[i] > max[i]){
							perNum = max[i] - roleInfo.getTodayPlusWorshipNum();
						}else{
							perNum = per[i];
						}
						
						int beWorshipNum = roleInfo.getBeWorshipNum() + perNum;
						int todayPlusWorShipNum = roleInfo.getTodayPlusWorshipNum() + perNum;
						//修改数据库
						if(!RoleDAO.getInstance().updateTodayPlusWorshipNum(roleId, todayPlusWorShipNum)||
								!RoleDAO.getInstance().updateRoleBeWorshipNum(roleId, beWorshipNum)){
							logger.error("WorshipMgtService.plusSuperRRandomValue() failure");
							return;
						}
						//成功加入数据库后修改缓存
						roleInfo.setBeWorshipNum(beWorshipNum);
						roleInfo.setTodayPlusWorshipNum(todayPlusWorShipNum);
						
					}
				}
			}
		} 
	}
	
	/**
	 * 检查superR计算时间
	 * 
	 * @return	boolean true-符合时间区间 false-不符合时间区间
	 */
	private static boolean isMatchTime(Date startTime, Date endTime) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2000);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		Date curDate = c.getTime();
		
		if (endTime.before(startTime)) {
			// 跨越24点
			if (curDate.before(startTime) && curDate.after(endTime)) {
				return false;
			}
		} else {
			if (curDate.before(startTime) || curDate.after(endTime)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 每天 24:00发放大R被膜拜奖励并刷新可膜拜数和被膜拜数
	 * @author luwd
	 */
	public static void sendPlaceReward() {
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		if (roleMap != null) {
			List<Integer> list = new ArrayList<Integer>();
			int i = 0;
			for(RoleInfo info : roleMap.values()){
				if (info != null){
					if(info.getWorshipCount()!=GameValue.WORSHIP_MAX_NUM){
						list.add(info.getId());
					}
				}
			}
			if(!RoleDAO.getInstance().updateRoleWorshipCountBatch(list, GameValue.WORSHIP_MAX_NUM)){
				logger.error("WorshipMgtService.sendPlaceReward() failure");
				return ;
			}
			
			list.clear();
			list.addAll(roleMap.keySet());
			
			if(!RoleDAO.getInstance().batchUpdateRoleWorldChatCount(list)){
				logger.error("batchUpdateRoleWorldChatCount failure");
				return ;
			}
			
			
			for (RoleInfo info : roleMap.values()) {
				if (info != null) {
					synchronized (info) {
						i++;
						if(info.getBeWorshipNum()>0){
							sendPlaceReward(info);
						}
						//更新可膜拜次数
						if(info.getWorshipCount()!=GameValue.WORSHIP_MAX_NUM){
							info.setWorshipCount(GameValue.WORSHIP_MAX_NUM);
						}
						
						if(info.getLoginStatus() == 1){
							//在线 通知MailServer清零
							RoleService.sendWorldChatLimit2MailServer(info, 1);
						}
						
					}
					
				}
				if(i>=100){
					
					try {
						TimeUnit.MILLISECONDS.sleep(50);
						i=0;
					} catch (InterruptedException e) {
						logger.error("WorshipMgtService.sendPlaceReward() sleep failure");
						return;
					}
				
				}
			}

		}
	}
	
	
	
	/**
	 * 大R被膜拜次数奖励发放
	 * @param roleInfo
	 * @return
	 * @author luwd
	 */
	private static int sendPlaceReward(RoleInfo roleInfo) {
		MailAttachment att = null;
		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		att = new MailAttachment(ConditionType.TYPE_MONEY.getName(), roleInfo.getBeWorshipNum()*GameValue.BE_WORSHIP_PER_SILVER, 1, 0);
		attachments.add(att);

		if(!RoleDAO.getInstance().updateRoleBeWorshipNum(roleInfo.getId(), 0)||
				!RoleDAO.getInstance().updateTodayPlusWorshipNum(roleInfo.getId(), 0)){
			logger.error("WorshipMgtService.sendPlaceReward() failure");
			return ErrorCode.WORSHIP_ERROR_7;
		}
		
		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() > 0) {
			
			String title = Resource.getMessage("game", "WORSHIP_PRIZE_PUSH_TITLE");
			String content = Resource.getMessage("game", "WORSHIP_PRIZE_PUSH_CONTENT");
			String reserve = roleInfo.getBeWorshipNum()+ "";

			MailService.pushMailPrize(roleInfo.getId() + "", attachment, title, content, reserve);
			logger.info("-----------------send worship prize-----------------------");
		}
		
		roleInfo.setBeWorshipNum(0);
		roleInfo.setTodayPlusWorshipNum(0);
		
		return 1;
	}
	
	public static boolean tryLock(RoleInfo roleInfo) {
		return roleInfo.getWorShipLock().compareAndSet(false, true);
	}

	public static void unLock(RoleInfo roleInfo) {
		roleInfo.getWorShipLock().compareAndSet(true, false);
	}
	
}
