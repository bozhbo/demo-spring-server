package com.spring.logic.server.info;

import org.apache.mina.common.IoSession;

public class RoomServerInfo {

	private int roomServerId;
	private String serverName;
	private String ip;
	private int port;
	private int roomCount;
	private int roleCount;
	
	private IoSession session;
	
	public int getRoomServerId() {
		return roomServerId;
	}

	public void setRoomServerId(int roomServerId) {
		this.roomServerId = roomServerId;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public int getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(int roleCount) {
		this.roleCount = roleCount;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}
	
	
}
