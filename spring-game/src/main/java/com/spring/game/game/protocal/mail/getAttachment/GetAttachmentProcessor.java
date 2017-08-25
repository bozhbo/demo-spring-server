package com.snail.webgame.game.protocal.mail.getAttachment;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.mail.service.MailService;

public class GetAttachmentProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	MailService mailService;

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GetAttachmentReq req = (GetAttachmentReq) message.getBody();
		GameMessageHead header = (GameMessageHead) message.getHeader();

		int roleId = header.getUserID0();
		long mailId = req.getMailId();

		header.setMsgType(Command.MAIL_GET_ATTACHMENT_RESP);
		GetAttachmentResp resp = null;
		resp = mailService.getAttachment(roleId, (int)mailId);
		message.setBody(resp);
		int result = resp.getResult();
		message.setHeader(header);
		if (response.write(message)) {
			if (logger.isInfoEnabled()) {
				logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_21") + ":result=" + result + ",roleId=" + roleId
						+ ",mailID=" + req.getMailId());
			}
		}

	}
}
