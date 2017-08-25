package com.snail.webgame.game.tool.handler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.common.IoSession;
import org.dom4j.Document;
import org.dom4j.Element;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.engine.common.util.XMLUtil4DOM;
import com.snail.webgame.engine.component.tool.facade.ToolServiceHandler;
import com.snail.webgame.engine.component.tool.message.ToolMsgHeader;
import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.cache.ToolBoxMap;
import com.snail.webgame.game.cache.ToolOpActMap;
import com.snail.webgame.game.cache.ToolProgramList;
import com.snail.webgame.game.charge.text.util.TextUtil;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.ToolProgram;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ConfigXmlDAO;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.GameSettingDAO;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.ItemDAO;
import com.snail.webgame.game.dao.RoleChargeErrorDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.SkillDAO;
import com.snail.webgame.game.dao.ToolBoxDAO;
import com.snail.webgame.game.dao.ToolDAO;
import com.snail.webgame.game.dao.ToolOpActDAO;
import com.snail.webgame.game.dao.ToolOpActRewardDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleChargeInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.ToolBoxInfo;
import com.snail.webgame.game.info.ToolOpActivityInfo;
import com.snail.webgame.game.info.ToolOpActivityRewardInfo;
import com.snail.webgame.game.protocal.app.common.AppStoreExInfo;
import com.snail.webgame.game.protocal.app.common.EChargeState;
import com.snail.webgame.game.protocal.appellation.entity.ResetTitleInfosResp;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.rolemgt.syncAdvert.SyncAdvertResp;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;
import com.snail.webgame.game.tool.Constants;
import com.snail.webgame.game.tool.helper.ToolHelper;
import com.snail.webgame.game.tool.pojo.DeleteVo;
import com.snail.webgame.game.tool.pojo.HeroVo;
import com.snail.webgame.game.tool.pojo.MailInfo;
import com.snail.webgame.game.tool.pojo.PostVo;
import com.snail.webgame.game.tool.pojo.RoleVo;
import com.snail.webgame.game.tool.pojo.SkillVo;
import com.snail.webgame.game.xml.cache.ChenghaoXMLInfoMap;
import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;
import com.snail.webgame.game.xml.info.PayXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;

/**
 * 实现业务类类
 * 
 * @author caowl
 * 
 */
public class ToolServiceHandlerImpl implements ToolServiceHandler {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static final String RESULT_SUCCESSFUL = "1";
	private static final String RESULT_FAILED = "0";
	private ConfigXmlDAO configXmlDAO = ConfigXmlDAO.getInstance();

	/**
	 * 发送邮件
	 * 
	 * @param fields
	 * @return String result[] 处理结果 result[0] 0:失败 , 1：成功, result[1] 空：正常,
	 *         其他：错误消息
	 */
	@ToolMsgHeader("SendMail")
	public String[] sendMail(List<Object> fields) {
		String[] result = { RESULT_FAILED, "" };

		String mailXML = "";
		String operator = "";

		if (fields.size() != 4) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
		} else {
			String mailTitle = (String) fields.get(1);
			mailXML = (String) fields.get(2);
			operator = (String) fields.get(3);
			MailInfo mail = ToolHelper.parseMail(mailXML);

			if (mail != null) {
				if (!mail.getTopic().equals(mailTitle)) {
					result[1] = Constants.GAME_TOOL_SENDMAIL_INFO_02;
				} else {
					// 发送邮件
					if (ToolHelper.sendMail(mail)) {
						result[0] = RESULT_SUCCESSFUL;
					} else {
						result[1] = Constants.GAME_TOOL_SENDMAIL_INFO_05;
					}
				}
			} else {
				result[1] = Constants.GAME_TOOL_SENDMAIL_INFO_01;
			}
		}

