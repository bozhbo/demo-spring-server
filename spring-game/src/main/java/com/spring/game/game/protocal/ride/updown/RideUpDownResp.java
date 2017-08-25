package com.snail.webgame.game.protocal.ride.updown;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RideUpDownResp extends MessageBody {
	private int result;
	private int rideId;
	private byte state;
	
	private int oldRideId;// 旧的上阵坐骑id
	private byte oldRideState;// 旧的上阵坐骑当前状态
	
	private int heroId;
	private int fightVal;// 当前战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("rideId", 0);
		ps.add("state", 0);
		ps.add("oldRideId", 0);
		ps.add("oldRideState", 0);
		ps.add("heroId", 0);
		ps.add("fightVal", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public int getOldRideId() {
		return oldRideId;
	}

	public void setOldRideId(int oldRideId) {
		this.oldRideId = oldRideId;
	}

	public byte getOldRideState() {
		return oldRideState;
	}

	public void setOldRideState(byte oldRideState) {
		this.oldRideState = oldRideState;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getFightVal() {
		return fightVal;
	}

	public void setFightVal(int fightVal) {
		this.fightVal = fightVal;
	}
	
}
