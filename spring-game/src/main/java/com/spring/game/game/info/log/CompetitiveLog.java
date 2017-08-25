package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 
 * 类介绍:PVP日志记录类
 *
 * @author zhoubo
 * @2015年6月4日
 */
public class CompetitiveLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;
	private int heroNo;// 武将编号
	private Timestamp createTime;// 日志时间
	private int beforeStageValue; // 对战前积分
	private int afterStageValue; // 对战后积分
	private int beforeStage; // 对战前段位
	private int afterStage; // 对战后段位
	private int beforeRank;// 对战前排名
	private int afterRank;// 对战后排名
	private String getItem;// 对战获得道具
	private int useEnergy;// 挑战消耗精力
	private String matchRole;// 匹配对象
	private int targetHeroNo;// 目标武将编号
	private String comment;// 备注
	private byte battleResult;//战斗结果 1.负;2.胜.3.平手
	private String heroNos;

	public int getBeforeStageValue() {
		return beforeStageValue;
	}

	public void setBeforeStageValue(int beforeStageValue) {
		this.beforeStageValue = beforeStageValue;
	}

	public int getAfterStageValue() {
		return afterStageValue;
	}

	public void setAfterStageValue(int afterStageValue) {
		this.afterStageValue = afterStageValue;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getBeforeStage() {
		return beforeStage;
	}

	public void setBeforeStage(int beforeStage) {
		this.beforeStage = beforeStage;
	}

	public int getAfterStage() {
		return afterStage;
	}

	public void setAfterStage(int afterStage) {
		this.afterStage = afterStage;
	}

	public int getBeforeRank() {
		return beforeRank;
	}

	public void setBeforeRank(int beforeRank) {
		this.beforeRank = beforeRank;
	}

	public int getAfterRank() {
		return afterRank;
	}

	public void setAfterRank(int afterRank) {
		this.afterRank = afterRank;
	}

	public String getGetItem() {
		if(getItem == null || "".equals(getItem))
		{
			return "0";
		}
		return getItem;
	}

	public void setGetItem(String getItem) {
		this.getItem = getItem;
	}

	public int getUseEnergy() {
		return useEnergy;
	}

	public void setUseEnergy(int useEnergy) {
		this.useEnergy = useEnergy;
	}

	public String getMatchRole() {
		return matchRole;
	}

	public void setMatchRole(String matchRole) {
		this.matchRole = matchRole;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte getBattleResult() {
		return battleResult;
	}

	public void setBattleResult(byte battleResult) {
		this.battleResult = battleResult;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getTargetHeroNo() {
		return targetHeroNo;
	}

	public void setTargetHeroNo(int targetHeroNo) {
		this.targetHeroNo = targetHeroNo;
	}

	public String getHeroNos() {
		return heroNos;
	}

	public void setHeroNos(String heroNos) {
		this.heroNos = heroNos;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