		GameLogService
				.insertToolOperatLog("SendMail", (String) fields.get(0), "", "", "", mailXML, operator, result[0]);
		return result;
	}

	/**
	 * 发送公告
	 * 
	 * @param list 传输信息
	 * @return result[] 处理结果 result[0] {0:失败 , 1：成功} result[1] {空：正常, 其他：错误消息}
	 */
	@ToolMsgHeader("AddPost")
	public String[] addPost(List<Object> list) {
		String[] result = { RESULT_FAILED, "" };

		if (list.size() != 3) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
		}

		PostVo postVo = ToolHelper.parsePost((String) list.get(1));

		if (postVo == null || StringUtils.isBlank((postVo.getContent()))) {
			result[1] = Constants.GAME_TOOL_XML_ERROR;
			return result;
		}

		String message = postVo.getContent();// 公告的消息
		if (StringUtils.isNotBlank(message)) {
			GmccMgtService.sendChatMessage(message);
			result[0] = RESULT_SUCCESSFUL;
		}

		GameLogService.insertToolOperatLog("AddPost", (String) list.get(0), "", "", "", (String) list.get(1),
				(String) list.get(2), result[0]);
		return result;
	}

	/**
	 * 修改角色数据
	 * 
	 * @param list 传输信息
	 * @return String[] 处理结果 string[0]{0:失败 , 1：成功} string[1]{空：正常, 其他：错误消息}
	 */
	@ToolMsgHeader("UpdateRoleData")
	public String[] modifyRoleData(List<Object> list) {
		String[] result = { RESULT_FAILED, " " };

		if (list.size() != 5) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}

		String roleName = (String) list.get(1);
		String account = (String) list.get(2);
		String roleDataXml = (String) list.get(3);

		RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(roleName);

		if (roleInfo != null) {
			RoleVo roleVo = ToolHelper.parseRoleDataXml(roleDataXml);

			if (roleVo == null) {
				result[1] = Constants.GAME_TOOL_XML_ERROR;
				return result;
			}

			String validateResult = validateRoleDataVo(roleVo);
			if (StringUtils.isNotBlank(validateResult)) {
				result[1] = validateResult;
				return result;
			}

			synchronized (roleInfo) {
				String updateSucc = updateRoleDataResource(roleInfo, roleVo);
				if (StringUtils.isBlank(updateSucc)) { // 成功
					result[0] = RESULT_SUCCESSFUL;
					// 刷新角色信息到客户端
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
				} else {
					result[1] = MessageFormat.format(Constants.GAME_TOOL_ROLE_ERROR_3, updateSucc);
				}
				
				// 跳过新手引导
				if (roleVo.getGuide() == 45) {
					String endGuideInfo = "1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1," +
							"1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
					if (!RoleDAO.getInstance().updateGuideData(roleInfo.getId(), endGuideInfo)) {
						result[1] = Constants.GAME_TOOL_DB_ERROR;
					}
					if (roleInfo.getRoleLoadInfo() != null) {
						roleInfo.getRoleLoadInfo().setGuideInfo(endGuideInfo);
					}
				}
				
				// 是否改名
				String chgName = roleVo.getChgName();
				if (chgName != null && !"".equals(chgName.trim())) {
					// 判断名称是否合法
					if (roleName.equalsIgnoreCase(chgName)) {
						result[1] = Constants.GAME_TOOL_ROLE_ERROR_5;
					}
					
					if (chgName.trim().length() > GameValue.MAX_NAME_LENGTH) {
						result[1] = Constants.GAME_TOOL_ROLE_ERROR_6;
					}
					
					if (RoleInfoMap.getRoleInfoByName(chgName) != null) {
						result[1] = Constants.GAME_TOOL_ROLE_ERROR_7;
					}
					
					// 改名
					if (RoleDAO.getInstance().updateRoleName(roleInfo.getId(), chgName, roleInfo.getChangeRoleNameTimes())) {
						roleInfo.setRoleName(chgName);
						RoleInfoMap.changeRoleName(roleInfo, roleName);
					} else {
						result[1] = Constants.GAME_TOOL_ROLE_ERROR_8;
					}
					
					// 更新排行榜名字
					RankInfo levelRankInfo = roleInfo.getLevelRankInfo();
					if (levelRankInfo != null) {
						levelRankInfo.setName(chgName);
					}
					RankInfo heroNumRankInfo = roleInfo.getHeroNumRankInfo();
					if (heroNumRankInfo != null) {
						heroNumRankInfo.setName(chgName);
					}
					RankInfo fightValueRankInfo = roleInfo.getFightValueRankInfo();
					if (fightValueRankInfo != null) {
						fightValueRankInfo.setName(chgName);
					}
					
					// 更新异步竞技场名字
					FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleInfo.getId());
					if (arenaInfo != null) {
						RoleDAO.getInstance().updateArenaRoleName(arenaInfo.getId(), chgName);
						arenaInfo.setRoleName(chgName);
					}
					
					if (roleInfo.getLoginStatus() == 1) {
						RoleMgtService.sendToMailRoleNameChange(roleInfo);

						// 改名后通知其它人
						SceneService1.roleNameUpdate(roleInfo);
					}
				}
			}
					
		} else {
			result[1] = MessageFormat.format(Constants.GAME_TOOL_ROLE_ERROR_2, roleName);
		}

		GameLogService.insertToolOperatLog("UpdateRoleData", (String) list.get(0), roleName, account, "", roleDataXml,
				(String) list.get(4), result[0]);

		return result;
	}

	/**
	 * 修改惩罚配置
	 * 
	 * @param list message fields
	 * @return String[] 处理结果 string[0]{0:失败 , 1：成功} string[1]{空：正常, 其他：错误消息}
	 */
	@ToolMsgHeader("UpdatePunishItem")
	public String[] updatePunishItem(List<Object> list) {
		String[] result = { RESULT_FAILED, " " };

		if (list.size() != 4) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}

		String idStr = (String) list.get(1);
		String xmlStr = (String) list.get(2);

		if (StringUtils.isBlank(idStr) || StringUtils.isBlank(xmlStr)) {
			result[1] = Constants.GAME_UPDATE_PUNISH_CONFIG_2;
			return result;
		}

		String xmlName = splitXmlName(idStr);
		if (StringUtils.isBlank(xmlName)) {
			result[1] = Constants.GAME_UPDATE_PUNISH_CONFIG_2 + idStr;
			return result;
		}

		List<String> idStrList = new ArrayList<String>();
		// 删除
		if (xmlStr.equals("DeletePunish") && xmlStr.startsWith("Punish")) {
			idStrList.add(idStr);
			// 设置删除标记
			configXmlDAO.batchUpdateDelFlag(idStrList);

			if (logger.isInfoEnabled()) {
				logger.info(Constants.GAME_UPDATE_PUNISH_CONFIG_1 + ", Success!");
			}

			result[0] = RESULT_SUCCESSFUL;
		} else {
			if (!ToolHelper.createOrUpdateXml(xmlName, idStr, xmlStr)) {
				result[1] = Constants.GAME_UPDATE_PUNISH_CONFIG_2 + idStr;
				return result;
			}
		}
		GameLogService.insertToolOperatLog("UpdatePunishItem", (String) list.get(0), "", "", idStr, xmlStr,
				(String) list.get(3), result[0]);
		return result;
	}

	/**
	 * 修改XML配置文件
	 * 
	 * @param list
	 * @return
	 */
	@ToolMsgHeader("UpdateConfig")
	public String[] updateConfig(List<Object> list) {
		String[] result = new String[] { RESULT_FAILED, "" };

		if (list.size() != 4) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}

		String idStrs = (String) list.get(1); // 关键字Army_14001(多个#分隔)
		String xmlStrs = (String) list.get(2); // xml字符串(多个#分隔)
		String operator = (String) list.get(3); // 操作人账号

		String ids[] = StringUtils.split(idStrs, '#');
		String xmls[] = StringUtils.split(xmlStrs, '#');

		if (ids == null || xmls == null || ids.length != xmls.length) {
			result[1] = Constants.GAME_TOOL_XML_ERROR;
			return result;
		}

		// List<String> idStrList = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			String idStr = ids[i];
			String xmlStr = xmls[i];

			/*
			 * if("DEL".equals(xmlStr) && "DropItem".equals(idStr)) {
			 * idStrList.add(idStr); continue; }
			 */

			String xmlName = splitXmlName(idStr);
			if (StringUtils.isBlank(xmlName)) {
				result[1] = Constants.GAME_TOOL_UPDATE_XML_CONFIG_2 + idStr;
				return result;
			}
			if (!ToolHelper.createOrUpdateXml(xmlName, idStr, xmlStr)) {
				result[1] = Constants.GAME_UPDATE_PUNISH_CONFIG_2 + idStr;
				return result;
			}
		}
		result[0] = RESULT_SUCCESSFUL;

