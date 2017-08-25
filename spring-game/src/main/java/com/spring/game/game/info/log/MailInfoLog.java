package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 邮件日志
 * @author zenggang
 */
public class MailInfoLog extends BaseTO {
	public static final int TYPE_GET_ATTACHMENT = 0;// 领取附件
	public static final int TYPE_SEND = 1;// 发送邮件
	public static final int TYPE_DEL = 2;// 删除邮件

	private long mailId;// 邮件编号
	private String sendRoleName;// 发送人
	private String receiveRoleName;// 接收人
	private Timestamp time;// 时间
	private int actType;// 0-领取附件 1-发送附件
	private String attachment;// 附件信息
	private String recAcc;//收件人账号
	private int recId;//收件人角色ID

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
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

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getActType() {
		return actType;
	}

	public void setActType(int actType) {
		this.actType = actType;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getRecAcc() {
		return recAcc;
	}

	public void setRecAcc(String recAcc) {
		this.recAcc = recAcc;
	}
	

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
