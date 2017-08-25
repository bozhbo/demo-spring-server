package org.epilot.ccf.common;

public class ServiceSend {
	private String LocalHost;
	private String LocalPort;
	private String RemoteHost;
	private String RemotePort;
	private String serviceHandle;
	private Filters[] filters;
	private String serviceName;
	private String sessionHandle;
	public String getLocalHost() {
		return LocalHost;
	}
	public void setLocalHost(String localHost) {
		LocalHost = localHost;
	}
	public String getLocalPort() {
		return LocalPort;
	}
	public void setLocalPort(String localPort) {
		LocalPort = localPort;
	}
	public String getRemoteHost() {
		return RemoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		RemoteHost = remoteHost;
	}
	public String getRemotePort() {
		return RemotePort;
	}
	public void setRemotePort(String remotePort) {
		RemotePort = remotePort;
	}
	public String getServiceHandle() {
		return serviceHandle;
	}
	public void setServiceHandle(String serviceHandle) {
		this.serviceHandle = serviceHandle;
	}
	public Filters[] getFilters() {
		return filters;
	}
	public void setFilters(Filters[] filters) {
		this.filters = filters;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getSessionHandle() {
		return sessionHandle;
	}
	public void setSessionHandle(String sessionHandle) {
		this.sessionHandle = sessionHandle;
	}
}
