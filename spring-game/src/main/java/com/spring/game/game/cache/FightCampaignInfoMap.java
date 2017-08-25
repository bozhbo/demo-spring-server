package com.snail.webgame.game.cache;

import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

/**
 * 宝物活动
 * @author zenggang
 */
public class FightCampaignInfoMap {

	public static void addFightCampaignInfo(FightCampaignInfo info) {
		int roleId = info.getRoleId();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		roleLoadInfo.setFightCampaignInfo(info);
	}

	public static FightCampaignInfo getFightCampaignInfo(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return null;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		return roleLoadInfo.getFightCampaignInfo();
	}
}
