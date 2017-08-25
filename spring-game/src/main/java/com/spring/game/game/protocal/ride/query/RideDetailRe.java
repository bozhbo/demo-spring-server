package com.snail.webgame.game.protocal.ride.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RideDetailRe extends MessageBody {
	
	private int rideId;// 唯一id
	private int rideNo;// 对应配置表的编号
	private byte rideState;// 0-下阵 1-上阵
	private int rideLv;// 等级
	private int rideQua;// 品阶
	private int fightVal;// 当前战斗力
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rideId", 0);
		ps.add("rideNo", 0);
		ps.add("rideState", 0);
		ps.add("rideLv", 0);
		ps.add("rideQua", 0);
		ps.add("fightVal", 0);
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}

	public byte getRideState() {
		return rideState;
	}

	public void setRideState(byte rideState) {
		this.rideState = rideState;
	}

	public int getRideLv() {
		return rideLv;
	}

	public void setRideLv(int rideLv) {
		this.rideLv = rideLv;
	}

	public int getRideQua() {
		return rideQua;
	}

	public void setRideQua(int rideQua) {
		this.rideQua = rideQua;
	}

	public int getFightVal() {
		return fightVal;
	}

	public void setFightVal(int fightVal) {
		this.fightVal = fightVal;
	}

}
