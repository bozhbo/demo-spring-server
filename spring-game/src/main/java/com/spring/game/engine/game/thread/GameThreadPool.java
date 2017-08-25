package com.snail.webgame.engine.game.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameThreadPool implements GameThreadPoolMBean {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public String name = null;
	
	public int coreThreads = 10;
	public int maxThreads = 10;
	public int keepLive = 60;
	public int queueSize = 50000;

	private ThreadPoolExecutor threadPool = null;
	
	public GameThreadPool(final String name, int coreThreads, int maxThreads, int keepLive, int queueSize, RejectedExecutionHandler rejectedExecutionHandler) {
		this.name = name;
		this.coreThreads = coreThreads;
		this.maxThreads = maxThreads;
		this.keepLive = keepLive;
		this.queueSize = queueSize;
		
		this.threadPool = new ThreadPoolExecutor(coreThreads, maxThreads, keepLive, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(queueSize),
					new ThreadFactory() {
						
						@Override
						public Thread newThread(Runnable r) {
							Thread t = new Thread(r);
							t.setName(name);
							return t;
						}
					}, rejectedExecutionHandler);
		
		logger.info(name + " threadPool started");
	}
	
	public void close() {
		if (threadPool != null) {
			threadPool.shutdown();

			try {
				if (!threadPool.awaitTermination(20, TimeUnit.SECONDS)) {
					threadPool.shutdownNow();
				}
			} catch (InterruptedException ie) {
				threadPool.shutdownNow();
			}
		}
	}
	
	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("coreThreads = " + coreThreads + ", maxThreads = " + maxThreads + ", keepLive = " + keepLive + ", queueSize = " + queueSize);
		return buffer.toString();
	}

	@Override
	public int getCoreThreads() {
		return this.coreThreads;
	}

	@Override
	public int getMaxThreads() {
		return this.maxThreads;
	}

	@Override
	public int getQueueSize() {
		return this.queueSize;
	}

	@Override
	public int getLargestPoolSize() {
		return this.getThreadPool().getLargestPoolSize();
	}

	@Override
	public void setCoreThreads(int coreThreads) {
		if (coreThreads <= 0 || coreThreads > 50) {
			return;
		} else {
			this.getThreadPool().setCorePoolSize(coreThreads);
			this.coreThreads = coreThreads;
		}
	}

	@Override
	public void setMaxThreads(int maxThreads) {
		if (maxThreads <= 0 || maxThreads > 100 || maxThreads < this.coreThreads) {
			return;
		} else {
			this.getThreadPool().setMaximumPoolSize(maxThreads);
			this.maxThreads = maxThreads;
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public void start() {
	}
}
