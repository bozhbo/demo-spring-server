package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.HireHeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class ClubHireHeroDao extends SqlMapDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private ClubHireHeroDao(){
		
	}
	
	public static ClubHireHeroDao getInstance(){
		return Inner.instance;
	}
	
	private static class Inner{
		private static final ClubHireHeroDao instance = new ClubHireHeroDao();
		
	}
	
	public boolean loadAllClubHireHero(){
		logger.info("loadAllClubHireHero start");

		boolean result = true;
		int count = 0;
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));

		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		if(session == null){
			logger.error("loadAllClubHireHero method get session is null");
			return false;
		}
		
		Connection conn = session.getConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		try {
			ps = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_CLUB_HIRE_HERO_INFO");
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				count = rs.getInt("ALL_COUNT");
			}
			
		} catch (SQLException e) {
			logger.error("loadAllClubHireHero error!",e);
			e.printStackTrace();
			return false;
		}finally{
			
			try {
				if (rs != null) {
					rs.close();
				}
				
				if (ps != null) {
					ps.close();
				}
				if(conn != null) {
					conn.close();
				}
				
				session.close();
			} catch (SQLException e) {
				
				logger.error("loadAllClubHireHero finally error!",e);
				e.printStackTrace();
				return false;
			}
		}
		
		System.out.println("GAME_CLUB_HIRE_HERO_INFO Num = " + count);
		int i = 0;
		int every = 6000000;
		
		while(true){
			if(i * every > count){
				break;
			}
			
			result = loadAllClubHireHeroByRange(i * every, every);

			i++;
			
		}
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllClubHireHero successful");
		}
		
		return result;
		
	}

	private boolean loadAllClubHireHeroByRange(int start, int limit) {
		System.gc();
		
		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.gc();
		
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		if(session == null){
			return false;
		}
		
		Connection conn = session.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			ps = conn.prepareStatement("SELECT N_ID, N_ROLE_ID, N_HERO_ID, " +
					"D_TIME, N_HIRE_MONEY_SUM FROM GAME_CLUB_HIRE_HERO_INFO LIMIT ?, ?");
			
			ps.setInt(1, start);
			ps.setInt(2, limit);
			ps.setFetchSize(500000);
			
			rs = ps.executeQuery();
			
			int index = 0;
			HireHeroInfo info = null;
			RoleInfo roleInfo = null;
			int roleId = 0;
			int heroId = 0;
			
			while(rs.next()){
				roleId = rs.getInt("N_ROLE_ID");
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				
				if(roleInfo == null){
					continue;
				}
				
				heroId = rs.getInt("N_HERO_ID");
								
				info = new HireHeroInfo();
				info.setId(rs.getInt("N_ID"));
				info.setRoleId(roleId);
				info.setHeroId(heroId);
				info.setTime(rs.getTimestamp("D_TIME"));
				info.setHireMoneySum(rs.getInt("N_HIRE_MONEY_SUM"));
				
				roleInfo.getHireHeroMap().put(info.getHeroId(), info);
				
				
				index++;
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_CLUB_HIRE_HERO_INFO Table success! " + index);
			}
			
			
		}catch (Exception e) {
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_CLUB_HIRE_HERO_INFO Table failed!", e);
			}
			return false;
			
		}finally{
			
			try {
				if (rs != null) {
					rs.close();
				}
				
				if (ps != null) {
					ps.close();
				}
				if(conn != null) {
					conn.close();
				}
				
				session.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
				
			}
		}
		
		return true;
		
	}
	
	/**
	 * 插入佣兵信息
	 * @param to
	 * @return
	 */
	public boolean insertHireHeroInfo(HireHeroInfo to){
		try {
			
			getSqlMapClient(DbConstants.GAME_DB).insert("insertHireHeroInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("insertHireHeroInfo success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("insertHireHeroInfo  failure", e);
			}
			
			return false;
		
		}
	}
	
	/**
	 * 删除佣兵
	 * @param id
	 * @return
	 */
	public boolean deleteHireHeroInfo(int id){
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteHireHeroInfoById", id);
			
			if(logger.isInfoEnabled()){
				logger.info("deleteHireHeroInfo success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("deleteHireHeroInfo  failure", e);
			}
			
			return false;
		
		}
		
	}
	
	/**
	 * 删除佣兵
	 * @param to
	 * @return
	 */
	public boolean deleteHireHeroInfo(HireHeroInfo to){
		try {
			
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteHireHeroInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("deleteHireHeroInfo success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("deleteHireHeroInfo  failure", e);
			}
			
			return false;
		
		}
		
	}
	
	/**
	 * 更新佣兵信息
	 * @param id
	 * @param hireMoneySum
	 * @param hireInfos
	 * @return
	 */
	public boolean updateHireHeroInfoById(int id, int hireMoneySum, String hireInfos){
		try {
			HireHeroInfo to = new HireHeroInfo();
			to.setId(id);
			to.setHireMoneySum(hireMoneySum);
			
			getSqlMapClient(DbConstants.GAME_DB).update("updateHireHeroInfoById", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateHireHeroInfoById success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateHireHeroInfoById  failure", e);
			}
			
			return false;
		
		}
	}
	
	/**
	 * 更新佣兵信息
	 * @param roleId
	 * @param heroId
	 * @param hireMoneySum
	 * @return
	 */
	public boolean updateHireHeroInfoByRoleId(int roleId, int heroId, int hireMoneySum){
		try {
			
			HireHeroInfo to = new HireHeroInfo();
			to.setRoleId(roleId);
			to.setHeroId(heroId);
			to.setHireMoneySum(hireMoneySum);
			
			getSqlMapClient(DbConstants.GAME_DB).update("updateHireHeroInfoByRoleId", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateHireHeroInfoByRoleId success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateHireHeroInfoByRoleId  failure", e);
			}
			
			return false;
		
		}
	}
	
	/**
	 * 删除角色所有佣兵
	 * @param to
	 * @return
	 */
	public boolean deleteHireHeroInfoByRoleId(int roleId){
		try {
			HireHeroInfo to = new HireHeroInfo();
			to.setRoleId(roleId);
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteHireHeroInfoByRoleId", to);
			
			if(logger.isInfoEnabled()){
				logger.info("deleteHireHeroInfo success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("deleteHireHeroInfo  failure", e);
			}
			
			return false;
		
		}
		
	}
	
}
