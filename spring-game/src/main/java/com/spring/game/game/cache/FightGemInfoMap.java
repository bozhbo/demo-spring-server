package com.snail.webgame.game.cache;

import com.snail.webgame.game.info.FightGemInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

/**
 * 宝石活动
 * @author zenggang
 */
public class FightGemInfoMap {

	public static void addFightGemInfo(FightGemInfo info) {
		int roleId = info.getRoleId();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		roleLoadInfo.setFightGemInfo(info);
	}

	public static FightGemInfo getFightGemInfo(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return null;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		return roleLoadInfo.getFightGemInfo();
	}
}
