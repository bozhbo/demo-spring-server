package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class RoleClubMemberInfo extends BaseTO {
	public static final int CLUB_BOSS = 1; // 公会会长
	public static final int CLUB_ASSISTANT = 2; // 副会长
	public static final int CLUB_LEADER = 3; // 官员
	public static final int CLUB_MEMBER = 0; // 普通会员
	public static final int CLUB_REQUEST_MEMBER = 99; // 申请会员

	private int roleId;
	private int clubId; // 公会ID
	private Timestamp joinTime; // 加入公会时间
	private int status; // 职位 0 -普通 1：会长 2 - 副会长 3 - 官员 99 - 申请加入公会的公会请求
	private float pointX;
	private float pointY;
	private float pointZ;
	private byte flag; // 0 - 不在场景中 1 - 在场景中 用于判断角色上下线后 再次进入的时候 是否在公会场景中
	private int sceneId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public Timestamp getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Timestamp joinTime) {
		this.joinTime = joinTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
