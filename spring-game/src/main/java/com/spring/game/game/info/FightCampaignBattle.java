package com.snail.webgame.game.info;

import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.HeroRecord;

/**
 * 宝物活动战斗关卡镜像信息
 * @author zenggang
 */
public class FightCampaignBattle extends BaseTO {

	public static final byte GET_PRIZE_STATUS_0 = 0;// 未领取
	public static final byte GET_PRIZE_STATUS_1 = 1;// 已领取

	private int roleId;// 用户编号
	private int battleNo;
	private int defendRoleId;// 关卡角色Id
	private String defendRoleName;// 关卡角色名称
	private byte isGetPrize;// 0-未领取 1-已领取
	// 关卡镜像信息<deployPos,HeroRecord>
	private Map<Byte, HeroRecord> fightDeployMap;
	private int fightValue;

	public int getBattleNo() {
		return battleNo;
	}

	public void setBattleNo(int battleNo) {
		this.battleNo = battleNo;
	}

	public String getDefendRoleName() {
		return defendRoleName;
	}

	public void setDefendRoleName(String defendRoleName) {
		this.defendRoleName = defendRoleName;
	}

	public byte getIsGetPrize() {
		return isGetPrize;
	}

	public void setIsGetPrize(byte isGetPrize) {
		this.isGetPrize = isGetPrize;
	}

	public Map<Byte, HeroRecord> getFightDeployMap() {
		return fightDeployMap;
	}

	public void setFightDeployMap(Map<Byte, HeroRecord> fightDeployMap) {
		this.fightDeployMap = fightDeployMap;
	}

	public byte getSaveMode() {
		return ONLINE;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getDefendRoleId() {
		return defendRoleId;
	}

	public void setDefendRoleId(int defendRoleId) {
		this.defendRoleId = defendRoleId;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
}
