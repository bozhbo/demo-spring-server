package com.snail.webgame.engine.gate.common;

public class LocalConfig {

	private int serverId;

	private String localIP;
	private String localPort;

	private String romateIP;
	private String romatePort;
	private byte[] gateServerId;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getLocalIP() {
		return localIP;
	}

	public String getLocalPort() {
		return localPort;
	}

	public void setLocalPort(String localPort) {
		this.localPort = localPort;
	}

	public String getRomateIP() {
		return romateIP;
	}

	public void setRomateIP(String romateIP) {
		this.romateIP = romateIP;
	}

	public String getRomatePort() {
		return romatePort;
	}

	public void setRomatePort(String romatePort) {
		this.romatePort = romatePort;
	}

	public byte[] getGateServerId() {
		return gateServerId;
	}

	public void setGateServerId(byte[] gateServerId) {
		this.gateServerId = gateServerId;
	}

	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}

}
