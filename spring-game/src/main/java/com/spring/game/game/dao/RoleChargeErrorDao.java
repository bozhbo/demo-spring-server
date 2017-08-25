package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.protocal.app.common.AppStoreExInfo;

public class RoleChargeErrorDao {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static final String insertChargeError = "insert into GAME_APPSTORE_EXCEPTION(N_AGENT_ID,S_AGENT_PWD,S_ORDER_ID,S_CARD_TYPE,N_AMOUNT,N_TOTAL_VALUE, S_USER_NAME, N_ACCOUNT_TYPE_ID, S_AREA_ID, S_IMPREST_ACCOUNT_IP, S_VERIFY_STR, N_ROLE_ID, S_ACCOUNT, D_TIME, S_RESULT, N_IS_RETRANSFER, N_ERROR_CODE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String selectInfoById = "SELECT * FROM GAME_APPSTORE_EXCEPTION WHERE N_ID = ?";
	private static final String updateRetransferRecord = "UPDATE GAME_APPSTORE_EXCEPTION SET N_ERROR_CODE = ?, S_RESULT = ?, N_IS_RETRANSFER = ?, D_RETRANSFER_TIME = ?, S_OPERATE_USER = ? WHERE N_ID = ?";
	private static final String updateRecordStatus = "UPDATE GAME_APPSTORE_EXCEPTION SET N_ERROR_CODE = ?, N_IS_RETRANSFER = ? WHERE N_ID = ?";
	private static final String deleteRecordStatus = "DELETE FROM GAME_APPSTORE_EXCEPTION WHERE N_ID = ?";

