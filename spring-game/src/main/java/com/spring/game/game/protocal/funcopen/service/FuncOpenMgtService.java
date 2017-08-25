package com.snail.webgame.game.protocal.funcopen.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.xml.cache.FuncOpenXMLInfoMap;
import com.snail.webgame.game.xml.info.FuncOpenXMLInfo;

/**
 * 功能开启
 * 
 * @author nijp
 *
 */
public class FuncOpenMgtService {
	
	/**
	 * 检测是否有功能开启
	 * 
	 * @param roleInfo
	 * @param isNotify 是否通知客户端刷新
	 */
	public static void checkIsHasFuncOpen(RoleInfo roleInfo, boolean isNotify,SqlMapClient client) {
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return;
		}
		
		Map<Integer,Integer> funcOpenMap = new HashMap<Integer,Integer>();
		generateFuncOpenMap(roleLoadInfo, funcOpenMap);
		
		List<DropInfo> addList = new ArrayList<DropInfo>();
		StringBuilder sb = new StringBuilder();
		int rt = 0;
		for (FuncOpenXMLInfo xmlInfo : FuncOpenXMLInfoMap.getMap().values()) {
			// 是否已开启过了
			if (funcOpenMap.containsKey(xmlInfo.getNo())) {
				continue;
			}
			
			rt = AbstractConditionCheck.checkCondition(roleInfo, xmlInfo.getCheckConds());
			if (rt == 1) {
				// 符合开启条件
				sb.append(xmlInfo.getNo()).append(",");
				
				funcOpenMap.put(xmlInfo.getNo(), 1);
				
				// 奖励
				List<DropInfo> subList = FuncOpenXMLInfoMap.fetchPrizeByNo(xmlInfo.getNo());
				if (subList != null) {
					addList.addAll(subList);
				}
			}
			
		}
		
		if (sb.length() > 0) {
			if (isNotify) {
				// 通知功能开启
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_FUNCOPEN, sb.deleteCharAt(sb.length() - 1).toString());
			}
			
			// 生成新的功能编号字符串
			StringBuilder newFuncOpenStr = new StringBuilder();
			for (int funcNo : funcOpenMap.keySet()) {
				newFuncOpenStr.append(funcNo).append(",");
			}
			
			if (newFuncOpenStr.length() > 0) {
				newFuncOpenStr.deleteCharAt(newFuncOpenStr.length() - 1);
				
				// 存数据库并修改缓存
				if (RoleDAO.getInstance().updateRoleFuncOpenStr(roleInfo.getId(), newFuncOpenStr.toString(),client)) {
					
					roleLoadInfo.setFuncOpenStr(newFuncOpenStr.toString());
				}
				
			}
			
			// 增加奖励
			if (addList.size() > 0) 
			{
				ItemService.addPrize(ActionType.action20.getType(), roleInfo, addList,null,
						null,null,null,
						null,null,null,
						null,null,true);
			}
			
		}
	}
	
	private static void generateFuncOpenMap(RoleLoadInfo roleLoadInfo, Map<Integer,Integer> funcOpenMap) {
		if (roleLoadInfo.getFuncOpenStr() == null || roleLoadInfo.getFuncOpenStr().length() <= 0) {
			return;
		}
		
		String[] tempArr = roleLoadInfo.getFuncOpenStr().split(",");
		if (tempArr != null && tempArr.length > 0) {
			for (String str : tempArr) {
				if (!funcOpenMap.containsKey(Integer.valueOf(str))) {
					funcOpenMap.put(Integer.valueOf(str), 1);
				}
			}
		}
		
	}
}
