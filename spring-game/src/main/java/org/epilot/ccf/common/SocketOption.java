package org.epilot.ccf.common;

public class SocketOption {
	private String SocketReceiveBuffer;
	private String SocketSendBuffer;
	private String KeepAlive;
	private String TcpNoDelay;
	private String []IdleTime;
	private String WriteTimeout;
	private String ConnectTimeout;
	private String WorkerTimeout;
	private String LinkTest;
	private String LinkTestTimeout;
	
	public String getConnectTimeout() {
		return ConnectTimeout;
	}
	public void setConnectTimeout(String connectTimeout) {
		ConnectTimeout = connectTimeout;
	}
	public String getWorkerTimeout() {
		return WorkerTimeout;
	}
	public void setWorkerTimeout(String workerTimeout) {
		WorkerTimeout = workerTimeout;
	}
	public String[] getIdleTime() {
		return IdleTime;
	}
	public void setIdleTime(String[] idleTime) {
		IdleTime = idleTime;
	}
	public String getKeepAlive() {
		return KeepAlive;
	}
	public void setKeepAlive(String keepAlive) {
		KeepAlive = keepAlive;
	}
	public String getSocketReceiveBuffer() {
		return SocketReceiveBuffer;
	}
	public void setSocketReceiveBuffer(String socketReceiveBuffer) {
		SocketReceiveBuffer = socketReceiveBuffer;
	}
	public String getSocketSendBuffer() {
		return SocketSendBuffer;
	}
	public void setSocketSendBuffer(String socketSendBuffer) {
		SocketSendBuffer = socketSendBuffer;
	}
	public String getTcpNoDelay() {
		return TcpNoDelay;
	}
	public void setTcpNoDelay(String tcpNoDelay) {
		TcpNoDelay = tcpNoDelay;
	}
	public String getWriteTimeout() {
		return WriteTimeout;
	}
	public void setWriteTimeout(String writeTimeout) {
		WriteTimeout = writeTimeout;
	}
	public String getLinkTest() {
		return LinkTest;
	}
	public void setLinkTest(String linkTest) {
		LinkTest = linkTest;
	}
	public String getLinkTestTimeout() {
		return LinkTestTimeout;
	}
	public void setLinkTestTimeout(String linkTestTimeout) {
		LinkTestTimeout = linkTestTimeout;
	}
	
}
