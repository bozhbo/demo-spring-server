package com.snail.webgame.game.thread;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.game.dao.base.DbConstants;

/**
 * 处理可以慢慢处理的事件，异步
 * 
 * @author xiasd
 *
 */
public class TempExecuteLogsThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(Integer.MAX_VALUE);

	private volatile boolean flag = false;
	
	public TempExecuteLogsThread() {
		this.setName("ExecuteLogsThread");
	}
	
	public static BlockingQueue<String> getQueue() {
		return queue;
	}
	
	@Override
	public void run() {
		String timeMessage = null;
		
		while (!flag) {
			try {
				if(timeMessage == null){
					timeMessage = queue.take();
				}

				if (timeMessage != null) {
					long start = System.currentTimeMillis();
					
					executeBatchLogs(timeMessage);
					
					logger.warn("executeBatchLogs time = " + (System.currentTimeMillis() - start));
				}
				
				TimeUnit.MILLISECONDS.sleep(30);

				timeMessage = null;
			} catch (Exception e) {
				logger.error("ExecuteLogsThread, takeMessage error", e);				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		logger.info("ExecuteLogsThread exited");
	}
	
	/**
	 * 处理日志
	 * 
	 * @param timeMessage
	 */
	private void executeBatchLogs(String timeMessage) {
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_LOG_DB).openSession(ExecutorType.SIMPLE);
		Connection conn = null;
		Statement psmt = null;
		
		try {
			conn = session.getConnection();
			psmt = conn.createStatement();
			psmt.executeUpdate(timeMessage);
			
			conn.commit();
			session.commit();
		} catch (SQLException e) {
			logger.error("executeBatchLogs error,"+timeMessage, e);
			
			try {
				conn.rollback();
				session.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try { 
				if (psmt != null) {
					psmt.close();
				}
				
				if (conn != null) {
					conn.close();
				}
				
				if (session != null) {
					session.close();
				}
			} catch (SQLException e) {
				logger.error("executeBatchLogs 1 error", e);
			}
		}
	}
	/**
	 * 添加消息
	 * 
	 * @param sendMessageInfo
	 */
	public void addMessage(String sql) {
		try {
			queue.put(sql);
		} catch (InterruptedException e) {
			logger.error("ExecuteLogsThread addMessage error", e);
		}
	}
	
	public void cancel() {
		this.flag = true;
	}
}
