package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 记录副本相关的日志
 * @author zenggang
 */
public class InstanceLog extends BaseTO {

	private String serial;// 序列号
	private int roleId;//角色ID
	private String instanceId;// 副本实例的ID (fightId)
	private String instanceTypeId;// 副本id (fightType+"+"+defendStr)
	private Timestamp startTime;// 实例创建时间
	private Timestamp endTime;// 实例结束时间
	private int duration;// 实例持续时长 单位：秒
	private int troopCount1;// 副本创建时组队人数
	private int troopCount2;// 副本完成时组队人数

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceTypeId() {
		return instanceTypeId;
	}

	public void setInstanceTypeId(String instanceTypeId) {
		this.instanceTypeId = instanceTypeId;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTroopCount1() {
		return troopCount1;
	}

	public void setTroopCount1(int troopCount1) {
		this.troopCount1 = troopCount1;
	}

	public int getTroopCount2() {
		return troopCount2;
	}

	public void setTroopCount2(int troopCount2) {
		this.troopCount2 = troopCount2;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