	/**
	 * 新增充值错误日志
	 * 
	 * @param roleId		角色Id
	 * @param account		账号
	 * @param chargeTime	充值时间
	 * @param message		充值信息
	 * @param error			充值错误
	 * @return	boolean 
	 */
	public static long insertChargeError(int N_AGENT_ID, String S_AGENT_PWD, String S_ORDER_ID, String S_CARD_TYPE, int N_AMOUNT, int N_TOTAL_VALUE, String S_USER_NAME, int N_ACCOUNT_TYPE_ID, String S_AREA_ID, String S_IMPREST_ACCOUNT_IP, String S_VERIFY_STR, int N_ROLE_ID, String S_ACCOUNT, String S_RESULT, int N_IS_RETRANSFER, int N_ERROR_CODE) {
		PreparedStatement psmt = null;
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, true);
		Connection conn = null;
		Long id = 0l;
		try {
			conn = session.getConnection();
			psmt = conn.prepareStatement(insertChargeError, new String[] { "N_ID" });
			psmt.setInt(1, N_AGENT_ID);
			psmt.setString(2, S_AGENT_PWD);
			psmt.setString(3, S_ORDER_ID);
			psmt.setString(4, S_CARD_TYPE);
			psmt.setInt(5, N_AMOUNT);
			psmt.setInt(6, N_TOTAL_VALUE);
			psmt.setString(7, S_USER_NAME);
			psmt.setInt(8, N_ACCOUNT_TYPE_ID);
			psmt.setString(9, S_AREA_ID);
			psmt.setString(10, S_IMPREST_ACCOUNT_IP);
			psmt.setString(11, S_VERIFY_STR);
			psmt.setLong(12, N_ROLE_ID);
			psmt.setString(13, S_ACCOUNT);
			psmt.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
			psmt.setString(15, S_RESULT);
			psmt.setInt(16, N_IS_RETRANSFER);
			psmt.setInt(17, N_ERROR_CODE);
			
			psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getLong(1);
			} 
			return id;
		} catch (SQLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("RoleChargeErrorDao.insertChargeError error!", e);
			}
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				session.close();
			} catch (SQLException e) {
				logger.error("RoleChargeErrorDao.insertChargeError finally error!",e);
				e.printStackTrace();
			}
		}
		
		return id;
	}

	public AppStoreExInfo selectInfoById(long primaryKeyId) {
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, true);
		Connection conn = session.getConnection();

		PreparedStatement psmt = null;
		ResultSet rs = null;
		AppStoreExInfo info = null;
		
		try {
			psmt = conn.prepareStatement(selectInfoById);
			psmt.setLong(1, primaryKeyId);

			rs = psmt.executeQuery();
			
			while (rs.next()) {
				info = new AppStoreExInfo();
				
				info.setId(rs.getInt("N_ID"));
				info.setsAgentId(rs.getInt("N_AGENT_ID"));
				info.setsAgentPwd(rs.getString("S_AGENT_PWD"));
				info.setsAgentOrderId(rs.getString("S_ORDER_ID"));
				info.setsCardType(rs.getString("S_CARD_TYPE"));
				info.setAmount(rs.getInt("N_AMOUNT"));
				info.setTotalValue(rs.getInt("N_TOTAL_VALUE"));
				info.setsUserName(rs.getString("S_USER_NAME"));
				info.setsAccountTypeID(rs.getInt("N_ACCOUNT_TYPE_ID"));
				info.setsAreaID(rs.getString("S_AREA_ID"));
				info.setsImprestAccountIP(rs.getString("S_IMPREST_ACCOUNT_IP"));
				info.setsVerifyStr(rs.getString("S_VERIFY_STR"));
				info.setRoleId(rs.getLong("N_ROLE_ID"));
				info.setAccount(rs.getString("S_ACCOUNT"));
				info.setCreateTime(rs.getDate("D_TIME"));
				info.setResultStr(rs.getString("S_RESULT"));
				info.setErrorCode(rs.getInt("N_ERROR_CODE"));
				info.setIsRetransfer(rs.getInt("N_IS_RETRANSFER"));
				info.setRetransferTime(rs.getDate("D_RETRANSFER_TIME"));
				info.setOperateUser(rs.getString("S_OPERATE_USER"));
			}
		} catch (SQLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				session.close();
			} catch (SQLException e) {
				logger.error("RoleChargeErrorDao.selectInfoById error!",e);
				e.printStackTrace();
			}
		}
		return info;
	}

	public static boolean updateRecordStatus(long seqId, int status, int errorCode) {
		PreparedStatement psmt = null;
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, true);
		Connection conn = session.getConnection();
	
		try {
			psmt = conn.prepareStatement(updateRecordStatus);
			
			psmt.setInt(1, errorCode);
			psmt.setInt(2, status);
			psmt.setLong(3, seqId);
	
			int row = psmt.executeUpdate();
			
			if(row <= 0){
				return false;
			}
		} catch (SQLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("RoleChargeErrorDao.updateRecordStatus error!", e);
			}
			return false;
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
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
	
	public static boolean deleteRecord(long seqId) {
		PreparedStatement psmt = null;
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, true);
		Connection conn = session.getConnection();
		
		try {
			psmt = conn.prepareStatement(deleteRecordStatus);
			
			psmt.setLong(1, seqId);
			
			int row = psmt.executeUpdate();
			
			if(row <= 0){
				return false;
			}
		} catch (SQLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("RoleChargeErrorDao.deleteRecord error!", e);
			}
			return false;
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
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
	
	public void updateRetransferRecord(AppStoreExInfo info) {
		PreparedStatement psmt = null;
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, true);
		Connection conn = session.getConnection();
	
		try {
			psmt = conn.prepareStatement(updateRetransferRecord);
			
			psmt.setInt(1, info.getErrorCode());
			psmt.setString(2, info.getResultStr());
			psmt.setInt(3, info.getIsRetransfer());
			psmt.setDate(4, new Date(System.currentTimeMillis()));
			psmt.setString(5, info.getOperateUser());
			psmt.setLong(6, info.getId());
	
			psmt.executeUpdate();
		} catch (SQLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("RoleChargeErrorDao.updateRetransferRecord error!", e);
			}
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				session.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
