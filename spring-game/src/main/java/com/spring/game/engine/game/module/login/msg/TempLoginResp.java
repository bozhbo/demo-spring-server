package com.snail.webgame.engine.game.module.login.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageResp;

public class TempLoginResp extends GameMessageResp {
	
	private int result;
	private int roleId;// 角色Id
	private String account;// 玩家登陆帐号
	private String roleName;// 角色名称
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, result);
		
		if (result == 1) {
			setInt(buffer, order, roleId);
			setString(buffer, order, account);
			setString(buffer, order, roleName);
		}
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
}
