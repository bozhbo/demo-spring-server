package com.snail.webgame.game.protocal.club.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.MailMessage;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.AbstractConditionCheck.BaseSubResource;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.ClubHireHeroDao;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.dao.RoleClubEventDao;
import com.snail.webgame.game.info.ClubEventInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.HireHeroInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.club.entity.ClubEveInfoRe;
import com.snail.webgame.game.protocal.club.entity.ClubFightInfo;
import com.snail.webgame.game.protocal.club.entity.ClubRequestInfoRe;
import com.snail.webgame.game.protocal.club.entity.ClubRoleInfoRe;
import com.snail.webgame.game.protocal.club.hire.service.HireHeroService;
import com.snail.webgame.game.protocal.club.info.GetClubInfoResp;
import com.snail.webgame.game.protocal.club.syn.SysKingdomChatReq;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.thread.SendServerMsgThread;

public class ClubService {

	/**
	 * 发送邮件 通知玩家
	 * @param mailInfo
	 * @param reserve
	 * @param mailRoleIdList
	 * @param roleId
	 */
	public static void sendMail2Role(String[] mailInfo, String reserve, List<Integer> mailRoleIdList, int roleId){
		
		String title = Resource.getMessage("game", mailInfo[0]);
		String content = Resource.getMessage("game", mailInfo[1]);
		if(mailRoleIdList == null){
			mailRoleIdList = new ArrayList<Integer>();
			mailRoleIdList.add(roleId);
		}
		
		SendServerMsgThread.addMessage(new MailMessage(ETimeMessageType.SEND_BATCH_SYS_MAIL, mailRoleIdList, null, content, title, reserve));

	}
	
