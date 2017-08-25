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
import com.snail.webgame.game.cache.RoleAddRquestMap;
import com.snail.webgame.game.cache.RoleBlackMap;
import com.snail.webgame.game.cache.RoleFriendMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleRelationInfo;

public class RoleRelationDao extends SqlMapDaoSupport{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private RoleRelationDao(){
		
	}
	
	private static class Inner{
		private static final RoleRelationDao instance = new RoleRelationDao();
	}
	
	public static RoleRelationDao getInstance(){
		return Inner.instance;
	}
	
	
	/**
	 * 删除好友
	 * @param roleId
	 * @param relRoleId
	 * @return
	 */
	public boolean delRoleFriend(int roleId, int relRoleId){
		RoleRelationInfo to = new RoleRelationInfo();
		to.setFriendId(relRoleId);
		to.setRoleId(roleId);
		boolean result = false;
		
		try{
			result = getSqlMapClient(DbConstants.GAME_DB).delete("deleteRoleRelationInfo", to);
			
			if(result){
				if(logger.isInfoEnabled()){
					logger.info("delRoleFriend success");
				}
			}else{
				if(logger.isInfoEnabled()){
					logger.info("delRoleFriend failure");
				}
			}
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("delRoleFriend error!", e);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param roleId 
	 * @param relRoleId 好友 或者 请求添加的 角色Id
	 * @param status 0 - 好友请求 1 - 好友
	 * @return
	 */
	public boolean insertRoleFriend(int roleId, int relRoleId, int status){
		RoleRelationInfo to = new RoleRelationInfo();
		to.setFriendId(relRoleId);
		to.setRoleId(roleId);
		to.setStatus(status);
		
		boolean result = false;
		
		try{
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertRoleRelationInfo", to);
			
			if(result){
				if(logger.isInfoEnabled()){
					logger.info("addRoleFriend success");
				}
			}else{
				if(logger.isInfoEnabled()){
					logger.info("addRoleFriend failure");
				}
			}
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("insertRoleFriend error!", e);
			}
		}
		

		
		return result;
	
	}
	
	public boolean loadAllRoleRelation(){
		logger.info("loadAllRoleRelation start");

		boolean result = true;
		int count = 0;
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));

		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		if(session == null){
			logger.error("loadAllRoleRelation method get session is null");
			return false;
		}
		
		Connection conn = session.getConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		try {
			ps = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_ROLE_RELATION_INFO");
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				count = rs.getInt("ALL_COUNT");
			}
			
		} catch (SQLException e) {
			logger.error("loadAllRoleRelation error!",e);
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
				logger.error("loadAllRoleRelation finally error!",e);
				e.printStackTrace();
				return false;
			}
		}
		
		System.out.println("GAME_ROLE_RELATION_INFO Num = " + count);
		int i = 0;
		int every = 6000000;
		
		while(true){
			if(i * every > count){
				break;
			}
			
			result = loadAllRoleRelationByRange(i * every, every);

			i++;
			
		}
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubInfo successful");
		}
		
		return result;
		
		
	}
	
	private boolean loadAllRoleRelationByRange(int start, int limit) {
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
			ps = conn.prepareStatement("SELECT N_ID, N_ROLE_ID, N_FRIEND_ID, N_STATUS FROM GAME_ROLE_RELATION_INFO LIMIT ?, ?");
			
			ps.setInt(1, start);
			ps.setInt(2, limit);
			ps.setFetchSize(500000);
			
			rs = ps.executeQuery();
			
			int index = 0;
			RoleRelationInfo info = null;
			RoleInfo roleInfo = null;
			RoleInfo relRoleInfo = null;
			int roleId = 0;
			int relRoleId = 0;
			
			while(rs.next()){
				roleId = rs.getInt("N_ROLE_ID");
				relRoleId = rs.getInt("N_FRIEND_ID");
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				relRoleInfo = RoleInfoMap.getRoleInfo(relRoleId);
				
				if(roleInfo == null || relRoleInfo == null){
					continue;
				}
				
				info = new RoleRelationInfo();
				info.setId(rs.getInt("N_ID"));
				info.setRoleId(roleId);
				info.setFriendId(relRoleId);
				info.setStatus(rs.getInt("N_STATUS"));
				
				if(info.getStatus() == 0){
					// 好友请求
					RoleAddRquestMap.addRequestRoleId(info.getRoleId(), info.getFriendId());
				}else if(info.getStatus() == 1){
					// 好友
					RoleFriendMap.addRoleFriendId(info.getRoleId(), info.getFriendId());
				}else if(info.getStatus() == 2){
					// 黑名单
					RoleBlackMap.addBlackRoleId(info.getRoleId(), info.getFriendId());
				}
				
				index++;
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_RELATION_INFO Table success! " + index);
			}
			
			
		}catch (Exception e) {
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_RELATION_INFO Table failed!", e);
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
	 * 
	 * @param roleId
	 * @param relRoleId
	 * @return
	 */
	public boolean updateRoleStatus(int roleId, int relRoleId, int status){
		RoleRelationInfo to = new RoleRelationInfo();
		to.setRoleId(roleId);
		to.setFriendId(relRoleId);
		to.setStatus(status);
		
		try{
			getSqlMapClient(DbConstants.GAME_DB).update("updateRoleRelationStatus", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateRoleStatus success");
			}
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateRoleStatus error!", e);
			}
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 根据Status批量删除
	 * @param roleId
	 * @param relRoleId
	 * @return
	 */
	public boolean delRoleFriendByStatus(int roleId, int status){
		RoleRelationInfo to = new RoleRelationInfo();
		to.setRoleId(roleId);
		to.setStatus(status);
		
		try{
			getSqlMapClient(DbConstants.GAME_DB).delete("delRoleFriendByStatus", to);

			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("", e);
			}
			
			if(logger.isInfoEnabled()){
				logger.info("delRoleFriendByStatus failure");
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("delRoleFriendByStatus success");
		}
		
		return true;
	}
	
}