//		 if(idStrList.size() > 0) {
//		 //设置删除标记
//		 ConfigXMLDAO.setDelFlagConfigDAO(idStrList);
//		 }
		GameLogService.insertToolOperatLog("UpdateXMLConfig", (String) list.get(0), "", "", idStrs, xmlStrs, operator,
				result[0]);
		return result;
	}
	
	/**
	 * 活动配置修改
	 * @param list
	 * @return
	 */
	@ToolMsgHeader("ModifyProgram")
	public String[] modifyProgram(List<Object> list) {
		String[] result = new String[] { RESULT_FAILED, "" };

		if (list.size() != 4) {
			result[1] = Constants.GAME_TOOL_MODIFY_PROGRAM_1;
			return result;
		}
		
		String programId = (String) list.get(1); // 活动ID，标识是双倍掉经验还是掉物品
		String xmlStr = (String) list.get(2); // xml字符串
		String operator = (String) list.get(3); // 操作人账号
		
		if(ToolDAO.getInstance().isExistToolProgrom(programId)){//已存在该活动
			if(!ToolDAO.getInstance().updateToolProgram(programId,operator,xmlStr))
			{
				result[1] = Constants.GAME_TOOL_MODIFY_PROGRAM_2;
				return result;
			}
		}else{//不存在该活动
			if(!ToolDAO.getInstance().insertToolProgram(programId, operator, xmlStr)){
				result[1] = Constants.GAME_TOOL_MODIFY_PROGRAM_2;
				return result;
			}
		}
		
		if(XMLToList(xmlStr)){
			result[0] = RESULT_SUCCESSFUL;
		}
		
		GameLogService.insertToolOperatLog("ModifyProgram", (String) list.get(0), "", "", programId, xmlStr, operator, result[0]);
		return result;
	}

	// private method

	/**
	 * 验证修改角色信息数据
	 * 
	 * @param roleVo
	 * @return
	 */
	private static String validateRoleDataVo(RoleVo roleVo) {
		if(roleVo.getRoleRace() < 1 || roleVo.getRoleRace() > 3){
			return "0";
		}
		//验证角色名是否重复
		return null;
	}

	/**
	 * 修改资源
	 * 
	 * @param roleInfo
	 * @param roleVo
	 * 
	 * @return 成功返回空字符串， 失败返回某一资源失败的type
	 */
	private static String updateRoleDataResource(RoleInfo roleInfo, RoleVo roleVo) {
		// int action = GameAction.GAME_ROLE_ACTION_4;
		// boolean flag = false;
		
		int coinCost = (int) (roleInfo.getTotalCoin() - roleInfo.getCoin());
		int totalCoin = (int) (roleVo.getCoin() + coinCost);
		
		//用户信息修改
		if (!RoleDAO.getInstance().updateRoleInfoForTool(roleInfo.getId(), roleVo.getRoleName(), roleVo.getRoleRace(),
				roleVo.getMoney(), totalCoin, roleVo.getCoin(), roleVo.getSp(), roleVo.getCourage(), roleVo.getJustice(),
				roleVo.getKuafuMoney(), roleVo.getExploit(), roleVo.getRankShow(), roleVo.getIsAdvert(), 
				roleVo.getEquip(), roleVo.getStarMoney())) {
			return "";
		}
		roleInfo.setRoleName(roleVo.getRoleName());
		roleInfo.setRoleRace(roleVo.getRoleRace());
		roleInfo.setMoney(roleVo.getMoney());
		
		roleInfo.setCoin(roleVo.getCoin());
		roleInfo.setTotalCoin(totalCoin);
		roleInfo.setSp(roleVo.getSp());
		
		roleInfo.setRankShow((byte) roleVo.getRankShow());
		
		if (roleVo.getIsAdvert() != roleInfo.getIsAdvert()) {
			roleInfo.setIsAdvert((byte) roleVo.getIsAdvert());
			
			if (roleInfo.getLoginStatus() == 1) {
				// 同步代言人
				syncAdvert2GateAndMail(roleInfo);
			}
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo != null) {
			roleLoadInfo.setCourage(roleVo.getCourage());
			roleLoadInfo.setJustice(roleVo.getJustice());
			roleLoadInfo.setKuafuMoney(roleVo.getKuafuMoney());
			roleLoadInfo.setExploit(roleVo.getExploit());
			roleLoadInfo.setEquip(roleVo.getEquip());
			roleLoadInfo.setStarMoney(roleVo.getStarMoney());
		}
		
		//修改装备碎片
		int itemNum = roleVo.getEquipStrengeh();
		Map<Integer,Integer> chgBagItemMap=new HashMap<Integer, Integer>();
		boolean equipFlag = updateItem(roleInfo, GameValue.EQUIP_STRENGEH_ITEM, itemNum, chgBagItemMap);
		if(equipFlag)
		{
			// 推送背包信息
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ITEM, chgBagItemMap);
		}
		
		//修改称号
		String titleStr = roleVo.getAllTitles();

		Map<Integer, Long> map = new HashMap<Integer, Long>();
		String allTitles = "";
		if(titleStr.length() > 0)
		{
			//检测发过来的玩家称号
			String[] titles = titleStr.split(";");
			for(String title : titles)
			{
				ChenghaoXMLInfo xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(NumberUtils.toInt(title));
				if(xmlInfo != null)
				{
					long time = xmlInfo.getKeepTime();
					if(time > 0){
						time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
					}
					
					if(map.containsKey(xmlInfo.getNo())){
						continue;
					}
					
					map.put(xmlInfo.getNo(), time);
				}
			}
		}
		
		allTitles = CommonUtil.Map2String(map);
		
		if(RoleDAO.getInstance().updateAllAppellation(roleInfo.getId(), allTitles)){
			if(roleInfo.getRoleLoadInfo() != null)
			{
				roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
				//刷新
				ResetTitleInfosResp resp = new ResetTitleInfosResp();
				resp.setResult(1);
				resp.setFlag(0);
				resp.setTitle(TitleService.nowTitleCheck(roleInfo, map));
				resp.setTitles(allTitles);
				
				HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				
				if(mainHeroInfo != null){
					HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
					
					resp.setFightValue(mainHeroInfo.getFightValue());
				}
				
				SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.RESET_TITLE_RESP);
			}
		}
		
		// 修改武将等级
		for (HeroVo heroVo : roleVo.getHeroList()) {
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroVo.getHeroId());
			if (heroInfo != null) {
				if (!HeroDAO.getInstance().updateHeroLv(heroInfo.getId(), heroVo.getHeroLevel(), heroVo.getHeroExp())) {
					return "";
				}
				
				heroInfo.setHeroLevel(heroVo.getHeroLevel());
				heroInfo.setHeroExp(heroVo.getHeroExp());
			}
		}
		
		// 修改主角技能等级
		if (roleVo.getSkillList().size() > 0) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return "";
			}
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXmlInfo == null) {
				return "";
			}
			
			Map<Integer, HeroXMLSkill> skillMap = heroXmlInfo.getSkillMap();
				
			String skillStr = "";
			for (SkillVo skillVo : roleVo.getSkillList()) {
				int skillNo = skillVo.getSkillNo();
				HeroXMLSkill skillXML = skillMap.get(skillNo);
				if (skillXML == null) {
					return "";
				}
				
				int skillPos = skillXML.getSkillPos();
				HeroSkillXMLInfo hsxml = HeroXMLInfoMap.getHeroSkillXMLInfo(skillPos);
				if (hsxml == null) {
					return "";
				}
				
				int skillLevel = skillVo.getSkillLv();
				if (skillLevel > hsxml.getMaxSkillLv()) {
					skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, hsxml.getMaxSkillLv());
				} else {
					if (hsxml.getUpMap().containsKey(skillLevel)) {
						skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, skillLevel);
					}
				}
				
				if (!SkillDAO.getInstance().addOrUpdateHeroSkill(heroInfo.getId(), skillStr)) {
					return "";
				}
				heroInfo.setSkillStr(skillStr);
			}
			
