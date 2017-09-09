package com.spring.logic.role.service.impl;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.login.LoginResp;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.role.service.RoleLoginService;

@Service
public class RoleLoginServiceImpl implements RoleLoginService {
	
	private MessageService messageService;

	@Override
	public void roleLogin(int gateId, int roleId, String account, String password, String validate, LoginResp resp, Function<LoginResp, Integer> function) {
		RoleInfo roleInfo = RoleCache.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			// TODO load from DB
			roleInfo = new RoleInfo();
			roleInfo.setRoleId(account.hashCode());
			RoleCache.addRoleInfo(roleInfo);
		}
		
		roleInfo.setGateId(gateId);
		
		resp.setResult(1);
		resp.setAccount(account);
		resp.setGateServerId(gateId);
		resp.setRoleId(account.hashCode());
		resp.setRoleName(account);
		
		int errorCode = function.apply(resp);
		
		if (errorCode == 1) {
			this.messageService.sendGateMessage(gateId, GameMessageType.GAME_CLIENT_LOGIN_RECEIVE, resp);
		} else {
			Message message = this.messageService.createErrorMessage(errorCode, "");
			this.messageService.sendGateMessage(gateId, message);
		}
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
