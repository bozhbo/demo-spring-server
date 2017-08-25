package com.snail.webgame.engine.common.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {

	//SimpleDateFormat是个不同步的方法
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final String cStartTime = "1899-12-30 00:00:00";
	private static Object obj = new Object();
	
	public static Date format(String time) {
		
		if (time != null && time.trim().length() > 0) 
		{
			synchronized(obj)
			{
				try {
					return formatter.parse(time);
					} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} 
		}
		else 
		{
				return null;	
		}

	}
	
	
	public static String format(Date time) {
		if (time != null)
		{
			synchronized(obj)
			{	
			try {
					return formatter.format(time);
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}
		}
		else 
		{
				return "";
		}
		
	}
	
	public static String format(long time) {

		Date date = new Date(time);	
		return format(date);
	}
	public static Date getNowTime() {
	 
		
		return new Date();
	}
	
	/**
	 * 将时间转换成C++使用的double型
	 * @param date
	 * @return 距 1899-12-30 00:00:00中的天数
	 */
	public static double getCDayTime(Date date) {
	
		Date time = format(cStartTime);

		double a = date.getTime();
		double b = time.getTime();

		double days = (a - b) / (86400 * 1000);

		return days;
	}
	/**
	 * 将C++使用的时间 转换成java 使用的标准格式
	 * @param t
	 * @return
	 */
	public static Date getJDate(double t)
	{
		Date time = format(cStartTime);

		
		double b = time.getTime();
		
		long a = (long) ((t*86400*1000)+b);
		Date date = new Date(a);
		return date;

	}
}
