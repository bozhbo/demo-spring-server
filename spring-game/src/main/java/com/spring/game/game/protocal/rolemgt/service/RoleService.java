package com.snail.webgame.game.protocal.rolemgt.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import org.apache.mina.common.IoSession;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.WordListMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.util.EmojiFilterUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.AbstractConditionCheck.BaseSubResource;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleClubInfoDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.rolemgt.loginqueue.LoginQueueResp;
import com.snail.webgame.game.protocal.rolemgt.worldChatLimit.WorldChatLimitReq;

public class RoleService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 加载用户数据
	 * @param roleInfo
	 * @return
	 */
	public static boolean loadRoleOtherData(RoleInfo roleInfo,SqlMapClient client) {
		return RoleDAO.getInstance().roleLoadOtherInfo(roleInfo,client);
		
	}

	/**
	 * 计算role
	 * @param roleInfo
	 */
	public static void recalRoleInfo(RoleInfo roleInfo) {
		
	}

	/**
	 * 判断是否能登录
	 * @param flag 0-重置登录数量 1-登录
	 * @return
	 */
	public synchronized static boolean isCanLogin(int flag) {
		if (flag == 0) {
			GameValue.LOGIN_FREQ_NUM = 0;
			return true;
		} else if (flag == 1) {
			if (GameConfig.getInstance().getLoginFreqNum() > 0 && GameConfig.getInstance().getLoginFreq() > 100) {
				if (GameValue.LOGIN_FREQ_NUM >= GameConfig.getInstance().getLoginFreqNum()) {
					return false;
				} else {
					GameValue.LOGIN_FREQ_NUM = GameValue.LOGIN_FREQ_NUM + 1;
					return true;
				}
			} else {
				return true;
			}

		} else {
			return false;
		}

	}

	/**
	 * 获取用户角色数量
	 * @param account
	 * @return
	 */
	public static boolean checkRoleAccountNum(String account) {
		HashMap<Integer, RoleInfo> accMap = RoleInfoMap.getRoleMapByAccount(account);
		if (accMap != null && accMap.size() >= HeroXMLInfoMap.getInitHeroNum()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检测玩家是否选过相同类型的角色
	 * @param account
	 * @param heroNo
	 * @return
	 */
	public static boolean checkRoleInitHero(String account,int heroNo)
	{
		HashMap<Integer, RoleInfo> accMap = RoleInfoMap.getRoleMapByAccount(account);
		if(accMap == null)
		{
			return true;
		}
		for(int roleId : accMap.keySet())
		{
			RoleInfo roleInfo = accMap.get(roleId);
			if(roleInfo == null)
			{
				return false;
			}
			
			HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(mainHeroInfo == null)
			{
				return false;
			}
			
			if(mainHeroInfo.getHeroNo() == heroNo)
			{
				return false;
			}
					
		}
		
		return true;
	}

	/**
	 * 检测角色名字
	 * @param name
	 * @return
	 */
	public static int checkRoleName(String roleName) {
		if (roleName == null || roleName.trim().length() == 0) {
			return ErrorCode.ROLE_NAME_ERROR_1;
		}
		try {
			if (roleName.trim().getBytes("gbk").length > GameValue.MAX_NAME_LENGTH * 2) {
				return ErrorCode.ROLE_NAME_ERROR_4;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("---------intput error Name="+roleName);
			return ErrorCode.ROLE_NAME_ERROR_2;
		}
		if (!checkName(roleName)) {
			return ErrorCode.ROLE_NAME_ERROR_2;
		}
		RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(roleName);
		if (roleInfo != null) {
			return ErrorCode.ROLE_NAME_ERROR_3;
		}
		return 1;
	}

	/**
	 * 检查名字是否符合规则
	 * @param name
	 * @return
	 */
	public static boolean checkName(String name) {
		// 空判断
		if (name == null || name.trim().length() <= 0) {
			return false;
		}		
		Document doc = null;
		try {
			// 验证名称为乱码，(例:
			// 玩家有英雄名字为"花’落無痕"、"’雲隨風︶ㄣ"，此号长期未上线被暂时删除，英雄信息被保存为XML格式存入ROLE_REBACK_LOG表，
			// 当此玩家上线恢复角色时，把ROLE_REBACK_LOG表里英雄信息XML字符串转换成Document对象时异常，导致英雄丢失。)
			doc = DocumentHelper.parseText("<name>" + name + "</name>");
		} catch (DocumentException e) {
			logger.error("---------intput error Name="+name);
			//e.printStackTrace();
			return false;
		}

		// 固定特殊字符检测
		if (doc == null || name.indexOf(":") != -1 || name.indexOf(";") != -1 || name.indexOf("*") != -1 || name.indexOf("_") != -1 
				|| name.indexOf("\\") != -1 
				|| name.indexOf(",") != -1 || name.indexOf("@") != -1 || name.indexOf("\"") != -1
				|| name.indexOf("'") != -1 || name.indexOf("#") != -1 || name.indexOf("<") != -1
				|| name.indexOf(">") != -1 || name.indexOf("&") != -1 || name.indexOf(" ") != -1
				|| name.indexOf("　") != -1 || name.indexOf("【") != -1 || name.indexOf("Δ") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("１") != -1 || name.indexOf("２") != -1
				|| name.indexOf("３") != -1 || name.indexOf("４") != -1 || name.indexOf("５") != -1
				|| name.indexOf("６") != -1 || name.indexOf("７") != -1 || name.indexOf("８") != -1
				|| name.indexOf("９") != -1 || name.indexOf("０") != -1 || name.indexOf("Ａ") != -1
				|| name.indexOf("Ｂ") != -1 || name.indexOf("Ｃ") != -1 || name.indexOf("Ｄ") != -1
				|| name.indexOf("Ｅ") != -1 || name.indexOf("Ｆ") != -1 || name.indexOf("Ｇ") != -1
				|| name.indexOf("Ｈ") != -1 || name.indexOf("Ｉ") != -1 || name.indexOf("Ｊ") != -1
				|| name.indexOf("Ｋ") != -1 || name.indexOf("Ｌ") != -1 || name.indexOf("Ｍ") != -1
				|| name.indexOf("Ｎ") != -1 || name.indexOf("Ｏ") != -1 || name.indexOf("Ｐ") != -1
				|| name.indexOf("Ｑ") != -1 || name.indexOf("Ｒ") != -1 || name.indexOf("Ｓ") != -1
				|| name.indexOf("Ｔ") != -1 || name.indexOf("Ｕ") != -1 || name.indexOf("Ｖ") != -1
				|| name.indexOf("Ｗ") != -1 || name.indexOf("Ｘ") != -1 || name.indexOf("Ｙ") != -1
				|| name.indexOf("Ｚ") != -1 || name.indexOf("ａ") != -1 || name.indexOf("ｂ") != -1
				|| name.indexOf("ｃ") != -1 || name.indexOf("ｄ") != -1 || name.indexOf("ｅ") != -1
				|| name.indexOf("ｆ") != -1 || name.indexOf("ｇ") != -1 || name.indexOf("ｈ") != -1
				|| name.indexOf("ｉ") != -1 || name.indexOf("ｊ") != -1 || name.indexOf("ｋ") != -1
				|| name.indexOf("ｌ") != -1 || name.indexOf("ｍ") != -1 || name.indexOf("ｎ") != -1
				|| name.indexOf("ｏ") != -1 || name.indexOf("ｐ") != -1 || name.indexOf("ｑ") != -1
				|| name.indexOf("ｒ") != -1 || name.indexOf("ｓ") != -1 || name.indexOf("ｔ") != -1
				|| name.indexOf("ｕ") != -1 || name.indexOf("ｖ") != -1 || name.indexOf("ｗ") != -1
				|| name.indexOf("ｘ") != -1 || name.indexOf("ｙ") != -1 || name.indexOf("ｚ") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1 || name.indexOf("") != -1 || name.indexOf("") != -1
				|| name.indexOf("") != -1) {
			return false;
		}
		
		
		// 特殊字符过滤
		if(!CommonUtil.isCanIn(name)){
			return false;
		}
		
		// 检测是否有emoji字符
		if(EmojiFilterUtil.containsEmoji(name)){
			return false;
		}

		// 屏蔽各种符号避免程序其它地方用该符号组合角色名称字符串（例:运营工具发邮件已出现过问题）
		if (WordListMap.isExistWord(name, GameConfig.getInstance().getWorldType())) {
			return false;
		}

		return true;
	}

	/**
	 * 判断玩家离线再等录时间是否超过规定时间
	 * @param lastLogoutTime
	 * @return
	 */
	public static boolean isLogoutTimeOut(long lastLogoutTime) {
		boolean flag = false;
		// 判断玩家下线时间是否超过6分钟,没超过6分钟则不管是否到达最大在线人数都让其登陆游戏

		long currTime = System.currentTimeMillis(); // 当前时间
		if (currTime - lastLogoutTime >= GameValue.USER_LOGOUT_TIME * 1000)// 超过600秒加入排队,小于600秒不进行最大人数判断
		{
			flag = true;
		}

		return flag;

	}

	/**
	 * 添加排队队列
	 * @param roleId
	 * @param req
	 * @param sequenceId
	 * @param head
	 * @param roleName
	 * @return
	 */
	public static LoginQueueResp getLoginQueueResp(String account) {

		int index = RoleLoginQueueInfoMap.getIndex(account);
		LoginQueueResp queueResp = new LoginQueueResp();
		queueResp.setResult(1);
		queueResp.setAccount(account);
		queueResp.setIndex(index);
		queueResp.setNum(RoleLoginQueueInfoMap.getListSize());
		queueResp.setFlag(0);

		return queueResp;
	}

	/**
	 * 更新购买次数
	 * @param roleId
	 * @param resource_type
	 * @param buyNum
	 * @param buyTime
	 * @return
	 */
	public static int updateRoleResourceBuyNum(RoleInfo roleInfo, ConditionType resource_type, int buyNum, Timestamp buyTime) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.ROLE_NOT_EXIST_ERROR_27;
		}
		switch (resource_type) {
		case TYPE_MONEY:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyMoneyNum(buyNum);
				roleLoadInfo.setLastBuyMoneyTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_16;
			}
			break;
		case TYPE_SP:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuySpNum((short) buyNum);
				roleLoadInfo.setLastBuySpTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_19;
			}
			break;
		case TYPE_COURAGE:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyCourageNum((byte) buyNum);
				roleLoadInfo.setLastBuyCourageTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_20;
			}
			break;
		case TYPE_JUSTICE:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyJusticeNum(buyNum);
				roleLoadInfo.setLastBuyJusticeTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_21;
			}
			break;
