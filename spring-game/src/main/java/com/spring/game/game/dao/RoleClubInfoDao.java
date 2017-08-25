package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;

public class RoleClubInfoDao extends SqlMapDaoSupport{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private RoleClubInfoDao(){
		
	}
	
	private static class Inner{
		private static final RoleClubInfoDao instance = new RoleClubInfoDao();
	}
	
	public static RoleClubInfoDao getInstance(){
		return Inner.instance;
	}
	
	public boolean loadAllRoleClubInfoByPage(){
		logger.info("loadAllRoleClubInfo start");

		boolean result = true;
		int count = 0;
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));

		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		if(session == null){
			logger.error("loadAllRoleClubInfo method get session is null");
			return false;
		}
		
		Connection conn = session.getConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_ROLE_CLUB_INFO");
			
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
		
		System.out.println("GAME_ROLE_CLUB_INFO Num = " + count);
		int i = 0;
		int every = 800000;
		
		while(true){
			if(i * every > count){
				break;
			}
			
			result = loadRoleClubInfoByRange(i * every, every);

			i++;
			
		}
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubInfo successful");
		}
		
		return result;
	}
	
	/**
	 * 分页查询
	 * @param start
	 * @param limit
	 * @return
	 */
	private boolean loadRoleClubInfoByRange(int start, int limit) {
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
			ps = conn.prepareStatement("SELECT N_ID, N_CREATE_ROLE_ID, S_CLUB_NAME, S_DECLARATION, " +
					"S_DESCRIPTION, N_IMAGE_ID, N_LEVEL, N_BUILD, N_LEVEL_LIMIT, " +
					"N_FLAG, D_CREATE_TIME FROM GAME_ROLE_CLUB_INFO LIMIT ?, ?");
			
			ps.setInt(1, start);
			ps.setInt(2, limit);
			ps.setFetchSize(500000);
			
			rs = ps.executeQuery();
			
			RoleClubInfo info = null;
			RoleInfo roleInfo = null;
			int index = 0;
			int roleId = 0;
			
			while(rs.next()){
				roleId = rs.getInt("N_CREATE_ROLE_ID");
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if(roleInfo == null){
					continue;
				}
				
				info = new RoleClubInfo();
				info.setId(rs.getInt("N_ID"));
				info.setBuild(rs.getInt("N_BUILD"));
				info.setClubName(rs.getString("S_CLUB_NAME"));
				info.setCreateRoleId(roleId);
				info.setCreateTime(rs.getTimestamp("D_CREATE_TIME"));
				info.setDeclaration(rs.getString("S_DECLARATION"));
				info.setDescription(rs.getString("S_DESCRIPTION"));
				info.setFlag(rs.getInt("N_FLAG"));
				info.setImageId(rs.getInt("N_BUILD"));
				info.setLevel(rs.getInt("N_LEVEL"));
				info.setLevelLimit(rs.getInt("N_LEVEL_LIMIT"));
				info.setImageId(rs.getInt("N_IMAGE_ID"));
				
				RoleClubInfoMap.addRoleClubInfo(info);
				
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
	 * 创建公会
	 * @param to
	 * @return
	 */
	public boolean insertRoleClubInfo(RoleClubInfo to){
		try {
			
			getSqlMapClient(DbConstants.GAME_DB).insert("insertRoleClubInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("create role club success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("create role club  failure", e);
			}
			
			return false;
		
		}
		
	}
	
	/**
	 * 更新公会
	 * @param to
	 * @return
	 */
	public boolean updateRoleClubInfo(RoleClubInfo to){
		
		try{
			getSqlMapClient(DbConstants.GAME_DB).insert("updateRoleClubInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("update role club success");
			}
			
			return true;
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("update role club failure", e);
			}
			
			return false;
		}
		
	
	}
	
	/**
	 * 删除公会
	 * @param to
	 * @return
	 */
	public boolean deleteRoleClubInfo(int id){
		try{
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteRoleClubInfo", id);
			
			if(logger.isInfoEnabled()){
				logger.info("delete role club success");
			}
			
			return true;
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("delete role club  failure", e);
			}
			
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadAllRoleClubInfo(){
		System.gc();

		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.gc();

		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		List<RoleClubInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleClubInfo");
		
		if(list != null && list.size() > 0){
			for(RoleClubInfo info : list){
				RoleClubInfoMap.addRoleClubInfo(info);
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubInfo successful");
		}
		
		return true;
	}
	
	public boolean updateRoleClubInfoCreateRoleId(int clubId, int roleId){
		RoleClubInfo to = new RoleClubInfo();
		to.setId(clubId);
		to.setCreateRoleId(roleId);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubInfoCreateRoleId", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateRoleClubInfoCreateRoleId success");
			}
			
			return true;
			
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateRoleClubInfoCreateRoleId failure", e);
			}
			
			return false;
			
		}
		
	}

	/**
	 * 更新公会建设度
	 * @param clubId
	 * @param build
	 * @return
	 */
	
	public boolean updateRoleClubInfoBuildAndLevel(int clubId, int build, int level){
		RoleClubInfo to = new RoleClubInfo();
		to.setId(clubId);
		to.setBuild(build);
		to.setLevel(level);
		
		try{
			getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubInfoBuildAndLevel", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateRoleClubInfoBuild success");
			}
			
			return true;
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateRoleClubInfoBuild failure", e);
			}
			
			return false;
			
		}
		
	}
	
	/**
	 * 更新公会科技
	 * @param clubId
	 * @param tech
	 * @return
	 */
	public boolean updateRoleClubInfoTech(int clubId, int tech){
		RoleClubInfo to = new RoleClubInfo();
		to.setId(clubId);
		to.setTech(tech);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubInfoTech", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateRoleClubInfoTech success");
			}
			
			return true;
			
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateRoleClubInfoTech failure", e);
			}
			
			return false;
			
		}
	}
	
	/**
	 * 更新公会扩容
	 * @param clubId
	 * @param tech
	 * @return
	 */
	public boolean updateRoleClubInfoGoldAndExtendLv(int clubId, int gold, int extendLv){
		RoleClubInfo to = new RoleClubInfo();
		to.setId(clubId);
		to.setGold(gold);
		to.setExtendLv(extendLv);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubInfoGoldAndExtendLv", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateRoleClubInfoGoldAndExtendLv success");
			}
			
			return true;
			
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateRoleClubInfoGoldAndExtendLv failure", e);
			}
			
			return false;
			
		}
	}

}
