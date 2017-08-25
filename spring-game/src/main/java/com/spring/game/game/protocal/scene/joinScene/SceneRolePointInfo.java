package com.snail.webgame.game.protocal.scene.joinScene;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 场景中角色AI
 * 
 * @author hongfm
 * 
 */
public class SceneRolePointInfo extends MessageBody {

	private long roleId;
	private String roleName;
	private int heroNo;// 主武将编号
	private float pointX;
	private float pointY;
	private float pointZ;
	private String equipNos;// AVATER装备编号
	private String clubName; // 公会名
	private int rideNo;// 当前上阵坐骑编号
	private int showPlanId;// 1-显示套装  0-显示时装
	private int titleNo; //角色当前的称号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("heroNo", 0);
		ps.add("pointX", 0);
		ps.add("pointY", 0);
		ps.add("pointZ", 0);
		ps.addString("equipNos", "flashCode", 0);
		ps.addString("clubName", "flashCode", 0);
		ps.add("rideNo", 0);
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

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public int getShowPlanId() {
		return showPlanId;
	}

	public void setShowPlanId(int showPlanId) {
		this.showPlanId = showPlanId;
	}

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}

	public int getTitleNo() {
		return titleNo;
	}

	public void setTitleNo(int titleNo) {
		this.titleNo = titleNo;
	}

}
