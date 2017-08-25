package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.xml.info.RideXMLInfo.LvUpInfo;

public class RideInfo extends BaseTO {
	
	private int roleId;// 所属角色id
	private int rideNo;// 编号
	private byte rideState;// 0-下阵 1-上阵

	private int rideLv;// 等级

	private int quality;// 品阶 1:白 2：绿 3：蓝 4：紫

	// 缓存
	private int fightVal;// 战斗力
	private int currHP;// 攻城略地要用
	private LvUpInfo lvUpInfo;// 坐骑属性
	private float rideRate;// 坐骑品质加成百分比
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public byte getRideState() {
		return rideState;
	}

	public void setRideState(byte rideState) {
		this.rideState = rideState;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getFightVal() {
		return fightVal;
	}

	public void setFightVal(int fightVal) {
		this.fightVal = fightVal;
	}

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}

	public int getRideLv() {
		return rideLv;
	}

	public void setRideLv(int rideLv) {
		this.rideLv = rideLv;
	}

	public int getCurrHP() {
		return currHP;
	}

	public void setCurrHP(int currHP) {
		this.currHP = currHP;
	}

	public LvUpInfo getLvUpInfo() {
		return lvUpInfo;
	}

	public void setLvUpInfo(LvUpInfo lvUpInfo) {
		this.lvUpInfo = lvUpInfo;
	}

	public float getRideRate() {
		return rideRate;
	}

	public void setRideRate(float rideRate) {
		this.rideRate = rideRate;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
