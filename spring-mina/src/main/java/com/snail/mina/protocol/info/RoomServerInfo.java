package com.snail.mina.protocol.info;

public class RoomServerInfo {

	private String serverIp; // 服务器IP
	private int serverPort; // 服务器端口
	private String serverName;// 服务器名称
	
	public RoomServerInfo() {
		
	}
	
	public RoomServerInfo(String serverIp, int serverPort, String serverName) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverName =serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String toString() {
		return serverIp + ":" + serverPort + "(" + serverName + ")";
	}
}
