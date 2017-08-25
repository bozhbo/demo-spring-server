package com.snail.webgame.game.protocal.mail.mailSend;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SendReq extends MessageBody{

 
	private byte mailType;  //1-系统 2-玩家
	private long sendRoleId;
	private String sendRoleName;
	private String recRoleIds;
	private String mailTitle;
	private String mailContent;
	private String attachment;
	private String reserve;
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mailType", 0);
		ps.add("sendRoleId", 0);
		ps.addString("sendRoleName", "flashCode", 0);
		ps.addString("recRoleIds", "flashCode", 0);
		ps.addString("mailTitle", "flashCode", 0);
		ps.addString("mailContent", "flashCode", 0);
		ps.addString("attachment", "flashCode", 0);
		ps.addString("reserve", "flashCode", 0);
	}
	
	public long getSendRoleId() {
		return sendRoleId;
	}
	public void setSendRoleId(long sendRoleId) {
		this.sendRoleId = sendRoleId;
	}
	public String getSendRoleName() {
		return sendRoleName;
	}
	public void setSendRoleName(String sendRoleName) {
		this.sendRoleName = sendRoleName;
	}
	
	public String getRecRoleIds() {
		return recRoleIds;
	}

	public void setRecRoleIds(String recRoleIds) {
		this.recRoleIds = recRoleIds;
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

	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public byte getMailType() {
		return mailType;
	}

	public void setMailType(byte mailType) {
		this.mailType = mailType;
	}
	 

}
