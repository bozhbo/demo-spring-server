package com.spring.logic.role.service;

import java.util.function.Function;

import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.logic.message.request.world.login.LoginResp;
import com.spring.logic.role.info.RoleInfo;

public interface RoleLoginService {

	public void roleLogin(int gateId, int roleId, String account, String password, String validate, RoomMessageHead head, LoginResp resp, Function<LoginResp, Integer> function);
	
	public void roleLogout(RoleInfo roleInfo);
}
