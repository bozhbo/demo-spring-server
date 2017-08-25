package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.club.service.ClubService;

public class RoleClubMemberDao extends SqlMapDaoSupport{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private RoleClubMemberDao(){
		
	}
	
	public static class Inner{
		private static final RoleClubMemberDao instance = new RoleClubMemberDao();
	}
	
	public static RoleClubMemberDao getInstance(){
		return Inner.instance;
	}
	
	public boolean loadAllRoleClubMemberByPage(){
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubMember method start");
		}
		
		boolean result = true;
		int count = 0;
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));

		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		
		if(session == null){
			logger.error("loadAllRoleClubMember method get session is null");
			return false;
		}
		
		Connection conn = session.getConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_CLUB_MEMBER_INFO");
			
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
		
		System.out.println("GAME_CLUB_MEMBER_INFO Num = " + count);
		int i = 0;
		int every = 6000000;
		
		while(true){
			if(i * every > count){
				break;
			}
			
			result = loadRoleClubMemberByRange(i * every, every);
				
			i++;
			
		}
		
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubMember successful");
		}
		
		return result;
	}
	
	private boolean loadRoleClubMemberByRange(int start, int limit) {
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
		
		try {
			ps = conn.prepareStatement("SELECT N_ID, N_ROLE_ID, N_CLUB_ID, D_JOIN_TIME, N_STATUS FROM GAME_CLUB_MEMBER_INFO LIMIT ?, ?");
			ps.setInt(1, start);
			ps.setInt(2, limit);
			ps.setFetchSize(500000);
			
			rs = ps.executeQuery();
			
			int index = 0;
			RoleClubMemberInfo info = null;
			RoleInfo roleInfo = null;
			int roleId = 0;
			
			while(rs.next()){
				roleId = rs.getInt("N_ROLE_ID");
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if(roleInfo == null){
					continue;
				}
				
				info = new RoleClubMemberInfo();
				info.setClubId(rs.getInt("N_CLUB_ID"));
				info.setJoinTime(rs.getTimestamp("D_JOIN_TIME"));
				info.setRoleId(roleId);
				info.setStatus(rs.getInt("N_STATUS"));
				
				RoleClubMemberInfoMap.addRoleClubMemberInfo(info);
				
				index++;
				
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_CLUB_INFO Table success! " + index);
			}
			
			
		} catch (SQLException e) {
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_CLUB_MEMBER_INFO Table failed!", e);
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
	 * 加入公会成员
	 * @param to
	 * @return
	 */
	public boolean insertRoleClubMemberInfo(RoleClubMemberInfo to) {
		try{
			getSqlMapClient(DbConstants.GAME_DB).insert("insertRoleClubMemberInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("add role club member success");
			}
			
			return true;
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("add role club member failure", e);
			}
			return false;
		}
		
	}

	/**
	 * 更新成员公会信息
	 * @param to
	 * @return
	 */
	public boolean updateRoleClubMemberInfo(RoleClubMemberInfo to) {
		try {
			getSqlMapClient(DbConstants.GAME_DB).insert("updateRoleClubMemberInfo", to);
			
			if(logger.isInfoEnabled()){
				logger.info("update role club member success");
			}
			
			return true;
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("update role club member failure", e);
			}
			
			return false;
		}
	}
	
	/**
	 * 批处理删除请求信息
	 * @param ids 数据库主键ID
	 * @return
	 */
	public boolean batchDeleteRoleMemberInfo(List<Integer> ids){
		if (ids == null || ids.size() <= 0) {
			return true;
		}
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				for (long id : ids) {
					client.delete("batchDeleteRoleClubMemberInfo", id);
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if(client != null){
				client.rollback();
			}
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
			return false;
		}
	}
	
	/**
	 * 删除所有的公会申请
	 * @param roleId
	 * @return
	 */
	public boolean deleteAllRoleMemberInfoByRoleId(int roleId){
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteRoleClubMemberInfoByRoleId", roleId);
			
			if(logger.isInfoEnabled()){
				logger.info("delete role club member by roleId success");
			}
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("delete role club member by roleId failure", e);
			}
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除多余的公会申请
	 * @param roleId
	 * @return
	 */
	public boolean deleteAllRoleMemberInfoByStatus(int clubId){
		RoleClubMemberInfo to = new RoleClubMemberInfo();
		to.setClubId(clubId);
		to.setStatus(RoleClubMemberInfo.CLUB_REQUEST_MEMBER);
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteRoleClubMemberInfoByStatus", to);
			if(logger.isInfoEnabled()){
				logger.info("delete role club member by status success");
			}
			return true;
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("delete role club member by status failure", e);
			}
			return false;
		}
	}
	
	/**
	 * 删除单个公会申请(踢出公会与拒绝加入)
	 * @param roleId
	 * @return
	 */
	public boolean deleteAllRoleMemberInfo4Refuse(int roleId, int clubId){
		RoleClubMemberInfo to = new RoleClubMemberInfo();
		to.setClubId(clubId);
		to.setRoleId(roleId);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deleteRoleClubMemberInfo4Refuse", to);
			
			if(logger.isInfoEnabled()){
				logger.info("delete role club member 4 refuse success");
			}
		
			return true;
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("delete role club member 4 refuse failure", e);
			}
			
			return false;
		}
		

	}
	
	/**
	 * 删除所有的公会申请
	 * @param roleId
	 * @return
	 */
	public boolean deleteAllRoleMemberInfoByClubId(int clubId){
		List<Integer> list = new ArrayList<Integer>();
		
		list.add(clubId);
		
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				for (long id : list) {
					client.delete("deleteRoleClubMemberInfoByClubId", id);
				}
				client.commit();
				
				if(logger.isInfoEnabled()){
					logger.info("delete role club member by roleId success");
				}
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if(client != null){
				client.rollback();
			}
			if (logger.isErrorEnabled()) {
				logger.error("delete role club member by roleId failure", e);
			}
			return false;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadAllRoleClubMember(){
		System.gc();

		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.gc();

		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		List<RoleClubMemberInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleClubMemberInfo");
		if(list != null && list.size() > 0){
			RoleInfo roleInfo = null;
			RoleClubInfo roleClubInfo = null;
			for(RoleClubMemberInfo info : list){
				roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(info.getClubId());
				if(roleClubInfo == null){
					continue;
				}
				
				if(info.getStatus() != RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
					roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
					if(roleInfo == null){
						continue;
					}
					
					roleInfo.setClubId(info.getClubId());
					
					if(info.getStatus() > 1){
						// 2 - 副会长 3 - 官员
						ClubService.addAdmin(roleClubInfo, roleInfo.getId());
					}
				}
				
				RoleClubMemberInfoMap.addRoleClubMemberInfo(info);
				
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("loadAllRoleClubMember successful");
		}
		
		return true;
	}
	
	/**
	 * 更新角色公会权限身份
	 * @param roleId
	 * @param clubId
	 * @param status
	 * @return
	 */
	public boolean updateClubMemberInfoStatus(int roleId, int clubId, int status){
		RoleClubMemberInfo to = new RoleClubMemberInfo();
		to.setClubId(clubId);
		to.setStatus(status);
		to.setRoleId(roleId);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateClubMemberInfoStatus", to);
			
			if(logger.isInfoEnabled()){
				logger.info("updateClubMemberInfoStatus success");
			}
			
			return true;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateClubMemberInfoStatus failure", e);
			}
			return false;
		}
		
	}

	/**
	 * 同步更改状态
	 * @param createRoleId
	 * @param status1 需要改变的状态
	 * @param srcRoleId
	 * @param status2 
	 * @param clubId
	 * @return
	 */
	public boolean updateClubMemberInfoStatus4Transaction(int createRoleId, int status1, int srcRoleId, int status2, int clubId) {
		
		SqlMapClient client = null;
		try{
			client = getSqlMapClient(DbConstants.GAME_DB, false);
			
			RoleClubMemberInfo to1 = new RoleClubMemberInfo();
			to1.setClubId(clubId);
			to1.setStatus(status1);
			to1.setRoleId(createRoleId);
			
			RoleClubMemberInfo to2 = new RoleClubMemberInfo();
			to2.setClubId(clubId);
			to2.setStatus(status2);
			to2.setRoleId(srcRoleId);
			
			client.update("updateClubMemberInfoStatus", to1);
			client.update("updateClubMemberInfoStatus", to2);
			client.commit();
			
			if(logger.isInfoEnabled()){
				logger.info("updateClubMemberInfoStatus4resign success");
			}
			
			return true;
		}catch (Exception e) {
			if(client != null) {
				client.rollback();
			}
			if(logger.isErrorEnabled()){
				logger.error("updateClubMemberInfoStatus4resign failure", e);
			}
			return false;
		}
		
	}
	
	/**
	 * 更新角色进出场景的标识
	 * @param roleId
	 * @param clubId
	 * @return
	 */
	public boolean updateClubSceneInfoFlag(int roleId, int clubId, int flag){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("roleId", roleId);
		map.put("clubId", clubId);
		map.put("flag", flag);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateClubMemberInfoFlagByRoleId", map);
	}

	
	public boolean updateClubMemberInfoPoints(RoleClubMemberInfo to){
		if(to == null){
			return false;
		}
		return getSqlMapClient(DbConstants.GAME_DB).update("updateClubMemberInfoPoints", to);
	}
}

