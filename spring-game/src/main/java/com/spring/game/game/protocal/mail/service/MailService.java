package com.snail.webgame.game.protocal.mail.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.MailMessage;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.info.MailInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mail.getAttachment.GetAttachmentResp;
import com.snail.webgame.game.protocal.mail.remind.MailRemindResp;
import com.snail.webgame.game.thread.SendServerMsgThread;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;

public class MailService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 获取邮件附件
	 * @param roleId
	 * @param mailId
	 * @return
	 */
	public GetAttachmentResp getAttachment(int roleId, int mailId) {
		GetAttachmentResp resp = new GetAttachmentResp();
		resp.setResult(1);
		resp.setMailId(mailId);

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_5);
			return resp;
		}

		synchronized (roleInfo) {

			MailInfo mailInfo = MailDAO.getInstance().getMailInfo(mailId);
			if (mailInfo == null) {
				resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_1);
				return resp;
			}
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_10);
				return resp;
			}

			if (mailInfo.getReceiveRoleId() != roleId) {
				resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_6);
				return resp;
			}

			byte flag = mailInfo.getFlag();
			List<MailAttachment> attachments = mailInfo.getAttachments();
			if (flag == 1 || attachments.isEmpty()) {
				resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_7);
				return resp;
			}

			List<DropInfo> addList = new ArrayList<DropInfo>(); // 获得武将

			getAttachmentList(attachments,addList);

			if (MailDAO.getInstance().getMailAttachment(mailId)) {
				// 记录领取附件日志
				int result = ItemService.addPrize(ActionType.action7.getType(), roleInfo, addList, null,
						null, null, null, null, null,null, null, null, true);

				if (result == 1) {
					resp.setResult(1);
					GameLogService.insertPlayActionLog(roleInfo, ActionType.action7.getType(), mailInfo.getId()+","+mailInfo.getAttachmentStr());
				} else {
					resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_8);
				}
			} else {
				resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_9);
				return resp;
			}
			//如果是EAI邮件和兑换码兑换奖励的有奖，一个角色领掉就把该账号下所有该邮件都删掉
			if(mailInfo.getMailTitle() != null)
			{
				if(mailInfo.getMailTitle().equals(Resource.getMessage("game", "ACTIVE_CODE_TITLE"))||
						mailInfo.getMailTitle().equals(Resource.getMessage("game", "SYSTEM_SEND_TITLE"))){
					
					HashMap<Integer, RoleInfo> roleMap = RoleInfoMap.getRoleMapByAccount(roleInfo.getAccount());
					for(int roleId1 : roleMap.keySet())
					{
						RoleInfo otherRole = roleMap.get(roleId1);
						if(otherRole == null)
						{
							continue;
						}
						if(otherRole.getId()==roleId){
							continue;
						}
						if(!MailDAO.getInstance().deleteMailByRoleNameAndTitleAndAttachMent(otherRole.getRoleName(), 
								mailInfo.getMailTitle(), MailDAO.encoderAttachment(mailInfo.getAttachments()))){
							resp.setResult(ErrorCode.GET_ATTACHMENT_FAIL_11);
							return resp;
						}
					}
				}
			}
			
		}

		return resp;
	}

	/**
	 * 获取附件分类列表
	 * @param attachments
	 * @param addHeroList 武将
	 * @param dropNotItemMap 资源奖励
	 * @param dropItemMap 装备、道具
	 */
	private void getAttachmentList(List<MailAttachment> attachments, List<DropInfo> addList) {
		String itemNo = "";
		Map<String, DropInfo> dropNotItemMap = new HashMap<String, DropInfo>();
		Map<String, DropInfo> dropItemMap = new HashMap<String, DropInfo>();
		
		for (MailAttachment attachment : attachments) {
			itemNo = attachment.getItemNo();
			if (AbstractConditionCheck.isResourceType(itemNo)) {
				// 资源奖励
				DropInfo dropInfo = dropNotItemMap.get(itemNo);
				if (dropInfo == null) {
					dropInfo = new DropInfo();
					dropInfo.setItemNo(itemNo);
					dropNotItemMap.put(itemNo, dropInfo);
				}
				dropInfo.setItemNum(dropInfo.getItemNum() + attachment.getNumber());
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				addList.add(dropInfo);

			} else if (itemNo.startsWith(GameValue.EQUIP_N0)//
					|| itemNo.startsWith(GameValue.PROP_N0)
					|| itemNo.startsWith(GameValue.WEAPAN_NO)) {
				// 道具、装备
				if (itemNo.startsWith(GameValue.EQUIP_N0)) {
					if (EquipXMLInfoMap.getEquipXMLInfo(Integer.valueOf(itemNo)) == null) {
						continue;
					}
				} else if (itemNo.startsWith(GameValue.PROP_N0)) {
					if (PropXMLInfoMap.getPropXMLInfo(Integer.valueOf(itemNo)) == null) {
						continue;
					}
				} else if (itemNo.startsWith(GameValue.WEAPAN_NO)) {
					if (WeaponXmlInfoMap.getWeaponXmlInfoByNo(Integer.valueOf(itemNo)) == null) {
						continue;
					}
				}
				DropInfo dropInfo = dropItemMap.get(itemNo);
				if (dropInfo == null) {
					dropInfo = new DropInfo();
					dropInfo.setItemNo(itemNo);
					dropInfo.setParam(attachment.getLevel() + "");
					dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
					dropItemMap.put(itemNo, dropInfo);
				}
				dropInfo.setItemNum(dropInfo.getItemNum() + attachment.getNumber());
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				addList.add(dropInfo);
			} else if (StringUtils.startsWith(itemNo, GameValue.HERO_N0)) {
				// 直接获得武将
				HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(Integer.valueOf(itemNo));
				if (heroXmlInfo != null) {
					DropInfo dropInfo = new DropInfo(itemNo, attachment.getNumber());
					//dropInfo.setParam(heroXmlInfo.getStar() + "");
					addList.add(dropInfo);
				}
			}
		}
	}

	/**
	 * 系统通过邮件给玩家发送奖励
	 * @param roleName 接收者，可以多个 角色名,角色名,......
	 * @param attachment 格式 物品编号,数量,等级,领取状态; 物品编号,数量,等级,领取状态 <br>
	 *            等级为0表示物品没等级概念。 <code>eg.3600001,10,1,0;3600003,9,2,0</code>
	 * @param title
	 * @param content
	 * @param reserve
	 */
	public static void pushMailPrize(String roleIds, String attachment, String title, String content, String reserve)
	{
		List<Integer> roleIdList = new ArrayList<Integer>();
		
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		
		if(roleIds != null && !"".equalsIgnoreCase(roleIds))
		{
			String[] recRoleIds = StringUtils.split(roleIds, ",");
			for(int i = 0 ; i < recRoleIds.length ; i++)
			{
				roleIdList.add(Integer.valueOf(recRoleIds[i]));

			}
		}
		else
		{
			for(int roleId : roleMap.keySet())
			{
				roleIdList.add(roleId);
			}
			
			logger.info("system send mail,attachment="+attachment+",title="+title+",content="+content);
		}
		
		MailMessage message = new MailMessage(ETimeMessageType.SEND_BATCH_SYS_MAIL,roleIdList,attachment,content,title,reserve);
		SendServerMsgThread.addMessage(message);
		
	}
	
	/**
	 * 发送新邮件提醒
	 * 
	 * @param roleId
	 * @param messageId
	 */
	public static void sendNewMail(int roleId, int messageId) {
		// 玩家只有在线才发送新邮件提醒
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo.getLoginStatus() != 1) {
			return;
		}
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();

		MailRemindResp resp = new MailRemindResp();
		resp.setResult(messageId);
		resp.setFlag(2);

		head.setMsgType(Command.MAIL_REMIND_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);

		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-"
				+ roleInfo.getGateServerId());

		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}

}
