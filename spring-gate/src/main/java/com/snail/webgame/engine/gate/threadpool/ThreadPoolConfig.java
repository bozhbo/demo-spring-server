package com.snail.webgame.engine.gate.threadpool;

public class ThreadPoolConfig {
	String poolName;
	String maxThreads;
	String minThreads;
	String keepAlive;
	String queueType;
	String maxQueues;
	String priority;

	public String getMaxQueues() {
		return maxQueues;
	}
	public void setMaxQueues(String maxQueues) {
		this.maxQueues = maxQueues;
	}
	public String getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(String maxThreads) {
		this.maxThreads = maxThreads;
	}
	public String getMinThreads() {
		return minThreads;
	}
	public void setMinThreads(String minThreads) {
		this.minThreads = minThreads;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(String keepAlive) {
		this.keepAlive = keepAlive;
	}
	public String getQueueType() {
		return queueType;
	}
	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

}
