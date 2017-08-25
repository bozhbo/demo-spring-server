package com.snail.webgame.game.protocal.mail.getAttachment;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetAttachmentReq extends MessageBody {

	private long mailId; // 邮件ID

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mailId", 0);
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}
}
