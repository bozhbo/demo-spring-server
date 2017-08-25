package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.ClubEventInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;

public class RoleClubEventDao extends SqlMapDaoSupport{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private RoleClubEventDao(){
		
	}
	
	private static class Inner{
		private static RoleClubEventDao instance = new RoleClubEventDao();
	}
	
	public static RoleClubEventDao getInstance(){
		return Inner.instance;
	}
	
	public boolean loadAllClubEventByPage(){
		logger.info("loadAllClubEvent start");

		boolean result = true;
		int count = 0;
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));

		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		if(session == null){
			logger.error("loadAllClubEvent method get session is null");
			return false;
		}
		
		Connection conn = session.getConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_CLUB_EVENT_INFO");
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				count = rs.getInt("ALL_COUNT");
			}
			
		} catch (SQLException e) {
		
			e.printStackTrace();
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
			}
		}
		
		System.out.println("GAME_CLUB_EVENT_INFO Num = " + count);
		int i = 0;
		int every = 800000;
		
		while(true){
			if(i * every > count){
				break;
			}
			
			result = loadAllClubEventByRange(i * every, every);

			i++;
			
		}
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubInfo successful");
		}
		
		return result;
	}
	
	private boolean loadAllClubEventByRange(int start, int limit) {
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
			ps = conn.prepareStatement("SELECT N_ID, N_CLUB_ID, N_ROLE_ID, N_EVENT, D_EVENT_TIME FROM GAME_CLUB_EVENT_INFO LIMIT ?, ?");
			
			ps.setInt(1, start);
			ps.setInt(2, limit);
			ps.setFetchSize(500000);
			
			rs = ps.executeQuery();
			
			RoleInfo roleInfo = null;
			ClubEventInfo info = null;
			RoleClubInfo clubInfo = null;
			int index = 0;
			int roleId = 0;
			int clubId = 0;
			
			while(rs.next()){
				roleId = rs.getInt("N_ROLE_ID");
				clubId=  rs.getInt("N_CLUB_ID");
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
				
				if(roleInfo == null || clubInfo == null){
					continue;
				}
				
				info = new ClubEventInfo();
				info.setId(rs.getInt("N_ID"));
				info.setRoleId(roleId);
				info.setClubId(clubId);
				info.setEvent(rs.getInt("N_EVENT"));
				info.setTime(rs.getTimestamp("D_EVENT_TIME"));
				
				RoleClubMemberInfoMap.addEvent(info);
				
				index++;
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_CLUB_INFO Table success! " + index);
			}
			
			
		}catch (Exception e) {
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_CLUB_INFO Table failed!", e);
			}
			
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
				
			}
		}
		
		return true;
	}

	/**
	 * 公会事件
	 * @param to
	 * @return
	 */
	public boolean insertRoleClubEventInfo(ClubEventInfo to){
		try {
			getSqlMapClient(DbConstants.GAME_DB).insert("insertClubEventInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("insert role club event success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("RoleClubEventDao.insertRoleClubEventInfo error!", e);
			}
			return false;
		}
		
	}
	
	/**
	 * 删除时间GM用
	 * @param roleId
	 * @return
	 */
	public boolean deleteRoleClubEventInfoByRoleId(int roleId){
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteClubEventInfo", roleId);
			
			if(logger.isInfoEnabled()){
				logger.info("delete role club event execute");
			}
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("RoleClubEventDao.deleteRoleClubEventInfoByRoleId error!", e);
			}
			
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadAllClubEvent(){
		System.gc();

		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.gc();

		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		List<ClubEventInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectClubEventInfo");
		
		if(list != null && list.size() > 0){
			for(ClubEventInfo info : list){
				if(info.getEvent() == 3){
					continue;
				}
				
				RoleClubMemberInfoMap.addEvent(info);
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("load club event successful");
		}
		
		return true;
		
	}
	
	/**
	 * 删除事件（公会解散）
	 * @param clubId
	 * @return
	 */
	public boolean deleteClubEventByClubId(int clubId){
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteClubEventInfoByClubId", clubId);
			
			if(logger.isInfoEnabled()){
				logger.info("delete club event by clubId succecc");
			}
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("RoleClubEventDao.deleteClubEventByClubId error!", e);
			}
			
			return false;
		}
		

		return true;
	}
	
	/**
	 * 批量删除公会时间
	 * @param list
	 * @return
	 */
	public boolean batchDeleteClubEvent(List<Integer> list){
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("batchDeleteClubEventInfo", list);
			
			if(logger.isInfoEnabled()){
				logger.info("batchDeleteClubEvent succecc");
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("RoleClubEventDao.batchDeleteClubEvent error!", e);
			}
		}
		

		return true;
	}
	
	/**
	 * 清理无效数据
	 * @return
	 */
	public boolean cleanupClubEvent(){
		ClubEventInfo to = new ClubEventInfo();
		to.setEvent(3);
		long time = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
		to.setTime(new Timestamp(time));
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteClubEventInfo4Cleanup", to);
			
		} catch (Exception e) {
			logger.error("RoleClubEventDao.cleanupClubEvent error!",e);
		}
		
		
		return true;
	}

}
