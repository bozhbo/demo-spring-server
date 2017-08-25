package com.snail.webgame.engine.game.thread;

public interface GameThreadPoolMBean {

	public int getCoreThreads();
	
	public int getMaxThreads();
	
	public int getQueueSize();
	
	public int getLargestPoolSize();
	
	public void setCoreThreads(int coreThreads);
	
	public void setMaxThreads(int maxThreads);
	
	public void stop();
	
	public void start();
}
