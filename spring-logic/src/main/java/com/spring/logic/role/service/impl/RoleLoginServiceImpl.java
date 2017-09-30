package com.spring.logic.role.service.impl;

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.init.InitResp;
import com.spring.logic.message.request.world.login.LoginResp;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.role.service.RoleLoginService;
import com.spring.logic.util.LogicUtil;

@Service
public class RoleLoginServiceImpl implements RoleLoginService {
	
	private static final Log logger = LogFactory.getLog(RoleLoginServiceImpl.class);
	
	private MessageService messageService;

	@Override
	public void roleLogin(int gateId, int roleId, String account, String password, String validate, RoomMessageHead head, LoginResp resp, Function<LoginResp, Integer> function) {
		RoleInfo roleInfo = RoleCache.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			// TODO load from DB
			roleInfo = new RoleInfo();
			roleInfo.setRoleId(LogicUtil.getSequenceId());
			RoleCache.addRoleInfo(roleInfo);
		}
		
		roleInfo.setGold(50000000);
		roleInfo.setGateId(gateId);
		
		head.setMsgType(GameMessageType.GAME_CLIENT_LOGIN_RECEIVE);
		head.setRoleId(roleInfo.getRoleId());
		
		resp.setResult(1);
		resp.setAccount(account);
		resp.setGateServerId(gateId);
		resp.setRoleId(account.hashCode());
		resp.setRoleName(account);
		
		int errorCode = function.apply(resp);
		
		if (errorCode == 1) {
			Message message = this.messageService.createMessage(head, resp);
			this.messageService.sendGateMessage(gateId, message);
		} else {
			messageService.sendGateMessage(gateId, messageService.createErrorMessage(roleInfo.getRoleId(), errorCode, ""));
		}
		
		logger.info("role login end " + head.getRoleId());
	}
	
	@Override
	public void roleLogout(RoleInfo roleInfo) {
		
	}
	
	@Override
	public void roleInit(RoleInfo roleInfo) {
		InitResp resp = new InitResp();
		resp.setGold(roleInfo.getGold());
		resp.setHead(roleInfo.getHeader());
		resp.setRoleId(roleInfo.getRoleId());
		resp.setRoleName(roleInfo.getRoleName());
		resp.setRoomId(roleInfo.getRoomId());
		resp.setVipLevel(roleInfo.getVipLevel());
		
		messageService.sendGateMessage(roleInfo.getGateId(), roleInfo.getRoleId(), GameMessageType.GAME_CLIENT_INIT_RECEIVE, resp);
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
