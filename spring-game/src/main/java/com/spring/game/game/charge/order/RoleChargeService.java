package com.snail.webgame.game.charge.order;

import java.sql.Timestamp;

import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.cache.ToolBoxMap;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleChargeDAO;
import com.snail.webgame.game.info.RoleChargeInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.ToolBoxInfo;
import com.snail.webgame.game.info.log.RoleChargeLog;
import com.snail.webgame.game.protocal.app.common.EChargeState;

public class RoleChargeService {
	
	/**
	 * 保存充值订单信息
	 * 
	 * @param roleInfo
	 * @param orderStr
	 * @param itemId 商品编号 对应充值xml的No
	 * @param seqId 此字段给苹果缓存用的，安卓此参数传0
	 * @param state 
	 */
	public static void saveChargeOrderInfo(RoleInfo roleInfo ,String orderStr, int itemId, long seqId, EChargeState state) {
		// 保存订单号信息
		RoleChargeInfo info = new RoleChargeInfo();
		info.setOrderStr(orderStr);
		info.setItemId(itemId);
		info.setRoleId(roleInfo.getId());
		info.setAccId(roleInfo.getAccountId());
		info.setChargeTime(new Timestamp(System.currentTimeMillis()));
		info.setSeqId(seqId);
		info.setState(state);
		
		ToolBoxInfo toolBoxInfo = ToolBoxMap.fetchBoxInfoById(ToolBoxInfo.TYPE_BOX_CHARGE, info.getItemId());
		if (toolBoxInfo != null) {
			// 购买充值宝箱时使用
			info.setItemStr(toolBoxInfo.getItemStr());
		}
		
		if (RoleChargeMap.addRoleChargeInfo(info)) {
			// 
			RoleChargeDAO.getInstance().insertRoleChargeInfo(info);
		}
		
		GameLogService.insertRoleChargeLog(roleInfo.getAccount(), roleInfo.getRoleName(), orderStr, itemId, RoleChargeLog.ROLE_CHARGE_EVENT_1);
	}
}
