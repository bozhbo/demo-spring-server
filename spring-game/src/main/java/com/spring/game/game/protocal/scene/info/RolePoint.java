package com.snail.webgame.game.protocal.scene.info;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 角色在场景中的位置
 * @author hongfm
 *
 */
public class RolePoint extends BaseTO{
	private int no;
	private int roleId;
	private int sceneId;
	private float pointX;
	private float pointY;
	private float pointZ;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getSceneId() {
		return sceneId;
	}
	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
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
	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
