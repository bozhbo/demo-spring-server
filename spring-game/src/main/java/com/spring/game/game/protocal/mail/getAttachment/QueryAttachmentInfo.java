package com.snail.webgame.game.protocal.mail.getAttachment;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryAttachmentInfo extends MessageBody {
	private String sendName;
	private long sendTime;
	private String attachment;
	private long mailId;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("sendName", "flashCode", 0);
		ps.add("sendTime", 0);
		ps.addString("attachment", "flashCode", 0);
		ps.add("mailId", 0);
	}
	
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getMailId() {
		return mailId;
	}
	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

}
