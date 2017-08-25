package com.snail.webgame.game.protocal.scene.outCity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 世界地图中玩家坐标
 * @author hongfm
 *
 */
public class MapRolePointRe extends MessageBody {

	private int roleId;
	private String roleName;
	private int mainHeroNo;
	private byte race;
	private float pointX;
	private float pointZ;
	private byte status;//状态0-普通状态  1-战斗中 2-新手状态 3-保护状态 4-押镖状态 5-押镖被拦截中 6-押镖战斗中(与本人战斗) 7-护镖中
	private int fightValue;// 战力
	private short protectPVPTime;// 大地图PVP失败保护倒计时(秒)
	private byte biaoCheType;// 镖车类型
	private String clubName;
	private int roleLv;
	private String equipNos;// AVATER装备编号
	private int biaocheOtherRoleId;// 护镖的时候，如果status=4，此字段标示护镖人，如果status=7，此字段表示押镖人
	private int rideNo;// 当前上阵坐骑编号
	private int showPlanId;//1-显示套装  0-显示时装
	private int titleNo; //当前使用的称号


	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("mainHeroNo", 0);
		ps.add("race", 0);
		ps.add("pointX", 0);
		ps.add("pointZ", 0);
		ps.add("status", 0);
		ps.add("fightValue", 0);
		ps.add("protectPVPTime", 0);
		ps.add("biaoCheType", 0);
		ps.addString("clubName", "flashCode", 0);
		ps.add("roleLv", 0);
		ps.addString("equipNos", "flashCode", 0);
		ps.add("biaocheOtherRoleId", 0);
		ps.add("rideNo", 0);
		ps.add("showPlanId", 0);
		ps.add("titleNo", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getMainHeroNo() {
		return mainHeroNo;
	}

	public void setMainHeroNo(int mainHeroNo) {
		this.mainHeroNo = mainHeroNo;
	}

	public byte getRace() {
		return race;
	}

	public void setRace(byte race) {
		this.race = race;
	}

	public float getPointX() {
		return pointX;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public float getPointZ() {
		return pointZ;
	}

	public void setPointZ(float pointZ) {
		this.pointZ = pointZ;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public short getProtectPVPTime() {
		return protectPVPTime;
	}

	public void setProtectPVPTime(short protectPVPTime) {
		this.protectPVPTime = protectPVPTime;
	}

	public byte getBiaoCheType() {
		return biaoCheType;
	}

	public void setBiaoCheType(byte biaoCheType) {
		this.biaoCheType = biaoCheType;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public int getRoleLv() {
		return roleLv;
	}

	public void setRoleLv(int roleLv) {
		this.roleLv = roleLv;
	}

	public String getEquipNos() {
		return equipNos;
	}

	public void setEquipNos(String equipNos) {
		this.equipNos = equipNos;
	}

	public int getBiaocheOtherRoleId() {
		return biaocheOtherRoleId;
	}

	public void setBiaocheOtherRoleId(int biaocheOtherRoleId) {
		this.biaocheOtherRoleId = biaocheOtherRoleId;
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
