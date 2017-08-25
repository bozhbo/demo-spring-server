package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.List;

import com.snail.webgame.engine.common.to.BaseTO;

public class MailInfo extends BaseTO {

	/**
	 * 邮件类型 0-玩家之间邮件 1系统邮件
	 */
	public static final int ROLE_MAIL = 0;
	public static final int SYSTEM_MAIL = 1;

	private byte mailType;// 邮件类型
	private int sendRoleId; // 发送人角色Id
	private String sendRoleName; // 发送人角色名称
	private int receiveRoleId; // 接收人角色Id
	private String receiveRoleName; // 接收人角色名称
	private String mailTitle; // 邮件标题
	private String mailContent;// 邮件内容
	private Timestamp sendTime; // 邮件发送时间(yyyy-MM-dd HH:mm:ss)
	private byte isRead;// 0-未读 1-已读
	private String attachmentStr;// 附件明细
	private List<MailAttachment> attachments;// 附件明细
	private byte flag; // 附件领取标记 0-未领 1-已领
	private String reserve;
	private String recAcc;//收件人账号

	public byte getMailType() {
		return mailType;
	}

	public void setMailType(byte mailType) {
		this.mailType = mailType;
	}

	public String getSendRoleName() {
		return sendRoleName;
	}

	public void setSendRoleName(String sendRoleName) {
		this.sendRoleName = sendRoleName;
	}

	public String getReceiveRoleName() {
		return receiveRoleName;
	}

	public void setReceiveRoleName(String receiveRoleName) {
		this.receiveRoleName = receiveRoleName;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public byte getIsRead() {
		return isRead;
	}

	public void setIsRead(byte isRead) {
		this.isRead = isRead;
	}

	public String getAttachmentStr() {
		return attachmentStr;
	}

	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}

	public List<MailAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	
	public String getRecAcc() {
		return recAcc;
	}

	public void setRecAcc(String recAcc) {
		this.recAcc = recAcc;
	}


	public static class MailAttachment {

		private String itemNo;
		private int number;
		private int level = 0; // 装备品质/武将星级
		private int quality;

		public MailAttachment() {

		}

		public MailAttachment(String itemNo, int number, int level,int quality) {
			this.itemNo = itemNo;
			this.number = number;
			this.level = level;
			this.quality = quality;
		}

		public String getItemNo() {
			return itemNo;
		}

		public void setItemNo(String itemNo) {
			this.itemNo = itemNo;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getQuality() {
			return quality;
		}

		public void setQuality(int quality) {
			this.quality = quality;
		}
		
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

	public int getSendRoleId() {
		return sendRoleId;
	}

	public void setSendRoleId(int sendRoleId) {
		this.sendRoleId = sendRoleId;
	}

	public int getReceiveRoleId() {
		return receiveRoleId;
	}

	public void setReceiveRoleId(int receiveRoleId) {
		this.receiveRoleId = receiveRoleId;
	}

	public static int getRoleMail() {
		return ROLE_MAIL;
	}

	public static int getSystemMail() {
		return SYSTEM_MAIL;
	}

}
