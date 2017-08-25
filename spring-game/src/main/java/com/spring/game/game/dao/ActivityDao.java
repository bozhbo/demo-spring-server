package com.snail.webgame.game.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.dao.base.DbConstants;

public class ActivityDao extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ActivityDao(){
		
	}
	private static class InternalClass {
		public final static ActivityDao instance = new ActivityDao();
	}

	public static ActivityDao getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 更新经验1活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpActivityLeftTimes1(int times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", times);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateExpActivityLeftTimes1", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateExpActivityLeftTimes1 error!!",e);
		}
		return result;

	}
	/**
	 * 更新经验2活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpActivityLeftTimes2(int times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", times);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateExpActivityLeftTimes2", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateExpActivityLeftTimes2 error!!",e);
		}
		return result;

	}
	/**
	 * 更新经验3活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpActivityLeftTimes3(int times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", times);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateExpActivityLeftTimes3", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateExpActivityLeftTimes3 error!!",e);
		}
		return result;

	}
	/**
	 * 更新经验4活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpActivityLeftTimes4(int times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", times);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateExpActivityLeftTimes4", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateExpActivityLeftTimes4 error!!",e);
		}
		return result;

	}
	/**
	 * 更新经验5活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpActivityLeftTimes5(int times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", times);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateExpActivityLeftTimes5", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateExpActivityLeftTimes5 error!!",e);
		}
		return result;

	}
	/**
	 * 更新经验6活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpActivityLeftTimes6(int times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", times);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateExpActivityLeftTimes6", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateExpActivityLeftTimes6 error!!",e);
		}
		return result;

	}
	/**
	 * 更新经验ALL活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateAllExpActivityLeftTimes(int times1, int times2,int times3,int times4,int times5,int times6, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times1", times1);
		map.put("times2", times2);
		map.put("times3", times3);
		map.put("times4", times4);
		map.put("times5", times5);
		map.put("times6", times6);
		map.put("roleId", roleId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateAllExpActivityLeftTimes", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateAllExpActivityLeftTimes error!!",e);
		}
		return result;

	}
	
	/**
	 * 更新银币活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateMoneyActivityLeftTimes(byte times, int roleId){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("times", (int) times);
		map.put("roleId", roleId);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateMoneyActivityAttendTimes", map);
		} catch (Exception e) {
			logger.error("ActivityDao.updateMoneyActivityLeftTimes error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新银币活动剩余次数
	 * 
	 * @param times
	 * @param roleId
	 * @return
	 */
	public boolean updateExpMoneyActivityLeftTimes(int roleId, 
			byte expTimes1, byte expTimes2, byte expTimes3, byte expTimes4, byte expTimes5, byte expTimes6,
			byte moneyTimes){		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("expTimes1", (int) expTimes1);
		map.put("expTimes2", (int) expTimes2);
		map.put("expTimes3", (int) expTimes3);
		map.put("expTimes4", (int) expTimes4);
		map.put("expTimes5", (int) expTimes5);
		map.put("expTimes6", (int) expTimes6);
		map.put("moneyTimes", (int) moneyTimes);
		map.put("roleId", roleId);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateExpMoneyActivityLeftTimes", map);		
	}

}
