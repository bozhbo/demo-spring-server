package com.spring.world.io.process.role.disconnect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.connect.DisconnectReq;
import com.spring.logic.message.request.world.login.LoginReq;
import com.spring.logic.message.request.world.login.LoginResp;
import com.spring.logic.role.service.RoleLoginService;
import com.spring.world.io.process.role.login.LoginProcessor;

/**
 * DisconnectReq.java
 * 
 * @author Administrator
 *
 */
public class DisconnectProcessor implements IProcessor {

	private static final Log logger = LogFactory.getLog(LoginProcessor.class);

	private RoleLoginService roleLoginService;

	@Override
	public void processor(Message message) {
		DisconnectReq req = (DisconnectReq) message.getiRoomBody();

		this.roleLoginService.roleLogout(req.getRoleId());

		logger.info("role logout " + req.getAccount() + ", roleId = " + req.getRoleId());
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return DisconnectReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_DISCONNECT_REQ;
	}

	@Autowired
	public void setRoleLoginService(RoleLoginService roleLoginService) {
		this.roleLoginService = roleLoginService;
	}
}
