package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 记录练兵场相关的日志
 * @author zhangyq
 */
public class ActivityLog extends BaseTO {

	private String account;
	private String roleName;
	private int heroNo;//主武将
	private int roleId;//角色ID
	private String activityTypeId;// 练兵场 No(10012 type=1,level=12)
	private Timestamp startTime;// 实例创建时间
	private Timestamp endTime;// 实例结束时间
	private String pos;// 布阵
	private String drop;// 掉落
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getActivityTypeId() {
		return activityTypeId;
	}
	public void setActivityTypeId(String activityTypeId) {
		this.activityTypeId = activityTypeId;
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
	public int getHeroNo() {
		return heroNo;
	}
	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}
	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
