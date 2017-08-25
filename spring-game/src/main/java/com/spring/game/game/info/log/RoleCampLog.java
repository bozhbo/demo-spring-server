package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

/**
 * 攻城略地日志
 * @author hongfm
 *
 */
public class RoleCampLog{
	private int roleId;
	private String account;
	private String roleName;
	private int campNo;//第几关
	private Timestamp startTime;//开始时间
	private Timestamp endTime;//结束时间
	private int battleResult;
	private String prize;//itemNo-itemNum,itemNo-itemNum;
	private String coment;
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
	public int getCampNo() {
		return campNo;
	}
	public void setCampNo(int campNo) {
		this.campNo = campNo;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public int getBattleResult() {
		return battleResult;
	}
	public void setBattleResult(int battleResult) {
		this.battleResult = battleResult;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getComent() {
		return coment;
	}
	public void setComent(String coment) {
		this.coment = coment;
	}
	
	

}
