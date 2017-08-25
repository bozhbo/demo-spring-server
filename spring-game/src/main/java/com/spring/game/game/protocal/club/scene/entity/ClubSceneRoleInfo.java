package com.snail.webgame.game.protocal.club.scene.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubSceneRoleInfo extends MessageBody {
	private long roleId;
	private String roleName;
	private int heroNo;// 主武将编号
	private float pointX;
	private float pointY;
	private float pointZ;
	private String equipNos;// AVATER装备编号
	private int position; // 1 - 会长 2 - 副会长 3 - 官员
	private int showPlanId;// 1-显示套装  0-显示时装
    private int titleNo;// 称号编号 0 -没有

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("heroNo", 0);
		ps.add("pointX", 0);
		ps.add("pointY", 0);
		ps.add("pointZ", 0);
		ps.addString("equipNos", "flashCode", 0);
		ps.add("position", 0);
		ps.add("showPlanId", 0);
		ps.add("titleNo", 0);
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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

	public String getEquipNos() {
		return equipNos;
	}

	public void setEquipNos(String equipNos) {
		this.equipNos = equipNos;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getShowPlanId() {
		return showPlanId;
	}

	public void setShowPlanId(int showPlanId) {
		this.showPlanId = showPlanId;
	}

	public int getTitleNo() {
		return titleNo;
	}

	public void setTitleNo(int titleNo) {
		this.titleNo = titleNo;
	}
	
}
