package com.spring.world.io.process.role.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;

@Component
public class LoginProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(LoginProcessor.class);
	
	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		LoginReq req = (LoginReq)message.getiRoomBody();
		
		RoleInfo roleInfo = RoleCache.getRoleInfo(head.getRoleId());
		
		if (roleInfo == null) {
			roleInfo = new RoleInfo();
			roleInfo.setRoleId(req.getAccount().hashCode());
			RoleCache.addRoleInfo(roleInfo);
		}
		
		roleInfo.setGateId(head.getGateId());
		
		LoginResp resp = new LoginResp();
		resp.setAccount(req.getAccount());
		resp.setGateServerId(head.getGateId());
		resp.setResult(1);
		resp.setRoleId(req.getAccount().hashCode());
		resp.setRoleName(req.getAccount());
		
		head.setRoleId(resp.getRoleId());
		head.setMsgType(GameMessageType.GAME_CLIENT_LOGIN_RECEIVE);
		message.setiRoomBody(resp);
		message.sendMessage();
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return LoginReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_LOGIN_SEND;
	}

}