	/**
	 * 同步到邮件服务器（公会聊天）
	 * @param clubId
	 * @param roleId
	 * @param flag 0-加入公会 1-退出公会 2-解散公会
	 */
	public static void send2MailServerSynClub(int clubId, int roleId, int flag){
		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			SysKingdomChatReq req = new SysKingdomChatReq();
			req.setFlag(flag);
			req.setKingdomId(clubId);
			req.setRoleId(roleId);
			
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setUserID0(roleId);
			header.setMsgType(Command.UPDATE_CLUB_INFO_4_SYN_CHAT);
			message.setHeader(header);
			message.setBody(req);
			session.write(message);
		}
	
	}
	
	/**
	 * 获取公会事件列表
	 * @param roleClubInfo
	 * @return
	 */
	public static List<ClubEveInfoRe> getClubEveInfoList(RoleClubInfo roleClubInfo){
		List<ClubEventInfo> cList = RoleClubMemberInfoMap.getEventListByClubId(roleClubInfo.getId());
		List<ClubEveInfoRe> eventList = new ArrayList<ClubEveInfoRe>();
		
		RoleInfo memberRole = null;
		ClubEveInfoRe cRe = null;
		HeroInfo heroInfo = null;
		
		if(cList == null){
			return eventList;
		}
		for(ClubEventInfo eventInfo : cList){
			//获取俱乐部事件
			memberRole = RoleInfoMap.getRoleInfo(eventInfo.getRoleId());
			if(memberRole == null){
				continue;
			}
			
			if(eventInfo.getEvent() == 3){
				//去除解散的时间
				continue;
			}
			
			cRe = new ClubEveInfoRe();
			cRe.setRoleName(memberRole.getRoleName());
			cRe.setEve(eventInfo.getEvent());
			cRe.setTime(eventInfo.getTime().getTime());
			cRe.setRoleId(memberRole.getId());
			cRe.setFlag(memberRole.getId() == roleClubInfo.getCreateRoleId() ? 1 : 0);
			cRe.setId(eventInfo.getId());
			
			heroInfo = HeroInfoMap.getMainHeroInfo(memberRole);
			if(heroInfo != null){
				cRe.setHeroNo(heroInfo.getHeroNo());
				cRe.setLevel(heroInfo.getHeroLevel());
			}
			
			eventList.add(cRe);
		}
		
		Collections.sort(eventList, new Comparator<ClubEveInfoRe>() {
			@Override
			public int compare(ClubEveInfoRe o1, ClubEveInfoRe o2) {
				if(o1.getTime() < o2.getTime()){
					return 1;
				}else if(o1.getTime() == o2.getTime()){
					return 0;
				}else{
					return -1;
				}
			}
		});
		
		
		
		if(eventList.size() > GameValue.CLUB_EVENT_LIMIT){
			List<ClubEveInfoRe> list = new ArrayList<ClubEveInfoRe>();
			List<Integer> delIdList = new ArrayList<Integer>();
			ClubEveInfoRe re = null;
			long dayTime = 24 * 60 * 60 * 1000;
			long time = System.currentTimeMillis();
			for(int i = 0; i < eventList.size(); i++){
				if(list.size() < GameValue.CLUB_EVENT_LIMIT){
					//取前面50个
					list.add(eventList.get(i));
					
				}else{
					//超过的判断时间是否大于24小时 有的则删除
					re = eventList.get(i);
					if(re == null){
						continue;
					}
					
					if(time - re.getTime() > dayTime){
						delIdList.add(re.getId());
					}
					
				}
				
			}
			
			if(delIdList.size() > 0){
				//删除
				RoleClubEventDao.getInstance().batchDeleteClubEvent(delIdList);
				for(Integer id : delIdList){
					synchronized(GameFlag.ROLE_CLUB){
						RoleClubMemberInfoMap.removeClubEventById(id);
					}
				}
			}
			
			return list;
			
		}else{
			return eventList;
		}
		
	}
	
	/**
	 * 获取请求列表
	 * @param roleInfo
	 * @param roleClubInfo
	 * @return
	 */
	public static List<ClubRequestInfoRe> getJoinRequestList(RoleInfo roleInfo, RoleClubInfo roleClubInfo, GetClubInfoResp resp){
		List<ClubRequestInfoRe> list = new ArrayList<ClubRequestInfoRe>();
		
		boolean isLeader = false;
		RoleClubMemberInfo roleClubMemberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleInfo.getId());
		if(roleClubMemberInfo == null){
			return list;
		}
		
		if(roleClubMemberInfo.getStatus() == RoleClubMemberInfo.CLUB_BOSS
				|| roleClubMemberInfo.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT
				|| roleClubMemberInfo.getStatus() == RoleClubMemberInfo.CLUB_LEADER){
			
			isLeader = true;
		}
		
		if(isLeader){
			//是公会会长 获得公会的加入请求
			if(resp != null){
				resp.setFlag(roleClubMemberInfo.getStatus());
				resp.setLevelLimit(roleClubInfo.getLevelLimit());
				resp.setApprove(roleClubInfo.getFlag());
			}
			
			ClubRequestInfoRe requestRe = null;
			Map<Integer, RoleClubMemberInfo> memberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(roleClubInfo.getId());
			if(memberMap == null || memberMap.size() <= 0){
				return list;
			}
			
			RoleInfo memberRole = null;
			HeroInfo heroInfo = null;
			
			for(Map.Entry<Integer, RoleClubMemberInfo> entry : memberMap.entrySet()){
				memberRole = RoleInfoMap.getRoleInfo(entry.getKey());

				if(memberRole == null || (entry.getValue() != null && entry.getValue().getStatus() != 99)){
					continue;
				}
				
				requestRe = new ClubRequestInfoRe();
				
				requestRe.setFightValue(memberRole.getFightValue());
				requestRe.setRoleId(memberRole.getId());
				requestRe.setRoleName(memberRole.getRoleName());
				
				heroInfo = HeroInfoMap.getMainHeroInfo(memberRole);
				if(heroInfo != null){
					requestRe.setHeroNo(heroInfo.getHeroNo());
					requestRe.setLevel(heroInfo.getHeroLevel());
				}
				
				list.add(requestRe);
			}
			
		}
		
		return list;
	}
	
	/**
	 * 获取公会成员列表
	 * @param roleInfo
	 * @return
	 */
	public static List<ClubRoleInfoRe> getRoleClubMemberList(RoleInfo roleInfo, int clubId){
		List<ClubRoleInfoRe> list = new ArrayList<ClubRoleInfoRe>();
		Map<Integer, RoleClubMemberInfo> memberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(clubId);
		if(memberMap == null || memberMap.size() <= 0){
			return list;
		}
		
		
		List<ClubRoleInfoRe> adminList = new ArrayList<ClubRoleInfoRe>(); //公会管理成员
		ClubRoleInfoRe selfRe = null;
		ClubRoleInfoRe leaderRe = null;
		ClubRoleInfoRe re = null;
		RoleInfo memberRole = null;
		HeroInfo heroInfo = null;
		
		for(Map.Entry<Integer, RoleClubMemberInfo> entry : memberMap.entrySet()){
			memberRole = RoleInfoMap.getRoleInfo(entry.getKey());

			if(memberRole == null || (entry.getValue() != null && entry.getValue().getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER)){
				continue;
			}
			
			if(entry.getKey() == roleInfo.getId()){
				//自己
				selfRe = new ClubRoleInfoRe();
				selfRe.setContribution(memberRole.getTotalBuild());
				selfRe.setFlag(entry.getValue().getStatus());
				selfRe.setRoleId(entry.getKey());
				selfRe.setRoleName(memberRole.getRoleName());
				selfRe.setLoginStatus(memberRole.getLoginStatus());
				
				if(memberRole.getLoginStatus() == 0){
					selfRe.setTime(memberRole.getLogoutTime().getTime());
				}
				
				heroInfo = HeroInfoMap.getMainHeroInfo(memberRole);
				if(heroInfo != null){
					selfRe.setLevel(heroInfo.getHeroLevel());
					selfRe.setHeroNo(heroInfo.getHeroNo());
				}
				
			}else if(entry.getValue().getStatus() == RoleClubMemberInfo.CLUB_BOSS
					|| entry.getValue().getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT
					|| entry.getValue().getStatus() == RoleClubMemberInfo.CLUB_LEADER
			){
				// 公会管理者
				leaderRe = new ClubRoleInfoRe();
				leaderRe.setContribution(memberRole.getTotalBuild());
				leaderRe.setFlag(entry.getValue().getStatus());
				leaderRe.setRoleId(entry.getKey());
				leaderRe.setRoleName(memberRole.getRoleName());
				leaderRe.setLoginStatus(memberRole.getLoginStatus());
				
				if(memberRole.getLoginStatus() == 0){
					leaderRe.setTime(memberRole.getLogoutTime().getTime());
				}
				
				heroInfo = HeroInfoMap.getMainHeroInfo(memberRole);
				if(heroInfo != null){
					leaderRe.setLevel(heroInfo.getHeroLevel());
					leaderRe.setHeroNo(heroInfo.getHeroNo());
				}
				
				adminList.add(leaderRe);
				
			}else{
				//其他人
				re = new ClubRoleInfoRe();
				re.setContribution(memberRole.getTotalBuild());
				re.setFlag(entry.getValue().getStatus());
				re.setRoleId(entry.getKey());
				re.setRoleName(memberRole.getRoleName());
				re.setLoginStatus(memberRole.getLoginStatus());
				
				if(memberRole.getLoginStatus() == 0){
					re.setTime(memberRole.getLogoutTime().getTime());
				}
				
				heroInfo = HeroInfoMap.getMainHeroInfo(memberRole);
				if(heroInfo != null){
					re.setLevel(heroInfo.getHeroLevel());
					re.setHeroNo(heroInfo.getHeroNo());
				}
				
				list.add(re);
			}
			
		}
		
		Collections.sort(adminList, new Comparator<ClubRoleInfoRe>() {
			//按照职位大小排序
			@Override
			public int compare(ClubRoleInfoRe o1, ClubRoleInfoRe o2) {
				if(o1.getFlag() > o2.getFlag()){
					return 1;
				}else if(o1.getFlag() < o2.getFlag()){
					return -1;
				}
			
				return 0;
			}
		});
		
		list.addAll(0, adminList);
		
		
		if(selfRe != null){
			list.add(0, selfRe);
		}
		
		return list;
	}
	
	/**
	 * 获得公会成员数量
	 * @param clubId
	 * @return
	 */
	public static int getClubMemberNum(int clubId){
		Map<Integer, RoleClubMemberInfo> map = RoleClubMemberInfoMap.getRoleClubMemberMap(clubId);
		
		if(map != null && map.size() > 0){
			int count = 0;
			for(RoleClubMemberInfo info : map.values()){
				if(info == null){
					continue;
				}
				
				if(info.getStatus() != RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
					count++;
				}
			}
			
			return count;
			
		}
		
		return 0;
	}
	
	public static boolean tryLock(RoleInfo roleInfo) {
		return roleInfo.getClubLock().compareAndSet(false, true);
	}

	public static void unLock(RoleInfo roleInfo) {
		roleInfo.getClubLock().compareAndSet(true, false);
	}
	
	/**
	 * 删除公会管理者
	 * @param roleClubInfo
	 * @param roleId
	 */
	public static void removeAdmin(RoleClubInfo roleClubInfo, int roleId){
		if(roleClubInfo.getAdminSet().contains(roleId)){
			roleClubInfo.getAdminSet().remove(roleId);
		}
	}
	
	/**
	 * 加入公会管理者
	 * @param roleClubInfo
	 * @param roleId
	 */
	public static void addAdmin(RoleClubInfo roleClubInfo, int roleId){
		if(!roleClubInfo.getAdminSet().contains(roleId)){
			roleClubInfo.getAdminSet().add(roleId);
		}
	}
	
	/**
	 * String转Map
	 * @param roleInfo
	 * @return
	 */
	public static Map<Integer, Integer> clubTechPlusStr2Map(RoleInfo roleInfo){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if(roleInfo == null){
			return map;
		}
		
		if(roleInfo.getClubTechPlusInfo() == null || "".equals(roleInfo.getClubTechPlusInfo())){
			return map;
		}
		
		String[] str = roleInfo.getClubTechPlusInfo().split(";");
		
		for(String s : str){
			String[] ss = s.split(":");
			if(ss.length != 2){
				continue;
			}
			
			map.put(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
		}
		
		return map;
		
	}
	
	/**
	 * Map转String
	 * @param map
	 * @return
	 */
	public static String clubTechPlusMap2Str(Map<Integer, Integer> map){
		StringBuffer sb = new StringBuffer();
		
		if( map == null || map.size() <= 0){
			return "";
		}
		
		for(Map.Entry<Integer, Integer> e : map.entrySet()){
			sb.append(e.getKey()).append(":").append(e.getValue()).append(";");
		}
		
		return sb.toString();
		
	}
	
	/**
	 * 资源改变
	 * @param conds
	 * @return
	 */
	public static String resourceChange(List<AbstractConditionCheck> conds){
		StringBuffer buff = new StringBuffer();
		BaseSubResource sub = AbstractConditionCheck.subCondition(conds);
		if (sub == null) {
			return "";
		}

		if (sub.upMoney > 0) {
			buff.append(ConditionType.TYPE_MONEY.getType() + ":-" + sub.upMoney).append(";");
		}

		if (sub.upCoin > 0) {
			buff.append(ConditionType.TYPE_COIN.getType() + ":-" + sub.upCoin).append(";");
		}
		
		if (sub.upSp > 0) {
			buff.append(ConditionType.TYPE_SP.getType() + ":-" + sub.upSp).append(";");
		}
		
		if (sub.upEnergy > 0) {
			buff.append(ConditionType.TYPE_ENERGY.getType()+ ":-" +sub.upEnergy).append(";");
		}
		
		if (sub.upCourage > 0) {
			buff.append(ConditionType.TYPE_COURAGE.getType() + ":-" +sub.upCourage).append(";");
		}
		
		if (sub.upJustice > 0) {
			buff.append(ConditionType.TYPE_JUSTICE.getType() + ":-"+sub.upJustice).append(";");
		}
		
		if (sub.upKuafuMoney > 0) {
			buff.append(ConditionType.TYPE_KUAFU_MONEY.getType() + ":-" + sub.upKuafuMoney).append(";");
		}
		
		if (sub.upTeamMoney > 0) {
			buff.append(ConditionType.TYPE_TEAM_MONEY.getType() + ":-" + sub.upTeamMoney).append(";");
		}
		
		if (sub.upExploit > 0) {
			buff.append(ConditionType.TYPE_EXPLOIT.getType()+ ":-" + sub.upExploit).append(";");
		}
		
		if (sub.clubContribution > 0) {
			buff.append(ConditionType.TYPE_CLUB_CONTRIBUTION.getType()+ ":-" + sub.clubContribution).append(";");
		}
		
		if (sub.starMoney > 0) {
			buff.append(ConditionType.TYPE_STAR_MONEY.getType()+ ":-" + sub.starMoney).append(";");
		}
		
		if(buff.toString().endsWith(";")){
			return buff.substring(0, buff.length() - 1).toString();
		}
		
		return buff.toString();
	}
	
	/**
	 * 计算所有公会等级战斗力
	 * @return
	 */
	public static List<ClubFightInfo> calcClubFight(){
		List<ClubFightInfo> list = new ArrayList<ClubFightInfo>();
		
		Map<Integer, RoleClubMemberInfo>  memberMap = null;
		RoleClubInfo clubInfo = null;
		RoleInfo roleInfo = null;
		ClubFightInfo fightInfo = null;
		long totalFight = 0;
		for(Integer clubId : RoleClubInfoMap.getAllClub().keySet()){
			clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			if(clubInfo == null){
				continue;
			}
			
			memberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(clubId);
			
			for(Integer roleId : memberMap.keySet()){
				if(memberMap.get(roleId) == null || memberMap.get(roleId).getStatus() == 99){
					continue;
				}
				
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if(roleInfo == null){
					continue;
				}
				
				totalFight += roleInfo.getFightValue();
				
			}
			
			fightInfo = new ClubFightInfo();
			fightInfo.setClubId(clubId);
			fightInfo.setClubName(clubInfo.getClubName());
			fightInfo.setImageId(clubInfo.getImageId());
			fightInfo.setLevel(clubInfo.getLevel());
			fightInfo.setTotalFight(totalFight);
			
			list.add(fightInfo);
			
			//重置战斗力
			totalFight = 0;
		}
		
		Collections.sort(list, new Comparator<ClubFightInfo>() {

			@Override
			public int compare(ClubFightInfo o1, ClubFightInfo o2) {
				if(o1.getTotalFight() > o2.getTotalFight()){
					return -1;
				}else if(o1.getTotalFight() < o2.getTotalFight()){
					return 11;
				}
				return 0;
			}
		});
		
		return list;
	}
	
	/**
	 * 佣兵结算
	 * @param roleInfo
	 * @return
	 */
	public static void hireBalance(RoleInfo roleInfo){
		//佣兵结算
		if(roleInfo.getHireHeroMap().size() > 0){
			HireHeroInfo hireHeroInfo = null;
			HeroInfo heroInfo = null;
			int sum = 0;
			for(Integer heroId : roleInfo.getHireHeroMap().keySet()){
				hireHeroInfo = roleInfo.getHireHeroMap().get(heroId);
				if(hireHeroInfo == null){
					continue;
				}
				
				heroInfo = roleInfo.getHeroMap().get(heroId);
				
				synchronized(hireHeroInfo){
					sum += HireHeroService.getHireHeroIncome(roleInfo, heroInfo, hireHeroInfo, false); //守营收入
					sum += hireHeroInfo.getHireMoneySum(); //雇佣收入
				}
				
			}
			
			if(sum > 0){
				//发送邮件领取
				MailAttachment att = null;
				List<MailAttachment> attachments = new ArrayList<MailAttachment>();
				att = new MailAttachment(ConditionType.TYPE_MONEY.getName(), sum, 1, 0);
				attachments.add(att);
				
				String attachment = MailDAO.encoderAttachment(attachments);
				if (attachment.length() > 0) {
					
					String title = Resource.getMessage("game", "HIRE_HERO_PRIZE_PUSH_TITLE");
					String content = Resource.getMessage("game", "HIRE_HERO_PRIZE_PUSH_CONTENT");

					MailService.pushMailPrize(roleInfo.getId() + "", attachment, title, content, "");
				}
			}
			
			
//			RoleService.addRoleRoleResource(ActionType.action504.getType(), roleInfo, ConditionType.TYPE_MONEY, sum);
			
			//删除全部佣兵
			if(ClubHireHeroDao.getInstance().deleteHireHeroInfoByRoleId(roleInfo.getId())){
				roleInfo.getHireHeroMap().clear();
			}
		}
	}
	
}
