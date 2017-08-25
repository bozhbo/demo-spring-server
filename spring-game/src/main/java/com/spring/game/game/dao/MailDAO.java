package com.snail.webgame.game.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.MailInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;

public class MailDAO extends SqlMapDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static class InternalClass {
		public final static MailDAO instance = new MailDAO();
	}

	public static MailDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 获取邮件
	 * @param mailId
	 * @return
	 */
	public MailInfo getMailInfo(int id) {
		MailInfo info = (MailInfo) getSqlMapClient(DbConstants.GAME_DB).query("selectMailInfoById", id);
		if (info != null) {
			info.setAttachments(paserAttachment(info.getAttachmentStr()));
		}
		return info;
	}

	/**
	 * 解析邮件附件
	 * 
	 * @param attachmentStr
	 * @return
	 */
	private static List<MailAttachment> paserAttachment(String attachmentStr) {
		List<MailAttachment> list = null;
		if (StringUtils.isNotBlank(attachmentStr)) {
			String[] attachments = StringUtils.split(attachmentStr, ';');
			list = new ArrayList<MailInfo.MailAttachment>(attachments.length);
			for (int i = 0; i < attachments.length; i++) {
				if (StringUtils.isNotBlank(attachments[i])) {
					String[] attachment = StringUtils.split(attachments[i], ',');
					if (attachment.length == 4) {
						String[] itemType = attachment[0].split("_");
						if(itemType.length == 2)
						{
							list.add(new MailAttachment(itemType[1], NumberUtils.toInt(attachment[1]), NumberUtils
									.toInt(attachment[2]),NumberUtils.toInt(attachment[3])));
						}
						else
						{
							list.add(new MailAttachment(attachment[0], NumberUtils.toInt(attachment[1]), NumberUtils
									.toInt(attachment[2]),NumberUtils.toInt(attachment[3])));
						}
					}
				}
			}
		} else {
			list = Collections.emptyList();
		}
		return list;
	}

	/**
	 * 格式化附件List to String
	 * 
	 * @param attachments
	 * @return
	 */
	public static String encoderAttachment(List<MailAttachment> attachments) {
		StringBuilder builder = new StringBuilder();
		if (attachments != null && attachments.size() > 0) {
			for (int i = 0; i < attachments.size(); i++) {
				MailAttachment mailAttachment = attachments.get(i);
				if (mailAttachment.getItemNo().startsWith(GameValue.EQUIP_N0)) {
					builder.append("Equip_" + mailAttachment.getItemNo());
					builder.append(",");
				} else if (mailAttachment.getItemNo().startsWith(GameValue.PROP_N0)) {
					builder.append("Prop_" + mailAttachment.getItemNo());
					builder.append(",");
				} else {
					builder.append(mailAttachment.getItemNo());
					builder.append(",");
				}
				builder.append(mailAttachment.getNumber());
				builder.append(",");
				builder.append(mailAttachment.getLevel());
				builder.append(",");
				builder.append(mailAttachment.getQuality());
				builder.append(",");
				if (i < attachments.size() - 1) {
					builder.append(";");
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 邮件领取附件
	 * 
	 * @param mailId
	 * @return
	 */
	public boolean getMailAttachment(int mailId) {
		MailInfo to = new MailInfo();
		to.setId(mailId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("getMailAttachment", to);
		} catch (Exception e) {
			logger.error("MailDAO.getMailAttachment error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 保存邮件
	 * 
	 * @param list
	 * @param receiveIds
	 * @return
	 */
	public void saveMail(List<MailInfo> list, List<Integer> receiveIds) {
		if (receiveIds == null || receiveIds.size() <= 0) {
			return;
		}
		SqlMapClient client = getSqlMapClient("GAME_DB", ExecutorType.BATCH, false);
		
		try {
//			List<Long> delList = new ArrayList<Long>();
//			
//			// 判断邮件数量上限，删除最老的一封邮件(没有附件或附件已经领取)
//			HashMap<String, Object> to = new HashMap<String, Object>();
//			to.put("receiveIds", receiveIds);
//			to.put("flag", 1);
//			List<HashMap<String, Object>> countlist = client.queryList("selectMailCountbyReceiveIds", to);
//			
//			for (HashMap<String, Object> map : countlist) {
//				long count = (Long) map.get("COUNT");
//				long receiveId = (Long) map.get("N_RECEIVE_ID");
//				long minId = (Long) map.get("MIN_N_ID");
//				
//				if (count >= GameValue.SAVE_MAIL_MAX && receiveIds.contains((int)receiveId)) {
//					delList.add(minId);
//				}
//			}
//			
//			// 删除符合条件的邮件
//			if (delList != null && delList.size() > 0) {
//				client.deleteBatch("deleteMailInfobyIds", delList);				
//			}

			// 插入新邮件
			for(MailInfo info : list){
				client.insert("insertMailInfo", info);
			}
			
			client.commit();
		} catch (Exception e) {
			client.rollback();
			if (logger.isErrorEnabled()) {
				logger.error("saveMail error!", e);
			}
		}
	}
	
	/**
	 * 根据角色名和邮件标题和邮件附件，删除邮件(只删一条记录)
	 * @param roleName
	 * @param title
	 * @param attachMent
	 * @return
	 */
	public boolean deleteMailByRoleNameAndTitleAndAttachMent(String roleName,String title,String attachMent){
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("roleName", roleName);
		map.put("title", title);
		map.put("attachMent", attachMent);
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteMailBYRoleNameAndTitle", map);
			return true;
		} catch (Exception e) {
			logger.error("MailDAO.deleteMailByRoleNameAndTitleAndAttachMent error!",e);
			return false;
		}
	}
	
}
