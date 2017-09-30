package com.snail.client.web.robot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RobotRoleInfo {

	private int roleId;
	private int gateId;
	private int roomId;
	private String roleName;
	private int gold;
	private int vipLevel;
	private String header;
	
	private int state; // 1-进入房间成功 2-已经准备 3-可以操作 6-接收到游戏结束 7-等待开始
	
	public int opCount = 0; // 操作次数
	
	private Map<Integer, Integer> roomOtherRoleMap = new ConcurrentHashMap<>();
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getGateId() {
		return gateId;
	}
	public void setGateId(int gateId) {
		this.gateId = gateId;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Map<Integer, Integer> getRoomOtherRoleMap() {
		return roomOtherRoleMap;
	}
	public void setRoomOtherRoleMap(Map<Integer, Integer> roomOtherRoleMap) {
		this.roomOtherRoleMap = roomOtherRoleMap;
	}
	
}
