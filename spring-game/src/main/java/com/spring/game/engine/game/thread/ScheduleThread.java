package com.snail.webgame.engine.game.thread;


import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时线程
 * 
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class ScheduleThread {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

	public static boolean start() {
		
		service.scheduleAtFixedRate(new DailyTask(), 0, 24 * 3600, TimeUnit.SECONDS);
		
		return true;
	}

	public static void shutdown() {
		if (!service.isShutdown()) {
			service.shutdown();
		}
	}

	/**
	 * 取得到达下一周时间点的时间间隔(每周任务)
	 * 
	 * @param weekPoint
	 *            星期几(考虑国家的不同以星期日为每周第一天)
	 * @param timePoint
	 *            时间点
	 * @return long 时间间隔
	 */
	private static long getEveryWeekTimePoint(int weekPoint, int timePoint) {
		long delayTime = 0;

		Calendar curTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		endTime.set(Calendar.DAY_OF_WEEK, weekPoint + 1);
		endTime.set(Calendar.HOUR_OF_DAY, timePoint);
		endTime.set(Calendar.MINUTE, 0);
		endTime.set(Calendar.SECOND, 0);
		endTime.set(Calendar.MILLISECOND, 0);

		long lCurTime = curTime.getTimeInMillis();
		long lEndTime = endTime.getTimeInMillis();

		if (lCurTime > lEndTime) {
			endTime.add(Calendar.DAY_OF_MONTH, 7);
			lEndTime = endTime.getTimeInMillis();
			delayTime = lEndTime - lCurTime;
		} else {
			delayTime = lEndTime - lCurTime;
		}
		return delayTime;
	}
	
	/**
	 * 取得到达下一时间点的时间间隔(每日任务)
	 * @param timePoint
	 * @param minute
	 * @return
	 */
	private static long getEveryDayTimePoint(int timePoint, int minute) {
		long delayTime = 0;

		Calendar curTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		endTime.set(Calendar.HOUR_OF_DAY, timePoint);
		endTime.set(Calendar.MINUTE, minute);
		endTime.set(Calendar.SECOND, 0);
		endTime.set(Calendar.MILLISECOND, 0);

		if (curTime.getTimeInMillis() < endTime.getTimeInMillis()) {
			delayTime = endTime.getTimeInMillis() - curTime.getTimeInMillis();
		} else {
			endTime.add(Calendar.DAY_OF_MONTH, 1);
			delayTime = endTime.getTimeInMillis() - curTime.getTimeInMillis();
		}

		return delayTime;
	}
	
	public static ScheduledExecutorService getService() {
		return service;
	}
}

class DailyTask implements Runnable {
	
	public void run() {
		
	}
}

