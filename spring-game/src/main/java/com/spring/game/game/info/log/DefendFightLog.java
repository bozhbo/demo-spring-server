package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;
/**
 * 防守玩法日志记录
 * @author nijy
 *
 */
public class DefendFightLog extends BaseTO {
	
	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;//角色ID
	private int mainHeroId;//主武将ID
	private int fightLv;//战斗难度编号
	private Timestamp beginTime;//战斗开始时间
	private Timestamp endTime;//战斗结束时间
	private String pos;//阵型
	private String drop;//掉落
	private int battleResult;//战斗结果
	
	
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


	public Timestamp getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}


	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public int getMainHeroId() {
		return mainHeroId;
	}


	public void setMainHeroId(int mainHeroId) {
		this.mainHeroId = mainHeroId;
	}


	public int getFightLv() {
		return fightLv;
	}


	public void setFightLv(int fightLv) {
		this.fightLv = fightLv;
	}


	public String getPos() {
		return pos;
	}


	public void setPos(String pos) {
		this.pos = pos;
	}


	public String getDrop() {
		return drop;
	}


	public void setDrop(String drop) {
		this.drop = drop;
	}


	public int getBattleResult() {
		return battleResult;
	}


	public void setBattleResult(int battleResult) {
		this.battleResult = battleResult;
	}


	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
