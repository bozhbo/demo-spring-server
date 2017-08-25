package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.info.ToolProgramInfo;
import com.snail.webgame.game.tool.handler.ToolServiceHandlerImpl;

public class ToolDAO extends SqlMapDaoSupport {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static ToolDAO toolDAO = new ToolDAO();
	
	public static ToolDAO getInstance(){
		
		return toolDAO;
	}
	
	private ToolDAO(){
		
	}

	/**
	 * 载入运营活动
	 * @param sqlMapClient
	 * @return 返回值
	 */
	public boolean loadToolProgramInfo(){
		try {
			@SuppressWarnings("unchecked")
			List<ToolProgramInfo> list = getSqlMapClient("GAME_DB").queryList("isExistToolProgrom");
			for (ToolProgramInfo toolProgramInfo : list) {
				ToolServiceHandlerImpl.XMLToList(toolProgramInfo.getXml());
			}
			if(logger.isInfoEnabled()){
				logger.info("Load GAME_TOOL_PROGRAM Table success!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("load role full info GAME_TOOL_PROGRAM failed!");
			return false;
		}
		return true;
		
	}
	
	/**
	 * 判断是否已存在该运营活动
	 * @param programId 活动编号
	 * @return 返回值
	 */
	public boolean isExistToolProgrom(String programId){
		
		boolean result = false;
		ToolProgramInfo toolProgramInfo = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("sid", programId);
		try {
			toolProgramInfo = (ToolProgramInfo)getSqlMapClient("GAME_DB").query("isExistToolProgrom", params);
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("isExistToolProgrom error!",e);
			}
		}
		if(toolProgramInfo!=null){
			result = true;
		}
		
		return result;
		
	}
	
	/**
	 * 添加运营活动（双倍经验，双倍掉落）
	 * @param programId 活动编号（event001-双倍经验，event002-双倍掉落）
	 * @param person 操作人
	 * @param xml
	 * @return
	 */
	public boolean insertToolProgram(String programId,String person,String xml){
		
		boolean result = false;
		
		ToolProgramInfo toolProgramInfo = new ToolProgramInfo();
		toolProgramInfo.setSid(programId);
		toolProgramInfo.setPerson(person);
		toolProgramInfo.setXml(xml);
		toolProgramInfo.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		try {
			result = getSqlMapClient("GAME_DB").insert("insertToolProgram", toolProgramInfo);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("insertToolProgram error!",e);
			}
		}
		
		return result;
	}
	
	/**
	 * 修改运营活动（双倍经验、双倍掉落）
	 * @param programId 活动编号（event001-双倍经验，event002-双倍掉落）
	 * @param person 操作人
	 * @param xml
	 * @return 返回值
	 */
	public boolean updateToolProgram(String programId,String person,String xml){
		
		boolean result = false;
		
		ToolProgramInfo toolProgramInfo = new ToolProgramInfo();
		toolProgramInfo.setSid(programId);
		toolProgramInfo.setPerson(person);
		toolProgramInfo.setXml(xml);
		toolProgramInfo.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		try {
			result = getSqlMapClient("GAME_DB").update("updateToolProgram", toolProgramInfo);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateToolProgram error!",e);
			}
		}
		
		return result;
		
	}
	
	

}
