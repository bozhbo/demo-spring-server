package com.snail.webgame.engine.gate.common;

public class SocketConfig {
	private int socketReceiveBuffer;
	private int socketSendBuffer;
	private boolean keepAlive;
	private boolean tcpNoDelay;
	private int idleState;
	private int timeout;

	public int getSocketReceiveBuffer() {
		return socketReceiveBuffer;
	}

	public void setSocketReceiveBuffer(int socketReceiveBuffer) {
		this.socketReceiveBuffer = socketReceiveBuffer;
	}

	public int getSocketSendBuffer() {
		return socketSendBuffer;
	}

	public void setSocketSendBuffer(int socketSendBuffer) {
		this.socketSendBuffer = socketSendBuffer;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getIdleState() {
		return idleState;
	}

	public void setIdleState(int idleState) {
		this.idleState = idleState;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
