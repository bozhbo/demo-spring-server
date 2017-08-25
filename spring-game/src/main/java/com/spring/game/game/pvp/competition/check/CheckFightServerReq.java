package com.snail.webgame.game.pvp.competition.check;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

public class CheckFightServerReq extends BaseRoomResp {

	private String fightServer;
	private String serverName;
	private long roleId;

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setString(buffer, order, fightServer);
		setString(buffer, order, serverName);
		setLong(buffer, order, roleId);
	}

	public String getFightServer() {
		return fightServer;
	}

	public void setFightServer(String fightServer) {
		this.fightServer = fightServer;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	
}
