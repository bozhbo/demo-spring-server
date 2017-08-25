package com.snail.webgame.game.protocal.scene.sceneRefre;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SceneRefreThread {

	private int maxSize = 40000;//2000人同时在线移动
	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 20, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(maxSize),
			new ThreadPoolExecutor.AbortPolicy());
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static SceneRefreThread thisInstance;

	private SceneRefreThread() {

	}

	public static synchronized SceneRefreThread getInstance() {
		if (thisInstance == null) {
			thisInstance = new SceneRefreThread();
		}
		return thisInstance;
	}

	/**
	 * 专门用于场景刷新
	 * @param runnable
	 */
	public void run(Runnable runnable) {
		try {
			threadPool.execute(runnable);
		} catch (Exception e) {
			logger.error("SceneRefreThread is so busy that task was Rejected", e);
		}
	}

	public synchronized boolean isCanAdd() {

		if (maxSize > threadPool.getQueue().size()) {
			return true;
		}
		return false;
	}

	public void close() {
		threadPool.shutdown();

		try {
			if (!threadPool.awaitTermination(20, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
				if (!threadPool.awaitTermination(20, TimeUnit.SECONDS))
					if (logger.isErrorEnabled()) {
						logger.error("SceneRefreThread Pool did not terminate");
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