//			HeroService.refeshHeroProperty(roleInfo, heroInfo);
//			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
		}
		
		// 删除道具
		if (roleVo.getItemList().size() > 0) {
			Map<Integer, Integer> updateItemMap = new HashMap<Integer, Integer>();
			
			for (DeleteVo vo : roleVo.getItemList()) {
				updateItemMap.put(vo.getId(), vo.getNum());
			}
			
			// 库更新
			if (!ItemDAO.getInstance().updateBagItem(updateItemMap)) {
				return "";
			}
			
			for (int itemId : updateItemMap.keySet()) {
				BagItemInfo bagItem = BagItemMap.getBagItemById(roleInfo, itemId);
				if (bagItem == null) {
					continue;
				}

				int afterNum = updateItemMap.get(itemId);

				if (afterNum > 0) {
					bagItem.setNum(afterNum);
				} else {
					BagItemMap.removeBagItem(roleInfo, bagItem.getId());
				}
			}
		}
		
		// 删除装备
		if (roleVo.getEquipList().size() > 0) {
			List<Integer> equipIdList = new ArrayList<Integer>();
			
			for (DeleteVo vo : roleVo.getEquipList()) {
				equipIdList.add(vo.getId());
			}
			
			if (!EquipDAO.getInstance().deleteEquip(equipIdList)) {
				return "";
			}
			
			for (int equipId : equipIdList) {
				EquipInfoMap.removeBagEquip(roleInfo.getId(), equipId);
			}
		}
		
		return "";
	}
	
	/**
	 * 修改道具
	 * @param roleInfo
	 * @param itemNo
	 * @param itemNum
	 * @return
	 */
	private static boolean updateItem(RoleInfo roleInfo, int itemNo, int itemNum, Map<Integer,Integer> chgBagItemMap)
	{
		Map<Integer, Integer> updateBagItem = new HashMap<Integer, Integer>();
		Map<Integer, BagItemInfo> addBagItemMap = new HashMap<Integer, BagItemInfo>();
		// 道具
		PropXMLInfo xmlInfo = PropXMLInfoMap.getPropXMLInfo(itemNo);
		if (xmlInfo == null) {
			return false;
		}
		int itemtype = BagItemInfo.getItemType(String.valueOf(itemNo));
		if (itemtype == 0) {
			return false;
		}
		
		List<BagItemInfo> itemList = ItemDAO.getBagItem(roleInfo.getId());
		BagItemInfo itemInfo = null;
		if(itemList != null)
		{
			for(BagItemInfo info : itemList)
			{
				if(info.getItemNo() == itemNo && info.getNum() != itemNum)
				{
					itemInfo = info;
				}
			}
		}
		if(itemInfo != null)
		{
			//背包有
			updateBagItem.put(itemInfo.getId(), itemNum);
		}
		else
		{
			if(itemNum > 0)
			{
				//背包无 需要加入
				itemInfo = new BagItemInfo((int) roleInfo.getId(), itemtype, itemNo,
						itemNum, xmlInfo.getColour(), 0);
				itemInfo.setIsTransition(0);
				addBagItemMap.put(itemInfo.getItemNo(), itemInfo);
			}
			else
			{
				return false;
			}
		}
		List<BagItemInfo> addBagItem = new ArrayList<BagItemInfo>(addBagItemMap.values());
		// 维护BagItem数据
		if (updateBagItem.size() > 0 || addBagItem.size() > 0) {
			// 更新数据库
			if (!ItemDAO.getInstance().addItemBatch(addBagItem, updateBagItem)) {
				return false;
			}
			
			// 新增的背包物品缓存
			for (BagItemInfo bagItemInfo : addBagItem) {
				// 添加新纪录
				BagItemMap.addBagItem(roleInfo, bagItemInfo);
				if(!chgBagItemMap.containsKey(bagItemInfo.getId())){
					chgBagItemMap.put(bagItemInfo.getId(),bagItemInfo.getItemNo());
				}
			}
			// 更新的背包物品缓存
			BagItemInfo bagItem1;
			for (int itemId : updateBagItem.keySet()) {
				bagItem1 = BagItemMap.getBagItemById(roleInfo, itemId);	
				if(bagItem1 != null){
					int num = updateBagItem.get(itemId);
					if(num > 0)
					{
						bagItem1.setNum(num);
					}
					else
					{
						BagItemMap.removeBagItem(roleInfo, itemId);
					}
					if(!chgBagItemMap.containsKey(itemId)){
						chgBagItemMap.put(itemId,bagItem1.getItemNo());
					}
				}
			}
		}
		return true;
	}
	
	private static void syncAdvert2GateAndMail(RoleInfo roleInfo) {
		SyncAdvertResp syncAdvertResp = new SyncAdvertResp();
		syncAdvertResp.setIsAdvert(roleInfo.getIsAdvert());
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setUserID0((int) (roleInfo.getId()));
		header.setMsgType(Command.SYNC_ADVERT_NOFI_MAIL);
		message.setHeader(header);
		message.setBody(syncAdvertResp);
		
		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			session.write(message);
		}
		
		session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}

	/**
	 * 截取idStr 获取 xmlName
	 * 
	 * @param idStr
	 * @return
	 */
	private static String splitXmlName(String idStr) {
		String[] splitArr = StringUtils.split(idStr, "_");
		String xmlName = null;
		if (splitArr.length == 2) {
			xmlName = splitArr[0];
		} else if (splitArr.length == 3) {
			xmlName = splitArr[0] + "_" + splitArr[1];
		}
		return xmlName;
	}
	
	/**
	 * XML转换成对象放入缓存
	 * @param xml
	 */
	public static boolean XMLToList(String xml)
	{
		Document doc = XMLUtil4DOM.string2Document(xml);
		Element rootEle = null;
		boolean flag = false;
		if(doc!=null)
		{
			try {
				rootEle = doc.getRootElement();
				byte type = Byte.parseByte(rootEle.element("type").getData().toString());
				String state = String.valueOf(rootEle.element("status").getData());//是否开启 0-关闭 1-开启
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_EXP)
				{
					ToolProgramList.getDoubleExpList().clear();//双倍经验
					if(state.equals("0")){
						GameValue.GAME_TOOL_EXP_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_MONEY)
				{
					ToolProgramList.getDoubleMoneyList().clear();//双倍掉率
					if(state.equals("0")){
						GameValue.GAME_TOOL_MONEY_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_COURAGE)
				{
					ToolProgramList.getDoubleCourageList().clear();//金币包活动
					if(state.equals("0")){
						GameValue.GAME_TOOL_COURAGE_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_JUSTICE)
				{
					ToolProgramList.getDoubleJusticeList().clear();//金币包活动
					if(state.equals("0")){
						GameValue.GAME_TOOL_JUSTICE_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_KUAFUMONEY)
				{
					ToolProgramList.getDoubleKuafuMoneyList().clear();//金币包活动
					if(state.equals("0")){
						GameValue.GAME_TOOL_KUAFUMONEY_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_TEAMMONEY)
				{
					ToolProgramList.getDoubleTeamMoneyList().clear();//金币包活动
					if(state.equals("0")){
						GameValue.GAME_TOOL_TEAMMONEY_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_EXPLOIT)
				{
					ToolProgramList.getDoubleExploitList().clear();//金币包活动
					if(state.equals("0")){
						GameValue.GAME_TOOL_EXPLOIT_RAND = 1;
						return true;
					}
				}else if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_HERO_BATTLE)
				{
					ToolProgramList.getDoubleHeroBattleList().clear();//金币包活动
					if(state.equals("0")){
						GameValue.GAME_TOOL_HERO_BATTLE_RAND = 1;
						return true;
					}
				}
				
				List<?> elem = rootEle.elements("opentimes");
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_EXP){
					GameValue.GAME_TOOL_EXP_RAND_TEMP = 2;
					ToolProgramList.setDoubleExpList(formateToolProgram(elem));
				}
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_MONEY){
					GameValue.GAME_TOOL_MONEY_RAND_TEMP = 2;
					ToolProgramList.setDoubleMoneyList(formateToolProgram(elem));
				}
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_COURAGE){
					GameValue.GAME_TOOL_COURAGE_RAND_TEMP = 2;
					ToolProgramList.setDoubleCourageList(formateToolProgram(elem));
				}
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_JUSTICE){
					GameValue.GAME_TOOL_JUSTICE_RAND_TEMP = 2;
					ToolProgramList.setDoubleJusticeList(formateToolProgram(elem));
				}
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_KUAFUMONEY){
					GameValue.GAME_TOOL_KUAFUMONEY_RAND_TEMP = 2;
					ToolProgramList.setDoubleKuafuMoneyList(formateToolProgram(elem));
				}
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_EXPLOIT){
					GameValue.GAME_TOOL_EXPLOIT_RAND_TEMP = 2;
					ToolProgramList.setDoubleExploitList(formateToolProgram(elem));
				}
				if(type == GameValue.GAME_TOOL_ACTIVE_TYPE_HERO_BATTLE){
					GameValue.GAME_TOOL_HERO_BATTLE_RAND_TEMP = 2;
					ToolProgramList.setDoubleHeroBattleList(formateToolProgram(elem));
				}
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		else
		{
			flag = false;
		}
		return flag;
	}
	
	private static List<ToolProgram> formateToolProgram(List<?> elem){
		List<ToolProgram> toolProgramList = new ArrayList<ToolProgram>();
		if(elem!=null && elem.size()>0)
		{
			for(int i=0;i<elem.size();i++)
			{
				Element e = (Element)elem.get(i);
				List<?> list = e.elements("opentime");
				if(list!=null&&list.size()>0)
				{
					for(int j=0;j<list.size();j++)
					{
						Element el = (Element)list.get(j);
						ToolProgram toolProgram = new ToolProgram();
						
						toolProgram.setStartDate(String.valueOf(el.element("startdate").getData()));
						toolProgram.setEndDate(String.valueOf(el.element("enddate").getData()));
						toolProgram.setStartTime(String.valueOf(el.element("starttime").getData()));
						toolProgram.setEndTime(String.valueOf(el.element("endtime").getData()));
						toolProgram.setWeekTime(String.valueOf(el.element("weektime").getData()));
						toolProgramList.add(toolProgram);
					}
				}
			}
		}
		return toolProgramList;
	}
	
	/**
	 * 重调appstore充值计费接口
	 * 
	 * @param toolMessage
	 * @return
	 */
	@ToolMsgHeader("RetransferAppStoreCharge")
	public static String[] retransferAppStoreCharge(List<Object> list) {
		String id = (String) list.get(1); // 表主键
		String operator = (String) list.get(2);// 操作人账号

		String[] result = new String[] { "1", " " };

		// 主键为空
		if (id == null) {
			result[0] = "0";
			result[1] = Constants.GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_2;
			return result;
		}

		long primaryKeyId;
		try {
			primaryKeyId = Integer.valueOf(id);
		} catch (Exception e) {
			result[0] = "0";
			result[1] = Constants.GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_2;
			return result;
		}

		RoleChargeErrorDao dao = new RoleChargeErrorDao();
		AppStoreExInfo info = dao.selectInfoById(primaryKeyId);

		if (info == null) {
			result[0] = "0";
			result[1] = Constants.GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_3;
			return result;
		}

		// 不需要重调
		if (info.getIsRetransfer() == EChargeState.RECEIVE_FROM_TRANSIT_SERVER_RESULT_IS_1.getValue() || info.getIsRetransfer() == EChargeState.ORDER_SUCCESSED.getValue()) {
			result[0] = "0";
			result[1] = Constants.GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_4;
			return result;
		}

		// 重调时间
		info.setRetransferTime(new Date());
		// 重调人
		info.setOperateUser(operator);

		try {
			String agentOrderId = info.getsAgentOrderId(); // 订单号
			String sImprestAccountIP = info.getsImprestAccountIP(); // 充值人IP
			String sAgentId = "" + info.getsAgentId(); // 平台ID
			String sAgentPwd = info.getsAgentPwd(); // 平台密码
			String sCardType = info.getsCardType(); // 卡类型
			String amount = "" + info.getAmount(); // 数量
			String totalValue = "" + info.getTotalValue(); // 总值(人民币数)
															// 总价!=卡类型配置价格*数量
			String sUserName = info.getsUserName(); // 玩家账号，GBK编码后的
			String sAccountTypeID = "" + info.getsAccountTypeID(); // 账户类型(0-中心
																	// , 1- 分区)
			String sAreaID = "" + info.getsAreaID(); // 充入分区ＩＤ（如果有赠送道具，也赠入此分区ＩＤ）
			String sVerifyStr = info.getsVerifyStr(); // 校验串，前面参数依次串接再加上ＳＥＥＤ，最后ＭＤ５，再转大写．

			info.setsAgentId(Integer.valueOf(sAgentId));
			info.setsAgentPwd(sAgentPwd);
			info.setsAgentOrderId(agentOrderId);
			info.setsCardType(sCardType);
			info.setAmount(Integer.valueOf(amount));
			info.setTotalValue(Integer.valueOf(totalValue));
			info.setsUserName(sUserName);
			info.setsAccountTypeID(Integer.valueOf(sAccountTypeID));
			info.setsAreaID(sAreaID);
			info.setsImprestAccountIP(sImprestAccountIP);
			info.setsVerifyStr(sVerifyStr);

			String jsonData = agentOrderId.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll("\f", "").replaceAll("\b", "");
			
			if(jsonData.indexOf("}") != -1){
				jsonData = jsonData.substring(0, jsonData.indexOf("}") + 1);
			}
			
			RoleChargeInfo roleChargeInfo = RoleChargeMap.fetchRoleChargeInfoBySeqId(primaryKeyId);
			
			if(roleChargeInfo != null){
				roleChargeInfo.setNeedDel(false);
			}
			
			boolean send = TextUtil.sendMessage("AppleCharge", jsonData, sCardType, sUserName, sImprestAccountIP, 
					info.getAmount(), Integer.valueOf(totalValue), Integer.valueOf(sAccountTypeID), Integer.valueOf(sAreaID),"" , id);
			
			info.setResultStr("" + send);

			if (logger.isInfoEnabled()) {
				logger.info("GAME-TOOL retransfer appStore imprest result = " + send + ", id = " + primaryKeyId + "; account = " + info.getAccount());
			}
			result[1] = send + "";
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("GAME-TOOL retransfer appStore imprest error : " + e.getMessage());
			}
			result[0] = "0";
			result[1] = e.getMessage();
		}
		return result;
	}
	
	/**
	 * 修改运营礼包信息
	 * 
	 * @param list 传输信息
	 * @return String[] 处理结果 string[0]{0:失败 , 1：成功} string[1]{空：正常, 其他：错误消息}
	 */
	@ToolMsgHeader("ModifyToolBox")
	public String[] modifyToolBox(List<Object> list) {
		String[] result = { RESULT_FAILED, " " };

		if (list.size() != 3) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}

		String toolBoxXml = (String) list.get(1);
		ToolBoxInfo toolBoxXmlInfo = ToolHelper.parseToolBoxXml(toolBoxXml);
		if (toolBoxXmlInfo == null) {
			result[1] = Constants.GAME_TOOL_XML_ERROR;
			return result;
		}
		
		ToolBoxInfo toolBoxInfo = null;
		if (toolBoxXmlInfo.getBoxType() == ToolBoxInfo.TYPE_BOX_CHARGE) {
			PayXMLInfo payXMLInfo = PayXMLInfoMap.fetchPayXMLInfo(toolBoxXmlInfo.getChargeNo());
			if (payXMLInfo == null || payXMLInfo.getPayType() != PayXMLInfo.PAY_TYPE_BOX) {
				result[1] = Constants.GAME_TOOL_BOX_ERROR_2;
				return result;
			}
			
			toolBoxInfo = ToolBoxMap.fetchBoxInfoById(toolBoxXmlInfo.getBoxType(), toolBoxXmlInfo.getChargeNo());
			
		} else if (toolBoxXmlInfo.getBoxType() == ToolBoxInfo.TYPE_BOX_GOLD) {
			if (toolBoxXmlInfo.getGuid() == 0) {
				// showid字段没传值
				result[1] = Constants.GAME_TOOL_BOX_ERROR_5;
				return result;
			}
			
			toolBoxInfo = ToolBoxMap.fetchBoxByGuId(toolBoxXmlInfo.getGuid());
			
		} else {
			result[1] = Constants.GAME_TOOL_BOX_ERROR_1;
			return result;
		}
		
		boolean isRefresh = false;
		boolean isVersionChg = false;
		long now = System.currentTimeMillis();
		
		if (toolBoxInfo == null) {
			// 新增礼包
			if (!ToolBoxDAO.getInstance().insertToolBoxInfo(toolBoxXmlInfo)) {
				result[1] = Constants.GAME_TOOL_BOX_ERROR_3;
				return result;
			}
			
		} else {
			// 修改礼包
			toolBoxXmlInfo.setId(toolBoxInfo.getId());
			if (!ToolBoxDAO.getInstance().updateToolBoxInfo(toolBoxXmlInfo)) {
				result[1] = Constants.GAME_TOOL_BOX_ERROR_3;
				return result;
			}
			
			if (!isRefresh) {
				if (toolBoxInfo.getStartTime().getTime() < now && now < toolBoxInfo.getEndTime().getTime()) {
					isRefresh = true;
				}
			}
			
			if (toolBoxXmlInfo.getBoxVersion() != toolBoxInfo.getBoxVersion()) {
				// 礼包版本修改，清零玩家购买次数
				isVersionChg = true;
				
				isRefresh = true;
			}
		}
		
		ToolBoxMap.addToolBoxInfo(toolBoxXmlInfo);
		
		if (!isRefresh) {
			if (toolBoxXmlInfo.getStartTime().getTime() < now && now < toolBoxXmlInfo.getEndTime().getTime()) {
				isRefresh = true;
			}
		}
		
		// 刷新礼包信息推送
		if (isRefresh) {
			int index = 0;
			RoleInfo roleInfo = null;
			for (Entry<Integer, RoleInfo> entry : RoleInfoMap.getRoleInfoEntrySet()) {
				roleInfo = entry.getValue();
				if (roleInfo == null || roleInfo.getLoginStatus() != 1) {
					continue;
				}
				synchronized (roleInfo) {
					if (isVersionChg) {
						// 礼包版本变化
						VipShopMgtService.dealRoleBoxVersionChg(roleInfo, toolBoxXmlInfo);
					}
					if (toolBoxXmlInfo.getBoxType() == ToolBoxInfo.TYPE_BOX_CHARGE) {
						SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHARGE, "");
					} else {
						SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_COIN_BOX, "");
					}
				}
				
				if (index++ > 500) {
					index = 0;
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						logger.error("Tool ModifyToolBox Error", e);
					}
				}
			}
		}
		
		GameLogService.insertToolOperatLog("ModifyToolBox", (String) list.get(0), "", "", "", toolBoxXml,
				(String) list.get(2), RESULT_SUCCESSFUL);
		
		result[0] = RESULT_SUCCESSFUL;
		return result;
	}
	
	/**
	 * 修改运营时限活动信息
	 * 
	 * @param list 传输信息
	 * @return String[] 处理结果 string[0]{0:失败 , 1：成功} string[1]{空：正常, 其他：错误消息}
	 */
	@ToolMsgHeader("ModifyToolOpAct")
	public String[] modifyToolOpAct(List<Object> list) {
		String[] result = { RESULT_FAILED, " " };

		if (list.size() != 3) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}

		String toolOpActXml = (String) list.get(1);

		ToolOpActivityInfo toolOpActXMLInfo = ToolHelper.parseToolOpActXml(toolOpActXml);
		if (toolOpActXMLInfo == null) {
			result[1] = Constants.GAME_TOOL_XML_ERROR;
			return result;
		}
		
		boolean isRefresh = false;
		boolean isVersionChg = false;
		
		long now = System.currentTimeMillis();
		ToolOpActivityInfo toolOpActInfo = ToolOpActMap.fetchInfoByGuid(toolOpActXMLInfo.getActNo());
		if (toolOpActInfo == null) {
			// 新增
			if (!ToolOpActDAO.getInstance().insertToolOpActInfo(toolOpActXMLInfo)) {
				result[1] = Constants.GAME_TOOL_OP_ACT_ERROR_2;
				return result;
			}
			
			ToolOpActMap.addToolOpActivityInfo(toolOpActXMLInfo, now);
			
			if (toolOpActXMLInfo.getRewardMap() != null) {
				for (ToolOpActivityRewardInfo rewardInfo : toolOpActXMLInfo.getRewardMap().values()) {
					rewardInfo.setOpActId(toolOpActXMLInfo.getId());
					if (ToolOpActRewardDAO.getInstance().insertToolOpActRewardInfo(rewardInfo)) {
						ToolOpActMap.addToolOpActivityRewardInfo(rewardInfo);
					}
				}
			}
			
		} else {
			// 修改
			toolOpActXMLInfo.setId(toolOpActInfo.getId());
			
			if (toolOpActXMLInfo.getActVersion() == toolOpActInfo.getActVersion()) {
				// 版本号不变，奖励条数不变
				if (toolOpActXMLInfo.getRewardMap() != null) {
					if (ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId()) == null 
							|| toolOpActXMLInfo.getRewardMap().size() != ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId()).size()) {
						result[1] = Constants.GAME_TOOL_OP_ACT_ERROR_4;
						return result;
					}
				}

				if (toolOpActXMLInfo.getRewardMap() != null) {
					for (ToolOpActivityRewardInfo rewardXMLInfo : toolOpActXMLInfo.getRewardMap().values()) {
						ToolOpActivityRewardInfo toolReward = ToolOpActMap.fetchToolOpActRewardInfo(toolOpActXMLInfo.getId(), rewardXMLInfo.getRewardNo());
						if (toolReward == null) {
							continue;
						}
						rewardXMLInfo.setId(toolReward.getId());
						rewardXMLInfo.setOpActId(toolOpActXMLInfo.getId());
						
						if (ToolOpActRewardDAO.getInstance().updateToolOpActRewardInfo(rewardXMLInfo)) {
							ToolOpActMap.addToolOpActivityRewardInfo(rewardXMLInfo);
						}
					}
				}
				
			} else {
				// 版本号修改,修改活动奖励配置
				if (ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId()) != null) {
					for (ToolOpActivityRewardInfo rewardInfo : ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId()).values()) {
						ToolOpActRewardDAO.getInstance().deleteToolOpActRewardInfo(rewardInfo);
					}
					
					// 清除旧活动的奖励配置
					ToolOpActMap.clearRewardOfToolOpAct(toolOpActInfo.getId());
					
					isVersionChg = true;
				}
				
				if (toolOpActXMLInfo.getRewardMap() != null) {
					for (ToolOpActivityRewardInfo rewardInfo : toolOpActXMLInfo.getRewardMap().values()) {
						rewardInfo.setOpActId(toolOpActXMLInfo.getId());
						if (ToolOpActRewardDAO.getInstance().insertToolOpActRewardInfo(rewardInfo)) {
							ToolOpActMap.addToolOpActivityRewardInfo(rewardInfo);
						}
					}
					
					isVersionChg = true;
				}
			}
			
			if (!ToolOpActDAO.getInstance().updateToolOpActInfo(toolOpActXMLInfo)) {
				result[1] = Constants.GAME_TOOL_OP_ACT_ERROR_3;
				return result;
			}
			
			ToolOpActMap.removeToolOpActivityInfo(toolOpActInfo);
			ToolOpActMap.addToolOpActivityInfo(toolOpActXMLInfo, now);
			
			if (!isRefresh) {
				if (toolOpActInfo.getActState() == 1 && 
						toolOpActInfo.getStartTime().getTime() < now && now < toolOpActInfo.getEndTime().getTime()) {
					isRefresh = true;
				}
			}
		}
		
		if (!isRefresh) {
			if (toolOpActXMLInfo.getActState() == 1 && 
					toolOpActXMLInfo.getStartTime().getTime() < now && now < toolOpActXMLInfo.getEndTime().getTime()) {
				isRefresh = true;
			}
		}
		
		// 处理活动版本变化,刷新活动信息推送
		if (isVersionChg || isRefresh) {
			OpActivityService.refreshOpActVersionChg(toolOpActXMLInfo, isVersionChg, isRefresh);
		}
		
		GameLogService.insertToolOperatLog("ModifyToolOpAct", (String) list.get(0), "", "", "", toolOpActXml,
				(String) list.get(2), RESULT_SUCCESSFUL);
		
		result[0] = RESULT_SUCCESSFUL;
		return result;
	}
	
	/**
	 * 修改登录弹出框样式
	 * @param list
	 * @return
	 */
	@ToolMsgHeader("modifyPopUpBoxType")
	public String[] modifyPopUpBoxType(List<Object> list) {
		String[] result = { RESULT_FAILED, " " };
		if (list.size() != 3) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}
		int toolOptype = NumberUtils.toInt((String) list.get(1));
		GameSettingInfo info = GameSettingMap.getValue(GameSettingKey.POP_UP_BOX_TYPE);
		if (info == null) {
			if (toolOptype != 0) {
				info = new GameSettingInfo();
				info.setKey(GameSettingKey.POP_UP_BOX_TYPE.getValue());
				info.setValue(toolOptype + "");
				if (GameSettingDAO.getInstance().insertGameSetting(info)) {
					GameSettingMap.addValue(info);
				}
			}
		} else {
			int oldValue = NumberUtils.toInt(info.getValue());
			if (toolOptype != oldValue) {
				if (GameSettingDAO.getInstance().updateGameSettingValue(GameSettingKey.POP_UP_BOX_TYPE, toolOptype+"")) {
					info.setValue(toolOptype + "");
				}
			}
		}
		GameLogService.insertToolOperatLog("modifyPopUpBoxType", (String) list.get(0), "", "", "", toolOptype+"",
				(String) list.get(2), RESULT_SUCCESSFUL);

		result[0] = RESULT_SUCCESSFUL;
		return result;
	}

	/**
	 * 在聊天频道内置顶一条系统公告
	 * @param list
	 * @return
	 */
	@ToolMsgHeader("modifySystemNotice")
	public String[] modifySystemNotice(List<Object> list) {
		String[] result = { RESULT_FAILED, " " };
		if (list.size() != 3) {
			result[1] = Constants.GAME_MSG_FIELD_SIZE_ERROR;
			return result;
		}
		boolean change = false;
		String toolOptype = (String) list.get(1);
		GameSettingInfo info = GameSettingMap.getValue(GameSettingKey.TOP_SYSTEM_NOTICE);
		if (info == null) {
			if (toolOptype.length() > 0) {
				info = new GameSettingInfo();
				info.setKey(GameSettingKey.TOP_SYSTEM_NOTICE.getValue());
				info.setValue(toolOptype);
				if (GameSettingDAO.getInstance().insertGameSetting(info)) {
					GameSettingMap.addValue(info);
					change = true;
				}
			}
		} else {
			String oldValue = info.getValue();
			if (!toolOptype.equals(oldValue)) {
				if (GameSettingDAO.getInstance().updateGameSettingValue(GameSettingKey.TOP_SYSTEM_NOTICE, toolOptype)) {
					info.setValue(toolOptype);
					change = true;
				}
			}
		}
		GameLogService.insertToolOperatLog("modifySystemNotice", (String) list.get(0), "", "", "", toolOptype+"",
				(String) list.get(2), RESULT_SUCCESSFUL);
		if(change){
			Set<Integer> set = RoleLoginMap.getSet();
			for (Integer roleId : set) {
				SceneService.sendRoleRefreshMsg(roleId, SceneService.REFRESH_TYPE_SYSTEM_NOTIFY, "");
			}
		}
		result[0] = RESULT_SUCCESSFUL;
		return result;
	}
}
