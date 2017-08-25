package com.snail.webgame.game.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreanNotifyThread {

	//private int maxSize = 2500;
	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 20, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(25000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static ScreanNotifyThread thisInstance;

	private ScreanNotifyThread() {

	}

	public static synchronized ScreanNotifyThread getInstance() {
		if (thisInstance == null) {
			thisInstance = new ScreanNotifyThread();
		}
		return thisInstance;
	}

	public void run(Runnable runnable) {

		threadPool.execute(runnable);
	}

	public synchronized boolean isCanAdd() {

//		if (maxSize > threadPool.getQueue().size()) {
//			return true;
//		}
//		return false;
		
		return true;
	}

	public void close() {
		threadPool.shutdown();

		try {
			if (!threadPool.awaitTermination(20, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
				if (!threadPool.awaitTermination(20, TimeUnit.SECONDS))
					if (logger.isErrorEnabled()) {
						logger.error("ScreanNotifyThread march move Pool did not terminate");
					}

			}
		} catch (InterruptedException ie) {
			if (logger.isErrorEnabled()) {
				logger.error("", ie);
			}

			threadPool.shutdownNow();
		}
	}

	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}
	
	
}
