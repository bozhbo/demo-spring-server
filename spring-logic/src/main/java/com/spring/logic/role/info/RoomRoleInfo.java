package com.spring.logic.role.info;

import com.spring.logic.gf.info.GoldFlowerInfo;
import com.spring.logic.role.enums.RoleCardState;
import com.spring.logic.role.enums.RoleRoomState;

public class RoomRoleInfo {

	private int roleId;
	private int gateId;
	private volatile int roomId;
	private String roleName;
	private int gold;
	private int vipLevel;
	private int online;
	private String header;
	
	private long lastStateTime;
	
	private RoleRoomState roleRoomState;
	
	private long startTime;	// 可以开始操作时间
	private GoldFlowerInfo goldFlowerInfo; // 当前牌
	private RoleCardState roleCardState;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getGateId() {
		return gateId;
	}
	public void setGateId(int gateId) {
		this.gateId = gateId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public RoleRoomState getRoleRoomState() {
		return roleRoomState;
	}
	public void setRoleRoomState(RoleRoomState roleRoomState) {
		this.roleRoomState = roleRoomState;
		this.lastStateTime = System.currentTimeMillis();
	}
	public long getLastStateTime() {
		return lastStateTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public void setLastStateTime(long lastStateTime) {
		this.lastStateTime = lastStateTime;
	}
	public GoldFlowerInfo getGoldFlowerInfo() {
		return goldFlowerInfo;
	}
	public void setGoldFlowerInfo(GoldFlowerInfo goldFlowerInfo) {
		this.goldFlowerInfo = goldFlowerInfo;
	}
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public RoleCardState getRoleCardState() {
		return roleCardState;
	}
	public void setRoleCardState(RoleCardState roleCardState) {
		this.roleCardState = roleCardState;
	}
	
}