//		case TYPE_DEVOTE:
//			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
//				roleLoadInfo.setBuyDevoteNum(buyNum);
//				roleLoadInfo.setLastBuyDevoteTime(buyTime);
//			} else {
//				return ErrorCode.EQUIP_MERGE_ERROR_22;
//			}
//			break;
		case TYPE_NORMAL_SHOP_FRE:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyNormalNum(buyNum);
				roleLoadInfo.setLastBuyNormalTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_23;
			}
		case TYPE_KUAFU_SHOP_FRE:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyKuafuNum(buyNum);
				roleLoadInfo.setLastBuyKuafuTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_24;
			}
			break;
		case TYPE_EXPLOIT_SHOP_FRE:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyExploitShopNum(buyNum);
				roleLoadInfo.setLastBuyExploitTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_25;
			}
		case TYPE_GOLD_SHOP_REF:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyGoldShopNum(buyNum);
				roleLoadInfo.setLastBuyGoldShopTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_26;
			}
			break;
		case TYPE_ENERGY:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyEnergyNum((short) buyNum);
				roleLoadInfo.setLastBuyEnergyTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_32;
			}
			break;
		case TYPE_EQUIP:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyEquipShopNum((short) buyNum);
				roleLoadInfo.setLastBuyEquipTime(buyTime);
			} else {
				return ErrorCode.EQUIP_SHOP_ERROR_1;
			}
			break;
		case TYPE_TECH:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyTechNum((short) buyNum);
				roleLoadInfo.setLastBuyTechTime(buyTime);
			} else {
				return ErrorCode.EQUIP_SHOP_ERROR_1;
			}
			break;
		case TYPE_PVP_3_SHOP_REF:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setPvp3ShopBuyNum((short) buyNum);
				roleLoadInfo.setPvp3ShopLastBuyTime(buyTime);
			} else {
				return ErrorCode.EQUIP_SHOP_ERROR_1;
			}
			break;
		case TYPE_TEAM_SHOP_FRE:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setTeamShopBuyNum((short) buyNum);
				roleLoadInfo.setTeamShopLastBuyTime(buyTime);
			} else {
				return ErrorCode.EQUIP_SHOP_ERROR_1;
			}
			break;
		case TYPE_TURK_SHOP_REF:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyTurkShopNum(buyNum);
				roleLoadInfo.setLastBuyTurkShopTime(buyTime);
			} else {
				return ErrorCode.EQUIP_MERGE_ERROR_26;
			}
			break;
		case TYPE_CLUB_CONTRIBUTION:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setBuyDevoteNum(buyNum);
				roleLoadInfo.setLastBuyDevoteTime(buyTime);
			} else {
				return ErrorCode.ROLE_CLUB_ERROR_30;
			}
			break;
		case TYPE_STAR_MONEY:
			if (RoleDAO.getInstance().updateRoleResourceBuyNum(roleInfo.getId(), resource_type, buyNum, buyTime)) {
				roleLoadInfo.setLastBuyStarTime(buyTime);
				roleLoadInfo.setBuyStarShopNum(buyNum);
			} else {
				return ErrorCode.ROLE_CLUB_ERROR_59;
			}
			break;
		default:
			return ErrorCode.EQUIP_MERGE_ERROR_28;
		}
		return 1;
	}

	/**
	 * 扣去消耗资源
	 * @param roleInfo
	 * @param conds
	 * @param drop 当消耗资源获得物品时，传入DropInfo对象，如果没有则置空
	 * @return
	 */
	public static boolean subRoleResource(int action, RoleInfo roleInfo, List<AbstractConditionCheck> conds , DropInfo drop) {
		BaseSubResource sub = AbstractConditionCheck.subCondition(conds);
		if (sub == null) {
			return true;
		}

		if (sub.upMoney > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_MONEY, sub.upMoney, false , drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_MONEY.getName() + ",num=" + sub.upMoney
						+ ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}

		if (sub.upCoin > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_COIN, sub.upCoin, false , drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_COIN.getName() + ",num=" + sub.upCoin
						+ ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		if (sub.upSp > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_SP, sub.upSp, false , drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_SP.getName() + ",num=" + sub.upSp
						+ ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		if (sub.upEnergy > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_ENERGY, sub.upEnergy, false , drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_ENERGY.getName() + ",num=" + sub.upEnergy
						+ ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		if (sub.upCourage > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_COURAGE, sub.upCourage, false , drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_COURAGE.getName() + ",num=" + sub.upCourage
						+ ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
			return true;
		}
		if (sub.upJustice > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_JUSTICE, sub.upJustice, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_JUSTICE.getName() + ",num=" + sub.upJustice
						+ ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}
//		if (sub.upDevote > 0) {
//			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_DEVOTE, sub.upDevote, false)) {
//				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_DEVOTE.getName() + ",num=" + sub.upDevote
//						+ ",roleId = " + roleInfo.getId() + "!!");
//				return false;
//			}
//		}
		if (sub.upKuafuMoney > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_KUAFU_MONEY, sub.upKuafuMoney, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_KUAFU_MONEY.getName() + ",num="
						+ sub.upKuafuMoney + ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		if (sub.upTeamMoney > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_TEAM_MONEY, sub.upTeamMoney, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_TEAM_MONEY.getName() + ",num="
						+ sub.upTeamMoney + ",roleId = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		if (sub.upExploit > 0) {
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_EXPLOIT, sub.upExploit, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_EXPLOIT.getName() + ",num=" + sub.upExploit
						+ ",upExploit = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		
		if(sub.upEquip > 0){
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_EQUIP, sub.upEquip, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_EQUIP.getName() + ",num=" + sub.upEquip
						+ ",upEquip = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		
		if(sub.upTech > 0){
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_TECH, sub.upTech, false,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_TECH.getName() + ",num=" + sub.upTech
						+ ",upTech = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		
		if(sub.pvp3Money > 0){
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_PVP_3_MONEY, sub.pvp3Money, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_PVP_3_MONEY.getName() + ",num=" + sub.pvp3Money
						+ ",pvp3Money = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		
		if(sub.clubContribution > 0){
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_CLUB_CONTRIBUTION, sub.clubContribution, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_CLUB_CONTRIBUTION.getName() + ",num=" + sub.clubContribution
						+ ",clubContribution = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		
		if(sub.starMoney > 0){
			if (!chgRoleResource(action, roleInfo, ConditionType.TYPE_STAR_MONEY, sub.starMoney, false ,drop)) {
				logger.error("Sub role Resource error!action=" + action + ", type:" + ConditionType.TYPE_STAR_MONEY.getName() + ",num=" + sub.starMoney
						+ ",starMoney = " + roleInfo.getId() + "!!");
				return false;
			}
		}
		
		if(sub.clubBuild > 0){
			//公会的建设度(在公会中，不是角色身上)
			RoleClubInfo clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
			if(clubInfo == null){
				logger.error("Sub ClubBuild Resource error!action=" + action + ", type:" + ConditionType.TYPE_CLUB_BUILD.getName() + ",num=" + sub.clubBuild
						+ ",clubBuild = " + roleInfo.getId() + "!!");
				return false;
			}
			
			long surplus = clubInfo.getBuild() - sub.clubBuild;
			if(surplus < 0){
				logger.error("Sub RoleClubInfo Resource error!action=" + action + ", type:" + ConditionType.TYPE_CLUB_BUILD.getName() + ",num=" + sub.clubBuild
						+ ",clubBuild = " + roleInfo.getId() + "!!");
				return false;
			}
			
			if(!RoleClubInfoDao.getInstance().updateRoleClubInfoBuildAndLevel(clubInfo.getId(), (int)surplus, clubInfo.getLevel())){
				logger.error("Sub RoleClubInfo Resource error!action=" + action + ", type:" + ConditionType.TYPE_CLUB_BUILD.getName() + ",num=" + sub.clubBuild
						+ ",clubBuild = " + roleInfo.getId() + "!!");
				return false;
			}
			
			clubInfo.setBuild((int)surplus);
			
			// 日志
			GameLogService.insertMoneyLog(roleInfo, action, ConditionType.TYPE_CLUB_BUILD, 1, (int) clubInfo.getBuild(), (int) surplus , drop);
		}

		return true;
	}

	/**
	 * 改变玩家资源
	 * 
	 * @param roleInfo
	 * @param type
	 * @param value
	 * @param add
	 * @return
	 */
	private static boolean chgRoleResource(int action, RoleInfo roleInfo, ConditionType type, long value, boolean add , DropInfo drop) {

		if (value <= 0) {
			return false;
		}

		if (add) {
			return addRoleRoleResource(action, roleInfo, type, value,drop);
		}
		return subRoleRoleResource(action, roleInfo, type, value,drop);
	}

	/**
	 * 增加资源,经验不属于资源类
	 * 
	 * @param roleInfo
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean addRoleRoleResource(int action, RoleInfo roleInfo, ConditionType type, long value,DropInfo drop) {
      	RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (type != ConditionType.TYPE_COIN) {
			if (roleLoadInfo == null) {
				return false;
			}
		}
		//添加的不能为负
		if(value < 0)
		{
			logger.info("#####you add resour less than 0,account="+roleInfo.getAccount()+",roleId="+roleInfo.getId()
					+",action="+action+",type="+type.getType()+",value="+value);
			return false;
		}
		long before = 0;
		Timestamp lastRecoverTime = null;
		switch (type) {
		case TYPE_MONEY:
			before = roleInfo.getMoney();
			long money = roleInfo.getMoney() + value;
			if(money > GameValue.MONEY_MAX){
				logger.info("####you money is too much===,account="+roleInfo.getAccount()+",roleId="+roleInfo.getId()
						+",money="+money+",action="+action+",addValue="+value+",source="+type.getType());
				money = GameValue.MONEY_MAX;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, money, null,"")) {
				roleInfo.setMoney(money);
				// 日志
				if(action != ActionType.action41.getType() && action != ActionType.action362.getType())
				{
					GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) money , null);
				}
				return true;
			}
			break;
		case TYPE_COIN:
			before = roleInfo.getCoin();
			long coin = roleInfo.getCoin() + value;
			if(coin > GameValue.GOLD_MAX){
				logger.info("####you money is too much===,account="+roleInfo.getAccount()+",roleId="+roleInfo.getId()
						+",coin="+coin+",action="+action+",addValue="+value+",source="+type.getType());
				coin = GameValue.GOLD_MAX;
			}
			long totalCoin = roleInfo.getTotalCoin() + coin - before;
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, coin, null,"" + totalCoin)) {
				roleInfo.setCoin(coin);
				roleInfo.setTotalCoin(totalCoin);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) coin , drop);
				
				return true;
			}
			break;
		case TYPE_SP:
			// 体力自动回复(加时自动回复，扣时检测自动回复)
			timerRecoverSp(roleInfo);
			before = roleInfo.getSp();
			int sp = roleInfo.getSp() + (int) value;			
			// 体力获取无上限（自动恢复除外）
			if (sp > GameValue.ROLE_SP_MAX) {
				sp = GameValue.ROLE_SP_MAX;
			}
			lastRecoverTime = roleInfo.getLastRecoverSPTime(); 
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, sp, lastRecoverTime,"")) {
				roleInfo.setSp((short) sp);
				roleInfo.setLastRecoverSPTime(lastRecoverTime);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) sp , null );
				return true;
			}
			break;
		case TYPE_ENERGY:
			// 精力自动回复(加时自动回复，扣时检测自动回复)
			timerRecoverEnergy(roleInfo);
			before = roleInfo.getEnergy();
			int energy = roleInfo.getEnergy() + (int) value;
			// 精力获取无上限（自动恢复除外）
			if (energy > GameValue.ROLE_ENERGY_MAX) {
				energy = GameValue.ROLE_ENERGY_MAX;
			}
			lastRecoverTime = roleInfo.getLastRecoverEnergyTime();
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, energy, lastRecoverTime,"")) {
				roleInfo.setEnergy((short) energy);
				roleInfo.setLastRecoverEnergyTime(lastRecoverTime);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) energy , null);
				return true;
			}
			break;
		case TYPE_COURAGE:
			before = roleLoadInfo.getCourage();
			long courage = roleLoadInfo.getCourage() + value;
			courage = courage < 0 ? 0 : courage;
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, courage, null,"")) {
				roleLoadInfo.setCourage(courage);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) courage , null);
				return true;
			}
			break;
		case TYPE_JUSTICE:
			before = roleLoadInfo.getJustice();
			long justice = roleLoadInfo.getJustice() + value;
			justice = justice < 0 ? 0 : justice;
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, justice, null,"")) {
				roleLoadInfo.setJustice(justice);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) justice , null);
				return true;
			}
			break;
//		case TYPE_DEVOTE:
//			before = roleLoadInfo.getDevotePoint();
//			long devote = roleLoadInfo.getDevotePoint() + value;
//			devote = devote < 0 ? 0 : devote;
//			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, devote, null,"")) {
//				roleLoadInfo.setDevotePoint(devote);
//				// 日志
//				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) devote);
//				return true;
//			}
//			break;
		case TYPE_KUAFU_MONEY:
			before = roleLoadInfo.getKuafuMoney();
			if (value > 0) {
				int after = roleLoadInfo.getKuafuMoney() + (int) value;
				after = after < 0 ? Integer.MAX_VALUE : after;
				if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, after, null,"")) {
					roleLoadInfo.setKuafuMoney(after);
					// 日志
					GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) after , null);
					return true;
				}
			}
			break;
		case TYPE_TEAM_MONEY:
			before = roleLoadInfo.getTeamMoney();
			long afterTeamMoney = roleLoadInfo.getTeamMoney() + value;
			if (afterTeamMoney > Integer.MAX_VALUE) {
				afterTeamMoney = Integer.MAX_VALUE;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, afterTeamMoney, null,"")) {
				roleLoadInfo.setTeamMoney((int)afterTeamMoney);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) afterTeamMoney , null);
				return true;
			}
			break;
		case TYPE_EXPLOIT:
			int exploit = roleLoadInfo.getExploit();
			int afterExploit = roleLoadInfo.getExploit() + (int) value;

			int beforeHisExploit = roleLoadInfo.getHisExploit();
			int afterHisExploit = beforeHisExploit + (int) value;
			afterExploit = afterExploit < 0 ? 0 : afterExploit;
			afterHisExploit = afterHisExploit < 0 ? 0 : afterHisExploit;
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, afterExploit, null,afterHisExploit+"")) {
				roleLoadInfo.setExploit(afterExploit);
				roleLoadInfo.setHisExploit(afterHisExploit);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, exploit, afterExploit , null);
				return true;
			}
			break;
		case TYPE_EQUIP:
			before = roleInfo.getRoleLoadInfo().getEquip();
			value = before + value > GameValue.GOLD_MAX ? GameValue.GOLD_MAX : before + value;
			
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null,"")) {
				roleInfo.getRoleLoadInfo().setEquip(value);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) value , null);
				return true;
			}
			break;
		case TYPE_TECH:
			// 体力自动回复(加时自动回复，扣时检测自动回复)
			timerRecoverTech(roleInfo);
			before = roleInfo.getTech();
			int tech = roleInfo.getTech() + (int) value;
			// 体力获取无上限（自动恢复除外）
			if (tech > GameValue.ROLE_TECH_MAX) {
				tech = GameValue.ROLE_TECH_MAX;
			}
			lastRecoverTime = roleInfo.getLastRecoverTechTime();
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, tech, lastRecoverTime,"")) {
				roleInfo.setTech((short) tech);
				roleInfo.setLastRecoverTechTime(lastRecoverTime);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) tech ,null);
				return true;
			}
			break;
		case TYPE_PVP_3_MONEY:
			before = roleLoadInfo.getPvp3Money();
			long afterPvp3Money = roleLoadInfo.getPvp3Money() + value;
			// 体力获取无上限（自动恢复除外）
			if (afterPvp3Money > Integer.MAX_VALUE) {
				afterPvp3Money = Integer.MAX_VALUE;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, afterPvp3Money, null,"")) {
				roleLoadInfo.setPvp3Money(afterPvp3Money);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) afterPvp3Money , null);
				return true;
			}
			break;
		case TYPE_CLUB_CONTRIBUTION:
			long sum = roleInfo.getClubContributionSum() + value; //累积获得的贡献值
			before = roleInfo.getClubContribution();
			value = before + value > GameValue.GOLD_MAX ? GameValue.GOLD_MAX : before + value;
			
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null, "" + sum)) {
				roleInfo.setClubContribution((int)value);
				roleInfo.setClubContributionSum(sum);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) value , null);
				return true;
			}
			break;
		case TYPE_STAR_MONEY:
			if(roleInfo.getRoleLoadInfo() == null){
				return false;
			}
			before = roleInfo.getRoleLoadInfo().getStarMoney();
			value = before + value > GameValue.GOLD_MAX ? GameValue.GOLD_MAX : before + value;
			
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null, "")) {
				roleInfo.getRoleLoadInfo().setStarMoney((int)value);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 0, (int) before, (int) value , null);
				return true;
			}
			break;	
		default:
			break;
		}
		return false;
	}

	/**
	 * 扣资源
	 * 
	 * @param roleInfo
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean subRoleRoleResource(int action, RoleInfo roleInfo, ConditionType type, long value,DropInfo drop) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return false;
		}
		long before = 0;
		Timestamp lastRecoverTime = null;
		if(value < 0)
		{
			// 扣除资源不允许小于0
			logger.info("#####you sub resour less than 0,account="+roleInfo.getAccount()+",roleId="+roleInfo.getId()
					+",action="+action+",type="+type.getType()+",value="+value);
			return false;
		}
		
		switch (type) {
		case TYPE_MONEY:
			before = roleInfo.getMoney();
			long money = before - value;
			if(money < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, money, null,"")) {
				roleInfo.setMoney(money);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) money , drop);
				
				return true;
			}
			break;
		case TYPE_COIN:			
			before = roleInfo.getCoin();
			long coin = roleInfo.getCoin() - value;
			if(coin < 0)
			{
				return false;
			}
			
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, coin, null, "")) {
				roleInfo.setCoin(coin);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) coin , drop);
				
				// 累计金币消耗时限活动检测
				OpActivityService.dealOpActProInfoCheck(roleInfo, ActionType.action411.getType(), value, true);
				
				// 精彩活动
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_WONDER, "");
				return true;
			}
			break;
		case TYPE_SP:			
			before = roleInfo.getSp();
			int sp = roleInfo.getSp() - (int) value;
			if(sp < 0)
			{
				return false;
			}
			lastRecoverTime = roleInfo.getLastRecoverSPTime();
			// 体力小于上限重置自动回复时间
			if (before >= roleInfo.getSpRecoverLimit() && sp < roleInfo.getSpRecoverLimit()) {
				lastRecoverTime = new Timestamp(System.currentTimeMillis());
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, sp, lastRecoverTime,"")) {
				roleInfo.setSp((short) sp);
				roleInfo.setLastRecoverSPTime(lastRecoverTime);
				// 时间重置后需要推送最新的体力和时间
				String spStr = roleInfo.getSp() + "," + roleInfo.getLastRecoverSPTime().getTime() + "," + roleLoadInfo.getTodayBuySpNum() + "," + 0;					
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SP, spStr);					
				
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) sp , drop);
				return true;
			}
			break;
		case TYPE_ENERGY:
			before = roleInfo.getEnergy();
			int energy = roleInfo.getEnergy() - (int) value;
			if(energy < 0)
			{
				return false;
			}
			lastRecoverTime = roleInfo.getLastRecoverEnergyTime();
			// 体力小于上限重置自动回复时间
			if (before >= roleInfo.getEnergyRecoverLimit() && energy < roleInfo.getEnergyRecoverLimit()) {
				lastRecoverTime = new Timestamp(System.currentTimeMillis());
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, energy, lastRecoverTime,"")) {
				roleInfo.setEnergy((short) energy);
				roleInfo.setLastRecoverEnergyTime(lastRecoverTime);
				// 时间重置后需要推送最新的体力和时间
				String energyStr = roleInfo.getEnergy() + "," + roleInfo.getLastRecoverEnergyTime().getTime() + "," + roleLoadInfo.getTodayBuyEnergyNum() + "," + 0;					
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_ENERGY, energyStr);					
				
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) energy , drop);
				return true;
			}
			break;
		case TYPE_COURAGE:
			before = roleLoadInfo.getCourage();
			long courage = roleLoadInfo.getCourage() - value;
			if(courage < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, courage, null,"")) {
				roleLoadInfo.setCourage(courage);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) courage ,drop);
				return true;
			}
			break;
		case TYPE_JUSTICE:
			before = roleLoadInfo.getJustice();
			long justice = roleLoadInfo.getJustice() - value;
			if(justice < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, justice, null,"")) {
				roleLoadInfo.setJustice(justice);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) justice ,drop);
				return true;
			}
			break;
//		case TYPE_DEVOTE:
//			before = roleLoadInfo.getDevotePoint();
//			long devote = roleLoadInfo.getDevotePoint() - value;
//			devote = devote < 0 ? 0 : devote;
//			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, devote, null,"")) {
//				roleLoadInfo.setDevotePoint(devote);
//				// 日志
//				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) devote);
//				return true;
//			}
//			break;
		case TYPE_KUAFU_MONEY:
			before = roleLoadInfo.getKuafuMoney();
			int kuafuMoney = roleLoadInfo.getKuafuMoney() - (int) value;
			if(kuafuMoney < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, kuafuMoney, null,"")) {
				roleLoadInfo.setKuafuMoney(kuafuMoney);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) kuafuMoney , drop);
				return true;
			}
			break;
		case TYPE_TEAM_MONEY:
			before = roleLoadInfo.getTeamMoney();
			int teamMoney = roleLoadInfo.getTeamMoney() - (int) value;
			if(teamMoney < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, teamMoney, null,"")) {
				roleLoadInfo.setTeamMoney(teamMoney);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) teamMoney , drop);
				return true;
			}
			break;
		case TYPE_EXPLOIT:
			before = roleLoadInfo.getExploit();
			int exploit = roleLoadInfo.getExploit() - (int) value;
			if(exploit < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, exploit, null,"")) {
				roleLoadInfo.setExploit(exploit);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) exploit ,drop);
				return true;
			}
			break;
		case TYPE_EQUIP:
			before = roleInfo.getRoleLoadInfo().getEquip();
			value = before - value;
			if(value < 0)
			{
				return false;
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null,"")) {
				roleInfo.getRoleLoadInfo().setEquip(value);
				// 日志
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) value , drop);
				return true;
			}
			break;
		case TYPE_TECH:
			before = roleInfo.getTech();
			int tech = roleInfo.getTech() - (int) value;
			if(tech < 0)
			{
				return false;
			}
			lastRecoverTime = roleInfo.getLastRecoverTechTime();
			// 体力小于上限重置自动回复时间
			if (before >= roleInfo.getTechRecoverLimit() && tech < roleInfo.getTechRecoverLimit()) {				
				lastRecoverTime = new Timestamp(System.currentTimeMillis());
			}
			if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, tech, lastRecoverTime,"")) {
				roleInfo.setTech((short) tech);
				roleInfo.setLastRecoverTechTime(lastRecoverTime);					
				GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) tech , drop);
				return true;
			}
			break;
		case TYPE_PVP_3_MONEY:
			 before = roleInfo.getRoleLoadInfo().getPvp3Money();
			 value = before - value;
			 if(value < 0)
			 {
				 return false;
			 }
			 if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null,"")) {
				 roleInfo.getRoleLoadInfo().setPvp3Money(value);
				 // 日志
				 GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) value , drop);
				 return true;
			 }
			break;
		case TYPE_CLUB_CONTRIBUTION:
			 before = roleInfo.getClubContribution();
			 value = before - value;
			 if(value < 0)
			 {
				 return false;
			 }
			 if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null,"")) {
				 roleInfo.setClubContribution((int)value);
				 // 日志
				 GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) value ,drop);
				 return true;
			 }
			break;
		case TYPE_STAR_MONEY:
			 before = roleInfo.getRoleLoadInfo().getStarMoney();
			 value = before - value;
			 if(value < 0)
			 {
				 return false;
			 }
			 if (RoleDAO.getInstance().updateRoleResource(action, roleInfo.getId(), type, value, null, "")) {
				 roleInfo.getRoleLoadInfo().setStarMoney((int)value);
				 // 日志
				 GameLogService.insertMoneyLog(roleInfo, action, type, 1, (int) before, (int) value ,drop);
				 return true;
			 }
			break;
		default:
			break;
		}

		return false;
	}

	/**
	 * 更新玩家体力
	 * 
	 * @param roleInfo
	 */
	public static void timerRecoverSp(RoleInfo roleInfo) {
		int unitTimeAdd = GameValue.ROLE_SP_ADD_RATE;
		int period = GameValue.ROLE_SP_ADD_PERIOD;

		long now = System.currentTimeMillis();
		if (roleInfo.getLastRecoverSPTime() == null) {
			roleInfo.setLastRecoverSPTime(new Timestamp(now));
		}
		if (roleInfo.getSp() >= roleInfo.getSpRecoverLimit()) {
			return;
		}
		long lastSpTime = roleInfo.getLastRecoverSPTime().getTime();
		long periodNum = (now - lastSpTime) / (period * 1000);
		if (periodNum >= 1) {
			long before = roleInfo.getSp();
			long after = roleInfo.getSp() + periodNum * unitTimeAdd;
			if (after > roleInfo.getSpRecoverLimit()) {
				after = roleInfo.getSpRecoverLimit();
			}
			Timestamp lastRecoverTime = new Timestamp(lastSpTime + periodNum * period * 1000);
			int action = ActionType.action5.getType();
			roleInfo.setSp((short) after);
			roleInfo.setLastRecoverSPTime(lastRecoverTime);
			// 日志
			GameLogService.insertMoneyLog(roleInfo, action, ConditionType.TYPE_SP, 0, (int) before, (int) after , null);
		}

	}
		
	/**
	 * 更新玩家精力
	 * 
	 * @param roleInfo
	 */
	public static void timerRecoverEnergy(RoleInfo roleInfo) {
		int unitTimeAdd = GameValue.ROLE_ENERGY_ADD_RATE;
		int period = GameValue.ROLE_ENERGY_ADD_PERIOD;

		long now = System.currentTimeMillis();
		if (roleInfo.getLastRecoverEnergyTime() == null) {
			roleInfo.setLastRecoverEnergyTime(new Timestamp(now));
		}
		if (roleInfo.getEnergy() >= roleInfo.getEnergyRecoverLimit()) {
			return;
		}
		long lastEnergyTime = roleInfo.getLastRecoverEnergyTime().getTime();
		long periodNum = (now - lastEnergyTime) / (period * 1000);
		if (periodNum >= 1) {
			long before = roleInfo.getEnergy();
			long after = roleInfo.getEnergy() + periodNum * unitTimeAdd;
			if (after > roleInfo.getEnergyRecoverLimit()) {
				after = roleInfo.getEnergyRecoverLimit();
			}
			Timestamp lastRecoverTime = new Timestamp(lastEnergyTime + periodNum * period * 1000);
			int action = ActionType.action22.getType();
			roleInfo.setEnergy((short) after);
			roleInfo.setLastRecoverEnergyTime(lastRecoverTime);
			// 日志
			GameLogService.insertMoneyLog(roleInfo, action, ConditionType.TYPE_ENERGY, 0, (int) before, (int) after ,null);
		}

	}
		
	/**
	 * 更新玩家技能点
	 * 
	 * @param roleInfo
	 */
	public static void timerRecoverTech(RoleInfo roleInfo) {
		int unitTimeAdd = GameValue.ROLE_TECH_ADD_RATE;
		int period = GameValue.ROLE_TECH_ADD_PERIOD;

		long now = System.currentTimeMillis();
		if (roleInfo.getLastRecoverTechTime() == null) {
			roleInfo.setLastRecoverTechTime(new Timestamp(now));
		}
		if (roleInfo.getTech() >= roleInfo.getTechRecoverLimit()) {
			return;
		}
		long lastTechTime = roleInfo.getLastRecoverTechTime().getTime();
		long periodNum = (now - lastTechTime) / (period * 1000);
		if (periodNum >= 1) {
			long before = roleInfo.getTech();
			long after = roleInfo.getTech() + periodNum * unitTimeAdd;
			if (after > roleInfo.getTechRecoverLimit()) {
				after = roleInfo.getTechRecoverLimit();
			}
			Timestamp lastRecoverTime = new Timestamp(lastTechTime + periodNum * period * 1000);
			int action = ActionType.action25.getType();
			roleInfo.setTech((short) after);
			roleInfo.setLastRecoverTechTime(lastRecoverTime);
			// 日志
			GameLogService.insertMoneyLog(roleInfo, action, ConditionType.TYPE_TECH, 0, (int) before, (int) after , null);
		}

	}

	/**
	 * 返回增加/消耗的资源
	 * @param conds
	 */
	public static String returnResourceChange(List<AbstractConditionCheck> conds)
	{
		StringBuffer buff = new StringBuffer();
		BaseSubResource sub = AbstractConditionCheck.subCondition(conds);
		if (sub == null) {
			return null;
		}

		if (sub.upMoney > 0) {
			buff.append(ConditionType.TYPE_MONEY.getType() + "," + sub.upMoney);
		}

		if (sub.upCoin > 0) {
			buff.append(ConditionType.TYPE_COIN.getType() + "," + sub.upCoin);
		}
		
		if (sub.upSp > 0) {
			buff.append(ConditionType.TYPE_SP.getType() + "," + sub.upSp);
		}
		
		if (sub.upEnergy > 0) {
			buff.append(ConditionType.TYPE_ENERGY.getType()+","+sub.upEnergy);
		}
		
		if (sub.upCourage > 0) {
			buff.append(ConditionType.TYPE_COURAGE.getType() + "," +sub.upCourage); 
		}
		
		if (sub.upJustice > 0) {
			buff.append(ConditionType.TYPE_JUSTICE.getType() + ","+sub.upJustice); 
		}
		
//		if (sub.upDevote > 0) {
//			buff.append(ConditionType.TYPE_DEVOTE.getType()+","+sub.upDevote); 
//		}
		
		if (sub.upKuafuMoney > 0) {
			buff.append(ConditionType.TYPE_KUAFU_MONEY.getType() + "," + sub.upKuafuMoney);
		}
		
		if (sub.upTeamMoney > 0) {
			buff.append(ConditionType.TYPE_TEAM_MONEY.getType() + "," + sub.upTeamMoney);
		}
		
		if (sub.upExploit > 0) {
			buff.append(ConditionType.TYPE_EXPLOIT.getType()+ "," + sub.upExploit);
		}
		
		if (sub.clubContribution > 0) {
			buff.append(ConditionType.TYPE_CLUB_CONTRIBUTION.getType()+ "," + sub.clubContribution);
		}
		
		if (sub.starMoney > 0) {
			buff.append(ConditionType.TYPE_STAR_MONEY.getType()+ "," + sub.starMoney);
		}
		
		return buff.toString();
	}
	
	/**
	 * 服务器启动，计算所有玩家战力
	 * @param roleInfo
	 */
	public static boolean calAllRoleFightValue(ExecutorService service){
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		
		if (roleMap.size() < 10000) {
			for (RoleInfo roleInfo : roleMap.values()) {
				HeroService.loginInitHeroProperty(roleInfo);
			}
		} else {
			int index = 0;
			
			final List<RoleInfo> list1 = new ArrayList<RoleInfo>();
			final List<RoleInfo> list2 = new ArrayList<RoleInfo>();
			final List<RoleInfo> list3 = new ArrayList<RoleInfo>();
			final List<RoleInfo> list4 = new ArrayList<RoleInfo>();
			
			final Set<Integer> set = Collections.synchronizedSet(new HashSet<Integer>());
			
			try {
				for (RoleInfo roleInfo : roleMap.values()) {
					if (index == 4) {
						index = 0;
					}

					if (index % 4 == 0) {
						list1.add(roleInfo);
					} else if (index % 4 == 1) {
						list2.add(roleInfo);
					} else if (index % 4 == 2) {
						list3.add(roleInfo);
					} else if (index % 4 == 3) {
						list4.add(roleInfo);
					}

					index++;
				}

				FutureTask<Boolean> initFightValue1 = null;
				FutureTask<Boolean> initFightValue2 = null;
				FutureTask<Boolean> initFightValue3 = null;
				FutureTask<Boolean> initFightValue4 = null;

				if (list1.size() > 0) {
					initFightValue1 = new FutureTask<Boolean>(new Callable<Boolean>() {
						public Boolean call() {
							for (RoleInfo roleInfo : list1) {
								HeroService.loginInitHeroProperty(roleInfo);
								set.add(roleInfo.getId());
							}

							return true;
						}
					});
					
					service.execute(initFightValue1);
				}

				if (list2.size() > 0) {
					initFightValue2 = new FutureTask<Boolean>(new Callable<Boolean>() {
						public Boolean call() {
							for (RoleInfo roleInfo : list2) {
								HeroService.loginInitHeroProperty(roleInfo);
								set.add(roleInfo.getId());
							}

							return true;
						}
					});
					
					service.execute(initFightValue2);
				}

				if (list3.size() > 0) {
					initFightValue3 = new FutureTask<Boolean>(new Callable<Boolean>() {
						public Boolean call() {
							for (RoleInfo roleInfo : list3) {
								HeroService.loginInitHeroProperty(roleInfo);
								set.add(roleInfo.getId());
							}

							return true;
						}
					});
					
					service.execute(initFightValue3);
				}

				if (list4.size() > 0) {
					initFightValue4 = new FutureTask<Boolean>(new Callable<Boolean>() {
						public Boolean call() {
							for (RoleInfo roleInfo : list4) {
								HeroService.loginInitHeroProperty(roleInfo);
								set.add(roleInfo.getId());
							}

							return true;
						}
					});
					
					service.execute(initFightValue4);
				}

				if (initFightValue1 != null) {
					initFightValue1.get();
				}

				if (initFightValue2 != null) {
					initFightValue2.get();
				}

				if (initFightValue3 != null) {
					initFightValue3.get();
				}

				if (initFightValue4 != null) {
					initFightValue4.get();
				}
				
				for (RoleInfo roleInfo : roleMap.values()) {
					if (!set.contains(roleInfo.getId())) {
						logger.error("========================= lose role =========================");
					}
				}
			} catch (Exception e) {
				logger.error("calAllRoleFightValue error", e);
				return false;
			} finally {
				list1.clear();
				list2.clear();
				list3.clear();
				list4.clear();
			}
		}
		
		return true;
	}
	
	/**
	 * 通知mail服角色下线
	 * @param roleId
	 */
	public static void nofityMailRoleOut(String roleIds)
	{
		// 通知mail服角色下线
		/*IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		
		if(session != null && session.isConnected())
		{
			Message message = new Message();
			
			NofityMailOutResp req = new NofityMailOutResp();
			req.setRoleIds(roleIds);
			
			GameMessageHead head = new GameMessageHead();
			head.setMsgType(Command.USER_OUT_NOTIFY_MAIL);
			message.setHeader(head);
			message.setBody(req);
			
			session.write(message);
		}*/
	}
	
	/**
	 * 判断角色是否在战斗中(战斗场景)
	 * @param roleInfo
	 */
	public static boolean checkRoleInFight(RoleInfo roleInfo) {
		if (roleInfo != null) {
			if (FightInfoMap.getFightInfoByRoleId(roleInfo.getId()) != null) {
				// pve 中
				return true;
			} else {
				RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
				if (roleLoadInfo != null) {
					if (roleLoadInfo.getInFight() == 2 || roleLoadInfo.getInFight() == 4
							|| roleLoadInfo.getInFight() == 7 || roleLoadInfo.getInFight() == 10 || roleLoadInfo.getInFight() == 12) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 推送角色世界聊天条数限制到聊天服务器（角色升级后，和角色登陆后推送）
	 * @param roleInfo
	 */
	public static void sendWorldChatLimit2MailServer(RoleInfo roleInfo, int flag){
		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			WorldChatLimitReq req = new WorldChatLimitReq();

			int level = HeroInfoMap.getMainHeroLv(roleInfo.getId());
			
			if(level < GameValue.WORLD_CHAT_TEN_NUM_LEVEL){
				return;
			}
			int limit = 0;
			
			if(level >= GameValue.WORLD_CHAT_TEN_NUM_LEVEL && level < GameValue.WORLD_CHAT_MAX_LEVEL){
				limit = (level - GameValue.WORLD_CHAT_TEN_NUM_LEVEL) * GameValue.WORLD_CHAT_NUM_LEVEL_PLUS;
				limit += 10;
				
			}else{
				limit = GameValue.WORLD_CHAT_MAX_NUM;
			}
			
			req.setLimit(limit);
			req.setCountFlag(flag);
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setUserID0(roleInfo.getId());
			header.setMsgType(Command.SEND_WORLD_CHAT_LIMIT_2_MAIL_SERVER_REQ);
			message.setHeader(header);
			message.setBody(req);
			session.write(message);
		}
		
	}
}
