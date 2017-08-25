package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.PresentEnergyInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.PresentEnergyInfo;
import com.snail.webgame.game.info.RoleInfo;

public class PresentEnergyDao extends SqlMapDaoSupport{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private PresentEnergyDao(){
		
	}
	
	private static class Inner{
		private static final PresentEnergyDao instance = new PresentEnergyDao();
	}
	
	public static PresentEnergyDao getInstence(){
		return Inner.instance;
	}
	
	/**
	 * 赠送精力入库
	 * @param roleId
	 * @param relRoleId
	 * @param presentTime
	 * @return
	 */
	public boolean insertPresentEnegerInfo(PresentEnergyInfo to){
		SqlSession session = null;
		
		try{
			session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
			if(session != null){
				if (session.insert("insertPresentEnergyInfo", to) != 1) {
					throw new Exception();
				}
				session.commit();
				session.close();
			}
		}catch (Exception e) {
			if(session != null)
			{
				session.rollback();
				session.close();
			}
			if (logger.isErrorEnabled()) {
				logger.error("PresentEnergyDao.insertPresentEnegerInfo error!", e);
			}
			
			return false;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("insertPresentEnergyInfo success");
		}
		
		return true;
		
	}
	
	/**
	 * 清理
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getAllPresentEnergy(){
		List<PresentEnergyInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectPresentEnergy");
		if(list != null && list.size() > 0){
			RoleInfo roleInfo = null;
			for(PresentEnergyInfo info : list){
				roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
				if(roleInfo == null){
					continue;
				}

				PresentEnergyInfoMap.addPresentEnergyInfo(roleInfo.getId(), info);
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("getAllRoleRelation getAllPresentEnergy");
		}
		
		return true;
		
	}
	
	/**
	 * 清理大于规定天数的赠送精力
	 * @return
	 */
	public boolean cleanupPresentEnergy(){
		PresentEnergyInfo to = new PresentEnergyInfo();
		
		long currentTime = System.currentTimeMillis();
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(currentTime);
		time.set(Calendar.MILLISECOND, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.DAY_OF_YEAR, time.get(Calendar.DAY_OF_YEAR) - (GameValue.AFTER_DAY_NUM_REMOVE_GET + 1));
		
		to.setPresentDate(new Timestamp(time.getTime().getTime()));
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deletePresentEnergyByTime", to);
			
			if(logger.isInfoEnabled()){
				logger.info("cleanup PresentEnergy");
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("PresentEnergyDao.cleanupPresentEnergy error!", e);
			}
		}

		
		return true;
		
	}
	
	/**
	 * 清理玩家大于规定天数的赠送精力
	 * @param roleId
	 * @return
	 */
	public boolean cleanupPresentEnergyByRoleId(int roleId,SqlMapClient client){
		PresentEnergyInfo to = new PresentEnergyInfo();
		to.setRoleId(roleId);
		
		long currentTime = System.currentTimeMillis();
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(currentTime);
		time.set(Calendar.MILLISECOND, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.DAY_OF_YEAR, time.get(Calendar.DAY_OF_YEAR) - (GameValue.AFTER_DAY_NUM_REMOVE_GET + 1));
		
		to.setPresentDate(new Timestamp(time.getTime().getTime()));
		
		try{
			client.delete("deletePresentEnergyByRoleId", to);
			
			if(logger.isInfoEnabled()){
				logger.info("cleanup PresentEnergy");
			}
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("PresentEnergyDao.cleanupPresentEnergyByRoleId error!", e);
			}
		}
		
		return true;
	}
	
	/**
	 * 删除赠送的精力
	 * @param id
	 * @return
	 */
	public boolean deletePresentEnergyById(int id){
		PresentEnergyInfo to = new PresentEnergyInfo();
		to.setId(id);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).delete("deletePresentEnergyById", to);
			
			if(logger.isInfoEnabled()){
				logger.info("deletePresentEnergyById success");
			}
			
			return true;
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				if(logger.isErrorEnabled()){
					logger.error("PresentEnergyDao.deletePresentEnergyById error!", e);
				}
			}
			
			return false;
		}
		
	}
	
	/**
	 * 赠送精力入库
	 * @param roleId
	 * @param relRoleId
	 * @param presentTime
	 * @return
	 */
	public boolean batchInsertPresentEnegerInfo(List<PresentEnergyInfo> list){
		SqlSession session = null;
		
		try{
			session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
			if(session != null){
				for(PresentEnergyInfo to : list){
					if (session.insert("insertPresentEnergyInfo", to) != 1) {
						throw new Exception();
					}
				}
				session.commit();
				session.close();
			}
		}catch (Exception e) {
			if(session != null)
			{
				session.rollback();
				session.close();
			}
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
			
			return false;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("batch insertPresentEnergyInfo success");
		}
		
		return true;
		
	}
	
	/**
	 * 删除赠送的精力
	 * @param id
	 * @return
	 */
	public boolean batchDeletePresentEnergyById(Set<Integer> set){
		if (set == null || set.size() <= 0) {
			return true;
		}
		
		PresentEnergyInfo to = null;
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			
			if(client != null){
				for(Integer id : set){
					to = new PresentEnergyInfo();
					to.setId(id);
					client.delete("deletePresentEnergyById", to);
				}
				client.commit();
				
				if(logger.isInfoEnabled()){
					logger.info("batchDeletePresentEnergyById success");
				}
				
				return true;
				
			}else{
				return false;
			}
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				if(logger.isErrorEnabled()){
					logger.error("batchDeletePresentEnergyById failure", e);
				}
			}
			
			return false;
		}
		
	}
	
}
