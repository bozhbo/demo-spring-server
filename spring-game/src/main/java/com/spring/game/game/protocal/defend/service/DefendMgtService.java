package com.snail.webgame.game.protocal.defend.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.DefendFightLog;
import com.snail.webgame.game.protocal.defend.query.QueryDefendResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.xml.cache.DefendXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DefendXMLInfo;
import com.snail.webgame.game.xml.info.DefendXMLInfo.DefendFightXMLInfo;
import com.snail.webgame.game.xml.info.DropXMLInfo;

/**
 * 防守玩法
 * @author wanglinhui
 *
 */
public class DefendMgtService {
	/**
	 * 查询防守玩法信息
	 * @param roleId
	 * @return
	 */
	public QueryDefendResp queryDefend(int roleId){
		
		QueryDefendResp resp = new QueryDefendResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.DEFEND_QUERY_ERROR_1);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null)
		{
			resp.setResult(ErrorCode.DEFEND_QUERY_ERROR_1);
			return resp;
		}
		if(GameValue.DEFEND_SWITCH!=1){//功能开发
			resp.setResult(ErrorCode.DEFEND_QUERY_ERROR_2);
			return resp;
		}
		int num = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.BLJDB_NUM);
		if(roleLoadInfo.getLastDefendTime()==null){
			resp.setRemainTime((byte)num);
			resp.setResult(1);
			return resp;
		}
		if(!DateUtil.isSameDay(roleLoadInfo.getLastDefendTime().getTime(), System.currentTimeMillis())&&roleLoadInfo.getDefendTime()>0){
			//隔天了并且有玩的次数就重置玩的次数
			synchronized (roleInfo) {
				if(RoleDAO.getInstance().updateRoleDefendTime(roleId, (byte)0, roleLoadInfo.getLastDefendTime())){
					roleLoadInfo.setDefendTime((byte)0);
				}else{
					resp.setResult(ErrorCode.DEFEND_QUERY_ERROR_3);
					return resp;
				}
			}
		}
		resp.setRemainTime((byte)(num - roleLoadInfo.getDefendTime()));
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 处理防守玩法战斗开始
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealStartFight(RoleInfo roleInfo, FightInfo fightInfo){
		
		//判断背包
		int itemCheck = ItemService.addItemAndEquipCheck(roleInfo);
		if(itemCheck!=1){
			return itemCheck;
		}
		DefendXMLInfo defendXMLInfo = DefendXMLInfoMap.getDefendXMLInfo(Integer.parseInt(fightInfo.getDefendStr()));
		if(defendXMLInfo == null){
			return ErrorCode.DEFEND_ERROR_1;
		}
		if(HeroInfoMap.getMainHeroLv(roleInfo.getId())<defendXMLInfo.getNeedLevel()){//判断进入等级
			return ErrorCode.DEFEND_ERROR_2;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo==null){
			return ErrorCode.DEFEND_ERROR_3;
		}
		if(roleLoadInfo.getLastDefendTime()!=null){//没玩过防守不用判断
			if(!DateUtil.isSameDay(roleLoadInfo.getLastDefendTime().getTime(), System.currentTimeMillis())&&roleLoadInfo.getDefendTime()>0){
				//隔天了并且有玩的次数就重置玩的次数(防止打开界面的时候刚好跨天了)
				if(RoleDAO.getInstance().updateRoleDefendTime(roleInfo.getId(), (byte)0, roleLoadInfo.getLastDefendTime())){
					roleLoadInfo.setDefendTime((byte)0);
				}else{
					return ErrorCode.DEFEND_ERROR_8;
				}
			}
			int num = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.BLJDB_NUM);
			if(roleLoadInfo.getDefendTime() >= num &&
					DateUtil.isSameDay(roleLoadInfo.getLastDefendTime().getTime(), System.currentTimeMillis())){//判断每日次数
				return ErrorCode.DEFEND_ERROR_4;
			}
		}
		Random random = new Random();
		int randomInt = random.nextInt(defendXMLInfo.getDefendFightList().size());
		fightInfo.setStartRespDefendStr(fightInfo.getDefendStr()+","+(defendXMLInfo.getDefendFightList().get(randomInt).getNo()));//难度编号,npc编号
		return 1;
	}
	
	/**
	 * 处理防守玩法战斗结束
	 * @param fightResult
	 * @param reserve 打过的最后一波编号
	 * @param roleInfo
	 * @param prizeList
	 * @param fightInfo
	 * @return
	 */
	public static int dealFightEnd(int fightResult,String reserve, RoleInfo roleInfo, FightInfo fightInfo, List<BattlePrize> prizeList) {

		int difficulty = Integer.parseInt(fightInfo.getStartRespDefendStr().split(",")[0]); //难度编号
		int defendFightNo = Integer.parseInt(fightInfo.getStartRespDefendStr().split(",")[1]); //NPC编号
		int defendLastFightPointNo = Integer.parseInt(reserve);
		DefendXMLInfo defendXMLInfo = DefendXMLInfoMap.getDefendXMLInfo(difficulty);
		if (defendXMLInfo == null) {
			return ErrorCode.DEFEND_ERROR_5;
		}
		DefendFightXMLInfo defendFightXMLInfo = null;
		for (int i = 0; i < defendXMLInfo.getDefendFightList().size(); i++) {
			if (defendXMLInfo.getDefendFightList().get(i).getNo() == defendFightNo) {
				defendFightXMLInfo = defendXMLInfo.getDefendFightList().get(i);
				break;
			}
		}
		if (defendFightXMLInfo == null) {
			return ErrorCode.DEFEND_ERROR_5;
		}
		String dropbagStr = defendFightXMLInfo.getDropBagNo().get(defendLastFightPointNo);
		if (dropbagStr == null) {
			return ErrorCode.DEFEND_ERROR_5;
		}

		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.DEFEND_ERROR_6;
		}
		// 扣次数和记录玩的时间
		// 先判断次数是否足够
		int num = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.BLJDB_NUM);
		if (roleLoadInfo.getDefendTime() >= num) {
			return ErrorCode.DEFEND_ERROR_9;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (RoleDAO.getInstance().updateRoleDefendTime(roleInfo.getId(), (byte) (roleLoadInfo.getDefendTime() + 1), now)) {
			roleLoadInfo.setDefendTime((byte) (roleLoadInfo.getDefendTime() + 1));
			roleLoadInfo.setLastDefendTime(now);
		} else {
			return ErrorCode.DEFEND_ERROR_7;
		}
		if (dropbagStr != null) {
			// 胜利则发送奖励
			List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLList(dropbagStr);
			if (list != null) {

				int check = ItemService.addPrizeForPropBag(ActionType.action370.getType(), roleInfo, list, null,
						prizeList, null, null, null, false);

				if (check != 1) {
					return check;
				}
			}
		}

		// 任务检测
		boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action370.getType(), null, true, false);
		boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
				GameValue.RED_POINT_STONE_COMPOSE);
		// 红点推送
		if (isRedPointQuest || isRedPointMonth) {
			RedPointMgtService.pop(roleInfo.getId());
		}
		
		//计入防守玩法日志
		DefendFightLog defendLog = new DefendFightLog();
		defendLog.setAccount(roleInfo.getAccount());
		defendLog.setRoleName(roleInfo.getRoleName());
		defendLog.setRoleId(roleInfo.getId());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo != null)
		{
			defendLog.setMainHeroId(heroInfo.getHeroNo());
		}
		defendLog.setFightLv(difficulty);
		defendLog.setBeginTime(new Timestamp(fightInfo.getFightTime()));
		defendLog.setEndTime(new Timestamp(System.currentTimeMillis()));
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
		if(pos != null && pos.length() > 0)
		{
			defendLog.setPos(pos.substring(0, pos.length()-1));
		}
		
		String drop = "";
		if(prizeList != null && prizeList.size() > 0)
		{
			for(BattlePrize prize : prizeList)
			{
				drop = drop + prize.getNo() + "," + prize.getNum() +";";
			}
		}
		if(drop != null && drop.length() > 0)
		{
			defendLog.setDrop(drop.substring(0, drop.length()-1));
		}
		defendLog.setBattleResult(fightResult);
		GameLogService.insertDefendLog(defendLog);
		
		return 1;
	}

}
