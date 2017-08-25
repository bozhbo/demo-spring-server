package com.snail.webgame.game.common;

import java.sql.Timestamp;

import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.scene.info.RolePoint;

public class RoleLogoutInfo {
	private int roleId;
	private int fightValue;
	private int sceneNo;//场景编号
	private float pointX;// 场景中坐标X
	private float pointY;// 场景中坐标X
	private float pointZ;// 场景中坐标X
	private Timestamp logoutime;

	public RoleLogoutInfo(RoleInfo roleInfo) {
		this.roleId = roleInfo.getId();
		this.fightValue = roleInfo.getFightValue();
		this.logoutime = new Timestamp(System.currentTimeMillis());
		RolePoint rolePoint = roleInfo.getRolePoint();
		if(rolePoint != null)
		{
			this.pointX = rolePoint.getPointX();
			this.pointY = rolePoint.getPointY();
			this.pointZ = rolePoint.getPointZ();
			this.sceneNo = rolePoint.getNo();
		}
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public Timestamp getLogoutime() {
		return logoutime;
	}

	public void setLogoutime(Timestamp logoutime) {
		this.logoutime = logoutime;
	}

	public int getSceneNo() {
		return sceneNo;
	}

	public void setSceneNo(int sceneNo) {
		this.sceneNo = sceneNo;
	}

	public float getPointX() {
		return pointX;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public float getPointY() {
		return pointY;
	}

	public void setPointY(float pointY) {
		this.pointY = pointY;
	}

	public float getPointZ() {
		return pointZ;
	}

	public void setPointZ(float pointZ) {
		this.pointZ = pointZ;
	}
	
}
