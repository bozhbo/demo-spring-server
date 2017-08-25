package com.snail.webgame.game.thread;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.DiffMail;
import com.snail.webgame.game.common.DiffMailMessage;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.MailMessage;
import com.snail.webgame.game.common.TimeMessage;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.info.MailInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.log.MailInfoLog;
import com.snail.webgame.game.protocal.mail.service.MailService;

/**
 * 处理可以慢慢处理的事件，异步
 * 
 * @author xiasd
 *
 */
public class SendServerMsgThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static BlockingQueue<TimeMessage> queue = new LinkedBlockingQueue<TimeMessage>(Integer.MAX_VALUE);

	public SendServerMsgThread() {
		this.setName("SendServerMsgThread");
	}
	@Override
	public void run() {
		TimeMessage timeMessage = null;
		
		while (true) {
			try {
				if(timeMessage == null){
					timeMessage = queue.take();
				}

				if (timeMessage.getType() == ETimeMessageType.SEND_BATCH_SYS_MAIL) {
					sendRolesMail((MailMessage) timeMessage);
				} else if (timeMessage.getType() == ETimeMessageType.SEND_BATCH_DIFF_MAIL) {
					sendRolesDiffMail((DiffMailMessage) timeMessage);
				}

				timeMessage = null;
			} catch (Exception e) {
				logger.error("SendServerMsgThread, takeMessage error", e);
				
				if (timeMessage != null && timeMessage.getReTryCount() < 3) {
					timeMessage.setReTryCount(timeMessage.getReTryCount() + 1);
				} else {
					if (timeMessage != null) {
						logger.error("SendServerMsgThread, Failed " + timeMessage.getType());
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
	}
	
	/**
	 * 系统发送指定批量玩家发相同邮件
	 * 
	 */
	private void sendRolesMail(MailMessage mailMessage) throws InterruptedException {
		List<Integer> roleIdList = mailMessage.getRoleIdList();
		String attachment = mailMessage.getAttachment();
		
		// 验证附件个数
		String[] attachments = StringUtils.split(attachment, ";");
		
		if (attachments != null) {
			if (attachments.length > GameValue.MAIL_ATTACHMENT_MAX_NUM) {
				logger.error("SendServerMsgThread  :  send mail failed , attachment is too many! , attachment = " + attachment);
				return ;
			}
		}

		
		if (roleIdList != null && roleIdList.size() > 0) {
			List<Integer> roleIdListTemp = new ArrayList<Integer>(roleIdList);// 防止出异常，重复发送邮件，临时缓存
			List<Integer> roleIdRealList = new ArrayList<Integer>();
			List<MailInfo> mailList = new ArrayList<MailInfo>();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			for (int i = 0; i < roleIdListTemp.size(); i++) {
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleIdListTemp.get(i));

				if (roleInfo == null) {
					continue;
				}
				
				MailInfo mail = new MailInfo();
				mail.setReceiveRoleId(roleInfo.getId());
				mail.setReceiveRoleName(roleInfo.getRoleName());

				mail.setAttachmentStr(attachment);
				mail.setMailContent(mailMessage.getContent());
				mail.setMailTitle(mailMessage.getTitle());
				mail.setMailType((byte) MailInfo.SYSTEM_MAIL);
				mail.setReserve(mailMessage.getReserve());
				mail.setSendRoleId(0);
				mail.setSendRoleName(Resource.getMessage("game", "GAME_SYSTEM"));
				mail.setSendTime(timestamp);
				mail.setRecAcc(roleInfo.getAccount());
				
				mailList.add(mail);
				roleIdRealList.add(roleInfo.getId());
				
				// 每500人发一次，或遍历完
				if((i > 0 && i % 500 == 0) || i+1 == roleIdListTemp.size()){
					// 保存邮件
					MailDAO.getInstance().saveMail(mailList, roleIdRealList);
					roleIdList.removeAll(roleIdRealList);// 防止出异常，重复发送邮件
					
					for (int j = 0; j < mailList.size(); j++) {
						MailInfo info = mailList.get(j);
						// info.setId(idList.get(j));
						// 通知收件人有新邮件
						// noticeNewMail(info.getReceiveId());
						MailService.sendNewMail(info.getReceiveRoleId(), 1);

						// 每100人，暂停200毫秒
						if (j > 0 && j % 100 == 0) {
							TimeUnit.MILLISECONDS.sleep(200);
						}
					}
					
					if (StringUtils.isNotBlank(attachment)) {
						GameLogService.saveMailInfoLog(mailList, MailInfoLog.TYPE_SEND);
					}
					roleIdRealList.clear();
					mailList.clear();
					timestamp = new Timestamp(System.currentTimeMillis());
				}
			}
		}
	}

	/**
	 * 系统发送指定多人不同邮件
	 * 
	 */
	private void sendRolesDiffMail(DiffMailMessage mailMessage) throws InterruptedException {
		
		List<DiffMail> mails = mailMessage.getDiffMailList();
		
		if (mails != null && mails.size() > 0) {
			List<DiffMail> mailsTemp = new ArrayList<DiffMail>(mails);// 防止出异常，重复发送邮件
			List<Integer> roleIdRealList = new ArrayList<Integer>();
			List<MailInfo> mailList = new ArrayList<MailInfo>();
			List<DiffMail> savedMailsTemp = new ArrayList<DiffMail>();//已经发送的邮件的临时缓存
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			DiffMail diffMail;
			
			for (int i = 0; i < mailsTemp.size(); i++) {
				diffMail = mailsTemp.get(i);
				
				if(diffMail == null){
					continue;
				}
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(diffMail.getRoleId());
				
				if (roleInfo == null) {
					continue;
				}
				
				MailInfo mail = new MailInfo();
				mail.setReceiveRoleId(roleInfo.getId());
				mail.setReceiveRoleName(roleInfo.getRoleName());
				
				// 验证附件个数
				String[] attachments = StringUtils.split(diffMail.getAttachment(), ";");
				
				if (attachments != null) {
					if (attachments.length > GameValue.MAIL_ATTACHMENT_MAX_NUM) {
						logger.error("SendServerMsgThread  :  send mail failed , attachment is too many! , attachment = " + diffMail.getAttachment());
						continue;
					}
				}
				mail.setAttachmentStr(diffMail.getAttachment());
				mail.setMailContent(diffMail.getContent());
				mail.setMailTitle(diffMail.getTitle());
				mail.setMailType((byte) MailInfo.SYSTEM_MAIL);
				mail.setReserve(diffMail.getReserve());
				mail.setSendRoleId(0);
				mail.setSendRoleName(Resource.getMessage("game", "GAME_SYSTEM"));
				mail.setSendTime(timestamp);
				mail.setRecAcc(roleInfo.getAccount());
				
				mailList.add(mail);
				savedMailsTemp.add(diffMail);
				roleIdRealList.add(roleInfo.getId());
				
				// 每500人发一次，或遍历完
				if((i > 0 && i % 500 == 0) || i+1 == mailsTemp.size()){
					// 保存邮件
					MailDAO.getInstance().saveMail(mailList, roleIdRealList);
					mails.removeAll(savedMailsTemp);// 防止出异常，重复发送邮件
					
					List<MailInfo> hasAttachmentMails = null;
					
					for (int j = 0; j < mailList.size(); j++) {
						MailInfo info = mailList.get(j);
						// info.setId(idList.get(j));
						// 通知收件人有新邮件
						// noticeNewMail(info.getReceiveId());
						MailService.sendNewMail(info.getReceiveRoleId(), 1);
						
						// 每100人，暂停200毫秒
						if (j > 0 && j % 100 == 0) {
							TimeUnit.MILLISECONDS.sleep(200);
						}
						
						if(StringUtils.isNotBlank(info.getAttachmentStr())){
							if(hasAttachmentMails == null){
								hasAttachmentMails = new ArrayList<MailInfo>();
							}
							hasAttachmentMails.add(info);
						}
						
					}
					GameLogService.saveMailInfoLog(hasAttachmentMails, MailInfoLog.TYPE_SEND);
					
					savedMailsTemp.clear();
					roleIdRealList.clear();
					mailList.clear();
					timestamp = new Timestamp(System.currentTimeMillis());
				}
			}
		}
	}
	
	/**
	 * 添加消息
	 * 
	 * @param sendMessageInfo
	 */
	public static void addMessage(TimeMessage timeMessage) {
		try {
			queue.put(timeMessage);
		} catch (InterruptedException e) {
			logger.error("SendServerMsgThread addMessage error", e);
		}
	}
}
